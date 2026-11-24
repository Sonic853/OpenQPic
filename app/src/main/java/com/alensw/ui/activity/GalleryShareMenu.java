package com.alensw.ui.activity;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.alensw.PicFolder.CropActivity;
import com.alensw.PicFolder.FileProvider;
import com.alensw.PicFolder.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

/** The gallery's activity-target menu, including direct shares and the Android Sharesheet. */
public final class GalleryShareMenu implements MenuItem.OnMenuItemClickListener {
    private final Context context;
    private final Intent send;

    private GalleryShareMenu(Context context, Intent send) {
        this.context = context;
        this.send = send;
    }

    /** Java implementation of the five-argument activity menu in com.alensw.ui.activity.a. */
    public static int show(Context context, Intent intent, View anchor, o callback) {
        boolean share = prepareForMenu(context, intent);
        List<ResolveInfo> targets = new ArrayList<>(query(context, intent));
        Intent alternate = new Intent();
        HashSet<ResolveInfo> alternates = new HashSet<>();
        String action = intent.getAction();
        if (Intent.ACTION_SEND.equals(action)) {
            alternate = new Intent(intent).setAction("android.intent.action.SEND_MSG");
            List<ResolveInfo> sms = query(context, alternate);
            targets.addAll(sms);
            alternates.addAll(sms);
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action)) {
            // Keep the legacy HTC single-action fallback and its original direct-target callback.
            alternate.setAction(Intent.ACTION_SEND).setType(intent.getType()).putExtras(intent);
            for (ResolveInfo info : query(context, alternate)) {
                if (info.activityInfo.applicationInfo.packageName.startsWith("com.htc.")) {
                    targets.add(info);
                    alternates.add(info);
                    break;
                }
            }
        } else if (Intent.ACTION_ATTACH_DATA.equals(action) && Build.VERSION.SDK_INT >= 19) {
            alternate.setAction("android.service.wallpaper.CROP_AND_SET_WALLPAPER")
                    .setDataAndType(intent.getData(), intent.getType());
            HashSet<String> names = new HashSet<>();
            for (ResolveInfo info : targets) names.add(info.activityInfo.name);
            for (ResolveInfo info : query(context, alternate)) {
                if (!"com.android.wallpapercropper".equals(info.activityInfo.applicationInfo.packageName)
                        && names.add(info.activityInfo.name)) {
                    targets.add(info);
                    alternates.add(info);
                }
            }
        }

        int size = targets.size();
        if (!share && size == 0) return 0;
        if (!share && size == 1) {
            ResolveInfo only = targets.get(0);
            callback.a(alternates.contains(only) ? alternate : intent, only);
            return 1;
        }
        PackageManager pm = context.getPackageManager();
        SharedPreferences prefs = context.getSharedPreferences("activity_menu", Context.MODE_PRIVATE);
        com.alensw.b.l.b.a(targets, new b(pm, context, prefs));
        g directClick = new g(targets, callback, alternates, alternate, intent, prefs);
        com.alensw.b.g.b root = new com.alensw.b.g.b(context);
        root.a(true);
        if (share) addChooserItem(context, intent, root);
        int directCount = anchor != null && size > 6 ? 5 : size;
        com.alensw.b.g.b group = root;
        for (int i = 0; i < size; i++) {
            if (i == directCount) {
                String more = com.alensw.b.l.b.a(context.getResources(), "more_item_label", "More");
                group = (com.alensw.b.g.m) root.addSubMenu(more + '…');
            }
            ResolveInfo info = targets.get(i);
            CharSequence label = info.loadLabel(pm);
            com.alensw.b.g.l item = group.a(Menu.NONE, i, Menu.NONE, label == null ? info.activityInfo.name : label);
            item.a(info);
            item.setOnMenuItemClickListener(directClick);
        }
        root.a(anchor);
        return size + (share ? 1 : 0);
    }

    @SuppressWarnings("unchecked")
    private static List<ResolveInfo> query(Context context, Intent intent) {
        return a.a(context, intent, 0);
    }

    /** Resolve the MIME before querying direct targets as well as before creating the chooser. */
    private static boolean prepareForMenu(Context context, Intent intent) {
        if (!Intent.ACTION_SEND.equals(intent.getAction())
                && !Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction())) return false;
        // The editor's input is just a template. Its actual output is available after rendering.
        if (!(context instanceof CropActivity)) prepareStreams(context, intent);
        return true;
    }

    private static void addChooserItem(Context context, Intent send, Menu menu) {
        // The custom menu supplies its share icon from the item ID; direct IDs are list indices.
        menu.add(Menu.NONE, R.id.share, Menu.NONE, R.string.share_other)
                .setOnMenuItemClickListener(new GalleryShareMenu(context, send));
    }

    @Override public boolean onMenuItemClick(MenuItem item) {
        Intent target = new Intent(send).setComponent(null).setPackage(null);
        // These are private inputs for the legacy file-grid direct-target callback.
        target.removeExtra("multiple_files");
        target.removeExtra("multiple_types");
        if (context instanceof CropActivity) {
            ((CropActivity) context).a(target);
        } else {
            openChooser(context, target);
        }
        return true;
    }

    /** A direct editor target has a component. "Other…" defers the chooser until the crop is saved. */
    public static void shareRenderedCrop(Context context, Intent pending, Intent result) {
        Intent target = new Intent(pending).setType(result.getType());
        target.putExtra(Intent.EXTRA_STREAM, result.getData());
        prepareStreams(context, target);
        if (target.getComponent() == null) openChooser(context, target);
        else a.a(context, target);
    }

    private static void openChooser(Context context, Intent target) {
        // On our minSdk (23+), createChooser copies ClipData and URI grants onto ACTION_CHOOSER.
        a.a(context, Intent.createChooser(target, null));
    }

    @SuppressWarnings("deprecation") // Typed EXTRA_STREAM accessors were added in API 33.
    private static void prepareStreams(Context context, Intent send) {
        boolean multiple = Intent.ACTION_SEND_MULTIPLE.equals(send.getAction());
        List<Uri> streams = multiple ? send.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
                : Collections.singletonList((Uri) send.getParcelableExtra(Intent.EXTRA_STREAM));
        if (streams == null || streams.isEmpty()) return;

        ArrayList<Uri> readable = new ArrayList<>(streams.size());
        LinkedHashSet<String> types = new LinkedHashSet<>();
        String common = null;
        for (Uri uri : streams) {
            if (uri == null) return;
            if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) uri = FileProvider.a(uri.getPath());
            readable.add(uri);
            String type = streamType(context, uri, send.getType());
            types.add(type);
            if (common == null) common = type;
            else if (!common.equals(type)) {
                String family = common.substring(0, common.indexOf('/') + 1);
                common = type.startsWith(family) ? family + "*" : "*/*";
            }
        }

        ClipData clip = new ClipData(null, types.toArray(new String[0]), new ClipData.Item(readable.get(0)));
        for (int i = 1; i < readable.size(); i++) clip.addItem(new ClipData.Item(readable.get(i)));
        if (multiple) send.putParcelableArrayListExtra(Intent.EXTRA_STREAM, readable);
        else send.putExtra(Intent.EXTRA_STREAM, readable.get(0));
        send.setType(common);
        send.setClipData(clip);
        send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    }

    private static String streamType(Context context, Uri uri, String hint) {
        String name = uri.getLastPathSegment();
        int dot = name == null ? -1 : name.lastIndexOf('.');
        String byName = dot < 0 ? null : MimeTypeMap.getSingleton()
                .getMimeTypeFromExtension(name.substring(dot + 1).toLowerCase(Locale.ROOT));
        // Our legacy provider derives MIME from the path too, but aliases MOV to MP4 and WebM to MKV.
        if (byName != null && "com.alensw.PicFolder.FileProvider".equals(uri.getAuthority())) return byName;
        String type = null;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            try {
                type = Intent.normalizeMimeType(context.getContentResolver().getType(uri));
            } catch (RuntimeException unavailable) {
                // A provider may not implement getType even when its stream is readable.
            }
        }
        if (type != null && type.indexOf('/') > 0 && !type.endsWith("/*")
                && !"application/octet-stream".equals(type)) return type;
        if (byName != null) return byName;
        if (type != null && type.endsWith("/*") && !"*/*".equals(type)) return type;
        // The old grid uses image/jpeg and video/mp4 as family hints even for other formats.
        int slash = hint == null ? -1 : hint.indexOf('/');
        return slash > 0 ? hint.substring(0, slash + 1) + "*" : "*/*";
    }
}
