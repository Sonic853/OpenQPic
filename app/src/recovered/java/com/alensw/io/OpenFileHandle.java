package com.alensw.io;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class OpenFileHandle {
   private static Field c;
   private static Constructor d;
   private final Uri a;
   private ParcelFileDescriptor b;

   static {
      try {
         c = FileDescriptor.class.getDeclaredField("descriptor");
         c.setAccessible(true);
         d = ParcelFileDescriptor.class.getDeclaredConstructor(FileDescriptor.class);
         d.setAccessible(true);
      } catch (Throwable var1) {
      }
   }

   public OpenFileHandle(Uri var1, ParcelFileDescriptor var2) {
      this.b = var2;
      this.a = var1;
   }

   public static ParcelFileDescriptor a(ContentResolver var0, Uri var1, boolean var2) {
        try {
            return var0.openFileDescriptor(var1, var2 ? "r" : "rw");
        } catch (FileNotFoundException e) {
            if ("file".equals(var1.getScheme())) {
                return a(var1.getPath(), var2);
            }
            throw com.alensw.compat.LegacyPlatform.propagate(e);
        }
    }

   private static ParcelFileDescriptor a(String var0, boolean var1) {
      try {
         if (VERSION.SDK_INT >= 13) {
            int fd;
            if (var1) {
               FileInputStream in = new FileInputStream(var0);
               fd = c.getInt(in.getFD());
            } else {
               FileOutputStream out = new FileOutputStream(var0);
               fd = c.getInt(out.getFD());
            }
            if (fd != -1) return ParcelFileDescriptor.fromFd(fd);
         }
         throw new FileNotFoundException();
      } catch (java.io.IOException | IllegalAccessException e) {
         throw com.alensw.compat.LegacyPlatform.propagate(e);
      }
   }

   public static OpenFileHandle a(ContentResolver var0, Uri var1) {
      return new OpenFileHandle(var1, a(var0, var1, true));
   }

   public static OpenFileHandle a(File var0, boolean var1) {
      ParcelFileDescriptor var2 = b(var0, var1);
      return new OpenFileHandle(Uri.fromFile(var0), var2);
   }

   public static boolean a(Uri var0) {
      return "file".equals(var0.getScheme());
   }

   public static ParcelFileDescriptor b(File var0, boolean var1) {
      int var2;
      if (var1) {
         var2 = 268435456;
      } else {
         var2 = 939524096;
      }

      ParcelFileDescriptor var3;
      try {
         var3 = ParcelFileDescriptor.open(var0, var2);
      } catch (FileNotFoundException var4) {
         return a(var0.getPath(), var1);
      }

      return var3;
   }

   public boolean a() {
      return "file".equals(this.a.getScheme());
   }

   public FileDescriptor b() {
      if (this.b == null) {
         throw com.alensw.compat.LegacyPlatform.propagate(new FileNotFoundException("file not open"));
      } else {
         return this.b.getFileDescriptor();
      }
   }

   public int c() {
      if (this.b != null) {
         try {
            int var1;
            if (VERSION.SDK_INT >= 12) {
               var1 = this.b.getFd();
            } else {
               var1 = c.getInt(this.b.getFileDescriptor());
            }

            return var1;
         } catch (Throwable var3) {
            var3.printStackTrace();
         }
      }

      return -1;
   }

   public String d() {
      return this.a.getPath();
   }

   public long e() {
      if (this.b != null) {
         try {
            return this.b.getStatSize();
         } catch (Throwable var4) {
         }
      }

      return 0L;
   }

   @Override
   public boolean equals(Object var1) {
      boolean var2;
      if (var1 instanceof OpenFileHandle && this.a.equals(((OpenFileHandle)var1).a)) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public long f() {
      long var1;
      try {
         File var3 = new File(this.a.getPath());
         var1 = var3.lastModified();
      } catch (Throwable var4) {
         var1 = 0L;
      }

      return var1;
   }

   @Override
   protected void finalize() {
      this.h();
   }

   public Uri g() {
      return this.a;
   }

   public void h() {
      if (this.b != null) {
         try {
            this.b.close();
         } catch (Throwable var2) {
         }
      }

      this.b = null;
   }

   @Override
   public int hashCode() {
      return this.a.hashCode();
   }
}
