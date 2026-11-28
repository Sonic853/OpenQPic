package com.alensw.gallery;

import java.util.Comparator;

final class ReverseFolderDateComparator implements Comparator {
   public int a(ScannedFolder var1, ScannedFolder var2) {
      int var3 = GalleryFolders.e(var1, var2);
      if (var3 == 0) {
         var3 = GalleryFolders.b(var2, var1);
      }

      return var3;
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((ScannedFolder)var1, (ScannedFolder)var2);
   }
}
