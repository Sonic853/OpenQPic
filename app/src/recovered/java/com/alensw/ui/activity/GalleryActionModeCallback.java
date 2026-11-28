package com.alensw.ui.activity;

import android.os.Build.VERSION;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ActionMode.Callback;

class GalleryActionModeCallback implements Callback {
   final GalleryChromeActivity a;

   GalleryActionModeCallback(GalleryChromeActivity var1) {
      this.a = var1;
   }

   @Override
   public boolean onActionItemClicked(ActionMode var1, MenuItem var2) {
      return this.a.onOptionsItemSelected(var2);
   }

   @Override
   public boolean onCreateActionMode(ActionMode var1, Menu var2) {
      if (VERSION.SDK_INT < 21) {
         this.a.i();
      }

      return this.a.c(var2);
   }

   @Override
   public void onDestroyActionMode(ActionMode var1) {
      if (GalleryChromeActivity.a(this.a) == var1) {
         GalleryChromeActivity.a(this.a, null);
      }

      this.a.r();
   }

   @Override
   public boolean onPrepareActionMode(ActionMode var1, Menu var2) {
      return this.a.d(var2);
   }
}
