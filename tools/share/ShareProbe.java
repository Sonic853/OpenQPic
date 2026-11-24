package org.openqpic.shareprobe;

import android.app.Activity;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Process;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/** Installed under a separate UID, without storage permissions, only for gallery_share_smoke.py. */
public final class ShareProbe extends Activity {
    private static final String GALLERY = "com.alensw.PicFolder";

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        Intent intent = getIntent();
        JSONObject report = new JSONObject();
        try {
            if ("org.openqpic.shareprobe.ENABLE_OVERFLOW".equals(intent.getAction())) {
                for (int i = 1; i <= 7; i++) {
                    getPackageManager().setComponentEnabledSetting(
                            new ComponentName(this, getPackageName() + ".Overflow" + i),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                }
                report.put("overflow", true);
            } else {
                receive(intent, report);
            }
        } catch (Exception e) {
            try { report.put("error", e.toString()); } catch (Exception ignored) {}
            Log.e("ShareProbe", "Share check failed", e);
        }
        try {
            try (FileOutputStream out = openFileOutput("result.tmp", MODE_PRIVATE)) {
                out.write(report.toString().getBytes(StandardCharsets.UTF_8));
            }
            if (!getFileStreamPath("result.tmp").renameTo(getFileStreamPath("result")))
                throw new IOException("Could not publish probe result");
        } catch (IOException e) {
            Log.e("ShareProbe", "Could not report result", e);
        }
        finish();
    }

    @SuppressWarnings("deprecation")
    private void receive(Intent intent, JSONObject report) throws Exception {
        report.put("action", intent.getAction());
        report.put("type", intent.getType());
        report.put("flags", intent.getFlags());
        report.put("uid", Process.myUid());
        // Count the eligible activities independently of how many rows fit in the popup window.
        Intent query = new Intent(intent.getAction()).setType(intent.getType());
        int targets = getPackageManager().queryIntentActivities(query, 0).size();
        if (Intent.ACTION_SEND.equals(intent.getAction())) {
            targets += getPackageManager().queryIntentActivities(query.setAction("android.intent.action.SEND_MSG"), 0).size();
        } else {
            for (ResolveInfo info : getPackageManager().queryIntentActivities(query.setAction(Intent.ACTION_SEND), 0)) {
                if (info.activityInfo.packageName.startsWith("com.htc.")) { targets++; break; }
            }
        }
        report.put("directTargets", targets);
        Context gallery = createPackageContext(GALLERY, 0);
        report.put("galleryUid", gallery.getApplicationInfo().uid);
        Configuration config = new Configuration(gallery.getResources().getConfiguration());
        config.setLocale(Locale.SIMPLIFIED_CHINESE);
        Context cn = gallery.createConfigurationContext(config);
        int launcherLabel = getPackageManager().getActivityInfo(
                new ComponentName(GALLERY, GALLERY + ".GalleryActivity"), 0).labelRes;
        report.put("zhCN", new JSONArray().put(cn.getString(gallery.getApplicationInfo().labelRes))
                .put(cn.getString(launcherLabel))
                .put(cn.getString(cn.getResources().getIdentifier("share_other", "string", GALLERY))));
        report.put("privatePaths", intent.hasExtra("multiple_files"));

        List<Uri> streams = Intent.ACTION_SEND_MULTIPLE.equals(intent.getAction())
                ? intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)
                : Collections.singletonList((Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM));
        ClipData clip = intent.getClipData();
        if (streams == null || streams.isEmpty() || clip == null || clip.getItemCount() != streams.size())
            throw new IOException("EXTRA_STREAM and ClipData counts differ or are empty");
        JSONArray types = new JSONArray();
        for (int i = 0; i < clip.getDescription().getMimeTypeCount(); i++)
            types.put(clip.getDescription().getMimeType(i));
        report.put("clipTypes", types);
        JSONArray hashes = new JSONArray();
        for (int i = 0; i < streams.size(); i++) {
            Uri uri = streams.get(i);
            if (uri == null || !"content".equals(uri.getScheme()) || !uri.equals(clip.getItemAt(i).getUri()))
                throw new IOException("ClipData item does not match content URI #" + i);
            if (checkUriPermission(uri, Process.myPid(), Process.myUid(), Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    != PackageManager.PERMISSION_GRANTED) throw new IOException("Missing read grant for " + uri);
            MessageDigest hash = MessageDigest.getInstance("SHA-256");
            try (InputStream in = getContentResolver().openInputStream(uri)) {
                byte[] buf = new byte[8192];
                int count;
                while ((count = in.read(buf)) != -1) hash.update(buf, 0, count);
            }
            StringBuilder hex = new StringBuilder();
            for (byte b : hash.digest()) hex.append(String.format(Locale.ROOT, "%02x", b & 255));
            hashes.put(hex.toString());
        }
        report.put("sha256", hashes);
    }
}
