package com.alensw.ui.page;

import com.alensw.gallery.ScannedFolder;

class ReloadFolderContents implements Runnable {
   final ScannedFolder a;
   final ScannedFolder b;
   final Runnable c;
   final MediaActionPage d;

   ReloadFolderContents(MediaActionPage var1, ScannedFolder var2, ScannedFolder var3, Runnable var4) {
      this.d = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
   }

   @Override
   public void run() {
      this.a.b(false);
      this.d.b.post(new ApplyReloadedFolder(this));
   }
}
