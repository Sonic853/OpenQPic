package com.alensw.ui.view;

class ai implements Runnable {
   final ImageGridView a;

   ai(ImageGridView var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.isPressed() && this.a.isShown()) {
         int var6 = this.a.getScrollY();
         int var5 = this.a.getScrollRangeY();
         int var7 = this.a.getHeight();
         float var1 = ImageGridView.k(this.a) * 8.0F;
         float var4 = ImageGridView.k(this.a) * 64.0F;
         float var2 = this.a.getPaddingTop() + var4;
         float var3 = this.a.getPaddingBottom() + var4;
         if (ImageGridView.l(this.a) < ImageGridView.m(this.a) && ImageGridView.l(this.a) < var2) {
            var5 = Math.min(Math.round((var2 - ImageGridView.l(this.a)) / var4 * var1), var6);
            this.a.scrollBy(0, -var5);
         } else if (ImageGridView.l(this.a) > ImageGridView.m(this.a) && ImageGridView.l(this.a) > var7 - var3) {
            var5 = Math.min(Math.round((ImageGridView.l(this.a) - (var7 - var3)) / var4 * var1), var5 - var6);
            this.a.scrollBy(0, var5);
         }

         var6 = this.a.b(ImageGridView.n(this.a), ImageGridView.l(this.a));
         if (var6 != -1) {
            if (var6 >= ImageGridView.a(this.a)) {
               var7 = ImageGridView.a(this.a);
               var5 = var6 + 1;
               var6 = var7;
            } else {
               var5 = ImageGridView.a(this.a);
            }

            if (ImageGridView.c(this.a) != null && this.a.isShown()) {
               boolean var13;
               if (Math.abs(ImageGridView.o(this.a) - ImageGridView.p(this.a)) > 1) {
                  var13 = true;
               } else {
                  var13 = false;
               }

               if (var13) {
                  ImageGridView.c(this.a).a(ImageGridView.o(this.a), ImageGridView.p(this.a), false);
               }

               if (var13 || ImageGridView.o(this.a) != var6 || ImageGridView.p(this.a) != var5) {
                  ImageGridView.c(this.a).a(var6, var5, true);
               }
            }

            ImageGridView.c(this.a, var6);
            ImageGridView.d(this.a, var5);
         }

         this.a.a(this);
      }
   }
}
