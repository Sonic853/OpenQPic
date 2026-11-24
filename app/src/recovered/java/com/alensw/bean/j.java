package com.alensw.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class j implements Creator {
   public LocalFolder a(Parcel var1) {
      return new LocalFolder(var1, (char)var1.readInt());
   }

   public LocalFolder[] a(int var1) {
      return new LocalFolder[var1];
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
