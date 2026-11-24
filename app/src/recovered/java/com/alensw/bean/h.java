package com.alensw.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class h implements Creator {
   public DocumentFolder a(Parcel var1) {
      return new DocumentFolder(var1, (char)var1.readInt());
   }

   public DocumentFolder[] a(int var1) {
      return new DocumentFolder[var1];
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
