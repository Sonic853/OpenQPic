package com.alensw.b.e;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.InputStreamEntity;

final class m extends InputStreamEntity {
   m(InputStream var1, long var2) {
      super(var1, var2);
   }

   @Override
   public void writeTo(OutputStream param1) throws java.io.IOException {
        int i;
        InputStream content = getContent();
        long contentLength = getContentLength();
        try {
            byte[] bArr = new byte[16384];
            if (contentLength < 0) {
                while (true) {
                    int i2 = content.read(bArr);
                    if (i2 == -1) {
                        break;
                    } else {
                        param1.write(bArr, 0, i2);
                    }
                }
            } else {
                while (contentLength > 0 && (i = content.read(bArr, 0, (int) Math.min(bArr.length, contentLength))) != -1) {
                    if (i > 0) {
                        param1.write(bArr, 0, i);
                    }
                    contentLength -= (long) i;
                }
            }
        } finally {
            content.close();
        }
    }
}
