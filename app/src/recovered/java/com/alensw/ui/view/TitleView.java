package com.alensw.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class TitleView extends TextView {
   private boolean a;
   private int b;
   private Path c;

   public TitleView(Context var1) {
      this(var1, null);
   }

   public TitleView(Context var1, AttributeSet var2) {
      super(var1, var2);
   }

   public void a(boolean var1) {
      this.a = var1;
      if (this.a) {
         this.b = (int)(this.getResources().getDisplayMetrics().density * 12.0F);
      }

      this.requestLayout();
      this.invalidate();
   }

   @Override
   protected void onDraw(Canvas var1) {
      super.onDraw(var1);
      if (this.a) {
         int var4 = this.getWidth();
         int var3 = this.getHeight();
         int var5 = this.getPaddingRight();
         int var2 = this.getPaddingBottom();
         TextPaint var6 = this.getPaint();
         if (this.c == null) {
            this.c = new Path();
         } else {
            this.c.rewind();
         }

         this.c.moveTo(var4 - var5, var3 - var2);
         this.c.lineTo(var4 - var5 - this.b, var3 - var2);
         this.c.lineTo(var4 - var5, var3 - var2 - this.b);
         this.c.close();
         var2 = var6.getAlpha();
         var6.setAlpha(192);
         var1.drawPath(this.c, var6);
         var6.setAlpha(var2);
      }
   }

   @Override
   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, var2);
      LayoutParams var3 = this.getLayoutParams();
      if (this.a && var3 != null && var3.width == -2) {
         var1 = this.getMeasuredHeight();
         this.setMeasuredDimension(this.getMeasuredWidth() + this.b, var1);
      }
   }
}
