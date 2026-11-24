package com.alensw.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

class bj implements OnItemClickListener {
   final SelectThemeActivity a;

   bj(SelectThemeActivity var1) {
      this.a = var1;
   }

   @Override
   public void onItemClick(AdapterView adapterView, View view, int i, long j) {
        com.alensw.ui.activity.bl srcType0621 = (com.alensw.ui.activity.bl) SelectThemeActivity.c(this.a).get(i);
        boolean z = (srcType0621.a == this.a.T && srcType0621.c == this.a.V) ? false : true;
        if (z) {
            android.content.SharedPreferences.Editor sharedPreferences$EditorEdit = PreferenceManager.getDefaultSharedPreferences(this.a).edit();
            sharedPreferences$EditorEdit.putInt("main_theme", srcType0621.a);
            sharedPreferences$EditorEdit.putInt("actionbar_color", srcType0621.c);
            sharedPreferences$EditorEdit.commit();
        }
        this.a.setResult(z ? -1 : 0, new Intent());
        this.a.finish();
    }
}
