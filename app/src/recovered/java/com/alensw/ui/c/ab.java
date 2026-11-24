package com.alensw.ui.c;

import android.content.Intent;
import android.content.pm.ResolveInfo;
class ab implements com.alensw.ui.activity.o {
   final com.alensw.ui.c.z a;

   ab(com.alensw.ui.c.z var1) {
      this.a = var1;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      if ("android.intent.action.SEND_MULTIPLE".equals(var1.getAction())) {
         this.a.C.a(var1);
      } else {
         var1.setType(null);
         var1.setFlags(0);
         var1.removeExtra("android.intent.extra.STREAM");
      }

      com.alensw.ui.activity.a.a(this.a.C, var1, var2);
   }
}
