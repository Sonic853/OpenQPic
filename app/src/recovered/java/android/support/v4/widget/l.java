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
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.util.Arrays;

/** Drag helper: pointer history, edge detection, captured view and settling. */
public class l {
    private static final Interpolator v = new m();
    private int a;  // mDragState
    private int b;  // mTouchSlop
    private int c = -1;  // mActivePointerId
    private float[] d;  // mInitialMotionX
    private float[] e;  // mInitialMotionY
    private float[] f;  // mLastMotionX
    private float[] g;  // mLastMotionY
    private int[] h;  // mInitialEdgesTouched
    private int[] i;  // mEdgeDragsInProgress
    private int[] j;  // mEdgeDragsLocked
    private int k;  // mPointersDown
    private VelocityTracker l;  // mVelocityTracker
    private float m;  // mMaxVelocity
    private float n;  // mMinVelocity
    private int o;  // mEdgeSize
    private int p;  // mTrackingEdges
    private Scroller q;  // mScroller
    private final o r;  // mCallback
    private View s;  // mCapturedView
    private boolean t;  // mReleaseInProgress
    private final ViewGroup u;  // mParentView
    private final Runnable w = new n(this);  // mSetIdleRunnable

    private l(Context context, ViewGroup parent, o callback) {
        if (parent == null) {
            throw new IllegalArgumentException("Parent view may not be null");
        }
        if (callback == null) {
            throw new IllegalArgumentException("Callback may not be null");
        }
        this.u = parent;
        this.r = callback;
        ViewConfiguration config = ViewConfiguration.get(context);
        this.o = (int) (context.getResources().getDisplayMetrics().density * 16.0f + 0.5f);
        this.b = config.getScaledTouchSlop();
        this.m = config.getScaledMaximumFlingVelocity();
        this.n = config.getScaledMinimumFlingVelocity();
        this.q = new Scroller(context, v);
    }

    // clampMag
    private float a(float value, float absMin, float absMax) {
        final float absValue = Math.abs(value);
        if (absValue < absMin) return 0;
        if (absValue > absMax) return value > 0 ? absMax : -absMax;
        return value;
    }

    // computeAxisDuration
    private int a(int delta, int velocity, int motionRange) {
        if (delta == 0) {
            return 0;
        }

        final int width = this.u.getWidth();
        final int halfWidth = width / 2;
        final float distanceRatio = Math.min(1f, (float) Math.abs(delta) / width);
        final float distance = halfWidth + halfWidth *
                b(distanceRatio);

        int duration;
        velocity = Math.abs(velocity);
        if (velocity > 0) {
            duration = 4 * Math.round(1000 * Math.abs(distance / velocity));
        } else {
            final float range = (float) Math.abs(delta) / motionRange;
            duration = (int) ((range + 1) * 256);
        }
        return Math.min(duration, 600);
    }

    // computeSettleDuration
    private int a(View child, int dx, int dy, int xvel, int yvel) {
        xvel = b(xvel, (int) this.n, (int) this.m);
        yvel = b(yvel, (int) this.n, (int) this.m);
        final int absDx = Math.abs(dx);
        final int absDy = Math.abs(dy);
        final int absXVel = Math.abs(xvel);
        final int absYVel = Math.abs(yvel);
        final int addedVel = absXVel + absYVel;
        final int addedDistance = absDx + absDy;

        final float xweight = xvel != 0 ? (float) absXVel / addedVel :
                (float) absDx / addedDistance;
        final float yweight = yvel != 0 ? (float) absYVel / addedVel :
                (float) absDy / addedDistance;

        int xduration = a(dx, xvel, this.r.a(child));
        int yduration = a(dy, yvel, this.r.b(child));

        return (int) (xduration * xweight + yduration * yweight);
    }

    // create
    public static l a(ViewGroup forParent, float sensitivity, o cb) {
        final l helper = a(forParent, cb);
        helper.b = (int) (helper.b * (1 / sensitivity));
        return helper;
    }

    // create
    public static l a(ViewGroup forParent, o cb) {
        return new l(forParent.getContext(), forParent, cb);
    }

    // dispatchViewReleased
    private void a(float xvel, float yvel) {
        this.t = true;
        this.r.a(this.s, xvel, yvel);
        this.t = false;

        if (this.a == 1) {
            // onViewReleased didn't call a method that would have changed this. Go idle.
            c(0);
        }
    }

    // saveInitialMotion
    private void a(float x, float y, int pointerId) {
        f(pointerId);
        this.d[pointerId] = this.f[pointerId] = x;
        this.e[pointerId] = this.g[pointerId] = y;
        this.h[pointerId] = e((int) x, (int) y);
        this.k |= 1 << pointerId;
    }

    // checkNewEdgeDrag
    private boolean a(float delta, float odelta, int pointerId, int edge) {
        final float absDelta = Math.abs(delta);
        final float absODelta = Math.abs(odelta);

        if ((this.h[pointerId] & edge) != edge  || (this.p & edge) == 0 ||
                (this.j[pointerId] & edge) == edge ||
                (this.i[pointerId] & edge) == edge ||
                (absDelta <= this.b && absODelta <= this.b)) {
            return false;
        }
        if (absDelta < absODelta * 0.5f && this.r.b(edge)) {
            this.j[pointerId] |= edge;
            return false;
        }
        return (this.i[pointerId] & edge) == 0 && absDelta > this.b;
    }

    // forceSettleCapturedViewAt
    private boolean a(int finalLeft, int finalTop, int xvel, int yvel) {
        final int startLeft = this.s.getLeft();
        final int startTop = this.s.getTop();
        final int dx = finalLeft - startLeft;
        final int dy = finalTop - startTop;

        if (dx == 0 && dy == 0) {
            // Nothing to do. Send callbacks, be done.
            this.q.abortAnimation();
            c(0);
            return false;
        }

        final int duration = a(this.s, dx, dy, xvel, yvel);
        this.q.startScroll(startLeft, startTop, dx, dy, duration);

        c(2);
        return true;
    }

    // checkTouchSlop
    private boolean a(View child, float dx, float dy) {
        if (child == null) {
            return false;
        }
        final boolean checkHorizontal = this.r.a(child) > 0;
        final boolean checkVertical = this.r.b(child) > 0;

        if (checkHorizontal && checkVertical) {
            return dx * dx + dy * dy > this.b * this.b;
        } else if (checkHorizontal) {
            return Math.abs(dx) > this.b;
        } else if (checkVertical) {
            return Math.abs(dy) > this.b;
        }
        return false;
    }

    private float b(float var1) {
        return (float)Math.sin((float)((var1 - 0.5F) * 0.4712389167638204));
    }

    // clampMag
    private int b(int value, int absMin, int absMax) {
        final int absValue = Math.abs(value);
        if (absValue < absMin) return 0;
        if (absValue > absMax) return value > 0 ? absMax : -absMax;
        return value;
    }

    // reportNewEdgeDrags
    private void b(float dx, float dy, int pointerId) {
        int dragsStarted = 0;
        if (a(dx, dy, pointerId, 1)) {
            dragsStarted |= 1;
        }
        if (a(dy, dx, pointerId, 4)) {
            dragsStarted |= 4;
        }
        if (a(dx, dy, pointerId, 2)) {
            dragsStarted |= 2;
        }
        if (a(dy, dx, pointerId, 8)) {
            dragsStarted |= 8;
        }

        if (dragsStarted != 0) {
            this.i[pointerId] |= dragsStarted;
            this.r.b(dragsStarted, pointerId);
        }
    }

    // dragTo
    private void b(int left, int top, int dx, int dy) {
        int clampedX = left;
        int clampedY = top;
        final int oldLeft = this.s.getLeft();
        final int oldTop = this.s.getTop();
        if (dx != 0) {
            clampedX = this.r.a(this.s, left, dx);
            this.s.offsetLeftAndRight(clampedX - oldLeft);
        }
        if (dy != 0) {
            clampedY = this.r.b(this.s, top, dy);
            this.s.offsetTopAndBottom(clampedY - oldTop);
        }

        if (dx != 0 || dy != 0) {
            final int clampedDx = clampedX - oldLeft;
            final int clampedDy = clampedY - oldTop;
            this.r.a(this.s, clampedX, clampedY,
                    clampedDx, clampedDy);
        }
    }

    // saveLastMotion
    private void c(MotionEvent ev) {
        final int pointerCount = android.support.v4.a.f.c(ev);
        for (int i = 0; i < pointerCount; i++) {
            final int pointerId = android.support.v4.a.f.b(ev, i);
            final float x = android.support.v4.a.f.c(ev, i);
            final float y = android.support.v4.a.f.d(ev, i);
            this.f[pointerId] = x;
            this.g[pointerId] = y;
        }
    }

    // getEdgesTouched
    private int e(int x, int y) {
        int result = 0;

        if (x < this.u.getLeft() + this.o) result |= 1;
        if (y < this.u.getTop() + this.o) result |= 4;
        if (x > this.u.getRight() - this.o) result |= 2;
        if (y > this.u.getBottom() - this.o) result |= 8;

        return result;
    }

    // clearMotionHistory
    private void e(int pointerId) {
        if (this.d == null) {
            return;
        }
        this.d[pointerId] = 0;
        this.e[pointerId] = 0;
        this.f[pointerId] = 0;
        this.g[pointerId] = 0;
        this.h[pointerId] = 0;
        this.i[pointerId] = 0;
        this.j[pointerId] = 0;
        this.k &= ~(1 << pointerId);
    }

    // clearMotionHistory
    private void f() {
        if (this.d == null) {
            return;
        }
        Arrays.fill(this.d, 0);
        Arrays.fill(this.e, 0);
        Arrays.fill(this.f, 0);
        Arrays.fill(this.g, 0);
        Arrays.fill(this.h, 0);
        Arrays.fill(this.i, 0);
        Arrays.fill(this.j, 0);
        this.k = 0;
    }

    // ensureMotionHistorySizeForId
    private void f(int pointerId) {
        if (this.d == null || this.d.length <= pointerId) {
            float[] imx = new float[pointerId + 1];
            float[] imy = new float[pointerId + 1];
            float[] lmx = new float[pointerId + 1];
            float[] lmy = new float[pointerId + 1];
            int[] iit = new int[pointerId + 1];
            int[] edip = new int[pointerId + 1];
            int[] edl = new int[pointerId + 1];

            if (this.d != null) {
                System.arraycopy(this.d, 0, imx, 0, this.d.length);
                System.arraycopy(this.e, 0, imy, 0, this.e.length);
                System.arraycopy(this.f, 0, lmx, 0, this.f.length);
                System.arraycopy(this.g, 0, lmy, 0, this.g.length);
                System.arraycopy(this.h, 0, iit, 0, this.h.length);
                System.arraycopy(this.i, 0, edip, 0, this.i.length);
                System.arraycopy(this.j, 0, edl, 0, this.j.length);
            }

            this.d = imx;
            this.e = imy;
            this.f = lmx;
            this.g = lmy;
            this.h = iit;
            this.i = edip;
            this.j = edl;
        }
    }

    // releaseViewForPointerUp
    private void g() {
        this.l.computeCurrentVelocity(1000, this.m);
        final float xvel = a(
                android.support.v4.a.k.a(this.l, this.c),
                this.n, this.m);
        final float yvel = a(
                android.support.v4.a.k.b(this.l, this.c),
                this.n, this.m);
        a(xvel, yvel);
    }

    // getViewDragState
    public int a() {
        return this.a;
    }

    // setMinVelocity
    public void a(float minVel) {
        this.n = minVel;
    }

    // setEdgeTrackingEnabled
    public void a(int edgeFlags) {
        this.p = edgeFlags;
    }

    // captureChildView
    public void a(View childView, int activePointerId) {
        if (childView.getParent() != this.u) {
            throw new IllegalArgumentException("captureChildView: parameter must be a descendant " +
                    "of the ViewDragHelper's tracked parent view (" + this.u + ")");
        }

        this.s = childView;
        this.c = activePointerId;
        this.r.b(childView, activePointerId);
        c(1);
    }

    // settleCapturedViewAt
    public boolean a(int finalLeft, int finalTop) {
        if (!this.t) {
            throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to " +
                    "Callback#onViewReleased");
        }

        return a(finalLeft, finalTop,
                (int) android.support.v4.a.k.a(this.l, this.c),
                (int) android.support.v4.a.k.b(this.l, this.c));
    }

    // shouldInterceptTouchEvent
    public boolean a(MotionEvent ev) {
        final int action = android.support.v4.a.f.a(ev);
        final int actionIndex = android.support.v4.a.f.b(ev);

        if (action == MotionEvent.ACTION_DOWN) {
            // Reset things for a new event stream, just in case we didn't get
            // the whole previous stream.
            e();
        }

        if (this.l == null) {
            this.l = VelocityTracker.obtain();
        }
        this.l.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                final int pointerId = android.support.v4.a.f.b(ev, 0);
                a(x, y, pointerId);

                final View toCapture = d((int) x, (int) y);

                // Catch a settling view if possible.
                if (toCapture == this.s && this.a == 2) {
                    b(toCapture, pointerId);
                }

                final int edgesTouched = this.h[pointerId];
                if ((edgesTouched & this.p) != 0) {
                    this.r.a(edgesTouched & this.p, pointerId);
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerId = android.support.v4.a.f.b(ev, actionIndex);
                final float x = android.support.v4.a.f.c(ev, actionIndex);
                final float y = android.support.v4.a.f.d(ev, actionIndex);

                a(x, y, pointerId);

                // A ViewDragHelper can only manipulate one view at a time.
                if (this.a == 0) {
                    final int edgesTouched = this.h[pointerId];
                    if ((edgesTouched & this.p) != 0) {
                        this.r.a(edgesTouched & this.p, pointerId);
                    }
                } else if (this.a == 2) {
                    // Catch a settling view if possible.
                    final View toCapture = d((int) x, (int) y);
                    if (toCapture == this.s) {
                        b(toCapture, pointerId);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // First to cross a touch slop over a draggable view wins. Also report edge drags.
                final int pointerCount = android.support.v4.a.f.c(ev);
                for (int i = 0; i < pointerCount; i++) {
                    final int pointerId = android.support.v4.a.f.b(ev, i);
                    final float x = android.support.v4.a.f.c(ev, i);
                    final float y = android.support.v4.a.f.d(ev, i);
                    final float dx = x - this.d[pointerId];
                    final float dy = y - this.e[pointerId];

                    final View toCapture = d((int) x, (int) y);
                    final boolean pastSlop = toCapture != null && a(toCapture, dx, dy);
                    if (pastSlop) {
                        // check the callback's
                        // getView[Horizontal|Vertical]DragRange methods to know
                        // if you can move at all along an axis, then see if it
                        // would clamp to the same value. If you can't move at
                        // all in every dimension with a nonzero range, bail.
                        final int oldLeft = toCapture.getLeft();
                        final int targetLeft = oldLeft + (int) dx;
                        final int newLeft = this.r.a(toCapture,
                                targetLeft, (int) dx);
                        final int oldTop = toCapture.getTop();
                        final int targetTop = oldTop + (int) dy;
                        final int newTop = this.r.b(toCapture, targetTop,
                                (int) dy);
                        final int horizontalDragRange = this.r.a(
                                toCapture);
                        final int verticalDragRange = this.r.b(toCapture);
                        if ((horizontalDragRange == 0 || horizontalDragRange > 0
                                && newLeft == oldLeft) && (verticalDragRange == 0
                                || verticalDragRange > 0 && newTop == oldTop)) {
                            break;
                        }
                    }
                    b(dx, dy, pointerId);
                    if (this.a == 1) {
                        // Callback might have started an edge drag
                        break;
                    }

                    if (pastSlop && b(toCapture, pointerId)) {
                        break;
                    }
                }
                c(ev);
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerId = android.support.v4.a.f.b(ev, actionIndex);
                e(pointerId);
                break;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                e();
                break;
            }
        }

        return this.a == 1;
    }

    // smoothSlideViewTo
    public boolean a(View child, int finalLeft, int finalTop) {
        this.s = child;
        this.c = -1;

        boolean continueSliding = a(finalLeft, finalTop, 0, 0);
        if (!continueSliding && this.a == 0 && this.s != null) {
            // If we're in an IDLE state to begin with and aren't moving anywhere, we
            // end up having a non-null capturedView with an IDLE dragState
            this.s = null;
        }

        return continueSliding;
    }

    // continueSettling
    public boolean a(boolean deferCallbacks) {
        if (this.a == 2) {
            boolean keepGoing = this.q.computeScrollOffset();
            final int x = this.q.getCurrX();
            final int y = this.q.getCurrY();
            final int dx = x - this.s.getLeft();
            final int dy = y - this.s.getTop();

            if (dx != 0) {
                this.s.offsetLeftAndRight(dx);
            }
            if (dy != 0) {
                this.s.offsetTopAndBottom(dy);
            }

            if (dx != 0 || dy != 0) {
                this.r.a(this.s, x, y, dx, dy);
            }

            if (keepGoing && x == this.q.getFinalX() && y == this.q.getFinalY()) {
                // Close enough. The interpolator/scroller might think we're still moving
                // but the user sure doesn't.
                this.q.abortAnimation();
                keepGoing = false;
            }

            if (!keepGoing) {
                if (deferCallbacks) {
                    this.u.post(this.w);
                } else {
                    c(0);
                }
            }
        }

        return this.a == 2;
    }

    // getEdgeSize
    public int b() {
        return this.o;
    }

    // processTouchEvent
    public void b(MotionEvent ev) {
        final int action = android.support.v4.a.f.a(ev);
        final int actionIndex = android.support.v4.a.f.b(ev);

        if (action == MotionEvent.ACTION_DOWN) {
            // Reset things for a new event stream, just in case we didn't get
            // the whole previous stream.
            e();
        }

        if (this.l == null) {
            this.l = VelocityTracker.obtain();
        }
        this.l.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();
                final int pointerId = android.support.v4.a.f.b(ev, 0);
                final View toCapture = d((int) x, (int) y);

                a(x, y, pointerId);

                // Since the parent is already directly processing this touch event,
                // there is no reason to delay for a slop before dragging.
                // Start immediately if possible.
                b(toCapture, pointerId);

                final int edgesTouched = this.h[pointerId];
                if ((edgesTouched & this.p) != 0) {
                    this.r.a(edgesTouched & this.p, pointerId);
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_DOWN: {
                final int pointerId = android.support.v4.a.f.b(ev, actionIndex);
                final float x = android.support.v4.a.f.c(ev, actionIndex);
                final float y = android.support.v4.a.f.d(ev, actionIndex);

                a(x, y, pointerId);

                // A ViewDragHelper can only manipulate one view at a time.
                if (this.a == 0) {
                    // If we're idle we can do anything! Treat it like a normal down event.

                    final View toCapture = d((int) x, (int) y);
                    b(toCapture, pointerId);

                    final int edgesTouched = this.h[pointerId];
                    if ((edgesTouched & this.p) != 0) {
                        this.r.a(edgesTouched & this.p, pointerId);
                    }
                } else if (c((int) x, (int) y)) {
                    // We're still tracking a captured view. If the same view is under this
                    // point, we'll swap to controlling it with this pointer instead.
                    // (This will still work if we're "catching" a settling view.)

                    b(this.s, pointerId);
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (this.a == 1) {
                    final int index = android.support.v4.a.f.a(ev, this.c);
                    final float x = android.support.v4.a.f.c(ev, index);
                    final float y = android.support.v4.a.f.d(ev, index);
                    final int idx = (int) (x - this.f[this.c]);
                    final int idy = (int) (y - this.g[this.c]);

                    b(this.s.getLeft() + idx, this.s.getTop() + idy, idx, idy);

                    c(ev);
                } else {
                    // Check to see if any pointer is now over a draggable view.
                    final int pointerCount = android.support.v4.a.f.c(ev);
                    for (int i = 0; i < pointerCount; i++) {
                        final int pointerId = android.support.v4.a.f.b(ev, i);
                        final float x = android.support.v4.a.f.c(ev, i);
                        final float y = android.support.v4.a.f.d(ev, i);
                        final float dx = x - this.d[pointerId];
                        final float dy = y - this.e[pointerId];

                        b(dx, dy, pointerId);
                        if (this.a == 1) {
                            // Callback might have started an edge drag.
                            break;
                        }

                        final View toCapture = d((int) x, (int) y);
                        if (a(toCapture, dx, dy) &&
                                b(toCapture, pointerId)) {
                            break;
                        }
                    }
                    c(ev);
                }
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerId = android.support.v4.a.f.b(ev, actionIndex);
                if (this.a == 1 && pointerId == this.c) {
                    // Try to find another pointer that's still holding on to the captured view.
                    int newActivePointer = -1;
                    final int pointerCount = android.support.v4.a.f.c(ev);
                    for (int i = 0; i < pointerCount; i++) {
                        final int id = android.support.v4.a.f.b(ev, i);
                        if (id == this.c) {
                            // This one's going away, skip.
                            continue;
                        }

                        final float x = android.support.v4.a.f.c(ev, i);
                        final float y = android.support.v4.a.f.d(ev, i);
                        if (d((int) x, (int) y) == this.s &&
                                b(this.s, id)) {
                            newActivePointer = this.c;
                            break;
                        }
                    }

                    if (newActivePointer == -1) {
                        // We didn't find another pointer still touching the view, release it.
                        g();
                    }
                }
                e(pointerId);
                break;
            }

            case MotionEvent.ACTION_UP: {
                if (this.a == 1) {
                    g();
                }
                e();
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                if (this.a == 1) {
                    a(0.0f, 0.0f);
                }
                e();
                break;
            }
        }
    }

    // isPointerDown
    public boolean b(int pointerId) {
        return (this.k & 1 << pointerId) != 0;
    }

    // checkTouchSlop
    public boolean b(int directions, int pointerId) {
        if (!b(pointerId)) {
            return false;
        }

        final boolean checkHorizontal = (directions & 1) == 1;
        final boolean checkVertical = (directions & 2) == 2;

        final float dx = this.f[pointerId] - this.d[pointerId];
        final float dy = this.g[pointerId] - this.e[pointerId];

        if (checkHorizontal && checkVertical) {
            return dx * dx + dy * dy > this.b * this.b;
        } else if (checkHorizontal) {
            return Math.abs(dx) > this.b;
        } else if (checkVertical) {
            return Math.abs(dy) > this.b;
        }
        return false;
    }

    // tryCaptureViewForDrag
    boolean b(View toCapture, int pointerId) {
        if (toCapture == this.s && this.c == pointerId) {
            // Already done!
            return true;
        }
        if (toCapture != null && this.r.a(toCapture, pointerId)) {
            this.c = pointerId;
            a(toCapture, pointerId);
            return true;
        }
        return false;
    }

    // isViewUnder
    public boolean b(View view, int x, int y) {
        if (view == null) {
            return false;
        }
        return x >= view.getLeft() &&
                x < view.getRight() &&
                y >= view.getTop() &&
                y < view.getBottom();
    }

    // getCapturedView
    public View c() {
        return this.s;
    }

    // setDragState
    void c(int state) {
        if (this.a != state) {
            this.a = state;
            this.r.a(state);
            if (this.a == 0) {
                this.s = null;
            }
        }
    }

    // isCapturedViewUnder
    public boolean c(int x, int y) {
        return b(this.s, x, y);
    }

    // getTouchSlop
    public int d() {
        return this.b;
    }

    // findTopChildUnder
    public View d(int x, int y) {
        final int childCount = this.u.getChildCount();
        for (int i = childCount - 1; i >= 0; i--) {
            final View child = this.u.getChildAt(this.r.c(i));
            if (x >= child.getLeft() && x < child.getRight() &&
                    y >= child.getTop() && y < child.getBottom()) {
                return child;
            }
        }
        return null;
    }

    // checkTouchSlop
    public boolean d(int directions) {
        final int count = this.d.length;
        for (int i = 0; i < count; i++) {
            if (b(directions, i)) {
                return true;
            }
        }
        return false;
    }

    // cancel
    public void e() {
        this.c = -1;
        f();

        if (this.l != null) {
            this.l.recycle();
            this.l = null;
        }
    }
}
