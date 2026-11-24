package com.alensw.b.c;

import java.io.File;
import java.io.FileFilter;

final class c implements FileFilter {
   @Override
   public boolean accept(File var1) {
      if (var1.isDirectory()) {
         com.alensw.b.c.b.a(var1);
      } else {
         var1.delete();
      }

      return false;
   }
}
