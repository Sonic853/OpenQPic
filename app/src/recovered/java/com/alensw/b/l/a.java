package com.alensw.b.l;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;

public class a {
   private a() {}

   public static void a(Context var0, int var1) {
      try {
         ((NotificationManager)var0.getSystemService("notification")).cancel(var1);
      } catch (Throwable var2) {
         var2.printStackTrace();
      }
   }

   public static void a(Context var0, int var1, boolean var2, int var3, int var4, int var5, CharSequence var6, PendingIntent var7) {
      Object var9 = null;
      Bitmap var8 = (Bitmap)var9;
      if (var4 != 0) {
         var8 = (Bitmap)var9;
         if (VERSION.SDK_INT >= 11) {
            try {
               var8 = ((BitmapDrawable)var0.getResources().getDrawable(com.alensw.PicFolder.R.drawable.ic_about).mutate()).getBitmap();
            } catch (Throwable var10) {
               var10.printStackTrace();
               var8 = (Bitmap)var9;
            }
         }
      }

      a(var0, var1, var2, var3, var8, var0.getString(var5), var6, var7);
   }

   @TargetApi(11)
   public static void a(Context var0, int var1, boolean var2, int var3, Bitmap var4, CharSequence var5, CharSequence var6, PendingIntent var7) {
      boolean var8 = true;
      Notification var12;
      if (VERSION.SDK_INT >= 11) {
         Builder var9 = new Builder(var0);
         var9.setSmallIcon(var3);
         var9.setLargeIcon(var4);
         var9.setContentTitle(var5);
         var9.setContentText(var6);
         var9.setTicker(var6);
         var9.setOnlyAlertOnce(true);
         var9.setAutoCancel(var2);
         if (var7 != null) {
            var2 = var8;
         } else {
            var2 = false;
         }

         var9.setOngoing(var2);
         var9.setContentIntent(var7);
         if (VERSION.SDK_INT >= 16) {
            var12 = var9.build();
         } else {
            var12 = var9.getNotification();
         }
      } else {
         Notification var14 = new Notification();
         var14.icon = var3;
         var14.flags = 8;
         if (var2) {
            var14.flags |= 16;
         }

         if (var7 != null) {
            var14.flags |= 2;
         }

         var14.tickerText = var6;
         PendingIntent var13 = var7;
         if (var7 == null) {
            var13 = PendingIntent.getBroadcast(var0, 0, new Intent(), PendingIntent.FLAG_IMMUTABLE);
         }

         try {
            Notification.class.getMethod("setLatestEventInfo", Context.class, CharSequence.class,
               CharSequence.class, PendingIntent.class).invoke(var14, var0, var5, var6, var13);
         } catch (java.lang.reflect.InvocationTargetException e) {
            throw com.alensw.compat.LegacyPlatform.propagate(e.getCause());
         } catch (NoSuchMethodException | IllegalAccessException e) {
            throw com.alensw.compat.LegacyPlatform.propagate(e);
         }
         var12 = var14;
      }

      try {
         ((NotificationManager)var0.getSystemService("notification")).notify(var1, var12);
      } catch (Throwable var10) {
         var10.printStackTrace();
      }
   }
}
