package com.alensw.PicFolder;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
class ag extends android.support.a.a.a {
   final GalleryActivity a;

   ag(GalleryActivity var1, Activity var2, DrawerLayout var3, Object var4, int var5, int var6) {
      super(var2, var3, var4, var5, var6);
      this.a = var1;
   }

   @Override
   public void a(View view) {
        super.a(view);
        this.a.setTitle(com.alensw.PicFolder.R.string.app_name);
        this.a.a((CharSequence) null);
        GalleryActivity.d(this.a).requestFocus();
    }

   @Override
   public void a(View var1, boolean var2, int var3) {
      if (var2 && var3 != 0) {
         GalleryActivity.a(this.a).a();
      }
   }

   @Override
   public void b(View var1) {
      super.b(var1);
      com.alensw.ui.c.cu var2 = this.a.c();
      if (var2 != null) {
         this.a.setTitle(var2.B());
         this.a.a(var2.C());
         var2.D().requestFocus();
      }
   }
}
