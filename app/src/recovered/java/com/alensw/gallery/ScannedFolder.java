package com.alensw.gallery;

import android.net.Uri;
import android.os.Environment;
import android.os.Build.VERSION;
import com.alensw.PicFolder.QuickApp;
import com.alensw.image.ExifData;
import com.alensw.jni.JniUtils;
import com.alensw.media.MediaFileTypes;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

public class ScannedFolder extends File {
   public static final boolean a;
   public static final String b;
   public static final Comparator j;
   public static final Comparator k;
   public static final Comparator l;
   public static final Comparator m;
   public static final Comparator n;
   public static final Comparator o;
   public static final Comparator p;
   public static final Comparator q;
   public int c = -1;
   public int d = 0;
   public int e = 0;
   public int f = -1;
   public int g = 0;
   public ArrayList<ScannedFolder> h = null;
   public final ArrayList i = new ArrayList();
   private Comparator r;

   static {
      boolean var0;
      if (VERSION.SDK_INT >= 11) {
         var0 = true;
      } else {
         var0 = false;
      }

      a = var0;
      b = Environment.getExternalStorageDirectory().getPath();
      j = new MediaNameComparator();
      k = new ReverseMediaNameComparator();
      l = new MediaModifiedTimeComparator();
      m = new ReverseMediaModifiedTimeComparator();
      n = new MediaCaptureTimeComparator();
      o = new ReverseMediaCaptureTimeComparator();
      p = new MediaSizeComparator();
      q = new ReverseMediaSizeComparator();
   }

   public ScannedFolder(File var1, int var2) {
      super(var1.getPath());
      this.r = j;
      this.i(var2);
   }

   public ScannedFolder(File var1, String var2, int var3) {
      super(var1, var2);
      this.r = j;
      this.i(var3);
   }

   public ScannedFolder(String var1, int var2) {
      super(var1);
      this.r = j;
      this.i(var2);
   }

   public static int a(int var0, int var1) {
      return var0 << 4 | var1;
   }

   public static int a(int var0, int var1, int var2) {
      return var0 << 8 | var1 << 4 | var2;
   }

   public static boolean a(File var0) {
      var0 = new File(var0, "!qpicfake.jpg");
      boolean var1;
      if (var0.exists() && var0.length() == 0L && GalleryUtils.c(var0)) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public static boolean a(File var0, ScannedMediaFile var1, String var2) {
      boolean var3 = false;
      String var5 = var1.b;
      String var6 = GalleryUtils.a(var5, false);
      String var4 = var2;
      if (var6.length() > 0) {
         var4 = var2 + "." + var6;
      }

      if (GalleryUtils.b(new File(var0, var5), var4)) {
         var1.b = var4;
         var3 = true;
      }

      return var3;
   }

   private void i(int var1) {
      String var2 = this.getPath();
      this.g = QuickApp.p.c(var2);
      if (var1 == -1) {
         this.n();
      } else {
         this.e = var1;
      }
   }

   public final int a() {
      return this.d >> 4 & 15;
   }

   public final int a(int var1) {
      if (var1 == 0) {
         if (this.g == 0) {
            var1 = 2;
         } else if (a) {
            var1 = 6;
         } else {
            var1 = 1;
         }
      }

      return a(0, var1);
   }

   public int a(int var1, String var2) {
      int var3 = -1;
      ScannedMediaFile var4 = this.e(var1);
      if (var4 != null && a(this, var4, var2)) {
         this.h(var1);
         var3 = this.c(var4);
      }

      return var3;
   }

   public int a(ScannedMediaFile var1) {
      if (this.e() && var1.d == -1) {
         this.b(var1);
      }

      int var3 = GalleryUtils.a(this.i, var1, this.r);
      if (var3 < 0) {
         int var2 = this.a(var1.b);
         if (var2 != -1) {
            return var2;
         }
      }

      return var3;
   }

   public int a(File var1, char var2) {
      return this.a(new ScannedMediaFile(var1, var2, -1));
   }

   public int a(String var1) {
      int var3 = this.i.size();
      int var2 = 0;

      while (true) {
         if (var2 >= var3) {
            var2 = -1;
            break;
         }

         if (((ScannedMediaFile)this.i.get(var2)).b.equals(var1)) {
            break;
         }

         var2++;
      }

      return var2;
   }

   public int a(String var1, char var2) {
      return this.a(new ScannedMediaFile(new File(var1), var2, -1));
   }

   public void a(char var1, int var2, long var3, String var5) {
      if (var1 != 'D') {
         this.i.add(new ScannedMediaFile(this.getPath(), var5, var1, var2, var2, var3));
      } else if (this.h != null) {
         this.h.add(new ScannedFolder(this, var5, var2));
      }
   }

   public void a(ScannedFolder var1, boolean var2) {
      if (this.d == 0) {
         this.d = var1.d;
      }

      this.e = var1.e;
      this.f = var1.f;
      this.g = var1.g;
      if (var2) {
         ScannedMediaFile var3 = this.e(this.c);
         this.i.clear();
         this.i.addAll(var1.i);
         if (var1.h != null) {
            this.h = new ArrayList(var1.h);
         } else {
            this.h = null;
         }

         if (var3 != null) {
            this.c = this.a(var3);
         }
      } else if (this.c == -1) {
         this.c = var1.c;
      }
   }

   public void a(Collection var1) {
      for (Object var3 : var1) {
         if (var3 instanceof ScannedMediaFile) {
            this.d((ScannedMediaFile)var3);
         }
      }
   }

   public boolean a(ScannedFolder var1, int var2) {
      int var4 = this.j();
      boolean var5;
      if (var4 != var1.j()) {
         var5 = false;
      } else {
         int var3 = var2;
         if (var2 > var4) {
            var3 = var4;
         }

         var2 = 0;

         while (true) {
            if (var2 >= var3) {
               var5 = true;
               break;
            }

            if (!((ScannedMediaFile)this.i.get(var2)).equals(var1.e(var2))) {
               var5 = false;
               break;
            }

            var2++;
         }
      }

      return var5;
   }

   public boolean a(boolean var1) {
      boolean var3 = true;
      if (this.f == -1 || var1) {
         byte var2;
         if (GalleryUtils.h(this.getPath())) {
            var2 = 1;
         } else {
            var2 = 0;
         }

         this.f = var2;
      }

      if (this.f == 1) {
         var1 = var3;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final int b() {
      return this.d >> 8 & 15;
   }

   public int b(boolean var1) {
      ScannedMediaFile var4 = this.e(this.c);
      this.k();
      this.n();
      int var3 = QuickApp.q.b(this, var1);
      int var2 = var3;
      if (var3 == 1) {
         var2 = var3;
         if (this.o()) {
            var2 = var3 - 1;
         }
      }

      if (var4 != null) {
         if (this.e() && var4.d == -1) {
            this.b(var4);
         }

         this.c = this.a(var4);
      }

      return var2;
   }

   public final Uri b(String var1) {
      return Uri.fromFile(new File(this, var1));
   }

   public final String b(int var1) {
      String var2;
      if (var1 >= 0 && var1 < this.i.size()) {
         var2 = ((ScannedMediaFile)this.i.get(var1)).a();
      } else {
         var2 = "";
      }

      return var2;
   }

   public void b(ScannedMediaFile var1) {
      String var2 = var1.a();
      var1.d = JniUtils.fuGetFileTime(var2);
      if (this.f()) {
         var1.e = (int)(ExifData.a(var2) / 1000L);
      } else {
         var1.e = var1.d;
      }
   }

   public int c(ScannedMediaFile var1) {
      int var2 = this.a(var1);
      if (var2 >= 0) {
         ((ScannedMediaFile)this.i.get(var2)).a(var1);
      } else {
         var2 = -var2 - 1;
         this.i.add(var2, var1);
      }

      return var2;
   }

   public final File c(int var1) {
      File var2;
      if (var1 >= 0 && var1 < this.i.size()) {
         var2 = new File(this, ((ScannedMediaFile)this.i.get(var1)).b);
      } else {
         var2 = null;
      }

      return var2;
   }

   public final boolean c() {
      boolean var3 = true;
      int var1 = this.d >> 8 & 15;
      boolean var2 = var3;
      if (var1 != 1) {
         if (var1 == 0 && this.g == 2) {
            var2 = var3;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public final int d() {
      return this.d & 15;
   }

   public final Uri d(int var1) {
      Uri var2;
      if (var1 >= 0 && var1 < this.i.size()) {
         var2 = Uri.fromFile(new File(this, ((ScannedMediaFile)this.i.get(var1)).b));
      } else {
         var2 = Uri.EMPTY;
      }

      return var2;
   }

   public boolean d(ScannedMediaFile var1) {
      int var2 = this.a(var1);
      boolean var3;
      if (var2 >= 0) {
         this.i.remove(var2);
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public final ScannedMediaFile e(int var1) {
      ScannedMediaFile var2;
      if (var1 >= 0 && var1 < this.i.size()) {
         var2 = (ScannedMediaFile)this.i.get(var1);
      } else {
         var2 = null;
      }

      return var2;
   }

   public final boolean e() {
      boolean var3 = true;
      int var1 = this.d & 15;
      boolean var2 = var3;
      if (var1 != 3) {
         var2 = var3;
         if (var1 != 1) {
            var2 = var3;
            if (var1 != 5) {
               if (var1 == 6) {
                  var2 = var3;
               } else {
                  var2 = false;
               }
            }
         }
      }

      return var2;
   }

   public final char f(int var1) {
      char var2;
      if (var1 >= 0 && var1 < this.i.size()) {
         var2 = ((ScannedMediaFile)this.i.get(var1)).c;
      } else {
         var2 = 0;
      }

      return var2;
   }

   public final boolean f() {
      int var1 = this.d & 15;
      boolean var2;
      if (var1 != 5 && var1 != 6) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public final String g(int var1) {
      String var2;
      if (var1 >= 0 && var1 < this.i.size()) {
         ScannedMediaFile var3 = (ScannedMediaFile)this.i.get(var1);
         var2 = MediaFileTypes.a(var3.b, var3.c);
      } else {
         var2 = null;
      }

      return var2;
   }

   public final boolean g() {
      int var1 = this.d & 15;
      boolean var2;
      if (var1 != 2 && var1 != 4) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public final boolean h() {
      boolean var1;
      if (!this.i.isEmpty() || this.h != null && !this.h.isEmpty()) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }

   public boolean h(int var1) {
      int var2 = this.i.size();
      boolean var3;
      if (var1 >= 0 && var1 < var2) {
         this.i.remove(var1);
         if (this.c >= --var2) {
            this.c = var2 - 1;
         } else if (this.c > var1) {
            this.c--;
         }

         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public final int i() {
      int var1;
      if (this.h != null) {
         var1 = this.h.size();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public final int j() {
      return this.i.size();
   }

   public void k() {
      this.i.clear();
      this.h = null;
      this.c = -1;
      this.e = 0;
      this.f = -1;
   }

   public void l() {
      switch (this.d()) {
         case 1:
            this.r = m;
            break;
         case 2:
            this.r = j;
            break;
         case 3:
            this.r = l;
            break;
         case 4:
            this.r = k;
            break;
         case 5:
            this.r = n;
            break;
         case 6:
            this.r = o;
            break;
         case 7:
            this.r = p;
            break;
         case 8:
            this.r = q;
      }

      ScannedMediaFile var1 = this.e(this.c);
      GalleryUtils.a(this.i, this.r);
      if (var1 != null) {
         this.c = this.a(var1);
      }
   }

   public final boolean m() {
      boolean var1;
      if (this.e != JniUtils.fuGetFileTime(this.getPath())) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void n() {
      this.e = JniUtils.fuGetFileTime(this.getPath());
   }

   public boolean o() {
      File var2 = new File(this, "!qpicfake.jpg");
      boolean var1;
      if (var2.exists() && var2.length() == 0L) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var1 && this.i.size() == 1 && "!qpicfake.jpg".equals(this.e(0).b)) {
         this.h(0);
      }

      return var1;
   }

   public boolean p() {
      File var2 = new File(this, "!qpicfake.jpg");
      boolean var1;
      if (!var2.exists() && !GalleryUtils.b(var2)) {
         var1 = false;
      } else {
         var1 = true;
      }

      return var1;
   }
}
