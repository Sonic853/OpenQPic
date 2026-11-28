package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import java.util.HashSet;

class MediaViewerCopyMoveResult implements CopyMoveCallback {
   final MediaViewerPage a;

   MediaViewerCopyMoveResult(MediaViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void a(boolean var1) {
      if (!var1) {
         MediaViewerPage.c(this.a);
      }
   }

   @Override
   public void a(boolean var1, HashSet var2) {
      if (!var1 && var2.size() > 0) {
         QuickApp.u.a(MediaViewerPage.d(this.a).i(), true);
         MediaViewerPage.e(this.a);
      }
   }
}
