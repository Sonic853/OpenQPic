package com.alensw.ui.c;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;

class bm extends com.alensw.ui.a.ao {
   final String a;
   final HashSet b;
   final com.alensw.a.bc c;
   final ArrayList e;
   final com.alensw.ui.c.bf f;
   private final GregorianCalendar g;
   private final int[] h;

   bm(com.alensw.ui.c.bf var1, Activity var2, String var3, int var4, String var5, HashSet var6, com.alensw.a.bc var7, ArrayList var8) {
      super(var2, var3, var4);
      this.f = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
      this.e = var8;
      this.g = new GregorianCalendar();
      this.h = new int[com.alensw.ui.c.bf.m.length];
   }

   private String a(long j) {
        GregorianCalendar gregorianCalendar = this.g;
        int[] iArr = this.h;
        gregorianCalendar.setTimeInMillis(j);
        iArr[0] = gregorianCalendar.get(1);
        iArr[1] = gregorianCalendar.get(2) + 1;
        iArr[2] = gregorianCalendar.get(5);
        iArr[3] = gregorianCalendar.get(11);
        iArr[4] = gregorianCalendar.get(12);
        iArr[5] = gregorianCalendar.get(13);
        String strReplace = this.a;
        for (int i = 0; i < com.alensw.ui.c.bf.m.length; i++) {
            String string = Integer.toString(iArr[i]);
            if (string.length() < 2) {
                string = "0" + string;
            }
            strReplace = strReplace.replace(com.alensw.ui.c.bf.m[i], string);
        }
        return strReplace;
    }

   @Override
   protected void a() {
        for (Object entry : this.b) {
         com.alensw.a.d srcType0110 = (com.alensw.a.d) entry;
            String str = srcType0110.b;
            String strA = com.alensw.b.l.b.a(str, false);
            File file = new File(this.f.g, str);
            String path = file.getPath();
            long jLastModified = file.lastModified();
            com.alensw.b.h.c srcType0195A = com.alensw.b.h.c.a(path, true);
            if (srcType0195A != null) {
                jLastModified = srcType0195A.a(jLastModified);
                srcType0195A.a();
            }
            String str2 = a(jLastModified) + "." + strA;
            if (str.compareToIgnoreCase(str2) != 0) {
                File file2 = new File(this.f.g, str2);
                if (file2.exists()) {
                    file2 = com.alensw.b.l.b.a(file2, (String) null);
                }
                if (com.alensw.b.l.b.b(file, file2.getName())) {
                    srcType0110.b = file2.getName();
                    this.c.a(path);
                    this.e.add(file2.getName());
                }
            }
            if (!d()) {
                return;
            }
        }
    }

   @Override
   protected void b() {
      QuickApp.s.a(this.f.g.getPath(), this.e);
      QuickApp.o.a(this.f.g, true);
      if (this.f.g.g()) {
         this.f.g.l();
      }

      this.f.k.clear();
      this.f.o();
   }
}
