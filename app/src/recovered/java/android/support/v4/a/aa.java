package android.support.v4.a;

import android.view.View;
import java.util.Comparator;

/** Orders child drawing by decor status, then page position. */
class aa implements Comparator {
    public int a(View first, View second) {
        v left = (v) first.getLayoutParams();
        v right = (v) second.getLayoutParams();
        if (left.a != right.a) return left.a ? 1 : -1;
        return left.e - right.e;
    }

    @Override
    public int compare(Object first, Object second) {
        return a((View) first, (View) second);
    }
}
