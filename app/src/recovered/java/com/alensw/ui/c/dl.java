package com.alensw.ui.c;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import com.alensw.PicFolder.CropActivity;
class dl implements com.alensw.ui.activity.o {
   final Bundle a;
   final com.alensw.ui.c.df b;

   dl(com.alensw.ui.c.df var1, Bundle var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(Intent var1, ResolveInfo var2) {
      if (CropActivity.class.getName().equals(var2.activityInfo.name)) {
         var1.setClass(com.alensw.ui.c.df.a(this.b), CropActivity.class);
         if (this.a != null) {
            var1.putExtras(this.a);
         }

         com.alensw.ui.activity.a.a((Activity)com.alensw.ui.c.df.a(this.b), var1, 2);
      } else {
         com.alensw.ui.activity.a.a(com.alensw.ui.c.df.a(this.b), var1, var2);
      }
   }
}
