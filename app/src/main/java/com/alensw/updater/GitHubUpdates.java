package com.alensw.updater;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;
import com.alensw.PicFolder.R;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

public final class GitHubUpdates {
    // Accessed only on the UI thread.
    private static final Set<Activity> pending = Collections.newSetFromMap(new WeakHashMap<>());

    private GitHubUpdates() {}

    public static void onStartup(Activity activity) {
        if (PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("updates_on_startup", false))
            check(activity, false);
    }

    public static void check(Activity activity, boolean manual) {
        if (!pending.add(activity)) return;
        if (manual) toast(activity, R.string.update_check);
        new Thread(() -> {
            GitHubRelease available = null;
            int status;
            try {
                GitHubRelease latest = read((HttpURLConnection) new URL(GitHubRelease.API).openConnection());
                int installed = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0).versionCode;
                if (latest != null && latest.code > installed) available = latest;
                status = latest == null ? R.string.update_no_release : R.string.update_not_found;
            } catch (Exception failure) {
                Log.w("GitHubUpdates", "Release check failed", failure);
                status = R.string.update_check_failed;
            }
            final GitHubRelease release = available;
            final int message = status;
            activity.runOnUiThread(() -> {
                pending.remove(activity);
                if (activity.isFinishing() || activity.isDestroyed()) return;
                if (release == null) {
                    if (manual) toast(activity, message);
                    return;
                }
                new AlertDialog.Builder(activity).setTitle(R.string.update_avaliable)
                        .setMessage(release.notes.isEmpty() ? release.tag : release.tag + "\n\n" + release.notes)
                        .setNegativeButton(android.R.string.cancel, null)
                        .setPositiveButton(release.hasApk ? R.string.update_download : R.string.update_view_release,
                                (dialog, which) -> {
                                    try {
                                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(release.url)));
                                    } catch (ActivityNotFoundException e) { toast(activity, R.string.update_open_failed); }
                                }).show();
            });
        }, "GitHub release check").start();
    }

    static GitHubRelease read(HttpURLConnection c) throws Exception {
        try {
            c.setInstanceFollowRedirects(false);
            c.setConnectTimeout(10000);
            c.setReadTimeout(10000);
            c.setRequestProperty("Accept", "application/vnd.github+json");
            c.setRequestProperty("User-Agent", "OpenQPic-Android");
            int status = c.getResponseCode();
            if (status == 404) return null;
            if (status != 200) throw new IOException("GitHub HTTP " + status);
            try (InputStream in = c.getInputStream()) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                byte[] buf = new byte[8192];
                int n;
                while ((n = in.read(buf)) != -1) {
                    if (bytes.size() + n > 1024 * 1024) throw new IOException("Release metadata too large");
                    bytes.write(buf, 0, n);
                }
                return new GitHubRelease(new JSONObject(new String(bytes.toByteArray(), StandardCharsets.UTF_8)));
            }
        } finally { c.disconnect(); }
    }

    private static void toast(Activity activity, int message) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show();
    }
}
