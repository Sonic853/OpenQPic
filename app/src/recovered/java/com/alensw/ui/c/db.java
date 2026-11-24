package com.alensw.ui.c;

import android.net.Uri;
import android.nfc.NfcEvent;
import android.nfc.NfcAdapter.CreateBeamUrisCallback;

class db implements CreateBeamUrisCallback {
   final com.alensw.ui.c.cx a;

   db(com.alensw.ui.c.cx var1) {
      this.a = var1;
   }

   @Override
   public Uri[] createBeamUris(NfcEvent var1) {
      return this.a.f();
   }
}
