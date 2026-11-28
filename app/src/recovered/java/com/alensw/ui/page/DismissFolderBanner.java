package com.alensw.ui.page;

import android.view.View;
import android.view.View.OnClickListener;

class DismissFolderBanner implements OnClickListener {
   final MediaFolderPage a;

   DismissFolderBanner(MediaFolderPage var1) {
      this.a = var1;
   }

   @Override
   public void onClick(View var1) {
      this.a.g.setVisibility(8);
      this.a.g.startAnimation(this.a.C.c(false));
   }
}
