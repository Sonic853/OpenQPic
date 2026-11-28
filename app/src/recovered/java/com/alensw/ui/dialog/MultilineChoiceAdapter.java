package com.alensw.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

final class MultilineChoiceAdapter extends ArrayAdapter {
   MultilineChoiceAdapter(Context var1, int var2, List var3) {
      super(var1, var2, var3);
   }

   @Override
   public View getDropDownView(int var1, View var2, ViewGroup var3) {
      var2 = super.getDropDownView(var1, var2, var3);
      if (var2 instanceof TextView) {
         ((TextView)var2).setSingleLine(false);
      }

      return var2;
   }
}
