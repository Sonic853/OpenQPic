package com.alensw.gallery;

import android.os.Handler;
import android.os.Process;
import com.alensw.PicFolder.GalleryStorage;
import com.alensw.PicFolder.QuickApp;
import com.alensw.concurrent.QueuedWorker;
import com.alensw.concurrent.WorkItem;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

class FolderScanWorker extends QueuedWorker {
   final FolderScanner a;
   private Handler b;
   private FolderScanFilters c;
   private FolderSortSettings d;
   private HashMap e;
   private String[] f;
   private String[] g;
   private final ArrayList h;
   private StorageVolumes storage;

   private FolderScanWorker(FolderScanner var1) {
      this.a = var1;
      this.h = new ArrayList(FolderScanner.a.length);
   }

   FolderScanWorker(FolderScanner var1, QueryFolderMediaTask var2) {
      this(var1);
   }

   static FolderScanFilters a(FolderScanWorker var0) {
      return var0.c;
   }

   static HashMap a(FolderScanWorker var0, HashMap var1) {
      var0.e = var1;
      return var1;
   }

   static String[] a(FolderScanWorker var0, String[] var1) {
      var0.f = var1;
      return var1;
   }

   static ArrayList b(FolderScanWorker var0) {
      return var0.h;
   }

   static String[] b(FolderScanWorker var0, String[] var1) {
      var0.g = var1;
      return var1;
   }

   static HashMap c(FolderScanWorker var0) {
      return var0.e;
   }

   static String[] d(FolderScanWorker var0) {
      return var0.f;
   }

   static String[] e(FolderScanWorker var0) {
      return var0.g;
   }

   static FolderSortSettings f(FolderScanWorker var0) {
      return var0.d;
   }

   @Override
   protected WorkItem a(int var1, int var2, int var3, Object... var4) {
      FolderScanRequest var5 = new FolderScanRequest(this, null);
      var5.a = var1;
      var5.b = var4[0];
      var5.c = (Handler)var4[1];
      var5.d = this.a.b;
      var5.enhanced = GalleryStorage.enhancedScan(FolderScanner.e(this.a));
      if (var5.a != 4) {
         FolderScanRequest.a(var5, new NativeFolderScanner());
         FolderScanRequest.a(var5).a(FolderScanner.f(this.a));
      }

      return var5;
   }

   @Override
   public void a() {
      Process.setThreadPriority(3);
      this.c = FolderScanFilters.a(FolderScanner.e(this.a));
      this.d = FolderSortSettings.a(FolderScanner.e(this.a));
      FolderCovers.a(FolderScanner.e(this.a));
      refreshDefaultExclusions();
   }

   void refreshDefaultExclusions() {
      if (storage == QuickApp.p) return;
      storage = QuickApp.p;
      this.h.clear();
      for (String var7 : storage.a()) {
         for (String var8 : FolderScanner.a) {
            var8 = var7 + File.separatorChar + var8;
            if (new File(var8).exists()) {
               this.h.add(var8);
            }
         }
      }

      ScannedFolder var9 = new ScannedFolder(ScannedFolder.b + "/tencent/MicroMsg", 0);
      if (this.a.a(var9, false) > 0) {
         for (ScannedFolder var12 : var9.h) {
            String var11 = var12.getName();
            if (var11.length() > 16 && GalleryUtils.a(var11)) {
               this.h.add(var12.getPath());
               break;
            }
         }
      }
   }

   void invalidateDirectory(String path) {
      FolderScanner.a(this.a).c(path);
      FolderScanner.b(this.a).c(path);
      FolderScanner.g(this.a).c(path);
   }

   @Override
   protected void a(WorkItem var1) {
      FolderScanRequest var2 = (FolderScanRequest)var1;
      var2.e = true;
      if (FolderScanRequest.a(var2) != null) {
         FolderScanRequest.a(var2).a(true);
      }
   }

   @Override
   protected int b(WorkItem var1) {
        FolderScanRequest srcType0128 = (FolderScanRequest) var1;
        this.b = srcType0128.c;
        switch (srcType0128.a) {
            case 1:
                this.b.obtainMessage(10, srcType0128.a, 0).sendToTarget();
                FolderScanRequest.b(srcType0128);
                boolean z = !srcType0128.e;
                if (!z) {
                    this.b.removeMessages(12);
                }
                this.b.obtainMessage(11, srcType0128.a, z ? 1 : 0).sendToTarget();
                break;
            case 2:
            case 3:
                boolean z2 = srcType0128.a == 3;
                if (z2 && FolderScanner.g(this.a).b()) {
                    FolderScanner.g(this.a).d(FolderScanner.h(this.a) + this.a.c);
                }
                this.b.obtainMessage(10, srcType0128.a, 0).sendToTarget();
                FolderScanRequest.c(srcType0128);
                boolean z3 = !srcType0128.e;
                if (!z3) {
                    this.b.removeMessages(12);
                }
                this.b.obtainMessage(11, srcType0128.a, z3 ? 1 : 0).sendToTarget();
                if (z2 && z3 && FolderScanner.g(this.a).e()) {
                    FolderScanner.g(this.a).h();
                }
                break;
             case 4:
                 String str = (String) srcType0128.b;
                 if (str != null) {
                     invalidateDirectory(str);
                } else {
                    FolderScanner.a(this.a).d();
                    FolderScanner.b(this.a).d();
                    FolderScanner.g(this.a).d();
                    GalleryUtils.c(FolderScanner.c(this.a));
                    GalleryUtils.c(FolderScanner.d(this.a));
                    for (int i = 1; i <= 3; i++) {
                        GalleryUtils.i(FolderScanner.h(this.a) + i);
                    }
                }
                FolderScanner.a(this.a).h();
                FolderScanner.b(this.a).h();
                 FolderScanner.g(this.a).h();
                 break;
             case FolderScanner.REFRESH_DIRECTORIES:
                 ArrayList<ScannedFolder> directories = srcType0128.refreshDirectories();
                 this.b.obtainMessage(FolderScanner.DIRECTORIES_REFRESHED, 0,
                         srcType0128.e ? 0 : 1, directories).sendToTarget();
                 break;
        }
        this.b = null;
        return 0;
    }
}
