package com.alensw.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class LocalFileCreator implements Creator {
   public LocalFile a(Parcel var1) {
      return new LocalFile(var1, (char)var1.readInt());
   }

   public LocalFile[] a(int var1) {
      return new LocalFile[var1];
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
