package com.alensw.a;

class be {
   private com.alensw.b.a.a a = new com.alensw.b.a.a(128);

   private be() {
   }

   be(com.alensw.a.bd unused) {
      this();
   }

   public int a() {
      return this.a.a();
   }

   public void a(String param1, int param2, long param3, byte[] param5) {
        switch (param2) {
            case 1:
                com.alensw.a.bf srcType0106 = (com.alensw.a.bf) this.a.b(param1);
                if (srcType0106 != null) {
                    srcType0106.a = param2;
                    srcType0106.c = param5;
                    return;
                }
                break;
            case 2:
                com.alensw.a.bf srcType0107 = (com.alensw.a.bf) this.a.b(param1);
                if (srcType0107 != null) {
                    if (srcType0107.a != 1) {
                        srcType0107.a = param2;
                    }
                    srcType0107.b = param3;
                    return;
                }
                break;
            case 3:
                this.a.c(param1);
                break;
            case 5:
                this.a.c();
                break;
        }
        this.a.a(param1, new com.alensw.a.bf(param1, param2, param3, param5));
        synchronized (this) {
            notify();
        }
    }

   public byte[] a(String var1) {
      com.alensw.a.bf var2 = (com.alensw.a.bf)this.a.b(var1);
      byte[] var3;
      if (var2 != null) {
         if (var2.c != null) {
            var3 = var2.c;
         } else {
            var3 = com.alensw.a.bc.e();
         }
      } else {
         var3 = null;
      }

      return var3;
   }

   public com.alensw.a.bf b() {
      return (com.alensw.a.bf)this.a.b();
   }
}
