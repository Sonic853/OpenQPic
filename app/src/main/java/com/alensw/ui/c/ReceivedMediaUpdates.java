package com.alensw.ui.c;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import com.alensw.PicFolder.GalleryStorage;
import com.alensw.PicFolder.QuickApp;
import com.alensw.PicFolder.R;
import com.alensw.a.bh;
import com.alensw.a.e;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/** Coalesces directory changes until a local Albums or folder page can apply them. */
public final class ReceivedMediaUpdates {
    private static final int MAX_CHANGED_DIRECTORIES = 512;
    private static final Handler main = new Handler(Looper.getMainLooper());
    private static final Map<File, Long> changed = new HashMap<>();
    private static final WeakHashMap<cu, Long> pages = new WeakHashMap<>();
    private static final Runnable flush = ReceivedMediaUpdates::refresh;
    private static long version, reconcileAt, firstPending;
    private static MediaStoreDirectories mediaStore;

    private ReceivedMediaUpdates() {}

    public static void published(File directory) {
        File path = canonical(directory);
        main.post(() -> {
            if (!GalleryStorage.enhancedScan(QuickApp.a())) QuickApp.x = false;
            record(path);
            schedule();
        });
    }

    static void observe(cu page) {
        updateObserver();
        if (!pages.containsKey(page)) pages.put(page, 0L);
        if (pages.get(page) < version || mediaStore != null && mediaStore.hasPendingQuery()) schedule();
    }

    private static void updateObserver() {
        QuickApp app = QuickApp.a();
        if (!GalleryStorage.enhancedScan(app) || !GalleryStorage.hasAccess(app)) {
            if (mediaStore != null) mediaStore.close();
            mediaStore = null;
        } else if (mediaStore == null) {
            try {
                mediaStore = new MediaStoreDirectories(app, main, ReceivedMediaUpdates::schedule,
                        ReceivedMediaUpdates::indexedChanges, ReceivedMediaUpdates::volumeChanged);
            } catch (RuntimeException error) {
                Log.w("GalleryScan", "Cannot observe MediaStore", error);
            }
        }
    }

    private static void record(File directory) {
        changed.put(directory, ++version);
        if (changed.size() > MAX_CHANGED_DIRECTORIES) {
            changed.clear();
            reconcileAt = version;
        }
    }

    private static void indexedChanges(Set<File> directories) {
        if (!GalleryStorage.enhancedScan(QuickApp.a())) return;
        if (directories == null) {
            reconcileAt = ++version;
        } else if (!directories.isEmpty()) {
            for (File directory : directories) record(directory);
        } else {
            return;
        }
        schedule();
    }

    private static void volumeChanged() {
        QuickApp app = QuickApp.a();
        if (!GalleryStorage.enhancedScan(app)) return;
        QuickApp.p = new bh(app);
        QuickApp.x = false;
        reconcileAt = ++version;
        schedule();
    }

    private static void schedule() {
        long now = SystemClock.uptimeMillis();
        if (firstPending == 0) firstPending = now;
        main.removeCallbacks(flush);
        main.postAtTime(flush, Math.min(now + 180, firstPending + 1000));
    }

    static File canonical(File file) {
        try {
            return file.getCanonicalFile();
        } catch (IOException | SecurityException ignored) {
            return file.getAbsoluteFile();
        }
    }

    private static boolean visible(cu page) {
        return page.H == 2 && !page.I && page.C.c() == page;
    }

    private static boolean shows(cu page, File openDirectory, File dir) {
        if (page instanceof bf) return dir.equals(openDirectory);
        if (!((bz) page).o) return true;
        for (File ancestor = dir; ancestor != null; ancestor = ancestor.getParentFile()) {
            if (ancestor.equals(openDirectory)) return true;
        }
        return false;
    }

    private static void invalidate(File dir, Set<File> invalidated) {
        if (dir != null && invalidated.add(dir)) QuickApp.q.a(4, dir.getPath(), null);
    }

    private static void refresh() {
        firstPending = 0;
        boolean enhanced = GalleryStorage.enhancedScan(QuickApp.a());
        boolean canQuery = false;
        Set<File> invalidated = new HashSet<>();
        for (cu page : new ArrayList<>(pages.keySet())) {
            if (page == null || !visible(page)) continue;
            boolean busy = page instanceof bz ? ((bz) page).scanningAlbums() : page.J;
            if (busy) continue;
            canQuery = true;
            long previous = pages.get(page);
            if (previous == version) continue;
            long target = version;
            Runnable consumed = () -> pages.put(page, target);
            boolean reconcile = previous < reconcileAt;
            List<File> directories = new ArrayList<>();
            File open = page instanceof bf ? ((bf) page).g : ((bz) page).o ? ((bz) page).z : null;
            File openDirectory = open == null ? null : canonical(open);
            for (Map.Entry<File, Long> update : changed.entrySet()) {
                if (update.getValue() <= previous) continue;
                File dir = update.getKey();
                if (shows(page, openDirectory, dir)) directories.add(dir);
                else if (page instanceof bz) invalidateDirectory(dir, invalidated);
            }
            if (!reconcile && directories.isEmpty()) {
                consumed.run();
            } else if (page instanceof bz) {
                bz albums = (bz) page;
                if (enhanced && !albums.o && QuickApp.x && !reconcile) {
                    albums.scanChangedAlbums(directories, consumed);
                } else {
                    if (reconcile) QuickApp.q.a(4, null, null);
                    for (File dir : directories) {
                        invalidateDirectory(dir, invalidated);
                    }
                    QuickApp.x = false;
                    albums.scanAlbums(consumed);
                }
            } else {
                refreshFolder((bf) page, consumed);
            }
        }
        if (enhanced && canQuery && mediaStore != null) mediaStore.queryIfNeeded();
    }

    private static void invalidateDirectory(File dir, Set<File> invalidated) {
        invalidate(dir, invalidated);
        invalidate(dir.getParentFile(), invalidated);
    }

    private static void refreshFolder(bf folder, Runnable consumed) {
        e model = folder.g;
        folder.a(model, () -> {
            QuickApp.v.c(model);
            consumed.run();
            if (folder.g != model) return;
            if (!folder.k.isEmpty()) folder.k.retainAll(new HashSet<>(model.i));
            if (!visible(folder)) return;
            folder.f.setEmptyText(model.j() > 0 ? null : folder.g(R.string.no_picture_in_album));
            folder.f.requestLayout();
            folder.f.invalidate();
            folder.l();
        });
    }
}
