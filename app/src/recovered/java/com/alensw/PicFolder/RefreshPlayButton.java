package com.alensw.PicFolder;

class RefreshPlayButton implements Runnable {
   final VideoPreparedListener a;

   RefreshPlayButton(VideoPreparedListener var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.d();
   }
}
