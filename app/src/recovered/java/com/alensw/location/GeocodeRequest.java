package com.alensw.location;

class GeocodeRequest implements Runnable {
   final long a;
   final float b;
   final float c;
   final GeocodeCallback d;
   final ReverseGeocoding e;

   public GeocodeRequest(ReverseGeocoding var1, float var2, float var3, long var4, GeocodeCallback var6) {
      this.e = var1;
      this.a = var4;
      this.b = var2;
      this.c = var3;
      this.d = var6;
   }

   @Override
   public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        }
        if (!(var1 instanceof GeocodeRequest)) {
            return false;
        }
        GeocodeRequest srcType0160 = (GeocodeRequest) var1;
        return this.a == srcType0160.a && this.d == srcType0160.d;
    }

   @Override
   public int hashCode() {
      return (int)(this.a ^ this.a >>> 32);
   }

   @Override
   public void run() {
      PlaceAddress var3 = new PlaceAddress();
      boolean var1 = ReverseGeocoding.b(this.b, this.c);
      if (ReverseGeocoding.c(this.e).a(this.b, this.c, var3)) {
         if (var1) {
            var3.b = ReverseGeocoding.a(var3.b);
            var3.c = ReverseGeocoding.a(var3.c);
            var3.d = ReverseGeocoding.a(var3.d);
         }

         String var2 = var3.a();
         ReverseGeocoding.d(this.e).put(this.a, var2);
         ReverseGeocoding.e(this.e).a(this.a, var2);
         ReverseGeocoding.f(this.e);
      }

      if (this.d != null) {
         ReverseGeocoding.g(this.e).post(new DeliverGeocodeResult(this, var3));
      }

      ReverseGeocoding.h(this.e).remove(this.a);
      if (ReverseGeocoding.i(this.e) > 0 && ReverseGeocoding.h(this.e).isEmpty()) {
         ReverseGeocoding.j(this.e);
      }
   }
}
