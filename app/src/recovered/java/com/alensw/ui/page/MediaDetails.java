package com.alensw.ui.page;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.alensw.PicFolder.FileProvider;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.documents.DocumentProviderAccess;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.image.BitmapDecodeOptions;
import com.alensw.image.BitmapUtils;
import com.alensw.image.ExifData;
import com.alensw.image.Picture;
import com.alensw.image.PictureMetadata;
import com.alensw.io.OpenFileHandle;
import com.alensw.location.ReverseGeocoding;
import com.alensw.media.MediaFileTypes;
import com.alensw.ui.activity.GalleryActions;
import com.alensw.ui.dialog.GalleryAlertDialog;
import com.alensw.ui.dialog.GalleryDialogs;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.util.ArrayList;

public class MediaDetails {
   public static final String[] a = new String[]{"mime_type", "resolution", "duration", "datetaken"};
   private static DateFormat b;
   private static DateFormat c;
   private final Context d;
   private Uri e;
   private char f;
   private long g;
   private long h;
   private String i;
   private String j;
   private PictureMetadata k;
   private boolean l;
   private boolean m;

   public MediaDetails(Context var1, Uri var2, char var3, String var4, PictureMetadata var5) {
      this.d = var1;
      if (b == null) {
         b = android.text.format.DateFormat.getDateFormat(var1);
         c = android.text.format.DateFormat.getTimeFormat(var1);
      }

      this.a(var2, var3, var4, var5);
   }

   public MediaDetails(Context var1, CommonFile var2, PictureMetadata var3) {
      this.d = var1;
      if (b == null) {
         b = android.text.format.DateFormat.getDateFormat(var1);
         c = android.text.format.DateFormat.getTimeFormat(var1);
      }

      this.a(var2.i(), var2.h(), var2.b(), var3);
      String var4 = var2.j();
      if (var4 != null) {
         this.i = var4;
      }
   }

   static Context a(MediaDetails var0) {
      return var0.d;
   }

   public static Uri a(Uri var0, char var1) {
      Uri var2 = var0;
      if ("file".equals(var0.getScheme())) {
         var2 = a(var0.getPath(), var1);
      }

      return var2;
   }

   public static Uri a(String var0, char var1) {
      Uri var3 = QuickApp.s.b(var0, var1);
      Uri var2 = var3;
      if (var3 == null) {
         var2 = FileProvider.a(var0);
      }

      return var2;
   }

   private void a(boolean var1) {
      if (this.f == 'V') {
         if (this.i != null) {
            String[] var2 = QuickApp.s.a(this.i, this.f, a);
            if (var2 != null) {
               this.k.o = var2[0];
               this.j = var2[1];
               this.g = GalleryUtils.a(var2[2], 0L);
               this.k.j = GalleryUtils.a(var2[3], 0L);
            }

            this.h = new File(this.i).length();
         }
      } else {
         OpenFileHandle var5 = null;
         if (var1) {
            OpenFileHandle var3 = this.c();
            var5 = var3;
            if (var3 != null) {
               this.h = var3.e();
               ExifData var4 = ExifData.a(var3, true);
               var5 = var3;
               if (var4 != null) {
                  var4.a(this.k, var3.f());
                  var4.a();
                  var5 = var3;
               }
            }
         }

         OpenFileHandle var8;
         label66: {
            if (this.k.a != 0 && this.k.b != 0) {
               var8 = var5;
               if (this.h != 0L) {
                  break label66;
               }
            }

            OpenFileHandle var7 = var5;
            if (var5 == null) {
               var7 = this.c();
            }

            var8 = var7;
            if (var7 != null) {
               BitmapDecodeOptions var6 = new BitmapDecodeOptions();
               if (BitmapUtils.b(var7, var6)) {
                  this.k.a = var6.outWidth;
                  this.k.b = var6.outHeight;
                  this.k.o = var6.outMimeType;
               }

               this.h = var7.e();
               var8 = var7;
            }
         }

         if (this.k.j == 0L && this.i != null) {
            this.k.j = new File(this.i).lastModified();
         }

         if (this.k.a > 0 && this.k.b > 0) {
            this.j = Integer.toString(this.k.a) + "x" + this.k.b + "=" + GalleryUtils.b(this.k.a * this.k.b);
         }

         if (var8 != null) {
            var8.h();
         }
      }

      if (this.k.o == null && this.i != null) {
         this.k.o = MediaFileTypes.a(this.i, this.f);
      }
   }

   static long b(MediaDetails var0) {
      return var0.d();
   }

   private OpenFileHandle c() {
      OpenFileHandle var1;
      try {
         var1 = OpenFileHandle.a(this.d.getContentResolver(), this.e);
      } catch (Throwable var2) {
         var1 = null;
      }

      return var1;
   }

   static PictureMetadata c(MediaDetails var0) {
      return var0.k;
   }

   private long d() {
      File var5 = new File(this.i);
      long var3 = var5.lastModified();

      long var1;
      label34: {
         try {
            if (this.f == 'V' && this.k.j == var3) {
               this.k.j = GalleryUtils.a(var5, this.f, this.k.j);
            }
         } catch (Throwable var10) {
            var1 = var3;
            break label34;
         }

         var1 = var3;

         try {
            if (var5.setLastModified(this.k.j)) {
               var1 = this.k.j;
            }
         } catch (Throwable var9) {
            var1 = var3;
         }
      }

      GalleryMediaStore var6 = QuickApp.s;
      Integer var7 = var6.a(this.i, this.f);
      if (var7 != null) {
         Uri var11 = GalleryMediaStore.a(this.f);
         String var8 = "_id=" + var7;
         ContentValues var12 = new ContentValues();
         var12.put("datetaken", this.k.j);
         var12.put("date_modified", (int)(var1 / 1000L));
         var6.a(var11, var12, var8, null);
      }

      QuickApp.r.a(this.i, (int)(var1 / 1000L));
      return var1;
   }

   public String a(char var1) {
      this.a();
      StringBuilder var5 = new StringBuilder(256);
      if (this.j != null) {
         String var4 = this.j;
         int var2 = var4.lastIndexOf(61);
         String var3 = var4;
         if (var2 != -1) {
            var3 = var4.substring(0, var2);
         }

         var5.append(var3);
      }

      if (this.g != 0L) {
         var5.append(var1).append(GalleryUtils.a(this.g));
      }

      if (this.k.n != null) {
         var5.append(var1).append(this.k.n);
      }

      if (this.k.j != 0L) {
         var5.append(var1).append(this.a(this.k.j));
      }

      return var5.toString();
   }

   public String a(long var1) {
      StringBuffer var3 = new StringBuffer();
      FieldPosition var4 = new FieldPosition(0);
      b.format(var1, var3, var4);
      var3.append(' ');
      return c.format(var1, var3, var4).toString();
   }

   public String a(Uri var1) {
      this.e = var1;
      if (DocumentProviderAccess.d(var1)) {
         this.i = DocumentProviderAccess.b(var1);
      }

      if (this.i == null || this.i.length() == 0) {
         this.i = var1.getPath();
      }

      return this.i;
   }

   public void a() {
      if (!this.l) {
         this.a(this.m);
         this.l = true;
      }
   }

   public void a(Uri var1, char var2, String var3, PictureMetadata var4) {
      this.a(var1);
      this.f = var2;
      this.g = 0L;
      this.h = 0L;
      this.j = null;
      if (var2 == 0) {
         if (var4 != null && var4.o != null) {
            byte var6;
            if (var4.o.startsWith("video/")) {
               var6 = 86;
            } else {
               var6 = 73;
            }

            this.f = (char)var6;
         } else if (this.i != null) {
            this.f = MediaFileTypes.a(this.i);
         }
      }

      if (var4 != null) {
         this.k = new PictureMetadata(var4);
      } else {
         this.k = new PictureMetadata();
         this.k.o = var3;
      }

      this.l = false;
      boolean var5;
      if (var4 == null) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.m = var5;
   }

   public void a(View var1) {
      String var2;
      label11: {
         this.a();
         String var3 = this.k.o;
         if (var3 != null) {
            var2 = var3;
            if (!"image/webp".equals(var3)) {
               break label11;
            }
         }

         var2 = "image/*";
      }

      Uri var4 = a(this.e, this.f);
      Intent var5 = new Intent("android.intent.action.SEND");
      var5.setType(var2);
      var5.addFlags(1);
      var5.putExtra("android.intent.extra.STREAM", var4);
      GalleryActions.a(this.d, var5, com.alensw.PicFolder.R.string.share, var1, new ShareDetailsMedia(this));
   }

   public void a(View var1, Bundle var2) {
      this.a();
      Uri var3 = a(this.e, this.f);
      Intent var4 = new Intent("android.intent.action.EDIT");
      var4.setDataAndType(var3, "image/*");
      var4.addFlags(1);
      var4.putExtra("noFaceDetection", true);
      var4.putExtra("scale", true);
      GalleryActions.a(this.d, var4, com.alensw.PicFolder.R.string.edit, var1, new LaunchMediaEditor(this, var2));
   }

   public void a(String str, DateChangedCallback srcType0743) {
        GalleryAlertDialog srcType0574A = GalleryAlertDialog.a(this.d, 0, com.alensw.PicFolder.R.layout.details);
        if (srcType0574A == null) {
            return;
        }
        srcType0574A.setTitle(str);
        populateDetails((TableLayout) srcType0574A.findViewById(com.alensw.PicFolder.R.id.items));
        srcType0574A.setButton(-1, this.d.getString(17039370), new DismissMediaDetails(this));
        if (srcType0743 != null && this.k.j != 0 && this.i != null && this.i.startsWith("/")) {
            srcType0574A.setButton(-2, this.d.getString(com.alensw.PicFolder.R.string.fix_date), new ConfirmDetailsDateFix(this, srcType0743));
        }
        GalleryDialogs.a(srcType0574A);
   }

   void populateDetails(TableLayout tableLayout) {
        a();
        ArrayList<Pair> arrayList = new ArrayList(16);
        String strC = this.i;
        if (strC == null) {
            strC = a(this.e);
        }
        if (!strC.startsWith("/")) {
            strC = DocumentProviderAccess.c(this.d.getContentResolver(), this.e);
        }
        arrayList.add(new Pair(com.alensw.PicFolder.R.string.path, strC));
        if (this.k.m != null) {
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_description, this.k.m));
        }
        if (this.k.o != null) {
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_type, Picture.a(this.i) ? this.k.o + " (3D)" : this.k.o));
        }
        if (this.h != 0) {
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_size, Formatter.formatFileSize(this.d, this.h)));
        }
        if (this.j != null) {
            String str2 = this.j;
            if (this.k.d != 0) {
                str2 = str2 + " " + this.k.d + (char) 176;
            }
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_resolution, str2));
        }
        if (this.k.l != null) {
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_camera, this.k.l));
        }
        if (this.k.n != null) {
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_exif_info, this.k.n));
        }
        if (this.k.j != 0) {
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_date, a(this.k.j)));
        }
        if (this.k.k != null) {
            arrayList.add(new Pair(com.alensw.PicFolder.R.string.pic_address, GalleryUtils.a("(%.7f,%.7f)", this.k.k[0], this.k.k[1])));
        }
        LayoutInflater layoutInflater = (LayoutInflater) this.d.getSystemService("layout_inflater");
        tableLayout.removeAllViews();
        tableLayout.setColumnShrinkable(1, true);
        for (Pair pair : arrayList) {
            TableRow tableRow = (TableRow) layoutInflater.inflate(com.alensw.PicFolder.R.layout.detail_item, (android.view.ViewGroup) null);
            tableLayout.addView(tableRow);
            ((TextView) tableRow.findViewById(com.alensw.PicFolder.R.id.key)).setText(((Integer) pair.first).intValue());
            ((TextView) tableRow.findViewById(com.alensw.PicFolder.R.id.value)).setText((CharSequence) pair.second);
        }
        if (this.k.k != null) {
            float[] fArr = this.k.k;
            TextView textView = (TextView) ((TableRow) tableLayout.getChildAt(tableLayout.getChildCount() - 1)).findViewById(com.alensw.PicFolder.R.id.value);
            textView.setOnClickListener(new OpenLocationOnMap(this, fArr, textView));
            ReverseGeocoding.a(this.d).a(fArr[0], fArr[1], 0L, new SetLocationDescription(this, textView));
        }
   }

   public void b(View var1) {
      this.a();
      if (this.k.k != null) {
         GalleryActions.a(this.d, this.k.k[0], this.k.k[1], var1);
      } else {
         Toast.makeText(this.d, com.alensw.PicFolder.R.string.no_location_in_picture, 1).show();
      }
   }

   public void b(View var1, Bundle var2) {
      this.a();
      Uri var3 = a(this.e, this.f);
      Intent var4 = new Intent("android.intent.action.ATTACH_DATA");
      var4.setDataAndType(var3, "image/*");
      var4.addFlags(1);
      if (var2 != null) {
         var4.putExtras(var2);
      }

      var4.putExtra("mimeType", var4.getType());
      GalleryActions.a(this.d, var4, com.alensw.PicFolder.R.string.set_as, var1, new LaunchSetAsTarget(this, var3));
   }

   public boolean b() {
      boolean var1;
      if (this.k != null && this.k.h) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }
}
