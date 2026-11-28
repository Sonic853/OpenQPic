package com.alensw.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.alensw.utils.GalleryUtils;
import java.io.File;

public class LocalFile extends CommonFile {
   public static final Creator CREATOR = new LocalFileCreator();
   protected final String k;

   public LocalFile(char var1, String var2, String var3, String var4, int var5, long var6, long var8, long var10) {
      super(var1, var3, var4, var5, var6, var8, var10, null, null);
      this.k = var2;
   }

   public LocalFile(Parcel var1, char var2) {
      super(var1, var2);
      this.k = var1.readString();
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof LocalFile)) {
            return false;
        }
        LocalFile localFile = (LocalFile) var1;
        return this.g == localFile.g && this.b == localFile.b && (this.c == localFile.c || this.c.equals(localFile.c)) && (this.k == localFile.k || this.k.equals(localFile.k));
    }

   @Override
   public int hashCode() {
      int var1;
      if (this.k != null) {
         var1 = this.k.hashCode();
      } else {
         var1 = 0;
      }

      return var1 + this.c.hashCode() + (int)this.g;
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         this.j = Uri.fromFile(new File(this.k, this.c));
      }

      return this.j;
   }

   @Override
   public String j() {
      return GalleryUtils.e(this.k) + this.c;
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      var1.writeString(this.k);
   }
}
