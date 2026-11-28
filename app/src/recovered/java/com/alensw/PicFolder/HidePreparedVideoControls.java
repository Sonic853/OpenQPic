package com.alensw.PicFolder;

class HidePreparedVideoControls implements Runnable {
   final VideoPreparedListener a;

   HidePreparedVideoControls(VideoPreparedListener var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.a(false);
      this.a.b.c();
   }
}
