package com.alensw.ui.page;

import android.content.Intent;

class DeliverDestinationFolder implements Runnable {
   final FolderIntentCallback a;
   final Intent b;
   final MediaActionPage c;

   DeliverDestinationFolder(MediaActionPage var1, FolderIntentCallback var2, Intent var3) {
      this.c = var1;
      this.a = var2;
      this.b = var3;
   }

   @Override
   public void run() {
      this.a.a(this.b);
   }
}
