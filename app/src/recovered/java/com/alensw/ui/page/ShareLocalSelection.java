package com.alensw.ui.page;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.IntentTargetCallback;
class ShareLocalSelection implements IntentTargetCallback {
   final LocalFolderPage a;

   ShareLocalSelection(LocalFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      if ("android.intent.action.SEND_MULTIPLE".equals(var1.getAction())) {
         this.a.C.a(var1);
         var1.removeExtra("multiple_files");
      } else {
         var1.setType(null);
         var1.setFlags(0);
         var1.removeExtra("android.intent.extra.STREAM");
      }

      GalleryActions.a(this.a.C, var1, var2);
   }
}
