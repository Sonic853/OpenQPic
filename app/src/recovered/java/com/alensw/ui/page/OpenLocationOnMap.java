package com.alensw.ui.page;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.alensw.ui.activity.GalleryActions;
class OpenLocationOnMap implements OnClickListener {
   final float[] a;
   final TextView b;
   final MediaDetails c;

   OpenLocationOnMap(MediaDetails var1, float[] var2, TextView var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void onClick(View var1) {
      GalleryActions.a(MediaDetails.a(this.c), this.a[0], this.a[1], this.b);
   }
}
