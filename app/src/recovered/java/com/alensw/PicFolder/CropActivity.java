package com.alensw.PicFolder;

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Iterator;

public class CropActivity extends com.alensw.ui.activity.p {
   protected static int[][] a;
   protected static int[][] b;
   protected final Rect c = new Rect();
   private com.alensw.PicFolder.q d;
   private LinearLayout e;
   private Intent f;
   private boolean g;
   private boolean h;
   private boolean i;
   private int j;
   private int k;
   private int l;
   private int m;
   private int n;
   private com.alensw.a.as o;
   private com.alensw.a.as p;
   private boolean q;

   static {
      int[] var1 = new int[]{2560, 1600};
      int[] var0 = new int[]{320, 240};
      a = new int[][]{var1, {1920, 1200}, {1280, 800}, {1024, 768}, {800, 600}, {640, 480}, var0};
      var0 = new int[]{2, 3};
      var1 = new int[]{3, 4};
      int[] var2 = new int[]{3, 5};
      int[] var3 = new int[]{9, 16};
      b = new int[][]{{1, 1}, var0, var1, var2, var3};
   }

   static com.alensw.PicFolder.q a(CropActivity var0) {
      return var0.d;
   }

   private void a(Editor var1, String var2, int var3) {
      if (var3 > 0) {
         var1.putInt(var2, var3);
      } else {
         var1.remove(var2);
      }
   }

   private void a(com.alensw.b.h.j var1) {
      if (var1.k()) {
         Toast.makeText(this, com.alensw.PicFolder.R.string.load_failed, 1).show();
         this.finish();
      } else if (var1.d == 1) {
         this.n = var1.b.d;
         this.d.b(var1);
         if (this.p != null) {
            this.p.cancel(false);
         }

         this.p = QuickApp.u.a(var1.i, 'I', 4, this.A);
      } else if (var1.d > 0) {
         this.d.a(0);
         this.d.b(var1);
      }
   }

   static int b(CropActivity var0) {
      return var0.l;
   }

   @TargetApi(8)
   private View b() {
      super.M = false;
      return this.d;
   }

   static int c(CropActivity var0) {
      return var0.m;
   }

   static int d(CropActivity var0) {
      return var0.j;
   }

   static int e(CropActivity var0) {
      return var0.k;
   }

   protected void a(Menu var1) {
      if (this.d.e()) {
         ArrayList var11 = new ArrayList(a.length);
         com.alensw.PicFolder.m var13 = new com.alensw.PicFolder.m(this, var11);
         int var7 = this.d.getPictureWidth();
         int var8 = this.d.getPictureHeight();
         boolean var5;
         if (var7 >= var8) {
            var5 = true;
         } else {
            var5 = false;
         }

         float var4 = (float)var7 / var8;
         var11.clear();

         for (int var6 = 0; var6 < a.length && var4 != 0.0F; var6++) {
            int[] var12 = a[var6];
            float var2;
            if (var5) {
               var2 = var12[0];
            } else {
               var2 = var12[1];
            }

            float var3;
            if (var5) {
               var3 = var12[1];
            } else {
               var3 = var12[0];
            }

            if (var5) {
               var2 /= var7;
            } else {
               var2 = var3 / var8;
            }

            int var10 = Math.round(var7 * var2);
            int var9 = Math.round(var8 * var2);
            if (var10 * var9 < var7 * var8) {
               var1.add(0, var11.size(), 0, "" + var10 + " x " + var9).setOnMenuItemClickListener(var13);
               var11.add(var2);
            }
         }

         var1.add(0, -1, 0, this.getString(com.alensw.PicFolder.R.string.stable_keyword_default)).setOnMenuItemClickListener(var13);
      }
   }

   protected void a(boolean var1, int var2, int var3, int var4, int var5, boolean var6) {
      this.i = var1;
      this.j = var2;
      this.k = var3;
      this.l = var4;
      this.m = var5;
      float var7;
      if (var4 > 0 && var5 > 0) {
         var7 = (float)var4 / var5;
      } else {
         var7 = 0.0F;
      }

      this.d.a(var1, var2, var3, var7);
      if (var6) {
         Editor var8 = PreferenceManager.getDefaultSharedPreferences(this).edit();
         var8.putBoolean("output_crop", var1);
         this.a(var8, "output_width", var2);
         this.a(var8, "output_height", var3);
         this.a(var8, "output_ratio_x", var4);
         this.a(var8, "output_ratio_y", var5);
         var8.commit();
      }
   }

   @Override
   public boolean a(Intent var1) {
      this.f = var1;
      if (this.d.e()) {
         this.d.d(com.alensw.PicFolder.R.id.share);
      }

      return true;
   }

   protected void b(Menu menu) {
        if (this.d.e()) {
            int pictureWidth = this.d.getPictureWidth();
            int pictureHeight = this.d.getPictureHeight();
            int i = (int) (pictureWidth * pictureHeight * this.d.c * this.d.c);
            boolean z = pictureWidth >= pictureHeight;
            ArrayList arrayList = new ArrayList(b.length * 2);
            ArrayList arrayList2 = new ArrayList(a.length);
            com.alensw.PicFolder.n srcType0060 = new com.alensw.PicFolder.n(this, arrayList, arrayList2);
            SubMenu subMenuAddSubMenu = menu.addSubMenu(com.alensw.PicFolder.R.string.ratio);
            SubMenu subMenuAddSubMenu2 = menu.addSubMenu(com.alensw.PicFolder.R.string.pic_resolution);
            menu.add(1, 998, 0, com.alensw.PicFolder.R.string.crop_by_cropper).setOnMenuItemClickListener(srcType0060);
            menu.setGroupCheckable(1, true, true);
            for (int[] iArr : b) {
                int i2 = z ? iArr[1] : iArr[0];
                int i3 = z ? iArr[0] : iArr[1];
                subMenuAddSubMenu.add(2, arrayList.size() + 1000, 0, "" + i2 + " : " + i3).setOnMenuItemClickListener(srcType0060);
                arrayList.add(Long.valueOf((((long) i3) << 32) | ((long) i2)));
                if (i2 != i3) {
                    subMenuAddSubMenu.add(2, arrayList.size() + 1000, 0, "" + i3 + " : " + i2).setOnMenuItemClickListener(srcType0060);
                    arrayList.add(Long.valueOf((((long) i2) << 32) | ((long) i3)));
                }
            }
            subMenuAddSubMenu.add(2, arrayList.size() + 1000, 0, com.alensw.PicFolder.R.string.custom).setOnMenuItemClickListener(srcType0060);
            subMenuAddSubMenu.setGroupCheckable(2, true, true);
            int iIndexOf = arrayList.indexOf(Long.valueOf((((long) this.m) << 32) | ((long) this.l)));
            if (iIndexOf == -1 && this.l > 0 && this.m > 0) {
                iIndexOf = arrayList.size();
            }
            for (int[] iArr2 : a) {
                int i4 = z ? iArr2[0] : iArr2[1];
                int i5 = z ? iArr2[1] : iArr2[0];
                if (i4 * i5 <= i) {
                    subMenuAddSubMenu2.add(3, arrayList2.size() + 2000, 0, "" + i4 + " x " + i5).setOnMenuItemClickListener(srcType0060);
                    arrayList2.add(Long.valueOf(((long) i4) | (((long) i5) << 32)));
                }
            }
            subMenuAddSubMenu2.add(3, arrayList2.size() + 2000, 0, com.alensw.PicFolder.R.string.custom).setOnMenuItemClickListener(srcType0060);
            subMenuAddSubMenu2.setGroupCheckable(3, true, true);
            int iIndexOf2 = arrayList2.indexOf(Long.valueOf((((long) this.k) << 32) | ((long) this.j)));
            if (iIndexOf2 == -1 && this.j > 0 && this.k > 0) {
                iIndexOf2 = arrayList2.size();
            }
            if (iIndexOf >= 0) {
                subMenuAddSubMenu.findItem(iIndexOf + 1000).setChecked(true);
            } else if (iIndexOf2 >= 0) {
                subMenuAddSubMenu2.findItem(iIndexOf2 + 2000).setChecked(true);
            } else {
                menu.findItem(998).setChecked(true);
            }
        }
    }

   @Override
   public void finish() {
        if (this.d == null || !this.d.k) {
            this.q = true;
            QuickApp.u.b((com.alensw.b.h.j) null);
            if (this.o != null) {
                this.o.cancel(false);
                this.o = null;
            }
            if (this.p != null) {
                this.p.cancel(false);
                this.p = null;
            }
            if (this.d != null) {
                com.alensw.b.h.j srcType0202A = this.d.a((android.graphics.Bitmap.Config) null);
                this.d.c(this.n);
                this.d.a(srcType0202A, true);
                if (srcType0202A != null) {
                    srcType0202A.m();
                }
            }
            super.finish();
        }
    }

   @Override
   public boolean handleMessage(Message var1) {
      byte var4 = 0;
      byte var3 = 0;
      switch (var1.what) {
         case 30:
            if (var1.obj instanceof com.alensw.b.h.j) {
               com.alensw.b.h.j var11 = (com.alensw.b.h.j)var1.obj;
               if (!this.q) {
                  this.a(var11);
               }

               var11.m();
            }
            break;
         case 1000:
            boolean var15;
            if (var1.arg1 != 0) {
               var15 = true;
            } else {
               var15 = false;
            }

            int var12;
            if (var1.arg1 != 0) {
               var12 = var1.arg1;
            } else if (this.d.i) {
               var12 = com.alensw.PicFolder.R.string.wallpaper;
            } else {
               var12 = com.alensw.PicFolder.R.string.edit;
            }

            this.setTitle(var12);
            this.i(var15);
            if (this.e != null) {
               LinearLayout var10 = this.e;
               byte var13 = var3;
               if (var15) {
                  var13 = 8;
               }

               var10.setVisibility(var13);
            }
            break;
         case 1001:
            Intent var7 = (Intent)var1.obj;
            Uri var6 = var7.getData();
            boolean var2;
            if (var1.arg2 != 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            if (var1.arg1 == com.alensw.PicFolder.R.id.done) {
               if (var2) {
                  if (var6 != null) {
                     com.alensw.a.e var8 = QuickApp.o.a(this, var6);
                     if (var8 != null) {
                        com.alensw.a.x var9 = QuickApp.o;
                        boolean var5;
                        if (var8.c < 4) {
                           var5 = true;
                        } else {
                           var5 = false;
                        }

                        var9.a(var8, var5);
                     }

                     if (this.d.j) {
                        QuickApp.s.f(var6.getPath());
                     }
                  }
               } else if (var6 == null || !this.a(var6.getPath())) {
                  Toast.makeText(this, com.alensw.PicFolder.R.string.save_file_failed, 1).show();
               }

               var7.addFlags(1);
               var3 = var4;
               if (var2) {
                  var3 = -1;
               }

               this.setResult(var3, var7);
               this.finish();
            } else if (var1.arg1 == com.alensw.PicFolder.R.id.share) {
               if (!var2 || var6 == null) {
                  Toast.makeText(this, com.alensw.PicFolder.R.string.save_file_failed, 1).show();
               } else if (this.f != null) {
                  com.alensw.ui.activity.GalleryShareMenu.shareRenderedCrop(this, this.f, var7);
                  this.f = null;
               }
            }
      }

      return true;
   }

   @TargetApi(11)
   @Override
   public void onCreate(Bundle bundle) {
        boolean z;
        boolean z2 = false;
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        Uri data = intent.getData();
        String action = intent.getAction();
        Bundle extras = intent.getExtras();
        boolean zEquals = "android.intent.action.SET_WALLPAPER".equals(action);
        boolean z3 = (zEquals || extras == null) ? zEquals : extras.getBoolean("setWallpaper") || extras.getBoolean("set-as-wallpaper");
        if (z3) {
            setRequestedOrientation(com.alensw.b.l.b.b(this) == 2 ? 0 : 1);
        }
        boolean z4 = z3 && "com.android.camera.action.CROP".equals(action);
        if (!z4 && extras != null) {
            Iterator<String> it = extras.keySet().iterator();
            while (true) {
                if (it.hasNext()) {
                    if (it.next().contains("lockscreen")) {
                        z = true;
                        break;
                    }
                } else {
                    z = z4;
                    break;
                }
            }
        } else {
            z = z4;
        }
        if (z) {
            getPackageManager();
            String type = intent.getType();
            Intent intent2 = new Intent("com.android.camera.action.CROP");
            if (type == null) {
                type = "image/*";
            }
            intent2.setDataAndType(data, type);
            for (ResolveInfo resolveInfo : com.alensw.ui.activity.a.a((android.content.Context) this, intent2, 0)) {
                String str = resolveInfo.activityInfo.applicationInfo.packageName;
                if (str.contains(".android.gallery3d") || str.startsWith("com.cooliris.media") || str.startsWith("com.htc.album")) {
                    com.alensw.ui.activity.a.a(this, intent, resolveInfo);
                    break;
                }
            }
            finish();
            return;
        }
        this.d = new com.alensw.PicFolder.q(this);
        setContentView(b());
        if (!this.d.a(getIntent(), z3, this.A, QuickApp.s)) {
            finish();
            return;
        }
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (!H) {
            getWindow().addFlags(1024);
        }
        this.h = (this.d.g || this.d.i || this.d.l == null) ? false : true;
        this.e = (LinearLayout) View.inflate(this, com.alensw.PicFolder.R.layout.cropper_done, null);
        if (this.J != null) {
            this.J.setCustomView(this.e, new android.app.ActionBar.LayoutParams(5));
            this.J.setDisplayOptions(16, 16);
        } else if (this.K != null) {
            this.K.a(this.e, (android.view.ViewGroup.LayoutParams) new android.widget.LinearLayout.LayoutParams(-2, -1));
        }
        com.alensw.PicFolder.i srcType0055 = new com.alensw.PicFolder.i(this);
        Resources resources = getResources();
        ImageView imageView = (ImageView) this.e.findViewById(com.alensw.PicFolder.R.id.share);
        if (this.h) {
            imageView.setOnClickListener(this.Z);
            imageView.setOnLongClickListener(srcType0055);
            imageView.setImageDrawable(com.b.a.b.a(resources, com.alensw.PicFolder.R.raw.ic_menu_share, this.X));
        } else {
            imageView.setVisibility(8);
        }
        ImageView imageView2 = (ImageView) this.e.findViewById(com.alensw.PicFolder.R.id.done);
        imageView2.setOnClickListener(this.Z);
        imageView2.setOnLongClickListener(srcType0055);
        imageView2.setImageDrawable(com.b.a.b.a(resources, com.alensw.PicFolder.R.raw.ic_menu_done, this.X));
        this.g = !this.d.f && this.d.a == 0.0f;
        if (this.g) {
            a(defaultSharedPreferences.getBoolean("output_crop", true), defaultSharedPreferences.getInt("output_width", 0), defaultSharedPreferences.getInt("output_height", 0), defaultSharedPreferences.getInt("output_ratio_x", 0), defaultSharedPreferences.getInt("output_ratio_y", 0), false);
        }
        if (this.d.e()) {
            return;
        }
        com.alensw.a.ao srcType0088 = QuickApp.u;
        com.alensw.b.h.j srcType0202A = srcType0088.a(data);
        com.alensw.b.h.j srcType0202D = srcType0088.d();
        if (srcType0202D != null && (srcType0202A == null || !srcType0202D.a(data))) {
            srcType0202D.m();
            srcType0202D = null;
        }
        srcType0088.b();
        if (srcType0202A != null) {
            this.n = srcType0202A.b.d;
        }
        if (srcType0202A != null && srcType0202D != null) {
            z2 = true;
        }
        if (z2) {
            this.d.b(srcType0202A);
            this.d.b(srcType0202D);
        } else {
            this.d.a(com.alensw.PicFolder.R.string.loading);
            this.o = srcType0088.a(data, 'I', com.alensw.b.h.n.a ? 1 : 4, this.A);
        }
        if (srcType0202A != null) {
            srcType0202A.m();
        }
        if (srcType0202D != null) {
            srcType0202D.m();
        }
    }

   @TargetApi(11)
   @Override
   public boolean onCreateOptionsMenu(Menu var1) {
      com.alensw.b.g.b.a(this, com.alensw.PicFolder.R.menu.cropper, var1);
      if (this.g) {
         this.a(var1, com.alensw.PicFolder.R.id.shrink, 1);
         this.a(var1, com.alensw.PicFolder.R.id.crop, 1);
      } else {
         var1.removeItem(com.alensw.PicFolder.R.id.shrink);
         var1.removeItem(com.alensw.PicFolder.R.id.crop);
      }

      this.a(var1, com.alensw.PicFolder.R.id.rotate, 2);
      this.a(var1, com.alensw.PicFolder.R.id.flip, 2);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var4 = false;
      boolean var3 = false;
      if (!this.d.k) {
         int var2 = var1.getItemId();
         if (var2 == 16908332 || var2 == com.alensw.PicFolder.R.id.home) {
            this.setResult(0, new Intent());
            this.finish();
            var3 = true;
         } else if (this.d.e()) {
            View var7 = this.c(var2);
            switch (var2) {
               case com.alensw.PicFolder.R.id.done:
                  this.d.d(var2);
                  break;
               case com.alensw.PicFolder.R.id.share:
                  Intent var5 = this.getIntent();
                  Intent var6 = new Intent("android.intent.action.SEND");
                  var6.setType(var5.getType());
                  var6.putExtra("android.intent.extra.STREAM", var5.getData());
                  com.alensw.ui.activity.a.a(this, var6, com.alensw.PicFolder.R.string.share, var7, new com.alensw.PicFolder.l(this));
                  break;
               case com.alensw.PicFolder.R.id.shrink:
                  com.alensw.b.g.b.a(this, var7, new com.alensw.PicFolder.j(this));
                  break;
               case com.alensw.PicFolder.R.id.crop:
                  com.alensw.b.g.b.a(this, var7, new com.alensw.PicFolder.k(this));
                  break;
               case com.alensw.PicFolder.R.id.rotate:
                  com.alensw.ui.activity.a.c(this, var7);
                  break;
               case com.alensw.PicFolder.R.id.flip:
                  com.alensw.ui.activity.a.b(this, var7);
                  break;
               case com.alensw.PicFolder.R.id.flip_horz:
               case com.alensw.PicFolder.R.id.flip_vert:
                  com.alensw.PicFolder.q var8 = this.d;
                  var3 = var4;
                  if (var2 == com.alensw.PicFolder.R.id.flip_horz) {
                     var3 = true;
                  }

                  var8.a(var3);
                  break;
               case com.alensw.PicFolder.R.id.rotate_left:
                  this.d.b(-90);
                  break;
               case com.alensw.PicFolder.R.id.rotate_right:
                  this.d.b(90);
                  break;
               case com.alensw.PicFolder.R.id.rotate_180:
                  this.d.b(180);
            }

            var3 = true;
         }
      }

      return var3;
   }

   @Override
   public void onStart() {
      super.onStart();
      this.d.b(0, this.U);
   }
}
