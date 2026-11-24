package com.alensw.a;

import android.content.Context;
import android.net.Uri;
import java.io.File;

public class n {
   private static com.alensw.a.n b;
   private final com.alensw.b.c.h a = new com.alensw.b.c.h(16);

   public static synchronized com.alensw.a.n a(Context context) {
      if (b == null) {
         b = new com.alensw.a.n();
         b.b(context);
      }
      return b;
   }

   private void b(Context var1) {
      File var2 = com.alensw.b.l.b.b(var1, "folder_covers");
      this.a.a(var2);
   }

   public String a(Uri var1) {
      String var2;
      if ("file".equals(var1.getScheme())) {
         var2 = this.a(var1.getPath());
      } else {
         var2 = this.a(var1.toString());
      }

      return var2;
   }

   public String a(String var1) {
      return (String)this.a.b(var1);
   }

   public void a(String var1, String var2) {
      if (var2 != null) {
         this.a.a(var1, var2);
      } else {
         this.a.c(var1);
      }

      this.a.h();
   }
}
