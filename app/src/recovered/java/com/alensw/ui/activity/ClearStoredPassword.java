package com.alensw.ui.activity;

class ClearStoredPassword implements Runnable {
   final PasswordModePreference a;

   ClearStoredPassword(PasswordModePreference var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      this.a.b.edit().remove("password").commit();
      this.a.a.setChecked(false);
      this.a.a.setSummary(null);
      this.a.c.a(com.alensw.PicFolder.R.string.password_cleared);
   }
}
