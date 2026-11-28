package com.alensw.ui.dialog;

import com.alensw.concurrent.CancellableCallable;

class ProgressDialogTask implements CancellableCallable {
   final BackgroundProgressDialog a;

   ProgressDialogTask(BackgroundProgressDialog var1) {
      this.a = var1;
   }

   @Override
   public void a() {
   }

   @Override
   public void b() {
      BackgroundProgressDialog.a(this.a).runOnUiThread(new FinishProgressDialog(this));
   }

   public Void c() {
      this.a.a();
      return null;
   }

   @Override
   public Object call() {
      return this.c();
   }
}
