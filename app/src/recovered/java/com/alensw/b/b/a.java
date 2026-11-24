package com.alensw.b.b;

public class a extends com.alensw.b.b.c {
   @Override
   public int a(String var1, String var2) {
        int iA;
        int i;
        int i2;
        int length = var1 != null ? var1.length() : 0;
        int length2 = var2 != null ? var2.length() : 0;
        int i3 = 0;
        int i4 = 0;
        while (i4 < length && i3 < length2) {
            char cCharAt = var1.charAt(i4);
            char cCharAt2 = var2.charAt(i3);
            boolean z = cCharAt >= '0' && cCharAt <= '9';
            boolean z2 = cCharAt2 >= '0' && cCharAt2 <= '9';
            if (z && z2) {
                long j = cCharAt - '0';
                long j2 = cCharAt2 - '0';
                i = i4 + 1;
                i2 = i3 + 1;
                while (i < length) {
                    char cCharAt3 = var1.charAt(i);
                    if (cCharAt3 < '0' || cCharAt3 > '9') {
                        break;
                    }
                    i++;
                    j = (j * 10) + ((long) (cCharAt3 - '0'));
                }
                while (i2 < length2) {
                    char cCharAt4 = var2.charAt(i2);
                    if (cCharAt4 < '0' || cCharAt4 > '9') {
                        break;
                    }
                    i2++;
                    j2 = (j2 * 10) + ((long) (cCharAt4 - '0'));
                }
                if (j == j2) {
                    int i5 = i - i4;
                    int i6 = i2 - i3;
                    iA = i5 == i6 ? 0 : i6 - i5;
                } else {
                    iA = j < j2 ? -1 : 1;
                }
            } else if (cCharAt == cCharAt2) {
                iA = 0;
                i = i4 + 1;
                i2 = i3 + 1;
            } else {
                iA = super.a(var1.substring(i4), var2.substring(i3));
                i = i4 + 1;
                i2 = i3 + 1;
            }
            if (iA != 0) {
                return iA;
            }
            i3 = i2;
            i4 = i;
        }
        return length - length2;
    }

   @Override
   public int compare(Object var1, Object var2) {
      return this.a((String)var1, (String)var2);
   }
}
