package com.alensw.updater;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqpic.version.GalleryVersion;

/** A published date-tagged Release. Navigation stays within the project's GitHub releases. */
final class GitHubRelease {
    static final String API = "https://api.github.com/repos/Sonic853/OpenQPic/releases/latest";
    private static final String RELEASES = "https://github.com/Sonic853/OpenQPic/releases/";
    final String tag, notes, url;
    final int code;
    final boolean hasApk;

    GitHubRelease(JSONObject json) throws JSONException {
        if (json.getBoolean("draft") || json.getBoolean("prerelease"))
            throw new IllegalArgumentException("Not a stable release");
        tag = json.getString("tag_name");
        code = GalleryVersion.code(tag);
        String body = json.isNull("body") ? "" : json.getString("body").trim();
        notes = body.length() <= 4000 ? body : body.substring(0, 4000) + "…";

        String filename = "OpenQPic-" + tag + ".apk";
        String download = RELEASES + "download/" + tag + "/" + filename;
        JSONArray assets = json.getJSONArray("assets");
        boolean found = false;
        for (int i = 0; i < assets.length(); i++) {
            JSONObject a = assets.getJSONObject(i);
            if (filename.equals(a.optString("name")) && "uploaded".equals(a.optString("state"))
                    && download.equals(a.optString("browser_download_url"))) {
                found = true;
                break;
            }
        }
        hasApk = found;
        url = found ? download : RELEASES + "tag/" + tag;
    }
}
