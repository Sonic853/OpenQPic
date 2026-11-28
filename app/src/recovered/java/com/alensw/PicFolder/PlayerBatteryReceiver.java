package com.alensw.PicFolder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.alensw.ui.shape.BatteryIconShape;
class PlayerBatteryReceiver extends BroadcastReceiver {
   final PlayerActivity a;

   PlayerBatteryReceiver(PlayerActivity var1) {
      this.a = var1;
   }

   @Override
   public void onReceive(Context var1, Intent var2) {
      boolean var6 = false;
      int var4 = var2.getIntExtra("scale", 100);
      if (var4 > 0) {
         int var5 = var2.getIntExtra("level", 0);
         int var3 = var2.getIntExtra("status", 0);
         var4 = var5 * 100 / var4;
         BatteryIconShape var7 = this.a.w;
         if (var3 == 2) {
            var6 = true;
         }

         var7.a(var4, var6);
         if (this.a.m != null) {
            this.a.m.setTitle(Integer.toString(var4) + "%");
            this.a.m.setVisible(true);
         }
      }
   }
}
