package com.alensw.ui.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.Resources.Theme;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
@SuppressLint("Instantiatable")
public class TitleBar extends LinearLayout {
   private final int a;
   private final int b;
   private final int c;
   private final int d;
   private boolean e;
   private ImageView f;
   private HomeButton g;
   private TitleView h;
   private TextView i;
   private ProgressBar j;
   private ActionLayout k;
   private LinearLayout l;
   private com.alensw.b.g.b m;
   private com.alensw.ui.view.cb n;
   private final OnClickListener o = new com.alensw.ui.view.bv(this);
   private final OnLongClickListener p = new com.alensw.ui.view.bw(this);
   private final Runnable q = new com.alensw.ui.view.bz(this);

   public TitleBar(Context var1) {
      this(var1, null);
   }

   public TitleBar(Context var1, AttributeSet var2) {
      super(var1, var2);
      Resources var4 = this.getResources();
      Theme var3 = var1.getTheme();
      TypedValue var5 = new TypedValue();
      var3.resolveAttribute(com.alensw.PicFolder.R.attr.colorActionIcon, var5, true);
      this.a = var4.getColor(var5.resourceId);
      this.b = var4.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_width);
      this.c = var4.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_height);
      this.d = (int)(var4.getDisplayMetrics().density * 480.0F);
      TypedArray var6 = var1.obtainStyledAttributes(var2, new int[]{16842964}, 16842844, 0);
      this.setBackgroundDrawable(var6.getDrawable(0));
      var6.recycle();
   }

   static com.alensw.b.g.b a(TitleBar var0) {
      return var0.m;
   }

   static ActionLayout b(TitleBar var0) {
      return var0.k;
   }

   static boolean c(TitleBar var0) {
      return var0.e;
   }

   static LinearLayout d(TitleBar var0) {
      return var0.l;
   }

   static TitleView e(TitleBar var0) {
      return var0.h;
   }

   public View a() {
        this.l = new LinearLayout(this.getContext());
        this.l.setBackgroundDrawable(this.getBackground());
        this.l.setClickable(true);
        ((ViewGroup)this.getParent()).addView((View)this.l, -1, this.c);
        ViewGroup.LayoutParams layoutParams = this.l.getLayoutParams();
        if (layoutParams instanceof RelativeLayout.LayoutParams) {
            ((RelativeLayout.LayoutParams)layoutParams).addRule(12);
        } else {
            try {
                layoutParams.getClass().getDeclaredField("gravity").setInt(layoutParams, 80);
            }
            catch (Throwable throwable) {}
        }
        this.f();
        return this.l;
    }

   public View a(int var1) {
      View var3 = this.findViewById(var1);
      View var2 = var3;
      if (var3 == null) {
         var2 = this.k.findViewById(var1);
      }

      return var2;
   }

   public void a(Menu var1) {
      byte var3 = 0;

      for (int var2 = this.k.getChildCount() - 2; var2 >= 0; var2--) {
         View var5 = this.k.getChildAt(var2);
         MenuItem var6 = var1.findItem(var5.getId());
         if (var6 != null) {
            boolean var4;
            if (var5.getVisibility() != 0) {
               var4 = true;
            } else {
               var4 = false;
            }

            var6.setVisible(var4);
         }
      }

      ImageView var8 = this.f;
      byte var7;
      if (var1.hasVisibleItems()) {
         var7 = var3;
      } else {
         var7 = 8;
      }

      var8.setVisibility(var7);
   }

   public void a(MenuItem menuItem, int i) {
        int itemId = menuItem.getItemId();
        this.k.removeView(this.k.findViewById(itemId));
        if (i == 0) {
            return;
        }
        Context context = getContext();
        int i2 = this.b;
        ImageView imageView = (ImageView) View.inflate(context, com.alensw.PicFolder.R.layout.action_button, null);
        imageView.setId(itemId);
        imageView.setImageDrawable(menuItem.getIcon());
        imageView.setOnClickListener(this.o);
        imageView.setOnLongClickListener(this.p);
        imageView.setTag(Integer.valueOf(i));
        imageView.setVisibility(menuItem.isVisible() ? 0 : 8);
        this.k.addView(imageView, this.k.getChildCount() - 1, new android.widget.LinearLayout.LayoutParams(i2, this.c));
        f();
    }

   public void a(View var1) {
      if (this.d() && this.m.hasVisibleItems()) {
         if (var1 == null) {
            if (this.f.getVisibility() == 0) {
               var1 = this.f;
            } else {
               var1 = this.k;
            }
         }

         this.m.a((View)var1);
      }
   }

   public void a(View var1, ViewGroup.LayoutParams var2) {
      this.addView(var1, 2, var2);
      this.f();
   }

   public void a(boolean var1) {
      this.g.a(var1);
   }

   public void b(boolean var1) {
      this.g.b(var1);
   }

   public boolean b() {
      boolean var1;
      if (this.l != null && this.e) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public void c() {
      for (int var1 = this.k.getChildCount() - 2; var1 >= 0; var1--) {
         this.k.removeViewAt(var1);
      }

      if (this.m != null) {
         this.m.clear();
         this.m = null;
      }

      this.f();
   }

   public void c(boolean var1) {
      this.h.a(var1);
   }

   public boolean d() {
      boolean var1;
      boolean var2;
      Context var3;
      label27: {
         var2 = false;
         var3 = this.getContext();
         if (this.m == null) {
            this.m = new com.alensw.b.g.b(var3);
            if (this.n != null) {
               var1 = this.n.a(this, this.m);
               break label27;
            }

            if (var3 instanceof Activity) {
               var1 = ((Activity)var3).onCreateOptionsMenu(this.m);
               break label27;
            }

            this.m = null;
         }

         var1 = false;
      }

      if (this.m != null) {
         if (this.n != null) {
            var1 = this.n.b(this, this.m);
         } else {
            var1 = var2;
            if (var3 instanceof Activity) {
               var1 = ((Activity)var3).onPrepareOptionsMenu(this.m);
            }
         }
      }

      return var1;
   }

   protected void e() {
        int width = getWidth();
        this.e = this.l != null && width < this.d;
        int i = this.e ? width : width / 2;
        int childCount = this.k.getChildCount() - 1;
        int i2 = 0;
        int measuredWidth = i;
        while (i2 < childCount) {
            View childAt = this.k.getChildAt(i2);
            Object tag = childAt.getTag();
            i2++;
            measuredWidth = ((tag != null ? ((Integer) tag).intValue() : 0) & 2) != 0 ? measuredWidth - childAt.getMeasuredWidth() : measuredWidth;
        }
        if (this.f.getVisibility() != 8) {
            measuredWidth -= this.f.getMeasuredWidth();
        }
        int i3 = 0;
        while (i3 < childCount) {
            View childAt2 = this.k.getChildAt(i3);
            Object tag2 = childAt2.getTag();
            boolean z = ((tag2 != null ? ((Integer) tag2).intValue() : 0) & 2) != 0 || measuredWidth >= childAt2.getMeasuredWidth();
            childAt2.setVisibility(z ? 0 : 8);
            i3++;
            measuredWidth = z ? measuredWidth - childAt2.getMeasuredWidth() : measuredWidth;
        }
        ViewGroup viewGroup = (ViewGroup) this.k.getParent();
        LinearLayout linearLayout = this.e ? this.l : this;
        if (viewGroup != linearLayout) {
            if (viewGroup != null) {
                viewGroup.removeView(this.k);
            }
            linearLayout.addView(this.k, new android.widget.LinearLayout.LayoutParams(this.e ? -1 : -2, this.c));
        }
        if (this.l != null) {
            this.l.setVisibility(this.e ? getVisibility() : 8);
        }
    }

   protected void f() {
      this.post(this.q);
   }

   public int getButtonHeight() {
      return this.c;
   }

   public int getSplitBarHeight() {
      int var1;
      if (this.l != null && this.e) {
         var1 = Math.max(this.l.getHeight(), this.c);
      } else {
         var1 = 0;
      }

      return var1;
   }

   public int getTitleColor() {
      return this.a & 16777215 | 0xFF000000;
   }

   @Override
   protected void onFinishInflate() {
      super.onFinishInflate();
      Resources var2 = this.getResources();
      this.g = (HomeButton)this.findViewById(com.alensw.PicFolder.R.id.home);
      this.g.setId(16908332);
      this.g.a(this.a);
      int var1 = this.getTitleColor();
      this.h = (TitleView)this.findViewById(16908310);
      this.h.setTextColor(var1);
      this.i = (TextView)this.findViewById(com.alensw.PicFolder.R.id.subtitle);
      this.i.setTextColor(var1 & 16777215 | -1073741824);
      this.j = (ProgressBar)this.findViewById(com.alensw.PicFolder.R.id.circular);
      this.k = (ActionLayout)this.findViewById(com.alensw.PicFolder.R.id.container);
      this.f = (ImageView)this.k.findViewById(com.alensw.PicFolder.R.id.menu);
      this.f.setImageDrawable(com.b.a.b.a(var2, com.alensw.PicFolder.R.raw.ic_menu_overflow, this.a));
      this.f.setOnClickListener(new com.alensw.ui.view.bx(this));
   }

   @Override
   protected void onMeasure(int var1, int var2) {
      super.onMeasure(var1, MeasureSpec.makeMeasureSpec(this.c, 1073741824));
      this.setMeasuredDimension(this.getMeasuredWidth(), this.c);
   }

   @TargetApi(11)
   @Override
   public void onSizeChanged(int var1, int var2, int var3, int var4) {
      super.onSizeChanged(var1, var2, var3, var4);
      this.e();
      this.post(new com.alensw.ui.view.by(this));
   }

   @Override
   public void setAnimation(Animation var1) {
      super.setAnimation(var1);
      if (this.l != null && this.e) {
         this.l.setAnimation(var1);
      }
   }

   @Override
   public void setBackgroundDrawable(Drawable var1) {
      super.setBackgroundDrawable(var1);
      if (this.l != null) {
         this.l.setBackgroundDrawable(var1);
      }
   }

   public void setHomeButtonEnabled(boolean var1) {
      this.g.setEnabled(var1);
   }

   public void setMenuCallback(com.alensw.ui.view.cb var1) {
      this.n = var1;
   }

   public void setOnHomeClickListener(OnClickListener var1) {
      this.g.setOnClickListener(var1);
   }

   public void setOnTitleClickListener(OnClickListener var1) {
      ((View)this.h.getParent()).setOnClickListener(new com.alensw.ui.view.ca(this, var1));
   }

   public void setProgressVisible(boolean var1) {
      ProgressBar var3 = this.j;
      byte var2;
      if (var1) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      var3.setVisibility(var2);
   }

   public void setSubtitle(CharSequence var1) {
      this.i.setText(var1);
      TextView var3 = this.i;
      byte var2;
      if (var1 != null && var1.length() > 0) {
         var2 = 0;
      } else {
         var2 = 8;
      }

      var3.setVisibility(var2);
   }

   public void setTitle(CharSequence var1) {
      this.h.setText(var1);
   }

   @Override
   public void setVisibility(int var1) {
      super.setVisibility(var1);
      if (this.l != null && this.e) {
         this.l.setVisibility(var1);
      }
   }
}
