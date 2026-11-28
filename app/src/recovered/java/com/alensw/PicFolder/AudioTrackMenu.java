package com.alensw.PicFolder;

import android.annotation.TargetApi;
import android.media.MediaPlayer.TrackInfo;
import android.view.Menu;
import android.view.View;
import com.alensw.ui.menu.MenuBuilderCallback;
class AudioTrackMenu implements MenuBuilderCallback {
   final PlayerActivity a;

   AudioTrackMenu(PlayerActivity var1) {
      this.a = var1;
   }

   @TargetApi(16)
   @Override
   public boolean a(Menu menu, View view) {
        for (int i = 0; i < this.a.u.length; i++) {
            android.media.MediaPlayer.TrackInfo mediaPlayer$TrackInfo = this.a.u[i];
            if (mediaPlayer$TrackInfo.getTrackType() == 2) {
                String language = mediaPlayer$TrackInfo.getLanguage();
                if (language == null || "und".equals(language)) {
                    language = "Audio #" + i;
                }
                menu.add(language).setOnMenuItemClickListener(new SelectAudioTrack(this, i));
            }
        }
        return true;
    }
}
