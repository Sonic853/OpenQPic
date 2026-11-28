package com.alensw.ui.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

class OpenStorageHelp implements OnClickListener {
   final GalleryIntentActivity a;

   OpenStorageHelp(GalleryIntentActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface dialogInterface, int i) {
        GalleryActions.a(this.a, (String) null, "http://www.google.com/search?q=Android+4.4+External+SD+Card");
    }
}
