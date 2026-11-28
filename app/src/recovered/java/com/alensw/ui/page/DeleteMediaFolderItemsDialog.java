package com.alensw.ui.page;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import com.alensw.bean.CommonFile;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import java.util.ArrayList;
import java.util.HashSet;

class DeleteMediaFolderItemsDialog extends BackgroundProgressDialog {
   final HashSet a;
   final ContentResolver b;
   final ThumbnailDatabase c;
   final ArrayList e;
   final MediaFolderPage f;

   DeleteMediaFolderItemsDialog(MediaFolderPage var1, Activity var2, String var3, int var4, HashSet var5, ContentResolver var6, ThumbnailDatabase var7, ArrayList var8) {
      super(var2, var3, var4);
      this.f = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
      this.e = var8;
   }

   @Override
   protected void a() {
      for (Object entry : this.a) {
         com.alensw.bean.CommonFile var2 = (com.alensw.bean.CommonFile) entry;
         Uri var3 = var2.i();
         boolean var1;
         if (this.b.delete(var3, null, null) > 0) {
            var1 = true;
         } else {
            var1 = false;
         }

         if (var1) {
            this.c.a(var3.toString());
            this.e.add(var2);
         }

         if (!this.d()) {
            break;
         }
      }
   }

   @Override
   protected void b() {
      this.f.a(this.e);
      this.f.l();
      this.f.n();
   }
}
