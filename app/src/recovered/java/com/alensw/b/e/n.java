package com.alensw.b.e;

import android.util.Log;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.apache.http.impl.DefaultHttpServerConnection;

class n extends Thread {
   final com.alensw.b.e.j a;

   n(com.alensw.b.e.j var1) {
      this.a = var1;
   }

   @Override
   public void run() {
      ServerSocket var1 = com.alensw.b.e.j.a(this.a);

      do {
         try {
            Socket var2 = var1.accept();
            var2.setSoTimeout(15000);
            DefaultHttpServerConnection var3 = new DefaultHttpServerConnection();
            var3.bind(var2, com.alensw.b.e.j.b(this.a));
            this.a.b(var3);
         } catch (IOException var4) {
            if (!var1.isClosed()) {
               Log.e("HttpServer", "server loop: ", var4);
            }
         }
      } while (!var1.isClosed());

      Log.d("HttpServer", "server quit");
   }
}
