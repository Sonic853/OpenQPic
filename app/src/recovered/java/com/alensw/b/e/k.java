package com.alensw.b.e;

import org.apache.http.impl.DefaultHttpServerConnection;

class k extends Thread {
   final DefaultHttpServerConnection a;
   final com.alensw.b.e.j b;

   k(com.alensw.b.e.j var1, DefaultHttpServerConnection var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.a(this.a);
   }
}
