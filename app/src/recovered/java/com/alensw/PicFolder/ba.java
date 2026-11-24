package com.alensw.PicFolder;

import android.app.Activity;
import android.app.Application.ActivityLifecycleCallbacks;
import android.os.Bundle;

class ba implements ActivityLifecycleCallbacks {
   final QuickApp a;

   ba(QuickApp var1) {
      this.a = var1;
   }

   @Override
   public void onActivityCreated(Activity var1, Bundle var2) {
      QuickApp.a(this.a).incrementAndGet();
   }

   @Override
   public void onActivityDestroyed(Activity var1) {
      if (QuickApp.a(this.a).decrementAndGet() == 0) {
         this.a.d();
      }
   }

   @Override
   public void onActivityPaused(Activity var1) {
   }

   @Override
   public void onActivityResumed(Activity var1) {
   }

   @Override
   public void onActivitySaveInstanceState(Activity var1, Bundle var2) {
   }

   @Override
   public void onActivityStarted(Activity var1) {
      if (QuickApp.b(this.a).incrementAndGet() == 1) {
         this.a.b();
      }
   }

   @Override
   public void onActivityStopped(Activity var1) {
      if (QuickApp.b(this.a).decrementAndGet() == 0) {
         this.a.c();
      }
   }
}
