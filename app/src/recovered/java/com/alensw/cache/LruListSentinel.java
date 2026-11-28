package com.alensw.cache;

class LruListSentinel extends LruEntry {
   final LruEntryList a;

   LruListSentinel(LruEntryList var1) {
      this.a = var1;
   }
}
