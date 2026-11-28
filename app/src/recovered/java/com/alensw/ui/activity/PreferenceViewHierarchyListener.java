package com.alensw.ui.activity;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.OnHierarchyChangeListener;

class PreferenceViewHierarchyListener implements OnHierarchyChangeListener {
   final GalleryPreferenceActivity a;

   PreferenceViewHierarchyListener(GalleryPreferenceActivity var1) {
      this.a = var1;
   }

   @Override
   public void onChildViewAdded(View var1, View var2) {
      if (var2 instanceof ViewGroup) {
         this.a.a((ViewGroup)var2);
      }
   }

   @Override
   public void onChildViewRemoved(View var1, View var2) {
   }
}
