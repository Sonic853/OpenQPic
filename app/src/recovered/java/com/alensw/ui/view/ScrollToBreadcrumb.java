package com.alensw.ui.view;

class ScrollToBreadcrumb implements Runnable {
   final PathBar a;

   ScrollToBreadcrumb(PathBar var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      int var1 = PathBar.b(this.a);
      int var2 = this.a.getWidth() - this.a.getPaddingLeft() - this.a.getPaddingRight();
      if (var2 > 0) {
         PathBar var3 = this.a;
         if (var1 > var2) {
            var1 -= var2;
         } else {
            var1 = 0;
         }

         var3.smoothScrollTo(var1, 0);
      }
   }
}
