package android.support.v4.a;

import android.view.MotionEvent;

class h implements i {
    h() {
    }

    @Override
    public int a(MotionEvent motionEvent) {
        return motionEvent.getPointerCount();
    }

    @Override
    public int a(MotionEvent motionEvent, int i) {
        return motionEvent.findPointerIndex(i);
    }

    @Override
    public int b(MotionEvent motionEvent, int i) {
        return motionEvent.getPointerId(i);
    }

    @Override
    public float c(MotionEvent motionEvent, int i) {
        return motionEvent.getX(i);
    }

    @Override
    public float d(MotionEvent motionEvent, int i) {
        return motionEvent.getY(i);
    }
}
