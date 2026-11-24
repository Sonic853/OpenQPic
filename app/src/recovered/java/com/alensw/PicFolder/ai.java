package com.alensw.PicFolder;

import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import com.alensw.bean.CommonRoot;

class ai implements OnItemClickListener {
   final GalleryActivity a;

   ai(GalleryActivity var1) {
      this.a = var1;
   }

   @Override
   public void onItemClick(AdapterView var1, View var2, int var3, long var4) {
      GalleryActivity.b(this.a).d(3);
      CommonRoot var6 = GalleryActivity.a(this.a).c(var3);
      if (var6 != null) {
         if (var6.n != null) {
            com.alensw.PicFolder.aj var7 = new com.alensw.PicFolder.aj(this, var3, var6);
            PreferenceManager.getDefaultSharedPreferences(this.a).edit().putString("last_drive", var6.n).commit();
            var7.run();
         } else {
            GalleryActivity.a(this.a).a(var1, var2, var3, var4);
         }
      }
   }
}
