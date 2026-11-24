package com.alensw.ui.c;

class cg implements Runnable {
   final com.alensw.ui.c.bz a;

   cg(com.alensw.ui.c.bz var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      if (this.a.C.q()) {
         this.a.C.d(false);
      }

      this.a.j.requestLayout();
      com.alensw.ui.a.f.a(this.a.C, 17039380, (CharSequence)this.a.g(com.alensw.PicFolder.R.string.excluded_tips));
   }
}
