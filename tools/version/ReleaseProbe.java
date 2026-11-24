package com.alensw.updater;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.json.JSONException;
import org.openqpic.version.GalleryVersion;

/** Runs against the installed APK and Android's real org.json and networking APIs. */
public final class ReleaseProbe {
    private static final String APK = "https://github.com/Sonic853/OpenQPic/releases/download/2026.11.24p10/OpenQPic-2026.11.24p10.apk";
    private static final String JSON = "{\"tag_name\":\"2026.11.24p10\",\"body\":\"changes\",\"draft\":false,\"prerelease\":false,"
            + "\"assets\":[{\"name\":\"OpenQPic-2026.11.24p10.apk\",\"state\":\"uploaded\",\"browser_download_url\":\"" + APK + "\"}]}";

    private static GitHubRelease read(int status, String json) throws Exception {
        return GitHubUpdates.read(new HttpURLConnection(new URL(GitHubRelease.API)) {
            public void connect() {}
            public boolean usingProxy() { return false; }
            public void disconnect() {}
            public int getResponseCode() { return status; }
            public InputStream getInputStream() { return new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8)); }
        });
    }

    private static void check(boolean condition) { if (!condition) throw new AssertionError(); }

    public static void main(String[] args) throws Exception {
        GitHubRelease r = read(200, JSON);
        check(r.hasApk && APK.equals(r.url) && "changes".equals(r.notes));
        check(r.code > GalleryVersion.code("2026.11.24p9") && r.code < GalleryVersion.code("2026.11.25"));
        r = read(200, JSON.replace(APK, "https://example.invalid/app.apk"));
        check(!r.hasApk && r.url.equals("https://github.com/Sonic853/OpenQPic/releases/tag/2026.11.24p10"));
        check(read(404, "") == null);
        try { read(403, "rate limit"); throw new AssertionError(); } catch (IOException expected) {}
        try { read(200, "{"); throw new AssertionError(); } catch (JSONException expected) {}
        for (String invalid : new String[]{JSON.replace("false", "true"), JSON.replace("2026.11.24p10", "9.7")}) {
            try { read(200, invalid); throw new AssertionError(); } catch (IllegalArgumentException expected) {}
        }
        System.out.println("GitHub Releases: APK, fallback, date ordering, no release, HTTP error and invalid metadata: OK");
    }
}
