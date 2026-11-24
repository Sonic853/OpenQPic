package com.alensw.ui.c;

import android.app.Activity;
import android.net.Uri;
import android.text.format.Formatter;
import com.alensw.PicFolder.QuickApp;
import com.alensw.bean.CommonFile;
import com.alensw.jni.JniUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;

class d extends com.alensw.ui.a.ao {
   final File a;
   final com.alensw.a.ba b;
   final com.alensw.a.bc c;
   final HashSet e;
   final ArrayList f;
   final ArrayList g;
   final com.alensw.ui.c.c h;

   d(
      com.alensw.ui.c.c var1, Activity var2, String var3, int var4, File var5, com.alensw.a.ba var6, com.alensw.a.bc var7, HashSet var8, ArrayList var9, ArrayList var10
   ) {
      super(var2, var3, var4);
      this.h = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
      this.e = var8;
      this.f = var9;
      this.g = var10;
   }

   private boolean a(File var1, long var2) {
      long var4 = JniUtils.fuGetAvailBytes(var1.getPath());
      boolean var6;
      if (var4 > 4194304L + var2) {
         var6 = true;
      } else {
         this.a("Low available space: " + Formatter.formatFileSize(this.h.f.C, var4));
         var6 = false;
      }

      return var6;
   }

   @Override
   protected void a() {
        char cH;
        String str;
        String strA;
        Uri uriFromFile;
        boolean zA;
        com.alensw.a.e.a(this.a);
        for (Object obj : this.h.d) {
            if (obj instanceof com.alensw.a.d) {
                com.alensw.a.d srcType0110 = (com.alensw.a.d) obj;
                cH = srcType0110.c;
                str = srcType0110.b;
                strA = srcType0110.a();
                uriFromFile = Uri.fromFile(new File(strA));
            } else if (obj instanceof CommonFile) {
                CommonFile commonFile = (CommonFile) obj;
                String strA2 = commonFile.a();
                cH = commonFile.h();
                if (com.alensw.b.f.c.a(strA2) == 0) {
                    strA2 = com.alensw.b.f.c.a(strA2, commonFile.b());
                }
                String strJ = commonFile.j();
                uriFromFile = commonFile.i();
                str = strA2;
                strA = strJ;
            } else {
                continue;
            }
            File file = new File(this.a, str);
            if (strA == null || !strA.equalsIgnoreCase(file.getPath())) {
                File fileA = file.exists() ? com.alensw.b.l.b.a(file, (String) null) : file;
                zA = false;
                if (this.h.b) {
                    try {
                        com.alensw.b.c.f source = com.alensw.b.c.f.a(this.h.e, uriFromFile);
                        try {
                            if (!a(this.a, source.e())) return;
                            zA = com.alensw.b.l.b.a(source, fileA);
                        } finally { source.h(); }
                    } catch (Throwable problem) { problem.printStackTrace(); }
                } else {
                    try {
                        File source = new File(strA);
                        zA = source.renameTo(fileA);
                        if (!zA) {
                            if (!a(this.a, source.length())) return;
                            zA = com.alensw.b.l.b.a(source, fileA);
                            if (zA) com.alensw.b.l.b.c(source);
                        }
                    } catch (Throwable problem) { problem.printStackTrace(); }
                    if (zA && strA != null) {
                        this.b.a(strA, cH, false);
                        this.c.a(strA);
                    }
                }
                if (zA) {
                    this.e.add(obj);
                    this.f.add(fileA.getName());
                } else if (strA != null) {
                    this.g.add(strA);
                }
                if (!d()) {
                    return;
                }
            }
        }
    }

   @Override
   protected void b() {
      this.h.a.a(this.h.b, this.e);
      String var2 = this.a.getPath();
      com.alensw.a.e var1 = QuickApp.o.a(this.h.f.C, Uri.fromFile(this.a));
      if (var1 != null) {
         QuickApp.o.a(var1, true);
      }

      if (this.f.size() > 0) {
         QuickApp.s.a(var2, this.f);
         QuickApp.x = false;
         QuickApp.q.a(4, var2, null);
      } else {
         this.g.add(0, var2);

         for (Object entry : this.g) {
         java.lang.String var4 = (java.lang.String) entry;
            if (!var4.startsWith(com.alensw.a.e.b + File.separatorChar)) {
               this.h.f.a(var4);
               break;
            }
         }
      }
   }
}
