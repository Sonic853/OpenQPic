package com.alensw.transfer;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.net.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.view.*;
import android.widget.*;
import android.support.v4.a.p;
import com.alensw.PicFolder.GalleryActivity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.bean.UriFile;
import com.alensw.ui.activity.ao;
import com.alensw.ui.view.ImageGridView;
import org.openqpic.localsend.LocalSend;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import static com.alensw.PicFolder.R.*;

/** The original gallery, system share target and launcher shortcut all enter here. */
public final class TransferActivity extends ao implements TransferSessionService.Client {
    private final ArrayList<Uri> selection=new ArrayList<>();
    private final Map<Uri,CommonFile> thumbs=new HashMap<>();
    private final ExecutorService metadata=Executors.newSingleThreadExecutor();
    private final ArrayList<NearbyDevices.Entry> devices=new ArrayList<>();
    private final ArrayList<TransferSessionService.Row> rows=new ArrayList<>();
    private final Handler main=new Handler(Looper.getMainLooper());
    private final Set<String> deferred=new HashSet<>();
    private final View[] pages=new View[2];
    private final TextView[] tabs=new TextView[2];
    private View send; private p pager; private TextView tip, count; private ImageGridView grid;
    private Devices deviceAdapter; private History historyAdapter;
    private FrameLayout root; private View tabBar; private TextView nearby;
    private ReceivePage receive; private AlertDialog pinDialog; private String pinKey;
    private SharedPreferences prefs;
    private TransferSessionService service; private boolean bound,resumed;
    private static final int PICK=1, CONNECT=0x4c5301, PIN=0x4c5302;
    private final ServiceConnection connection=new ServiceConnection() {
        public void onServiceConnected(ComponentName n,IBinder value) {
            if(!resumed || !bound) return;
            service=((TransferSessionService.Link)value).service(); service.enter(TransferActivity.this);
        }
        public void onServiceDisconnected(ComponentName n) { service=null; peersChanged(); dismissPin(); hideReceive(); }
    };
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        prefs=PreferenceManager.getDefaultSharedPreferences(this);
        root=new FrameLayout(this); pager=new p(this); root.addView(pager); setContentView(root); g(true);
        pages[0]=send=getLayoutInflater().inflate(layout.transfer_send,pager,false);
        pages[1]=getLayoutInflater().inflate(layout.list_view,pager,false);
        tip=send.findViewById(id.empty_tip); count=send.findViewById(id.files_to_send);
        nearby=send.findViewById(id.ls_nearby);
        tip.setOnClickListener(v->manual()); send.findViewById(id.ls_connect).setOnClickListener(v->manual());
        ListView peers=send.findViewById(id.devices), history=pages[1].findViewById(id.list);
        TextView empty=pages[1].findViewById(id.empty); empty.setText(string.transfer_list_empty);
        peers.setEmptyView(send.findViewById(id.empty)); history.setEmptyView(empty);
        peers.setAdapter(deviceAdapter=new Devices()); history.setAdapter(historyAdapter=new History());
        peers.setOnItemClickListener((parent,v,pos,n)->sendTo(devices.get(pos).peer));
        history.setOnItemClickListener((parent,v,pos,n)->{
            String key=rows.get(pos).key; deferred.remove(key); promptsChanged();
        });
        grid=send.findViewById(id.grid); grid.setTextMode(1);
        grid.a(new Thumbnails(),QuickApp.r.d,QuickApp.r.e);
        icon(send.findViewById(id.add),raw.add_2);
        icon(send.findViewById(id.clear),raw.delete_2);
        send.findViewById(id.add).setContentDescription(getString(string.files_to_send));
        send.findViewById(id.add).setOnClickListener(v->pick());
        send.findViewById(id.clear).setOnClickListener(v->{ selection.clear(); thumbs.clear(); selectionChanged(); });
        pager.setOnPageChangeListener(new android.support.v4.a.x() {
            public void a(int n) { if(J!=null) J.setSelectedNavigationItem(n); else select(n); }
            public void a(int n,float f,int px) {} public void b(int n) {}
        });
        pager.setAdapter(new android.support.v4.a.j() {
            public int a() { return pages.length; }
            public Object a(ViewGroup parent,int n) { parent.addView(pages[n]); return pages[n]; }
            public void a(ViewGroup parent,int n,Object page) { parent.removeView((View)page); }
            public boolean a(View view,Object page) { return view==page; }
        });
        if(J!=null) {
            J.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            ActionBar.TabListener listener=new ActionBar.TabListener() {
                public void onTabSelected(ActionBar.Tab t,FragmentTransaction ft) { pager.setCurrentItem(t.getPosition()); }
                public void onTabUnselected(ActionBar.Tab t,FragmentTransaction ft) {}
                public void onTabReselected(ActionBar.Tab t,FragmentTransaction ft) {}
            };
            for(int title:new int[]{string.send,string.transfer_list}) J.addTab(J.newTab().setText(title).setTabListener(listener));
        } else if(K!=null) {
            View bar=tabBar=getLayoutInflater().inflate(layout.tab_two_items,null);
            tabs[0]=bar.findViewById(id.first_item); tabs[1]=bar.findViewById(id.second_item);
            for(int i=0;i<2;i++) {
                final int n=i; tabs[i].setText(i==0?string.send:string.transfer_list);
                tabs[i].setTextColor(K.getTitleColor()); tabs[i].setBackgroundDrawable(com.alensw.ui.d.d.a(K.getTitleColor()));
                tabs[i].setOnClickListener(v->pager.setCurrentItem(n));
            }
            select(0); K.a(bar,new LinearLayout.LayoutParams(-2,-1));
        }
        orient(getResources().getConfiguration());
        if(state!=null && state.containsKey("deferred")) deferred.addAll(state.getStringArrayList("deferred"));
        if(state!=null && state.containsKey("uris")) add(state.getParcelableArrayList("uris"));
        else collect(getIntent());
        selectionChanged(); peersChanged(); a((CharSequence)alias());
        if(TransferSessionService.VIEW.equals(getIntent().getAction()) || state!=null && state.getInt("page")==1) pager.setCurrentItem(1);
    }
    private String alias() { return prefs.getString("transfer_name",Build.MODEL); }
    private void select(int index) { for(int i=0;i<2;i++) if(tabs[i]!=null) tabs[i].setSelected(index==i); }
    private void icon(ImageView v,int resource) {
        v.setImageDrawable(com.b.a.b.a(getResources(),resource,ao.b(this,attr.colorMenuDrawable),ao.b(this)));
    }
    private void orient(Configuration c) {
        LinearLayout l=(LinearLayout)send; boolean wide=c.orientation==Configuration.ORIENTATION_LANDSCAPE;
        l.setOrientation(wide?LinearLayout.HORIZONTAL:LinearLayout.VERTICAL);
        for(int i=0;i<l.getChildCount();i++) {
            View child=l.getChildAt(i); LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)child.getLayoutParams();
            lp.width=wide?0:-1; lp.height=wide?-1:0; child.setLayoutParams(lp);
        }
    }
    @Override public void onConfigurationChanged(Configuration c) { super.onConfigurationChanged(c); orient(c); }
    @Override protected void onSaveInstanceState(Bundle b) {
        super.onSaveInstanceState(b); b.putParcelableArrayList("uris",selection);
        b.putStringArrayList("deferred",new ArrayList<>(deferred)); b.putInt("page",pager.getCurrentItem());
    }
    @Override protected void onNewIntent(Intent i) {
        super.onNewIntent(i); setIntent(i);
        if(TransferSessionService.VIEW.equals(i.getAction())) { deferred.clear(); pager.setCurrentItem(1); promptsChanged(); }
        else collect(i);
    }
    @Override protected void onActivityResult(int code,int result,Intent i) {
        super.onActivityResult(code,result,i); if(code==PICK && result==RESULT_OK) collect(i);
    }
    private void collect(Intent i) {
        if(i==null) return;
        LinkedHashSet<Uri> found=new LinkedHashSet<>();
        if(Intent.ACTION_SEND_MULTIPLE.equals(i.getAction())) {
            ArrayList<?> list=i.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
            if(list!=null) for(Object u:list) if(u instanceof Uri) found.add((Uri)u);
        } else if(Intent.ACTION_SEND.equals(i.getAction())) {
            Object u=i.getParcelableExtra(Intent.EXTRA_STREAM); if(u instanceof Uri) found.add((Uri)u);
        }
        ClipData data=i.getClipData();
        if(data!=null) for(int n=0;n<data.getItemCount() && found.size()<=LocalSend.MAX_FILES;n++) found.add(data.getItemAt(n).getUri());
        add(found);
    }
    private void add(Collection<Uri> uris) {
        if(uris==null) return;
        for(Uri uri:uris) {
            if(!TransferFiles.allowed(uri) || selection.contains(uri)) continue;
            if(selection.size()>=LocalSend.MAX_FILES) { toast(getString(string.ls_limit)); break; }
            selection.add(uri);
            metadata.execute(()->{
                try { CommonFile f=UriFile.a(uri); main.post(()->{
                    if(!isDestroyed() && selection.contains(uri)) { thumbs.put(uri,f); grid.requestLayout(); grid.invalidate(); }
                }); } catch(RuntimeException ignored) {}
            });
        }
        selectionChanged();
    }
    private void selectionChanged() {
        count.setText(getString(string.files_to_send)+(selection.isEmpty()?"":" ("+selection.size()+")"));
        grid.requestLayout(); grid.invalidate();
    }
    private void pick() {
        startActivityForResult(new Intent("com.alensw.PicFolder.action.PICK_MULTIPLE").setClass(this,GalleryActivity.class),PICK);
    }
    private final class Thumbnails implements com.alensw.ui.view.aj {
        public int a() { return selection.size(); } public String a(int i) { return null; }
        public void a(int from,int to,int speed) {
            for(int i=Math.max(0,from);i<Math.min(to,selection.size());i++) {
                CommonFile f=thumbs.get(selection.get(i));
                if(f!=null && !QuickApp.w.a(f)) QuickApp.t.a(f,false,(key,image)->{
                    QuickApp.w.a(key,(com.alensw.b.a.c)image.l()); grid.postInvalidate();
                });
            }
        }
        public void a(int i,com.alensw.ui.view.ak cell) {
            if(i<0 || i>=selection.size()) return;
            Uri u=selection.get(i); CommonFile f=thumbs.get(u);
            cell.a=f==null?null:(com.alensw.b.h.h)QuickApp.w.b(f);
            if(cell.a!=null) cell.a.l();
            cell.c=f==null?u.getLastPathSegment():f.h()=='I'?null:f.a();
        }
        public void a(int i,int j,boolean b) {} public void a(int i,Canvas c,RectF r) {}
        public void b(int i) {} public void c(int i) {}
    }
    private static final class DeviceRow {
        final TextView name,detail; final ImageView image;
        DeviceRow(View v) { name=v.findViewById(id.title); detail=v.findViewById(id.summary); image=v.findViewById(id.icon); }
    }
    private final class Devices extends BaseAdapter {
        public int getCount() { return devices.size(); }
        public NearbyDevices.Entry getItem(int i) { return devices.get(i); }
        public long getItemId(int i) { return getItem(i).id; }
        public boolean hasStableIds() { return true; }
        public boolean areAllItemsEnabled() { return false; }
        public boolean isEnabled(int i) { return getItem(i).available(); }
        public View getView(int i,View v,ViewGroup parent) {
            if(v==null) { v=getLayoutInflater().inflate(layout.ls_device,parent,false); v.setTag(new DeviceRow(v)); }
            DeviceRow r=(DeviceRow)v.getTag(); LocalSend.Peer p=getItem(i).peer;
            boolean mobile="mobile".equals(p.deviceType), desktop="desktop".equals(p.deviceType);
            r.image.setImageResource(mobile?drawable.ls_mobile:desktop?drawable.ls_desktop:drawable.ls_device);
            r.name.setText(p.alias);
            String kind=p.deviceModel.isEmpty()?getString(mobile?string.ls_mobile:desktop?string.ls_desktop:string.ls_device):p.deviceModel;
            r.detail.setText((getItem(i).available()?kind:getString(string.ls_encrypted))+" · "+p.ip+":"+p.port);
            v.setAlpha(getItem(i).available()?1f:.6f); return v;
        }
    }
    private final class History extends BaseAdapter {
        public int getCount() { return rows.size(); }
        public Object getItem(int i) { return rows.get(i); }
        public long getItemId(int i) { return i; }
        public View getView(int i,View v,ViewGroup parent) {
            if(v==null) {
                v=getLayoutInflater().inflate(layout.transfer_list_item,parent,false);
                icon(v.findViewById(id.cancel),raw.ic_menu_cancel);
                v.findViewById(id.cancel).setContentDescription(getString(android.R.string.cancel));
            }
            TransferSessionService.Row r=rows.get(i);
            ((TextView)v.findViewById(id.sender)).setText(r.label);
            ((TextView)v.findViewById(id.content)).setText(r.finished?"":r.percent+"%");
            ((ProgressBar)v.findViewById(id.progress)).setProgress(r.percent);
            ImageView cancel=v.findViewById(id.cancel); cancel.setVisibility(r.finished?View.GONE:View.VISIBLE);
            cancel.setOnClickListener(b->{ if(service!=null) service.cancel(r.key); });
            return v;
        }
    }
    private void sendTo(LocalSend.Peer peer) {
        if(service==null) return;
        if(!"http".equals(peer.protocol)) { toast(getString(string.ls_https)); return; }
        if(selection.isEmpty()) { toast(getString(string.ls_pick)); pick(); return; }
        if(service.send(peer,selection)) pager.setCurrentItem(1);
    }
    private void toast(String msg) { Toast.makeText(this,msg,Toast.LENGTH_LONG).show(); }
    @Override public void problem(String msg) { toast(msg); }
    @Override public void peersChanged() {
        devices.clear(); if(service!=null) devices.addAll(service.nearby.rows);
        deviceAdapter.notifyDataSetChanged(); nearby.setText(getString(string.ls_nearby,devices.size()));
        String address=service==null?null:service.address();
        tip.setText(address==null?getString(string.ls_offline):getString(string.ls_here,address));
    }
    @Override public void rowsChanged() {
        rows.clear(); rows.addAll(TransferSessionService.history); historyAdapter.notifyDataSetChanged();
    }
    private void hideReceive() {
        if(receive==null) return;
        root.removeView(receive.view); receive=null; pager.setVisibility(View.VISIBLE);
        if(J!=null) { J.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS); J.setSelectedNavigationItem(pager.getCurrentItem()); }
        if(tabBar!=null) tabBar.setVisibility(View.VISIBLE);
    }
    private void decision(String key,boolean yes) {
        if(service==null) return;
        hideReceive(); pager.setCurrentItem(1); service.answer(key,yes);
    }
    private void dismissPin() {
        if(pinDialog!=null) { pinDialog.dismiss(); pinDialog=null; } pinKey=null;
    }
    @Override public void promptsChanged() {
        if(!resumed || service==null) return;
        if(pinKey!=null && !service.pins.containsKey(pinKey)) dismissPin();
        if(receive!=null && !service.offers.containsKey(receive.offer.key)) hideReceive();
        if(receive!=null || pinDialog!=null) return;
        for(TransferSessionService.Offer offer:service.offers.values()) if(!deferred.contains(offer.key)) {
            receive=new ReceivePage(this,root,offer,()->decision(offer.key,true),()->decision(offer.key,false));
            pager.setVisibility(View.GONE); if(tabBar!=null) tabBar.setVisibility(View.GONE);
            if(J!=null) J.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            root.addView(receive.view); return;
        }
        for(String key:service.pins.keySet()) if(!deferred.contains(key)) {
            pinKey=key; EditText input=new EditText(this); input.setSingleLine();
            pinDialog=new AlertDialog.Builder(this).setTitle(service.pins.get(key).alias+" · PIN").setView(input)
                    .setPositiveButton(android.R.string.ok,(d,w)->{ if(service!=null) service.pin(key,input.getText().toString()); })
                    .setNegativeButton(android.R.string.cancel,(d,w)->{ if(service!=null) service.cancel(key); })
                    .setOnCancelListener(d->{ deferred.add(key); dismissPin(); }).show(); return;
        }
    }
    @Override public void onBackPressed() {
        if(receive!=null) {
            deferred.add(receive.offer.key); hideReceive(); pager.setCurrentItem(1); return;
        }
        super.onBackPressed();
    }
    @Override protected void onResume() {
        super.onResume(); resumed=true;
        bound=bindService(new Intent(this,TransferSessionService.class),connection,BIND_AUTO_CREATE);
    }
    @Override protected void onPause() {
        resumed=false;
        if(service!=null) service.leave(this);
        if(bound) { unbindService(connection); bound=false; }
        service=null; dismissPin(); hideReceive(); super.onPause();
    }
    @Override public void onDestroy() { metadata.shutdownNow(); super.onDestroy(); }
    private void manual() {
        if(service==null) return;
        if(service.address()==null) { service.refreshNetwork(); if(service.address()==null) { toast(getString(string.ls_offline)); return; } }
        EditText field=new EditText(this); field.setSingleLine(); field.setHint(string.ls_address_hint);
        new AlertDialog.Builder(this).setTitle(getString(string.ls_address)+" · "+service.address()).setView(field)
                .setPositiveButton(android.R.string.ok,(d,w)->{
                    String[] parts=field.getText().toString().trim().split(":",-1);
                    try {
                        int port=parts.length==1?LocalSend.PORT:Integer.parseInt(parts[1]);
                        if(parts.length>2 || port<1 || port>65535 || !android.util.Patterns.IP_ADDRESS.matcher(parts[0]).matches())
                            throw new IllegalArgumentException();
                        if(service!=null) service.probe(parts[0],port);
                    } catch(IllegalArgumentException e) { toast(getString(string.ls_address_hint)); }
                }).setNegativeButton(android.R.string.cancel,null).show();
    }
    @Override public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(0,CONNECT,0,string.ls_address); menu.add(0,id.rename,0,string.rename);
        menu.add(0,PIN,0,string.ls_pin); menu.add(0,id.share,0,string.share);
        return true;
    }
    @Override public boolean onOptionsItemSelected(MenuItem item) {
        int key=item.getItemId();
        if(key==CONNECT) { manual(); return true; }
        if(key==PIN || key==id.rename) {
            boolean pin=key==PIN; EditText field=new EditText(this); field.setSingleLine();
            if(pin) { field.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_VARIATION_PASSWORD); field.setHint(string.ls_pin_hint); }
            field.setText(pin?prefs.getString("localsend.pin",""):alias());
            new AlertDialog.Builder(this).setTitle(pin?string.ls_pin:string.rename).setView(field)
                    .setPositiveButton(android.R.string.ok,(d,w)->{
                        String value=field.getText().toString().trim();
                        if(value.length()>128 || (!pin && value.isEmpty())) return;
                        prefs.edit().putString(pin?"localsend.pin":"transfer_name",value).apply();
                        a((CharSequence)alias()); if(service!=null) service.refreshNetwork();
                    }).setNegativeButton(android.R.string.cancel,null).show();
            return true;
        }
        if(key==id.share) { startActivity(Intent.createChooser(com.alensw.ui.activity.MainPreference.a(this),getString(string.share))); return true; }
        if(key==id.desktop_link) {
            Intent launch=new Intent(this,TransferActivity.class).setAction(Intent.ACTION_VIEW);
            sendBroadcast(new Intent("com.android.launcher.action.INSTALL_SHORTCUT")
                    .putExtra(Intent.EXTRA_SHORTCUT_INTENT,launch).putExtra(Intent.EXTRA_SHORTCUT_NAME,getString(string.transfer))
                    .putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,Intent.ShortcutIconResource.fromContext(this,drawable.ic_about)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
