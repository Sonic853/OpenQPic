package com.alensw.b.e;

import android.util.Log;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.RequestLine;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

public class j {
   private ServerSocket a;
   private Thread b;
   private final int c;
   private final HttpService d;
   private final HttpParams e;

   public j(int var1) {
      BasicHttpProcessor var2 = new BasicHttpProcessor();
      var2.addInterceptor(new ResponseDate());
      var2.addInterceptor(new ResponseServer());
      var2.addInterceptor(new ResponseContent());
      var2.addInterceptor(new ResponseConnControl());
      this.e = new BasicHttpParams();
      this.e.setIntParameter("http.socket.timeout", 15000);
      this.e.setIntParameter("http.socket.buffer-size", 16384);
      this.e.setBooleanParameter("http.connection.stalecheck", false);
      this.e.setBooleanParameter("http.tcp.nodelay", true);
      HttpRequestHandlerRegistry var3 = new HttpRequestHandlerRegistry();
      this.a(var3);
      this.c = var1;
      this.d = new HttpService(var2, new DefaultConnectionReuseStrategy(), new DefaultHttpResponseFactory());
      this.d.setHandlerResolver(var3);
      this.d.setParams(this.e);
   }

   public static String a(Header var0) {
      String var1;
      if (var0 != null) {
         var1 = var0.getValue();
      } else {
         var1 = null;
      }

      return var1;
   }

   public static String a(HttpRequest var0, String var1) {
      return a(var0.getFirstHeader(var1));
   }

   static ServerSocket a(com.alensw.b.e.j var0) {
      return var0.a;
   }

   public static InputStreamEntity a(InputStream var0, long var1) {
      return new com.alensw.b.e.m(var0, var1);
   }

   public static void a(com.alensw.b.e.i var0, long var1, String var3, HttpResponse var4) {
      long var6;
      long var8;
      label47: {
         var6 = -1L;
         String var14 = var3;
         if (var3 != null) {
            var14 = var3;
            if (var3.startsWith("bytes=")) {
               var3 = var3.substring("bytes=".length());
               int var5 = var3.indexOf(45);
               var14 = var3;
               if (var5 > 0) {
                  try {
                     var8 = Long.parseLong(var3.substring(0, var5));
                  } catch (NumberFormatException var16) {
                     var8 = 0L;
                     break label47;
                  }

                  long var10;
                  try {
                     var10 = Long.parseLong(var3.substring(var5 + 1));
                  } catch (NumberFormatException var15) {
                     break label47;
                  }

                  var6 = var10;
                  break label47;
               }
            }
         }

         var8 = 0L;
         var3 = var14;
      }

      Log.d("HttpServer", "range=" + var3 + "/" + var1);
      var4.addHeader("Accept-Ranges", "bytes");
      var4.addHeader("Content-Type", var0.c);
      if (var3 == null || var8 < 0L) {
         var4.setEntity(a(var0.e, var1));
         var4.setStatusCode(200);
      } else if (var8 >= var1) {
         var4.addHeader("Content-Range", "bytes 0-0/" + var1);
         var4.setStatusCode(416);
      } else {
         long var12 = var0.a;
         long var18 = var12;
         if (var12 <= 0L) {
            var18 = var1 - var8;
         }

         var12 = var6;
         if (var6 < 0L) {
            var12 = var8 + var18 - 1L;
         }

         var4.addHeader("Content-Range", "bytes " + var8 + "-" + var12 + "/" + var1);
         var4.setEntity(a(var0.e, var18));
         var4.setStatusCode(206);
      }
   }

   protected static void a(String var0, Map var1) {
      StringTokenizer var4 = new StringTokenizer(var0, "&");

      while (var4.hasMoreTokens()) {
         String var3 = var4.nextToken();
         int var2 = var3.indexOf(61);
         if (var2 >= 0) {
            var1.put(URLDecoder.decode(var3.substring(0, var2)).trim(), URLDecoder.decode(var3.substring(var2 + 1)));
         } else {
            var1.put(URLDecoder.decode(var3).trim(), "");
         }
      }
   }

   public static void a(HttpRequest var0, HttpResponse var1, int var2, String var3) {
      if (var3 != null) {
         var1.setStatusLine(new BasicStatusLine(var0.getProtocolVersion(), var2, var3));
      } else {
         var1.setStatusCode(var2);
      }
   }

   public static String b(String var0, Map var1) {
      int var2 = var0.indexOf(63);
      if (var2 >= 0) {
         a(var0.substring(var2 + 1), var1);
         var0 = URLDecoder.decode(var0.substring(0, var2));
      } else {
         var0 = URLDecoder.decode(var0);
      }

      return var0;
   }

   static HttpParams b(com.alensw.b.e.j var0) {
      return var0.e;
   }

   protected void a(String var1, Map var2, HttpRequest var3, HttpResponse var4, HttpContext var5) {
   }

   protected void a(HttpRequest var1, HttpResponse var2, HttpContext var3) {
      RequestLine var6 = var1.getRequestLine();
      HashMap var5 = new HashMap(5);
      String var4 = b(var6.getUri(), var5);
      String var7 = var6.getMethod();
      if ("GET".equalsIgnoreCase(var7)) {
         this.a(var4, var5, var1, var2, var3);
      } else if ("POST".equalsIgnoreCase(var7)) {
         this.b(var4, var5, var1, var2, var3);
      } else {
         Log.w("HttpServer", "don't handle: " + var7);
      }
   }

   protected void a(DefaultHttpServerConnection param1) {
      try {
         HttpContext context = new org.apache.http.protocol.BasicHttpContext();
         context.setAttribute("RemoteAddress", param1.getRemoteAddress());
         while (param1.isOpen() && !Thread.interrupted()) {
            d.handleRequest(param1, context);
         }
      } catch (java.net.SocketException failure) {
         Log.w("HttpServer", "socket exception");
      } catch (java.net.SocketTimeoutException failure) {
         Log.w("HttpServer", "socket timeout");
      } catch (Exception failure) {
         Log.e("HttpServer", "handle request: ", failure);
      } finally {
         try {
            param1.close();
         } catch (Exception failure) {
            Log.e("HttpServer", "close connection: ", failure);
         }
      }
   }

   protected void a(HttpRequestHandlerRegistry var1) {
      var1.register("*", new com.alensw.b.e.l(this));
   }

   protected void b(String var1, Map var2, HttpRequest var3, HttpResponse var4, HttpContext var5) {
   }

   protected void b(DefaultHttpServerConnection var1) {
      new com.alensw.b.e.k(this, var1).start();
   }

   protected InetSocketAddress bindAddress(int var1) {
      return new InetSocketAddress(var1);
   }

   public void c() {
      if (this.b == null) {
         try {
            ServerSocket var1 = new ServerSocket();
            this.a = var1;
            this.a.bind(this.bindAddress(this.c));
            com.alensw.b.e.n var3 = new com.alensw.b.e.n(this);
            this.b = var3;
            this.b.setDaemon(true);
            this.b.start();
         } catch (Exception var2) {
            Log.e("HttpServer", "start: ", var2);
         }
      }
   }

   public void d() {
      if (this.a != null) {
         try {
            this.a.close();
         } catch (Exception var3) {
            Log.e("HttpServer", "close socket: ", var3);
         }
      }

      if (this.b != null) {
         try {
            this.b.join();
            this.b = null;
            this.a = null;
         } catch (Exception var2) {
            Log.e("HttpServer", "stop: ", var2);
         }
      }
   }

   public int e() {
      ServerSocket var2 = this.a;
      int var1;
      if (var2 != null) {
         var1 = var2.getLocalPort();
      } else {
         var1 = 0;
      }

      return var1;
   }

   public boolean f() {
      ServerSocket var2 = this.a;
      Thread var3 = this.b;
      boolean var1;
      if (var2 != null && !var2.isClosed() && var3 != null && var3.isAlive()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
