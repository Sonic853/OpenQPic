package com.alensw.ui.page;

import android.view.View;
import android.view.View.OnClickListener;
import com.alensw.gallery.ScannedFolder;
class AlbumBreadcrumbClick implements OnClickListener {
   final AlbumsPage a;

   AlbumBreadcrumbClick(AlbumsPage var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      if (this.a.o) {
         this.a.b(new ScannedFolder(this.a.k.getPath(), 0));
      }
   }
}
