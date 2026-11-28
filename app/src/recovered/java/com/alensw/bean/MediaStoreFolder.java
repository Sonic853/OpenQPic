package com.alensw.bean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.text.format.DateUtils;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.FolderScanFilters;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.location.ReverseGeocoding;
import com.alensw.media.MediaFileTypes;
import com.alensw.utils.GalleryUtils;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

public class MediaStoreFolder extends CommonFolder {
   public static final Creator CREATOR = new MediaStoreFolderCreator();
   protected static final HashMap u = new HashMap();
   public static final String[] v = new String[]{"_id", "_data", "_display_name", "mime_type", "_size", "date_modified", "datetaken", "latitude", "longitude"};
   private Context A;
   private ReverseGeocoding B;
   private FolderScanFilters C;
   private boolean D;
   private ArrayList E;
   private ArrayList F;
   private final boolean w;
   private final boolean x;
   private int y;
   private String z;

   public MediaStoreFolder(int var1, String var2, int var3, long var4, long var6, String var8, boolean var9, boolean var10) {
      super(var2, var3, var4, var6, null);
      this.w = var9;
      this.x = var10;
      this.y = var1;
      this.z = var8;
      this.t();
   }

   public MediaStoreFolder(Parcel var1, char var2) {
      super(var1, var2);
      boolean var6 = true;
      int var3 = 0;
      boolean var5;
      if (var1.readInt() != 0) {
         var5 = true;
      } else {
         var5 = false;
      }

      this.w = var5;
      if (var1.readInt() != 0) {
         var5 = var6;
      } else {
         var5 = false;
      }

      this.x = var5;
      this.y = var1.readInt();
      this.z = var1.readString();
      this.t();

      for (int var4 = var1.readInt(); var3 < var4; var3++) {
         var2 = (char)var1.readInt();
         CommonFile var7;
         if (var2 == 'D') {
            var7 = new MediaStoreFolder(var1, var2);
         } else {
            var7 = new MediaStoreFile(var1, var2);
         }

         this.n.add(var7);
      }
   }

   public MediaStoreFolder(CommonFile var1, boolean var2, boolean var3) {
      super(var1);
      this.w = var2;
      this.x = var3;
      this.y = 0;
      this.z = null;
      this.t();
   }

   private String a(float var1, float var2) {
      String var5;
      if (var1 == 0.0F && var2 == 0.0F) {
         var5 = "";
      } else {
         long var3 = ReverseGeocoding.a(var1, var2);
         String var6 = this.B.a(var1, var2, var3);
         var5 = var6;
         if (var6 == null) {
            this.B.a(var1, var2, var3, null);
            this.D = true;
            var5 = "";
         }
      }

      return var5;
   }

   private String a(long var1) {
      return DateUtils.formatDateTime(this.A, var1, 52);
   }

   private void a(ContentResolver resolver, char type) {
      GregorianCalendar calendar = new GregorianCalendar();
      long currentStart = b(calendar);
      a(calendar);
      long currentEnd = calendar.getTimeInMillis() / 1000L;
      long start = currentStart, end = currentEnd;
      String title = a(currentStart * 1000L);
      Cursor cursor = null;
      try {
         cursor = resolver.query(GalleryMediaStore.a(type), v, null, null, "datetaken DESC");
         if (cursor == null || !cursor.moveToFirst()) return;
         int size = this.n.size();
         int count = cursor.getCount();
         if (count > 0) this.n.ensureCapacity(size + count);
         do {
            long modified = cursor.getLong(5);
            long taken = cursor.getLong(6) / 1000L;
            if (taken == 0) taken = modified;
            if (taken < start || taken >= end) {
               calendar.setTimeInMillis(taken * 1000L);
               start = b(calendar);
               a(calendar);
               end = calendar.getTimeInMillis() / 1000L;
               title = a(start * 1000L);
            }
            String path = cursor.getString(1);
            if (!a(path)) continue;
            int id = cursor.getInt(0);
            String location = a(cursor.getFloat(7), cursor.getFloat(8));
            MomentGroupKey key = new MomentGroupKey(start, location);
            MediaStoreFolder folder = (MediaStoreFolder)u.get(key);
            if (folder == null) {
               folder = new MediaStoreFolder(id, title, 0x10000033, modified, taken, path, true, false);
               folder.i = key.b;
               if (type == 'I') this.n.add(folder);
               else this.c(folder);
               u.put(key, folder);
            } else if (folder.h < taken) {
               folder.g = modified;
               folder.h = taken;
               folder.y = id;
               folder.z = path;
               folder.j = null;
            }
            if (taken >= currentStart && taken < currentEnd) {
               String name = cursor.getString(2);
               String mime = cursor.getString(3);
               long bytes = cursor.getLong(4);
               MediaStoreFile file = new MediaStoreFile(type, id, name, mime, 5, bytes, modified, taken, path);
               if (type == 'I') folder.n.add(file);
               else folder.c(file);
            }
         } while (cursor.moveToNext());
      } catch (Throwable error) {
         error.printStackTrace();
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   private static void a(GregorianCalendar var0) {
      if (var0.get(2) == 11) {
         var0.roll(1, 1);
      }

      var0.roll(2, 1);
   }

   private boolean a(String var1) {
      int var3 = this.E.size();
      int var2 = 0;

      boolean var4;
      while (true) {
         if (var2 >= var3) {
            var3 = this.F.size();
            if (var3 == 0) {
               var4 = true;
            } else {
               for (int var5 = 0; var5 < var3; var5++) {
                  if (GalleryUtils.c((String)this.F.get(var5), var1)) {
                     var4 = true;
                     return var4;
                  }
               }

               var4 = false;
            }
            break;
         }

         if (GalleryUtils.c((String)this.E.get(var2), var1)) {
            var4 = false;
            break;
         }

         var2++;
      }

      return var4;
   }

   private static long b(GregorianCalendar var0) {
      int var1 = var0.get(1);
      int var2 = var0.get(2);
      var0.clear();
      var0.set(var1, var2, 1);
      return var0.getTimeInMillis() / 1000L;
   }

   private void b(ContentResolver resolver, char type) {
      GregorianCalendar calendar = new GregorianCalendar();
      calendar.setTimeInMillis(this.h * 1000L);
      long start = b(calendar);
      a(calendar);
      long end = calendar.getTimeInMillis() / 1000L;
      String[] range = {Long.toString(start * 1000L), Long.toString(end * 1000L)};
      Cursor cursor = null;
      try {
         cursor = resolver.query(GalleryMediaStore.a(type), v, "datetaken>=? AND datetaken<?", range, "datetaken DESC");
         if (cursor == null || !cursor.moveToFirst()) return;
         int size = this.n.size();
         int count = cursor.getCount();
         if (count > 0) this.n.ensureCapacity(size + count);
         do {
            long modified = cursor.getLong(5);
            long taken = cursor.getLong(6) / 1000L;
            String path = cursor.getString(1);
            if (!a(path)) continue;
            int id = cursor.getInt(0);
            if (!a(cursor.getFloat(7), cursor.getFloat(8)).equals(this.i)) continue;
            String name = cursor.getString(2);
            String mime = cursor.getString(3);
            long bytes = cursor.getLong(4);
            MediaStoreFile file = new MediaStoreFile(type, id, name, mime, 5, bytes, modified, taken, path);
            if (type == 'I') this.n.add(file);
            else this.c(file);
            if (this.h < taken) {
               this.g = modified;
               this.h = taken;
               this.y = id;
               this.z = path;
               this.j = null;
            }
         } while (cursor.moveToNext());
      } catch (Throwable error) {
         error.printStackTrace();
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   private void c(ContentResolver resolver, char type) {
      Cursor cursor = null;
      try {
         cursor = resolver.query(GalleryMediaStore.a(type), v, null, null, "datetaken DESC");
         if (cursor == null || !cursor.moveToFirst()) return;
         int size = this.n.size();
         int count = cursor.getCount();
         if (count > 0) this.n.ensureCapacity(size + count);
         do {
            String path = cursor.getString(1);
            if (!a(path)) continue;
            int id = cursor.getInt(0);
            long modified = cursor.getLong(5);
            long taken = cursor.getLong(6) / 1000L;
            String name = cursor.getString(2);
            String mime = cursor.getString(3);
            long bytes = cursor.getLong(4);
            MediaStoreFile file = new MediaStoreFile(type, id, name, mime, 5, bytes, modified, taken, path);
            if (type == 'I') this.n.add(file);
            else this.c(file);
         } while (cursor.moveToNext());
      } catch (Throwable error) {
         error.printStackTrace();
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   private void t() {
      this.l = a(0, 1);
      this.m = r;
      this.A = QuickApp.a();
      this.B = ReverseGeocoding.a(this.A);
      this.C = FolderScanFilters.a(this.A);
   }

   @Override
   public int a(Uri var1) {
      int var2 = 0;
      if (!"media".equals(var1.getAuthority())) {
         var2 = super.a(var1);
      } else {
         int var3;
         try {
            var3 = GalleryUtils.a(var1.getLastPathSegment(), 0);
         } catch (Throwable var6) {
            var2 = super.a(var1);
            return var2;
         }

         int var4 = this.n.size();

         while (true) {
            if (var2 >= var4) {
               var2 = super.a(var1);
               break;
            }

            CommonFile var5 = (CommonFile)this.n.get(var2);
            if (var5 instanceof MediaStoreFile && ((MediaStoreFile)var5).k == var3) {
               break;
            }

            var2++;
         }
      }

      return var2;
   }

   @Override
   public Cursor a(ContentResolver var1, String var2) {
      boolean var3;
      if ((QuickApp.q.c & 2) != 0) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.E = this.C.a(true);
      this.F = this.C.a(false);
      if (this.w) {
         this.D = false;
         if (this.x) {
            u.clear();
            long var4 = System.currentTimeMillis();
            this.a(var1, 'I');
            if (var3) {
               this.a(var1, 'V');
            }

            Log.d("MediaStoreFolder", "query: " + this.o() + " folders, used=" + (System.currentTimeMillis() - var4));
         } else {
            this.b(var1, 'I');
            if (var3) {
               this.b(var1, 'V');
            }
         }

         if (this.D) {
            this.D = false;
            this.B.a(new NotifyMediaStoreChange(this, var1));
         }
      } else {
         long var6 = System.currentTimeMillis();
         this.c(var1, 'I');
         if (var3) {
            this.c(var1, 'V');
         }

         Log.d("MediaStoreFolder", "query: " + this.o() + " folders, used=" + (System.currentTimeMillis() - var6));
      }

      return null;
   }

   @Override
   public CommonFolder a(boolean var1) {
      return this.b(var1);
   }

   @Override
   public void a(ContentResolver var1, ContentObserver var2) {
      var1.registerContentObserver(GalleryMediaStore.a('I'), false, var2);
      var1.registerContentObserver(GalleryMediaStore.a('V'), false, var2);
   }

   public MediaStoreFolder b(boolean var1) {
      MediaStoreFolder var2 = new MediaStoreFolder(this, this.w, this.x);
      var2.k = this.k;
      var2.l = this.l;
      if (var1) {
         var2.a(this, true);
      }

      return var2;
   }

   @Override
   public void b(ContentResolver var1, ContentObserver var2) {
      var1.unregisterContentObserver(var2);
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof MediaStoreFolder)) {
            return false;
        }
        MediaStoreFolder mediaStoreFolder = (MediaStoreFolder) var1;
        return this.w == mediaStoreFolder.w && this.x == mediaStoreFolder.x && this.g == mediaStoreFolder.g && this.b == mediaStoreFolder.b && this.y == mediaStoreFolder.y;
    }

   @Override
   public int hashCode() {
      return this.y + (int)this.g + this.b;
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         if (this.x) {
            this.j = GalleryMediaStore.a('I');
         } else if (!this.n()) {
            this.j = this.b(0);
         } else if (this.z != null) {
            this.j = GalleryMediaStore.a(MediaFileTypes.a(this.z)).buildUpon().appendEncodedPath(Integer.toString(this.y)).build();
         } else {
            this.j = GalleryMediaStore.a('I');
         }
      }

      return this.j;
   }

   @Override
   public String j() {
      return this.z;
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      byte var4 = 1;
      super.writeToParcel(var1, var2);
      byte var3;
      if (this.w) {
         var3 = 1;
      } else {
         var3 = 0;
      }

      var1.writeInt(var3);
      if (this.x) {
         var3 = var4;
      } else {
         var3 = 0;
      }

      var1.writeInt(var3);
      var1.writeInt(this.y);
      var1.writeString(this.z);
      var1.writeInt(this.n.size());
      Iterator var5 = this.n.iterator();

      while (var5.hasNext()) {
         ((CommonFile)var5.next()).writeToParcel(var1, var2);
      }
   }
}
