package com.alensw.gallery;

class FolderScanStamp {
   int a;
   int b;

   public FolderScanStamp(int var1, int var2) {
      this.a = var1;
      this.b = var2;
   }

   @Override
   public boolean equals(Object var1) {
        if (!(var1 instanceof FolderScanStamp)) {
            return false;
        }
        FolderScanStamp srcType0073 = (FolderScanStamp) var1;
        return this.a == srcType0073.a && this.b == srcType0073.b;
    }
}
