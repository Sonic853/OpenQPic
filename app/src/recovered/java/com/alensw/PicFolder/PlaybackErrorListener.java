package com.alensw.PicFolder;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;

class PlaybackErrorListener implements OnErrorListener {
   final Uri a;
    final PlayerActivity b;
    private boolean retryRequested;

   PlaybackErrorListener(PlayerActivity var1, Uri var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
    public boolean onError(MediaPlayer var1, int var2, int var3) {
       if(!retryRequested && com.alensw.video.VideoProxyService.retryInPlayer(this.b,this.a,this.b.getIntent().getType())) {
          retryRequested=true;
          return true;
       }
       this.b.x.c(this.a.toString());
      this.b.setTitle(com.alensw.PicFolder.R.string.load_failed);
      this.b.i(false);
      return false;
   }
}
