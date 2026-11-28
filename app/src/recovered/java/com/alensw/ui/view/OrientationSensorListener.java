package com.alensw.ui.view;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.FloatMath;

final class OrientationSensorListener implements SensorEventListener {
   private static final int[][] b = new int[][]{{-25, 70}, {-25, 65}, {-25, 60}, {-25, 65}};
   private final DeviceOrientationMonitor a;
   private long c;
   private float d;
   private float e;
   private float f;
   private int g;
   private int h;
   private long i;
   private long j;
   private long k;
   private long l;
   private float[] m = new float[40];
   private long[] n = new long[40];
   private int o;

   public OrientationSensorListener(DeviceOrientationMonitor var1) {
      this.a = var1;
      this.a();
   }

   private int a(int var1) {
      int var2 = var1;
      if (var1 == 0) {
         var2 = 40;
      }

      var1 = var2 - 1;
      if (this.n[var1] == Long.MIN_VALUE) {
         var1 = -1;
      }

      return var1;
   }

   private void a() {
      this.c = Long.MIN_VALUE;
      this.g = -1;
      this.j = Long.MIN_VALUE;
      this.k = Long.MIN_VALUE;
      this.l = Long.MIN_VALUE;
      this.b();
      this.c();
   }

   private void a(long var1, float var3) {
      this.m[this.o] = var3;
      this.n[this.o] = var1;
      this.o = (this.o + 1) % 40;
      this.n[this.o] = Long.MIN_VALUE;
   }

   private void a(long var1, int var3) {
      if (this.h != var3) {
         this.h = var3;
         this.i = var1;
      }
   }

   private boolean a(float var1) {
      boolean var2;
      if (!(var1 < 5.80665F) && !(var1 > 13.80665F)) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private boolean a(int var1, int var2) {
      boolean var3 = true;
      if (var2 < b[var1][0] || var2 > b[var1][1]) {
         var3 = false;
      }

      return var3;
   }

   private boolean a(long var1) {
      boolean var4 = false;
      boolean var3;
      if (var1 < this.i + 40000000L) {
         var3 = var4;
      } else {
         var3 = var4;
         if (var1 >= this.j + 500000000L) {
            var3 = var4;
            if (var1 >= this.k + 300000000L) {
               var3 = var4;
               if (var1 >= this.l + 500000000L) {
                  var3 = true;
               }
            }
         }
      }

      return var3;
   }

   private void b() {
      this.h = -1;
      this.i = Long.MIN_VALUE;
   }

   private boolean b(int var1, int var2) {
      boolean var6 = false;
      int var3 = DeviceOrientationMonitor.a(this.a);
      if (var3 >= 0) {
         if (var1 == var3 || var1 == (var3 + 1) % 4) {
            int var4 = var1 * 90 - 45 + 22;
            if (var1 == 0) {
               if (var2 >= 315 && var2 < var4 + 360) {
                  return var6;
               }
            } else if (var2 < var4) {
               return var6;
            }
         }

         if (var1 == var3 || var1 == (var3 + 3) % 4) {
            var3 = var1 * 90 + 45 - 22;
            if (var1 == 0) {
               if (var2 <= 45 && var2 > var3) {
                  return var6;
               }
            } else if (var2 > var3) {
               return var6;
            }
         }
      }

      return true;
   }

   private boolean b(long var1) {
      int var3 = this.o;

      boolean var5;
      while (true) {
         int var4 = this.a(var3);
         if (var4 >= 0 && !(this.m[var4] < 75.0F)) {
            var3 = var4;
            if (this.n[var4] + 1000000000L > var1) {
               continue;
            }

            var5 = true;
            break;
         }

         var5 = false;
         break;
      }

      return var5;
   }

   private boolean b(long var1, float var3) {
      int var4 = this.o;

      boolean var6;
      while (true) {
         int var5 = this.a(var4);
         if (var5 >= 0 && this.n[var5] + 300000000L >= var1) {
            var4 = var5;
            if (!(this.m[var5] + 20.0F <= var3)) {
               continue;
            }

            var6 = true;
            break;
         }

         var6 = false;
         break;
      }

      return var6;
   }

   private void c() {
      this.n[0] = Long.MIN_VALUE;
      this.o = 1;
   }

   @Override
   public void onAccuracyChanged(Sensor var1, int var2) {
   }

   @Override
   public void onSensorChanged(SensorEvent sensorEvent) {
        boolean z;
        float f = sensorEvent.values[0];
        float f2 = sensorEvent.values[1];
        float f3 = sensorEvent.values[2];
        long j = sensorEvent.timestamp;
        long j2 = this.c;
        float f4 = (j - j2) * 1.0E-6f;
        if (j < j2 || j > j2 + 1000000000 || (f == 0.0f && f2 == 0.0f && f3 == 0.0f)) {
            a();
            z = true;
        } else {
            float f5 = f4 / (200.0f + f4);
            f = ((f - this.d) * f5) + this.d;
            f2 = ((f2 - this.e) * f5) + this.e;
            f3 = this.f + (f5 * (f3 - this.f));
            z = false;
        }
        this.c = j;
        this.d = f;
        this.e = f2;
        this.f = f3;
        if (!z) {
            float fSqrt = (float) Math.sqrt((f * f) + (f2 * f2) + (f3 * f3));
            if (fSqrt < 1.0f) {
                b();
            } else {
                if (a(fSqrt)) {
                    this.l = j;
                }
                int iRound = (int) Math.round(Math.asin(f3 / fSqrt) * 57.295780181884766d);
                a(j, (float) iRound);
                if (b(j)) {
                    this.j = j;
                }
                if (b(j, iRound)) {
                    this.k = j;
                }
                if (Math.abs(iRound) > 75) {
                    b();
                } else {
                    int iRound2 = (int) Math.round((-Math.atan2(-f, f2)) * 57.295780181884766d);
                    if (iRound2 < 0) {
                        iRound2 += 360;
                    }
                    int i = (iRound2 + 45) / 90;
                    if (i == 4) {
                        i = 0;
                    }
                    if (a(i, iRound) && b(i, iRound2)) {
                        a(j, i);
                    } else {
                        b();
                    }
                }
            }
        }
        int i2 = this.g;
        if (this.h < 0 || a(j)) {
            this.g = this.h;
        }
        if (this.g == i2 || this.g < 0) {
            return;
        }
        this.a.a(this.g);
    }
}
