package com.alensw.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import com.alensw.gallery.GalleryMediaStore;
public class MediaStoreFile extends CommonFile {
   public static final Creator CREATOR = new MediaStoreFileCreator();
   protected final int k;
   protected final String l;

   public MediaStoreFile(char var1, int var2, String var3, String var4, int var5, long var6, long var8, long var10, String var12) {
      super(var1, var3, var4, var5, var6, var8, var10, null, null);
      this.k = var2;
      this.l = var12;
   }

   public MediaStoreFile(char var1, int var2, String var3, String var4, int var5, long var6, long var8, long var10, String var12, Uri var13) {
      super(var1, var3, var4, var5, var6, var8, var10, null, var13);
      this.k = var2;
      this.l = var12;
   }

   public MediaStoreFile(Parcel var1, char var2) {
      super(var1, var2);
      this.k = var1.readInt();
      this.l = var1.readString();
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof MediaStoreFile)) {
            return false;
        }
        MediaStoreFile mediaStoreFile = (MediaStoreFile) var1;
        return this.g == mediaStoreFile.g && this.b == mediaStoreFile.b && this.k == mediaStoreFile.k;
    }

   @Override
   public int hashCode() {
      return this.k + (int)this.g + this.b;
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         this.j = GalleryMediaStore.a(this.b).buildUpon().appendEncodedPath(Integer.toString(this.k)).build();
      }

      return this.j;
   }

   @Override
   public String j() {
      return this.l;
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      var1.writeInt(this.k);
      var1.writeString(this.l);
   }
}
