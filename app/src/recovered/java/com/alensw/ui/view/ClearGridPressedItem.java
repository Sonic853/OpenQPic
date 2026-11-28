package com.alensw.ui.view;

class ClearGridPressedItem implements Runnable {
   final ImageGridView a;

   ClearGridPressedItem(ImageGridView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b(-1, false);
   }
}
