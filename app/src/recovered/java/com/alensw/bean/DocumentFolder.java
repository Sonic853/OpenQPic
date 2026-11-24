package com.alensw.bean;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import java.util.Iterator;
import java.util.Set;

public class DocumentFolder extends CommonFolder {
   public static final Creator CREATOR = new com.alensw.bean.h();
   public static final String[] w = new String[]{"document_id", "_display_name", "mime_type", "flags", "_size", "last_modified", "datetaken", "comment"};
   protected final String u;
   protected final String v;
   private Uri x;

   public DocumentFolder(Parcel var1, char var2) {
      super(var1, var2);
      this.u = var1.readString();
      this.v = var1.readString();
      int var4 = var1.readInt();

      for (int var3 = 0; var3 < var4; var3++) {
         var2 = (char)var1.readInt();
         CommonFile var5;
         if (var2 == 'D') {
            var5 = new DocumentFolder(var1, var2);
         } else {
            var5 = new DocumentFile(var1, var2);
         }

         this.n.add(var5);
      }
   }

   public DocumentFolder(CommonFile var1, String var2, String var3) {
      super(var1);
      this.u = var2;
      if (var3 == null) {
         var3 = "";
      }

      this.v = var3;
   }

   public DocumentFolder(String var1, String var2, String var3, int var4, long var5, long var7, String var9) {
      super(var3, var4, var5, var7, var9);
      this.u = var1;
      if (var2 == null) {
         var2 = "";
      }

      this.v = var2;
   }

   @Override
   public int a(Uri var1) {
      int var2;
      if (!this.u.equals(var1.getAuthority())) {
         var2 = super.a(var1);
      } else {
         String var5;
         try {
            var5 = com.alensw.b.j.a.b(var1);
         } catch (Throwable var6) {
            var2 = super.a(var1);
            return var2;
         }

         int var3 = this.n.size();
         var2 = 0;

         while (true) {
            if (var2 >= var3) {
               var2 = super.a(var1);
               break;
            }

            CommonFile var4 = (CommonFile)this.n.get(var2);
            if (var4 instanceof DocumentFile && ((DocumentFile)var4).l.equals(var5)) {
               break;
            }

            var2++;
         }
      }

      return var2;
   }

   @Override
   public Cursor a(ContentResolver var1, String var2) {
      return var1.query(this.q(), w, null, null, var2);
   }

   @Override
   public CommonFolder a(boolean var1) {
      return this.b(var1);
   }

   @Override
   public void a(Cursor var1, Set var2) {
      if (var1.moveToFirst()) {
         do {
            String var10 = var1.getString(0);
            String var8 = var1.getString(1);
            String var9 = var1.getString(2);
            int var3 = var1.getInt(3);
            long var6 = var1.getLong(4) / 1000L;
            long var4 = var1.getLong(5) / 1000L;
            String var11 = var1.getString(6);
            if ((var2 == null || !var2.contains(var10)) && CommonFile.a(var9, var8) == 'D') {
               this.n.add(new DocumentFolder(this.u, var10, var8, var3, var6, var4, var11));
            }
         } while (var1.moveToNext());
      }
   }

   @Override
   public Object[] a(ContentResolver resolver) {
      String[] columns = {"_size", "last_modified"};
      Cursor cursor = null;
      try {
         cursor = resolver.query(i(), columns, null, null, null);
         if (cursor != null && cursor.moveToNext()) {
            return new Object[]{cursor.getLong(0), cursor.getLong(1)};
         }
         return null;
      } catch (Throwable ignored) {
         return null;
      } finally {
         if (cursor != null) cursor.close();
      }
   }

   public DocumentFolder b(boolean var1) {
      DocumentFolder var2 = new DocumentFolder(this, this.u, this.v);
      var2.k = this.k;
      var2.l = this.l;
      if (var1) {
         var2.a(this, true);
      }

      return var2;
   }

   @Override
   public void b(ContentResolver var1) {
   }

   @Override
   public void b(Cursor var1, Set var2) {
        if (var1.moveToFirst()) {
            int size = this.n.size();
            int count = var1.getCount();
            if (count > 0) {
                this.n.ensureCapacity(size + count);
            }
            do {
                String string = var1.getString(0);
                String string2 = var1.getString(1);
                String string3 = var1.getString(2);
                int i = var1.getInt(3);
                long j = var1.getLong(4);
                long j2 = var1.getLong(5) / 1000;
                long j3 = var1.getLong(6) / 1000;
                String string4 = var1.getString(7);
                if (var2 == null || !var2.contains(string)) {
                    char cA = CommonFile.a(string3, string2);
                    this.n.add(cA == 'D' ? new DocumentFolder(this.u, string, string2, i, j2, j3, string4) : new DocumentFile(cA, this.u, string, string2, string3, i, j, j2, j3, string4));
                }
            } while (var1.moveToNext());
        }
    }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof DocumentFolder)) {
            return false;
        }
        DocumentFolder documentFolder = (DocumentFolder) var1;
        return this.g == documentFolder.g && this.b == documentFolder.b && (this.v == documentFolder.v || this.v.equals(documentFolder.v));
    }

   @Override
   public int hashCode() {
      return this.v.hashCode() + (int)this.g + this.b;
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         this.j = com.alensw.b.j.a.c(this.u, this.v);
      }

      return this.j;
   }

   @Override
   public Uri q() {
      if (this.x == null) {
         this.x = com.alensw.b.j.a.a(this.i());
      }

      return this.x;
   }

   public String t() {
      return this.v;
   }

   @Override
   public void writeToParcel(Parcel var1, int var2) {
      super.writeToParcel(var1, var2);
      var1.writeString(this.u);
      var1.writeString(this.v);
      var1.writeInt(this.n.size());
      Iterator var3 = this.n.iterator();

      while (var3.hasNext()) {
         ((CommonFile)var3.next()).writeToParcel(var1, var2);
      }
   }
}
