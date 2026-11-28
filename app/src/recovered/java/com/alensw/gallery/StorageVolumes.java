package com.alensw.gallery;

import android.content.Context;
import android.os.Build.VERSION;
import android.os.storage.StorageManager;
import com.alensw.PicFolder.GalleryStorage;
import com.alensw.PicFolder.QuickApp;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageVolumes {
   public static final String[] a = new String[]{
      "/mnt/emmc", "/mnt/sdcard-ext", "/mnt/sdcard2", "/mnt/external_sd", "/mnt/external_sdcard", "/mnt/flash", "/external1", "/Removable/MicroSD"
   };
   private final String[] b;
   private final String[] c;
   private final String[] d;
   private final String[] e;
   private final String[] f;
   private final String[] g;

   public StorageVolumes(Context var1) {
        ArrayList arrayList = new ArrayList(4);
        HashMap map = new HashMap(4);
        HashMap map2 = new HashMap(4);
        try {
            if (VERSION.SDK_INT >= 30) {
                GalleryStorage.volumes(var1, arrayList, map, map2);
            } else {
                Object[] objArr = (Object[]) StorageManager.class.getMethod("getVolumeList", new Class[0]).invoke((StorageManager) var1.getSystemService("storage"), new Object[0]);
                Class<?> cls = Class.forName("android.os.storage.StorageVolume");
                Method method4 = cls.getMethod("getPath", new Class[0]);
                Method uuid = null;
                Method label = null;
                try {
                    uuid = cls.getMethod("getUuid");
                    label = cls.getMethod("getUserLabel");
                } catch (Throwable ignored) {
                    // Older StorageVolume implementations expose only some of these methods.
                }
                for (Object obj : objArr) {
                    String str = (String) method4.invoke(obj, new Object[0]);
                    if (str != null) {
                        arrayList.add(str);
                        if (uuid != null) {
                            map.put(str, (String) uuid.invoke(obj, new Object[0]));
                        }
                        if (label != null) {
                            map2.put(str, (String) label.invoke(obj, new Object[0]));
                        }
                    }
                }
            }
        } catch (Throwable th3) {
        }
        if (arrayList.size() <= 0) {
            for (String str2 : a) {
                if (new File(str2).exists()) {
                    if (!ScannedFolder.b.equals(str2)) {
                        arrayList.add(str2);
                        break;
                    }
                    break;
                }
            }
            arrayList.add(0, ScannedFolder.b);
        } else if (arrayList.size() > 1 && arrayList.remove(ScannedFolder.b)) {
            arrayList.add(0, ScannedFolder.b);
        }
        int size = arrayList.size();
        this.b = new String[size];
        arrayList.toArray(this.b);
        this.c = new String[size];
        for (int i = 0; i < size; i++) {
            this.c[i] = this.b[i] + "/DCIM";
        }
        String str3 = QuickApp.j == 3 ? new File(new StringBuilder().append(ScannedFolder.b).append("/Camera").toString()).exists() ? "/Camera" : "/DCIM" : "/DCIM/" + QuickApp.l;
        this.d = new String[size];
        for (int i2 = 0; i2 < size; i2++) {
            this.d[i2] = this.b[i2] + str3;
        }
        this.e = new String[size];
        for (int i3 = 0; i3 < size; i3++) {
            this.e[i3] = this.b[i3] + "/Android/data";
        }
        this.f = new String[size];
        this.g = new String[size];
        for (int i4 = 0; i4 < size; i4++) {
            this.f[i4] = (String) map.get(this.b[i4]);
            this.g[i4] = (String) map2.get(this.b[i4]);
        }
        if (this.f[0] == null) {
            this.f[0] = "primary";
        }
    }

   public boolean a(String var1) {
      boolean var5 = false;
      String[] var6 = this.b;
      int var3 = var6.length;
      int var2 = 0;

      boolean var4;
      while (true) {
         var4 = var5;
         if (var2 >= var3) {
            break;
         }

         if (GalleryUtils.a(var1, var6[var2])) {
            var4 = true;
            break;
         }

         var2++;
      }

      return var4;
   }

   public final String[] a() {
      return this.b;
   }

   public boolean b(String var1) {
      boolean var5 = false;
      String[] var6 = this.b;
      int var3 = var6.length;
      int var2 = 0;

      boolean var4;
      while (true) {
         var4 = var5;
         if (var2 >= var3) {
            break;
         }

         if (GalleryUtils.c(var6[var2], var1)) {
            var4 = true;
            break;
         }

         var2++;
      }

      return var4;
   }

   public int c(String var1) {
      byte var5 = 0;
      byte var4 = 0;
      String[] var7 = this.c;
      int var6 = var7.length;
      int var3 = 0;

      int var2;
      while (true) {
         var2 = var5;
         if (var3 >= var6) {
            break;
         }

         if (GalleryUtils.c(var7[var3], var1)) {
            var7 = this.d;
            var3 = var7.length;

            for (var2 = var4; var2 < var3; var2++) {
               if (GalleryUtils.a(var7[var2], var1)) {
                  var2 = (byte)2;
                  return var2;
               }
            }

            var2 = 1;
            break;
         }

         var3++;
      }

      return var2;
   }

   public String d(String var1) {
      String[] var4 = this.b;
      int var2 = 0;

      while (true) {
         if (var2 >= var4.length) {
            var1 = null;
            break;
         }

         String var3 = this.f[var2];
         if (var3 == null ? var1 == null : var3.equals(var1)) {
            var1 = var4[var2];
            break;
         }

         var2++;
      }

      return var1;
   }

   public String[] e(String var1) {
        String[] strArr = this.b;
        for (int i = 0; i < strArr.length; i++) {
            String str2 = strArr[i];
            if (GalleryUtils.c(str2, var1)) {
                int length = str2.length();
                if (var1.length() > length && var1.charAt(length) == File.separatorChar) {
                    length++;
                }
                return new String[]{this.f[i], var1.substring(length)};
            }
        }
        return null;
    }

   public String f(String var1) {
      String[] var3 = this.b;
      int var2 = 0;

      while (true) {
         if (var2 >= var3.length) {
            var1 = null;
            break;
         }

         if (GalleryUtils.c(var3[var2], var1)) {
            var1 = this.g[var2];
            break;
         }

         var2++;
      }

      return var1;
   }

   public boolean g(String var1) {
      boolean var5 = false;
      String[] var6 = this.e;
      int var3 = var6.length;
      int var2 = 0;

      boolean var4;
      while (true) {
         var4 = var5;
         if (var2 >= var3) {
            break;
         }

         if (GalleryUtils.c(var6[var2], var1)) {
            var4 = true;
            break;
         }

         var2++;
      }

      return var4;
   }
}
