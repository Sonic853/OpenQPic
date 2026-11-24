package android.support.v4.a;

import android.view.animation.Interpolator;

final class r implements Interpolator {
    r() {
    }

    @Override
    public float getInterpolation(float f) {
        float f2 = f - 1.0f;
        return (f2 * f2 * f2 * f2 * f2) + 1.0f;
    }
}
