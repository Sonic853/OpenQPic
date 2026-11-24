package com.alensw.bean;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import com.alensw.PicFolder.GalleryStorage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class CommonFolder extends CommonFile {
   public static final Comparator o = new com.alensw.bean.a();
   public static final Comparator p = new com.alensw.bean.b();
   public static final Comparator q = new com.alensw.bean.c();
   public static final Comparator r = new com.alensw.bean.d();
   public static final Comparator s = new com.alensw.bean.e();
   public static final Comparator t = new com.alensw.bean.f();
   public int k = -1;
   public int l = 0;
   protected Comparator m = o;
   protected final ArrayList<CommonFile> n = new ArrayList<>();

   public CommonFolder(Parcel var1, char var2) {
      super(var1, var2);
      this.k = var1.readInt();
      this.l = var1.readInt();
   }

   public CommonFolder(CommonFile var1) {
      super(var1);
   }

   public CommonFolder(String var1, int var2, long var3, long var5, String var7) {
      super('D', var1, "vnd.android.document/directory", var2, 0L, var3, var5, var7, null);
   }

   public static int a(int var0, int var1) {
      return var0 << 4 | var1;
   }

   public int a(Uri var1) {
      int var3 = this.n.size();
      int var2 = 0;

      while (true) {
         if (var2 >= var3) {
            var2 = -1;
            break;
         }

         if (((CommonFile)this.n.get(var2)).i().equals(var1)) {
            break;
         }

         var2++;
      }

      return var2;
   }

   public int a(CommonFile var1) {
      int var2 = com.alensw.b.l.b.a(this.n, var1, this.m);
      if (var2 < 0) {
         int var3 = this.a(var1.i());
         if (var3 != -1) {
            var2 = var3;
         }
      }

      return var2;
   }

   public Cursor a(ContentResolver var1, String var2) {
      return null;
   }

   public final CommonFile a(int var1) {
      CommonFile var2;
      if (var1 >= 0 && var1 < this.n.size()) {
         var2 = (CommonFile)this.n.get(var1);
      } else {
         var2 = CommonFile.a;
      }

      return var2;
   }

   public CommonFolder a(boolean var1) {
      CommonFolder var2 = new CommonFolder(this);
      var2.k = this.k;
      var2.l = this.l;
      if (var1) {
         var2.a(this, true);
      }

      return var2;
   }

   public void a(int var1, CommonFile var2) {
      this.n.add(var1, var2);
   }

   public void a(ContentResolver var1, ContentObserver var2) {
      var1.registerContentObserver(GalleryStorage.observerUri(this.i()), false, var2);
   }

   public void a(Cursor var1, Set var2) {
   }

   public void a(CommonFolder var1, boolean var2) {
      this.n.clear();
      if (!var1.n()) {
         this.n.ensureCapacity(var1.o());
      }

      CommonFile var3 = var1.a(var1.k);

      for (CommonFile var4 : var1.n) {
         if (var2 || var4.b != 'D') {
            if (var3 == var4) {
               this.k = this.n.size();
            }

            this.n.add(var4);
         }
      }
   }

   public void a(Collection var1) {
      for (Object var3 : var1) {
         if (var3 instanceof CommonFile) {
            this.d((CommonFile)var3);
         }
      }
   }

   public Object[] a(ContentResolver var1) {
      return null;
   }

   public int b(CommonFile var1) {
      return com.alensw.b.l.b.a(this.n, var1, this.m);
   }

   public final Uri b(int var1) {
      Uri var2;
      if (var1 >= 0 && var1 < this.n.size()) {
         var2 = ((CommonFile)this.n.get(var1)).i();
      } else {
         var2 = Uri.EMPTY;
      }

      return var2;
   }

   public void b(ContentResolver var1) {
   }

   public void b(ContentResolver var1, ContentObserver var2) {
      var1.unregisterContentObserver(var2);
   }

   public void b(Cursor var1, Set var2) {
   }

   public int c(CommonFile var1) {
      int var3 = this.a(var1);
      int var2 = var3;
      if (var3 < 0) {
         var2 = -var3 - 1;
      }

      this.n.add(var2, var1);
      return var2;
   }

   public boolean c(int var1) {
      int var2 = this.n.size();
      boolean var3;
      if (var1 >= 0 && var1 < var2) {
         this.n.remove(var1);
         if (this.k >= --var2) {
            this.k = var2 - 1;
         } else if (this.k > var1) {
            this.k--;
         }

         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean d(CommonFile var1) {
      int var2 = this.a(var1);
      boolean var3;
      if (var2 >= 0) {
         this.c(var2);
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }

   @Override
   public int describeContents() {
      return 0;
   }

   public final int k() {
      return (this.l & 240) >> 4;
   }

   public final int l() {
      return this.l & 15;
   }

   public final boolean m() {
      boolean var3 = true;
      int var1 = this.l & 15;
      boolean var2 = var3;
      if (var1 != 3) {
         if (var1 == 1) {
            var2 = var3;
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public boolean n() {
      return this.n.isEmpty();
   }

   public int o() {
      return this.n.size();
   }

   public final List p() {
      ArrayList var2 = new ArrayList(this.n.size());

      for (CommonFile var1 : this.n) {
         if (var1.h() != 'D') {
            var2.add(var1);
         }
      }

      return var2;
   }

   public Uri q() {
      return Uri.EMPTY;
   }

   public void r() {
      this.n.clear();
      this.k = -1;
   }

   public void s() {
      switch (this.l()) {
         case 1:
            this.m = r;
            break;
         case 2:
            this.m = o;
            break;
         case 3:
            this.m = q;
            break;
         case 4:
            this.m = p;
         case 5:
         case 6:
         default:
            break;
         case 7:
            this.m = s;
            break;
         case 8:
            this.m = t;
      }

      CommonFile var1 = this.a(this.k);
      com.alensw.b.l.b.a(this.n, this.m);
      if (var1 != null) {
         this.k = this.a(var1);
      }
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      var1.writeInt(this.k);
      var1.writeInt(this.l);
   }
}
