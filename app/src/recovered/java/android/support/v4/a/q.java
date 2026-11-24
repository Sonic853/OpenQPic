package android.support.v4.a;

import java.util.Comparator;

/** Orders page items by adapter position. */
final class q implements Comparator {
    public int a(u first, u second) {
        return first.b - second.b;
    }

    @Override
    public int compare(Object first, Object second) {
        return a((u) first, (u) second);
    }
}
