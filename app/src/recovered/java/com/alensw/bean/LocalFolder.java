package com.alensw.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class LocalFolder extends CommonFolder {
   public static final Creator CREATOR = new com.alensw.bean.j();
   protected final String u;
   public ArrayList v = null;

   public LocalFolder(Parcel var1, char var2) {
      super(var1, var2);
      this.u = var1.readString();
      int var4 = var1.readInt();

      for (int var3 = 0; var3 < var4; var3++) {
         var2 = (char)var1.readInt();
         CommonFile var5;
         if (var2 == 'D') {
            var5 = new LocalFolder(var1, var2);
         } else {
            var5 = new LocalFile(var1, var2);
         }

         this.n.add(var5);
      }
   }

   public LocalFolder(CommonFile var1, String var2) {
      super(var1);
      this.u = var2;
   }

   public LocalFolder(String var1, String var2, int var3, long var4, long var6) {
        super(var2 != null ? var2 : com.alensw.b.l.b.b(var1), var3, var4, var6, null);
        this.u = var1;
    }

   @Override
   public int a(Uri var1) {
      int var2;
      if ("file".equals(var1.getScheme())) {
         var2 = super.a(var1);
      } else {
         String var5 = var1.getLastPathSegment();
         int var3 = this.n.size();
         var2 = 0;

         while (true) {
            if (var2 >= var3) {
               var2 = super.a(var1);
               break;
            }

            CommonFile var4 = (CommonFile)this.n.get(var2);
            if (var4 instanceof LocalFile && ((LocalFile)var4).c.equals(var5)) {
               break;
            }

            var2++;
         }
      }

      return var2;
   }

   @Override
   public CommonFolder a(boolean var1) {
      return this.b(var1);
   }

   public void a(char var1, int var2, String var3) {
      this.n.ensureCapacity(256);
      if (var1 != 'D') {
         this.n.add(new LocalFile(var1, this.u, var3, com.alensw.b.f.c.a(var3, var1), 5, 0L, var2, var2));
      } else if (this.v != null) {
         this.v.add(new LocalFolder(com.alensw.b.l.b.b(this.u, var3), var3, 50, var2, var2));
      }
   }

   public LocalFolder b(boolean var1) {
      LocalFolder var2 = new LocalFolder(this, this.u);
      var2.k = this.k;
      var2.l = this.l;
      if (var1) {
         var2.a(this, true);
      }

      return var2;
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof LocalFolder)) {
            return false;
        }
        LocalFolder localFolder = (LocalFolder) var1;
        return this.g == localFolder.g && this.b == localFolder.b && (this.u == localFolder.u || this.u.equals(localFolder.u));
    }

   @Override
   public int hashCode() {
      return this.u.hashCode() + (int)this.g;
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         this.j = Uri.fromFile(new File(this.u));
      }

      return this.j;
   }

   @Override
   public String j() {
      return this.u;
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      var1.writeString(this.u);
      var1.writeInt(this.n.size());
      Iterator var3 = this.n.iterator();

      while (var3.hasNext()) {
         ((CommonFile)var3.next()).writeToParcel(var1, var2);
      }
   }
}
