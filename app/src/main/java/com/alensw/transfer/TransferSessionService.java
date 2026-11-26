package com.alensw.transfer;

import android.app.*;
import android.content.*;
import android.net.*;
import android.net.wifi.WifiManager;
import android.os.*;
import android.preference.PreferenceManager;
import com.alensw.PicFolder.R;
import org.openqpic.localsend.LocalSend;
import org.openqpic.localsend.SystemTls;
import java.io.IOException;
import java.net.*;
import java.util.*;

/** Started while the UI is visible, bound while it is resumed, foreground while it has jobs.
 * All Android state is main-thread confined. Each endpoint keeps its original address for its jobs. */
public final class TransferSessionService extends Service {
    interface Client {
        void peersChanged();
        void rowsChanged();
        void promptsChanged();
        void problem(String message);
    }
    final class Link extends Binder { TransferSessionService service() { return TransferSessionService.this; } }
    static final class Row {
        final String key; String label; int percent; boolean finished;
        Row(String key) { this.key=key; }
    }
    static final class Offer {
        final String key, folder;
        final LocalSend.Peer peer;
        final List<LocalSend.Item> files;
        final Set<String> selected=new LinkedHashSet<>();
        final int skipped;
        Offer(String key,LocalSend.Peer p,List<LocalSend.Item> files,int skipped,String folder) {
            this.key=key; peer=p; this.files=files; this.skipped=skipped; this.folder=folder;
            for(LocalSend.Item f:files) selected.add(f.id);
        }
    }
    static final String VIEW="com.alensw.transfer.VIEW_SESSION";
    static final String TLS_MODE="localsend.tls";
    private static final String CANCEL="com.alensw.transfer.CANCEL_SESSION", CHANNEL="localsend-transfers";
    private static final int NOTICE=0x4c53;
    private final Link binder=new Link();
    private final Handler main=new Handler(Looper.getMainLooper());
    // Keep recent results when an idle service stops before the user reopens the list.
    // These rows contain no Context, Uri grants, FDs or endpoint references.
    static final List<Row> history=new ArrayList<>();
    final Map<String,Offer> offers=new LinkedHashMap<>();
    final Map<String,LocalSend.Peer> pins=new LinkedHashMap<>();
    final NearbyDevices nearby=new NearbyDevices();
    private final List<Lane> lanes=new ArrayList<>();
    private TransferFiles storage; private SharedPreferences prefs;
    private Client client; private Lane discovery;
    private WifiManager.MulticastLock multicast;
    private PowerManager.WakeLock cpu; private WifiManager.WifiLock wifiTransfer;
    private boolean watching,foreground,destroyed;
    private boolean preparingIdentity;
    private SystemTls tlsIdentity;
    private final Runnable refreshNotice=this::updateNotice;
    private final Runnable tick=new Runnable() {
        public void run() {
            if(client==null) return;
            if(discovery!=null && nearby.maintain(discovery.endpoint)) client.peersChanged();
            main.postDelayed(this,10000);
        }
    };
    private final BroadcastReceiver network=new BroadcastReceiver() {
        @Override public void onReceive(Context c,Intent i) { if(client!=null) refreshNetwork(); }
    };
    private final class Lane implements LocalSend.Events {
        final String ip;
        final SystemTls identity;
        LocalSend endpoint;
        Lane(String ip,SystemTls identity) { this.ip=ip; this.identity=identity; }
        private void post(Runnable r) { main.post(()->{ if(!destroyed) r.run(); }); }
        public void peer(LocalSend.Peer p) { post(()->{
            if(this==discovery && client!=null && nearby.seen(p)) client.peersChanged();
        }); }
        public void problem(String msg) { post(()->{ if(client!=null && this==discovery) client.problem(msg); }); }
        public String receiveType(LocalSend.Item item) { return TransferMedia.offered(item); }
        public LocalSend.Sink destination(LocalSend.Item item,LocalSend.Cancellation cancellation) throws IOException {
            return storage.destination(item,cancellation);
        }
        public void offer(String key,LocalSend.Peer p,List<LocalSend.Item> files,int skipped) { post(()->{
            if(!endpoint.hasSession(key)) return;
            offers.put(key,new Offer(key,p,files,skipped,storage.folder()));
            record(key,getString(R.string.ls_offer_from,p.alias),0,false);
            settle(); if(client!=null) client.promptsChanged();
        }); }
        public void needPin(String key,LocalSend.Peer p) { post(()->{
            if(!endpoint.hasSession(key)) return;
            pins.put(key,p); record(key,p.alias+" · PIN",0,false);
            settle(); if(client!=null) client.promptsChanged();
        }); }
        public void update(String key,String label,int percent,boolean finished) { post(()->{
            if(!finished && !endpoint.hasSession(key)) return;
            boolean prompt=finished && (offers.remove(key)!=null | pins.remove(key)!=null);
            record(key,label,percent,finished); settle();
            if(finished && client!=null) client.peersChanged();
            if(prompt && client!=null) client.promptsChanged();
        }); }
    }
    @Override public void onCreate() {
        super.onCreate(); storage=new TransferFiles(this); prefs=PreferenceManager.getDefaultSharedPreferences(this);
        if(!prefs.contains("localsend.id")) prefs.edit().putString("localsend.id",UUID.randomUUID().toString()).apply();
        PowerManager power=(PowerManager)getSystemService(POWER_SERVICE);
        cpu=power.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"OpenQPic:transfer"); cpu.setReferenceCounted(false);
        WifiManager wifi=(WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        if(wifi!=null) {
            multicast=wifi.createMulticastLock("OpenQPic:discovery"); multicast.setReferenceCounted(false);
            wifiTransfer=wifi.createWifiLock(WifiManager.WIFI_MODE_FULL_HIGH_PERF,"OpenQPic:transfer"); wifiTransfer.setReferenceCounted(false);
        }
        if(Build.VERSION.SDK_INT>=26) {
            NotificationManager nm=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(new NotificationChannel(CHANNEL,getString(R.string.transfer),NotificationManager.IMPORTANCE_LOW));
        }
    }
    @Override public IBinder onBind(Intent i) { return binder; }
    @Override public int onStartCommand(Intent i,int flags,int id) {
        if(i!=null && CANCEL.equals(i.getAction()) && i.getData()!=null) cancel(i.getData().getSchemeSpecificPart());
        settle(); return START_NOT_STICKY;
    }
    void enter(Client c) {
        client=c;
        startService(new Intent(this,TransferSessionService.class));
        if(!watching) {
            watching=true; registerReceiver(network,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            main.postDelayed(tick,10000);
        }
        refreshNetwork(); c.peersChanged(); c.rowsChanged(); c.promptsChanged();
    }
    void leave(Client c) {
        if(client!=c) return;
        client=null; unwatch(); hideDiscovery(); settle();
    }
    private void unwatch() {
        if(watching) { unregisterReceiver(network); watching=false; }
        main.removeCallbacks(tick);
    }
    private void hideDiscovery() {
        if(discovery!=null) { discovery.endpoint.hide(); discovery=null; }
        if(multicast!=null && multicast.isHeld()) multicast.release();
        nearby.rows.clear();
    }
    String address() { return discovery==null?null:(discovery.endpoint.encrypted()?"https://":"http://")+discovery.ip+":"+discovery.endpoint.port(); }
    private String lanAddress() {
        ConnectivityManager cm=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        for(Network net:cm.getAllNetworks()) {
            NetworkCapabilities caps=cm.getNetworkCapabilities(net); LinkProperties props=cm.getLinkProperties(net);
            if(props==null || caps==null || !(caps.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    caps.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))) continue;
            for(LinkAddress a:props.getLinkAddresses()) if(a.getAddress() instanceof Inet4Address) return a.getAddress().getHostAddress();
        }
        try { for(NetworkInterface n:Collections.list(NetworkInterface.getNetworkInterfaces())) {
            if(!n.isUp() || !n.getName().matches("(wlan|wifi|ap|swlan|eth|en)[A-Za-z0-9_.-]*")) continue;
            for(InetAddress a:Collections.list(n.getInetAddresses()))
                if(a instanceof Inet4Address && !a.isLoopbackAddress()) return a.getHostAddress();
        } } catch(SocketException ignored) {}
        return null;
    }
    void refreshNetwork() { refreshNetwork(false); }
    private void refreshNetwork(boolean allowHttpWithoutIdentity) {
        if(client==null) return;
        String ip=lanAddress(), name=prefs.getString("transfer_name",Build.MODEL), pin=prefs.getString("localsend.pin","");
        boolean encrypted=prefs.getBoolean(TLS_MODE,true);
        if(discovery!=null && discovery.ip.equals(ip) && discovery.identity==tlsIdentity &&
                discovery.endpoint.encrypted()==encrypted && discovery.endpoint.isOpen()) {
            discovery.endpoint.show(name,pin); return;
        }
        hideDiscovery();
        if(ip!=null && tlsIdentity==null && (encrypted || !allowHttpWithoutIdentity)) {
            if(!preparingIdentity) {
                preparingIdentity=true;
                new Thread(()->{
                    try {
                        SystemTls ready=SystemTls.transfers();
                        main.post(()->{ preparingIdentity=false; tlsIdentity=ready; if(client!=null && !destroyed) refreshNetwork(); });
                    } catch(IOException | RuntimeException e) {
                        android.util.Log.w("LocalSend","Cannot prepare TLS identity",e);
                        main.post(()->{
                            preparingIdentity=false;
                            if(client==null || destroyed) return;
                            if(prefs.getBoolean(TLS_MODE,true)) client.problem(e.getMessage());
                            else refreshNetwork(true);
                        });
                    }
                },"QP LocalSend identity").start();
            }
            return;
        }
        if(ip!=null) try {
            if(multicast!=null) multicast.acquire();
            for(Lane l:lanes) if(l.ip.equals(ip) && l.identity==tlsIdentity) synchronized(l.endpoint) {
                if(l.endpoint.isOpen() && l.endpoint.encrypted()==encrypted) { l.endpoint.show(name,pin); discovery=l; break; }
            }
            if(discovery==null) {
                Lane l=new Lane(ip,tlsIdentity);
                l.endpoint=LocalSend.start(ip,name,prefs.getString("localsend.id",""),Build.MODEL,pin,l,
                        tlsIdentity,encrypted);
                lanes.add(l); discovery=l;
            }
        } catch(IOException | RuntimeException ex) {
            android.util.Log.w("LocalSend","Cannot open transfer listener",ex);
            hideDiscovery(); client.problem(ex.getMessage());
        }
        client.peersChanged(); settle();
    }
    void probe(String ip,int port,String protocol) {
        if(discovery==null) return;
        boolean tls=protocol==null?discovery.endpoint.encrypted():"https".equals(protocol);
        discovery.endpoint.probe(ip,port,true,tls);
    }
    boolean sendingTo(LocalSend.Peer peer) {
        for(Lane l:lanes) if(l.endpoint.sendingTo(peer)) return true;
        return false;
    }
    boolean send(List<LocalSend.Peer> peers,List<Uri> selected) {
        if(discovery==null || selected.isEmpty()) {
            if(client!=null) client.problem(getString(discovery==null?R.string.ls_offline:R.string.ls_pick));
            return false;
        }
        for(LocalSend.Peer p:peers) if(sendingTo(p)) {
            if(client!=null) client.problem(p.alias+" · "+getString(R.string.ls_sending_to));
            return false;
        }
        ArrayList<Uri> uris=new ArrayList<>(selected);
        // The service needs the temporary read grants even if the sharing Activity is finished.
        Intent hold=new Intent(this,TransferSessionService.class);
        ClipData grants=null;
        for(Uri u:uris) if("content".equals(u.getScheme())) {
            if(grants==null) grants=ClipData.newRawUri("LocalSend",u); else grants.addItem(new ClipData.Item(u));
        }
        if(grants!=null) { hold.setClipData(grants); hold.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); }
        try { startService(hold); }
        catch(SecurityException denied) {
            if(client!=null) client.problem(getString(R.string.ls_no_grant));
            return false;
        }
        boolean started=discovery.endpoint.send(peers,uris.size(),(i,cancel)->storage.source(uris.get(i),cancel));
        if(started) { settle(); if(client!=null) client.peersChanged(); }
        return started;
    }
    void answer(String key,boolean accept) {
        Offer offer=offers.remove(key); if(offer==null) return;
        for(Lane l:lanes) l.endpoint.answer(key,accept?offer.selected:null);
        if(client!=null) client.promptsChanged(); settle();
    }
    void cancel(String key) {
        if(key==null) return;
        for(Lane l:lanes) l.endpoint.cancel(key);
    }
    void pin(String key,String value) {
        pins.remove(key); for(Lane l:lanes) l.endpoint.enterPin(key,value);
        if(client!=null) client.promptsChanged();
    }
    private void record(String key,String label,int percent,boolean finished) {
        Row row=null; for(Row r:history) if(r.key.equals(key)) { row=r; break; }
        if(row==null) {
            row=new Row(key); history.add(0,row);
            for(int i=history.size()-1;i>=0 && history.size()>100;i--) if(history.get(i).finished) history.remove(i);
        }
        row.label=label; row.percent=percent; row.finished=finished;
        if(client!=null) client.rowsChanged();
    }
    private boolean hasWork() {
        for(Lane l:lanes) if(l.endpoint.hasWork()) return true;
        return false;
    }
    private void holdPower(boolean hold) {
        if(hold && !cpu.isHeld()) cpu.acquire();
        else if(!hold && cpu.isHeld()) cpu.release();
        if(wifiTransfer!=null) {
            if(hold && !wifiTransfer.isHeld()) wifiTransfer.acquire();
            else if(!hold && wifiTransfer.isHeld()) wifiTransfer.release();
        }
    }
    private void settle() {
        for(Iterator<Lane> it=lanes.iterator();it.hasNext();) {
            Lane l=it.next(); if(l!=discovery && !l.endpoint.isOpen()) it.remove();
        }
        boolean work=hasWork();
        if(work) {
            if(!foreground) updateNotice();
            else if(!noticeQueued) { noticeQueued=true; main.postDelayed(refreshNotice,400); }
        } else {
            main.removeCallbacks(refreshNotice); noticeQueued=false; holdPower(false);
            if(foreground) { stopForeground(true); foreground=false; }
            // A terminal event is posted from the worker. Do not discard its history row.
            if(client==null) stopSelf();
        }
    }
    private boolean noticeQueued;
    private void updateNotice() {
        noticeQueued=false; if(!hasWork()) return;
        Row first=null; int count=0; boolean waitingOnly=true;
        for(Row r:history) if(!r.finished) {
            if(first==null) first=r; count++;
            if(!offers.containsKey(r.key) && !pins.containsKey(r.key)) waitingOnly=false;
        }
        holdPower(!waitingOnly || count==0);
        Intent view=new Intent(this,TransferActivity.class).setAction(VIEW)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int flags=PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_IMMUTABLE;
        Notification.Builder b=Build.VERSION.SDK_INT>=26?new Notification.Builder(this,CHANNEL):new Notification.Builder(this);
        b.setSmallIcon(android.R.drawable.stat_sys_upload).setContentTitle(getString(R.string.transfer))
                .setContentText(count>1?getString(R.string.ls_running,count):first!=null?first.label:getString(R.string.ls_preparing))
                .setContentIntent(PendingIntent.getActivity(this,0,view,flags))
                .setOngoing(true).setOnlyAlertOnce(true).setCategory(Notification.CATEGORY_PROGRESS);
        if(first!=null && count==1) {
            Intent stop=new Intent(this,TransferSessionService.class).setAction(CANCEL).setData(Uri.fromParts("session",first.key,null));
            b.addAction(android.R.drawable.ic_menu_close_clear_cancel,getString(android.R.string.cancel),PendingIntent.getService(this,0,stop,flags));
            if(!waitingOnly) b.setProgress(100,first.percent,false);
        }
        startForeground(NOTICE,b.build()); foreground=true;
    }
    @Override public void onDestroy() {
        destroyed=true; client=null; unwatch(); hideDiscovery(); main.removeCallbacksAndMessages(null);
        holdPower(false); if(foreground) stopForeground(true);
        super.onDestroy();
    }
}
