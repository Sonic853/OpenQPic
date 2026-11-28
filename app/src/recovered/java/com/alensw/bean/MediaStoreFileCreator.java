package com.alensw.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class MediaStoreFileCreator implements Creator {
   public MediaStoreFile a(Parcel var1) {
      return new MediaStoreFile(var1, (char)var1.readInt());
   }

   public MediaStoreFile[] a(int var1) {
      return new MediaStoreFile[var1];
   }

   @Override
   public Object createFromParcel(Parcel var1) {
      return this.a(var1);
   }

   @Override
   public Object[] newArray(int var1) {
      return this.a(var1);
   }
}
