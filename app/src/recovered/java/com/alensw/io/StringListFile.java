package com.alensw.io;

import android.util.Log;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class StringListFile implements Iterable {
   private File a;
   private boolean b;
   private final CopyOnWriteArrayList c = new CopyOnWriteArrayList();

   public int a(Object var1) {
      return this.c.indexOf(var1);
   }

   public Object a(int var1) {
      return this.c.get(var1);
   }

   public Object a(int var1, Object var2) {
      Object var3 = this.c.set(var1, var2);
      if (var3 != var2) {
         if (var3 == null || var2 == null) {
            this.b = true;
         } else if (!var3.equals(var2)) {
            this.b = true;
         }
      }

      return var3;
   }

   public abstract String a();

   public abstract void a(DataOutputStream var1, Object var2);

   public void a(Collection var1) {
      this.c.addAll(var1);
      this.b = true;
   }

   public boolean a(File var1) {
        e();
        this.a = var1;
        this.b = false;
        try {
            LinkedList linkedList = new LinkedList();
            FileInputStream fileInputStream = new FileInputStream(var1);
            DataInputStream dataInputStream = new DataInputStream(fileInputStream);
            int i = dataInputStream.readInt();
            String utf = dataInputStream.readUTF();
            if (i == 1 && a().equals(utf)) {
                int i2 = dataInputStream.readInt();
                for (int i3 = 0; i3 < i2; i3++) {
                    Object objB = b(dataInputStream);
                    if (objB != null) {
                        linkedList.add(objB);
                    }
                }
            }
            this.c.addAll(linkedList);
            dataInputStream.close();
            fileInputStream.close();
            return linkedList.size() > 0;
        } catch (FileNotFoundException e) {
            return false;
        } catch (Throwable th) {
            Log.e("ListFile", "load: ", th);
            return false;
        }
    }

   public abstract Object b(DataInputStream var1);

   public void b(Object var1) {
      this.c.add(var1);
      this.b = true;
   }

   public boolean b() {
      return this.c.isEmpty();
   }

   public int c() {
      return this.c.size();
   }

   public boolean c(Object var1) {
      boolean var2 = this.c.remove(var1);
      if (var2) {
         this.b = true;
      }

      return var2;
   }

   public List d() {
      Object[] var4 = this.c.toArray();
      ArrayList var3 = new ArrayList(var4.length);
      int var2 = var4.length;

      for (int var1 = 0; var1 < var2; var1++) {
         var3.add(var4[var1]);
      }

      return var3;
   }

   public void e() {
      boolean var1;
      if (this.c.size() > 0) {
         var1 = true;
      } else {
         var1 = false;
      }

      this.b = var1;
      this.c.clear();
   }

   public boolean f() {
      return this.b;
   }

   public boolean g() {
      if (this.a != null) {
         try {
            boolean var3 = this.a.delete();
            if (this.b()) {
               return var3;
            }
         } catch (Throwable var10) {
            Log.e("ListFile", "save: ", var10);
            return false;
         }

         int var2;
         Object[] var4;
         DataOutputStream var5;
         FileOutputStream var6;
         try {
            var4 = this.c.toArray();
            var6 = new FileOutputStream(this.a);
            var5 = new DataOutputStream(var6);
            var5.writeInt(1);
            var5.writeUTF(this.a());
            var5.writeInt(var4.length);
            var2 = var4.length;
         } catch (Throwable var9) {
            Log.e("ListFile", "save: ", var9);
            return false;
         }

         for (int var1 = 0; var1 < var2; var1++) {
            try {
               this.a(var5, var4[var1]);
            } catch (Throwable var8) {
               Log.e("ListFile", "save: ", var8);
               return false;
            }
         }

         try {
            var5.close();
            var6.close();
         } catch (Throwable var7) {
            Log.e("ListFile", "save: ", var7);
            return false;
         }

         return true;
      } else {
         return false;
      }
   }

   @Override
   public Iterator iterator() {
      return this.c.iterator();
   }
}
