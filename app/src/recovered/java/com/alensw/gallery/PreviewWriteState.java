package com.alensw.gallery;

class PreviewWriteState {
   boolean a;
   final PictureLoader b;

   private PreviewWriteState(PictureLoader var1) {
      this.b = var1;
   }

   PreviewWriteState(PictureLoader var1, PictureMemoryCache var2) {
      this(var1);
   }
}
