package com.alensw.ui.a;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import com.alensw.ui.view.TitleView;

final class p implements OnClickListener {
   final Context a;
   final int b;
   final ArrayAdapter c;
   final TitleView d;

   p(Context var1, int var2, ArrayAdapter var3, TitleView var4) {
      this.a = var1;
      this.b = var2;
      this.c = var3;
      this.d = var4;
   }

   @Override
   public void onClick(View var1) {
      com.alensw.ui.a.f.a(com.alensw.ui.a.f.a(this.a, this.b, this.c, new com.alensw.ui.a.q(this)));
   }
}
