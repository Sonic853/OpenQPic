package com.alensw.ui.view;

class ab implements Runnable {
   final ImageGridView a;

   ab(ImageGridView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b(-1, false);
   }
}
