package android.support.v4.a;

import android.view.VelocityTracker;

class l implements n {
    l() {
    }

    @Override
    public float a(VelocityTracker velocityTracker, int i) {
        return velocityTracker.getXVelocity();
    }

    @Override
    public float b(VelocityTracker velocityTracker, int i) {
        return velocityTracker.getYVelocity();
    }
}
