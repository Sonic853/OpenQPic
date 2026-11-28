package com.alensw.ui.menu;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.alensw.svg.SvgDrawable;
import com.alensw.svg.SvgPicture;
import java.util.ArrayList;

public class MenuListAdapter extends BaseAdapter {
   public static final int[] i = new int[]{16842912};
   public static final int[] j = new int[0];
   protected final int a;
   protected final int b;
   protected final int c;
   protected final boolean d;
   protected final Context e;
   protected boolean f;
   protected int g;
   protected ArrayList h;

   public MenuListAdapter(Context var1, int var2) {
      super();
      boolean var4 = false;
      this.g = 0;
      Resources var5 = var1.getResources();
      this.e = var1;
      this.a = var5.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.check_box_size);
      this.b = var2;
      this.c = var5.getColor(com.alensw.PicFolder.R.color.material_warning);
      boolean var3 = var4;
      if (VERSION.SDK_INT >= 17) {
         var3 = var4;
         if ((var5.getConfiguration().screenLayout & 192) == 128) {
            var3 = true;
         }
      }

      this.d = var3;
   }

   public static Drawable a(Resources var0, int var1, int var2, boolean var3) {
      StateListDrawable var5 = new StateListDrawable();
      int var4;
      if (var3) {
         var4 = com.alensw.PicFolder.R.raw.ic_button_radio_on;
      } else {
         var4 = com.alensw.PicFolder.R.raw.ic_button_check_on;
      }

      SvgDrawable var6 = SvgPicture.a(var0, var4, var2, var1);
      if (var3) {
         var4 = com.alensw.PicFolder.R.raw.ic_button_radio_off;
      } else {
         var4 = com.alensw.PicFolder.R.raw.ic_button_check_off;
      }

      SvgDrawable var7 = SvgPicture.a(var0, var4, var2, var1);
      var5.addState(i, var6);
      var5.addState(j, var7);
      return var5;
   }

   public static int[] a(ListAdapter var0) {
      int var6 = MeasureSpec.makeMeasureSpec(0, 0);
      int var4 = MeasureSpec.makeMeasureSpec(0, 0);
      int var5 = var0.getCount();
      int var2 = 0;
      View var7 = null;
      int var3 = 0;

      int var1;
      for (var1 = 0; var2 < var5; var2++) {
         var7 = var0.getView(var2, var7, null);
         var7.measure(var6, var4);
         var1 = Math.max(var7.getMeasuredWidth(), var1);
         var3 += var7.getMeasuredHeight();
      }

      return new int[]{var1, var3};
   }

   public void a(GalleryMenu var1) {
      int var3 = var1.size();
      if (this.h == null) {
         this.h = new ArrayList(var3);
      } else {
         this.h.clear();
      }

      for (int var2 = 0; var2 < var3; var2++) {
         GalleryMenuItem var4 = (GalleryMenuItem)var1.getItem(var2);
         if (var4.isVisible()) {
            this.h.add(var4);
         }
      }

      this.f = var1.a();
   }

   public int[] a() {
      Resources var3 = this.e.getResources();
      int var1 = var3.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_min_width);
      int var2 = var3.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.menu_max_width);
      int[] var4 = a(this);
      var4[0] = Math.min(Math.max(var4[0], var1), var2);
      return var4;
   }

   @Override
   public int getCount() {
      return this.h.size();
   }

   @Override
   public Object getItem(int var1) {
      return this.h.get(var1);
   }

   @Override
   public long getItemId(int var1) {
      return ((GalleryMenuItem)this.h.get(var1)).getItemId();
   }

   @Override
   public View getView(int var1, View var2, ViewGroup var3) {
        LinearLayout linearLayout;
        if (var2 == null) {
            LinearLayout linearLayout2 = (LinearLayout) View.inflate(this.e, com.alensw.PicFolder.R.layout.menu_item, null);
            if (this.d) {
                int childCount = linearLayout2.getChildCount();
                for (int i3 = 0; i3 < childCount / 2; i3++) {
                    linearLayout2.bringChildToFront(linearLayout2.getChildAt(i3));
                }
                ((TextView) linearLayout2.findViewById(com.alensw.PicFolder.R.id.title)).setGravity(21);
            }
            if (this.g == 0) {
                this.g = ((TextView) linearLayout2.findViewById(com.alensw.PicFolder.R.id.title)).getTextColors().getDefaultColor();
                linearLayout = linearLayout2;
            } else {
                linearLayout = linearLayout2;
            }
        } else {
            linearLayout = (LinearLayout) var2;
        }
        ImageView imageView = (ImageView) linearLayout.findViewById(com.alensw.PicFolder.R.id.icon);
        TextView textView = (TextView) linearLayout.findViewById(com.alensw.PicFolder.R.id.title);
        CompoundButton compoundButton = (CompoundButton) linearLayout.findViewById(com.alensw.PicFolder.R.id.check);
        GalleryMenuItem srcType0191 = (GalleryMenuItem) this.h.get(var1);
        boolean zIsEnabled = srcType0191.isEnabled();
        imageView.setVisibility(this.f ? 0 : 8);
        if (this.f) {
            imageView.setImageDrawable(srcType0191.getIcon());
        }
        srcType0191.a();
        int iB = srcType0191.b();
        if ((iB & 32) != 0) {
            textView.setTextColor(this.c);
        } else if (zIsEnabled) {
            textView.setTextColor(this.g);
        } else {
            textView.setTextColor((this.g & 16777215) | Integer.MIN_VALUE);
        }
        textView.setText(srcType0191.getTitle());
        if (srcType0191.isCheckable()) {
            boolean zIsChecked = srcType0191.isChecked();
            boolean z = (iB & 16) != 0;
            compoundButton.setVisibility(0);
            compoundButton.setChecked(zIsChecked);
            compoundButton.setButtonDrawable(a(this.e.getResources(), this.a, this.b, z));
        } else {
            compoundButton.setVisibility(8);
        }
        return linearLayout;
    }
}
