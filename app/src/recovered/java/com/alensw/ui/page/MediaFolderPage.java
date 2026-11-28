package com.alensw.ui.page;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Paint.Align;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.alensw.PicFolder.GalleryStorage;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.bean.CommonFolder;
import com.alensw.cache.ConcurrentLruCache;
import com.alensw.concurrent.AsyncExecutor;
import com.alensw.concurrent.CancellableFutureTask;
import com.alensw.documents.DocumentProviderAccess;
import com.alensw.gallery.FolderCoverBitmap;
import com.alensw.gallery.FolderQueryCallback;
import com.alensw.gallery.FolderSortSettings;
import com.alensw.gallery.QueryFolderTask;
import com.alensw.gallery.ThumbnailCallback;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.image.PictureBitmap;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.GalleryChromeActivity;
import com.alensw.ui.activity.GalleryIntentActivity;
import com.alensw.ui.dialog.GalleryDialogs;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.view.ImageGridAdapter;
import com.alensw.ui.view.ImageGridView;
import com.alensw.utils.GalleryUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class MediaFolderPage extends MediaActionPage {
   public static final AsyncExecutor A = new AsyncExecutor(2, 3, 8, 5);
   public static final HashMap z = new HashMap(16);
   private String B;
   private PictureBitmap L;
   private CancellableFutureTask M;
   private ConcurrentLruCache O;
   private int P;
   private final Rect Q;
   private final ThumbnailCallback R;
   private final ImageGridAdapter S;
   private final Runnable T;
   private final ContentObserver U;
   private final FolderQueryCallback V;
   protected ImageGridView f;
   protected ViewGroup g;
   protected TextView h;
   protected int i;
   protected int j;
   protected int k;
   protected boolean l;
   protected boolean n;
   protected boolean o;
   protected boolean p;
   protected boolean q = true;
   protected boolean r;
   protected final LinkedHashSet s = new LinkedHashSet(256);
   protected final Paint t = new Paint(1);
   protected final Paint u = new Paint(1);
   protected int v;
   protected int w;
   protected int x;
   protected CommonFolder y;

   public MediaFolderPage() {
      this.Q = new Rect();
      this.R = new MediaFolderThumbnailCallback(this);
      this.S = new MediaFolderGridAdapter(this);
      this.T = new RefreshMediaFolder(this);
      this.U = new MediaFolderObserver(this, this.b);
      this.V = new MediaFolderQueryListener(this);
   }

   public static Intent a(CommonFolder var0, boolean var1, String var2) {
      Intent var3 = new Intent();
      var3.setData(var0.i());
      var3.putExtra("QuickPic.folder", var0);
      var3.putExtra("QuickPic.is_root", var1);
      if (var2 != null) {
         var3.putExtra("QuickPic.root_id", var2);
      }

      return var3;
   }

   static ConcurrentLruCache a(MediaFolderPage var0) {
      return var0.O;
   }

   static CancellableFutureTask a(MediaFolderPage var0, CancellableFutureTask var1) {
      var0.M = var1;
      return var1;
   }

   static void a(MediaFolderPage var0, String var1) {
      var0.f(var1);
   }

   static void b(MediaFolderPage var0) {
      var0.y();
   }

   static Runnable c(MediaFolderPage var0) {
      return var0.T;
   }

   static String d(MediaFolderPage var0) {
      return var0.B;
   }

   private void f(String var1) {
      ContentResolver var2 = this.C.getContentResolver();
      Uri var3 = this.y.i();
      new CreateDocumentFolderDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.add), 0, var2, var3, var1).c();
   }

   private void w() {
      GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.new_folder, "", null, new SubmitDocumentFolderName(this));
   }

   private void y() {
      if (this.y != null && (this.y.c() & 1) != 0) {
         if (this.H == 2) {
            this.O.c(this.y);
         }

         QuickApp.r.a(this.y.i().toString());
      }
   }

   @Override
   public long a(boolean var1, RectF var2) {
      return this.f.a(var1, this.C.b(var1), var2);
   }

   @Override
   public void a() {
      super.a();
      if (this.P != 0) {
         this.D.setPadding(this.D.getPaddingLeft(), this.D.getPaddingTop() - this.P, this.D.getPaddingRight(), this.D.getPaddingBottom());
         this.P = 0;
      }

      SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(this.C);
      this.f.setHorizontal(var1.getBoolean("horizontal_scroll", this.f.c));
      if (this.a(this.i)) {
         if (this.o) {
            this.y.s();
         }

         this.O.c();
      }

      this.r = false;
      if (this.q) {
         this.q = false;
         this.n();
      } else {
         this.f.requestLayout();
      }

      this.l();
   }

   @Override
   public void a(int var1, int var2, int var3, int var4) {
      super.a(var1, var2, var3, var4);
      this.C.a(this.Q);
      this.f.setPadding(this.Q.left, this.Q.top, this.Q.right, this.Q.bottom);
      if (this.g != null) {
         this.g.setPadding(this.Q.left, this.Q.top, this.Q.right, 0);
      }
   }

   protected void a(int var1, CommonFile var2) {
      if (this.y.a(this.y.k) != var2) {
         this.y.k = this.y.a(var2);
      }

      if (var2.h() != 'D') {
         View var3 = this.C.c(var1);
         switch (var1) {
            case com.alensw.PicFolder.R.id.details:
               new MediaDetails(this.C, var2, null).a(Integer.toString(this.y.k + 1) + "/" + this.y.o(), new MediaFolderDateChanged(this, var2));
               break;
            case com.alensw.PicFolder.R.id.edit:
               new MediaDetails(this.C, var2, null).a(var3, null);
               break;
            case com.alensw.PicFolder.R.id.set_as:
               new MediaDetails(this.C, var2, null).b(var3, null);
               break;
            case com.alensw.PicFolder.R.id.show_in_map:
               new MediaDetails(this.C, var2, null).b(var3);
         }
      }
   }

   @Override
   public void a(Bundle var1) {
      super.a(var1);
      super.h(com.alensw.PicFolder.R.layout.browser);
      this.y = (CommonFolder)this.E.getParcelableExtra("QuickPic.folder");
      if (this.y == null) {
         this.E();
      } else {
         this.O = QuickApp.w;
         this.n = this.E.getBooleanExtra("QuickPic.is_root", false);
         this.B = this.E.getStringExtra("QuickPic.root_id");
         if (this.B == null) {
            this.B = DocumentProviderAccess.d(this.C.getContentResolver(), this.y.i());
         }

         int var2 = this.y.c();
         boolean var3;
         if ((-2147483648 & var2) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.l = var3;
         if ((268435456 & var2) == 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.o = var3;
         if ((134217728 & var2) != 0) {
            var3 = true;
         } else {
            var3 = false;
         }

         this.p = var3;
         if (this.n && !this.l) {
            this.n = false;
         }

         if (this.p) {
            Typeface var4 = Typeface.create("sans-serif-light", 0);
            if (var4 != null) {
               this.u.setTypeface(var4);
            }

            Typeface var5 = Typeface.create("sans-serif-thin", 0);
            if (var5 != null) {
               this.t.setTypeface(var5);
            } else if (var4 != null) {
               this.t.setTypeface(var4);
            }
         }

         this.t.setTextAlign(Align.CENTER);
         this.u.setTextAlign(Align.CENTER);
         this.b((CharSequence) this.y.a());
         String var6 = this.y.g();
         if (var6 == null || var6.length() <= 0) {
            var6 = null;
         }

         this.c((CharSequence) var6);
         this.i = GalleryIntentActivity.b(this.E);
         this.c = (ViewGroup)this.f(com.alensw.PicFolder.R.id.bottom_bar);
         this.f = (ImageGridView)this.f(com.alensw.PicFolder.R.id.grid);
         this.f.setFastScrollEnabled(true);
         this.f.setSpaceWidth(QuickApp.h / 320 * 2);
         this.y.l = FolderSortSettings.a(this.C).a(this.y);
         this.j = this.y.k();
         this.k = this.y.l();
         this.b(this.j);
         this.y.a(this.C.getContentResolver(), this.U);
      }
   }

   public void a(View var1) {
      Intent var3 = this.s();
      if (var3 != null) {
         if ("android.intent.action.SEND".equals(var3.getAction())) {
            Uri var2 = (Uri)var3.getParcelableExtra("android.intent.extra.STREAM");
            if (var2 != null) {
               new MediaDetails(this.C, var2, '\u0000', var3.getType(), null).a(var1);
            }
         } else {
            GalleryActions.a(this.C, var3, com.alensw.PicFolder.R.string.share, var1, new ShareMediaFolderSelection(this));
         }
      }
   }

   public void a(CommonFile var1) {
      QuickApp.t.a(var1, this.l, this.R);
   }

   public void a(CommonFolder var1) {
      this.C.a(MediaFolderPage.class, a(var1, false, this.B), null);
   }

   public void a(CharSequence var1) {
      if (this.g == null) {
         this.g = (ViewGroup)View.inflate(this.C, com.alensw.PicFolder.R.layout.message_bar, null);
         ((ViewGroup)this.D).addView(this.g, -1, -2);
         this.h = (TextView)this.g.findViewById(com.alensw.PicFolder.R.id.message);
         ImageView var2 = (ImageView)this.g.findViewById(com.alensw.PicFolder.R.id.message_close);
         var2.setImageDrawable(GalleryUtils.a(this.F(), com.alensw.PicFolder.R.raw.ic_menu_cancel, this.h.getTextColors().getDefaultColor()));
         var2.setOnClickListener(new DismissFolderBanner(this));
      }

      this.h.setText(var1);
      if (this.g.getVisibility() != 0) {
         this.g.setPadding(this.Q.left, this.Q.top, this.Q.right, 0);
         this.g.setVisibility(0);
         this.g.startAnimation(this.C.c(true));
      }
   }

   public void a(Collection var1) {
      for (Object var2 : var1) {
         QuickApp.w.c(var2);
      }

      this.s.removeAll(var1);
      this.y.a(var1);
      this.f.requestLayout();
   }

   public void a(boolean var1) {
      z.put(this.y.i(), this.f.getScrollPos());
      this.f.c();
      QuickApp.t.a(true);
      if (var1) {
         this.O.c();
      } else {
         this.v();
      }
   }

   @TargetApi(11)
   @Override
   public boolean a(Menu var1) {
      boolean var3 = false;
      boolean var2 = false;
      if (!this.f.d) {
         GalleryMenu.a(this.C, com.alensw.PicFolder.R.menu.common_folder, var1);
         if (this.i == 0) {
            this.a(var1, com.alensw.PicFolder.R.id.search, 2);
            this.a(var1, com.alensw.PicFolder.R.id.slideshow, 2);
         }

         Uri var4 = this.y.i();
         if ((this.y.c() & 8) != 0) {
            this.a(var1, com.alensw.PicFolder.R.id.add, 1);
         } else {
            var1.removeItem(com.alensw.PicFolder.R.id.add);
         }

         if (this.n) {
            var2 = true;
         } else {
            var2 = false;
         }

         this.a(var1, com.alensw.PicFolder.R.id.search, var2);
         this.a(var1, com.alensw.PicFolder.R.id.sort, this.o);
         var2 = var3;
         if (this.n) {
            var2 = true;
         }

         this.a(var1, com.alensw.PicFolder.R.id.transfer, var2);
         this.a(var1, com.alensw.PicFolder.R.id.settings, this.n);
         if (!DocumentProviderAccess.d(var4)) {
            var1.removeItem(com.alensw.PicFolder.R.id.desktop_link);
         }

         var2 = true;
      }

      return var2;
   }

   @TargetApi(11)
   @Override
   public boolean a(MenuItem var1) {
      int var3;
      boolean var7;
      int var2 = 2;
      var7 = true;
      int var4 = var1.getItemId();
      View var8 = this.C.c(var4);
      int var5 = this.s.size();
      var3 = this.y.l;
      label119:
      switch (var4) {
         case 16908319:
            if (this.f.d) {
               if (this.s.size() < this.y.o()) {
                  this.s.addAll(this.y.p());
               } else {
                  this.s.clear();
               }

               this.f.b();
               if (this.C.q()) {
                  this.C.a(Integer.toString(this.s.size()) + "/" + this.y.o(), false);
               }
            }
            break;
         case com.alensw.PicFolder.R.id.share:
            this.a(var8);
            break;
         case com.alensw.PicFolder.R.id.add:
            this.w();
            break;
         case com.alensw.PicFolder.R.id.details:
         case com.alensw.PicFolder.R.id.edit:
         case com.alensw.PicFolder.R.id.set_as:
         case com.alensw.PicFolder.R.id.show_in_map:
            if (this.s.size() > 0) {
               this.a(var4, (CommonFile)this.s.iterator().next());
            }
            break;
         case com.alensw.PicFolder.R.id.browse_mode:
            GalleryMenu.a(this.C, var8, new MediaFolderBrowseMenu(this));
            break;
         case com.alensw.PicFolder.R.id.browse_grid:
            this.j = 0;
            break;
         case com.alensw.PicFolder.R.id.browse_list:
            this.j = 2;
            break;
         case com.alensw.PicFolder.R.id.delete:
            GalleryActions.a(this.C, var5, var8, new ConfirmMediaFolderDelete(this));
            break;
         case com.alensw.PicFolder.R.id.move_to:
            if (a && this.d()) {
               break;
            }
         case com.alensw.PicFolder.R.id.copy_to:
            HashSet var10 = this.u();
            boolean var22;
            if (var4 == com.alensw.PicFolder.R.id.copy_to) {
               var22 = true;
            } else {
               var22 = false;
            }

            this.a(var10, var22, new MediaFolderCopyMoveResult(this));
            break;
         case com.alensw.PicFolder.R.id.search:
            if (this.B != null) {
               this.c(this.B);
            }
            break;
         case com.alensw.PicFolder.R.id.slideshow:
            this.b(true);
            break;
         case com.alensw.PicFolder.R.id.sort:
            boolean var21;
            label112: {
               label111: {
                  label110: {
                     switch (this.k) {
                        case 1:
                           var21 = false;
                           break label110;
                        case 2:
                           var21 = true;
                           break label111;
                        case 3:
                           var21 = true;
                           break label110;
                        case 4:
                           var21 = false;
                           break label111;
                        case 5:
                        case 6:
                        default:
                           var21 = false;
                           var2 = 0;
                           break label112;
                        case 7:
                           var21 = true;
                           break;
                        case 8:
                           var21 = false;
                     }

                     var2 = com.alensw.PicFolder.R.id.sort_by_size;
                     break label112;
                  }

                  var2 = com.alensw.PicFolder.R.id.sort_by_date;
                  break label112;
               }

               var2 = com.alensw.PicFolder.R.id.sort_by_name;
            }

            GalleryActions.a(this.C, var8, new int[]{com.alensw.PicFolder.R.id.sort_by_name, com.alensw.PicFolder.R.id.sort_by_date, com.alensw.PicFolder.R.id.sort_by_size}, var2, var21);
            break;
         case com.alensw.PicFolder.R.id.desktop_link:
            var4 = this.y.o();

            for (int var13 = 0; var13 < var4; var13++) {
               CommonFile var9 = this.y.a(var13);
               if (var9 != null && (var9.c() & 1) != 0) {
                  this.a(this.y.a(), this.y.i(), this.y.b(var13));
                  break label119;
               }
            }
            break;
         case com.alensw.PicFolder.R.id.rotate:
            GalleryActions.c(this.C, var8);
            break;
         case com.alensw.PicFolder.R.id.select:
            if ((this.y.c() & 2) != 0 && !this.C.q()) {
               this.C.d(true);
               this.m();
            }
            break;
         case com.alensw.PicFolder.R.id.rotate_left:
            this.c(-90);
            break;
         case com.alensw.PicFolder.R.id.rotate_right:
            this.c(90);
            break;
         case com.alensw.PicFolder.R.id.rotate_180:
            this.c(180);
            break;
         case com.alensw.PicFolder.R.id.sort_by_name:
            if (this.k == 2) {
               var2 = 4;
            }

            this.k = var2;
            break;
         case com.alensw.PicFolder.R.id.sort_by_date:
            if (this.k == 1) {
               var2 = (byte)3;
            } else {
               var2 = (byte)1;
            }

            this.k = var2;
            break;
         case com.alensw.PicFolder.R.id.sort_by_size:
            if (this.k == 7) {
               var2 = (byte)8;
            } else {
               var2 = (byte)7;
            }

            this.k = var2;
            break;
         default:
            return super.a(var1);
      }

      var4 = CommonFolder.a(this.j, this.k);
      boolean var6 = var7;
      if (var4 != var3) {
         var3 = this.y.k();
         var2 = this.y.l();
         this.y.l = var4;
         FolderSortSettings.a(this.C).b(this.y);
         if (this.j != var3) {
            this.j = this.y.k();
            this.b(this.j);
            var4 = this.f.getWidth();
            var3 = this.f.getHeight();
            if (var4 > 0 && var3 > 0) {
               this.f.a(true, this.C.b(true), new RectF(var4 / 4, var3 / 4, var4 * 3 / 4, var3 * 3 / 4));
            }
         }

         var6 = var7;
         if (this.k != var2) {
            this.k = this.y.l();
            this.a(false);
            this.y.s();
            this.n();
            var6 = var7;
         }
      }

      return var6;
   }

   @Override
   public void b() {
      super.b();
      this.a(false);
   }

   protected void b(int i) {
        int i2;
        switch (i) {
            case 2:
                int[] iArrE = e();
                this.w = iArrE[0];
                this.x = iArrE[1];
                this.v = 0;
                i2 = 2;
                break;
            default:
                this.w = this.l ? QuickApp.r.f : QuickApp.r.d;
                this.x = this.l ? QuickApp.r.g : QuickApp.r.e;
                this.v = 0;
                i2 = 1;
                break;
        }
        if (this.L != null) {
            this.L.m();
            this.L = null;
        }
        this.f.a(this.S, this.w, this.x);
        this.f.a(i2, this.l);
    }

   @Override
   public void b(Intent var1) {
      if (var1 != null && this.y != null) {
         Uri var2 = var1.getData();
         if (var2 != null) {
            if (!var2.equals(this.y.b(this.y.k))) {
               this.y.k = this.y.a(var2);
            }

            this.f.c(this.y.k);
         }
      }

      CommonFile var3 = this.y.a(this.y.k);
      if (var3 instanceof CommonFolder && ((CommonFolder)var3).n()) {
         this.y.c(this.y.k);
         this.f.requestLayout();
      }
   }

   @Override
   public void b(Bundle var1) {
   }

   public void b(boolean var1) {
      if (var1) {
         this.y.k = -1;
      }

      CommonFolder var4 = this.y.a(false);
      var4.a(this.y, false);
      CommonFolder var3 = var4;
      if (var4.o() == this.y.o()) {
         var3 = this.y;
      }

      var4 = var3;
      if (var3.n()) {
         var4 = var3;
         if (var1) {
            int var2 = 0;

            while (true) {
               if (var2 >= this.y.o()) {
                  var4 = var3;
                  break;
               }

               CommonFile var9 = this.y.a(var2);
               if (var9 instanceof CommonFolder) {
                  var4 = (CommonFolder)var9;
                  if (!var4.n()) {
                     break;
                  }

                  var3 = var4;
               }

               var2++;
            }
         }
      }

      if (!var4.n()) {
         if (!this.C.s()) {
            int[] var5 = new int[2];
            View var6 = this.D;
            var6.getLocationOnScreen(var5);
            var6.setPadding(var6.getPaddingLeft(), var6.getPaddingTop() + var5[1], var6.getPaddingRight(), var6.getPaddingBottom());
            this.P = var5[1];
         } else {
            this.P = 0;
         }

         this.r = true;
         Intent var7 = MediaViewerPage.a(var4, var1);
         this.C.a(MediaViewerPage.class, var7, null);
      }
   }

   @Override
   public boolean b(Menu var1) {
      boolean var2;
      if (this.f.d) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   public void c(int var1) {
      HashSet var3 = this.u();
      int var2 = var3.size();
      if (var2 != 0) {
         this.a(false);
         new RotateMediaFolderItemsDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.rotate) + ": " + var1, var2, var3, var1).c();
      }
   }

   @TargetApi(11)
   @Override
   protected boolean c(Menu var1) {
      GalleryMenu.a(this.C, com.alensw.PicFolder.R.menu.picture, var1);
      var1.add(0, 16908319, 0, 17039373);
      if (GalleryChromeActivity.F) {
         for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
            var1.getItem(var2).setShowAsAction(0);
         }
      }

      this.a(var1, com.alensw.PicFolder.R.id.share, 2);
      this.a(var1, com.alensw.PicFolder.R.id.delete, 2);
      this.a(var1, com.alensw.PicFolder.R.id.rotate, 2);
      var1.removeItem(com.alensw.PicFolder.R.id.rename);
      var1.removeItem(com.alensw.PicFolder.R.id.slideshow);
      var1.removeItem(com.alensw.PicFolder.R.id.settings);
      this.s.clear();
      this.f.setMultiSelect(true);
      return true;
   }

   @Override
   protected boolean d(Menu menu) {
        int size = this.s.size();
        char cH = size == 1 ? ((CommonFile) this.s.iterator().next()).h() : (char) 0;
        boolean z2 = cH == 'I';
        a(menu, com.alensw.PicFolder.R.id.rotate, cH != 'D');
        a(menu, com.alensw.PicFolder.R.id.details, cH == 'I' || cH == 'V');
        a(menu, com.alensw.PicFolder.R.id.edit, z2);
        a(menu, com.alensw.PicFolder.R.id.set_as, z2);
        a(menu, com.alensw.PicFolder.R.id.show_in_map, z2);
        menu.findItem(16908319).setTitle(size == this.f.getCount() ? com.alensw.PicFolder.R.string.deselect_all : 17039373);
        return true;
    }

   @Override
   public void g() {
      super.g();
      ContentResolver var1 = this.C.getContentResolver();
      this.y.b(var1, this.U);
      this.y.b(var1);
      this.b.removeCallbacks(this.T);
      if (this.M != null) {
         this.M.cancel(false);
         this.M = null;
      }

      if (this.L != null) {
         this.L.m();
         this.L = null;
      }
   }

   @Override
   public RectF h() {
      RectF var1 = new RectF();
      this.f.c(this.y.k);
      if (this.y.a(this.y.k).h() == 'I') {
         this.f.a(this.y.k);
      }

      if (this.y.k >= 0 && this.f.b(this.y.k, var1)) {
         var1.offset(0.0F, this.P);
      } else {
         var1 = super.h();
      }

      return var1;
   }

   @Override
   public void i() {
      this.f.a(-1);
      super.i();
   }

   @Override
   protected Uri[] j() {
      return this.r();
   }

   @Override
   protected void k() {
      this.s.clear();
      this.f.setMultiSelect(false);
      this.m();
   }

   protected boolean l() {
      boolean var1 = false;
      if (this.C.q() && this.s.isEmpty()) {
         this.C.d(false);
      }

      if (this.m() > 0) {
         var1 = true;
      }

      return var1;
   }

   protected int m() {
      int var1 = this.y.o();
      if (this.f.d) {
         String var2 = Integer.toString(this.s.size()) + "/" + var1;
         if (this.C.q()) {
            this.C.a(var2, true);
         } else {
            this.b((CharSequence) var2);
         }
      } else {
         String var3 = this.y.a();
         String var4 = var3;
         if (var1 > 0) {
            var4 = var3;
            if (!this.n) {
               var4 = var3;
               if (!this.p) {
                  var4 = var3 + " (" + var1 + ")";
               }
            }
         }

         this.b((CharSequence) var4);
      }

      return var1;
   }

   public void n() {
      if (!this.y.n()) {
         Float var1 = (Float)z.remove(this.y.i());
         if (var1 != null) {
            this.f.setScrollPos(var1);
         }
      }

      if (this.M == null) {
         this.c(true);
         this.f.setEmptyText(this.g(com.alensw.PicFolder.R.string.loading));
         this.M = A.a(new QueryFolderTask(this.C.getContentResolver(), this.y, false, null, this.b, this.V));
      }
   }

   public void o() {
      HashSet var4 = this.u();
      int var1 = var4.size();
      if (var1 != 0) {
         this.a(false);
         ThumbnailDatabase var2 = QuickApp.r;
         ContentResolver var3 = this.C.getContentResolver();
         ArrayList var5 = new ArrayList(var1);
         new DeleteMediaFolderItemsDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.delete), var1, var4, var3, var2, var5).c();
      }
   }

   public Uri[] r() {
      int var1 = this.s.size();
      Uri[] var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = new Uri[var1];
         Iterator var3 = this.s.iterator();

         for (int var4 = 0; var3.hasNext(); var4++) {
            var2[var4] = ((CommonFile)var3.next()).i();
         }
      }

      return var2;
   }

   public Intent s() {
      int var5 = this.s.size();
      Intent var7;
      if (var5 == 0) {
         var7 = null;
      } else {
         ArrayList var8 = new ArrayList(var5);
         Iterator var6 = this.s.iterator();
         int var4 = 0;

         while (var6.hasNext()) {
            CommonFile var10 = (CommonFile)var6.next();
            char var1 = var10.h();
            int var3;
            if (var1 == 'I') {
               var3 = var4 | 1;
            } else {
               char var2 = var10.h();
               var3 = var4;
               var1 = var2;
               if (var2 == 'V') {
                  var3 = var4 | 2;
                  var1 = var2;
               }
            }

            var8.add(MediaDetails.a(var10.i(), var1));
            var4 = var3;
         }

         String var9;
         if (var4 == 1) {
            var9 = "image/jpeg";
         } else if (var4 == 2) {
            var9 = "video/mp4";
         } else {
            var9 = "*/*";
         }

         if (var5 > 1) {
            var7 = new Intent("android.intent.action.SEND_MULTIPLE");
            var7.putParcelableArrayListExtra("android.intent.extra.STREAM", var8);
         } else {
            var7 = new Intent("android.intent.action.SEND");
            var7.putExtra("android.intent.extra.STREAM", (Parcelable)var8.get(0));
         }

         GalleryStorage.clipStreams(var7, var8);
         var7.setType(var9);
         var7.addFlags(1);
      }

      return var7;
   }

   protected PictureBitmap t() {
      if (this.L == null) {
         this.L = FolderCoverBitmap.a(this.F(), this.v, this.w, this.x, this.C.U);
      }

      return this.L;
   }

   public HashSet u() {
      LinkedHashSet var1 = new LinkedHashSet(this.s.size());
      var1.addAll(this.s);
      return var1;
   }

   public void v() {
      int var2 = this.f.getFirstVisible();
      int var3 = this.f.getLastVisible();

      for (int var1 = var2; var1 < var3; var1++) {
         this.O.b(this.y.a(var1));
      }

      if (var3 > var2) {
         this.O.b(var3 - var2);
      }
   }
}
