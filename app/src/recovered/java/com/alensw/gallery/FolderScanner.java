package com.alensw.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import com.alensw.concurrent.AsyncExecutor;
import com.alensw.io.IntegerPropertyFile;
import com.alensw.io.StoragePaths;
import com.alensw.jni.JniUtils;
import com.alensw.media.MediaFileTypes;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Future;

public class FolderScanner {
   public static final int REFRESH_DIRECTORIES = 5;
   public static final int DIRECTORIES_REFRESHED = 15;
   public static final String[] a = new String[]{
      "Android/data/com.facebook.orca/files/stickers", "clockworkmod/blobs", "Pictures/cache", "autonavi/mini_mapv3", "BaiduMap/vmp", "taobao/wvcache"
   };
   private static final AsyncExecutor d = new AsyncExecutor(2, 2, Integer.MAX_VALUE, 3);
   private static final AsyncExecutor e = new AsyncExecutor(2, 2, Integer.MAX_VALUE, 3);
   public boolean b;
   public int c = 0;
   private final Context f;
   private final NativeFolderScanner g;
   private final FolderScanStampStore h = new FolderScanStampStore(2048);
   private final FolderScanStampStore i = new FolderScanStampStore(2048);
   private final File j;
   private final File k;
   private final String l;
   private final IntegerPropertyFile m = new IntegerPropertyFile(4096);
   private String n = "";
   private FolderScanWorker o;

   public FolderScanner(Context var1) {
      this.f = var1;
      this.g = new NativeFolderScanner();
      this.j = StoragePaths.a(var1, "folder_cache");
      this.k = StoragePaths.a(var1, "folder_cache2");
      this.l = StoragePaths.b(var1, "others_").getPath();
   }

   private int a(NativeFolderScanner var1, ScannedFolder var2, boolean var3, boolean var4, boolean enhanced) {
        HashMap map;
        int i;
        Integer num;
        String path = var2.getPath();
        if (var4 && var2.h != null && (num = (Integer) this.m.b(path)) != null) {
            if (num.intValue() == var2.e && var2.e != 0) {
                return 0;
            }
            this.m.c(path);
        }
        int i2 = var2.h != null ? 1 : 0;
        if (var3) {
            i2 |= 8;
        }
        int iD = var2.d();
        boolean z3 = (iD == 2 || iD == 4) ? false : true;
        boolean z4 = ScannedFolder.a || var2.f();
        boolean z5 = iD == 7 || iD == 8;
        Future futureSubmit = null;
        if (z3) {
            boolean indexed = z4 && MediaBucketIndex.d(path);
            if (z4 && (!enhanced || indexed)) {
                futureSubmit = d.submit(new QueryFolderMediaTask(this, z5, path));
            }
            if (!indexed) {
                i2 |= 240;
            }
        }
        if (var1.b(var2, i2) < 0) {
            if (enhanced && futureSubmit != null) futureSubmit.cancel(false);
            return 0;
        }
        ArrayList<ScannedMediaFile> arrayList = var2.i;
        ArrayList arrayList2 = var2.h;
        int size = arrayList.size();
        if (size <= 8 && arrayList2 != null) {
            int i3 = size - 1;
            while (i3 >= 0) {
                if (MediaFileTypes.c(((ScannedMediaFile) arrayList.get(i3)).b)) {
                    arrayList.remove(i3);
                    i = size - 1;
                } else {
                    i = size;
                }
                i3--;
                size = i;
            }
        }
        if (var4 && size == 0 && var2.e != 0 && arrayList2 != null && arrayList2.size() == 0) {
            if (this.m.c() < 4096) {
                this.m.a(path, Integer.valueOf(var2.e));
            }
        } else if (size > 0) {
            this.m.c(path);
        }
        if (z3 && z4 && size > 0) {
            try {
                // Unknown directories can still have newly indexed files. Query only once
                // the native pass confirms there is media whose metadata we need.
                if (futureSubmit == null) futureSubmit = d.submit(new QueryFolderMediaTask(this, z5, path));
                map = (HashMap) futureSubmit.get();
            } catch (Throwable th) {
                th.printStackTrace();
                map = null;
            }
            HashMap map2 = map == null ? new HashMap() : map;
            String strE = GalleryUtils.e(path);
            if (z5) {
                for (ScannedMediaFile srcType0110 : arrayList) {
                    long[] jArr = (long[]) map2.get(srcType0110.b);
                    if (jArr != null) {
                        srcType0110.f = jArr[0];
                    } else if (srcType0110.f == 0) {
                        srcType0110.f = JniUtils.fuGetFileSize(strE + srcType0110.b);
                    }
                }
            } else {
                for (ScannedMediaFile srcType0112 : arrayList) {
                    long[] jArr2 = (long[]) map2.get(srcType0112.b);
                    if (jArr2 != null) {
                        srcType0112.d = (int) jArr2[0];
                        srcType0112.e = (int) (jArr2[1] / 1000);
                        if (srcType0112.e == 0) {
                            srcType0112.e = srcType0112.d;
                        }
                    } else if (srcType0112.d == 0) {
                        int iFuGetFileTime = JniUtils.fuGetFileTime(strE + srcType0112.b);
                        srcType0112.d = iFuGetFileTime;
                        srcType0112.e = iFuGetFileTime;
                    }
                }
            }
            map2.clear();
        } else if (enhanced && futureSubmit != null) {
            futureSubmit.cancel(false);
        }
        var2.l();
        return size;
    }

   static int a(FolderScanner var0, NativeFolderScanner var1, ScannedFolder var2, boolean var3, boolean var4, boolean enhanced) {
      return var0.a(var1, var2, var3, var4, enhanced);
   }

   static FolderScanStampStore a(FolderScanner var0) {
      return var0.h;
   }

   static FolderScanStampStore b(FolderScanner var0) {
      return var0.i;
   }

   static File c(FolderScanner var0) {
      return var0.j;
   }

   static AsyncExecutor d() {
      return e;
   }

   static File d(FolderScanner var0) {
      return var0.k;
   }

   static Context e(FolderScanner var0) {
      return var0.f;
   }

   static String f(FolderScanner var0) {
      return var0.n;
   }

   static IntegerPropertyFile g(FolderScanner var0) {
      return var0.m;
   }

   static String h(FolderScanner var0) {
      return var0.l;
   }

   public int a(ScannedFolder var1, boolean var2) {
        if (var1.h != null) {
            var1.h.clear();
        } else {
            var1.h = new ArrayList(128);
        }
        int iA = this.g.a(var1, var2 ? 2 : 0);
        if (iA < 0) {
            return 0;
        }
        return iA;
    }

   public void a() {
      if (this.o == null) {
         this.o = new FolderScanWorker(this, null);
      }

      this.o.b();
   }

   public void a(int var1, SharedPreferences var2) {
      boolean var4 = true;
      if (this.c != var1) {
         this.c = var1;
         this.m.d();
         boolean var3;
         if ((var1 & 1) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         if ((var1 & 2) == 0) {
            var4 = false;
         }

         this.n = NativeFolderScanner.a(var3, var4, var2);
         this.g.a(this.n);
      }
   }

   public void a(int var1, Object var2, Handler var3) {
      if (this.o != null) {
         this.o.b(var1, 0, 0, var2, var3);
      }
   }

   public int b(ScannedFolder var1, boolean var2) {
      return this.a(this.g, var1, var2, false, false);
   }

   public void b() {
      if (this.o != null) {
         this.o.c();
         this.o = null;
      }
   }

   public void c() {
      if (this.o != null) {
         this.o.a(true);
      }
   }
}
