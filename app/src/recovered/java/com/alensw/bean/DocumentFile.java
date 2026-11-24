package com.alensw.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
public class DocumentFile extends CommonFile {
   public static final Creator CREATOR = new com.alensw.bean.g();
   protected final String k;
   protected final String l;

   public DocumentFile(char var1, String var2, String var3, String var4, String var5, int var6, long var7, long var9, long var11, String var13) {
      super(var1, var4, var5, var6, var7, var9, var11, var13, null);
      this.k = var2;
      if (var3 == null) {
         var3 = "";
      }

      this.l = var3;
   }

   public DocumentFile(char var1, String var2, String var3, String var4, String var5, int var6, long var7, long var9, long var11, String var13, Uri var14) {
      super(var1, var4, var5, var6, var7, var9, var11, var13, var14);
      this.k = var2;
      if (var3 == null) {
         var3 = "";
      }

      this.l = var3;
   }

   public DocumentFile(Parcel var1, char var2) {
      super(var1, var2);
      this.k = var1.readString();
      this.l = var1.readString();
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof DocumentFile)) {
            return false;
        }
        DocumentFile documentFile = (DocumentFile) var1;
        return this.g == documentFile.g && this.b == documentFile.b && (this.l == documentFile.l || this.l.equals(documentFile.l));
    }

   @Override
   public int hashCode() {
      return this.l.hashCode() + (int)this.g + this.b;
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         this.j = com.alensw.b.j.a.c(this.k, this.l);
      }

      return this.j;
   }

   public String k() {
      return this.l;
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      var1.writeString(this.k);
      var1.writeString(this.l);
   }
}
