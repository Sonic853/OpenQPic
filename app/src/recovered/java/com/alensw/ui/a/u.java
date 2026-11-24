package com.alensw.ui.a;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;
public class u extends AlertDialog {
   protected final Context a;
   protected View b;

   protected u(Context var1) {
      super(var1, a(var1));
      super.setIcon(0);
      this.a = var1;
   }

   public static int a(Context context) {
        if (android.os.Build.VERSION.SDK_INT < 11) {
            int i = PreferenceManager.getDefaultSharedPreferences(context).getInt("main_theme", 0);
            return i == 1 || i == 3 || i == -2 ? com.alensw.PicFolder.R.style.Theme_Dialog_Light : com.alensw.PicFolder.R.style.Theme_Dialog_Dark;
        }
        TypedValue typedValue = new TypedValue();
        context.getTheme().resolveAttribute(16843529, typedValue, true);
        return typedValue.resourceId;
    }

   public static com.alensw.ui.a.u a(Context var0, int var1, int var2) {
      com.alensw.ui.a.u var3 = new com.alensw.ui.a.u(var0);
      if (var1 != 0) {
         var3.setTitle(var1);
      }

      if (var2 != 0) {
         var3.a(var2);
      }

      return var3;
   }

   public static void a(Context context, Dialog dialog) {
        android.widget.ListView listView;
        if (android.os.Build.VERSION.SDK_INT < 21) {
            Resources resources = context.getResources();
            int iB = com.alensw.ui.activity.ao.b(context, 16842806);
            int dimensionPixelSize = resources.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_padding);
            int dimensionPixelSize2 = resources.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_height);
            try {
                Window window = dialog.getWindow();
                int identifier = resources.getIdentifier("alertTitle", "id", "android");
                View viewFindViewById = identifier != 0 ? window.findViewById(identifier) : null;
                if (viewFindViewById instanceof TextView) {
                    ((TextView) viewFindViewById).setTextColor(iB);
                }
                int identifier2 = resources.getIdentifier("titleDivider", "id", "android");
                View viewFindViewById2 = identifier2 != 0 ? window.findViewById(identifier2) : null;
                if (viewFindViewById2 != null) {
                    viewFindViewById2.setVisibility(4);
                }
                if ((dialog instanceof AlertDialog) && (listView = ((AlertDialog) dialog).getListView()) != null) {
                    listView.setDivider(null);
                }
                ViewGroup viewGroup = (ViewGroup) window.findViewById(resources.getIdentifier("buttonPanel", "id", "android"));
                LinearLayout linearLayout = (LinearLayout) viewGroup.getChildAt(0);
                if (android.os.Build.VERSION.SDK_INT >= 11) {
                    linearLayout.setPadding((dimensionPixelSize * 3) / 2, dimensionPixelSize, (dimensionPixelSize * 3) / 2, dimensionPixelSize);
                    linearLayout.setShowDividers(0);
                    if (viewGroup instanceof LinearLayout) {
                        ((LinearLayout) viewGroup).setShowDividers(0);
                    }
                } else {
                    dialog.getWindow().setBackgroundDrawable(com.alensw.ui.d.d.a(com.alensw.ui.activity.ao.b(context, com.alensw.PicFolder.R.attr.colorDialog), (dimensionPixelSize * 3) / 4));
                    viewGroup.setMinimumHeight(dimensionPixelSize2);
                    ViewGroup viewGroup2 = (ViewGroup) viewGroup.getParent();
                    viewGroup2.setPadding(0, 0, 0, 0);
                    for (int childCount = viewGroup2.getChildCount() - 1; childCount >= 0; childCount--) {
                        viewGroup2.getChildAt(childCount).setBackgroundDrawable(null);
                    }
                    View viewFindViewById3 = window.findViewById(16908294);
                    if (viewFindViewById3 != null) {
                        viewFindViewById3.setVisibility(8);
                    }
                    a((ViewGroup) window.getDecorView(), iB);
                }
                int dimensionPixelSize3 = resources.getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_width) + (dimensionPixelSize * 2);
                for (int childCount2 = linearLayout.getChildCount() - 1; childCount2 >= 0; childCount2--) {
                    View childAt = linearLayout.getChildAt(childCount2);
                    android.widget.LinearLayout.LayoutParams linearLayout$LayoutParams = (android.widget.LinearLayout.LayoutParams) childAt.getLayoutParams();
                    linearLayout$LayoutParams.width = -2;
                    linearLayout$LayoutParams.height = -2;
                    linearLayout$LayoutParams.weight = 0.0f;
                    if (childAt instanceof Button) {
                        childAt.setMinimumWidth(dimensionPixelSize3);
                        childAt.setMinimumHeight(dimensionPixelSize2);
                        if (android.os.Build.VERSION.SDK_INT < 11) {
                            childAt.setBackgroundResource(com.alensw.PicFolder.R.drawable.item_background);
                        }
                    }
                }
                View viewFindViewById4 = linearLayout.findViewById(16908315);
                if (viewFindViewById4 != null) {
                    android.widget.LinearLayout.LayoutParams linearLayout$LayoutParams2 = (android.widget.LinearLayout.LayoutParams) viewFindViewById4.getLayoutParams();
                    linearLayout.removeView(viewFindViewById4);
                    linearLayout.addView(viewFindViewById4, 0, linearLayout$LayoutParams2);
                    linearLayout.addView(new View(context), 1, new android.widget.LinearLayout.LayoutParams(1, 1, 1.0f));
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

   public static void a(ViewGroup var0, int var1) {
      for (int var2 = var0.getChildCount() - 1; var2 >= 0; var2--) {
         View var3 = var0.getChildAt(var2);
         if (var3 instanceof ViewGroup) {
            a((ViewGroup)var3, var1);
         } else if (var3 instanceof EditText) {
            a((EditText)var3);
         } else if (var3 instanceof TextView) {
            ((TextView)var3).setTextColor(var1);
         }
      }
   }

   public static void a(EditText var0) {
      var0.setOnLongClickListener(new com.alensw.ui.a.v());
   }

   public void a(int var1) {
      this.setView(View.inflate(this.a, var1, null));
   }

   @Override
   public View findViewById(int var1) {
      if (this.b != null) {
         View var2 = this.b.findViewById(var1);
         if (var2 != null) {
            return var2;
         }
      }

      return super.findViewById(var1);
   }

   @Override
   protected void onCreate(Bundle var1) {
      super.onCreate(var1);
      if (VERSION.SDK_INT < 21) {
         a(this.a, this);
      }
   }

   @Override
   public void setView(View var1) {
      this.b = var1;
      super.setView(this.b);
   }
}
