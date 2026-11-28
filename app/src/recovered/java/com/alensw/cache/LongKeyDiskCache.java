package com.alensw.cache;

import android.util.Log;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class LongKeyDiskCache {
   private final ConcurrentHashMap<Long, Long> a = new ConcurrentHashMap<>(1024);
   private final ReentrantReadWriteLock b = new ReentrantReadWriteLock();
   private RandomAccessFile c;
   private RandomAccessFile d;

   public Object a(long var1) {
      Long offset = a.get(var1);
      if (offset == null) return null;
      b.readLock().lock();
      try {
         d.seek(offset);
         return a(d);
      } catch (Throwable failure) {
         Log.e("LongKeyCache", "read value: ", failure);
         return null;
      } finally {
         b.readLock().unlock();
      }
   }

   protected abstract Object a(RandomAccessFile var1);

   public void a(long param1, Object param3) {
        this.b.writeLock().lock();
        try {
            if (!this.a.containsKey(Long.valueOf(param1))) {
                long length = this.d.length();
                try {
                    this.d.seek(length);
                    a(this.d, param3);
                    this.c.writeLong(param1);
                    this.c.writeLong(length);
                    this.a.put(Long.valueOf(param1), Long.valueOf(length));
                } catch (Throwable th) {
                    Log.e("LongKeyCache", "write value: ", th);
                }
            }
        } catch (Throwable th2) {
            Log.e("LongKeyCache", "write index: ", th2);
        } finally {
            this.b.writeLock().unlock();
        }
    }

   protected abstract void a(RandomAccessFile var1, Object var2);

   public boolean a() {
      b.readLock().lock();
      try {
         return c != null && d != null;
      } finally {
         b.readLock().unlock();
      }
   }

   public boolean a(File param1, String param2) {
      if (!param1.exists()) param1.mkdirs();
      File index = new File(param1, param2 + ".idx");
      File values = new File(param1, param2 + ".val");
      b.writeLock().lock();
      try {
         c = new RandomAccessFile(index, "rw");
         int count = 0;
         long length = c.length();
         if (length < 4) {
            c.writeInt(1);
            length = 4;
         } else if (c.readInt() == 1) {
            try {
               for (long pos = 4; pos + 16 <= length; pos += 16) {
                  long key = c.readLong();
                  long offset = c.readLong();
                  a.put(key, offset);
                  count++;
               }
            } catch (Throwable failure) {
               Log.e("LongKeyCache", "read index: " + count, failure);
            }
         }
         long end = count * 16 + 4;
         if (end < length) {
            c.setLength(end);
            Log.w("LongKeyCache", "trim index file: " + length + "->" + end);
         }
         if (c.getFilePointer() != end) c.seek(end);
         d = new RandomAccessFile(values, "rw");
         return true;
      } catch (Throwable failure) {
         Log.e("LongKeyCache", "open value file: ", failure);
         return false;
      } finally {
         b.writeLock().unlock();
      }
   }
}
