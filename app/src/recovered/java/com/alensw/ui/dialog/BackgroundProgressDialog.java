package com.alensw.ui.dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Build.VERSION;
import com.alensw.concurrent.AsyncExecutor;
public abstract class BackgroundProgressDialog extends ProgressDialog {
   public static final AsyncExecutor d = new AsyncExecutor(1, 2, 8, 10);
   private final Activity a;

   public BackgroundProgressDialog(Activity var1, String var2, int var3) {
      super(var1, GalleryAlertDialog.a(var1));
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
         this.setButton(-2, var1.getString(17039360), new CancelProgressDialog(this));
         this.setTitle(var2);
      } else {
         this.setMessage(var2);
      }
   }

   static Activity a(BackgroundProgressDialog var0) {
      return var0.a;
   }

   protected abstract void a();

   protected void a(String var1) {
      this.a.runOnUiThread(new ShowProgressMessage(this, var1));
   }

   protected void b() {
   }

   public void c() {
      if (GalleryDialogs.a(this)) {
         d.a(new ProgressDialogTask(this));
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
         GalleryAlertDialog.a(this.a, this);
      }
   }
}
