package com.alensw.ui.page;

import java.util.HashSet;

class MediaFolderCopyMoveResult implements CopyMoveCallback {
   final MediaFolderPage a;

   MediaFolderCopyMoveResult(MediaFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(boolean var1) {
      this.a.a(false);
   }

   @Override
   public void a(boolean var1, HashSet var2) {
      if (!var1) {
         this.a.a(var2);
         this.a.l();
      }

      this.a.n();
   }
}
