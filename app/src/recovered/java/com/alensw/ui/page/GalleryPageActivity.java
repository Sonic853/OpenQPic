package com.alensw.ui.page;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Build.VERSION;
import android.provider.Settings.System;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.widget.Toast;
import com.alensw.ui.activity.GalleryIntentActivity;
import com.alensw.ui.view.BoundsTransition;
import com.alensw.ui.view.ContainerLayout;
import com.alensw.ui.view.ViscousFluidInterpolator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

@SuppressLint("Registered")
public class GalleryPageActivity extends GalleryIntentActivity {
   // Older versions wrote these names into the task's Bundle. Keep them as stable page IDs.
   private static final Map<String, Class<? extends GalleryPage>> SAVED_PAGES = new HashMap<>();
   static {
      SAVED_PAGES.put("com.alensw.ui.c.bz", AlbumsPage.class);
      SAVED_PAGES.put("com.alensw.ui.c.bf", LocalFolderPage.class);
      SAVED_PAGES.put("com.alensw.ui.c.dp", LocalViewerPage.class);
      SAVED_PAGES.put("com.alensw.ui.c.z", MediaFolderPage.class);
      SAVED_PAGES.put("com.alensw.ui.c.as", MediaViewerPage.class);
      SAVED_PAGES.put("com.alensw.ui.c.dc", GallerySearchPage.class);
   }

   private static String savedPageKey(Class<? extends GalleryPage> page) {
      for (Map.Entry<String, Class<? extends GalleryPage>> entry : SAVED_PAGES.entrySet()) {
         if (entry.getValue() == page) return entry.getKey();
      }
      return page.getName();
   }

   protected int a = 0;
   protected int b = 400;
   protected float c = 1.0F;
   protected final Rect d = new Rect();
   private ContainerLayout e;
   private final Stack f = new Stack();

   static ContainerLayout a(GalleryPageActivity var0) {
      return var0.e;
   }

   static Stack b(GalleryPageActivity var0) {
      return var0.f;
   }

   public Animation a(boolean var1, boolean var2, RectF var3, RectF var4) {
      BoundsTransition var5 = new BoundsTransition(var1, var2, var3, var4);
      var5.setDuration(this.b(var1));
      var5.setInterpolator(this.j(var1));
      return var5;
   }

   public GalleryPage a(Class cls, Intent intent, Bundle bundle) {
        try {
            GalleryPage srcType0722 = (GalleryPage) cls.newInstance();
            srcType0722.C = this;
            srcType0722.E = intent;
            srcType0722.e(1);
            srcType0722.a(bundle);
            b(srcType0722);
            return srcType0722;
        } catch (Throwable th) {
            th.printStackTrace();
            Toast.makeText(this, "create failed: " + th, 1).show();
            return null;
        }
    }

   public void a(Rect var1) {
      super.a(this.d, var1);
   }

   @TargetApi(11)
   protected void a(GalleryPage var1) {
      boolean var4 = true;
      if (var1 != null) {
         this.setTitle(var1.F);
         this.a(var1.G);
         this.i(var1.J);
      }

      if (this.a >= 2) {
         this.invalidateOptionsMenu();
      }

      int var2 = this.d();
      boolean var3;
      if (var2 > 1) {
         var3 = true;
      } else {
         var3 = false;
      }

      this.g(var3);
      if (var2 > 1) {
         var3 = var4;
      } else {
         var3 = false;
      }

      this.h(var3);
   }

   public boolean a(GalleryPage srcType0722, boolean z) {
        RectF rectFH;
        GalleryPage srcType0722C = c();
        if (srcType0722 == null) {
            srcType0722 = srcType0722C;
        }
        if (srcType0722 == null || !this.f.remove(srcType0722)) {
            return false;
        }
        boolean z2 = srcType0722C == srcType0722;
        GalleryPage srcType0722C2 = c();
        srcType0722.I = true;
        srcType0722.e(1);
        if (z2) {
            a(srcType0722C2);
        }
        if (srcType0722C2 != null) {
            srcType0722C2.b(srcType0722.E);
            srcType0722C2.e(2);
            srcType0722C2.D.setVisibility(0);
            if (z) {
                rectFH = srcType0722C2.h();
            } else {
                rectFH = null;
            }
        } else {
            rectFH = null;
        }
        long jA = rectFH != null ? srcType0722.a(false, rectFH) : 0L;
        if (srcType0722C2 != null && jA > 0 && G) {
            AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
            alphaAnimation.setDuration(jA);
            srcType0722C2.D.startAnimation(alphaAnimation);
        }
        this.e.postDelayed(new FinishPageRemoval(this, srcType0722, srcType0722C2), Math.max(jA, 0L));
        return true;
    }

   protected ViewGroup a_() {
      ContainerLayout var1 = new ContainerLayout(this);
      this.setContentView(var1);
      return var1;
   }

   public long b(boolean var1) {
      float var4 = this.b;
      float var3 = this.c;
      float var2;
      if (var1) {
         var2 = 1.0F;
      } else {
         var2 = 0.8F;
      }

      return (long)(var2 * (var3 * var4));
   }

   public void b(GalleryPage var1) {
      RectF var5 = null;
      GalleryPage var6 = this.c();
      int var2;
      if (var6 != null) {
         var2 = this.e.indexOfChild(var6.D) + 1;
         var5 = var6.h();
         var6.e(1);
         var6.D.setVisibility(4);
      } else {
         var2 = 0;
      }

      long var3;
      label30: {
         this.f.push(var1);
         this.e.addView(var1.D, var2);
         var1.D.setVisibility(0);
         this.a(var1);
         if (this.a == 3) {
            var1.e(2);
            if (var6 != null && var5 != null) {
               var3 = var1.a(true, var5);
               break label30;
            }
         }

         var3 = 0L;
      }

      if (var6 != null) {
         if (var3 > 0L && G) {
            AlphaAnimation var7 = new AlphaAnimation(1.0F, 0.0F);
            var7.setDuration(3L * var3 / 4L);
            var6.D.startAnimation(var7);
         }

         if (var5 != null) {
            FinishPageEntry var8 = new FinishPageEntry(this, var6);
            this.e.postDelayed(var8, Math.max(var3, 0L));
         }
      }
   }

   public GalleryPage c() {
      GalleryPage var1;
      if (this.f.size() > 0) {
         var1 = (GalleryPage)this.f.peek();
      } else {
         var1 = null;
      }

      return var1;
   }

   @Override
   protected boolean c(Menu var1) {
      GalleryPage var3 = this.c();
      boolean var2;
      if (var3 != null) {
         var2 = var3.c(var1);
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean c(GalleryPage var1) {
      return this.a(var1, true);
   }

   public int d() {
      return this.f.size();
   }

   @Override
   protected boolean d(Menu var1) {
      GalleryPage var3 = this.c();
      boolean var2;
      if (var3 != null) {
         var2 = var3.d(var1);
      } else {
         var2 = false;
      }

      return var2;
   }

   @Override
   public boolean dispatchKeyEvent(KeyEvent var1) {
      GalleryPage var3 = this.c();
      boolean var2;
      if (var3 != null && var3.a(var1, false)) {
         var2 = true;
      } else {
         var2 = super.dispatchKeyEvent(var1);
      }

      return var2;
   }

   @TargetApi(11)
   @Override
   public boolean dispatchKeyShortcutEvent(KeyEvent var1) {
      boolean var2 = true;
      GalleryPage var3 = this.c();
      if (var3 == null || !var3.a(var1, true)) {
         var2 = super.dispatchKeyShortcutEvent(var1);
      }

      return var2;
   }

   @Override
   public boolean dispatchTouchEvent(MotionEvent var1) {
      GalleryPage var3 = this.c();
      boolean var2;
      if (var3 != null && var3.a(var1)) {
         var2 = true;
      } else {
         var2 = super.dispatchTouchEvent(var1);
      }

      return var2;
   }

   protected void e() {
        for (int size = this.f.size() - 1; size >= 0; size--) {
            a((GalleryPage) null, false);
        }
    }

   public Uri[] f() {
      GalleryPage var1 = this.c();
      Uri[] var2;
      if (var1 != null) {
         var2 = var1.j();
      } else {
         var2 = null;
      }

      return var2;
   }

   public Interpolator j(boolean var1) {
      return new ViscousFluidInterpolator();
   }

   @Override
   public void onActivityResult(int var1, int var2, Intent var3) {
      super.onActivityResult(var1, var2, var3);
      GalleryPage var4 = this.c();
      if (var4 != null) {
         var4.a(var1, var2, var3);
      }
   }

   @Override
   public void onBackPressed() {
        GalleryPage srcType0722C = c();
        if (srcType0722C == null || srcType0722C.f()) {
            if (d() <= 1 || q()) {
                super.onBackPressed();
            } else {
                c((GalleryPage) null);
            }
        }
    }

   @TargetApi(16)
   @Override
   public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.a = 1;
        this.b = Math.max(getResources().getInteger(17694721), 400);
        this.c = android.provider.Settings.System.getFloat(getContentResolver(), android.os.Build.VERSION.SDK_INT >= 17 ? "transition_animation_scale" : "transition_animation_scale", 1.0f);
        ViewGroup viewGroupA_ = a_();
        if (viewGroupA_ instanceof ContainerLayout) {
            this.e = (ContainerLayout) viewGroupA_;
        } else {
            this.e = (ContainerLayout) viewGroupA_.getChildAt(0);
        }
        this.e.setOnInsetsListener(new PageInsetsListener(this));
        if (android.os.Build.VERSION.SDK_INT < 11 && bundle == null) {
            bundle = (Bundle) getIntent().getParcelableExtra("activity_extras");
        }
        if (bundle != null) {
            ClassLoader classLoader = getClassLoader();
            int i = bundle.getInt("iteraction_count");
            for (int i2 = 0; i2 < i; i2++) {
                try {
                     String key = bundle.getString("iteraction_class_" + i2);
                     Class<? extends GalleryPage> page = SAVED_PAGES.get(key);
                     if (page == null) page = classLoader.loadClass(key).asSubclass(GalleryPage.class);
                     Intent intent = (Intent) bundle.getParcelable("iteraction_intent_" + i2);
                     intent.setExtrasClassLoader(classLoader);
                     a(page, intent, bundle.getBundle("iteraction_state_" + i2));
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }
        if (android.os.Build.VERSION.SDK_INT >= 16) {
            try {
                NfcAdapter defaultAdapter = NfcAdapter.getDefaultAdapter(this);
                if (defaultAdapter != null) {
                    NfcAdapter.class.getMethod("setBeamPushUrisCallback", NfcAdapter.CreateBeamUrisCallback.class, android.app.Activity.class)
                            .invoke(defaultAdapter, new PageBeamUris(this), this);
                }
            } catch (Throwable th2) {
            }
        }
    }

   @Override
   public boolean onCreateOptionsMenu(Menu var1) {
      boolean var2 = super.onCreateOptionsMenu(var1);
      GalleryPage var3 = this.c();
      if (var3 != null) {
         var2 = var3.a(var1);
      }

      return var2;
   }

   @Override
   public void onDestroy() {
      super.onDestroy();
      this.a = 0;

      while (this.f.size() > 0) {
         ((GalleryPage)this.f.pop()).e(0);
      }
   }

   @Override
   public boolean onMenuOpened(int var1, Menu var2) {
      GalleryPage var4 = this.c();
      boolean var3;
      if (var4 != null && !var4.a(var1, var2)) {
         var3 = false;
      } else {
         var3 = super.onMenuOpened(var1, var2);
      }

      return var3;
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem var1) {
      GalleryPage var3 = this.c();
      boolean var2;
      if (var3 != null && var3.a(var1)) {
         var2 = true;
      } else {
         var2 = super.onOptionsItemSelected(var1);
      }

      return var2;
   }

   @Override
   public void onOptionsMenuClosed(Menu var1) {
      super.onOptionsMenuClosed(var1);
      GalleryPage var2 = this.c();
      if (var2 != null) {
         var2.e(var1);
      }
   }

   @Override
   public void onPause() {
      super.onPause();
      this.a = 2;
      GalleryPage var1 = this.c();
      if (var1 != null) {
         var1.e(1);
      }
   }

   @Override
   public boolean onPrepareOptionsMenu(Menu var1) {
      boolean var2 = super.onPrepareOptionsMenu(var1);
      GalleryPage var3 = this.c();
      if (var3 != null) {
         var2 = var3.b(var1);
      }

      return var2;
   }

   @Override
   public void onResume() {
      super.onResume();
      this.a = 3;
      GalleryPage var1 = this.c();
      if (var1 != null) {
         var1.e(2);
      }
   }

   @Override
   public void onSaveInstanceState(Bundle var1) {
      super.onSaveInstanceState(var1);
      Iterator var3 = this.f.iterator();

      int var2;
      for (var2 = 0; var3.hasNext(); var2++) {
         GalleryPage var4 = (GalleryPage)var3.next();
         Bundle var5 = new Bundle();
         var4.b(var5);
         var1.putString("iteraction_class_" + var2, savedPageKey(var4.getClass()));
         var1.putParcelable("iteraction_intent_" + var2, var4.E);
         var1.putBundle("iteraction_state_" + var2, var5);
      }

      var1.putInt("iteraction_count", var2);
   }

   @Override
   public void onStart() {
      super.onStart();
      this.a = 2;
   }

   @Override
   public void onStop() {
      super.onStop();
      this.a = 1;
   }

   @Override
   protected void r() {
      GalleryPage var1 = this.c();
      if (var1 != null) {
         var1.k();
      }
   }

   @Override
   public void recreate() {
      super.recreate();
   }
}
