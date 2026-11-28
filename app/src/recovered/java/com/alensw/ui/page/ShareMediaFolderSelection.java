package com.alensw.ui.page;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.IntentTargetCallback;
class ShareMediaFolderSelection implements IntentTargetCallback {
   final MediaFolderPage a;

   ShareMediaFolderSelection(MediaFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      if ("android.intent.action.SEND_MULTIPLE".equals(var1.getAction())) {
         this.a.C.a(var1);
      } else {
         var1.setType(null);
         var1.setFlags(0);
         var1.removeExtra("android.intent.extra.STREAM");
      }

      GalleryActions.a(this.a.C, var1, var2);
   }
}
