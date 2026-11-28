package com.alensw.PicFolder;

import android.view.View;
import android.view.View.OnClickListener;

class ReplayVideoClick implements OnClickListener {
   final PlayerActivity a;

   ReplayVideoClick(PlayerActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.k.seekTo(0);
      this.a.b(true);
      this.a.c();
   }
}
