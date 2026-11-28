package com.alensw.bean;

import android.database.Cursor;
import android.net.Uri;
import com.alensw.documents.DocumentProviderAccess;
public class DocumentRoot extends CommonRoot {
   public static final String[] s = new String[]{"root_id", "document_id", "title", "mime_type", "flags", "icon", "summary"};
   protected final String q;
   protected final String r;

   public DocumentRoot(String var1, String var2, String var3, String var4, int var5) {
      super(var3, var4, var5);
      this.q = var1;
      if (var2 == null) {
         var2 = "";
      }

      this.r = var2;
   }

   public static DocumentRoot a(String var0, Cursor var1) {
      String var2 = var1.getString(0);
      DocumentRoot var3 = new DocumentRoot(var0, var1.getString(1), var1.getString(2), var1.getString(3), var1.getInt(4));
      var3.l = var1.getInt(5);
      var3.n = var2;
      var3.o = var1.getString(6);
      return var3;
   }

   @Override
   public Uri i() {
      if (this.j == null) {
         this.j = DocumentProviderAccess.c(this.q, this.r);
      }

      return this.j;
   }

   @Override
   public CommonFolder k() {
      return this.m();
   }

   public String l() {
      return this.r;
   }

   public DocumentFolder m() {
      return new DocumentFolder(this.q, this.r, this.c, this.e, this.g, this.h, null);
   }
}
