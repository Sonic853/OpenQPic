package com.alensw.ui.page;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.alensw.PicFolder.GalleryStorage;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.FolderSortSettings;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.gallery.ScannedFolder;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.gallery.ThumbnailCallback;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.media.MediaFileTypes;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.GalleryChromeActivity;
import com.alensw.ui.activity.GalleryIntentActivity;
import com.alensw.ui.dialog.GalleryDialogs;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.menu.GalleryMenuItem;
import com.alensw.ui.view.ImageGridAdapter;
import com.alensw.ui.view.ImageGridView;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

public class LocalFolderPage extends MediaActionPage {
   protected static final HashMap l = new HashMap(16);
   public static final String[] m = new String[]{"%Y", "%M", "%D", "%h", "%m", "%s"};
   protected ImageGridView f;
   protected ScannedFolder g;
   protected int h;
   protected int i;
   protected int j;
   protected final LinkedHashSet k = new LinkedHashSet(256);
   private int n;
   private final Rect o = new Rect();
   private final ThumbnailCallback p = new LocalThumbnailCallback(this);
   private final ImageGridAdapter q = new LocalFileGridAdapter(this);

   private void a(ScannedMediaFile var1) {
      QuickApp.t.a(var1, false, this.p);
   }

   static void a(LocalFolderPage var0, ScannedMediaFile var1) {
      var0.a(var1);
   }

   @Override
   public long a(boolean var1, RectF var2) {
      return this.f.a(var1, this.C.b(var1), var2);
   }

   @Override
   public void a() {
      super.a();
       ReceivedMediaUpdates.observe(this);
      if (this.n != 0) {
         this.D.setPadding(this.D.getPaddingLeft(), this.D.getPaddingTop() - this.n, this.D.getPaddingRight(), this.D.getPaddingBottom());
         this.n = 0;
      }

      SharedPreferences var1 = PreferenceManager.getDefaultSharedPreferences(this.C);
      this.f.setHorizontal(var1.getBoolean("horizontal_scroll", this.f.c));
      if (this.a(this.h)) {
         this.g.k();
      }

      this.o();
   }

   @Override
   public void a(int var1, int var2, int var3, int var4) {
      super.a(var1, var2, var3, var4);
      this.C.a(this.o);
      this.f.setPadding(this.o.left, this.o.top, this.o.right, this.o.bottom);
   }

   @Override
   public void a(int var1, int var2, Intent var3) {
      if (this.h != 0 && var1 == 2 && var2 == -1) {
         this.C.setResult(var2, var3);
         this.C.finish();
      }
   }

   protected void a(int var1, ScannedMediaFile var2) {
      Uri var5 = this.g.b(var2.b);
      String var4 = MediaFileTypes.a(var2.b, var2.c);
      if (this.g.e(this.g.c) != var2) {
         this.g.c = this.g.a(var2);
      }

      View var3 = this.C.c(var1);
      switch (var1) {
         case com.alensw.PicFolder.R.id.details:
            new MediaDetails(this.C, var5, var2.c, var4, null).a(Integer.toString(this.g.c + 1) + "/" + this.g.j(), new LocalFileDateChanged(this, var2));
            break;
         case com.alensw.PicFolder.R.id.rename:
            if (!a || !this.d()) {
               this.c(this.g.a(var2));
            }
            break;
         case com.alensw.PicFolder.R.id.edit:
            new MediaDetails(this.C, var5, var2.c, var4, null).a(var3, null);
            break;
         case com.alensw.PicFolder.R.id.set_as:
            new MediaDetails(this.C, var5, var2.c, var4, null).b(var3, null);
            break;
         case com.alensw.PicFolder.R.id.show_in_map:
            new MediaDetails(this.C, var5, var2.c, var4, null).b(var3);
      }
   }

   public void a(Uri var1, String var2, boolean var3) {
      if (!this.C.s()) {
         int[] var4 = new int[2];
         View var5 = this.D;
         var5.getLocationOnScreen(var4);
         var5.setPadding(var5.getPaddingLeft(), var5.getPaddingTop() + var4[1], var5.getPaddingRight(), var5.getPaddingBottom());
         this.n = var4[1];
      } else {
         this.n = 0;
      }

      Intent var6 = new Intent();
      var6.setDataAndType(var1, var2);
      if (var3) {
         var6.putExtra("QuickPic.slide_show", true);
      }

      this.C.a(LocalViewerPage.class, var6, null);
   }

   @Override
   public void a(Bundle var1) {
      super.a(var1);
      super.h(com.alensw.PicFolder.R.layout.browser);
      Uri var3 = this.E.getData();
      Uri var2 = var3;
      if (var3 == null) {
         var2 = var3;
         if (var1 != null) {
            var2 = (Uri)var1.getParcelable("current_uri");
         }
      }

      this.g = QuickApp.o.a(this.C, var2);
      if (this.g == null) {
         Toast.makeText(this.C, com.alensw.PicFolder.R.string.load_failed, 1).show();
         this.E();
      } else {
         this.h = GalleryIntentActivity.b(this.E);
         this.c = (ViewGroup)this.f(com.alensw.PicFolder.R.id.bottom_bar);
         this.f = (ImageGridView)this.f(com.alensw.PicFolder.R.id.grid);
         this.f.setFastScrollEnabled(true);
         this.f.setSpaceWidth(QuickApp.h / 320 * 2);
         if (this.h == 5) {
            this.f.setMultiSelect(true);
         }

         this.i = this.g.a();
         this.j = this.g.d();
         this.b(this.i);
      }
   }

   public void a(View var1) {
      Intent var2 = this.r();
      if (var2 != null) {
         if ("android.intent.action.SEND".equals(var2.getAction())) {
            Uri var3 = (Uri)var2.getParcelableExtra("android.intent.extra.STREAM");
            if (var3 != null) {
               new MediaDetails(this.C, var3, '\u0000', var2.getType(), null).a(var1);
            }
         } else {
            GalleryActions.a(this.C, var2, com.alensw.PicFolder.R.string.share, var1, new ShareLocalSelection(this));
         }
      }
   }

   public void a(Collection var1) {
      for (Object var3 : var1) {
         QuickApp.w.c(var3);
      }

      this.k.removeAll(var1);
      this.g.a(var1);
      this.f.requestLayout();
   }

   public void a(boolean var1) {
      l.put(this.g.getPath(), this.f.getScrollPos());
      this.f.c();
      QuickApp.t.a(true);
      if (var1) {
         QuickApp.w.c();
      } else {
         this.t();
      }
   }

   @TargetApi(11)
   @Override
   public boolean a(Menu var1) {
      boolean var2 = true;
      if (this.h == 5) {
         MenuItem var3 = var1.add(0, com.alensw.PicFolder.R.id.done, 0, 17039370);
         GalleryMenuItem.a(this.C, var3);
         this.a(var3, 2);
         var1.add(0, com.alensw.PicFolder.R.id.browse_mode, 0, com.alensw.PicFolder.R.string.browse_mode);
         var1.add(0, com.alensw.PicFolder.R.id.sort, 0, com.alensw.PicFolder.R.string.sort);
         var1.add(0, 16908319, 0, 17039373);
      } else if (this.f.d) {
         var2 = false;
      } else {
         GalleryMenu.a(this.C, com.alensw.PicFolder.R.menu.folder, var1);
         if (this.h == 0) {
            this.a(var1, com.alensw.PicFolder.R.id.slideshow, 2);
            this.a(var1, com.alensw.PicFolder.R.id.select, 2);
         } else {
            this.a(var1, com.alensw.PicFolder.R.id.slideshow, false);
            this.a(var1, com.alensw.PicFolder.R.id.select, false);
         }

         var1.removeItem(com.alensw.PicFolder.R.id.cover);
         var1.removeItem(com.alensw.PicFolder.R.id.delete);
         var1.removeItem(com.alensw.PicFolder.R.id.top);
      }

      return var2;
   }

   @TargetApi(11)
   @Override
   public boolean a(MenuItem var1) {
      boolean var11 = false;
      boolean var6 = false;
      boolean var7 = false;
      boolean var8 = false;
      boolean var5 = false;
      boolean var10 = false;
      boolean var9 = true;
      int var2 = var1.getItemId();
      View var12 = this.C.c(var2);
      int var4 = this.k.size();
      int var3 = this.g.d;
      switch (var2) {
         case 16908319:
            if (this.f.d) {
               if (this.k.size() < this.g.j()) {
                  this.k.addAll(this.g.i);
               } else {
                  this.k.clear();
               }

               this.f.b();
               if (this.C.q()) {
                  this.C.a(Integer.toString(this.k.size()) + "/" + this.g.j(), false);
               }
            }
            break;
         case com.alensw.PicFolder.R.id.done:
            Intent var15 = this.r();
            var5 = var9;
            if (var15 != null) {
               super.a(var15);
               var5 = var9;
            }

            return var5;
         case com.alensw.PicFolder.R.id.share:
            this.a(this.C.c(var2));
            break;
         case com.alensw.PicFolder.R.id.details:
         case com.alensw.PicFolder.R.id.edit:
         case com.alensw.PicFolder.R.id.set_as:
         case com.alensw.PicFolder.R.id.show_in_map:
            if (this.k.size() > 0) {
               this.a(var2, (ScannedMediaFile)this.k.iterator().next());
            }
            break;
         case com.alensw.PicFolder.R.id.browse_mode:
            GalleryMenu.a(this.C, var12, new LocalFolderBrowseMenu(this));
            break;
         case com.alensw.PicFolder.R.id.browse_grid:
            this.i = 0;
            break;
         case com.alensw.PicFolder.R.id.browse_list:
            this.i = 2;
            break;
         case com.alensw.PicFolder.R.id.delete:
            GalleryActions.a(this.C, var4, this.C.c(var2), new ConfirmLocalFileDelete(this));
            break;
         case com.alensw.PicFolder.R.id.move_to:
            if (a && this.d()) {
               break;
            }
         case com.alensw.PicFolder.R.id.copy_to:
            HashSet var14 = this.s();
            if (var2 == com.alensw.PicFolder.R.id.copy_to) {
               var5 = true;
            }

            this.a(var14, var5, new LocalFolderCopyMoveResult(this));
            break;
         case com.alensw.PicFolder.R.id.slideshow:
            this.a(Uri.fromFile(this.g), "image/*", true);
            break;
         case com.alensw.PicFolder.R.id.sort:
            var5 = var11;
            switch (this.j) {
               case 2:
                  var5 = true;
               case 4:
                  var2 = com.alensw.PicFolder.R.id.sort_by_name;
                  break;
               case 3:
                  var6 = true;
               case 1:
                  var2 = com.alensw.PicFolder.R.id.sort_by_date;
                  var5 = var6;
                  break;
               case 5:
                  var7 = true;
               case 6:
                  var2 = com.alensw.PicFolder.R.id.sort_by_exif;
                  var5 = var7;
                  break;
               case 7:
                  var8 = true;
               case 8:
                  var2 = com.alensw.PicFolder.R.id.sort_by_size;
                  var5 = var8;
                  break;
               default:
                  var2 = 0;
                  var5 = var10;
            }

            GalleryActions.a(this.C, var12, new int[]{com.alensw.PicFolder.R.id.sort_by_name, com.alensw.PicFolder.R.id.sort_by_date, com.alensw.PicFolder.R.id.sort_by_exif, com.alensw.PicFolder.R.id.sort_by_size}, var2, var5);
            break;
         case com.alensw.PicFolder.R.id.desktop_link:
            this.a(this.g);
            break;
         case com.alensw.PicFolder.R.id.rotate:
            GalleryActions.c(this.C, this.C.c(var2));
            break;
         case com.alensw.PicFolder.R.id.select:
            if (!this.C.q()) {
               this.C.d(true);
               this.m();
            }
            break;
         case com.alensw.PicFolder.R.id.hide:
            if (!this.g.a(true)) {
               if (!this.d()) {
                  GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.hide, this.g(com.alensw.PicFolder.R.string.hide_confirm), new ConfirmHideLocalFolder(this), null);
               }
            } else {
               this.a(this.g, false, null);
            }
            break;
         case com.alensw.PicFolder.R.id.exclude:
            this.a(this.g, new ExcludeLocalFolder(this));
            break;
         case com.alensw.PicFolder.R.id.rename:
            if (a && this.d()) {
               break;
            }

            if (this.f.d) {
               var4 = this.k.size();
               if (var4 > 1) {
                  SharedPreferences var13 = PreferenceManager.getDefaultSharedPreferences(this.C);
                  String var29 = var13.getString("name_format", "IMG_%Y-%M-%D_%h.%m.%s");
                  GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.rename, var29, this.g(com.alensw.PicFolder.R.string.rename_format), new SubmitRenameFormat(this, var13));
               } else if (var4 == 1) {
                  this.a(var2, (ScannedMediaFile)this.k.iterator().next());
               }
               break;
            }
         case 16908310:
            if (!a || !this.d()) {
               GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.rename, this.g.getName(), null, new SubmitLocalFolderRename(this));
            }
            break;
         case com.alensw.PicFolder.R.id.fix_date:
            GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.fix_date, this.g(com.alensw.PicFolder.R.string.fix_date_confirm), new ConfirmFixFileDates(this), null);
            break;
         case com.alensw.PicFolder.R.id.rotate_left:
            this.d(-90);
            break;
         case com.alensw.PicFolder.R.id.rotate_right:
            this.d(90);
            break;
         case com.alensw.PicFolder.R.id.rotate_180:
            this.d(180);
            break;
         case com.alensw.PicFolder.R.id.sort_by_name:
            byte var19;
            if (this.j == 2) {
               var19 = 4;
            } else {
               var19 = 2;
            }

            this.j = var19;
            break;
         case com.alensw.PicFolder.R.id.sort_by_date:
            byte var18;
            if (this.j == 1) {
               var18 = 3;
            } else {
               var18 = 1;
            }

            this.j = var18;
            break;
         case com.alensw.PicFolder.R.id.sort_by_exif:
            byte var17;
            if (this.j == 6) {
               var17 = 5;
            } else {
               var17 = 6;
            }

            this.j = var17;
            break;
         case com.alensw.PicFolder.R.id.sort_by_size:
            byte var16;
            if (this.j == 7) {
               var16 = 8;
            } else {
               var16 = 7;
            }

            this.j = var16;
            break;
         default:
            return super.a(var1);
      }

      var4 = ScannedFolder.a(this.g.b(), this.i, this.j);
      var5 = var9;
      if (var4 != var3) {
         var3 = this.g.a();
         var2 = this.g.d();
         this.g.d = var4;
         FolderSortSettings.a(this.C).b(this.g);
         if (this.i != var3) {
            this.i = this.g.a();
            this.b(this.i);
            var3 = this.f.getWidth();
            var4 = this.f.getHeight();
            if (var3 > 0 && var4 > 0) {
               this.f.a(true, this.C.b(true), new RectF(var3 / 4, var4 / 4, var3 * 3 / 4, var4 * 3 / 4));
            }
         }

         var5 = var9;
         if (this.j != var2) {
            this.j = this.g.d();
            this.a(true);
            this.g.e = -1;
            this.o();
            var5 = var9;
         }
      }

      return var5;
   }

   @Override
   public void b() {
      super.b();
      if (this.g != null) {
         Uri var1 = Uri.fromFile(this.g);
         this.E.setData(var1);
         if (this.h == 2) {
            GalleryIntentActivity.E = var1;
         } else {
            GalleryIntentActivity.D = var1;
         }

         SharedPreferences var2 = PreferenceManager.getDefaultSharedPreferences(this.C);
         if (var2.getBoolean("open_last_folder", false)) {
            var2.edit().putString("last_folder", this.g.getPath()).commit();
         }
      }

      this.a(false);
   }

   protected void b(int var1) {
      byte var6;
      switch (var1) {
         case 0:
            var6 = 1;
            break;
         case 1:
         default:
            var6 = 0;
            break;
         case 2:
            var6 = 2;
      }

      int var2;
      int var3;
      if (var6 == 2) {
         int[] var5 = this.e();
         var2 = var5[0];
         var3 = var5[1];
      } else {
         var2 = QuickApp.r.d;
         var3 = QuickApp.r.e;
      }

      this.f.a(this.q, var2, var3);
      ImageGridView var7 = this.f;
      boolean var4;
      if (var6 == 2) {
         var4 = true;
      } else {
         var4 = false;
      }

      var7.a(var6, var4);
   }

   @Override
   public void b(Intent var1) {
      if (var1 != null && this.g != null) {
         Uri var2 = var1.getData();
         if (var2 != null) {
            String var3 = var2.getPath();
            if (var3 != null && !var3.equals(this.g.b(this.g.c))) {
               this.g.c = this.g.a(var3, MediaFileTypes.a(var3));
            }

            this.f.c(this.g.c);
         }
      }
   }

   @Override
   public void b(Bundle var1) {
      var1.putParcelable("current_uri", Uri.fromFile(this.g));
   }

   @Override
   public boolean b(Menu var1) {
      if (!this.f.d) {
         MenuItem var3 = var1.findItem(com.alensw.PicFolder.R.id.hide);
         if (var3 != null) {
            int var2;
            if (this.g.a(true)) {
               var2 = com.alensw.PicFolder.R.string.unhide;
            } else {
               var2 = com.alensw.PicFolder.R.string.hide;
            }

            var3.setTitle(var2);
         }
      }

      return true;
   }

   protected String c() {
      return "Folder";
   }

   protected void c(int var1) {
      super.a(this.g, var1, new LocalFolderRenameResult(this));
   }

   @Override
   public void c(boolean var1) {
      super.c(var1);
      if (!var1) {
          ReceivedMediaUpdates.observe(this);
      }
   }

   @TargetApi(11)
   @Override
   protected boolean c(Menu var1) {
      GalleryMenu.a(this.C, com.alensw.PicFolder.R.menu.picture, var1);
      var1.add(0, 16908319, 0, 17039373);
      var1.removeItem(com.alensw.PicFolder.R.id.slideshow);
      var1.removeItem(com.alensw.PicFolder.R.id.settings);
      if (GalleryChromeActivity.F) {
         for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
            var1.getItem(var2).setShowAsAction(0);
         }
      }

      this.a(var1, com.alensw.PicFolder.R.id.share, 2);
      this.a(var1, com.alensw.PicFolder.R.id.delete, 2);
      this.a(var1, com.alensw.PicFolder.R.id.rotate, 2);
      this.k.clear();
      this.f.setMultiSelect(true);
      if (this.g != null) {
         this.d(this.g.getPath());
      }

      return true;
   }

   public void d(int var1) {
      HashSet var3 = this.s();
      int var2 = var3.size();
      if (var2 != 0) {
         this.a(false);
         new RotateLocalFilesDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.rotate) + ": " + var1, var2, var3, var1).c();
      }
   }

   @Override
   protected boolean d(Menu var1) {
      boolean var4 = false;
      int var2 = this.k.size();
      boolean var3;
      if (var2 == 1) {
         if (((ScannedMediaFile)this.k.iterator().next()).c == 'I') {
            var3 = true;
         } else {
            var3 = false;
         }
      } else {
         var3 = false;
      }

      if (var2 == 1) {
         var4 = true;
      }

      this.a(var1, com.alensw.PicFolder.R.id.details, var4);
      this.a(var1, com.alensw.PicFolder.R.id.edit, var3);
      this.a(var1, com.alensw.PicFolder.R.id.set_as, var3);
      this.a(var1, com.alensw.PicFolder.R.id.show_in_map, var3);
      MenuItem var5 = var1.findItem(16908319);
      if (var2 == this.f.getCount()) {
         var2 = com.alensw.PicFolder.R.string.deselect_all;
      } else {
         var2 = 17039373;
      }

      var5.setTitle(var2);
      return true;
   }

   public void f(String var1) {
      ScannedFolder var3 = this.g;
      this.a(false);
      int var2 = QuickApp.o.a(var3);
      var2 = QuickApp.o.a(var2, var1);
      if (var2 >= 0) {
         this.g = QuickApp.o.b(var2);
         new ClearLocalFolderThumbnails(this, this.C, this.g(com.alensw.PicFolder.R.string.running_wait), 0, var3).c();
      } else {
         this.a(var3.getPath());
      }
   }

   public void g(String var1) {
      HashSet var3 = this.s();
      int var2 = var3.size();
      if (var2 != 0) {
         this.a(false);
         ThumbnailDatabase var4 = QuickApp.r;
         ArrayList var5 = new ArrayList(var2);
         new BatchRenameFilesDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.rename), var2, var1, var3, var4, var5).c();
      }
   }

   @Override
   public RectF h() {
      RectF var1 = new RectF();
      this.f.c(this.g.c);
      this.f.a(this.g.c);
      if (this.g.c >= 0 && this.f.b(this.g.c, var1)) {
         var1.offset(0.0F, this.n);
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
      return this.q();
   }

   @Override
   protected void k() {
      this.k.clear();
      this.f.setMultiSelect(false);
      this.m();
   }

   protected boolean l() {
      boolean var1 = false;
      if (this.C.q() && this.k.isEmpty()) {
         this.C.d(false);
      }

      if (this.m() > 0) {
         var1 = true;
      }

      return var1;
   }

   protected int m() {
      int var1 = this.g.j();
      if (this.f.d) {
         String var2 = Integer.toString(this.k.size()) + "/" + var1;
         if (this.C.q()) {
            this.C.a(var2, true);
         } else {
            this.b((CharSequence) var2);
         }
      } else {
         String var4 = this.g.getName();
         if (this.g.g == 2) {
            var4 = this.g(com.alensw.PicFolder.R.string.camera);
         }

         String var3 = var4;
         if (var1 > 0) {
            var3 = var4 + " (" + var1 + ")";
         }

         this.b((CharSequence) var3);
      }

      return var1;
   }

   protected void n() {
      ArrayList var2 = new ArrayList(this.g.i);
      int var1 = var2.size();
      this.a(false);
      GalleryMediaStore var3 = QuickApp.s;
      new FixFileDatesDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.fix_date), var1, var2, var3, new String[]{"datetaken"}).c();
   }

   public void o() {
      BindLocalFolderGrid var2 = new BindLocalFolderGrid(this, (Float)l.remove(this.g.getPath()));
      boolean var1;
      if (this.g.j() != 0 && !this.g.m()) {
         var1 = false;
      } else {
         var1 = true;
      }

      if (var1) {
         this.k.clear();
         QuickApp.o.a(this.g, true);
         QuickApp.w.c();
         this.f.setEmptyText(this.g(com.alensw.PicFolder.R.string.loading));
         this.a(this.g, var2);
      } else {
         var2.run();
      }
   }

   public void p() {
      HashSet var5 = this.s();
      int var1 = var5.size();
      if (var1 != 0) {
         this.a(false);
         ThumbnailDatabase var4 = QuickApp.r;
         GalleryMediaStore var2 = QuickApp.s;
         ArrayList var3 = new ArrayList(var1);
         new DeleteLocalFilesDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.delete), var1, var5, var2, var4, var3).c();
      }
   }

   public Uri[] q() {
      int var1 = this.k.size();
      Uri[] var2;
      if (var1 == 0) {
         var2 = null;
      } else {
         var2 = new Uri[var1];
         Iterator var4 = this.k.iterator();

         for (int var5 = 0; var4.hasNext(); var5++) {
            ScannedMediaFile var3 = (ScannedMediaFile)var4.next();
            var2[var5] = this.g.b(var3.b);
         }
      }

      return var2;
   }

   public Intent r() {
      int var5 = this.k.size();
      Intent var7;
      if (var5 == 0) {
         var7 = null;
      } else {
         ArrayList var8 = new ArrayList(var5);
         ArrayList var10 = new ArrayList(var5);
         ArrayList var9 = new ArrayList(var5);
         Iterator var6 = this.k.iterator();
         int var4 = 0;

         while (var6.hasNext()) {
            ScannedMediaFile var13 = (ScannedMediaFile)var6.next();
            File var11 = new File(this.g, var13.b);
            char var1 = var13.c;
            int var3;
            if (var1 == 'I') {
               var3 = var4 | 1;
            } else {
               char var2 = var13.c;
               var3 = var4;
               var1 = var2;
               if (var2 == 'V') {
                  var3 = var4 | 2;
                  var1 = var2;
               }
            }

            String var14 = var11.getPath();
            var8.add(MediaDetails.a(var14, var1));
            var10.add(var14);
            var9.add(Integer.valueOf(var13.c));
            var4 = var3;
         }

         String var12;
         if (var4 == 1) {
            var12 = "image/jpeg";
         } else if (var4 == 2) {
            var12 = "video/mp4";
         } else {
            var12 = "*/*";
         }

         if (var5 > 1) {
            var7 = new Intent("android.intent.action.SEND_MULTIPLE");
            var7.putParcelableArrayListExtra("android.intent.extra.STREAM", var8);
            var7.putStringArrayListExtra("multiple_files", var10);
            var7.putIntegerArrayListExtra("multiple_types", var9);
         } else {
            var7 = new Intent("android.intent.action.SEND");
            var7.putExtra("android.intent.extra.STREAM", (Parcelable)var8.get(0));
         }

         GalleryStorage.clipStreams(var7, var8);
         var7.setType(var12);
         var7.addFlags(1);
      }

      return var7;
   }

   public HashSet s() {
      LinkedHashSet var1 = new LinkedHashSet(this.k.size());
      var1.addAll(this.k);
      return var1;
   }

   public void t() {
      int var2 = this.f.getFirstVisible();
      int var3 = this.f.getLastVisible();

      for (int var1 = var2; var1 < var3; var1++) {
         QuickApp.w.b(this.g.e(var1));
      }

      if (var3 > var2) {
         QuickApp.w.b(var3 - var2);
      }
   }
}
