package com.alensw.gallery;

import com.alensw.sort.NameSortOrder;
import java.util.Comparator;

final class MediaSizeComparator implements Comparator {
   public int a(ScannedMediaFile var1, ScannedMediaFile var2) {
      int var4 = (int)(var1.f - var2.f);
      int var3 = var4;
      if (var4 == 0) {
         var3 = NameSortOrder.a.a(var1.b, var2.b);
      }

      return var3;
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((ScannedMediaFile)var1, (ScannedMediaFile)var2);
   }
}
