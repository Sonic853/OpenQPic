package com.alensw.ui.page;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.IntentTargetCallback;
class ShareDetailsMedia implements IntentTargetCallback {
   final MediaDetails a;

   ShareDetailsMedia(MediaDetails var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      GalleryActions.a(MediaDetails.a(this.a), var1, var2);
   }
}
