package com.alensw.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class MediaStoreFolderCreator implements Creator {
   public MediaStoreFolder a(Parcel var1) {
      return new MediaStoreFolder(var1, (char)var1.readInt());
   }

   public MediaStoreFolder[] a(int var1) {
      return new MediaStoreFolder[var1];
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
