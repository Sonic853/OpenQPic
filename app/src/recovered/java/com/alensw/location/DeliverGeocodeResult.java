package com.alensw.location;

class DeliverGeocodeResult implements Runnable {
   final PlaceAddress a;
   final GeocodeRequest b;

   DeliverGeocodeResult(GeocodeRequest var1, PlaceAddress var2) {
      this.b = var1;
      this.a = var2;
   }

   @Override
   public void run() {
      this.b.d.a(this.a);
   }
}
