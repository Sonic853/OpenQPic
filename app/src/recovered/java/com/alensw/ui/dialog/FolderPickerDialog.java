package com.alensw.ui.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Build.VERSION;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.ScannedFolder;
import com.alensw.svg.SvgPicture;
import com.alensw.ui.activity.GalleryChromeActivity;
import com.alensw.ui.activity.GalleryIntentActivity;
import com.alensw.ui.view.PathBar;
import com.alensw.utils.GalleryUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FolderPickerDialog extends GalleryAlertDialog {
   public static final Comparator m = new PickerFileNameComparator();
   protected final PathBar c;
   protected final ListView d;
   protected final Drawable e;
   protected final boolean f;
   protected final boolean g;
   protected final int h;
   protected final int i;
   protected final int j;
   protected ScannedFolder k;
   protected ScannedFolder l;
   protected BaseAdapter n = new PickerFolderAdapter(this);

   @TargetApi(17)
   public FolderPickerDialog(Context context, int i, File file, FolderSelectionCallback srcType0549) {
        super(context);
        this.n = new PickerFolderAdapter(this);
        super.setTitle(com.alensw.PicFolder.R.string.select_a_folder);
        super.a(com.alensw.PicFolder.R.layout.path_dlg);
        getWindow().addFlags(132096);
        Resources resources = context.getResources();
        this.h = resources.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_material_icon_size);
        this.j = resources.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_padding);
        this.i = this.h + (this.j * 2);
        this.e = SvgPicture.a(resources, com.alensw.PicFolder.R.raw.folder_2, GalleryChromeActivity.b(this.a, 16842808), this.h);
        this.f = (i & 1) != 0;
        this.g = (i & 2) != 0;
        file = (file == null || !file.exists()) ? Environment.getExternalStorageDirectory() : file;
        this.k = new ScannedFolder(file, 0);
        this.k.h = new ArrayList(128);
        File parentFile = this.k.getParentFile();
        this.l = parentFile != null ? new ScannedFolder(parentFile, 0) : null;
        this.c = (PathBar) findViewById(com.alensw.PicFolder.R.id.path);
        this.c.setPath(file);
        this.c.setOnClickListener(new OpenPickerBreadcrumb(this));
        this.d = (ListView) findViewById(com.alensw.PicFolder.R.id.list);
        this.d.setAdapter((ListAdapter) this.n);
        this.d.setScrollbarFadingEnabled(true);
        this.d.setOnItemClickListener(new OpenPickerFolder(this));
        super.setButton(-1, context.getString(17039370), new ConfirmFolderSelection(this, srcType0549));
        super.setButton(-2, context.getString(17039360), new DismissFolderPicker(this));
        if (this.g) {
            super.setButton(-3, context.getString(com.alensw.PicFolder.R.string.new_folder), new PromptNewFolderName(this, context, srcType0549));
        }
    }

   public static void a(Context var0, int var1, File var2, FolderSelectionCallback var3) {
      SharedPreferences var5 = var0.getSharedPreferences("com.alensw.PicFolder_preferences", 0);
      boolean var4;
      if (VERSION.SDK_INT >= 30) {
         var4 = var5.getBoolean("external_files", true);
      } else {
         var4 = var5.getBoolean("external_files", false);
      }

      if (var4 && var1 == 2) {
         setupSaf(var0);
      } else {
         new FolderPickerDialog(var0, var1, var2, var3).show();
      }
   }

   static void setupSaf(Context var0) {
      ((Activity)var0).startActivityForResult(new Intent("android.intent.action.OPEN_DOCUMENT_TREE"), 205452);
   }

   protected File a(String var1) {
      File var5 = this.c.getPath();
      File var4 = new File(var5, var1);
      int var2 = GalleryUtils.a(this.k.h, var4, m);
      File var3;
      if (var2 >= 0) {
         var3 = (File)this.k.h.get(var2);
      } else {
         var3 = var4;
         if (!var4.exists()) {
            var3 = var4;
            if (!GalleryUtils.a(var4)) {
               Context var6 = this.a;
               if (!(var6 instanceof GalleryIntentActivity) || !((GalleryIntentActivity)var6).a(var5.getPath())) {
                  Toast.makeText(var6, "Create folder '" + var1 + "' failed!", 1).show();
               }

               var3 = null;
            }
         }
      }

      return var3;
   }

   protected void a(File var1) {
      boolean var3 = false;
      this.k = new ScannedFolder(var1, 0);
      if (QuickApp.q.a(this.k, this.f) > 0) {
         GalleryUtils.a((List)this.k.h, m);
      }

      File var4 = var1.getParentFile();
      if (var4 != null) {
         this.l = new ScannedFolder(var4, 0);
         this.k.h.add(0, this.l);
      } else {
         this.l = null;
      }

      this.n.notifyDataSetChanged();
      if (this.g) {
         boolean var2;
         label21: {
            String var5 = var1.getPath();
            if (!var1.canWrite()) {
               var2 = var3;
               if (!QuickApp.p.b(var5)) {
                  break label21;
               }
            }

            var2 = var3;
            if (!QuickApp.p.g(var5)) {
               var2 = true;
            }
         }

         this.getButton(-1).setEnabled(var2);
         this.getButton(-3).setEnabled(var2);
      }
   }

   @Override
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.a(this.c.getPath());
   }
}
