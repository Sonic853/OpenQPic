package com.alensw.ui.dialog;

import com.alensw.sort.NameSortOrder;
import java.io.File;
import java.util.Comparator;

final class PickerFileNameComparator implements Comparator {
   public int a(File var1, File var2) {
      return NameSortOrder.a.a(var1.getName(), var2.getName());
   }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((File)var1, (File)var2);
   }
}
