package com.alensw.ui.view;

import android.net.Uri;
import android.widget.ImageView;
import com.alensw.PicFolder.QuickApp;
class aa implements com.alensw.a.aj, Runnable {
   final com.alensw.ui.view.z a;
   private final ImageView b;
   private final Uri c;
   private com.alensw.b.h.h d;

   public aa(com.alensw.ui.view.z var1, ImageView var2, Uri var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public void a(Object var1, com.alensw.b.h.h var2) {
      this.d = (com.alensw.b.h.h)var2.l();
      QuickApp.w.a(var1, (com.alensw.b.a.c)var2.l());
      this.b.post(this);
   }

   @Override
   public void run() {
      if (this.a.a(this.c) == this.b) {
         com.alensw.ui.view.z.a(this.b, this.d);
      }

      if (this.d != null) {
         this.d.m();
      }

      this.d = null;
   }
}
