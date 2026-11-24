package com.alensw.a;

import android.content.Context;
import java.io.File;
import java.util.ArrayList;

public class o {
   private static com.alensw.a.o b;
   private final com.alensw.b.c.a a = new com.alensw.b.c.a(16);

   public static synchronized com.alensw.a.o a(Context context) {
      if (b == null) {
         b = new com.alensw.a.o();
         b.b(context);
      }
      return b;
   }

   private void b(Context var1) {
      File var2 = com.alensw.b.l.b.b(var1, "folder_list");
      this.a.a(var2);
   }

   public String a() {
      ArrayList var8 = this.a(false);
      int var5 = var8.size();
      String var6;
      if (var5 == 0) {
         var6 = null;
      } else {
         String var7 = (String)var8.get(0);
         var6 = var7;
         if (var5 != 1) {
            StringBuilder var9 = new StringBuilder();
            int var4 = var7.length();
            int var2 = var7.indexOf(File.separatorChar);

            while (var2 >= 0 && var2 < var4 - 1) {
               int var1 = var7.indexOf(File.separatorChar, var2 + 1);
               if (var1 == -1) {
                  var1 = var4;
               }

               var6 = var7.substring(var2, var1);

               for (int var3 = 1; var3 < var5; var3++) {
                  if (!((String)var8.get(var3)).startsWith(var6, var2)) {
                     if (var9.length() > 0) {
                        var6 = var9.toString();
                     } else {
                        var6 = File.separator;
                     }

                     return var6;
                  }
               }

               var9.append(var6);
               var2 = var1;
            }

            if (var9.length() > 0) {
               var6 = var9.toString();
            } else {
               var6 = File.separator;
            }
         }
      }

      return var6;
   }

   public synchronized ArrayList a(boolean param1) {
        ArrayList arrayList;
        arrayList = new ArrayList(this.a.c());
        for (Object value : this.a.f()) {
            java.util.Map.Entry map$Entry = (java.util.Map.Entry)value;
            if (((Boolean) map$Entry.getValue()).booleanValue() == param1) {
                arrayList.add(map$Entry.getKey());
            }
        }
        return arrayList;
    }

   public synchronized void a(com.alensw.a.e folder) {
      this.a.a(folder.getPath(), true);
      this.a.h();
   }

   public synchronized void a(ArrayList param1, boolean param2) {
        ArrayList arrayList2 = new ArrayList(this.a.c());
        for (Object value : this.a.f()) {
            java.util.Map.Entry map$Entry = (java.util.Map.Entry)value;
            if (((Boolean) map$Entry.getValue()).booleanValue() != param2) {
                arrayList2.add(map$Entry.getKey());
            }
        }
        this.a.d();
        java.util.Iterator it = arrayList2.iterator();
        while (it.hasNext()) {
            this.a.a((String) it.next(), Boolean.valueOf(!param2));
        }
        java.util.Iterator it2 = param1.iterator();
        while (it2.hasNext()) {
            this.a.a((String) it2.next(), Boolean.valueOf(param2));
        }
        this.a.h();
    }
}
