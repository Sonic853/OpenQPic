package com.alensw.ui.c;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.text.format.Formatter;
import com.alensw.PicFolder.CropActivity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.bean.CommonFolder;
import java.util.GregorianCalendar;

class ae implements com.alensw.ui.view.aj {
   final com.alensw.ui.c.z a;
   private final GregorianCalendar b;
   private final char[] c;
   private int d;

   ae(com.alensw.ui.c.z var1) {
      this.a = var1;
      this.b = new GregorianCalendar();
      this.c = new char[32];
   }

   private int a(char[] var1, int var2, int var3) {
      var2 = this.b(var1, var2 * 1000 + var3, 0);
      var1[var1.length - 3] = '/';
      return var2;
   }

   private int b(char[] var1, int var2, int var3) {
      int var6 = var1.length;
      int var4 = var2;
      var2 = var6;

      while (true) {
         int var5 = var2;
         if (var4 == 0) {
            while (var6 - var5 < var3) {
               var1[--var5] = '0';
            }

            return var5;
         }

         var5 = var4 / 10;
         var1[--var2] = (char)(var4 - var5 * 10 + 48);
         var4 = var5;
      }
   }

   @Override
   public int a() {
      return this.a.y.o();
   }

   @Override
   public String a(int var1) {
      CommonFile var2 = this.a.y.a(var1);
      String var3;
      if (var2 != null) {
         if (this.a.k == 3 || this.a.k == 1) {
            var3 = QuickApp.a(var2.f() * 1000L);
         } else if (this.a.k != 7 && this.a.k != 8) {
            var3 = this.a.b(var2.a());
         } else {
            var3 = Formatter.formatFileSize(this.a.C, var2.d());
         }
      } else {
         var3 = null;
      }

      return var3;
   }

   @Override
   public void a(int var1, int var2, int var3) {
      boolean var4 = true;
      if (this.a.H == 2) {
         if (var3 != 0) {
            if (var3 < 0) {
               var4 = false;
            }
         } else if (this.d < 0) {
            var4 = false;
         }

         if (this.d != var3) {
            this.d = var3;
            QuickApp.t.a(false);
         }

         for (int var7 = 0; var7 < var2 - var1; var7++) {
            CommonFolder var6 = this.a.y;
            int var5;
            if (var4) {
               var5 = var1 + var7;
            } else {
               var5 = var2 - 1 - var7;
            }

            CommonFile var8 = var6.a(var5);
            if (var8 != null && (var8.c() & 1) != 0 && !com.alensw.ui.c.z.a(this.a).a(var8)) {
               this.a.a(var8);
            }
         }
      }
   }

   @Override
   public void a(int var1, int var2, boolean var3) {
      while (var1 < var2) {
         CommonFile var4 = this.a.y.a(var1);
         if (var4 != null && var4.h() != 'D') {
            if (var3) {
               this.a.s.add(var4);
            } else {
               this.a.s.remove(var4);
            }
         }

         var1++;
      }

      this.a.f.b();
      this.a.m();
   }

   @Override
   public void a(int var1, Canvas var2, RectF var3) {
      CommonFile var10 = this.a.y.a(var1);
      if (var10 != null) {
         this.a.t.setColor(this.a.C.U & 16777215 | -872415232);
         var2.drawRect(var3, this.a.t);
         this.b.setTimeInMillis(var10.f() * 1000L);
         var1 = this.b.get(1);
         int var8 = this.b.get(2);
         int var9 = this.b.get(5);
         char[] var15 = this.c;
         float var6 = var3.height();
         float var7 = var6 / 2.0F;
         float var4 = 2.0F * var7 / 7.0F;
         float var5 = var4 / 3.0F;
         this.a.t.setColor(this.a.C.Y);
         this.a.t.setTextSize(var7);
         var9 = this.b(var15, var9, 2);
         var6 = var3.top + (var6 - (var7 + var4 + var5)) / 2.0F + var7;
         var2.drawText(var15, var9, var15.length - var9, var3.centerX(), var6 - this.a.t.descent(), this.a.t);
         this.a.u.setColor(this.a.C.Y);
         this.a.u.setTextSize(var4);
         var1 = this.a(var15, var1, var8 + 1);
         var2.drawText(var15, var1, var15.length - var1, var3.centerX(), var6 + var4 + var5 - this.a.u.descent(), this.a.u);
      }
   }

   @Override
   public void a(int var1, com.alensw.ui.view.ak var2) {
      CommonFile var6 = this.a.y.a(var1);
      if (var6 != null) {
         int var3 = var6.c();
         char var4 = var6.h();
         if ((var3 & 1) != 0) {
            var2.a = (com.alensw.b.h.h)com.alensw.ui.c.z.a(this.a).b(var6);
         } else if (var4 == 'D') {
            var2.a = this.a.t();
         }

         if (var2.a != null) {
            var2.a.l();
         }

         boolean var7;
         if (this.a.j == 2) {
            var7 = true;
         } else {
            var7 = false;
         }

         if (var4 != 'I' || var7) {
            var2.c = var6.a();
            String var5 = var6.g();
            if (var7) {
               var2.d[0] = "";
               var2.d[1] = var5;
               if (var5 == null) {
                  if (this.a.k != 7 && this.a.k != 8) {
                     var2.d[1] = QuickApp.b(1000L * var6.f());
                  } else {
                     var2.d[1] = Formatter.formatFileSize(this.a.C, var6.d());
                  }
               }
            } else {
               var2.d[0] = var5;
            }
         }

         if (this.a.f.d && this.a.s.contains(var6)) {
            var2.b |= 2;
         }

         if ((134217728 & var3) != 0) {
            var2.b |= 16;
         }
      }
   }

   @Override
   public void b(int var1) {
      if (this.a.H == 2) {
         this.a.y.k = var1;
         CommonFile var4 = this.a.y.a(var1);
         char var2 = var4.h();
         if (this.a.f.d) {
            if (this.a.s.contains(var4)) {
               this.a.s.remove(var4);
            } else if (var2 != 'D') {
               this.a.s.add(var4);
            }

            this.a.f.d(var1);
            if (this.a.C.q() && this.a.s.isEmpty()) {
               this.a.C.d(false);
            } else {
               this.a.m();
            }
         } else if (this.a.i != 0) {
            Intent var3 = this.a.C.getIntent();
            if (var2 == 'I' && var3.hasExtra("crop")) {
               Intent var5 = new Intent(var3.getAction(), var4.i());
               var5.setClass(this.a.C, CropActivity.class);
               var5.putExtras(var3);
               com.alensw.ui.activity.a.a(this.a.C, var5, 2);
            } else {
               this.a.a(var4.i(), var4.b());
            }
         } else if (var2 == 'D') {
            this.a.a((CommonFolder)var4);
         } else if (var2 == 'I') {
            this.a.b(false);
         } else if (var2 == 'V') {
            com.alensw.ui.c.as.a(this.a.C, var4);
         }
      }
   }

   @Override
   public void c(int var1) {
      if (this.a.H == 2 && this.a.i == 0 && (this.a.y.c() & 2) != 0) {
         CommonFile var2 = this.a.y.a(var1);
         if (var2 != null && var2.h() != 'D' && !this.a.C.q()) {
            this.a.C.d(true);
            this.a(var1, var1 + 1, true);
         }
      }
   }
}
