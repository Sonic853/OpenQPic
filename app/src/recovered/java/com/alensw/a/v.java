package com.alensw.a;

import android.os.Handler;
import android.util.Log;
import com.alensw.PicFolder.QuickApp;
import com.alensw.jni.JniUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

class v extends com.alensw.b.k.e {
   public int a;
   public Object b;
   public Handler c;
   public boolean d;
   public volatile boolean e;
   final com.alensw.a.u f;
   private com.alensw.a.al h;
   private final String i;

   private v(com.alensw.a.u var1) {
      this.f = var1;
      this.i = File.separator + "cache";
   }

   v(com.alensw.a.u var1, com.alensw.a.t var2) {
      this(var1);
   }

   private int a(com.alensw.a.e var1) {
      ArrayList var4 = new ArrayList(128);
      Stack var5 = new Stack();
      var5.push(var1);
      int var2 = 0;

      while (!this.e) {
         while (var5.size() > 0) {
            com.alensw.a.e var6 = (com.alensw.a.e)var5.pop();
            String var7 = var6.getPath();
            if ((!var7.endsWith(this.i) || !QuickApp.p.g(var7)) && !this.a(var7, true, true) && this.a(var7, false)) {
               var4.add(var6);
            }
         }

         if (var4.isEmpty() || this.e) {
            break;
         }

         int var3 = this.a(var4, false, false, var5);
         var4.clear();
         var2 += var3;
      }

      return var2;
   }

   private int a(com.alensw.a.e var1, boolean var2) {
      int var3 = 0;
      String var7 = var1.getPath();
      boolean var6 = QuickApp.p.a(var7);
      boolean var5;
      if (!this.d) {
         var5 = true;
      } else {
         var5 = false;
      }

      if (var6) {
         var5 = false;
         var2 = false;
      }

      var1.n();
      if (!var2 || this.a(var7, var1.e)) {
         var1.d = com.alensw.a.u.f(this.f).a(var1);
         if (var1.h == null) {
            var1.h = new ArrayList(64);
         } else {
            var1.h.clear();
         }

         int var4 = com.alensw.a.s.a(this.f.a, this.h, var1, var5, var2);
         var3 = var4;
         if (var2) {
            var3 = var4;
            if (var4 > 0) {
               if (var1.a(true)) {
                  var3 = var4;
                  if (this.d) {
                     com.alensw.a.s.b(this.f.a).a(var1);
                     var3 = var4;
                  }
               } else {
                  com.alensw.a.s.a(this.f.a).a(var1);
                  var3 = var4;
               }
            }
         }
      }

      return var3;
   }

   static int a(com.alensw.a.v var0, com.alensw.a.e var1, boolean var2) {
      return var0.a(var1, var2);
   }

   private int a(ArrayList var1, boolean var2, boolean var3, Stack var4) {
      AtomicInteger var6 = new AtomicInteger();
      ArrayList var7 = new ArrayList(var1.size());
      Iterator var9 = var1.iterator();

      while (var9.hasNext()) {
         var7.add(new com.alensw.a.w(this, var2, (com.alensw.a.e)var9.next(), var6, var4, var3));
      }

      int var5;
      if (this.e) {
         var5 = 0;
      } else {
         try {
            com.alensw.a.s.d().invokeAll(var7);
         } catch (Throwable var8) {
            var8.printStackTrace();
         }

         var5 = var6.get();
      }

      return var5;
   }

   static com.alensw.a.al a(com.alensw.a.v var0) {
      return var0.h;
   }

   static com.alensw.a.al a(com.alensw.a.v var0, com.alensw.a.al var1) {
      var0.h = var1;
      return var1;
   }

   private void a(ArrayList var1) {
      boolean var3 = QuickApp.o.d();
      com.alensw.b.c.d var4 = new com.alensw.b.c.d(2048);
      if (var4.a(com.alensw.a.s.d(this.f.a))) {
         var1.ensureCapacity(var1.size() + var4.c());
         Iterator var5 = var4.f().iterator();

         while (var5.hasNext()) {
            String var6 = (String)((Entry)var5.next()).getKey();
            if (!this.a(var6, true, false) && this.a(var6, false)) {
               int var2;
               if (var3) {
                  var2 = JniUtils.fuGetFileTime(var6);
               } else {
                  var2 = 0;
               }

               var1.add(new com.alensw.a.e(var6, var2));
            }
         }
      }

      com.alensw.b.l.b.a(var1, QuickApp.o.a);
   }

   private boolean a(String var1, int var2) {
      boolean var3;
      if (!com.alensw.a.s.a(this.f.a).a(var1, var2)) {
         var3 = false;
      } else if (this.d) {
         var3 = com.alensw.a.s.b(this.f.a).a(var1, var2);
      } else {
         var3 = true;
      }

      return var3;
   }

   private boolean a(String var1, boolean var2) {
      boolean var6 = true;
      boolean var5 = var6;
      if (com.alensw.a.u.e(this.f) != null) {
         if (com.alensw.a.u.e(this.f).length == 0) {
            var5 = var6;
         } else {
            String[] var8 = com.alensw.a.u.e(this.f);
            int var4 = var8.length;
            int var3 = 0;

            while (true) {
               if (var3 >= var4) {
                  var5 = false;
                  break;
               }

               String var7 = var8[var3];
               var5 = var6;
               if (com.alensw.b.l.b.c(var7, var1)) {
                  break;
               }

               if (var2) {
                  var5 = var6;
                  if (com.alensw.b.l.b.c(var1, var7)) {
                     break;
                  }
               }

               var3++;
            }
         }
      }

      return var5;
   }

   private boolean a(String var1, boolean var2, boolean var3) {
      if (var3 && com.alensw.a.u.c(this.f) != null) {
         Boolean var7 = (Boolean)com.alensw.a.u.c(this.f).get(var1);
         if (var7 == null || !var2 && !var7) {
            var2 = false;
         } else {
            var2 = true;
         }
      } else {
         if (com.alensw.a.u.d(this.f) != null) {
            String[] var6 = com.alensw.a.u.d(this.f);
            int var5 = var6.length;

            for (int var4 = 0; var4 < var5; var4++) {
               if (com.alensw.b.l.b.c(var6[var4], var1)) {
                  var2 = true;
                  return var2;
               }
            }
         }

         var2 = false;
      }

      return var2;
   }

   private int b(com.alensw.a.e var1) {
      var1.h = new ArrayList(64);
      int var2 = this.a(var1, false);
      ArrayList var4 = var1.h;
      var1.h = null;
      if (var2 > 0) {
         this.c.obtainMessage(12, 0, 0, var1).sendToTarget();
      }

      boolean var3 = QuickApp.o.d();

      for (int var7 = var4.size() - 1; var7 >= 0; var7--) {
         var1 = (com.alensw.a.e)var4.get(var7);
         String var5 = var1.getPath();
         if (this.a(var5, false, true) || !this.a(var5, true)) {
            var4.remove(var7);
         } else if (var3 && var1.e == 0) {
            var1.n();
         }
      }

      com.alensw.b.l.b.a(var4, QuickApp.o.a);
      if (!this.e) {
         var2 = this.a(var4, false, true, null) + 1;
      } else {
         var2 = 1;
      }

      return var2;
   }

   private void b() {
        ArrayList<com.alensw.a.e> arrayList = (ArrayList) this.b;
        HashMap mapG = com.alensw.a.s.a(this.f.a).g();
        HashMap mapG2 = com.alensw.a.s.b(this.f.a).g();
        com.alensw.a.s.a(this.f.a).d();
        com.alensw.a.s.b(this.f.a).d();
        for (com.alensw.a.e srcType0111 : arrayList) {
            if (!srcType0111.h()) {
                String path = srcType0111.getPath();
                com.alensw.a.a srcType0073 = (com.alensw.a.a) mapG2.get(path);
                if (srcType0073 == null) {
                    com.alensw.a.a srcType0074 = (com.alensw.a.a) mapG.get(path);
                    if (srcType0074 != null) {
                        com.alensw.a.s.a(this.f.a).a(path, srcType0074);
                    }
                } else if (this.d && srcType0111.a(true)) {
                    com.alensw.a.s.b(this.f.a).a(path, srcType0073);
                }
            }
        }
        com.alensw.a.s.a(this.f.a).a(com.alensw.a.s.a(this.f.a).c() != mapG.size());
        com.alensw.a.s.b(this.f.a).a(com.alensw.a.s.b(this.f.a).c() != mapG2.size());
        if (this.e) {
            return;
        }
        a(arrayList, true, false, (Stack) null);
    }

   static void b(com.alensw.a.v var0) {
      var0.b();
   }

   private void b(ArrayList var1) {
      com.alensw.b.c.d var3 = new com.alensw.b.c.d(2048);
      if (var3.a(com.alensw.a.s.c(this.f.a))) {
         var1.ensureCapacity(var1.size() + var3.c());
         Iterator var6 = var3.f().iterator();

         while (var6.hasNext()) {
            String var4 = (String)((Entry)var6.next()).getKey();
            if (!this.a(var4, true, false) && this.a(var4, false)) {
               com.alensw.a.e var9 = new com.alensw.a.e(var4, 0);
               int var2 = com.alensw.b.l.b.a(var1, var9, com.alensw.a.x.j);
               if (var2 < 0) {
                  var1.add(-var2 - 1, var9);
               }
            }
         }
      }

      for (String var10 : com.alensw.a.am.b()) {
         if (!this.a(var10, true, false) && this.a(var10, false)) {
            com.alensw.a.e var11 = new com.alensw.a.e(var10, 0);
            int var5 = com.alensw.b.l.b.a(var1, var11, com.alensw.a.x.j);
            if (var5 < 0) {
               var1.add(-var5 - 1, var11);
            }
         }
      }

      if (QuickApp.o.d()) {
         Iterator var8 = var1.iterator();

         while (var8.hasNext()) {
            ((com.alensw.a.e)var8.next()).n();
         }
      }

      com.alensw.b.l.b.a(var1, QuickApp.o.a);
   }

   private int c() {
        int iB;
        int i;
        com.alensw.a.bh srcType0108 = QuickApp.p;
        boolean z = this.a == 3;
        ArrayList arrayListA = com.alensw.a.u.a(this.f).a(true);
        ArrayList<String> arrayListA2 = com.alensw.a.u.a(this.f).a(false);
        com.alensw.a.u.a(this.f, new HashMap(com.alensw.a.u.b(this.f).size() + arrayListA.size()));
        if (z) {
            Iterator it = com.alensw.a.u.b(this.f).iterator();
            while (it.hasNext()) {
                com.alensw.a.u.c(this.f).put((String) it.next(), false);
            }
        }
        Iterator it2 = arrayListA.iterator();
        while (it2.hasNext()) {
            com.alensw.a.u.c(this.f).put((String) it2.next(), true);
        }
        if (com.alensw.a.u.c(this.f).isEmpty()) {
            com.alensw.a.u.a(this.f, (HashMap) null);
        } else {
            com.alensw.a.u.a(this.f, new String[com.alensw.a.u.c(this.f).size()]);
            com.alensw.a.u.c(this.f).keySet().toArray(com.alensw.a.u.d(this.f));
        }
        if (arrayListA2.isEmpty()) {
            com.alensw.a.u.b(this.f, null);
        } else {
            com.alensw.a.u.b(this.f, new String[arrayListA2.size()]);
            arrayListA2.toArray(com.alensw.a.u.e(this.f));
        }
        if (z) {
            ArrayList arrayList = new ArrayList(128);
            ArrayList<com.alensw.a.e> arrayList2 = new ArrayList(128);
            for (String str : arrayListA2) {
                com.alensw.a.e srcType0111 = new com.alensw.a.e(str, 0);
                if (srcType0111.exists()) {
                    if (srcType0108.a(str)) {
                        srcType0111.f = 0;
                    }
                    arrayList2.add(srcType0111);
                }
            }
            if (arrayList2.isEmpty()) {
                for (String str2 : srcType0108.a()) {
                    com.alensw.a.e srcType0112 = new com.alensw.a.e(str2, 0);
                    if (srcType0112.exists()) {
                        srcType0112.f = 0;
                        arrayList2.add(srcType0112);
                    }
                }
            }
            long jCurrentTimeMillis = System.currentTimeMillis();
            if (this.d) {
                a(arrayList);
            } else {
                b(arrayList);
            }
            if (arrayList.isEmpty()) {
                i = 0;
            } else {
                int iA = 0 + a(arrayList, false, false, (Stack) null);
                long jCurrentTimeMillis2 = System.currentTimeMillis();
                Log.d("FolderScanner", "prescan: " + iA + ", used=" + (jCurrentTimeMillis2 - jCurrentTimeMillis));
                this.c.obtainMessage(14, 0, 0).sendToTarget();
                jCurrentTimeMillis = jCurrentTimeMillis2;
                i = iA;
            }
            iB = i;
            for (com.alensw.a.e srcType0113 : arrayList2) {
                if (this.e) {
                    break;
                }
                if (this.d || !srcType0113.a(false)) {
                    iB = a(srcType0113) + iB;
                }
            }
            Log.d("FolderScanner", "scan: " + iB + ", used=" + (System.currentTimeMillis() - jCurrentTimeMillis));
            if (!this.e) {
                if (com.alensw.a.s.a(this.f.a).e()) {
                    com.alensw.a.s.a(this.f.a).b(com.alensw.a.s.c(this.f.a));
                }
                if (this.d && com.alensw.a.s.b(this.f.a).e()) {
                    com.alensw.a.s.b(this.f.a).b(com.alensw.a.s.d(this.f.a));
                }
            }
        } else {
            iB = this.b != null ? 0 + b(new com.alensw.a.e((String) this.b, 0)) : 0;
        }
        com.alensw.a.u.a(this.f, (HashMap) null);
        com.alensw.a.u.a(this.f, (String[]) null);
        com.alensw.a.u.b(this.f, null);
        return iB;
    }

   static int c(com.alensw.a.v var0) {
      return var0.c();
   }
}
