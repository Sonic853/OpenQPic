package com.alensw.ui.view;

class ae implements Runnable {
   final ImageGridView a;

   ae(ImageGridView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
        if (ImageGridView.f(this.a) == null && !this.a.o.c()) {
            boolean z = ImageGridView.h(this.a) >= 0;
            int i = ImageGridView.i(this.a) - ImageGridView.j(this.a);
            this.a.a(Math.max(z ? ImageGridView.i(this.a) : ImageGridView.j(this.a) - i, 0), Math.min(z ? ImageGridView.i(this.a) + i : ImageGridView.j(this.a), this.a.getCount()), false);
            return;
        }
        long jG = ImageGridView.g(this.a) - System.currentTimeMillis();
        ImageGridView imageGridView = this.a;
        if (jG <= 0) {
            jG = 300;
        }
        imageGridView.postDelayed(this, jG);
    }
}
