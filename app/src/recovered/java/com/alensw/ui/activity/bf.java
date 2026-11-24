package com.alensw.ui.activity;

import android.view.View;
import android.view.View.OnClickListener;

class bf implements OnClickListener {
   final PathListActivity a;

   bf(PathListActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.a.remove(var1.getTag());
      this.a.d = true;
      this.a.b.notifyDataSetChanged();
   }
}
