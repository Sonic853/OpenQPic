package com.alensw.ui.a;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Build.VERSION;
public abstract class ao extends ProgressDialog {
   public static final com.alensw.b.k.a d = new com.alensw.b.k.a(1, 2, 8, 10);
   private final Activity a;

   public ao(Activity var1, String var2, int var3) {
      super(var1, com.alensw.ui.a.u.a(var1));
      boolean var5 = false;
      this.a = var1;
      this.setCanceledOnTouchOutside(false);
      boolean var4;
      if (var3 > 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.setCancelable(var4);
      if (!var4) {
         var5 = true;
      }

      this.setIndeterminate(var5);
      if (var4) {
         this.setMax(var3);
         this.setProgressStyle(1);
         this.setButton(-2, var1.getString(17039360), new com.alensw.ui.a.ap(this));
         this.setTitle(var2);
      } else {
         this.setMessage(var2);
      }
   }

   static Activity a(com.alensw.ui.a.ao var0) {
      return var0.a;
   }

   protected abstract void a();

   protected void a(String var1) {
      this.a.runOnUiThread(new com.alensw.ui.a.as(this, var1));
   }

   protected void b() {
   }

   public void c() {
      if (com.alensw.ui.a.f.a(this)) {
         d.a(new com.alensw.ui.a.aq(this));
      }
   }

   protected boolean d() {
      boolean var1 = true;
      if (this.isShowing()) {
         this.incrementProgressBy(1);
      } else {
         var1 = false;
      }

      return var1;
   }

   @Override
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (VERSION.SDK_INT < 21) {
         com.alensw.ui.a.u.a(this.a, this);
      }
   }
}
