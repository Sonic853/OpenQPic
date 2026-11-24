package android.support.v4.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.widget.OverScroller;
import android.widget.Scroller;

/** OverScroller / legacy Scroller facade used by the gallery and image grid. */
public class k {

    private OverScroller a;

    private Scroller b;

    @TargetApi(9)
    public k(Context context) {
        if (Build.VERSION.SDK_INT >= 9) {
            this.a = new OverScroller(context);
        } else {
            this.b = new Scroller(context);
        }
    }

    public void a(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, int i9, int i10) {
        if (this.a != null) {
            this.a.fling(i, i2, i3, i4, i5, i6, i7, i8, i9, i10);
        } else {
            this.b.fling(i, i2, i3, i4, i5, i6, i7, i8);
        }
    }

    public void a(boolean z) {
        if (this.a != null) {
            this.a.forceFinished(z);
        } else {
            this.b.forceFinished(z);
        }
    }

    public boolean a() {
        return this.a != null;
    }

    public boolean a(int i, int i2, int i3, int i4, int i5, int i6) {
        if (this.a != null) {
            return this.a.springBack(i, i2, i3, i4, i5, i6);
        }
        this.b.abortAnimation();
        return true;
    }

    public boolean b() {
        return this.a != null ? this.a.computeScrollOffset() : this.b.computeScrollOffset();
    }

    public int c() {
        return this.a != null ? this.a.getCurrX() : this.b.getCurrX();
    }

    public int d() {
        return this.a != null ? this.a.getCurrY() : this.b.getCurrY();
    }

    public float e() {
        if (this.a != null) {
            return this.a.getCurrVelocity();
        }
        if (Build.VERSION.SDK_INT >= 14) {
            return this.b.getCurrVelocity();
        }
        return 0.0f;
    }

    public boolean f() {
        return this.a != null ? this.a.isFinished() : this.b.isFinished();
    }

    public void g() {
        if (this.a != null) {
            this.a.abortAnimation();
        } else {
            this.b.abortAnimation();
        }
    }
}
