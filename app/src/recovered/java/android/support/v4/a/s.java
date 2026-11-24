package android.support.v4.a;

/** Ends the scroll animation and repopulates the pager. */
class s implements Runnable {

    final p a;

    s(p pVar) {
        this.a = pVar;
    }

    @Override
    public void run() {
        p.a(this.a, 0);
        this.a.c();
    }
}
