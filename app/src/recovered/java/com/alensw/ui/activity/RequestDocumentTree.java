package com.alensw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;

class RequestDocumentTree implements OnClickListener {
   final GalleryIntentActivity a;

   RequestDocumentTree(GalleryIntentActivity var1) {
      this.a = var1;
   }

   @Override
   public void onClick(DialogInterface var1, int var2) {
      Intent var3 = new Intent("android.intent.action.OPEN_DOCUMENT_TREE");
      var3.putExtra("android.content.extra.SHOW_ADVANCED", true);
      GalleryActions.a(this.a, var3, 4);
   }
}
