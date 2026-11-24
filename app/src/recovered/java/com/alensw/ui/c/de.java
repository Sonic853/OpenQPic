package com.alensw.ui.c;

class de implements Runnable {
   final com.alensw.ui.c.dd a;

   de(com.alensw.ui.c.dd var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.a.H != 0) {
         this.a.a.c(false);
         if (this.a.a.y.n()) {
            this.a.a.f.setEmptyText(this.a.a.g(com.alensw.PicFolder.R.string.no_picture_in_album));
         } else {
            this.a.a.m();
         }
      }
   }
}
