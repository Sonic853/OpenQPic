package com.alensw.ui.view;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

public abstract class cd {
   private final SensorManager a;
   private com.alensw.ui.view.ce b;
   private boolean c;
   private final int d;
   private final Sensor e;
   private int f = -1;

   public cd(Context var1) {
      this(var1, 2);
   }

   private cd(Context var1, int var2) {
      this.d = var2;
      this.a = (SensorManager)var1.getSystemService("sensor");
      this.e = this.a.getDefaultSensor(1);
      if (this.e != null) {
         this.b = new com.alensw.ui.view.ce(this);
      }
   }

   static int a(com.alensw.ui.view.cd var0) {
      return var0.f;
   }

   public void a() {
      if (this.e != null && !this.c) {
         this.a.registerListener(this.b, this.e, this.d);
         this.c = true;
      }
   }

   public abstract void a(int var1);

   public void b() {
      if (this.e != null && this.c) {
         this.a.unregisterListener(this.b);
         this.c = false;
      }
   }
}
