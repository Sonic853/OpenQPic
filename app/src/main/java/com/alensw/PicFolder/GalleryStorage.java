package com.alensw.PicFolder;

import android.annotation.TargetApi;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import java.io.File;
import java.util.List;
import java.util.Map;

/** Storage APIs used by the filesystem-backed gallery on newer Android releases. */
public final class GalleryStorage {
    private GalleryStorage() {}

    public static boolean hasAccess(Context context) {
        if(Build.VERSION.SDK_INT>=30) return Environment.isExternalStorageManager();
        return context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED
                && context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_GRANTED;
    }

    public static void requestAccess(Activity activity) {
        if(Build.VERSION.SDK_INT<30) {
            activity.requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            return;
        }
        try {
            activity.startActivity(new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    Uri.fromParts("package",activity.getPackageName(),null)));
        } catch(ActivityNotFoundException missing) {
            try { activity.startActivity(new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)); }
            catch(ActivityNotFoundException e) { Log.w("GalleryStorage","No all-files settings activity",e); }
        }
    }

    public static void accessGranted(Context context) {
        // The first scan can complete with no readable folders while the permission UI is open.
        QuickApp.p=new com.alensw.a.bh(context);
        QuickApp.q.a(4,null,null); // forget cached empty directories
        QuickApp.a(true); // existing page resume path reloads the current gallery
    }

    @TargetApi(30)
    public static void volumes(Context context, List<String> paths, Map<String,String> ids,
                               Map<String,String> labels) {
        StorageManager storage=context.getSystemService(StorageManager.class);
        for(StorageVolume volume:storage.getStorageVolumes()) {
            File dir=volume.getDirectory();
            if(dir==null) continue;
            String path=dir.getPath();
            paths.add(path);
            ids.put(path,volume.isPrimary()?"primary":volume.getUuid());
            labels.put(path,volume.getDescription(context));
        }
    }

    public static Cursor buckets(ContentResolver resolver, Uri collection, String[] columns) {
        if(Build.VERSION.SDK_INT>=30) {
            Bundle args=new Bundle();
            args.putStringArray(ContentResolver.QUERY_ARG_GROUP_COLUMNS,
                    new String[]{MediaStore.MediaColumns.BUCKET_ID});
            return resolver.query(collection,columns,args,null);
        }
        // Keep the original one-row-per-bucket query on pre-Q providers. Q has no public
        // group argument and validates selections; the caller deduplicates the paths there.
        return resolver.query(collection,columns,Build.VERSION.SDK_INT<29?"1) GROUP BY (1":null,null,null);
    }

    /** Android O+ only dispatches ContentObserver events for a registered provider. */
    public static Uri observerUri(Uri uri) {
        if(Build.VERSION.SDK_INT>=26 && "file".equals(uri.getScheme()))
            return FileProvider.a(uri.getPath());
        return uri;
    }

    /** Also carry read grants when a multi-selection is returned via Activity.setResult. */
    public static void clipStreams(Intent intent, List<Uri> streams) {
        ClipData clip=ClipData.newRawUri("",streams.get(0));
        for(int i=1;i<streams.size();i++) clip.addItem(new ClipData.Item(streams.get(i)));
        intent.setClipData(clip);
    }
}
