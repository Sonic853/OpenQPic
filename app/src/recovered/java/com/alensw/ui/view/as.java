package com.alensw.ui.view;

import android.view.View;
import android.view.ViewGroup.OnHierarchyChangeListener;
import android.widget.CheckedTextView;

class as implements OnHierarchyChangeListener {
   final int a;
   final int b;
   final com.alensw.ui.view.ar c;

   as(com.alensw.ui.view.ar var1, int var2, int var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void onChildViewAdded(View var1, View var2) {
      if (var2 instanceof CheckedTextView) {
         ModernListPreference.a((CheckedTextView)var2, this.a, this.b, this.c.b);
      }
   }

   @Override
   public void onChildViewRemoved(View var1, View var2) {
   }
}
