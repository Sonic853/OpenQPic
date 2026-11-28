package com.alensw.PicFolder;

import android.os.Message;

class DeliverCropResult implements Runnable {
   final Message a;
   final RenderCropResult b;

   DeliverCropResult(RenderCropResult var1, Message var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      if (this.b.d) {
         if (this.b.h.q != null) {
            this.b.h.q.recycle();
         }

         this.b.h.q = null;
      }

      if (this.b.d) {
         this.b.h.a(0);
      }

      this.b.h.s.sendMessage(this.a);
   }
}
