package com.alensw.bean;

import android.os.Parcel;
import android.os.Parcelable.Creator;

final class DocumentFileCreator implements Creator {
   public DocumentFile a(Parcel var1) {
      return new DocumentFile(var1, (char)var1.readInt());
   }

   public DocumentFile[] a(int var1) {
      return new DocumentFile[var1];
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
