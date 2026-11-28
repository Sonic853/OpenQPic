package com.alensw.ui.view;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.os.Build.VERSION;
import android.preference.ListPreference;
import android.util.AttributeSet;
import android.widget.CheckedTextView;
import com.alensw.ui.activity.GalleryChromeActivity;
import com.alensw.ui.menu.MenuListAdapter;
public class ModernListPreference extends ListPreference {
   public ModernListPreference(Context var1) {
      super(var1, null);
   }

   public ModernListPreference(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public ModernListPreference(Context var1, AttributeSet var2, int var3) {
      super(var1, var2);
   }

   public static void a(ListPreference var0, boolean var1) {
      if (VERSION.SDK_INT < 21) {
         Handler var2 = new Handler(Looper.getMainLooper());
         var2.postDelayed(new StyleListPreferenceDialog(var0, var1, var2), 16L);
      }
   }

   public static void a(CheckedTextView var0, int var1, int var2, boolean var3) {
      Resources var4 = var0.getResources();
      if (!var3) {
         var3 = true;
      } else {
         var3 = false;
      }

      var0.setCheckMarkDrawable(MenuListAdapter.a(var4, var1, var2, var3));
      if (VERSION.SDK_INT < 11) {
         var0.setTextColor(GalleryChromeActivity.b(var0.getContext(), 16842806));
         var0.setTextSize(2, 18.0F);
      }
   }

   @Override
   protected void onPrepareDialogBuilder(Builder var1) {
      super.onPrepareDialogBuilder(var1);
      a(this, false);
   }
}
