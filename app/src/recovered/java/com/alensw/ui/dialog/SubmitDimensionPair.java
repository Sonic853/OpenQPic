package com.alensw.ui.dialog;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.EditText;
import com.alensw.utils.GalleryUtils;
final class SubmitDimensionPair implements OnClickListener {
   final EditText a;
   final EditText b;
   final DimensionPairCallback c;

   SubmitDimensionPair(EditText var1, EditText var2, DimensionPairCallback var3) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      if (var2 == -1) {
         var2 = GalleryUtils.a(this.a.getText().toString(), 0);
         int var3 = GalleryUtils.a(this.b.getText().toString(), 0);
         this.c.a(var2, var3);
      }
   }
}
