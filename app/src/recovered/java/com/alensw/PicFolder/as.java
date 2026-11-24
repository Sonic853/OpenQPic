package com.alensw.PicFolder;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.net.Uri;

class as implements OnErrorListener {
   final Uri a;
   final PlayerActivity b;

   as(PlayerActivity var1, Uri var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public boolean onError(MediaPlayer var1, int var2, int var3) {
      this.b.x.c(this.a.toString());
      this.b.setTitle(com.alensw.PicFolder.R.string.load_failed);
      this.b.i(false);
      return false;
   }
}
