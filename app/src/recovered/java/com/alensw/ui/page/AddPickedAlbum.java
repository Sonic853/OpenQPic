package com.alensw.ui.page;

import com.alensw.ui.dialog.FolderSelectionCallback;
import java.io.File;

class AddPickedAlbum implements FolderSelectionCallback {
   final AlbumsPage a;

   AddPickedAlbum(AlbumsPage var1) {
      this.a = var1;
   }

   @Override
   public void a(File var1) {
      this.a.b(var1);
   }
}
