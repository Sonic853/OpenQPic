package com.alensw.ui.c;

import android.app.Activity;
import android.content.ContentValues;
import android.net.Uri;
import com.alensw.PicFolder.QuickApp;
import java.io.File;
import java.util.ArrayList;

class bh extends com.alensw.ui.a.ao {
   final ArrayList a;
   final com.alensw.a.ba b;
   final String[] c;
   final com.alensw.ui.c.bf e;

   bh(com.alensw.ui.c.bf var1, Activity var2, String var3, int var4, ArrayList var5, com.alensw.a.ba var6, String[] var7) {
      super(var2, var3, var4);
      this.e = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
   }

   @Override
   protected void a() {
        for (Object object : this.a) {
            Uri uri;
            File file = new File(this.e.g, ((com.alensw.a.d)object).b);
            String string2 = file.getPath();
            long l = file.lastModified();
            long l2 = com.alensw.b.l.b.a(file, ((com.alensw.a.d)object).c, l);
            Number number = this.b.a(string2, ((com.alensw.a.d)object).c);
            String string3 = null;
            if (number != null) {
                uri = com.alensw.a.ba.a(((com.alensw.a.d)object).c);
                string3 = "_id=" + number;
                if (((com.alensw.a.d)object).c == 'V' && l2 == l && (number = this.b.a(uri, this.c, string3)) != null && (Long)number > 0L) {
                    l2 = (Long)number;
                }
            } else {
                uri = null;
            }
            long l3 = l;
            if (l != l2) {
                try {
                    boolean bl = file.setLastModified(l2);
                    l3 = l;
                    if (bl) {
                        l3 = l2;
                    }
                }
                catch (Throwable throwable) {
                    l3 = l;
                }
            }
            ((com.alensw.a.d)object).d = (int)(l3 / 1000L);
            ((com.alensw.a.d)object).e = (int)(l2 / 1000L);
            QuickApp.r.a(string2, (int)(l3 / 1000L));
            if (string3 != null) {
                object = new ContentValues();
                ((ContentValues)object).put("datetaken", l2);
                ((ContentValues)object).put("date_modified", (int)(l3 / 1000L));
                this.b.a(uri, (ContentValues)object, string3, null);
            }
            if (this.d()) continue;
        }
    }

   @Override
   protected void b() {
      if (this.e.g.e()) {
         this.e.g.l();
         this.e.f.requestLayout();
      }
   }
}
