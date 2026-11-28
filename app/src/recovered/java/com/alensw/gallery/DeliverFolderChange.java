package com.alensw.gallery;

class DeliverFolderChange implements Runnable {
   final boolean a;
   final FolderQueryObserver b;

   DeliverFolderChange(FolderQueryObserver var1, boolean var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      QueryFolderTask.e(this.b.b).a(QueryFolderTask.c(this.b.b), this.a);
   }
}
