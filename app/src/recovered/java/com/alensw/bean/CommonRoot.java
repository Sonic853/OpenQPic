package com.alensw.bean;

import android.graphics.drawable.Drawable;
import android.net.Uri;

public class CommonRoot extends CommonFile {
   public int l;
   public int m;
   public String n;
   public String o;
   public Drawable p;

   public CommonRoot(String var1, String var2, int var3) {
      super('\u0000', var1, var2, var3, 0L, 0L, 0L, null, null);
   }

   @Override
   public boolean equals(Object value) {
      if (value == this) return true;
      if (!(value instanceof CommonRoot)) return false;
      CommonRoot other = (CommonRoot)value;
      if (this.n != null) return this.n == other.n || this.n.equals(other.n);
      Uri uri = this.i();
      Uri otherUri = other.i();
      return uri == otherUri || uri.equals(otherUri);
   }

   public CommonFolder k() {
      return new CommonFolder(this);
   }
}
