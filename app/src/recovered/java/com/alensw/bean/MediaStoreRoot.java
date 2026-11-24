package com.alensw.bean;

import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore.Images.Media;
import com.alensw.PicFolder.QuickApp;

public class MediaStoreRoot extends CommonRoot {
   public MediaStoreRoot(String var1, String var2) {
      super(var1, var2, 268435506);
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         this.j = Media.EXTERNAL_CONTENT_URI;
      }

      return this.j;
   }

   @Override
   public CommonFolder k() {
      return this.l();
   }

   public MediaStoreFolder l() {
      boolean var3 = PreferenceManager.getDefaultSharedPreferences(QuickApp.a()).getBoolean("moments_group", true);
      int var2 = this.e;
      int var1 = var2;
      if (var3) {
         var1 = var2 | -1073741824;
      }

      MediaStoreFolder var4 = new MediaStoreFolder(0, this.c, var1, this.g, this.h, null, var3, true);
      var4.n.addAll(MediaStoreFolder.u.values());
      var4.s();
      return var4;
   }
}
