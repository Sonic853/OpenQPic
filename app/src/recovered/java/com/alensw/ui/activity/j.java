package com.alensw.ui.activity;

import android.content.Context;
final class j implements Runnable {
   final Context a;
   final Runnable b;

   j(Context var1, Runnable var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public void run() {
      com.alensw.ui.a.x.a(this.a, "protect_delete", this.b, null);
   }
}
