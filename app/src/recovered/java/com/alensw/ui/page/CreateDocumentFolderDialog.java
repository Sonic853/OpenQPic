package com.alensw.ui.page;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import com.alensw.documents.DocumentProviderAccess;
import com.alensw.ui.dialog.BackgroundProgressDialog;
class CreateDocumentFolderDialog extends BackgroundProgressDialog {
   final ContentResolver a;
   final Uri b;
   final String c;
   final MediaFolderPage e;
   private boolean f;

   CreateDocumentFolderDialog(MediaFolderPage var1, Activity var2, String var3, int var4, ContentResolver var5, Uri var6, String var7) {
      super(var2, var3, var4);
      this.e = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
   }

   @Override
   protected void a() {
      boolean var1;
      if (DocumentProviderAccess.a(this.a, this.b, "vnd.android.document/directory", this.c) != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.f = var1;
   }

   @Override
   protected void b() {
      if (this.f && this.e.H == 2) {
         this.e.a(false);
         this.e.n();
      }
   }
}
