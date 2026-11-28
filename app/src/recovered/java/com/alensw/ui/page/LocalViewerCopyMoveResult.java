package com.alensw.ui.page;

import com.alensw.PicFolder.QuickApp;
import java.util.HashSet;

class LocalViewerCopyMoveResult implements CopyMoveCallback {
   final LocalViewerPage a;

   LocalViewerCopyMoveResult(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void a(boolean var1) {
      if (!var1) {
         LocalViewerPage.f(this.a);
      }
   }

   @Override
   public void a(boolean var1, HashSet var2) {
      if (!var1 && var2.size() > 0) {
         QuickApp.u.a(LocalViewerPage.b(this.a), true);
         LocalViewerPage.g(this.a);
      }
   }
}
