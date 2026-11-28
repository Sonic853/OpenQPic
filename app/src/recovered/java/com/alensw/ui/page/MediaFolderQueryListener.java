package com.alensw.ui.page;

import com.alensw.bean.CommonFile;
import com.alensw.bean.CommonFolder;
import com.alensw.concurrent.CancellableFutureTask;
import com.alensw.gallery.FolderQueryCallback;

class MediaFolderQueryListener implements FolderQueryCallback {
   final MediaFolderPage a;
   private final CommonFile b;

   MediaFolderQueryListener(MediaFolderPage var1) {
      this.a = var1;
      CommonFile var2;
      if (this.a.y != null) {
         var2 = this.a.y.a(this.a.y.k);
      } else {
         var2 = null;
      }

      this.b = var2;
   }

   @Override
   public void a(CommonFolder var1, boolean var2) {
      if (var2) {
         this.a.b.removeCallbacks(MediaFolderPage.c(this.a));
         this.a.b.postDelayed(MediaFolderPage.c(this.a), 50L);
      } else {
         this.b(null, true);
      }
   }

   @Override
   public void b(CommonFolder commonFolder, boolean z) {
        MediaFolderPage.a(this.a, (CancellableFutureTask) null);
        if (this.a.H == 0) {
            return;
        }
        if (commonFolder != null) {
            this.a.y.a(commonFolder, true);
        }
        Float f = (Float) MediaFolderPage.z.remove(this.a.y.i());
        if (f != null) {
            this.a.f.setScrollPos(f.floatValue());
        }
        if (this.b != null && !this.b.equals(this.a.y.a(this.a.y.k))) {
            this.a.y.k = this.a.y.a(this.b);
        }
        if (this.a.y.k != -1) {
            this.a.f.c(this.a.y.k);
        }
        this.a.f.requestLayout();
        this.a.m();
        if (z) {
            this.a.c(false);
            if (this.a.y.n()) {
                this.a.f.setEmptyText(this.a.g(com.alensw.PicFolder.R.string.no_picture_in_album));
            }
        }
    }
}
