package com.alensw.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;

public class PasswordView extends LinearLayout {
   public static final char[] a = new char[]{'1', '2', '3', '4', '5', '6', '7', '8', '9', '✓', '0', '←'};
   private boolean b;
   private TextView c;
   private com.alensw.ui.view.ay d;

   public PasswordView(Context var1) {
      this(var1, null);
   }

   public PasswordView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   static com.alensw.ui.view.ay a(PasswordView var0) {
      return var0.d;
   }

   static TextView b(PasswordView var0) {
      return var0.c;
   }

   @Override
   protected void onFinishInflate() {
      super.onFinishInflate();
      this.c = (TextView)this.findViewById(com.alensw.PicFolder.R.id.pin_code);
   }

   public void setInputListener(com.alensw.ui.view.ay var1) {
      this.d = var1;
   }

   public void setPatternMode(boolean var1) {
      byte var3 = 8;
      this.b = var1;
      LockPatternView var6 = (LockPatternView)this.findViewById(com.alensw.PicFolder.R.id.pattern);
      GridView var4 = (GridView)this.findViewById(com.alensw.PicFolder.R.id.grid);
      byte var2;
      if (this.b) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      var6.setVisibility(var2);
      if (this.b) {
         var2 = 8;
      } else {
         var2 = 0;
      }

      var4.setVisibility(var2);
      TextView var5 = this.c;
      if (this.b) {
         var2 = var3;
      } else {
         var2 = 0;
      }

      var5.setVisibility(var2);
      if (this.b) {
         var6.setOnPatternListener(new com.alensw.ui.view.av(this, var6));
      } else {
         var4.setAdapter((ListAdapter)(new com.alensw.ui.view.ax(this, new com.alensw.ui.view.aw(this))));
      }
   }
}
