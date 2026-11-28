package com.alensw.ui.activity;

class RevertAutoHideNavigation implements Runnable {
   final NavigationBarTipPreference a;

   RevertAutoHideNavigation(NavigationBarTipPreference var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.a.setChecked(false);
   }
}
