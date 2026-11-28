package com.alensw.ui.activity;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.alensw.PicFolder.QuickApp;
import com.alensw.gallery.FolderScanFilters;
import com.alensw.gallery.GalleryMediaStore;
import com.alensw.sort.NameSortOrder;
import com.alensw.svg.SvgPicture;
import com.alensw.ui.dialog.FolderPickerDialog;
import com.alensw.ui.menu.GalleryMenuItem;
import com.alensw.utils.GalleryUtils;
import java.util.ArrayList;
import java.util.List;

public class PathListActivity extends GalleryChromeActivity {
   protected ArrayList a;
   protected BaseAdapter b;
   protected ListView c;
   protected boolean d;
   protected boolean e;
   protected final OnClickListener f = new RemovePathClick(this);

   public static Intent a(Context var0, boolean var1) {
      Intent var2 = new Intent(var0, PathListActivity.class);
      var2.putExtra("exclude_mode", var1);
      return var2;
   }

   protected void b() {
      byte var1;
      if (this.e) {
         var1 = 0;
      } else {
         var1 = 1;
      }

      FolderPickerDialog.a(this, var1, null, new AddSelectedPath(this));
   }

   @TargetApi(14)
   @Override
   public void onCreate(Bundle var1) {
      super.onCreate(var1);
      this.c = new ListView(this);
      this.setContentView(this.c);
      this.g(true);
      this.e = this.getIntent().getBooleanExtra("exclude_mode", true);
      int var2;
      if (this.e) {
         var2 = com.alensw.PicFolder.R.string.excluded_folders;
      } else {
         var2 = com.alensw.PicFolder.R.string.included_folders;
      }

      this.setTitle(var2);
      this.a = FolderScanFilters.a(this).a(this.e);
      GalleryUtils.a((List)this.a, NameSortOrder.a);
      this.b = new PathListAdapter(this, this.getLayoutInflater(), SvgPicture.a(this.getResources(), com.alensw.PicFolder.R.raw.ic_menu_cancel, GalleryChromeActivity.b(this, com.alensw.PicFolder.R.attr.colorMenuDrawable)));
      this.c.setAdapter((ListAdapter)this.b);
   }

   @TargetApi(11)
   @Override
   public boolean onCreateOptionsMenu(Menu var1) {
      MenuItem var2 = var1.add(0, com.alensw.PicFolder.R.id.add, 0, com.alensw.PicFolder.R.string.add);
      GalleryMenuItem.a(this, var2);
      this.a(var2, 2);
      return true;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem var1) {
      boolean var2;
      switch (var1.getItemId()) {
         case com.alensw.PicFolder.R.id.add:
            this.b();
            var2 = true;
            break;
         default:
            var2 = super.onOptionsItemSelected(var1);
      }

      return var2;
   }

   @Override
   public void onStop() {
      super.onStop();
      if (this.d) {
         FolderScanFilters.a(this).a(this.a, this.e);
         this.getContentResolver().notifyChange(GalleryMediaStore.a('I'), null);
         QuickApp.a(true);
         QuickApp.q.a(4, null, null);
         this.d = false;
      }
   }
}
