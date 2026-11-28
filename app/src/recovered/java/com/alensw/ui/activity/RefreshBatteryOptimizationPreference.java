package com.alensw.ui.activity;

import android.preference.Preference;

class RefreshBatteryOptimizationPreference extends Thread {
   final MainPreference this$0;

   RefreshBatteryOptimizationPreference(MainPreference var1) {
      this.this$0 = var1;
   }

   @Override
   public void run() {
      try {
         Preference var1 = this.this$0.findPreference("disable_battery_opt");
         if (this.this$0.isBatteryOptimizationEnabled(false)) {
            this.this$0.getPreferenceScreen().removePreference(var1);
         }

         Thread.sleep(1000L);
         var1 = this.this$0.findPreference("disable_battery_opt");
         if (this.this$0.isBatteryOptimizationEnabled(false)) {
            this.this$0.getPreferenceScreen().removePreference(var1);
         }
      } catch (Exception var2) {
      }
   }
}
