package com.alensw.gallery;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Build.VERSION;
import com.alensw.bean.CommonFolder;
import com.alensw.concurrent.CancellableCallable;
import java.util.Set;

public class QueryFolderTask implements CancellableCallable {
   private String a = "QueryCursorTask";
   private final boolean b;
   private final Set c;
   private final boolean d;
   private final boolean e;
   private final String f;
   private final FolderQueryCallback g;
   private final CommonFolder h;
   private final ContentResolver i;
   private final Handler j;
   private volatile boolean k;

   public QueryFolderTask(ContentResolver var1, CommonFolder var2, boolean var3, Set var4, Handler var5, FolderQueryCallback var6) {
      this.b = var3;
      this.c = var4;
      int var7 = var2.c();
      if ((268435456 & var7) == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.d = var3;
      if ((536870912 & var7) == 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.e = var3;
      this.i = var1;
      this.j = var5;
      this.g = var6;
      this.h = var2.a(false);
      var7 = var2.l();
      if (this.d && var7 == 3) {
         this.f = "datetaken ASC";
      } else if (this.d && var7 == 1) {
         this.f = "datetaken DESC";
      } else {
         this.f = null;
      }
   }

   static ContentResolver a(QueryFolderTask var0) {
      return var0.i;
   }

   static boolean b(QueryFolderTask var0) {
      return var0.e;
   }

   static CommonFolder c(QueryFolderTask var0) {
      return var0.h;
   }

   static String d(QueryFolderTask var0) {
      return var0.a;
   }

   static FolderQueryCallback e(QueryFolderTask var0) {
      return var0.g;
   }

   static Handler f(QueryFolderTask var0) {
      return var0.j;
   }

   @Override
   public void a() {
      this.k = true;
   }

   @Override
   public void b() {
   }

   public Void c() {
      Object[] var2;
      if (this.e) {
         var2 = this.h.a(this.i);
      } else {
         var2 = null;
      }

      Cursor var4 = this.h.a(this.i, this.f);
      boolean var1;
      if (var4 != null) {
         if (this.k) {
            return null;
         }

         Bundle var3 = var4.getExtras();
         if (var3 != null && var3.getBoolean("loading")) {
            var1 = true;
         } else {
            var1 = false;
         }

         if (var1) {
            Uri var5;
            if (VERSION.SDK_INT >= 19) {
               var5 = var4.getNotificationUri();
            } else {
               var5 = this.h.q();
            }

            this.i.registerContentObserver(var5, false, new FolderQueryObserver(this, this.j, var2));
         }

         if (this.b) {
            this.h.a(var4, this.c);
         } else {
            this.h.b(var4, this.c);
         }

         var4.close();
      } else {
         var1 = false;
      }

      if (!this.k) {
         if (this.d && !this.h.m()) {
            this.h.s();
         }

         if (!this.k) {
            this.j.post(new DeliverFolderQueryResult(this, var4, var1));
         }
      }

      return null;
   }

   @Override
   public Object call() {
      return this.c();
   }
}
