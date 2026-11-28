package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import com.alensw.utils.GalleryUtils;
import java.util.HashSet;

class RotateMediaFolderItemsDialog extends BackgroundProgressDialog {
   final HashSet a;
   final int b;
   final MediaFolderPage c;

   RotateMediaFolderItemsDialog(MediaFolderPage var1, Activity var2, String var3, int var4, HashSet var5, int var6) {
      super(var2, var3, var4);
      this.c = var1;
      this.a = var5;
      this.b = var6;
   }

   @Override
   protected void a() {
      for (Object entry : this.a) {
         com.alensw.bean.CommonFile var2 = (com.alensw.bean.CommonFile) entry;
         String var4 = var2.j();
         if (var4 != null && var2.h() == 'I') {
            int var1 = GalleryUtils.a(var4, this.b, false);
            if (var1 != -1) {
               QuickApp.s.a(var4, var1);
               QuickApp.r.a(var4);
            }
         }

         if (!this.d()) {
            break;
         }
      }
   }

   @Override
   protected void b() {
        java.util.Iterator it = this.a.iterator();
        while (it.hasNext()) {
            MediaFolderPage.a(this.c).c((CommonFile) it.next());
        }
        this.c.f.requestLayout();
        this.c.n();
    }
}
