package com.alensw.ui.page;

import static com.alensw.gallery.FolderScanner.DIRECTORIES_REFRESHED;
import static com.alensw.gallery.FolderScanner.REFRESH_DIRECTORIES;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.alensw.PicFolder.GalleryActivity;
import com.alensw.PicFolder.GalleryStorage;
import com.alensw.PicFolder.QuickApp;
import com.alensw.cache.BoundedLruCache;
import com.alensw.gallery.FolderCoverBitmap;
import com.alensw.gallery.FolderCovers;
import com.alensw.gallery.FolderScanFilters;
import com.alensw.gallery.FolderSortSettings;
import com.alensw.gallery.GalleryFolders;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.gallery.ScannedFolder;
import com.alensw.bean.UriFile;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.image.BitmapUtils;
import com.alensw.image.PictureBitmap;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.activity.GalleryChromeActivity;
import com.alensw.ui.activity.GalleryIntentActivity;
import com.alensw.ui.dialog.FolderPickerDialog;
import com.alensw.ui.dialog.GalleryDialogs;
import com.alensw.ui.dialog.GalleryPasswordDialog;
import com.alensw.ui.menu.GalleryMenu;
import com.alensw.ui.view.ImageGridAdapter;
import com.alensw.ui.view.ImageGridView;
import com.alensw.ui.view.PathBar;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AlbumsPage extends MediaActionPage {
   protected static final String[] B = new String[]{"Pictures", "Photos", "Picture", "Photo"};
   private static final BoundedLruCache N = new BoundedLruCache(256);
   protected static String f;
   protected static int g;
   protected static float h;
   protected static int i;
   protected final LinkedHashSet A;
   private final Rect L;
   private ScannedFolder M;
   private final ImageGridAdapter O;
   private Handler scanHandler;
   private boolean batchScanUpdates;
   private boolean albumGridPending;
   private Set<ScannedFolder> albumsToVerify;
   private final Runnable drawAlbums = this::drawAlbumGrid;
   protected ImageGridView j;
   protected PathBar k;
   protected GalleryFolders l;
   protected PictureBitmap m;
   protected boolean n;
   protected boolean o;
   protected boolean p;
   protected int q;
   protected int r;
   protected int s;
   protected int t;
   protected int u;
   protected int v = -1;
   protected int w;
   protected int x;
   protected float y;
   protected ScannedFolder z = new ScannedFolder(ScannedFolder.b, 0);

   public AlbumsPage() {
      this.A = new LinkedHashSet(64);
      this.L = new Rect();
      this.O = new AlbumGridAdapter(this);
   }

   public static String c(int var0) {
      String var2 = (String)N.a(var0);
      String var1 = var2;
      if (var2 == null) {
         var1 = QuickApp.b(1000L * var0);
         N.a(var0, var1);
      }

      return var1;
   }

   protected static File t() {
      String[] var3 = B;
      int var1 = var3.length;
      int var0 = 0;

      File var4;
      while (true) {
         if (var0 >= var1) {
            var4 = new File(ScannedFolder.b);
            break;
         }

         String var2 = var3[var0];
         var4 = new File(ScannedFolder.b + File.separatorChar + var2 + File.separatorChar);
         if (var4.exists()) {
            break;
         }

         var0++;
      }

      return var4;
   }

   @Override
   public void a() {
        String string;
        boolean z = false;
        super.a();
         ReceivedMediaUpdates.observe(this);
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.C);
        this.j.setHorizontal(defaultSharedPreferences.getBoolean("horizontal_scroll", this.j.c));
        int iA = GalleryFolders.a(this.o, this.s, this.t);
        int iE = this.l.e();
        boolean z2 = iA != iE;
        if (z2) {
            b(iE);
        }
        boolean z3 = a(this.u) || z2;
        if (z3 || !this.o) {
            z = z3;
        } else if (this.z == null || this.z.e != g || !this.z.getPath().equals(f)) {
            z = true;
        }
        if (z) {
            a(true);
        } else {
            u();
        }
        if (this.u != 3 || (string = defaultSharedPreferences.getString("dest_folder", null)) == null) {
            return;
        }
        ScannedFolder srcType0111 = new ScannedFolder(string, -1);
        srcType0111.d = FolderSortSettings.a(this.C).a(srcType0111);
        int iA2 = this.l.a(srcType0111);
        if (iA2 >= 0) {
            this.j.post(new SelectAlbumInGrid(this, iA2));
        }
    }

   @Override
   public void a(int var1, int var2, int var3, int var4) {
      super.a(var1, var2, var3, var4);
      this.p();
   }

   @Override
   public void a(int var1, int var2, Intent var3) {
      if (var1 == 1 && var2 == -1) {
         Uri var4 = var3.getData();
         if (var4 != null && this.M != null) {
            String var5 = UriFile.a('I', var4, var3.getType()).j();
            if (var5 != null) {
               FolderCovers.a(this.C).a(this.M.getPath(), var5);
               this.c(this.M);
            }
         }
      }

      this.M = null;
   }

   @Override
   public void a(Bundle var1) {
      super.a(var1);
      super.h(com.alensw.PicFolder.R.layout.browser);
      if (i != this.C.U) {
         i = this.C.U;
         QuickApp.v.c();
      }

      this.l = QuickApp.o;
      this.u = GalleryIntentActivity.b(this.E);
      if (this.u == 0) {
         this.n = this.C.getPackageManager().hasSystemFeature("android.hardware.camera");
      }

      this.y = this.F().getDisplayMetrics().density;
      if (var1 != null) {
         Uri var2 = (Uri)var1.getParcelable("current_uri");
         if (var2 != null && var2.getPath() != null) {
            this.z = new ScannedFolder(var2.getPath(), 0);
         }

         h = var1.getFloat("scroll_pos");
      }

      this.c = (ViewGroup)this.f(com.alensw.PicFolder.R.id.bottom_bar);
      this.j = (ImageGridView)this.f(com.alensw.PicFolder.R.id.grid);
      this.j.setFastScrollEnabled(true);
      this.j.setSpaceWidth(QuickApp.h / 320 * 2);
      String var4 = this.g(com.alensw.PicFolder.R.string.local_albums);
      String var3;
      if (this.u != 0) {
         var3 = this.E.getStringExtra("activity_title");
         if (var3 == null) {
            var3 = var4 + " - " + this.g(com.alensw.PicFolder.R.string.pick);
         }
      } else {
         var3 = var4;
      }

      this.b((CharSequence) var3);
      this.b(PreferenceManager.getDefaultSharedPreferences(this.C).getInt("gallery_mode", this.l.e()));
   }

   protected void a(ScannedFolder var1, int var2) {
      Uri var5 = Uri.fromFile(var1);
      Intent var4 = new Intent();
      if (this.u != 0) {
         Intent var3 = this.C.getIntent();
         var4.setAction(var3.getAction());
         var4.setDataAndType(var5, var3.getType());
         var4.putExtras(var3);
      } else {
         var4.setData(var5);
      }

      this.C.a(LocalFolderPage.class, var4, null);
   }

   public void a(ScannedFolder var1, ScannedMediaFile var2) {
      QuickApp.t.a(var2, true, new BindAlbumCover(this, var1));
   }

   protected void a(ScannedFolder var1, boolean var2) {
      if (var2) {
         this.M = var1;
         Intent var3 = new Intent();
         var3.setAction("android.intent.action.GET_CONTENT");
         var3.setClass(this.C, GalleryActivity.class);
         var3.setType("image/*");
         GalleryActions.a(this.C, var3, 1);
      } else {
         this.M = null;
         FolderCovers.a(this.C).a(var1.getPath(), null);
         this.c(var1);
      }
   }

   public void a(boolean var1) {
      this.v();
      this.v = -1;
      if (var1) {
         QuickApp.v.c();
         QuickApp.w.c();
         this.l.g();
         this.A.clear();
      }

      QuickApp.x = false;
      this.u();
   }

   @TargetApi(11)
   @Override
   public boolean a(Menu var1) {
      boolean var2 = true;
      if (this.j.d) {
         var2 = false;
      } else {
         GalleryMenu.a(this.C, com.alensw.PicFolder.R.menu.gallery, var1);
         if (this.u == 0) {
            this.a(var1, com.alensw.PicFolder.R.id.search, 2);
         } else {
            var1.removeItem(com.alensw.PicFolder.R.id.search);
            var1.removeItem(com.alensw.PicFolder.R.id.transfer);
            var1.removeItem(com.alensw.PicFolder.R.id.settings);
            if (this.u == 3) {
               this.a(var1.findItem(com.alensw.PicFolder.R.id.add).setVisible(true), 2);
               if (this.o) {
                  this.a(var1.findItem(com.alensw.PicFolder.R.id.done).setVisible(true), 2);
               }
            }
         }

         if (this.n) {
            this.a(var1, com.alensw.PicFolder.R.id.camera, 2);
         } else {
            var1.removeItem(com.alensw.PicFolder.R.id.camera);
         }
      }

      return var2;
   }

   @TargetApi(11)
   @Override
   public boolean a(MenuItem var1) {
      int var2 = 2;
      boolean var3 = false;
      boolean var7 = true;
      int var5 = var1.getItemId();
      View var9 = this.C.c(var5);
      int var4 = GalleryFolders.a(this.o, this.s, this.t);
      ScannedFolder var8;
      if (this.A.size() > 0) {
         var8 = (ScannedFolder)this.A.iterator().next();
      } else {
         var8 = this.l.b(this.v);
      }

      switch (var5) {
         case 16908319:
            if (this.j.d) {
               var2 = this.l.f();
               if (this.A.size() < var2) {
                  this.A.addAll(this.l.h());
               } else {
                  this.A.clear();
               }

               this.j.b();
               if (this.C.q()) {
                  this.C.a(Integer.toString(this.A.size()) + "/" + this.l.f(), false);
               }
            }
            break;
         case com.alensw.PicFolder.R.id.done:
            if (this.u == 3) {
               ScannedFolder var11;
               if (var8 == null && this.o) {
                  var11 = this.z;
               } else {
                  var11 = var8;
               }

               if (var11 != null) {
                  this.a((File) var11);
               } else {
                  Toast.makeText(this.C, com.alensw.PicFolder.R.string.select_a_folder, 0).show();
               }
            }
            break;
         case com.alensw.PicFolder.R.id.add:
            if (this.o && this.z != null) {
               GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.new_folder, "", null, new SubmitNewAlbumName(this));
            } else {
               FolderPickerDialog.a(this.C, 2, t(), new AddPickedAlbum(this));
            }
            break;
         case com.alensw.PicFolder.R.id.browse_mode:
            GalleryMenu.a(this.C, var9, new AlbumsBrowseMenu(this));
            break;
         case com.alensw.PicFolder.R.id.browse_default:
            this.s = 0;
            break;
         case com.alensw.PicFolder.R.id.browse_stack:
            this.s = 1;
            break;
         case com.alensw.PicFolder.R.id.browse_grid:
            this.s = 3;
            break;
         case com.alensw.PicFolder.R.id.browse_list:
            this.s = 2;
            break;
         case com.alensw.PicFolder.R.id.browse_explorer:
            boolean var22;
            if (!this.o) {
               var22 = true;
            } else {
               var22 = false;
            }

            this.o = var22;
            break;
         case com.alensw.PicFolder.R.id.delete:
            Iterator var10 = this.A.iterator();
            var2 = 0;

            while (var10.hasNext()) {
               var2 += ((ScannedFolder)var10.next()).j();
            }

            GalleryActions.a(this.C, var2, var9, new ConfirmAlbumDelete(this));
            break;
         case com.alensw.PicFolder.R.id.search:
            this.c("folders");
            break;
         case com.alensw.PicFolder.R.id.sort:
            boolean var21;
            label156: {
               label155: {
                  label154: {
                     switch (this.t) {
                        case 1:
                           var21 = true;
                           break label155;
                        case 2:
                           var21 = false;
                           break label155;
                        case 3:
                           var21 = true;
                           break label154;
                        case 4:
                           var21 = false;
                           break label154;
                        case 5:
                           var21 = true;
                           break;
                        case 6:
                           var21 = false;
                           break;
                        default:
                           var21 = false;
                           var2 = 0;
                           break label156;
                     }

                     var2 = com.alensw.PicFolder.R.id.sort_by_path;
                     break label156;
                  }

                  var2 = com.alensw.PicFolder.R.id.sort_by_date;
                  break label156;
               }

               var2 = com.alensw.PicFolder.R.id.sort_by_name;
            }

            GalleryActions.a(this.C, var9, new int[]{com.alensw.PicFolder.R.id.sort_by_name, com.alensw.PicFolder.R.id.sort_by_date, com.alensw.PicFolder.R.id.sort_by_path}, var2, var21);
            break;
         case com.alensw.PicFolder.R.id.desktop_link:
            if (var8 != null) {
               this.a(var8);
            }
            break;
         case com.alensw.PicFolder.R.id.hide:
            if (var8 != null) {
               if (!var8.a(true)) {
                  if (!this.d()) {
                     GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.hide, this.g(com.alensw.PicFolder.R.string.hide_confirm), new ConfirmHideAlbum(this, var8), null);
                  }
               } else if (this.a(var8, false, null)) {
                  this.j.invalidate();
               }
            }
            break;
         case com.alensw.PicFolder.R.id.top:
            if (var8 != null) {
               byte var14;
               if (var8.c()) {
                  var14 = 2;
               } else {
                  var14 = 1;
               }

               var8.d = ScannedFolder.a(var14, var8.a(), var8.d());
               this.l.i();
               this.j.requestLayout();
               FolderSortSettings.a(this.C).b(var8);
            }
            break;
         case com.alensw.PicFolder.R.id.exclude:
            this.z();
            break;
         case com.alensw.PicFolder.R.id.rename:
            if (var8 != null && !this.d()) {
               GalleryDialogs.a(this.C, com.alensw.PicFolder.R.string.rename, var8.getName(), null, new RenameAlbum(this, var8));
            }
            break;
         case com.alensw.PicFolder.R.id.cover_select:
         case com.alensw.PicFolder.R.id.cover_default:
            if (var8 != null) {
               boolean var20;
               if (var5 == com.alensw.PicFolder.R.id.cover_select) {
                  var20 = true;
               } else {
                  var20 = false;
               }

               this.a(var8, var20);
            }
            break;
         case com.alensw.PicFolder.R.id.camera:
            GalleryActions.a(this.C, var9);
            break;
         case com.alensw.PicFolder.R.id.show_hidden:
            this.o();
            break;
         case com.alensw.PicFolder.R.id.sort_by_name:
            if (this.t != 1) {
               var2 = 1;
            }

            this.t = var2;
            break;
         case com.alensw.PicFolder.R.id.sort_by_date:
            if (this.t == 4) {
               var2 = (byte)3;
            } else {
               var2 = (byte)4;
            }

            this.t = var2;
            break;
         case com.alensw.PicFolder.R.id.sort_by_path:
            if (this.t == 5) {
               var2 = (byte)6;
            } else {
               var2 = (byte)5;
            }

            this.t = var2;
            break;
         default:
            return super.a(var1);
      }

      var5 = GalleryFolders.a(this.o, this.s, this.t);
      boolean var6 = var7;
      if (var5 != var4) {
         boolean var18;
         if (this.l.b() != this.o) {
            var18 = true;
         } else {
            var18 = false;
         }

         if (this.l.a() != this.s) {
            var3 = true;
         }

         if (var18) {
            this.v();
         }

         PreferenceManager.getDefaultSharedPreferences(this.C).edit().putInt("gallery_mode", var5).commit();
         this.b(var5);
         this.n();
         if (var18) {
            this.a(true);
         } else if (var3) {
            QuickApp.v.c();
         }

         var6 = var7;
         if (var18) {
            this.c.startAnimation(this.C.c(this.o));
            var6 = var7;
         }
      }

      return var6;
   }

   @Override
   public void b() {
      super.b();
      this.v();
   }

   protected void b(int var1) {
      byte var3 = 2;
      byte var2 = 0;
      boolean var4 = this.l.b();
      this.l.a(var1);
      this.o = this.l.b();
      this.s = this.l.a();
      this.t = this.l.c();
      var1 = this.j.e;
      switch (this.s) {
         case 0:
            this.w = QuickApp.r.f;
            this.x = QuickApp.r.g;
            this.q = 0;
            var1 = 1;
            break;
         case 1:
            this.w = QuickApp.r.f;
            this.x = Math.min(QuickApp.r.g, QuickApp.r.e);
            this.q = 2;
            var1 = 3;
            break;
         case 2:
            int[] var5 = this.e();
            this.w = var5[0];
            this.x = var5[1];
            this.q = 0;
            var1 = var3;
            break;
         case 3:
            this.w = QuickApp.r.f;
            this.x = QuickApp.r.g;
            this.q = 1;
            var1 = 3;
      }

      QuickApp.v.a(BitmapUtils.b * 1024000 / 5 / (this.w * this.x * 2));
      this.r = FolderCoverBitmap.b(this.q);
      if (this.m != null) {
         this.m.m();
         this.m = null;
      }

      if (var4 != this.o && this.u == 3) {
         this.C.invalidateOptionsMenu();
      }

      if (this.o) {
         if (PathBar.a.getPath().equals(f)) {
            f = null;
         }

         if (f != null) {
            this.z = new ScannedFolder(f, g);
         }

         if (ScannedFolder.b.equals(this.z.getPath())) {
            String var9 = FolderScanFilters.a(this.C).a();
            if (var9 != null) {
               this.z = new ScannedFolder(var9, 0);
            }
         }

         this.q();
         this.k.setPath(this.z);
      }

      ImageGridView var10 = this.j;
      if (ImageGridView.a && this.r == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      var10.b = var4;
      this.j.a(this.O, this.w, this.x);
      this.j.a(var1, true);
      if (this.j.getHeight() > 0) {
         this.p();
      }

      ViewGroup var11 = this.c;
      byte var7;
      if (this.o) {
         var7 = var2;
      } else {
         var7 = 8;
      }

      var11.setVisibility(var7);
   }

   @Override
   public void b(Intent var1) {
      if (var1 != null) {
         Uri var5 = var1.getData();
         if (var5 != null) {
            String var6 = var5.getPath();
            int var3 = this.v;
            ScannedFolder var4 = this.l.b(var3);
            int var2 = var3;
            if (var6 != null) {
               var2 = var3;
               if (var4 != null) {
                  var2 = var3;
                  if (!var6.equals(var4.getPath())) {
                     ScannedFolder var7 = new ScannedFolder(var6, -1);
                     var7.d = FolderSortSettings.a(this.C).a(var7);
                     var2 = this.l.b(var7);
                  }
               }
            }

            if (var2 < 0) {
               var2 = -1;
            }

            this.v = var2;
            this.j.c(this.v);
         }
      }
   }

   @Override
   public void b(Bundle var1) {
      if (this.z != null) {
         var1.putParcelable("current_uri", Uri.fromFile(this.z));
      } else {
         var1.remove("current_uri");
      }

      h = this.j.getScrollPos();
      var1.putFloat("scroll_pos", h);
   }

   protected void b(ScannedFolder var1) {
      ScannedFolder var2 = var1;
      if (var1 == null) {
         var2 = new ScannedFolder(ScannedFolder.b, 0);
      }

      this.z = var2;
      this.v = -1;
      this.q();
      this.k.setPath(this.z);
      this.a(true);
   }

   protected void b(File var1) {
      if (var1.exists() || GalleryUtils.a(var1)) {
         QuickApp.q.a(4, var1.getPath(), null);
         ScannedFolder var3 = new ScannedFolder(var1, -1);
         var3.d = FolderSortSettings.a(this.C).a(var3);
         if (this.u == 3) {
            this.a((File) var3);
         } else {
            LocateAlbumInGrid var2 = new LocateAlbumInGrid(this, var3);
            if (!QuickApp.q.b && var3.a(true)) {
               GalleryPasswordDialog.a(this.C, "protect_hidden", var2, null);
            } else {
               var2.run();
            }
         }
      } else if (!this.a(var1.getParent())) {
         Toast.makeText(this.C, "Create folder '" + var1.getName() + "' failed!", 1).show();
      }
   }

   @Override
   public boolean b(Menu var1) {
      boolean var3;
      if (this.j.d) {
         var3 = false;
      } else {
         MenuItem var4 = var1.findItem(com.alensw.PicFolder.R.id.show_hidden);
         if (var4 != null) {
            int var2;
            if (QuickApp.q.b) {
               var2 = com.alensw.PicFolder.R.string.hide_hidden;
            } else {
               var2 = com.alensw.PicFolder.R.string.show_hidden;
            }

            var4.setTitle(var2);
         }

         var3 = true;
      }

      return var3;
   }

   public void c(ScannedFolder var1) {
      QuickApp.v.c(var1);
      this.j.d(this.l.a(var1));
      this.d(var1);
   }

   @TargetApi(11)
   @Override
   protected boolean c(Menu var1) {
      GalleryMenu.a(this.C, com.alensw.PicFolder.R.menu.folder, var1);
      var1.findItem(com.alensw.PicFolder.R.id.hide).setIcon(null);
      var1.findItem(com.alensw.PicFolder.R.id.exclude).setIcon(null);
      var1.add(0, 16908319, 0, 17039373);
      var1.removeItem(com.alensw.PicFolder.R.id.slideshow);
      var1.removeItem(com.alensw.PicFolder.R.id.browse_mode);
      var1.removeItem(com.alensw.PicFolder.R.id.sort);
      var1.removeItem(com.alensw.PicFolder.R.id.fix_date);
      this.a(var1, com.alensw.PicFolder.R.id.select, false);
      if (GalleryChromeActivity.F) {
         for (int var2 = var1.size() - 1; var2 >= 0; var2--) {
            var1.getItem(var2).setShowAsAction(0);
         }
      }

      this.a(var1, com.alensw.PicFolder.R.id.delete, 2);
      this.A.clear();
      this.j.setMultiSelect(true);
      return true;
   }

   public void d(ScannedFolder var1) {
      int var3 = Math.min(this.r, var1.j());
      String var4 = FolderCovers.a(this.C).a(var1.getPath());
      if (var4 != null) {
         if (!QuickApp.v.a(var1)) {
            this.a(var1, new ScannedMediaFile(new File(var4), 'I', -1));
         }
      } else if (var3 == 1) {
         if (!QuickApp.v.a(var1)) {
            this.a(var1, var1.e(0));
         }
      } else if (var3 > 0) {
         PictureBitmap var5 = (PictureBitmap)QuickApp.v.b(var1);
         int var2;
         FolderCoverBitmap var6;
         if (var5 instanceof FolderCoverBitmap) {
            var6 = (FolderCoverBitmap)var5;
            if (var6.b()) {
               return;
            }

            var2 = 0;

            while (var2 < var3 && var6.a(var2)) {
               var2++;
            }
         } else {
            var6 = new FolderCoverBitmap(this.q, this.w, this.x, this.y, var3, this.C.U, QuickApp.r.b);
            QuickApp.v.a(var1, var6);
            var2 = 0;
         }

         QuickApp.t.a(var1.e(var2), false, new LoadStackedAlbumCover(this, var1, var6, var3, var2));
      }
   }

   @Override
   protected boolean d(Menu var1) {
      boolean var5 = false;
      int var3 = this.A.size();
      boolean var4;
      if (var3 == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a(var1, com.alensw.PicFolder.R.id.hide, var4);
      if (var3 == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a(var1, com.alensw.PicFolder.R.id.rename, var4);
      if (var3 == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a(var1, com.alensw.PicFolder.R.id.cover, var4);
      if (var3 == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a(var1, com.alensw.PicFolder.R.id.desktop_link, var4);
      if (var3 == 1) {
         var4 = true;
      } else {
         var4 = false;
      }

      this.a(var1, com.alensw.PicFolder.R.id.top, var4);
      if (var3 == 1) {
         ScannedFolder var6 = (ScannedFolder)this.A.iterator().next();
         MenuItem var7 = var1.findItem(com.alensw.PicFolder.R.id.hide);
         int var2;
         if (var6.a(true)) {
            var2 = com.alensw.PicFolder.R.string.unhide;
         } else {
            var2 = com.alensw.PicFolder.R.string.hide;
         }

         var7.setTitle(var2);
         var7 = var1.findItem(com.alensw.PicFolder.R.id.top);
         if (var6.c()) {
            var2 = com.alensw.PicFolder.R.string.top_cancel;
         } else {
            var2 = com.alensw.PicFolder.R.string.top_on;
         }

         var7.setTitle(var2);
         if (var6.j() > 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         this.a(var1, com.alensw.PicFolder.R.id.cover, var4);
         var4 = var5;
         if (var6.j() > 0) {
            var4 = true;
         }

         this.a(var1, com.alensw.PicFolder.R.id.desktop_link, var4);
      }

      MenuItem var8 = var1.findItem(16908319);
      int var10;
      if (var3 == this.l.f()) {
         var10 = com.alensw.PicFolder.R.string.deselect_all;
      } else {
         var10 = 17039373;
      }

      var8.setTitle(var10);
      return true;
   }

   @Override
   public boolean f() {
      boolean var1 = false;
      if (this.C.q() && this.l.f() != 0) {
         this.C.d(false);
      } else {
         label22: {
            if (this.s()) {
               File var2 = this.z.getParentFile();
               if (var2 != null) {
                  this.b(new ScannedFolder(var2, 0));
                  break label22;
               }
            }

            var1 = true;
         }
      }

      this.n();
      return var1;
   }

   @Override
   public void g() {
      super.g();
      if (this.m != null) {
         this.m.m();
         this.m = null;
      }
   }

   @Override
   public RectF h() {
      RectF var2 = new RectF();
      this.j.c(this.v);
      RectF var1;
      if (this.v >= 0 && this.j.b(this.v, var2)) {
         var1 = var2;
         if (this.s == 0) {
            var2.right = var2.left + var2.height();
            var1 = var2;
         }
      } else {
         var1 = super.h();
      }

      return var1;
   }

   @Override
   public boolean handleMessage(Message message) {
        switch (message.what) {
            case 10:
                boolean z = message.arg1 == 1;
                this.j.setEmptyText(z ? null : g(com.alensw.PicFolder.R.string.loading));
                if (!z) {
                    QuickApp.x = false;
                    this.p = true;
                    this.l.i();
                    c(true);
                }
                return true;
            case 11:
                 if (message.arg1 != 1) {
                     if (message.arg2 == 1) removeUnverifiedAlbums();
                     albumsToVerify = null;
                     boolean z2 = this.l.f() == 0;
                    QuickApp.x = message.arg2 == 1;
                    this.p = false;
                    if (QuickApp.x && z2) {
                        this.j.setEmptyText(g(com.alensw.PicFolder.R.string.no_picture_in_album));
                    } else {
                        this.j.setEmptyText(z2 ? g(com.alensw.PicFolder.R.string.no_picture_in_album) : null);
                        this.j.requestLayout();
                    }
                    c(false);
                 } else if (!QuickApp.x || this.l.f() == 0 && (!batchScanUpdates || this.o)) {
                     w();
                 } else if (this.l.f() == 0) {
                     this.j.setEmptyText(g(com.alensw.PicFolder.R.string.no_picture_in_album));
                }
                return true;
            case 12:
                 ScannedFolder album = (ScannedFolder) message.obj;
                 this.l.d(album);
                 if (albumsToVerify != null) albumsToVerify.remove(album);
                 requestAlbumGrid();
                if (this.l.f() >= this.j.getVisibleCount() + 1) {
                    this.p = false;
                }
                return true;
            case 13:
                ScannedFolder srcType0111 = (ScannedFolder) message.obj;
                if (this.C.q() && this.A.remove(srcType0111)) {
                    this.C.a((CharSequence) (Integer.toString(this.A.size()) + "/" + this.l.f()), false);
                 }
                 this.l.f(srcType0111);
                 if (batchScanUpdates) requestAlbumGrid();
                 else this.j.requestLayout();
                 return true;
            case 14:
                this.p = false;
                c(false);
                if (this.H == 2) {
                    this.j.requestLayout();
                 }
                 return true;
            case DIRECTORIES_REFRESHED:
                 if (message.arg2 == 1) applyChangedAlbums((List<ScannedFolder>) message.obj);
                 return true;
            default:
                return true;
        }
    }

   @Override
   public void i() {
      super.i();
   }

   private void requestAlbumGrid() {
      if (this.H != 2) return;
      if (!batchScanUpdates) {
         drawAlbumGrid();
      } else if (!albumGridPending) {
         albumGridPending = true;
         this.j.postOnAnimation(drawAlbums);
      }
   }

   private void drawAlbumGrid() {
      albumGridPending = false;
      this.j.requestLayout();
      this.j.a();
   }

   private void updateAlbumSelection() {
      if (this.C.q()) {
         this.C.a(Integer.toString(this.A.size()) + "/" + this.l.f(), false);
      }
   }

   private void removeUnverifiedAlbums() {
      if (albumsToVerify == null || albumsToVerify.isEmpty()) return;
      for (ScannedFolder album : albumsToVerify) {
         int index = this.l.a(album);
         if (this.l.b(index) == album) {
            this.l.c(index);
            this.A.remove(album);
         }
      }
      updateAlbumSelection();
      requestAlbumGrid();
   }

   private void applyChangedAlbums(List<ScannedFolder> updatedAlbums) {
      if (updatedAlbums.isEmpty()) return;
      ScannedFolder focused = this.l.b(this.v);
      Map<ScannedFolder, ScannedFolder> replacements = new HashMap<>();
      boolean changed = false;
      for (ScannedFolder album : updatedAlbums) {
         int index = this.l.a(album);
         ScannedFolder old = this.l.b(index);
         if (old == null && album.j() == 0) continue;
         changed = true;
         if (old != null) this.l.c(index);  // invalidate its cached cover even if the mtime matches
         if (album.j() > 0) {
            this.l.d(album);
            if (old != null) replacements.put(old, album);
         } else if (old != null) {
            replacements.put(old, null);
         }
      }
      if (!changed) return;
      if (!replacements.isEmpty() && !this.A.isEmpty()) {
         LinkedHashSet<ScannedFolder> selection = new LinkedHashSet<>();
         for (Object value : this.A) {
            ScannedFolder selected = (ScannedFolder) value;
            ScannedFolder updated = replacements.containsKey(selected) ? replacements.get(selected) : selected;
            if (updated != null) selection.add(updated);
         }
         this.A.clear();
         this.A.addAll(selection);
      }
      if (focused != null) this.v = Math.max(-1, this.l.a(focused));
      this.j.setEmptyText(this.l.f() == 0 ? g(com.alensw.PicFolder.R.string.no_picture_in_album) : null);
      updateAlbumSelection();
      requestAlbumGrid();
   }

   @Override
   protected Uri[] j() {
      return this.l();
   }

   @Override
   protected void k() {
      this.A.clear();
      this.j.setMultiSelect(false);
   }

   public Uri[] l() {
      Uri[] var5;
      if (this.A.size() == 0) {
         var5 = null;
      } else {
         Iterator var9 = this.A.iterator();
         int var1 = 0;

         while (var9.hasNext()) {
            var1 += ((ScannedFolder)var9.next()).j();
         }

         if (var1 == 0) {
            var5 = null;
         } else {
            var5 = new Uri[var1];
            Iterator var6 = this.A.iterator();
            var1 = 0;

            while (var6.hasNext()) {
               ScannedFolder var7 = (ScannedFolder)var6.next();
               int var4 = var7.j();
               int var3 = 0;
               int var2 = var1;

               while (true) {
                  var1 = var2;
                  if (var3 >= var4) {
                     break;
                  }

                  var5[var2] = var7.d(var3);
                  var3++;
                  var2++;
               }
            }
         }
      }

      return var5;
   }

   protected PictureBitmap m() {
      if (this.m == null) {
         this.m = FolderCoverBitmap.a(this.F(), this.q, this.w, this.x, this.C.U);
      }

      return this.m;
   }

   protected void n() {
   }

   public void o() {
      boolean var1;
      if (!QuickApp.q.b) {
         var1 = true;
      } else {
         var1 = false;
      }

      if (var1) {
         GalleryPasswordDialog.a(this.C, "protect_hidden", new SetHiddenAlbumsVisible(this, var1), null);
      } else {
         QuickApp.q.b = var1;
         this.v();
         if (this.l.a(true)) {
            this.j.requestLayout();
         }

         this.u();
      }
   }

   protected void p() {
      int var1;
      if (this.o) {
         var1 = this.r();
      } else {
         var1 = 0;
      }

      this.C.a(this.L);
      this.j.setPadding(this.L.left, this.L.top, this.L.right, var1 + this.L.bottom);
      this.c.setPadding(this.L.left, 0, this.L.right, this.L.bottom);
   }

   protected void q() {
      if (this.k == null) {
         int var1 = (int)(this.y * 4.0F);
         this.k = new PathBar(this.C);
         this.k.setBackgroundColor(GalleryChromeActivity.b(this.C, com.alensw.PicFolder.R.attr.colorPathBar));
         this.k.setPadding(var1, var1, var1, var1);
         this.k.setOnClickListener(new AlbumBreadcrumbClick(this));
         this.c.addView(this.k, -1, this.F().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_height));
      }
   }

   protected int r() {
      this.q();
      int var2 = this.k.getHeight();
      int var1 = var2;
      if (var2 == 0) {
         var1 = this.F().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_height);
      }

      return var1;
   }

   protected boolean s() {
      boolean var2 = false;
      boolean var1 = var2;
      if (this.o) {
         if (this.z == null) {
            var1 = var2;
         } else {
            var1 = var2;
            if (!ScannedFolder.b.equals(this.z.getPath())) {
               var1 = var2;
               if (!PathBar.a.equals(this.z)) {
                  var1 = true;
               }
            }
         }
      }

      return var1;
   }

   public void u() {
      this.j.setScrollPos(h);
      this.j.requestLayout();
      this.j.post(new RestoreAlbumScroll(this));
      ArrayList var1 = this.l.h();
       requestScan(1, var1, null);
      this.n();
      if (this.C.q()) {
         this.C.a(Integer.toString(this.A.size()) + "/" + this.l.f(), false);
      }
   }

   public void v() {
       h = this.j.getScrollPos();
       this.j.c();
       scanHandler = null;
       albumsToVerify = null;
       albumGridPending = false;
       this.j.removeCallbacks(drawAlbums);
       QuickApp.q.c();
      QuickApp.t.a(true);
      this.x();
   }

   public void w() {
       scanAlbums(null);
   }

   void scanAlbums(Runnable completed) {
      if (this.l.d()) {
         this.l.i();
      }

      if (this.o) {
         this.z.n();
         f = this.z.getPath();
         g = this.z.e;
          requestScan(2, this.z.getPath(), completed);
       } else {
          requestScan(3, null, completed);
       }
   }

   boolean scanningAlbums() {
      return scanHandler != null;
   }

   void scanChangedAlbums(List<File> directories, Runnable completed) {
      requestScan(REFRESH_DIRECTORIES, directories, completed);
   }

   private void requestScan(int operation, Object argument, Runnable completed) {
      batchScanUpdates = GalleryStorage.enhancedScan(this.C);
      albumsToVerify = batchScanUpdates && (operation == 2 || operation == 3)
              ? new HashSet<ScannedFolder>(this.l.h()) : null;
      // Each request owns its callbacks, including messages already posted as a page is left.
      scanHandler = new Handler(this.b.getLooper()) {
         @Override
         public void handleMessage(Message message) {
            if (scanHandler != this) return;
            boolean finished = message.what == 11 || message.what == DIRECTORIES_REFRESHED;
            if (finished) scanHandler = null;
            AlbumsPage.this.handleMessage(message);
            if (finished) {
               if (message.arg2 == 1 && completed != null) completed.run();
               ReceivedMediaUpdates.observe(AlbumsPage.this);
            }
         }
      };
      QuickApp.q.a(operation, argument, scanHandler);
   }

   protected void x() {
      int var2 = this.j.getFirstVisible();
      int var3 = this.j.getLastVisible();

      for (int var1 = var2; var1 < var3; var1++) {
         QuickApp.v.b(this.l.b(var1));
      }

      if (var3 > var2) {
         QuickApp.v.b(var3 - var2);
      }
   }

   protected void y() {
      ArrayList var3 = new ArrayList(this.A.size());
      Iterator var5 = this.A.iterator();
      int var1 = 0;

      int var2;
      for (var2 = 0; var5.hasNext(); var1++) {
         ScannedFolder var4 = (ScannedFolder)var5.next();
         var3.add(var4);
         var2 += var4.j();
      }

      if (var2 != 0 || var1 != 0) {
         ThumbnailDatabase var7 = QuickApp.r;
         GalleryMediaStore var6 = QuickApp.s;
         new DeleteAlbumsDialog(this, this.C, this.g(com.alensw.PicFolder.R.string.delete), var2, var3, var6, var7).c();
      }
   }

   protected void z() {
      FinishAlbumExclusion var1 = new FinishAlbumExclusion(this);
      FolderScanFilters var2 = FolderScanFilters.a(this.C);
      if (this.A.size() == 1) {
         this.a((ScannedFolder)this.A.iterator().next(), new ExcludeAlbumPath(this, var2, var1));
      } else {
         for (Object entry : this.A) {
         ScannedFolder var3 = (ScannedFolder) entry;
            var2.a(var3);
            this.l.f(var3);
            this.l.e(var3);
         }

         var1.run();
      }
   }
}
