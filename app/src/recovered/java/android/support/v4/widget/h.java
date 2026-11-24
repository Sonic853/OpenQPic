package android.support.v4.widget;

/** Delayed edge-peek callback. */
class h implements Runnable {

    final g a;

    h(g gVar) {
        this.a = gVar;
    }

    @Override
    public void run() {
        g.a(this.a);
    }
}
