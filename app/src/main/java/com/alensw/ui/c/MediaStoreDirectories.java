package com.alensw.ui.c;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Process;
import android.provider.MediaStore;
import android.util.Log;
import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** A directory-level snapshot also handles providers that notify only a collection URI. */
final class MediaStoreDirectories {
    private static final Uri[] COLLECTIONS = {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    };
    private static final String[] COLUMNS = {"_data", "_id", "date_modified", "_size", "datetaken"};
    private static final String[] GENERATION_COLUMNS = {
            "_data", "_id", "date_modified", "_size", "datetaken", "generation_modified"
    };
    private static final com.alensw.b.k.a queries = com.alensw.b.k.a.a(1, Process.THREAD_PRIORITY_BACKGROUND);

    interface Changes {
        /** A null result requests a full reconciliation if the provider has no usable baseline. */
        void found(Set<File> directories);
    }

    private final Context context;
    private final ContentResolver resolver;
    private final Handler main;
    private final Runnable schedule;
    private final Changes changes;
    private final Runnable volumeChanged;
    private boolean watchingVolumes;
    private boolean closed;
    private boolean pending = true;
    private boolean signaled;
    private boolean querying;
    private Map<String, Stamp> snapshot;

    private final BroadcastReceiver volumes = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!closed) volumeChanged.run();
        }
    };
    private final ContentObserver observer;

    MediaStoreDirectories(Context context, Handler main, Runnable schedule, Changes changes, Runnable volumeChanged) {
        this.context = context;
        this.resolver = context.getContentResolver();
        this.main = main;
        this.schedule = schedule;
        this.changes = changes;
        this.volumeChanged = volumeChanged;
        queries.allowCoreThreadTimeOut(true);
        observer = new ContentObserver(main) {
            @Override
            public void onChange(boolean selfChange, Uri uri) {
                if (closed || !isMedia(uri)) return;
                pending = signaled = true;
                schedule.run();
            }
        };
        resolver.registerContentObserver(Uri.parse("content://media"), true, observer);
        IntentFilter mounts = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        mounts.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        mounts.addAction(Intent.ACTION_MEDIA_EJECT);
        mounts.addAction(Intent.ACTION_MEDIA_REMOVED);
        mounts.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        mounts.addDataScheme("file");
        try {
            context.registerReceiver(volumes, mounts);
            watchingVolumes = true;
        } catch (RuntimeException error) {
            Log.w("GalleryScan", "Cannot observe storage volumes", error);
        }
    }

    private static boolean isMedia(Uri uri) {
        if (uri == null) return true;
        List<String> parts = uri.getPathSegments();
        if (parts.isEmpty()) return true;
        if ("internal".equals(parts.get(0))) return false;
        if (parts.size() < 2) return true;
        String collection = parts.get(1);
        if ("images".equals(collection) || "video".equals(collection)) {
            return parts.size() < 3 || "media".equals(parts.get(2));
        }
        return "file".equals(collection);
    }

    boolean hasPendingQuery() {
        return pending && !querying;
    }

    void close() {
        closed = true;
        resolver.unregisterContentObserver(observer);
        if (watchingVolumes) context.unregisterReceiver(volumes);
    }

    void queryIfNeeded() {
        if (!hasPendingQuery()) return;
        boolean changedBeforeQuery = signaled;
        pending = signaled = false;
        querying = true;
        Map<String, Stamp> previous = snapshot;
        queries.execute(() -> {
            Map<String, Stamp> current = readSnapshot();
            Set<File> directories = previous == null || current == null ? null : changedDirectories(previous, current);
            main.post(() -> {
                if (closed) return;
                querying = false;
                snapshot = current;
                if (directories != null || changedBeforeQuery) changes.found(directories);
                if (pending) schedule.run();
            });
        });
    }

    private static Set<File> changedDirectories(Map<String, Stamp> previous, Map<String, Stamp> current) {
        Set<File> directories = new HashSet<>();
        for (Map.Entry<String, Stamp> entry : current.entrySet()) {
            if (!entry.getValue().matches(previous.get(entry.getKey()))) {
                directories.add(ReceivedMediaUpdates.canonical(new File(entry.getKey())));
            }
        }
        for (String path : previous.keySet()) {
            if (!current.containsKey(path)) directories.add(ReceivedMediaUpdates.canonical(new File(path)));
        }
        return directories;
    }

    private Map<String, Stamp> readSnapshot() {
        Map<String, Stamp> directories = new HashMap<>();
        boolean hasGeneration = Build.VERSION.SDK_INT >= 30;
        String[] projection = hasGeneration ? GENERATION_COLUMNS : COLUMNS;
        try {
            for (Uri collection : COLLECTIONS) {
                try (Cursor cursor = resolver.query(collection, projection, null, null, null)) {
                    if (cursor == null) return null;
                    while (cursor.moveToNext()) {
                        String path = cursor.getString(0);
                        if (path == null) continue;
                        int separator = path.lastIndexOf('/');
                        if (separator <= 0) continue;
                        String directory = path.substring(0, separator);
                        Stamp stamp = directories.get(directory);
                        if (stamp == null) {
                            stamp = new Stamp();
                            directories.put(directory, stamp);
                        }
                        long row = 31L * cursor.getLong(1) + path.hashCode();
                        for (int column = 2; column < projection.length; column++) {
                            row = row * 31L + cursor.getLong(column);
                        }
                        stamp.add(row);
                    }
                }
            }
        } catch (RuntimeException error) {
            Log.w("GalleryScan", "Cannot reconcile indexed media", error);
            return null;
        }
        return directories;
    }

    private static final class Stamp {
        private int count;
        private long hash;

        void add(long row) {
            count++;
            // Order-independent change detection, including a rename or a same-second replacement.
            row = (row ^ (row >>> 33)) * 0xff51afd7ed558ccdL;
            hash += row ^ (row >>> 33);
        }

        boolean matches(Stamp other) {
            return other != null && count == other.count && hash == other.hash;
        }
    }
}
