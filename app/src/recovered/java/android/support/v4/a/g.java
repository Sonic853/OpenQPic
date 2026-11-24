package android.support.v4.a;

import android.view.MotionEvent;

class g implements i {
    g() {
    }

    @Override
    public int a(MotionEvent motionEvent) {
        return 1;
    }

    @Override
    public int a(MotionEvent motionEvent, int i) {
        return i == 0 ? 0 : -1;
    }

    @Override
    public int b(MotionEvent motionEvent, int i) {
        if (i == 0) {
            return 0;
        }
        throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
    }

    @Override
    public float c(MotionEvent motionEvent, int i) {
        if (i == 0) {
            return motionEvent.getX();
        }
        throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
    }

    @Override
    public float d(MotionEvent motionEvent, int i) {
        if (i == 0) {
            return motionEvent.getY();
        }
        throw new IndexOutOfBoundsException("Pre-Eclair does not support multiple pointers");
    }
}
