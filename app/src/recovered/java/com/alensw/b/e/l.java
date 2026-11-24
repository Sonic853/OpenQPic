package com.alensw.b.e;

import android.util.Log;
import java.io.IOException;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

class l implements HttpRequestHandler {
   final com.alensw.b.e.j a;

   l(com.alensw.b.e.j var1) {
      this.a = var1;
   }

   @Override
   public void handle(HttpRequest var1, HttpResponse var2, HttpContext var3) {
      try {
         this.a.a(var1, var2, var3);
      } catch (Exception var6) {
         if (var6 instanceof HttpException || var6 instanceof IOException) {
            throw com.alensw.compat.LegacyPlatform.propagate(var6);
         }
         com.alensw.b.e.j.a(var1, var2, 500, var6.getMessage());
         Log.e("HttpServer", "handle request: ", var6);
      }
   }
}
