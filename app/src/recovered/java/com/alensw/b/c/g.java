package com.alensw.b.c;

import android.os.Environment;
import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public abstract class g {
   private static final String e = Environment.getExternalStorageDirectory().getPath();
   private File a;
   private boolean b;
   private boolean c;
   private final ConcurrentHashMap d;

   public g(int var1) {
      this.d = new ConcurrentHashMap(var1);
      this.c = true;
   }

   public static String a(String var0, int var1, String var2, String var3) {
      int var4 = var2.length();
      if (var1 >= var4 && var0.substring(0, var4).equals(var2)) {
         var0 = var3 + var0.substring(var4);
      } else {
         var0 = null;
      }

      return var0;
   }

   private boolean a(File var1, ArrayList var2) {
        try {
            boolean zDelete = var1.delete();
            if (var2.isEmpty()) {
                return zDelete;
            }
            FileOutputStream fileOutputStream = new FileOutputStream(var1);
            DataOutputStream dataOutputStream = new DataOutputStream(fileOutputStream);
            dataOutputStream.writeInt(2);
            dataOutputStream.writeUTF(a());
            dataOutputStream.writeInt(var2.size());
            Iterator it = var2.iterator();
            while (it.hasNext()) {
                Entry map$Entry = (Entry) it.next();
                String strF = (String) map$Entry.getKey();
                if (this.c) {
                    strF = f(strF);
                }
                Object value = map$Entry.getValue();
                if (strF != null && value != null) {
                    dataOutputStream.writeUTF(strF);
                    a(dataOutputStream, value);
                }
            }
            dataOutputStream.close();
            fileOutputStream.close();
            return true;
        } catch (Throwable th) {
            Log.e("PropertyFile", "save: ", th);
            return false;
        }
    }

   public static String e(String var0) {
      if (var0 != null) {
         String var1 = a(var0, var0.length(), "${SD}", e);
         if (var1 != null) {
            var0 = var1;
         }
      }

      return var0;
   }

   public static String f(String var0) {
      if (var0 != null) {
         String var1 = a(var0, var0.length(), e, "${SD}");
         if (var1 != null) {
            var0 = var1;
         }
      }

      return var0;
   }

   public Object a(String var1, Object var2) {
      Object var3 = this.d.put(var1, var2);
      if (var3 != var2) {
         if (var3 == null || var2 == null) {
            this.b = true;
         } else if (!var3.equals(var2)) {
            this.b = true;
         }
      }

      return var3;
   }

   protected abstract String a();

   protected abstract void a(DataOutputStream var1, Object var2);

   public void a(boolean var1) {
      this.b = var1;
   }

   public boolean a(File var1) {
        d();
        this.a = var1;
        this.b = false;
        try {
            ConcurrentHashMap concurrentHashMap = this.d;
            FileInputStream fileInputStream = new FileInputStream(var1);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            int i = dataInputStream.readInt();
            String utf = dataInputStream.readUTF();
            if (i == 2 && a().equals(utf)) {
                int i2 = dataInputStream.readInt();
                for (int i3 = 0; i3 < i2; i3++) {
                    String utf2 = dataInputStream.readUTF();
                    if (this.c) {
                        utf2 = e(utf2);
                    }
                    Object objB = b(dataInputStream);
                    if (utf2 != null && utf2.length() > 0 && objB != null) {
                        concurrentHashMap.put(utf2, objB);
                    }
                }
            }
            dataInputStream.close();
            fileInputStream.close();
            return concurrentHashMap.size() > 0;
        } catch (FileNotFoundException e2) {
            return false;
        } catch (Throwable th) {
            Log.e("PropertyFile", "load: ", th);
            return false;
        }
    }

   public boolean a(String var1) {
      return this.d.containsKey(var1);
   }

   protected abstract Object b(DataInputStream var1);

   public Object b(String var1) {
      return this.d.get(var1);
   }

   public void b(File var1) {
      if (var1 != null) {
         this.a = var1;
      }

      this.h();
   }

   public boolean b() {
      return this.d.isEmpty();
   }

   public int c() {
      return this.d.size();
   }

   public Object c(String var1) {
      Object var2 = this.d.remove(var1);
      if (var2 != null) {
         this.b = true;
      }

      return var2;
   }

   public void d() {
      boolean var1;
      if (this.d.size() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.b = var1;
      this.d.clear();
   }

   public boolean d(String var1) {
      boolean var2;
      if (var1 != null && this.a(new File(var1))) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean e() {
      return this.b;
   }

   public Set f() {
      return this.d.entrySet();
   }

   public HashMap g() {
      return new HashMap(this.d);
   }

   public void h() {
      if (this.a != null) {
         try {
            File var1 = new File(this.a.getPath());
            ArrayList var2 = new ArrayList(this.d.size());
            var2.addAll(this.d.entrySet());
            if (this.a(var1, var2)) {
               this.b = false;
            }
         } catch (Throwable var3) {
         }
      }
   }
}
