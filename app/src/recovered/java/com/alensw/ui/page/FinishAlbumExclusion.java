package com.alensw.ui.page;

import com.alensw.ui.dialog.GalleryDialogs;

class FinishAlbumExclusion implements Runnable {
   final AlbumsPage a;

   FinishAlbumExclusion(AlbumsPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.C.q()) {
         this.a.C.d(false);
      }

      this.a.j.requestLayout();
      GalleryDialogs.a(this.a.C, 17039380, (CharSequence)this.a.g(com.alensw.PicFolder.R.string.excluded_tips));
   }
}
