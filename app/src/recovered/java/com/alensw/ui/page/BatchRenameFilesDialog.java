package com.alensw.ui.page;

import android.app.Activity;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.ScannedMediaFile;
import com.alensw.gallery.ThumbnailDatabase;
import com.alensw.image.ExifData;
import com.alensw.ui.dialog.BackgroundProgressDialog;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;

class BatchRenameFilesDialog extends BackgroundProgressDialog {
   final String a;
   final HashSet b;
   final ThumbnailDatabase c;
   final ArrayList e;
   final LocalFolderPage f;
   private final GregorianCalendar g;
   private final int[] h;

   BatchRenameFilesDialog(LocalFolderPage var1, Activity var2, String var3, int var4, String var5, HashSet var6, ThumbnailDatabase var7, ArrayList var8) {
      super(var2, var3, var4);
      this.f = var1;
      this.a = var5;
      this.b = var6;
      this.c = var7;
      this.e = var8;
      this.g = new GregorianCalendar();
      this.h = new int[LocalFolderPage.m.length];
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
        for (int i = 0; i < LocalFolderPage.m.length; i++) {
            String string = Integer.toString(iArr[i]);
            if (string.length() < 2) {
                string = "0" + string;
            }
            strReplace = strReplace.replace(LocalFolderPage.m[i], string);
        }
        return strReplace;
    }

   @Override
   protected void a() {
        for (Object entry : this.b) {
         ScannedMediaFile srcType0110 = (ScannedMediaFile) entry;
            String str = srcType0110.b;
            String strA = GalleryUtils.a(str, false);
            File file = new File(this.f.g, str);
            String path = file.getPath();
            long jLastModified = file.lastModified();
            ExifData srcType0195A = ExifData.a(path, true);
            if (srcType0195A != null) {
                jLastModified = srcType0195A.a(jLastModified);
                srcType0195A.a();
            }
            String str2 = a(jLastModified) + "." + strA;
            if (str.compareToIgnoreCase(str2) != 0) {
                File file2 = new File(this.f.g, str2);
                if (file2.exists()) {
                    file2 = GalleryUtils.a(file2, (String) null);
                }
                if (GalleryUtils.b(file, file2.getName())) {
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
