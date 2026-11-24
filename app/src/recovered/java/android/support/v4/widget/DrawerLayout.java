/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Adapted to the shipped gallery behavior and member names; see docs/recovery/android-support.md.
 */
package android.support.v4.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Parcel;
import android.os.Parcelable.Creator;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;

/** Gallery drawer container; left and right drag helpers track the two edges separately. */
public class DrawerLayout extends ViewGroup {
    static final a a;
    private static final int[] b = new int[]{16842931};
    private int c;  // mMinDrawerMargin
    private int d = -1728053248;  // mScrimColor
    private float e;  // mScrimOpacity
    private Paint f = new Paint();  // mScrimPaint
    private final l g;  // mLeftDragger
    private final l h;  // mRightDragger
    private final g i;  // mLeftCallback
    private final g j;  // mRightCallback
    private boolean k;  // mInLayout
    private boolean l = true;  // mFirstLayout
    private int m;  // mLockModeLeft
    private int n;  // mLockModeRight
    private boolean o;  // mDisallowInterceptRequested
    private boolean p;  // mChildrenCanceledTouch
    private d q;  // mListener
    private float r;  // mInitialMotionX
    private float s;  // mInitialMotionY
    private Drawable t;  // mShadowLeft
    private Drawable u;  // mShadowRight
    private Drawable v;  // mStatusBarBackground
    private Object w;  // mLastInsets
    private boolean x;  // mDrawStatusBarBackground

    static {
        if (VERSION.SDK_INT >= 21) {
            a = new b();
        } else {
            a = new c();
        }
    }

    public DrawerLayout(Context var1) {
        this(var1, null);
    }

    public DrawerLayout(Context var1, AttributeSet var2) {
        this(var1, var2, 0);
    }

    public DrawerLayout(Context var1, AttributeSet var2, int var3) {
        super(var1, var2, var3);
        float var4 = this.getResources().getDisplayMetrics().density;
        this.c = (int)(64.0F * var4 + 0.5F);
        var4 *= 400.0F;
        this.i = new g(this, 3);
        this.j = new g(this, 5);
        this.g = android.support.v4.widget.l.a(this, 1.0F, this.i);
        this.g.a(1);
        this.g.a(var4);
        this.i.a(this.g);
        this.h = android.support.v4.widget.l.a(this, 1.0F, this.j);
        this.h.a(2);
        this.h.a(var4);
        this.j.a(this.h);
    }

    protected static boolean a(View view) {
        return VERSION.SDK_INT >= 16 && view.getFitsSystemWindows();
    }

    protected static int b(View view) {
        return VERSION.SDK_INT >= 17 ? view.getLayoutDirection() : 0;
    }

    // gravityToString
    static String b(int gravity) {
        if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            return "LEFT";
        }
        if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            return "RIGHT";
        }
        return Integer.toHexString(gravity);
    }

    static int[] c() {
        return b;
    }

    // hasPeekingDrawer
    private boolean d() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final e lp = (e) getChildAt(i).getLayoutParams();
            if (lp.c) {
                return true;
            }
        }
        return false;
    }

    private static boolean n(View view) {
        Drawable background = view.getBackground();
        return background != null && background.getOpacity() == android.graphics.PixelFormat.OPAQUE;
    }

    // findOpenDrawer
    View a() {
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            if (((e) child.getLayoutParams()).d) {
                return child;
            }
        }
        return null;
    }

    // findDrawerWithGravity
    View a(int gravity) {
        final int absHorizGravity = android.support.v4.a.a.a(
                gravity, b(this)) & Gravity.HORIZONTAL_GRAVITY_MASK;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final int childAbsGravity = g(child);
            if ((childAbsGravity & Gravity.HORIZONTAL_GRAVITY_MASK) == absHorizGravity) {
                return child;
            }
        }
        return null;
    }

    public void a(int var1, int var2) {
        var2 = android.support.v4.a.a.a(var2, b(this));
        if (var2 == 3) {
            this.m = var1;
        } else if (var2 == 5) {
            this.n = var1;
        }

        if (var1 != 0) {
            l var3;
            if (var2 == 3) {
                var3 = this.g;
            } else {
                var3 = this.h;
            }

            var3.e();
        }

        switch (var1) {
            case 1:
                View var6 = this.a(var2);
                if (var6 != null) {
                    this.k(var6);
                }
                break;
            case 2:
                View var5 = this.a(var2);
                if (var5 != null) {
                    this.j(var5);
                }
        }
    }

    void a(int gravity, int activeState, View drawer) {
        int leftState = this.g.a();
        int rightState = this.h.a();
        int state = leftState == 1 || rightState == 1 ? 1
                : leftState == 2 || rightState == 2 ? 2 : 0;
        if (drawer == null) return;

        e params = (e) drawer.getLayoutParams();
        if (activeState == 0) {
            if (params.b == 0.0f) d(drawer);
            else if (params.b == 1.0f) e(drawer);
        }
        // Gallery listeners receive the drawer and its closed flag, not just a global state.
        if (params.e != state) {
            params.e = state;
            if (this.q != null) this.q.a(drawer, params.b == 0.0f, state);
        }
    }

    void a(View var1, float var2) {
        if (this.q != null) {
            this.q.a(var1, var2);
        }
    }

    // closeDrawers
    void a(boolean peekingOnly) {
        boolean needsInvalidate = false;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final e lp = (e) child.getLayoutParams();

            if (!i(child) || (peekingOnly && !lp.c)) {
                continue;
            }

            final int childWidth = child.getWidth();

            if (a(child, Gravity.LEFT)) {
                needsInvalidate |= this.g.a(child,
                        -childWidth, child.getTop());
            } else {
                needsInvalidate |= this.h.a(child,
                        getWidth(), child.getTop());
            }

            lp.c = false;
        }

        this.i.a();
        this.j.a();

        if (needsInvalidate) {
            invalidate();
        }
    }

    // checkDrawerViewAbsoluteGravity
    boolean a(View drawerView, int checkFor) {
        final int absGravity = g(drawerView);
        return (absGravity & checkFor) == checkFor;
    }

    // cancelChildViewTouch
    void b() {
        // Cancel child touches
        if (!this.p) {
            final long now = SystemClock.uptimeMillis();
            final MotionEvent cancelEvent = MotionEvent.obtain(now, now,
                    MotionEvent.ACTION_CANCEL, 0.0f, 0.0f, 0);
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                getChildAt(i).dispatchTouchEvent(cancelEvent);
            }
            cancelEvent.recycle();
            this.p = true;
        }
    }

    // setDrawerViewOffset
    void b(View drawerView, float slideOffset) {
        final e lp = (e) drawerView.getLayoutParams();
        if (slideOffset == lp.b) {
            return;
        }

        lp.b = slideOffset;
        a(drawerView, slideOffset);
    }

    public int c(View var1) {
        int var2 = this.g(var1);
        if (var2 == 3) {
            var2 = this.m;
        } else if (var2 == 5) {
            var2 = this.n;
        } else {
            var2 = 0;
        }

        return var2;
    }

    public void c(int var1) {
        View var2 = this.a(var1);
        if (var2 == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + b(var1));
        } else {
            this.j(var2);
        }
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams var1) {
        boolean var2;
        if (var1 instanceof e && super.checkLayoutParams(var1)) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    @Override
    public void computeScroll() {
        int var3 = this.getChildCount();
        float var1 = 0.0F;

        for (int var2 = 0; var2 < var3; var2++) {
            var1 = Math.max(var1, ((e)this.getChildAt(var2).getLayoutParams()).b);
        }

        this.e = var1;
        if (this.g.a(true) | this.h.a(true)) {
            if (VERSION.SDK_INT >= 16) {
                this.postInvalidateOnAnimation();
            } else {
                this.invalidate();
            }
        }
    }

    public void d(int var1) {
        View var2 = this.a(var1);
        if (var2 == null) {
            throw new IllegalArgumentException("No drawer view found with gravity " + b(var1));
        } else {
            this.k(var2);
        }
    }

    void d(View var1) {
        e var2 = (e)var1.getLayoutParams();
        if (var2.d) {
            var2.d = false;
            if (this.q != null) {
                this.q.b(var1);
            }

            if (this.hasWindowFocus()) {
                var1 = this.getRootView();
                if (var1 != null) {
                    var1.sendAccessibilityEvent(32);
                }
            }
        }
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        final int height = getHeight();
        final boolean drawingContent = h(child);
        int clipLeft = 0, clipRight = getWidth();

        final int restoreCount = canvas.save();
        if (drawingContent) {
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View v = getChildAt(i);
                if (v == child || v.getVisibility() != VISIBLE ||
                        !n(v) || !i(v) ||
                        v.getHeight() < height) {
                    continue;
                }

                if (a(v, Gravity.LEFT)) {
                    final int vright = v.getRight();
                    if (vright > clipLeft) clipLeft = vright;
                } else {
                    final int vleft = v.getLeft();
                    if (vleft < clipRight) clipRight = vleft;
                }
            }
            canvas.clipRect(clipLeft, 0, clipRight, getHeight());
        }
        final boolean result = super.drawChild(canvas, child, drawingTime);
        canvas.restoreToCount(restoreCount);

        if (this.e > 0 && drawingContent) {
            final int baseAlpha = (this.d & 0xff000000) >>> 24;
            final int imag = (int) (baseAlpha * this.e);
            final int color = imag << 24 | (this.d & 0xffffff);
            this.f.setColor(color);

            final e left = (e) a(Gravity.LEFT).getLayoutParams();
            canvas.drawRect(clipLeft, left.topMargin, clipRight, getHeight() - left.bottomMargin, this.f);
        } else if (this.t != null && a(child, Gravity.LEFT)) {
            final int shadowWidth = this.t.getIntrinsicWidth();
            final int childRight = child.getRight();
            final int drawerPeekDistance = this.g.b();
            final float alpha =
                    Math.max(0, Math.min((float) childRight / drawerPeekDistance, 1.f));
            this.t.setBounds(childRight, child.getTop(),
                    childRight + shadowWidth, child.getBottom());
            this.t.setAlpha((int) (0xff * alpha));
            this.t.draw(canvas);
        } else if (this.u != null && a(child, Gravity.RIGHT)) {
            final int shadowWidth = this.u.getIntrinsicWidth();
            final int childLeft = child.getLeft();
            final int showing = getWidth() - childLeft;
            final int drawerPeekDistance = this.h.b();
            final float alpha =
                    Math.max(0, Math.min((float) showing / drawerPeekDistance, 1.f));
            this.u.setBounds(childLeft - shadowWidth, child.getTop(),
                    childLeft, child.getBottom());
            this.u.setAlpha((int) (0xff * alpha));
            this.u.draw(canvas);
        }
        return result;
    }

    void e(View var1) {
        e var2 = (e)var1.getLayoutParams();
        if (!var2.d) {
            var2.d = true;
            if (this.q != null) {
                this.q.a(var1);
            }

            this.sendAccessibilityEvent(32);
        }
    }

    public boolean e(int var1) {
        View var3 = this.a(var1);
        boolean var2;
        if (var3 != null) {
            var2 = this.l(var3);
        } else {
            var2 = false;
        }

        return var2;
    }

    // getDrawerViewOffset
    float f(View drawerView) {
        return ((e) drawerView.getLayoutParams()).b;
    }

    public boolean f(int var1) {
        View var3 = this.a(var1);
        boolean var2;
        if (var3 != null) {
            var2 = this.m(var3);
        } else {
            var2 = false;
        }

        return var2;
    }

    // getDrawerViewAbsoluteGravity
    int g(View drawerView) {
        final int gravity = ((e) drawerView.getLayoutParams()).a;
        return android.support.v4.a.a.a(gravity, b(this));
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new e(-1, -1);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet var1) {
        return new e(this.getContext(), var1);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams var1) {
        e var2;
        if (var1 instanceof e) {
            var2 = new e((e)var1);
        } else if (var1 instanceof MarginLayoutParams) {
            var2 = new e((MarginLayoutParams)var1);
        } else {
            var2 = new e(var1);
        }

        return var2;
    }

    boolean h(View var1) {
        boolean var2;
        if (var1.getVisibility() == 0 && ((e)var1.getLayoutParams()).a == 0) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    boolean i(View var1) {
        boolean var2;
        if ((android.support.v4.a.a.a(((e)var1.getLayoutParams()).a, b(var1)) & 7) != 0) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    public void j(View var1) {
        if (!this.i(var1)) {
            throw new IllegalArgumentException("View " + var1 + " is not a sliding drawer");
        } else {
            if (this.l) {
                e var2 = (e)var1.getLayoutParams();
                var2.b = 1.0F;
                var2.d = true;
            } else if (this.a(var1, 3)) {
                this.g.a(var1, 0, var1.getTop());
            } else {
                this.h.a(var1, this.getWidth() - var1.getWidth(), var1.getTop());
            }

            this.invalidate();
        }
    }

    public void k(View var1) {
        if (!this.i(var1)) {
            throw new IllegalArgumentException("View " + var1 + " is not a sliding drawer");
        } else {
            if (this.l) {
                e var2 = (e)var1.getLayoutParams();
                var2.b = 0.0F;
                var2.d = false;
            } else if (this.a(var1, 3)) {
                this.g.a(var1, -var1.getWidth(), var1.getTop());
            } else {
                this.h.a(var1, this.getWidth(), var1.getTop());
            }

            this.invalidate();
        }
    }

    public boolean l(View var1) {
        if (!this.i(var1)) {
            throw new IllegalArgumentException("View " + var1 + " is not a drawer");
        } else {
            return ((e)var1.getLayoutParams()).d;
        }
    }

    public boolean m(View var1) {
        if (!this.i(var1)) {
            throw new IllegalArgumentException("View " + var1 + " is not a drawer");
        } else {
            boolean var2;
            if (((e)var1.getLayoutParams()).b > 0.0F) {
                var2 = true;
            } else {
                var2 = false;
            }

            return var2;
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.l = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.l = true;
    }

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);
        if (this.x && this.v != null) {
            final int inset = a.a(this.w);
            if (inset > 0) {
                this.v.setBounds(0, 0, getWidth(), inset);
                this.v.draw(c);
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = android.support.v4.a.f.a(ev);

        // "|" used deliberately here; both methods should be invoked.
        final boolean interceptForDrag = this.g.a(ev) |
                this.h.a(ev);

        boolean interceptForTap = false;

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                this.r = x;
                this.s = y;
                if (this.e > 0) {
                    final View child = this.g.d((int) x, (int) y);
                    if (h(child)) {
                        interceptForTap = true;
                    }
                }
                this.o = false;
                this.p = false;
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // If we cross the touch slop, don't perform the delayed peek for an edge touch.
                if (this.g.d(3)) {
                    this.i.a();
                    this.j.a();
                }
                break;
            }

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP: {
                a(true);
                this.o = false;
                this.p = false;
            }
        }

        return interceptForDrag || interceptForTap || d() || this.p;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        this.k = true;
        final int width = r - l;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final e lp = (e) child.getLayoutParams();

            if (h(child)) {
                child.layout(lp.leftMargin, lp.topMargin,
                        lp.leftMargin + child.getMeasuredWidth(),
                        lp.topMargin + child.getMeasuredHeight());
            } else { // Drawer, if it wasn't onMeasure would have thrown an exception.
                final int childWidth = child.getMeasuredWidth();
                final int childHeight = child.getMeasuredHeight();
                int childLeft;

                final float newOffset;
                if (a(child, Gravity.LEFT)) {
                    childLeft = -childWidth + (int) (childWidth * lp.b);
                    newOffset = (float) (childWidth + childLeft) / childWidth;
                } else { // Right; onMeasure checked for us.
                    childLeft = width - (int) (childWidth * lp.b);
                    newOffset = (float) (width - childLeft) / childWidth;
                }

                final boolean changeOffset = newOffset != lp.b;

                final int vgrav = lp.a & Gravity.VERTICAL_GRAVITY_MASK;

                switch (vgrav) {
                    default:
                    case Gravity.TOP: {
                        child.layout(childLeft, lp.topMargin, childLeft + childWidth,
                                lp.topMargin + childHeight);
                        break;
                    }

                    case Gravity.BOTTOM: {
                        final int height = b - t;
                        child.layout(childLeft,
                                height - lp.bottomMargin - child.getMeasuredHeight(),
                                childLeft + childWidth,
                                height - lp.bottomMargin);
                        break;
                    }

                    case Gravity.CENTER_VERTICAL: {
                        final int height = b - t;
                        int childTop = (height - childHeight) / 2;

                        // Offset for margins. If things don't fit right because of
                        // bad measurement before, oh well.
                        if (childTop < lp.topMargin) {
                            childTop = lp.topMargin;
                        } else if (childTop + childHeight > height - lp.bottomMargin) {
                            childTop = height - lp.bottomMargin - childHeight;
                        }
                        child.layout(childLeft, childTop, childLeft + childWidth,
                                childTop + childHeight);
                        break;
                    }
                }

                if (changeOffset) {
                    b(child, newOffset);
                }

                final int newVisibility = lp.b > 0 ? VISIBLE : INVISIBLE;
                if (child.getVisibility() != newVisibility) {
                    child.setVisibility(newVisibility);
                }
            }
        }
        this.k = false;
        this.l = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY) {
            if (isInEditMode()) {
                // Don't crash the layout editor. Consume all of the space if specified
                // or pick a magic number from thin air otherwise.
                // TODO Better communication with tools of this bogus state.
                // It will crash on a real device.
                if (widthMode == MeasureSpec.AT_MOST) {
                    widthMode = MeasureSpec.EXACTLY;
                } else if (widthMode == MeasureSpec.UNSPECIFIED) {
                    widthMode = MeasureSpec.EXACTLY;
                    widthSize = 300;
                }
                if (heightMode == MeasureSpec.AT_MOST) {
                    heightMode = MeasureSpec.EXACTLY;
                }
                else if (heightMode == MeasureSpec.UNSPECIFIED) {
                    heightMode = MeasureSpec.EXACTLY;
                    heightSize = 300;
                }
            } else {
                throw new IllegalArgumentException(
                        "DrawerLayout must be measured with MeasureSpec.EXACTLY.");
            }
        }

        setMeasuredDimension(widthSize, heightSize);

        final boolean applyInsets = this.w != null && a(this);
        final int layoutDirection = b(this);

        // In the shipped layout the mask is initialized but never updated.
        int foundDrawers = 0;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            final e lp = (e) child.getLayoutParams();

            if (applyInsets) {
                final int cgrav = android.support.v4.a.a.a(lp.a, layoutDirection);
                if (a(child)) {
                    a.a(child, this.w, cgrav);
                } else {
                    a.a(lp, this.w, cgrav);
                }
            }

            if (h(child)) {
                // Content views get measured at exactly the layout's size.
                final int contentWidthSpec = MeasureSpec.makeMeasureSpec(
                        widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY);
                final int contentHeightSpec = MeasureSpec.makeMeasureSpec(
                        heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY);
                child.measure(contentWidthSpec, contentHeightSpec);
            } else if (i(child)) {
                final int childGravity =
                        g(child) & Gravity.HORIZONTAL_GRAVITY_MASK;
                if ((foundDrawers & childGravity) != 0) {
                    throw new IllegalStateException("Child drawer has absolute gravity " +
                            b(childGravity) + " but this " + "DrawerLayout" + " already has a " +
                            "drawer view along that edge");
                }
                final int drawerWidthSpec = getChildMeasureSpec(widthMeasureSpec,
                        this.c + lp.leftMargin + lp.rightMargin,
                        lp.width);
                final int drawerHeightSpec = getChildMeasureSpec(heightMeasureSpec,
                        lp.topMargin + lp.bottomMargin,
                        lp.height);
                child.measure(drawerWidthSpec, drawerHeightSpec);
            } else {
                throw new IllegalStateException("Child " + child + " at index " + i +
                        " does not have a valid layout_gravity - must be Gravity.LEFT, " +
                        "Gravity.RIGHT or Gravity.NO_GRAVITY");
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Parcelable var1) {
        SavedState var3 = (SavedState)var1;
        super.onRestoreInstanceState(var3.getSuperState());
        if (var3.a != 0) {
            View var2 = this.a(var3.a);
            if (var2 != null) {
                this.j(var2);
            }
        }

        this.a(var3.b, 3);
        this.a(var3.c, 5);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState var3 = new SavedState(super.onSaveInstanceState());
        int var2 = this.getChildCount();

        for (int var1 = 0; var1 < var2; var1++) {
            View var4 = this.getChildAt(var1);
            if (this.i(var4)) {
                e var5 = (e)var4.getLayoutParams();
                if (var5.d) {
                    var3.a = var5.a;
                    break;
                }
            }
        }

        var3.b = this.m;
        var3.c = this.n;
        return var3;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        this.g.b(ev);
        this.h.b(ev);

        final int action = ev.getAction();
        boolean wantTouchEvents = true;

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                this.r = x;
                this.s = y;
                this.o = false;
                this.p = false;
                break;
            }

            case MotionEvent.ACTION_UP: {
                final float x = ev.getX();
                final float y = ev.getY();
                boolean peekingOnly = true;
                final View touchedView = this.g.d((int) x, (int) y);
                if (touchedView != null && h(touchedView)) {
                    final float dx = x - this.r;
                    final float dy = y - this.s;
                    final int slop = this.g.d();
                    if (dx * dx + dy * dy < slop * slop) {
                        // Taps close a dimmed open drawer but only if it isn't locked open.
                        final View openDrawer = a();
                        if (openDrawer != null) {
                            peekingOnly = c(openDrawer) == 2;
                        }
                    }
                }
                a(peekingOnly);
                this.o = false;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                a(true);
                this.o = false;
                this.p = false;
                break;
            }
        }

        return wantTouchEvents;
    }

    @Override
    public void requestDisallowInterceptTouchEvent(boolean var1) {
        super.requestDisallowInterceptTouchEvent(var1);
        this.o = var1;
        if (var1) {
            this.a(true);
        }
    }

    @Override
    public void requestLayout() {
        if (!this.k) {
            super.requestLayout();
        }
    }

    public void setDrawerListener(d var1) {
        this.q = var1;
    }

    public void setDrawerLockMode(int var1) {
        this.a(var1, 3);
        this.a(var1, 5);
    }

    public void setScrimColor(int var1) {
        this.d = var1;
        this.invalidate();
    }

    public void setStatusBarBackground(int var1) {
        Drawable var2;
        if (var1 != 0) {
            var2 = this.getResources().getDrawable(var1);
        } else {
            var2 = null;
        }

        this.v = var2;
    }

    public void setStatusBarBackground(Drawable var1) {
        this.v = var1;
    }

    public void setStatusBarBackgroundColor(int var1) {
        this.v = new ColorDrawable(var1);
    }

    public static class SavedState extends View.BaseSavedState {
        public static final Creator CREATOR = new f();
        int a = 0;
        int b = 0;
        int c = 0;

        public SavedState(Parcel source) {
            super(source);
            this.a = source.readInt();
        }

        public SavedState(Parcelable parentState) {
            super(parentState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            // Lock modes b/c stay in process. Only the open drawer gravity is in the parcel.
            dest.writeInt(this.a);
        }
    }
}
