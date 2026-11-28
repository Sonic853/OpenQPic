package com.alensw.bean;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.alensw.media.MediaFileTypes;
public class CommonFile implements Parcelable {
   public static CommonFile a = new CommonFile('\u0000', "", "", 0, 0L, 0L, 0L, null, Uri.EMPTY);
   protected final char b;
   protected final String c;
   protected final String d;
   protected final int e;
   protected final long f;
   protected long g;
   protected long h;
   protected String i;
   protected Uri j;

   public CommonFile(char var1, String var2, String var3, int var4, long var5, long var7, long var9, String var11, Uri var12) {
      this.b = var1;
      if (var2 == null) {
         var2 = "";
      }

      this.c = var2;
      this.d = var3;
      this.e = var4;
      this.f = var5;
      this.g = var7;
      if (var9 != 0L) {
         var7 = var9;
      }

      this.h = var7;
      this.i = var11;
      this.j = var12;
   }

   public CommonFile(Parcel var1, char var2) {
      this.b = var2;
      this.c = var1.readString();
      this.d = var1.readString();
      this.e = var1.readInt();
      this.f = var1.readLong();
      this.g = var1.readLong();
      this.h = var1.readLong();
      this.i = var1.readString();
   }

   public CommonFile(CommonFile var1) {
      this.b = var1.b;
      this.c = var1.c;
      this.d = var1.d;
      this.e = var1.e;
      this.f = var1.f;
      this.g = var1.g;
      this.h = var1.h;
      this.i = var1.i;
      this.j = var1.j;
   }

   public static char a(String var0, String var1) {
      char var2;
      if (var0 == null) {
         var2 = MediaFileTypes.a(var1);
      } else if (var0.startsWith("image/")) {
         var2 = 'I';
      } else if (var0.startsWith("video/")) {
         var2 = 'V';
      } else if ("vnd.android.document/directory".equals(var0)) {
         var2 = 'D';
      } else if (var0.startsWith("audio/")) {
         var2 = 'A';
      } else {
         var2 = MediaFileTypes.a(var1);
      }

      return var2;
   }

   public String a() {
      return this.c;
   }

   public void a(long var1, long var3) {
      this.g = var1;
      this.h = var3;
   }

   public String b() {
      return this.d;
   }

   public int c() {
      return this.e;
   }

   public long d() {
      return this.f;
   }

   @Override
   public int describeContents() {
      return 0;
   }

   public long e() {
      return this.g;
   }

   @Override
   public boolean equals(Object value) {
      if (value == this) return true;
      if (!(value instanceof CommonFile)) return false;
      CommonFile other = (CommonFile)value;
      if (this.g != other.g || this.b != other.b) return false;
      Uri uri = this.i();
      Uri otherUri = other.i();
      return uri == otherUri || uri.equals(otherUri);
   }

   public long f() {
      return this.h;
   }

   public String g() {
      return this.i;
   }

   public char h() {
      return this.b;
   }

   @Override
   public int hashCode() {
      return this.i().hashCode() + (int)this.g + this.b;
   }

   public Uri i() {
      Uri var1;
      if (this.j != null) {
         var1 = this.j;
      } else {
         var1 = Uri.EMPTY;
      }

      return var1;
   }

   public String j() {
      return null;
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      var1.writeInt(this.b);
      var1.writeString(this.c);
      var1.writeString(this.d);
      var1.writeInt(this.e);
      var1.writeLong(this.f);
      var1.writeLong(this.g);
      var1.writeLong(this.h);
      var1.writeString(this.i);
   }
}
