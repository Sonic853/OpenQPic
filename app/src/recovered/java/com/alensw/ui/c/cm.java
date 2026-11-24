package com.alensw.ui.c;

class cm implements com.alensw.ui.a.s {
   final com.alensw.a.e a;
   final com.alensw.ui.c.bz b;

   cm(com.alensw.ui.c.bz var1, com.alensw.a.e var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void a(String var1) {
      var1 = com.alensw.b.l.b.g(com.alensw.b.l.b.a(var1.trim(), '.'));
      if (var1.length() != 0 && !this.a.getName().equals(var1)) {
         int var2 = this.b.l.a(this.a);
         var2 = this.b.l.a(var2, var1);
         if (var2 != -1) {
            com.alensw.a.e var4 = this.b.l.b(var2);
            if (this.b.C.q()) {
               this.b.A.clear();
               this.b.A.add(var4);
            }

            this.b.j.requestLayout();
            this.b.d(var4);
            new com.alensw.ui.c.cn(this, this.b.C, this.b.g(com.alensw.PicFolder.R.string.running_wait), 0).c();
         } else {
            this.b.a(this.a.getPath());
         }
      }
   }
}
