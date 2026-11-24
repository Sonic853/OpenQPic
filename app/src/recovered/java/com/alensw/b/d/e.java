package com.alensw.b.d;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import java.io.IOException;
import java.util.List;

public class e {
   private final Geocoder a;

   public e(Context var1) {
      this.a = new Geocoder(var1);
   }

   public boolean a(float var1, float var2, com.alensw.b.d.c var3) {
      try {
         List var5 = this.a.getFromLocation(var1, var2, 1);
         if (!var5.isEmpty()) {
            Address var8 = (Address)var5.get(0);
            var3.a = var8.getCountryName();
            var3.b = var8.getAdminArea();
            var3.c = var8.getSubAdminArea();
            var3.d = var8.getLocality();
            var3.e = var8.getThoroughfare();
            var3.f = var8.getFeatureName();
            return true;
         }
      } catch (IOException var6) {
      } catch (Throwable var7) {
         Log.e("GeoCodeApi", "query failed: " + var1 + "," + var2, var7);
      }

      return false;
   }
}
