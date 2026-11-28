package com.alensw.ui.dialog;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import com.alensw.ui.view.TitleView;

final class OpenChoiceList implements OnClickListener {
   final Context a;
   final int b;
   final ArrayAdapter c;
   final TitleView d;

   OpenChoiceList(Context var1, int var2, ArrayAdapter var3, TitleView var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   @Override
   public void onClick(View var1) {
      GalleryDialogs.a(GalleryDialogs.a(this.a, this.b, this.c, new SetSelectedChoice(this)));
   }
}
