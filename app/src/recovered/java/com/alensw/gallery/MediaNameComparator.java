package com.alensw.gallery;

import com.alensw.sort.NameSortOrder;
import java.util.Comparator;

final class MediaNameComparator implements Comparator {
   public int a(ScannedMediaFile var1, ScannedMediaFile var2) {
      return NameSortOrder.a.a(var1.b, var2.b);
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((ScannedMediaFile)var1, (ScannedMediaFile)var2);
   }
}
