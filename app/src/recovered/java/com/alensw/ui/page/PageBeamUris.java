package com.alensw.ui.page;

import android.net.Uri;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;

class PageBeamUris implements CreateBeamUrisCallback {
   final GalleryPageActivity a;

   PageBeamUris(GalleryPageActivity var1) {
      this.a = var1;
   }

   @Override
   public Uri[] createBeamUris(NfcEvent var1) {
      return this.a.f();
   }
}
