package com.alensw.ui.page;

class RefreshViewedLocalFolder implements Runnable {
   final LocalViewerPage a;

   RefreshViewedLocalFolder(LocalViewerPage var1) {
      this.a = var1;
   }

   @Override
   public void run() {
        if (this.a.H == 2) {
            if (LocalViewerPage.a(this.a).m()) {
                this.a.a(LocalViewerPage.a(this.a), (Runnable) null);
            }
            this.a.b.postDelayed(this, 60000L);
        }
    }
}
