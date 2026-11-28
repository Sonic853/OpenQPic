package com.alensw.ui.activity;

import android.preference.SwitchPreference;

class DisableProtectedSwitch implements Runnable {
   final SwitchPreference a;
   final ProtectedSwitchClick b;

   DisableProtectedSwitch(ProtectedSwitchClick var1, SwitchPreference var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.a.setChecked(false);
   }
}
