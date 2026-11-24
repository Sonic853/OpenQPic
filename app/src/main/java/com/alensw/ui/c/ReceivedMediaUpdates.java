package com.alensw.ui.c;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import com.alensw.PicFolder.QuickApp;
import com.alensw.a.e;
import java.io.File;
import java.io.IOException;
import java.util.*;

/** The local Albums (bz) and folder (bf) grids are filesystem-backed, not MediaStore observers.
 * Track publication, not temporary writes. All gallery models and views are touched on main. */
public final class ReceivedMediaUpdates {
    private static final Handler main=new Handler(Looper.getMainLooper());
    private static final Map<File,Long> changed=new HashMap<>(); // only app-selected receive directories
    private static final WeakHashMap<cu,Long> pages=new WeakHashMap<>();
    private static long version,firstPending;
    private static final Runnable flush=ReceivedMediaUpdates::refresh;

    private ReceivedMediaUpdates() {}

    public static void published(File directory) {
        File path=canonical(directory);
        main.post(()->{
            changed.put(path,++version);
            QuickApp.x=false; // the cached Albums scan is no longer complete
            schedule();
        });
    }
    /** On page resume or completion of an earlier folder load. Paused pages retain their watermark. */
    static void observe(cu page) {
        if(!pages.containsKey(page)) pages.put(page,0L);
        if(pages.get(page)<version) schedule();
    }
    private static void schedule() {
        long now=SystemClock.uptimeMillis();
        if(firstPending==0) firstPending=now;
        main.removeCallbacks(flush);
        main.postAtTime(flush,Math.min(now+180,firstPending+1000));
    }
    private static File canonical(File file) {
        try { return file.getCanonicalFile(); }
        catch(IOException | SecurityException ignored) { return file.getAbsoluteFile(); }
    }
    private static boolean visible(cu page) {
        return page.H==2 && !page.I && page.C.c()==page;
    }
    private static boolean shows(cu page,File dir) {
        if(page instanceof bf) {
            File open=((bf)page).g;
            return open!=null && (dir.equals(open) || dir.equals(canonical(open)));
        }
        bz albums=(bz)page;
        if(!albums.o) return true;
        if(albums.z==null) return false;
        File root=canonical(albums.z);
        for(File ancestor=dir;ancestor!=null;ancestor=ancestor.getParentFile())
            if(root.equals(ancestor)) return true;
        return false;
    }
    private static void invalidate(File dir,Set<File> invalidated) {
        // Scanner operation 4 evicts the folder and empty-directory caches (mtimes are in seconds).
        if(dir!=null && invalidated.add(dir)) QuickApp.q.a(4,dir.getPath(),null);
    }
    private static void refresh() {
        firstPending=0;
        Set<File> invalidated=new HashSet<>();
        for(cu page:new ArrayList<>(pages.keySet())) {
            if(page==null || !visible(page)) continue;
            long previous=pages.get(page);
            // Let the older asynchronous folder snapshot finish before issuing another; c(false)
            // wakes us again. Overlapping loads could otherwise put an older count back on screen.
            if(previous==version || (page instanceof bf && page.J)) continue;
            pages.put(page,version);
            boolean reload=false;
            for(Map.Entry<File,Long> update:changed.entrySet()) {
                if(update.getValue()<=previous) continue;
                File dir=update.getKey();
                if(page instanceof bz) {
                    // Also expire the parent's "no children" entry if this was the first receipt.
                    invalidate(dir,invalidated); invalidate(dir.getParentFile(),invalidated);
                }
                if(shows(page,dir)) reload=true;
            }
            if(!reload) continue;
            if(page instanceof bz) {
                QuickApp.x=false;
                ((bz)page).w(); // normal filtered, asynchronous album scan; keep scroll and selection
            } else {
                bf folder=(bf)page;
                e model=folder.g;
                folder.a(model,()->{
                    QuickApp.v.c(model); // this model may also be the cached album cover's key
                    if(!visible(folder) || folder.g!=model) return;
                    if(model.j()>0) folder.f.setEmptyText(null);
                    folder.f.requestLayout(); folder.f.invalidate(); folder.m();
                });
            }
        }
    }
}
