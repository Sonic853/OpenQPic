package com.alensw.ui.page;

import android.widget.TextView;
import com.alensw.location.GeocodeCallback;
import com.alensw.location.PlaceAddress;
class SetLocationDescription implements GeocodeCallback {
   final TextView a;
   final MediaDetails b;

   SetLocationDescription(MediaDetails var1, TextView var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(PlaceAddress var1) {
      if (var1 != null) {
         try {
            this.a.setText(var1.a(MediaDetails.a(this.b)));
         } catch (Throwable var2) {
         }
      }
   }
}
