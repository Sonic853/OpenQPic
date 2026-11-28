package com.alensw.gallery;

import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import com.alensw.concurrent.WorkItem;
import com.alensw.io.IntegerPropertyFile;
import com.alensw.jni.JniUtils;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class FolderScanRequest extends WorkItem {
   public int a;
   public Object b;
   public Handler c;
   public boolean d;
   public volatile boolean e;
   final FolderScanWorker f;
   private NativeFolderScanner h;
   private final String i;
   boolean enhanced;
   private boolean prescanning;
   private Map<String, Prescan> prescans;
   private Set<String> walked;
   private int reusedPrescans;

   private static final class Prescan {
      final int modified;
      final ArrayList<ScannedFolder> children;

      Prescan(ScannedFolder folder) {
         modified = folder.e;
         children = folder.h;
      }
   }

   private FolderScanRequest(FolderScanWorker var1) {
      this.f = var1;
      this.i = File.separator + "cache";
   }

   FolderScanRequest(FolderScanWorker var1, QueryFolderMediaTask var2) {
      this(var1);
   }

   private int a(ScannedFolder var1) {
      ArrayList var4 = new ArrayList(128);
      Stack var5 = new Stack();
      var5.push(var1);
      int var2 = 0;

      while (!this.e) {
         while (var5.size() > 0) {
             ScannedFolder var6 = (ScannedFolder)var5.pop();
             String var7 = var6.getPath();
             if ((!var7.endsWith(this.i) || !QuickApp.p.g(var7)) && !this.a(var7, true, true) && this.a(var7, false)) {
                if (walked != null && !walked.add(var7)) continue;
                if (prescans != null) {
                   Prescan earlier = prescans.remove(var7);
                   if (earlier != null && earlier.modified == (var6.e != 0 ? var6.e : JniUtils.fuGetFileTime(var7))) {
                      var5.addAll(earlier.children);
                      reusedPrescans++;
                      continue;
                   }
                }
                var4.add(var6);
            }
         }

         if (var4.isEmpty() || this.e) {
            break;
         }

         int var3 = this.a(var4, false, false, var5);
         var4.clear();
         var2 += var3;
      }

      return var2;
   }

   boolean keepPrescan(ScannedFolder folder, int mediaCount) {
      if (prescans == null) return true;
      // The scan has already checked ancestor .nomedia when it found media. A
      // cached path can bypass a hidden parent that the recursive pass would prune.
      if (mediaCount > 0 && !this.d && folder.a(false) && !QuickApp.p.a(folder.getPath())) return false;
      if (folder.h != null) prescans.put(folder.getPath(), new Prescan(folder));
      return true;
   }

   private int a(ScannedFolder var1, boolean var2) {
      int var3 = 0;
      String var7 = var1.getPath();
      boolean var6 = QuickApp.p.a(var7);
      boolean var5;
      if (!this.d) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var6) {
         var5 = false;
         var2 = false;
      }

      var1.n();
      // Enhanced full scans remove albums they don't report. An unchanged
      // cached leaf still needs to be scanned so it can be reported.
      boolean rebuildAlbums = enhanced && (this.a == 2 || this.a == 3);
      if (!var2 || rebuildAlbums || this.a(var7, var1.e)) {
         var1.d = FolderScanWorker.f(this.f).a(var1);
         if (var1.h == null) {
            var1.h = new ArrayList(64);
         } else {
            var1.h.clear();
         }

          int var4 = FolderScanner.a(this.f.a, this.h, var1, var5, var2, enhanced && !prescanning);
         var3 = var4;
         if (var2) {
            var3 = var4;
            if (var4 > 0) {
               if (var1.a(true)) {
                  var3 = var4;
                  if (this.d) {
                     FolderScanner.b(this.f.a).a(var1);
                     var3 = var4;
                  }
               } else {
                  FolderScanner.a(this.f.a).a(var1);
                  var3 = var4;
               }
            }
         }
      }

      return var3;
   }

   static int a(FolderScanRequest var0, ScannedFolder var1, boolean var2) {
      return var0.a(var1, var2);
   }

   private int a(ArrayList var1, boolean var2, boolean var3, Stack var4) {
      AtomicInteger var6 = new AtomicInteger();
      ArrayList var7 = new ArrayList(var1.size());
      Iterator var9 = var1.iterator();

      while (var9.hasNext()) {
         var7.add(new ScanSubdirectoriesTask(this, var2, (ScannedFolder)var9.next(), var6, var4, var3));
      }

      int var5;
      if (this.e) {
         var5 = 0;
      } else {
         try {
            FolderScanner.d().invokeAll(var7);
         } catch (Throwable var8) {
            var8.printStackTrace();
         }

         var5 = var6.get();
      }

      return var5;
   }

   static NativeFolderScanner a(FolderScanRequest var0) {
      return var0.h;
   }

   static NativeFolderScanner a(FolderScanRequest var0, NativeFolderScanner var1) {
      var0.h = var1;
      return var1;
   }

   private void a(ArrayList var1) {
      boolean var3 = QuickApp.o.d();
      IntegerPropertyFile var4 = new IntegerPropertyFile(2048);
      if (var4.a(FolderScanner.d(this.f.a))) {
         var1.ensureCapacity(var1.size() + var4.c());
         Iterator var5 = var4.f().iterator();

         while (var5.hasNext()) {
            String var6 = (String)((Entry)var5.next()).getKey();
            if (!this.a(var6, true, false) && this.a(var6, false)) {
               int var2;
               if (var3) {
                  var2 = JniUtils.fuGetFileTime(var6);
               } else {
                  var2 = 0;
               }

               var1.add(new ScannedFolder(var6, var2));
            }
         }
      }

      GalleryUtils.a(var1, QuickApp.o.a);
   }

   private boolean a(String var1, int var2) {
      boolean var3;
      if (!FolderScanner.a(this.f.a).a(var1, var2)) {
         var3 = false;
      } else if (this.d) {
         var3 = FolderScanner.b(this.f.a).a(var1, var2);
      } else {
         var3 = true;
      }

      return var3;
   }

   private boolean a(String var1, boolean var2) {
      boolean var6 = true;
      boolean var5 = var6;
      if (FolderScanWorker.e(this.f) != null) {
         if (FolderScanWorker.e(this.f).length == 0) {
            var5 = var6;
         } else {
            String[] var8 = FolderScanWorker.e(this.f);
            int var4 = var8.length;
            int var3 = 0;

            while (true) {
               if (var3 >= var4) {
                  var5 = false;
                  break;
               }

               String var7 = var8[var3];
               var5 = var6;
               if (GalleryUtils.c(var7, var1)) {
                  break;
               }

               if (var2) {
                  var5 = var6;
                  if (GalleryUtils.c(var1, var7)) {
                     break;
                  }
               }

               var3++;
            }
         }
      }

      return var5;
   }

   private boolean a(String var1, boolean var2, boolean var3) {
      if (var3 && FolderScanWorker.c(this.f) != null) {
         Boolean var7 = (Boolean)FolderScanWorker.c(this.f).get(var1);
         if (var7 == null || !var2 && !var7) {
            var2 = false;
         } else {
            var2 = true;
         }
      } else {
         if (FolderScanWorker.d(this.f) != null) {
            String[] var6 = FolderScanWorker.d(this.f);
            int var5 = var6.length;

            for (int var4 = 0; var4 < var5; var4++) {
               if (GalleryUtils.c(var6[var4], var1)) {
                  var2 = true;
                  return var2;
               }
            }
         }

         var2 = false;
      }

      return var2;
   }

   private int b(ScannedFolder var1) {
      var1.h = new ArrayList(64);
      int var2 = this.a(var1, false);
      ArrayList var4 = var1.h;
      var1.h = null;
      if (var2 > 0) {
         this.c.obtainMessage(12, 0, 0, var1).sendToTarget();
      }

      boolean var3 = QuickApp.o.d();

      for (int var7 = var4.size() - 1; var7 >= 0; var7--) {
         var1 = (ScannedFolder)var4.get(var7);
         String var5 = var1.getPath();
         if (this.a(var5, false, true) || !this.a(var5, true)) {
            var4.remove(var7);
         } else if (var3 && var1.e == 0) {
            var1.n();
         }
      }

      GalleryUtils.a(var4, QuickApp.o.a);
      if (!this.e) {
         var2 = this.a(var4, false, true, null) + 1;
      } else {
         var2 = 1;
      }

      return var2;
   }

   private void b() {
        ArrayList<ScannedFolder> arrayList = (ArrayList) this.b;
        HashMap mapG = FolderScanner.a(this.f.a).g();
        HashMap mapG2 = FolderScanner.b(this.f.a).g();
        FolderScanner.a(this.f.a).d();
        FolderScanner.b(this.f.a).d();
        for (ScannedFolder srcType0111 : arrayList) {
            if (!srcType0111.h()) {
                String path = srcType0111.getPath();
                FolderScanStamp srcType0073 = (FolderScanStamp) mapG2.get(path);
                if (srcType0073 == null) {
                    FolderScanStamp srcType0074 = (FolderScanStamp) mapG.get(path);
                    if (srcType0074 != null) {
                        FolderScanner.a(this.f.a).a(path, srcType0074);
                    }
                } else if (this.d && srcType0111.a(true)) {
                    FolderScanner.b(this.f.a).a(path, srcType0073);
                }
            }
        }
        FolderScanner.a(this.f.a).a(FolderScanner.a(this.f.a).c() != mapG.size());
        FolderScanner.b(this.f.a).a(FolderScanner.b(this.f.a).c() != mapG2.size());
        if (this.e) {
            return;
        }
        a(arrayList, true, false, (Stack) null);
    }

   static void b(FolderScanRequest var0) {
      var0.b();
   }

   private void b(ArrayList var1) {
      IntegerPropertyFile var3 = new IntegerPropertyFile(2048);
      if (var3.a(FolderScanner.c(this.f.a))) {
         var1.ensureCapacity(var1.size() + var3.c());
         Iterator var6 = var3.f().iterator();

         while (var6.hasNext()) {
            String var4 = (String)((Entry)var6.next()).getKey();
            if (!this.a(var4, true, false) && this.a(var4, false)) {
               ScannedFolder var9 = new ScannedFolder(var4, 0);
               int var2 = GalleryUtils.a(var1, var9, GalleryFolders.j);
               if (var2 < 0) {
                  var1.add(-var2 - 1, var9);
               }
            }
         }
      }

      for (String var10 : MediaBucketIndex.b()) {
         if (!this.a(var10, true, false) && this.a(var10, false)) {
            ScannedFolder var11 = new ScannedFolder(var10, 0);
            int var5 = GalleryUtils.a(var1, var11, GalleryFolders.j);
            if (var5 < 0) {
               var1.add(-var5 - 1, var11);
            }
         }
      }

      if (QuickApp.o.d()) {
         Iterator var8 = var1.iterator();

         while (var8.hasNext()) {
            ((ScannedFolder)var8.next()).n();
         }
      }

      GalleryUtils.a(var1, QuickApp.o.a);
   }

   private ArrayList<String> prepareFilters(boolean allAlbums) {
      this.f.refreshDefaultExclusions();
      List<String> excluded = FolderScanWorker.a(this.f).a(true);
      ArrayList<String> included = FolderScanWorker.a(this.f).a(false);
      HashMap<String, Boolean> exclusions = new HashMap<>();
      if (allAlbums) {
         for (Object path : FolderScanWorker.b(this.f)) exclusions.put((String) path, false);
      }
      for (String path : excluded) exclusions.put(path, true);
      FolderScanWorker.a(this.f, exclusions.isEmpty() ? null : exclusions);
      FolderScanWorker.a(this.f, exclusions.isEmpty() ? null : exclusions.keySet().toArray(new String[0]));
      FolderScanWorker.b(this.f, included.isEmpty() ? null : included.toArray(new String[0]));
      return included;
   }

   private void clearFilters() {
      FolderScanWorker.a(this.f, (HashMap) null);
      FolderScanWorker.a(this.f, (String[]) null);
      FolderScanWorker.b(this.f, null);
   }

   private ArrayList<ScannedFolder> scanRoots(List<String> included) {
      ArrayList<ScannedFolder> roots = new ArrayList<>(4);
      for (String path : included) {
         ScannedFolder root = new ScannedFolder(path, 0);
         if (root.exists()) {
            if (QuickApp.p.a(path)) root.f = 0;
            roots.add(root);
         }
      }
      if (roots.isEmpty()) {
         for (String path : QuickApp.p.a()) {
            ScannedFolder root = new ScannedFolder(path, 0);
            if (root.exists()) {
               root.f = 0;
               roots.add(root);
            }
         }
      }
      return roots;
   }

   private static boolean under(File root, File directory) {
      return root.equals(directory) || GalleryUtils.c(root.getPath(), directory.getPath())
              || ("/".equals(root.getPath()) && directory.isAbsolute());
   }

   private static boolean isPrescanCandidate(File directory, List<ScannedFolder> roots) {
      String path = directory.getPath();
      for (File root : roots) {
         if (under(root, directory) && path.indexOf("/.", root.getPath().length()) < 0) return true;
      }
      return false;
   }

   private File inScanRoot(File directory, List<ScannedFolder> roots) {
      for (File root : roots) {
         if (under(root, directory)) return directory;
      }
      // MediaStore and the filesystem can use different aliases for the same volume.
      try {
         File canonical = directory.getCanonicalFile();
         for (File root : roots) {
            File canonicalRoot = root.getCanonicalFile();
            if (canonicalRoot.equals(canonical)) return root;
            String prefix = GalleryUtils.e(canonicalRoot.getPath());
            if (canonical.getPath().startsWith(prefix)) {
               return new File(root, canonical.getPath().substring(prefix.length()));
            }
         }
      } catch (IOException | SecurityException ignored) {}
      return null;
   }

   private boolean reachable(File directory, List<ScannedFolder> roots) {
      for (ScannedFolder root : roots) {
         if (!under(root, directory) || !this.d && root.a(false)) continue;
         for (File ancestor = directory; ancestor != null; ancestor = ancestor.getParentFile()) {
            String path = ancestor.getPath();
            if (this.a(path, true, true) || (path.endsWith(this.i) && QuickApp.p.g(path))
                    || (!this.d && !QuickApp.p.a(path) && new File(ancestor, ".nomedia").exists())) break;
            if (ancestor.equals(root)) return true;
            if (ancestor.getName().startsWith(".")) break;
         }
      }
      return false;
   }

   ArrayList<ScannedFolder> refreshDirectories() {
      ArrayList<ScannedFolder> results = new ArrayList<>();
      List<ScannedFolder> roots = scanRoots(prepareFilters(true));
      Set<File> visited = new HashSet<>();
      Set<File> invalidated = new HashSet<>();
      long start = SystemClock.uptimeMillis();
      try {
         for (File changed : (List<File>) this.b) {
            if (this.e) break;
            File directory = inScanRoot(changed, roots);
            if (directory == null || !visited.add(directory)) continue;
            for (File ancestor = directory; ancestor != null && invalidated.add(ancestor);
                 ancestor = ancestor.getParentFile()) {
               this.f.invalidateDirectory(ancestor.getPath());
            }
            ScannedFolder album = new ScannedFolder(directory, 0);
            if (this.a(directory.getPath(), false) && reachable(directory, roots)) {
               this.a(album, true);
            }
            album.h = null;
            results.add(album);
         }
      } finally {
         clearFilters();
      }
      Log.d("FolderScanner", "refresh directories: " + results.size()
              + ", used=" + (SystemClock.uptimeMillis() - start));
      return results;
   }

   private int c() {
      ArrayList<String> included = prepareFilters(this.a == 3);
      try {
         if (this.a == 3) return scanAll(scanRoots(included));
         return this.b == null ? 0 : b(new ScannedFolder((String) this.b, 0));
      } finally {
         clearFilters();
      }
   }

   private int scanAll(ArrayList<ScannedFolder> roots) {
      long start = SystemClock.uptimeMillis();
      ArrayList<ScannedFolder> known = new ArrayList<>(128);
      if (this.d) a(known);
      else b(known);
      if (enhanced) {
         for (Iterator<ScannedFolder> it = known.iterator(); it.hasNext();) {
            if (!isPrescanCandidate(it.next(), roots)) it.remove();
         }
         prescans = new ConcurrentHashMap<>();
         if (roots.size() > 1) walked = new HashSet<>();
      }
      int count = 0;
      if (!known.isEmpty()) {
         prescanning = true;
         count = a(known, false, false, null);
         prescanning = false;
         long finished = SystemClock.uptimeMillis();
         Log.d("FolderScanner", "prescan: " + count + ", used=" + (finished - start));
         this.c.obtainMessage(14, 0, 0).sendToTarget();
         start = finished;
      }
      for (ScannedFolder root : roots) {
         if (this.e) break;
         if (this.d || !root.a(false)) count += a(root);
      }
      Log.d("FolderScanner", "scan: " + count + ", used=" + (SystemClock.uptimeMillis() - start));
      if (enhanced) Log.d("FolderScanner", "reused prescanned directories: " + reusedPrescans);
      prescans = null;
      walked = null;
      if (!this.e) {
         if (FolderScanner.a(this.f.a).e()) {
            FolderScanner.a(this.f.a).b(FolderScanner.c(this.f.a));
         }
         if (this.d && FolderScanner.b(this.f.a).e()) {
            FolderScanner.b(this.f.a).b(FolderScanner.d(this.f.a));
         }
      }
      return count;
   }

   static int c(FolderScanRequest var0) {
      return var0.c();
   }
}
