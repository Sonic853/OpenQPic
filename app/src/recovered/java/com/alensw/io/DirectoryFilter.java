package com.alensw.io;

import java.io.File;
import java.io.FileFilter;

final class DirectoryFilter implements FileFilter {
   @Override
   public boolean accept(File var1) {
      if (var1.isDirectory()) {
         StoragePaths.a(var1);
      } else {
         var1.delete();
      }

      return false;
   }
}
