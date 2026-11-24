package com.alensw.ui.a;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.alensw.ui.view.TitleView;
import java.util.List;

public class f {
   public static AlertDialog a(Context var0, int var1, Object var2) {
      return a(var0, var1, var2, null);
   }

   public static AlertDialog a(Context context, int i, Object obj, OnClickListener dialogInterface$OnClickListener) {
        try {
            android.app.AlertDialog.Builder alertDialog$Builder = new android.app.AlertDialog.Builder(context);
            alertDialog$Builder.setIcon(0);
            if (i != 0) {
                alertDialog$Builder.setTitle(i);
            }
            if (obj instanceof CharSequence) {
                alertDialog$Builder.setMessage((CharSequence) obj);
            } else if (obj instanceof CharSequence[]) {
                alertDialog$Builder.setItems((CharSequence[]) obj, dialogInterface$OnClickListener);
            } else if (obj instanceof ListAdapter) {
                alertDialog$Builder.setAdapter((ListAdapter) obj, dialogInterface$OnClickListener);
            }
            AlertDialog alertDialogCreate = alertDialog$Builder.create();
            if (android.os.Build.VERSION.SDK_INT >= 21) {
                return alertDialogCreate;
            }
            alertDialogCreate.setOnShowListener(new com.alensw.ui.a.g(context, alertDialogCreate));
            return alertDialogCreate;
        } catch (Throwable th) {
            return null;
        }
    }

   public static void a(Context context, int i, int i2, List list, com.alensw.ui.a.s srcType0572) {
        View view;
        com.alensw.ui.a.u srcType0574A = com.alensw.ui.a.u.a(context, i, 0);
        if (srcType0574A == null) {
            return;
        }
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_padding);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        srcType0574A.setView(linearLayout);
        if (i2 != 0) {
            TextView textView = new TextView(context);
            textView.setText(i2);
            textView.setTextSize(18.0f);
            linearLayout.addView(textView);
        }
        com.alensw.ui.a.o srcType0568 = new com.alensw.ui.a.o(context, 17367048, list);
        srcType0568.setDropDownViewResource(17367049);
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            Spinner spinner = new Spinner(context);
            spinner.setAdapter((SpinnerAdapter) srcType0568);
            view = spinner;
        } else {
            TitleView titleView = (TitleView) View.inflate(context, com.alensw.PicFolder.R.layout.action_text, null);
            titleView.a(true);
            if (list.size() > 0) {
                titleView.setText((CharSequence) list.get(0));
            }
            titleView.setOnClickListener(new com.alensw.ui.a.p(context, i, srcType0568, titleView));
            view = titleView;
        }
        linearLayout.addView(view);
        srcType0574A.setButton(-1, context.getString(17039370), new com.alensw.ui.a.r(view, srcType0572));
        srcType0574A.setButton(-2, context.getString(17039360), new com.alensw.ui.a.h());
        a(srcType0574A);
    }

   public static void a(Context var0, int var1, CharSequence var2) {
      AlertDialog var3 = a(var0, var1, (Object)var2);
      if (var3 != null) {
         var3.setButton(-1, var0.getString(17039370), new com.alensw.ui.a.j());
         a(var3);
      }
   }

   public static void a(Context var0, int var1, CharSequence var2, int var3, int var4, com.alensw.ui.a.t var5) {
      com.alensw.ui.a.u var6 = com.alensw.ui.a.u.a(var0, var1, com.alensw.PicFolder.R.layout.size_dlg);
      if (var6 != null) {
         if (var2 != null) {
            ((TextView)var6.findViewById(com.alensw.PicFolder.R.id.middle)).setText(var2);
         }

         EditText var8 = (EditText)var6.findViewById(com.alensw.PicFolder.R.id.width);
         EditText var7 = (EditText)var6.findViewById(com.alensw.PicFolder.R.id.height);
         var8.setText(Integer.toString(var3));
         var7.setText(Integer.toString(var4));
         com.alensw.ui.a.i var9 = new com.alensw.ui.a.i(var8, var7, var5);
         var6.setButton(-1, var0.getString(17039370), var9);
         var6.setButton(-2, var0.getString(17039360), var9);
         var6.getWindow().setSoftInputMode(5);
         a(var6);
      }
   }

   public static void a(Context var0, int var1, CharSequence var2, Runnable var3, Runnable var4) {
      AlertDialog var5 = a(var0, var1, (Object)var2);
      if (var5 != null) {
         var5.setButton(-1, var0.getString(17039379), new com.alensw.ui.a.k(var3));
         var5.setButton(-2, var0.getString(17039369), new com.alensw.ui.a.l(var4));
         var5.setOnCancelListener(new com.alensw.ui.a.m(var4));
         a(var5);
      }
   }

   public static void a(Context context, int i, Object obj, CharSequence charSequence, com.alensw.ui.a.s srcType0572) {
        com.alensw.ui.a.u srcType0574A = com.alensw.ui.a.u.a(context, i, 0);
        if (srcType0574A == null) {
            return;
        }
        int dimensionPixelSize = context.getResources().getDimensionPixelSize(com.alensw.PicFolder.R.dimen.button_padding);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(1);
        linearLayout.setPadding(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        srcType0574A.setView(linearLayout);
        EditText editText = new EditText(context);
        editText.setFocusable(true);
        editText.setSingleLine(true);
        if (obj != null) {
            if (obj instanceof Integer) {
                editText.setInputType(2);
            }
            editText.setText(obj.toString());
            editText.selectAll();
        }
        linearLayout.addView(editText);
        if (charSequence != null) {
            TextView textView = new TextView(context);
            textView.setText(charSequence);
            textView.setTextSize(18.0f);
            linearLayout.addView(textView);
        }
        android.content.DialogInterface.OnClickListener srcType0567 = new com.alensw.ui.a.n(srcType0572, editText);
        srcType0574A.setButton(-1, context.getString(17039370), srcType0567);
        srcType0574A.setButton(-2, context.getString(17039360), srcType0567);
        srcType0574A.getWindow().setSoftInputMode(5);
        a(srcType0574A);
    }

   public static boolean a(Dialog var0) {
      try {
         var0.show();
      } catch (Throwable var3) {
         if (var0 != null) {
            com.alensw.b.l.b.a(var0.getContext(), var3);
         }

         return false;
      }

      return true;
   }

   public static boolean b(Dialog var0) {
      try {
         var0.dismiss();
      } catch (Throwable var2) {
         return false;
      }

      return true;
   }
}
