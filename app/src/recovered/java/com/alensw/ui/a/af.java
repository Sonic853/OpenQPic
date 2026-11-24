package com.alensw.ui.a;

import java.io.File;
import java.util.Comparator;

final class af implements Comparator {
   public int a(File var1, File var2) {
      return com.alensw.b.b.b.a.a(var1.getName(), var2.getName());
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((File)var1, (File)var2);
   }
}
