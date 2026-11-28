package com.alensw.ui.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.preference.ListPreference;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ListView;
import com.alensw.ui.activity.GalleryChromeActivity;
import com.alensw.ui.dialog.GalleryAlertDialog;
final class StyleListPreferenceDialog implements Runnable {
   final ListPreference a;
   final boolean b;
   final Handler c;

   StyleListPreferenceDialog(ListPreference var1, boolean var2, Handler var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public void run() {
      Dialog var4 = this.a.getDialog();
      if (var4 != null) {
         Context var5 = this.a.getContext();
         GalleryAlertDialog.a(var5, var4);
         if (var4 instanceof AlertDialog) {
            ListView var6 = ((AlertDialog)var4).getListView();
            if (var6 != null) {
               int var3 = var5.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.check_box_size);
               int var2 = GalleryChromeActivity.b(var5, com.alensw.PicFolder.R.attr.colorMenuDrawable);

               for (int var1 = var6.getChildCount() - 1; var1 >= 0; var1--) {
                  View var7 = var6.getChildAt(var1);
                  if (var7 instanceof CheckedTextView) {
                     ModernListPreference.a((CheckedTextView)var7, var3, var2, this.b);
                  }
               }

               var6.setDivider(null);
               var6.setOnHierarchyChangeListener(new StylePreferenceChoice(this, var3, var2));
            }
         }
      } else {
         this.c.postDelayed(this, 16L);
      }
   }
}
