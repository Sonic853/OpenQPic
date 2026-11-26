package com.alensw.transfer;

import android.os.SystemClock;
import org.openqpic.localsend.LocalSend;
import java.util.*;

/** Main-thread list. Heartbeats refresh a timestamp without invalidating visible rows. */
final class NearbyDevices {
    static final class Entry {
        final long id;
        LocalSend.Peer peer;
        long seen, probed;
        Entry(long id,LocalSend.Peer p) { this.id=id; peer=p; }
    }
    final List<Entry> rows=new ArrayList<>();
    private long nextId=1;
    boolean seen(LocalSend.Peer peer) {
        long now=SystemClock.elapsedRealtime();
        Entry entry=null; boolean changed=false;
        for(Iterator<Entry> it=rows.iterator();it.hasNext();) {
            Entry e=it.next(); LocalSend.Peer p=e.peer;
            if(p.fingerprint.equals(peer.fingerprint)) entry=e;
            else if(p.ip.equals(peer.ip) && p.port==peer.port) { it.remove(); changed=true; }
        }
        if(entry==null) {
            if(rows.size()>=256) return changed;
            entry=new Entry(nextId++,peer); rows.add(entry); changed=true;
        } else {
            LocalSend.Peer p=entry.peer;
            changed|=!p.alias.equals(peer.alias) || !p.ip.equals(peer.ip) || p.port!=peer.port ||
                    !p.protocol.equals(peer.protocol) || !p.deviceType.equals(peer.deviceType) || !p.deviceModel.equals(peer.deviceModel);
            entry.peer=peer;
        }
        entry.seen=now;
        if(changed) Collections.sort(rows,(a,b)->{
            int c=a.peer.alias.compareToIgnoreCase(b.peer.alias);
            return c!=0?c:a.peer.fingerprint.compareTo(b.peer.fingerprint);
        });
        return changed;
    }
    boolean maintain(LocalSend endpoint) {
        boolean changed=false; long now=SystemClock.elapsedRealtime();
        for(Iterator<Entry> it=rows.iterator();it.hasNext();) {
            Entry e=it.next(); long age=now-e.seen;
            if(age>90000) { it.remove(); changed=true; }
            else if(age>45000 && now-e.probed>15000) {
                e.probed=now; endpoint.probe(e.peer);
            }
        }
        return changed;
    }
}
