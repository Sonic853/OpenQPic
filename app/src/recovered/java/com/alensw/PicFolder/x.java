package com.alensw.PicFolder;

import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Message;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.util.Locale;

class x implements Runnable {
   final RectF a;
   final Matrix b;
   final Matrix c;
   final boolean d;
   final Context e;
   final ContentResolver f;
   final int g;
   final com.alensw.PicFolder.q h;

   x(com.alensw.PicFolder.q var1, RectF var2, Matrix var3, Matrix var4, boolean var5, Context var6, ContentResolver var7, int var8) {
      this.h = var1;
      this.a = var2;
      this.b = var3;
      this.c = var4;
      this.d = var5;
      this.e = var6;
      this.f = var7;
      this.g = var8;
   }

   private boolean a() {
      float var2 = this.a.width();
      float var3 = this.a.height();
      float[] var13 = new float[9];
      this.b.getValues(var13);
      float var1 = com.alensw.b.h.j.a(var13);
      int var4;
      if (this.h.n > 0) {
         var4 = this.h.n;
      } else {
         var4 = Math.round(this.h.c * var2 / var1);
      }

      int var5;
      if (this.h.o > 0) {
         var5 = this.h.o;
      } else {
         var5 = Math.round(var3 * this.h.c / var1);
      }

      if (this.h.q != null) {
         if (this.h.q.getWidth() == var4 && this.h.q.getHeight() == var5) {
            this.h.q.eraseColor(0);
         } else {
            this.h.q.recycle();
            this.h.q = null;
         }
      }

      if (this.h.q == null) {
         this.h.a(var4, var5);
      }

      boolean var12;
      if (this.h.q == null) {
         var12 = false;
      } else {
         com.alensw.b.h.j var26 = this.h.getFullPicture();
         if (var26 == null) {
            var12 = false;
         } else {
            Paint var14 = new Paint(6);
            Canvas var15 = new Canvas(this.h.q);
            var2 = this.h.q.getWidth() / var2;
            var15.scale(var2, var2);
            var15.translate(-this.a.left, -this.a.top);
            if (!(var26 instanceof com.alensw.b.h.n)) {
               var26.a(var15, this.b, null, null, var14, false);
            } else {
               com.alensw.b.h.j var16 = this.h.getPicture();
               if (var16 != null) {
                  var16.a(var15, this.c, null, null, var14, false);
                  var16.m();
               }

               com.alensw.b.h.n var27 = (com.alensw.b.h.n)var26;
               var4 = com.alensw.b.h.n.a(1.0F / var1 / var2, 1.0F);
               int var6 = 1 << var4;
               int var7 = com.alensw.b.h.m.b << var4;
               RectF var17 = new RectF(this.a);
               Matrix var18 = new Matrix();
               this.b.invert(var18);
               var18.mapRect(var17);
               int var8 = var4 + com.alensw.b.h.m.a;
               int var10 = Math.max((int)Math.floor(var17.left), 0);
               var4 = Math.max((int)Math.floor(var17.top), 0);
               int var9 = Math.min((int)Math.ceil(var17.right), var27.e());
               int var11 = Math.min((int)Math.ceil(var17.bottom), var27.f());
               com.alensw.b.h.a var19 = new com.alensw.b.h.a();
               Matrix var20 = new Matrix();
               Rect var28 = new Rect();

               for (int var24 = var4 >> var8 << var8; var24 < var11; var24 += var7) {
                  for (int var25 = var10 >> var8 << var8; var25 < var9; var25 += var7) {
                     var28.left = var25;
                     var28.right = var25 + var7;
                     var28.top = var24;
                     var28.bottom = var24 + var7;
                     var19.inSampleSize = var6;
                     Bitmap var29 = var27.a(var28, var19);
                     if (var29 != null) {
                        var20.setScale(var6, var6);
                        var20.postTranslate(var28.left, var28.top);
                        var20.postConcat(this.b);
                        var15.drawBitmap(var29, var20, var14);
                        com.alensw.b.h.n.b(var29);
                     }
                  }
               }
            }

            var26.m();
            var12 = true;
         }
      }

      return var12;
   }

   public boolean a(Intent intent) {
        Uri uriFromFile;
        File file;
        OutputStream outputStreamE;
        com.alensw.b.c.f srcType0150A;
        String lowerCase;
        Uri pictureUri = this.h.getPictureUri();
        Uri uri = this.h.m;
        if (uri == null) {
            String path = this.h.l != null ? this.h.l : pictureUri.getPath();
            if (this.h.r != null) {
                lowerCase = this.h.r.toString().toLowerCase(Locale.ENGLISH);
            } else {
                String strA = com.alensw.b.l.b.a(path, true);
                if ("png".equals(strA) || "bmp".equals(strA)) {
                    this.h.r = android.graphics.Bitmap.CompressFormat.PNG;
                    lowerCase = "png";
                } else {
                    this.h.r = android.graphics.Bitmap.CompressFormat.JPEG;
                    lowerCase = "jpg";
                }
            }
            File fileC = this.d ? com.alensw.b.c.b.c(this.e, path) : com.alensw.b.l.b.a(new File(path), "." + lowerCase);
            if (fileC != null) {
                com.alensw.b.l.b.c(fileC);
                uriFromFile = Uri.fromFile(fileC);
                intent.setDataAndType(uriFromFile, "image/" + lowerCase);
                file = fileC;
            } else {
                intent.setDataAndType(uri, "image/" + lowerCase);
                uriFromFile = uri;
                file = fileC;
            }
        } else {
            uriFromFile = uri;
            file = null;
        }
        if (uriFromFile != null) {
            try {
                try {
                    outputStreamE = this.f.openOutputStream(uriFromFile);
                } catch (Throwable th) {
                    outputStreamE = file != null ? com.alensw.b.l.b.e(file) : null;
                }
                if (outputStreamE != null) {
                    BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStreamE);
                    if (this.h.r == null) {
                        this.h.r = android.graphics.Bitmap.CompressFormat.JPEG;
                    }
                    this.h.q.compress(this.h.r, 90, bufferedOutputStream);
                    bufferedOutputStream.close();
                    outputStreamE.close();
                }
                if (file != null && (srcType0150A = com.alensw.b.c.f.a(this.f, pictureUri)) != null) {
                    com.alensw.b.h.c srcType0195A = com.alensw.b.h.c.a(srcType0150A, true);
                    if (srcType0195A != null) {
                        com.alensw.b.c.f srcType0150D = com.alensw.b.l.b.d(file);
                        if (srcType0150D != null) {
                            srcType0195A.a(srcType0150D);
                            srcType0150D.h();
                        }
                        srcType0195A.a();
                    }
                    srcType0150A.h();
                }
                if (this.h.p != 0) {
                    try {
                        file.setLastModified(this.h.p);
                    } catch (Throwable th2) {
                    }
                }
                return true;
            } catch (Throwable th3) {
            }
        }
        return false;
    }

   @Override
   public void run() {
      byte var1 = 1;
      byte var2 = 1;
      Intent var4 = new Intent();
      Message var3 = this.h.s.obtainMessage(1001, this.g, 0, var4);
      if (this.a()) {
         if (this.h.g) {
            var4.putExtra("data", this.h.q);
            if (this.h.q != null) {
               var1 = var2;
            } else {
               var1 = 0;
            }

            var3.arg2 = var1;
            this.h.q = null;
         } else if (this.h.i) {
            WallpaperManager var7 = WallpaperManager.getInstance(this.e);

            try {
               var7.setBitmap(this.h.q);
               var3.arg2 = 1;
            } catch (Throwable var5) {
            }
         } else {
            if (!this.a(var4)) {
               var1 = 0;
            }

            var3.arg2 = var1;
         }
      }

      this.h.k = false;
      this.h.s.post(new com.alensw.PicFolder.y(this, var3));
   }
}
