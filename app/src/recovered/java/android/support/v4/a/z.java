package android.support.v4.a;

import android.database.DataSetObserver;

/** Notifies the pager when its adapter data changes. */
class z extends DataSetObserver {

    final p a;

    private z(p pVar) {
        this.a = pVar;
    }

    z(p pVar, q qVar) {
        this(pVar);
    }

    @Override
    public void onChanged() {
        this.a.b();
    }

    @Override
    public void onInvalidated() {
        this.a.b();
    }
}
