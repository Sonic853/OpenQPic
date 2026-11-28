package com.alensw.ui.page;

class BindLocalFolderGrid implements Runnable {
   final Float a;
   final LocalFolderPage b;

   BindLocalFolderGrid(LocalFolderPage var1, Float var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (this.b.H == 2) {
         if (this.b.g.j() == 0) {
            this.b.f.setEmptyText(this.b.g(com.alensw.PicFolder.R.string.no_picture_in_album));
         }

         if (this.a != null) {
            this.b.f.setScrollPos(this.a);
         }

         this.b.f.requestLayout();
         this.b.l();
      }
   }
}
