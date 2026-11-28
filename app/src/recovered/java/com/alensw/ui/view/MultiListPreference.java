package com.alensw.ui.view;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

public class MultiListPreference extends ModernListPreference {
   private boolean a;
   private int b;
   private final boolean[] c = new boolean[this.getEntries().length];

   public MultiListPreference(Context var1) {
      this(var1, null);
   }

   public MultiListPreference(Context var1, AttributeSet var2) {
      this(var1, var2, 0);
   }

   public MultiListPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2, var3);
   }

   static boolean[] a(MultiListPreference var0) {
      return var0.c;
   }

   public void a(int var1) {
      byte var3 = 0;
      boolean var2;
      if (this.b != var1) {
         var2 = true;
      } else {
         var2 = false;
      }

      if (var2 || !this.a) {
         this.b = var1;
         this.a = true;
         this.persistInt(var1);
         if (var2) {
            this.notifyChanged();
         }

         CharSequence[] var5 = this.getEntries();
         StringBuilder var4 = new StringBuilder(128);

         for (int var6 = var3; var6 < this.c.length; var6++) {
            if ((this.b & 1 << var6) != 0) {
               if (var4.length() > 0) {
                  var4.append(", ");
               }

               var4.append(var5[var6]);
            }
         }

         this.setSummary(var4.toString());
      }
   }

   @Override
   protected void onDialogClosed(boolean var1) {
      int var2 = 0;
      int var4 = 0;

      while (var2 < this.c.length) {
         int var3 = var4;
         if (this.c[var2]) {
            var3 = var4 | 1 << var2;
         }

         var2++;
         var4 = var3;
      }

      if (var1 && this.b != var4 && this.callChangeListener(var4)) {
         this.a(var4);
      }
   }

   @Override
   protected Object onGetDefaultValue(TypedArray var1, int var2) {
      return var1.getInt(var2, 0);
   }

   @Override
   protected void onPrepareDialogBuilder(Builder var1) {
      for (int var2 = 0; var2 < this.c.length; var2++) {
         boolean[] var4 = this.c;
         boolean var3;
         if ((this.b & 1 << var2) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         var4[var2] = var3;
      }

      var1.setMultiChoiceItems(this.getEntries(), this.c, new TogglePreferenceChoice(this));
      a(this, true);
   }

   @Override
   protected void onSetInitialValue(boolean var1, Object var2) {
      int var3;
      if (var1) {
         var3 = this.getPersistedInt(this.b);
      } else {
         var3 = (Integer)var2;
      }

      this.a(var3);
   }
}
