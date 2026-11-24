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
import java.util.ArrayList;
import java.util.List;

public class PathListActivity extends com.alensw.ui.activity.ao {
   protected ArrayList a;
   protected BaseAdapter b;
   protected ListView c;
   protected boolean d;
   protected boolean e;
   protected final OnClickListener f = new com.alensw.ui.activity.bf(this);

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

      com.alensw.ui.a.ae.a(this, var1, null, new com.alensw.ui.activity.bh(this));
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
      this.a = com.alensw.a.o.a(this).a(this.e);
      com.alensw.b.l.b.a((List)this.a, com.alensw.b.b.b.a);
      this.b = new com.alensw.ui.activity.bg(this, this.getLayoutInflater(), com.b.a.b.a(this.getResources(), com.alensw.PicFolder.R.raw.ic_menu_cancel, com.alensw.ui.activity.ao.b(this, com.alensw.PicFolder.R.attr.colorMenuDrawable)));
      this.c.setAdapter((ListAdapter)this.b);
   }

   @TargetApi(11)
   @Override
   public boolean onCreateOptionsMenu(Menu var1) {
      MenuItem var2 = var1.add(0, com.alensw.PicFolder.R.id.add, 0, com.alensw.PicFolder.R.string.add);
      com.alensw.b.g.l.a(this, var2);
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
         com.alensw.a.o.a(this).a(this.a, this.e);
         this.getContentResolver().notifyChange(com.alensw.a.ba.a('I'), null);
         QuickApp.a(true);
         QuickApp.q.a(4, null, null);
         this.d = false;
      }
   }
}
