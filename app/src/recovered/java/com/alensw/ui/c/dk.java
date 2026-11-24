package com.alensw.ui.c;

class dk implements Runnable {
   final com.alensw.ui.c.dj a;

   dk(com.alensw.ui.c.dj var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      long var1 = com.alensw.ui.c.df.b(this.a.b);
      if (this.a.a != null) {
         this.a.a.a(var1, com.alensw.ui.c.df.c(this.a.b).j);
      }
   }
}
