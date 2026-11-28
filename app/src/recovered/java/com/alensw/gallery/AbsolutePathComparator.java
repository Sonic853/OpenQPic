package com.alensw.gallery;

import java.util.Comparator;

final class AbsolutePathComparator implements Comparator {
   public int a(ScannedFolder var1, ScannedFolder var2) {
      return GalleryFolders.a(var1, var2);
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((ScannedFolder)var1, (ScannedFolder)var2);
   }
}
