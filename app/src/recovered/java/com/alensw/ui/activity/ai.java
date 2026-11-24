package com.alensw.ui.activity;

import android.preference.SwitchPreference;

class ai implements Runnable {
   final SwitchPreference a;
   final com.alensw.ui.activity.ah b;

   ai(com.alensw.ui.activity.ah var1, SwitchPreference var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.a.setChecked(false);
   }
}
