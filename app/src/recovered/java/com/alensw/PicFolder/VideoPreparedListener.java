package com.alensw.PicFolder;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Handler;
import android.os.Build.VERSION;
import com.alensw.utils.GalleryUtils;
class VideoPreparedListener implements OnPreparedListener {
   final Uri a;
   final PlayerActivity b;

   VideoPreparedListener(PlayerActivity var1, Uri var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void onPrepared(MediaPlayer mediaPlayer) {
        this.b.l = mediaPlayer;
        if (this.b.n == -2) {
            this.b.setRequestedOrientation(mediaPlayer.getVideoWidth() > mediaPlayer.getVideoHeight() ? 0 : 1);
        }
        String strB = null;
        if ("file".equals(this.a.getScheme())) {
            strB = this.a.getPath();
        } else if ("media".equals(this.a.getAuthority())) {
            strB = QuickApp.s.b(this.a);
        }
        if (strB == null) {
            strB = this.a.getLastPathSegment();
        }
        if (strB != null) {
            this.b.setTitle(GalleryUtils.c(strB));
        }
        this.b.i(false);
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            try {
                this.b.u = mediaPlayer.getTrackInfo();
                if (this.b.u != null) {
                    for (android.media.MediaPlayer.TrackInfo mediaPlayer$TrackInfo : this.b.u) {
                        if (mediaPlayer$TrackInfo.getTrackType() == 2) {
                            this.b.o++;
                        }
                    }
                    if (this.b.o > 1) {
                        this.b.invalidateOptionsMenu();
                    }
                }
            } catch (Throwable th) {
            }
        }
        PlayerActivity.h(this.b).post(new RefreshPlayButton(this));
        PlayerActivity.i(this.b).postDelayed(new HidePreparedVideoControls(this), this.b.i.getVisibility() == 0 ? 5000L : 0L);
        PlayerActivity.j(this.b).sendEmptyMessage(100);
    }
}
