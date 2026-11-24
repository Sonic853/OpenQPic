package android.support.v4.widget;

import android.view.View;

/** Captures, peeks and settles a drawer on one physical edge. */
class g extends o {

    final DrawerLayout a;

    private final int b;
    private l c;
    private final Runnable d = new h(this);

    public g(DrawerLayout drawerLayout, int i) {
        this.a = drawerLayout;
        this.b = i;
    }

    private void b() {
        View viewA = this.a.a(this.b == 3 ? 5 : 3);
        if (viewA != null) {
            this.a.k(viewA);
        }
    }

    static void a(g callback) {
        callback.c();
    }

    private void c() {
        View view;
        int i;
        int iB = this.c.b();
        boolean z = this.b == 3;
        if (z) {
            View viewA = this.a.a(3);
            int i2 = (viewA != null ? -viewA.getWidth() : 0) + iB;
            view = viewA;
            i = i2;
        } else {
            View viewA2 = this.a.a(5);
            int width = this.a.getWidth() - iB;
            view = viewA2;
            i = width;
        }
        if (view != null) {
            if (((!z || view.getLeft() >= i) && (z || view.getLeft() <= i)) || this.a.c(view) != 0) {
                return;
            }
            e eVar = (e) view.getLayoutParams();
            this.c.a(view, i, view.getTop());
            eVar.c = true;
            this.a.invalidate();
            b();
            this.a.b();
        }
    }

    @Override
    public int a(View view) {
        if (this.a.i(view)) {
            return view.getWidth();
        }
        return 0;
    }

    @Override
    public int a(View view, int i, int i2) {
        if (this.a.a(view, 3)) {
            return Math.max(-view.getWidth(), Math.min(i, 0));
        }
        int width = this.a.getWidth();
        return Math.max(width - view.getWidth(), Math.min(i, width));
    }

    public void a() {
        this.a.removeCallbacks(this.d);
    }

    @Override
    public void a(int i) {
        this.a.a(this.b, i, this.c.c());
    }

    @Override
    public void a(int i, int i2) {
        this.a.postDelayed(this.d, 160L);
    }

    public void a(l lVar) {
        this.c = lVar;
    }

    @Override
    public void a(View view, float f, float f2) {
        int width;
        float f3 = this.a.f(view);
        int width2 = view.getWidth();
        if (this.a.a(view, 3)) {
            width = (f > 0.0f || (f == 0.0f && f3 > 0.5f)) ? 0 : -width2;
        } else {
            width = this.a.getWidth();
            if (f < 0.0f || (f == 0.0f && f3 > 0.5f)) {
                width -= width2;
            }
        }
        this.c.a(width, view.getTop());
        this.a.invalidate();
    }

    @Override
    public void a(View view, int i, int i2, int i3, int i4) {
        int width = view.getWidth();
        float width2 = this.a.a(view, 3) ? (float) (width + i) / width : (float) (this.a.getWidth() - i) / width;
        this.a.b(view, width2);
        view.setVisibility(width2 == 0.0f ? 4 : 0);
        this.a.invalidate();
    }

    @Override
    public boolean a(View view, int i) {
        return this.a.i(view) && this.a.a(view, this.b) && this.a.c(view) == 0;
    }

    @Override
    public int b(View view, int i, int i2) {
        return view.getTop();
    }

    @Override
    public void b(int i, int i2) {
        View viewA = (i & 1) == 1 ? this.a.a(3) : this.a.a(5);
        if (viewA == null || this.a.c(viewA) != 0) {
            return;
        }
        this.c.a(viewA, i2);
    }

    @Override
    public void b(View view, int i) {
        ((e) view.getLayoutParams()).c = false;
        b();
    }

    @Override
    public boolean b(int i) {
        return false;
    }
}
