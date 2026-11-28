package com.alensw.gallery;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Build.VERSION;
import android.provider.MediaStore.Files;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import com.alensw.documents.DocumentProviderAccess;
import com.alensw.jni.JniUtils;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class GalleryMediaStore {
   public static final Uri a = Media.EXTERNAL_CONTENT_URI;
   public static final Uri b = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
   public static final Uri c = Thumbnails.EXTERNAL_CONTENT_URI;
   public static final Uri d = android.provider.MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI;
   public static final Uri[] e = new Uri[]{a, b};
   public static final String[] f = new String[]{"volume"};
   public static final String[] g = new String[]{"bucket_id"};
   public static final String[] h = new String[]{"_data"};
   public static final String[] i = new String[]{"_id"};
   public static final String[] j = new String[]{"_id", "_data"};
   public static final String[] k = new String[]{"date_modified", "_size", "mini_thumb_magic", "mime_type"};
   public static final String[] l = new String[]{"_data", "date_modified", "datetaken"};
   public static final String[] m = new String[]{"_data", "_size"};
   public static final String n = ScannedFolder.b + File.separatorChar + "DCIM" + File.separatorChar + ".thumbnails" + File.separatorChar;
   private final ContentResolver o;
   private final MediaBucketIndex p;
   private final MediaScanQueue q;
   private final MediaScannerConnectionClient r = new ScannedMediaIdListener(this);

   public GalleryMediaStore(Context var1) {
      this.o = var1.getContentResolver();
      this.p = new MediaBucketIndex(this.o);
      this.q = new MediaScanQueue(var1, this.r);
   }

   public static Uri a(char var0) {
      Uri var1;
      switch (var0) {
         case 'A':
            var1 = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            break;
         case 'I':
            var1 = a;
            break;
         case 'V':
            var1 = b;
            break;
         default:
            if (VERSION.SDK_INT >= 11) {
               var1 = Files.getContentUri("external");
            } else {
               var1 = a;
            }
      }

      return var1;
   }

   static MediaBucketIndex a(GalleryMediaStore var0) {
      return var0.p;
   }

   @SuppressLint("DefaultLocale")
   public static String c(String var0) {
      return GalleryUtils.e(var0).toLowerCase() + '%';
   }

   public int a(Uri var1, ContentValues var2, String var3, String[] var4) {
      int var5;
      try {
         var5 = this.o.update(var1, var2, var3, var4);
      } catch (Throwable var6) {
         Log.e("SysMediaStore", "update: ", var6);
         var5 = -1;
      }

      return var5;
   }

   protected int a(Uri var1, String var2, String[] var3) {
      int var4;
      try {
         var4 = this.o.delete(var1, var2, var3);
      } catch (Throwable var5) {
         Log.e("SysMediaStore", "remove: ", var5);
         var4 = -1;
      }

      return var4;
   }

   public Integer a(Uri uri, String[] columns, String selection, String[] args) {
      Cursor cursor = null;
      try {
         cursor = Media.query(this.o, uri, columns, selection, args, null);
         return cursor != null && cursor.moveToFirst() ? cursor.getInt(0) : null;
      } catch (Throwable ignored) {
         return null;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   public Integer a(String var1, char var2) {
      Integer var3 = null;
      Integer var4 = this.p.a(var1);
      if (var4 != MediaBucketIndex.a) {
         if (var4 != null) {
            var3 = var4;
         } else {
            var4 = this.a(a(var2), i, "_data=?", new String[]{var1});
            if (var4 != null) {
               this.p.a(var1, var4);
               var3 = var4;
            }
         }
      }

      return var3;
   }

   public Long a(Uri uri, String[] columns, String selection) {
      Cursor cursor = null;
      try {
         cursor = Media.query(this.o, uri, columns, selection, null);
         return cursor != null && cursor.moveToFirst() ? cursor.getLong(0) : null;
      } catch (Throwable ignored) {
         return null;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   public String a(Uri var1) {
        String strB = null;
        if (var1 == null) {
            return null;
        }
        if ("file".equals(var1.getScheme())) {
            return var1.getPath();
        }
        String authority = var1.getAuthority();
        if ("media".equals(authority)) {
            String queryParameter = var1.getQueryParameter("bucketId");
            if (queryParameter != null) {
                strB = b(queryParameter);
            } else {
                strB = Media.EXTERNAL_CONTENT_URI.equals(var1) ? b((String) null) : b(var1);
            }
        } else if ("downloads".equals(authority)) {
            strB = b(var1);
        } else if (DocumentProviderAccess.d(var1)) {
            try {
                String strB2 = DocumentProviderAccess.b(var1);
                int iIndexOf = strB2.indexOf(58);
                if (iIndexOf >= 0) {
                    String strSubstring = strB2.substring(0, iIndexOf);
                    String strSubstring2 = strB2.substring(iIndexOf + 1);
                    String strD = QuickApp.p.d(strSubstring);
                    if (strD != null) {
                        strB = new File(strD, strSubstring2).getPath();
                    } else if ("image".equals(strSubstring)) {
                        strB = b(a, h, "_id=" + strSubstring2);
                    } else if ("video".equals(strSubstring)) {
                        strB = b(b, h, "_id=" + strSubstring2);
                    }
                } else if ("com.android.providers.downloads.documents".equals(authority)) {
                    strB = b(ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(strB2).longValue()));
                }
            } catch (Throwable th) {
                Log.e("SysMediaStore", "get path: ", th);
            }
        }
        if (strB != null) {
            return strB;
        }
        Log.w("SysMediaStore", "get path: " + strB + ", uri=" + var1);
        return strB;
    }

   public HashMap a(String path, String[] columns) {
      Integer bucket = MediaBucketIndex.e(path);
      String prefix = GalleryUtils.e(path);
      int length = prefix.length();
      boolean byBucket = bucket != null;
      String selection = byBucket ? "bucket_id=?" : "LOWER(_data) LIKE ?";
      String[] args = {byBucket ? bucket.toString() : c(prefix)};
      int valueCount = columns.length - 1;
      HashMap<String, long[]> rows = null;
      for (Uri uri : e) {
         Cursor cursor = null;
         try {
            cursor = Media.query(this.o, uri, columns, selection, args, null);
            if (cursor != null && cursor.moveToFirst()) {
               if (rows == null) rows = new HashMap<>(Math.max(cursor.getCount(), 256));
               do {
                  String name = cursor.getString(0);
                  if (name.length() > length && (byBucket || name.indexOf(File.separatorChar, length) == -1)) {
                     long[] values = new long[valueCount];
                     for (int i = 0; i < valueCount; i++) values[i] = cursor.getLong(i + 1);
                     rows.put(name.substring(length), values);
                  }
               } while (cursor.moveToNext());
            }
         } catch (Throwable ignored) {
            // Keep rows read from this URI and proceed to the next media table.
         } finally {
            if (cursor != null) cursor.close();
         }
      }
      return rows;
   }

   public void a() {
      this.p.a();
   }

   public void a(String var1) {
      this.p.b(var1);
   }

   public void a(String var1, ArrayList var2) {
        if (var1 == null || var2 == null || GalleryUtils.f(var1) || GalleryUtils.h(var1)) {
            return;
        }
        String strE = GalleryUtils.e(var1);
        java.util.Iterator it = var2.iterator();
        while (it.hasNext()) {
            this.q.b(strE + ((String) it.next()));
        }
    }

   public boolean a(String var1, char var2, boolean var3) {
      boolean var6 = true;
      boolean var4 = true;
      boolean var5 = false;
      if (var1 == null) {
         var3 = var5;
      } else {
         this.q.a(var1);
         Uri var9 = a(var2);
         Integer var8 = this.p.c(var1);
         if (var3) {
            Integer var11;
            if (var8 == null) {
               var11 = this.a(a(var2), i, "_data=?", new String[]{var1});
            } else {
               var11 = var8;
            }

            var3 = var5;
            if (var11 != null) {
               String[] var7 = new String[]{var11.toString()};
               ContentValues var12 = new ContentValues();
               var12.put("_data", "");
               var3 = var5;
               if (this.a(var9, var12, "_id=?", var7) > 0) {
                  if (this.a(var9, "_id=?", var7) > 0) {
                     var3 = var4;
                  } else {
                     var3 = false;
                  }
               }
            }
         } else {
            StringBuilder var10 = new StringBuilder();
            String var14;
            if (var8 != null) {
               var14 = "_id";
            } else {
               var14 = "_data";
            }

            var14 = var10.append(var14).append("=?").toString();
            if (var8 != null) {
               var1 = var8.toString();
            }

            if (this.a(var9, var14, new String[]{var1}) > 0) {
               var3 = var6;
            } else {
               var3 = false;
            }
         }
      }

      return var3;
   }

   public boolean a(String var1, int var2) {
      boolean var3 = true;
      boolean var4 = false;
      Integer var5 = this.a(var1, 'I');
      if (var5 == null) {
         var3 = var4;
      } else {
         String var7 = "_id=" + var5;
         Long var6 = this.a(a, new String[]{"datetaken"}, var7);
         ContentValues var8 = new ContentValues();
         var8.put("orientation", var2);
         var8.put("date_modified", JniUtils.fuGetFileTime(var1));
         if (var6 != null) {
            var8.put("datetaken", var6);
         }

         if (this.a(a, var8, var7, null) <= 0) {
            var3 = false;
         }
      }

      return var3;
   }

   public String[] a(String var1, char var2, String[] var3) {
      Integer var5 = this.a(var1, var2);
      StringBuilder var6 = new StringBuilder();
      String var4;
      if (var5 != null) {
         var4 = "_id";
      } else {
         var4 = "_data";
      }

      var4 = var6.append(var4).append("=?").toString();
      if (var5 != null) {
         var1 = var5.toString();
      }

      return this.b(a(var2), var3, var4, new String[]{var1});
   }

   public Uri b(String var1, char var2) {
      Integer var3 = this.a(var1, var2);
      Uri var4;
      if (var3 != null) {
         var4 = ContentUris.withAppendedId(a(var2), var3.intValue());
      } else {
         var4 = null;
      }

      return var4;
   }

   public String b(Uri uri) {
      Cursor cursor = null;
      try {
         cursor = this.o.query(uri, h, null, null, null);
         return cursor != null && cursor.moveToFirst() ? cursor.getString(0) : null;
      } catch (Throwable ignored) {
         return null;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   public String b(Uri uri, String[] columns, String selection) {
      Cursor cursor = null;
      try {
         cursor = Media.query(this.o, uri, columns, selection, null);
         return cursor != null && cursor.moveToFirst() ? cursor.getString(0) : null;
      } catch (Throwable ignored) {
         return null;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   @SuppressLint("DefaultLocale")
   public String b(String bucket) {
      Uri uri = a.buildUpon().appendQueryParameter("limit", "1").build();
      String selection = bucket != null ? "bucket_id=?" : "LOWER(_data) LIKE ?";
      String[] args = {bucket != null ? bucket : ScannedFolder.b.toLowerCase() + '%'};
      Cursor cursor = null;
      try {
         cursor = Media.query(this.o, uri, h, selection, args, "datetaken DESC");
         return cursor != null && cursor.moveToFirst() ? cursor.getString(0) : null;
      } catch (Throwable ignored) {
         return null;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   protected void b() {
        for (Uri uri : new Uri[]{c, d}) {
            a(uri, (String) null, (String[]) null);
        }
        for (Uri uri2 : e) {
            String str = n + ".thumbdata3-" + uri2.hashCode();
            GalleryUtils.i(str);
            File file = new File(str);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        GalleryUtils.i(n + "image_last_thumb");
        GalleryUtils.i(n + "video_last_thumb");
    }

   public String[] b(Uri uri, String[] columns, String selection, String[] args) {
      String[] values = new String[columns.length];
      Cursor cursor = null;
      try {
         cursor = Media.query(this.o, uri, columns, selection, args, null);
         if (cursor != null && cursor.moveToFirst()) {
            for (int i = 0; i < values.length; i++) values[i] = cursor.getString(i);
         }
      } catch (Throwable error) {
         Log.e("SysMediaStore", "query values: " + error);
      } finally {
         if (cursor != null) cursor.close();
      }
      return values;
   }

   public boolean c() {
      Cursor cursor = null;
      try {
         cursor = this.o.query(android.provider.MediaStore.getMediaScannerUri(), f, null, null, null);
         if (cursor == null || !cursor.moveToFirst()) return false;
         String volume = cursor.getString(0);
         return volume != null && volume.length() > 0;
      } catch (Throwable error) {
         Log.e("SysMediaStore", "scanning: ", error);
         return true;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   @SuppressLint("DefaultLocale")
   @TargetApi(14)
   public boolean d(String var1) {
      boolean var8 = false;
      boolean var7 = var8;
      if (var1 != null) {
         if (var1.length() == 0) {
            var7 = var8;
         } else {
            ContentValues var10 = new ContentValues();
            String[] var9 = new String[]{c(var1)};
            if (VERSION.SDK_INT >= 16) {
               var10.put("media_type", 0);
               Uri[] var11 = e;
               int var4 = var11.length;
               int var2 = 0;
               int var3 = 0;

               while (true) {
                  if (var2 >= var4) {
                     Log.d("SysMediaStore", "update folder: " + var1 + ", count=" + var3);
                     this.p.a();
                     var7 = true;
                     break;
                  }

                  int var5 = this.a(var11[var2], var10, "LOWER(_data) LIKE ?", var9);
                  var7 = var8;
                  if (var5 < 0) {
                     break;
                  }

                  var3 += var5;
                  var2++;
               }
            } else {
               var10.put("_data", "");
               Uri[] var20 = e;
               int var15 = var20.length;
               int var13 = 0;
               int var12 = 0;

               while (true) {
                  if (var13 >= var15) {
                     Log.d("SysMediaStore", "update folder: " + var1 + ", count=" + var12);
                     Uri[] var19 = e;
                     int var18 = var19.length;
                     var13 = 0;

                     while (var13 < var18) {
                        int var6 = this.a(var19[var13], "_data=?", new String[]{""});
                        var15 = var12;
                        if (var6 >= 0) {
                           var15 = var12 - var6;
                        }

                        var13++;
                        var12 = var15;
                     }

                     Log.d("SysMediaStore", "folder remain: " + var1 + ", count=" + var12);
                     this.p.a();
                     if (var12 == 0) {
                        var7 = true;
                     } else {
                        var7 = false;
                     }
                     break;
                  }

                  int var17 = this.a(var20[var13], var10, "LOWER(_data) LIKE ?", var9);
                  var7 = var8;
                  if (var17 < 0) {
                     break;
                  }

                  var13++;
                  var12 += var17;
               }
            }
         }
      }

      return var7;
   }

   public int e(String path) {
      int count = 0;
      if (VERSION.SDK_INT >= 16 && !GalleryUtils.f(path) && !GalleryUtils.h(path)) {
         Uri uri = Files.getContentUri("external");
         String[] columns = {"media_type", "_data"};
         String[] args = {c(path)};
         Cursor cursor = null;
         try {
            cursor = this.o.query(uri, columns, "LOWER(_data) LIKE ?", args, null);
            if (cursor != null && cursor.moveToFirst()) {
               do {
                  if (cursor.getInt(0) == 0) {
                     String name = cursor.getString(1);
                     if (name != null) {
                        this.q.b(name);
                        count++;
                     }
                  }
               } while (cursor.moveToNext());
            }
         } catch (Throwable error) {
            Log.e("SysMediaStore", "rescan: ", error);
         } finally {
            if (cursor != null) cursor.close();
         }
      }
      Log.d("SysMediaStore", "rescan: " + count);
      return count;
   }

   public void f(String var1) {
      if (var1 != null && !GalleryUtils.f(var1) && !GalleryUtils.h(var1)) {
         this.q.b(var1);
      }
   }
}
