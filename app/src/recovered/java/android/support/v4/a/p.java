/*
 * Copyright (C) 2011 The Android Open Source Project
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
package android.support.v4.a;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.util.Log;
import android.view.FocusFinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SoundEffectConstants;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/** Gallery build of ViewPager. The short member names are the shipped ABI. */
public class p extends ViewGroup {
    private static final int[] a = new int[]{16842931};
    private static final Comparator c = new q();
    private static final Interpolator d = new r();
    private static final aa ag = new aa();
    private boolean A;  // mIsBeingDragged
    private boolean B;  // mIsUnableToDrag
    private int C;  // mDefaultGutterSize
    private int D;  // mGutterSize
    private int E;  // mTouchSlop
    private float F;  // mLastMotionX
    private float G;  // mLastMotionY
    private float H;  // mInitialMotionX
    private float I;  // mInitialMotionY
    private int J;  // mActivePointerId
    private VelocityTracker K;  // mVelocityTracker
    private int L;  // mMinimumVelocity
    private int M;  // mMaximumVelocity
    private int N;  // mFlingDistance
    private int O;  // mCloseEnough
    private boolean P;  // mFakeDragging
    private android.support.v4.widget.j Q;  // mLeftEdge
    private android.support.v4.widget.j R;  // mRightEdge
    private boolean S;  // mFirstLayout
    private boolean T;  // mNeedCalculatePageOffsets
    private boolean U;  // mCalledSuper
    private int V;  // mDecorChildCount
    private x W;  // mOnPageChangeListener
    private x aa;  // mInternalPageChangeListener
    private w ab;  // mAdapterChangeListener
    private y ac;  // mPageTransformer
    private Method ad;  // mSetChildrenDrawingOrderEnabled
    private int ae;  // mDrawingOrder
    private ArrayList<View> af;  // mDrawingOrderedChildren
    private final Runnable ah;  // mEndScrollRunnable
    private int ai;  // mScrollState
    private int b;  // mExpectedAdapterCount
    private final ArrayList<u> e = new ArrayList<>();  // mItems
    private final u f = new u();  // mTempItem
    private final Rect g = new Rect();  // mTempRect
    private j h;  // mAdapter
    private int i;  // mCurItem
    private int j = -1;  // mRestoredCurItem
    private Parcelable k = null;  // mRestoredAdapterState
    private ClassLoader l = null;  // mRestoredClassLoader
    private Scroller m;  // mScroller
    private z n;  // mObserver
    private int o;  // mPageMargin
    private Drawable p;  // mMarginDrawable
    private int q;  // mTopPageBounds
    private int r;  // mBottomPageBounds
    private float s = -Float.MAX_VALUE;  // mFirstOffset
    private float t = Float.MAX_VALUE;  // mLastOffset
    private int u;  // mChildWidthMeasureSpec
    private int v;  // mChildHeightMeasureSpec
    private boolean w;  // mInLayout
    private boolean x;  // mScrollingCacheEnabled
    private boolean y;  // mPopulatePending
    private int z = 1;  // mOffscreenPageLimit

    public p(Context var1) {
        super(var1);
        this.J = -1;
        this.S = true;
        this.T = false;
        this.ah = new s(this);
        this.ai = 0;
        this.a();
    }

    // determineTargetPage
    private int a(int currentPage, float pageOffset, int velocity, int deltaX) {
        int targetPage;
        if (Math.abs(deltaX) > this.N && Math.abs(velocity) > this.L) {
            targetPage = velocity > 0 ? currentPage : currentPage + 1;
        } else {
            final float truncator = currentPage >= this.i ? 0.4f : 0.6f;
            targetPage = (int) (currentPage + pageOffset + truncator);
        }

        if (this.e.size() > 0) {
            final u firstItem = this.e.get(0);
            final u lastItem = this.e.get(this.e.size() - 1);

            // Only let the user target pages we have items for
            targetPage = Math.max(firstItem.b, Math.min(targetPage, lastItem.b));
        }

        return targetPage;
    }

    // getChildRectInPagerCoordinates
    private Rect a(Rect outRect, View child) {
        if (outRect == null) {
            outRect = new Rect();
        }
        if (child == null) {
            outRect.set(0, 0, 0, 0);
            return outRect;
        }
        outRect.left = child.getLeft();
        outRect.right = child.getRight();
        outRect.top = child.getTop();
        outRect.bottom = child.getBottom();

        ViewParent parent = child.getParent();
        while (parent instanceof ViewGroup && parent != this) {
            final ViewGroup group = (ViewGroup) parent;
            outRect.left += group.getLeft();
            outRect.right += group.getRight();
            outRect.top += group.getTop();
            outRect.bottom += group.getBottom();

            parent = group.getParent();
        }
        return outRect;
    }

    // recomputeScrollPosition
    private void a(int width, int oldWidth, int margin, int oldMargin) {
        if (oldWidth > 0 && !this.e.isEmpty()) {
            final int widthWithMargin = width - getPaddingLeft() - getPaddingRight() + margin;
            final int oldWidthWithMargin = oldWidth - getPaddingLeft() - getPaddingRight()
                                           + oldMargin;
            final int xpos = getScrollX();
            final float pageOffset = (float) xpos / oldWidthWithMargin;
            final int newOffsetPixels = (int) (pageOffset * widthWithMargin);

            scrollTo(newOffsetPixels, getScrollY());
            if (!this.m.isFinished()) {
                // We now return to your regularly scheduled scroll, already in progress.
                final int newDuration = this.m.getDuration() - this.m.timePassed();
                u targetInfo = b(this.i);
                this.m.startScroll(newOffsetPixels, 0,
                        (int) (targetInfo.e * width), 0, newDuration);
            }
        } else {
            final u ii = b(this.i);
            final float scrollOffset = ii != null ? Math.min(ii.e, this.t) : 0;
            final int scrollPos = (int) (scrollOffset *
                                         (width - getPaddingLeft() - getPaddingRight()));
            if (scrollPos != getScrollX()) {
                a(false);
                scrollTo(scrollPos, getScrollY());
            }
        }
    }

    // scrollToItem
    private void a(int item, boolean smoothScroll, int velocity,
            boolean dispatchSelected) {
        final u curInfo = b(item);
        int destX = 0;
        if (curInfo != null) {
            final int width = getClientWidth();
            destX = (int) (width * Math.max(this.s,
                    Math.min(curInfo.e, this.t)));
        }
        if (smoothScroll) {
            a(destX, 0, velocity);
            if (dispatchSelected && this.W != null) {
                this.W.a(item);
            }
            if (dispatchSelected && this.aa != null) {
                this.aa.a(item);
            }
        } else {
            if (dispatchSelected && this.W != null) {
                this.W.a(item);
            }
            if (dispatchSelected && this.aa != null) {
                this.aa.a(item);
            }
            a(false);
            scrollTo(destX, 0);
            d(destX);
        }
    }

    static void a(p var0, int var1) {
        var0.setScrollState(var1);
    }

    // calculatePageOffsets
    private void a(u curItem, int curIndex, u oldCurInfo) {
        final int N = this.h.a();
        final int width = getClientWidth();
        final float marginOffset = width > 0 ? (float) this.o / width : 0;
        // Fix up offsets for later layout.
        if (oldCurInfo != null) {
            final int oldCurPosition = oldCurInfo.b;
            // Base offsets off of oldCurInfo.
            if (oldCurPosition < curItem.b) {
                int itemIndex = 0;
                u ii = null;
                float offset = oldCurInfo.e + oldCurInfo.d + marginOffset;
                for (int pos = oldCurPosition + 1;
                        pos <= curItem.b && itemIndex < this.e.size(); pos++) {
                    ii = this.e.get(itemIndex);
                    while (pos > ii.b && itemIndex < this.e.size() - 1) {
                        itemIndex++;
                        ii = this.e.get(itemIndex);
                    }
                    while (pos < ii.b) {
                        // We don't have an item populated for this,
                        // ask the adapter for an offset.
                        offset += this.h.a(pos) + marginOffset;
                        pos++;
                    }
                    ii.e = offset;
                    offset += ii.d + marginOffset;
                }
            } else if (oldCurPosition > curItem.b) {
                int itemIndex = this.e.size() - 1;
                u ii = null;
                float offset = oldCurInfo.e;
                for (int pos = oldCurPosition - 1;
                        pos >= curItem.b && itemIndex >= 0; pos--) {
                    ii = this.e.get(itemIndex);
                    while (pos < ii.b && itemIndex > 0) {
                        itemIndex--;
                        ii = this.e.get(itemIndex);
                    }
                    while (pos > ii.b) {
                        // We don't have an item populated for this,
                        // ask the adapter for an offset.
                        offset -= this.h.a(pos) + marginOffset;
                        pos--;
                    }
                    offset -= ii.d + marginOffset;
                    ii.e = offset;
                }
            }
        }

        // Base all offsets off of curItem.
        final int itemCount = this.e.size();
        float offset = curItem.e;
        int pos = curItem.b - 1;
        this.s = curItem.b == 0 ? curItem.e : -Float.MAX_VALUE;
        this.t = curItem.b == N - 1 ?
                curItem.e + curItem.d - 1 : Float.MAX_VALUE;
        // Previous pages
        for (int i = curIndex - 1; i >= 0; i--, pos--) {
            final u ii = this.e.get(i);
            while (pos > ii.b) {
                offset -= this.h.a(pos--) + marginOffset;
            }
            offset -= ii.d + marginOffset;
            ii.e = offset;
            if (ii.b == 0) this.s = offset;
        }
        offset = curItem.e + curItem.d + marginOffset;
        pos = curItem.b + 1;
        // Next pages
        for (int i = curIndex + 1; i < itemCount; i++, pos++) {
            final u ii = this.e.get(i);
            while (pos < ii.b) {
                offset += this.h.a(pos++) + marginOffset;
            }
            if (ii.b == N - 1) {
                this.t = offset + ii.d - 1;
            }
            ii.e = offset;
            offset += ii.d + marginOffset;
        }

        this.T = false;
    }

    // onSecondaryPointerUp
    private void a(MotionEvent ev) {
        final int pointerIndex = android.support.v4.a.f.b(ev);
        final int pointerId = android.support.v4.a.f.b(ev, pointerIndex);
        if (pointerId == this.J) {
            // This was our active pointer going up. Choose a new
            // active pointer and adjust accordingly.
            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
            this.F = android.support.v4.a.f.c(ev, newPointerIndex);
            this.J = android.support.v4.a.f.b(ev, newPointerIndex);
            if (this.K != null) {
                this.K.clear();
            }
        }
    }

    public static void a(View var0, Runnable var1) {
        if (VERSION.SDK_INT >= 16) {
            var0.postOnAnimation(var1);
        } else {
            var0.postDelayed(var1, 16L);
        }
    }

    // completeScroll
    private void a(boolean postEvents) {
        boolean needPopulate = this.ai == 2;
        if (needPopulate) {
            // Done with scroll, no longer want to cache view drawing.
            setScrollingCacheEnabled(false);
            this.m.abortAnimation();
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.m.getCurrX();
            int y = this.m.getCurrY();
            if (oldX != x || oldY != y) {
                scrollTo(x, y);
            }
        }
        this.y = false;
        for (int i=0; i<this.e.size(); i++) {
            u ii = this.e.get(i);
            if (ii.c) {
                needPopulate = true;
                ii.c = false;
            }
        }
        if (needPopulate) {
            if (postEvents) {
                a(this, this.ah);
            } else {
                this.ah.run();
            }
        }
    }

    // isGutterDrag
    private boolean a(float x, float dx) {
        return (x < this.D && dx > 0) || (x > getWidth() - this.D && dx < 0);
    }

    // enableLayers
    private void b(boolean enable) {
        if (VERSION.SDK_INT < 11) return;
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final int layerType = enable ?
                    View.LAYER_TYPE_HARDWARE : View.LAYER_TYPE_NONE;
            getChildAt(i).setLayerType(layerType, null);
        }
    }

    // performDrag
    private boolean b(float x) {
        boolean needsInvalidate = false;

        final float deltaX = this.F - x;
        this.F = x;

        float oldScrollX = getScrollX();
        float scrollX = oldScrollX + deltaX;
        final int width = getClientWidth();

        float leftBound = width * this.s;
        float rightBound = width * this.t;
        boolean leftAbsolute = true;
        boolean rightAbsolute = true;

        final u firstItem = this.e.get(0);
        final u lastItem = this.e.get(this.e.size() - 1);
        if (firstItem.b != 0) {
            leftAbsolute = false;
            leftBound = firstItem.e * width;
        }
        if (lastItem.b != this.h.a() - 1) {
            rightAbsolute = false;
            rightBound = lastItem.e * width;
        }

        if (scrollX < leftBound) {
            if (leftAbsolute) {
                float over = leftBound - scrollX;
                needsInvalidate = this.Q.a(Math.abs(over) / width);
            }
            scrollX = leftBound;
        } else if (scrollX > rightBound) {
            if (rightAbsolute) {
                float over = scrollX - rightBound;
                needsInvalidate = this.R.a(Math.abs(over) / width);
            }
            scrollX = rightBound;
        }
        // Don't lose the rounded component
        this.F += scrollX - (int) scrollX;
        scrollTo((int) scrollX, getScrollY());
        d((int) scrollX);

        return needsInvalidate;
    }

    public static void c(View var0) {
        if (VERSION.SDK_INT >= 16) {
            var0.postInvalidateOnAnimation();
        } else {
            var0.invalidate();
        }
    }

    // requestParentDisallowInterceptTouchEvent
    private void c(boolean disallowIntercept) {
        final ViewParent parent = getParent();
        if (parent != null) {
            parent.requestDisallowInterceptTouchEvent(disallowIntercept);
        }
    }

    // pageScrolled
    private boolean d(int xpos) {
        if (this.e.size() == 0) {
            this.U = false;
            a(0, 0.0f, 0);
            if (!this.U) {
                throw new IllegalStateException(
                        "onPageScrolled did not call superclass implementation");
            }
            return false;
        }
        final u ii = i();
        final int width = getClientWidth();
        final int widthWithMargin = width + this.o;
        final float marginOffset = (float) this.o / width;
        final int currentPage = ii.b;
        final float pageOffset = (((float) xpos / width) - ii.e) /
                (ii.d + marginOffset);
        final int offsetPixels = (int) (pageOffset * widthWithMargin);

        this.U = false;
        a(currentPage, pageOffset, offsetPixels);
        if (!this.U) {
            throw new IllegalStateException(
                    "onPageScrolled did not call superclass implementation");
        }
        return true;
    }

    static int[] f() {
        return a;
    }

    // removeNonDecorViews
    private void g() {
        for (int i = 0; i < getChildCount(); i++) {
            final View child = getChildAt(i);
            final v lp = (v) child.getLayoutParams();
            if (!lp.a) {
                removeViewAt(i);
                i--;
            }
        }
    }

    private int getClientWidth() {
        return getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    // sortChildDrawingOrder
    private void h() {
        if (this.ae != 0) {
            if (this.af == null) {
                this.af = new ArrayList<View>();
            } else {
                this.af.clear();
            }
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                this.af.add(child);
            }
            Collections.sort(this.af, ag);
        }
    }

    // infoForCurrentScrollPosition
    private u i() {
        final int width = getClientWidth();
        final float scrollOffset = width > 0 ? (float) getScrollX() / width : 0;
        final float marginOffset = width > 0 ? (float) this.o / width : 0;
        int lastPos = -1;
        float lastOffset = 0.f;
        float lastWidth = 0.f;
        boolean first = true;

        u lastItem = null;
        for (int i = 0; i < this.e.size(); i++) {
            u ii = this.e.get(i);
            float offset;
            if (!first && ii.b != lastPos + 1) {
                // Create a synthetic item for a missing page.
                ii = this.f;
                ii.e = lastOffset + lastWidth + marginOffset;
                ii.b = lastPos + 1;
                ii.d = this.h.a(ii.b);
                i--;
            }
            offset = ii.e;

            final float leftBound = offset;
            final float rightBound = offset + ii.d + marginOffset;
            if (first || scrollOffset >= leftBound) {
                if (scrollOffset < rightBound || i == this.e.size() - 1) {
                    return ii;
                }
            } else {
                return lastItem;
            }
            first = false;
            lastPos = ii.b;
            lastOffset = offset;
            lastWidth = ii.d;
            lastItem = ii;
        }

        return lastItem;
    }

    // endDrag
    private void j() {
        this.A = false;
        this.B = false;

        if (this.K != null) {
            this.K.recycle();
            this.K = null;
        }
    }

    private void setScrollState(int newState) {
        if (this.ai == newState) {
            return;
        }

        this.ai = newState;
        if (this.ac != null) {
            // PageTransformers can do complex things that benefit from hardware layers.
            b(newState != 0);
        }
        if (this.W != null) {
            this.W.b(newState);
        }
    }

    private void setScrollingCacheEnabled(boolean var1) {
        if (this.x != var1) {
            this.x = var1;
        }
    }

    float a(float var1) {
        return (float)Math.sin((float)((var1 - 0.5F) * 0.4712389167638204));
    }

    // addNewItem
    u a(int position, int index) {
        u ii = new u();
        ii.b = position;
        ii.a = this.h.a(this, position);
        ii.d = this.h.a(position);
        if (index < 0 || index >= this.e.size()) {
            this.e.add(ii);
        } else {
            this.e.add(index, ii);
        }
        return ii;
    }

    // infoForChild
    u a(View child) {
        for (int i=0; i<this.e.size(); i++) {
            u ii = this.e.get(i);
            if (this.h.a(child, ii.a)) {
                return ii;
            }
        }
        return null;
    }

    void a() {
        this.setWillNotDraw(false);
        this.setDescendantFocusability(262144);
        this.setFocusable(true);
        Context var2 = this.getContext();
        this.m = new Scroller(var2, d);
        ViewConfiguration var3 = ViewConfiguration.get(var2);
        float var1 = var2.getResources().getDisplayMetrics().density;
        this.E = var3.getScaledTouchSlop() * 2;
        this.L = (int)(400.0F * var1);
        this.M = var3.getScaledMaximumFlingVelocity();
        this.Q = new android.support.v4.widget.j(var2);
        this.R = new android.support.v4.widget.j(var2);
        this.N = (int)(25.0F * var1);
        this.O = (int)(2.0F * var1);
        this.C = (int)(16.0F * var1);
    }

    // populate
    void a(int newCurrentItem) {
        u oldCurInfo = null;
        int focusDirection = View.FOCUS_FORWARD;
        if (this.i != newCurrentItem) {
            focusDirection = this.i < newCurrentItem ? View.FOCUS_RIGHT : View.FOCUS_LEFT;
            oldCurInfo = b(this.i);
            this.i = newCurrentItem;
        }

        if (this.h == null) {
            h();
            return;
        }

        // Bail now if we are waiting to populate.  This is to hold off
        // on creating views from the time the user releases their finger to
        // fling to a new position until we have finished the scroll to
        // that position, avoiding glitches from happening at that point.
        if (this.y) {
            h();
            return;
        }

        // Also, don't populate until we are attached to a window.  This is to
        // avoid trying to populate before we have restored our view hierarchy
        // state and conflicting with what is restored.
        if (getWindowToken() == null) {
            return;
        }

        this.h.a(this);

        final int pageLimit = this.z;
        final int startPos = Math.max(0, this.i - pageLimit);
        final int N = this.h.a();
        final int endPos = Math.min(N-1, this.i + pageLimit);

        if (N != this.b) {
            String resName;
            try {
                resName = getResources().getResourceName(getId());
            } catch (NotFoundException e) {
                resName = Integer.toHexString(getId());
            }
            throw new IllegalStateException("The application's PagerAdapter changed the adapter's" +
                    " contents without calling PagerAdapter#notifyDataSetChanged!" +
                    " Expected adapter item count: " + this.b + ", found: " + N +
                    " Pager id: " + resName +
                    " Pager class: " + getClass() +
                    " Problematic adapter: " + this.h.getClass());
        }

        // Locate the currently focused item or add it if needed.
        int curIndex = -1;
        u curItem = null;
        for (curIndex = 0; curIndex < this.e.size(); curIndex++) {
            final u ii = this.e.get(curIndex);
            if (ii.b >= this.i) {
                if (ii.b == this.i) curItem = ii;
                break;
            }
        }

        if (curItem == null && N > 0) {
            curItem = a(this.i, curIndex);
        }

        // Fill 3x the available width or up to the number of offscreen
        // pages requested to either side, whichever is larger.
        // If we have no current item we have no work to do.
        if (curItem != null) {
            float extraWidthLeft = 0.f;
            int itemIndex = curIndex - 1;
            u ii = itemIndex >= 0 ? this.e.get(itemIndex) : null;
            final int clientWidth = getClientWidth();
            final float leftWidthNeeded = clientWidth <= 0 ? 0 :
                    2.f - curItem.d + (float) getPaddingLeft() / (float) clientWidth;
            for (int pos = this.i - 1; pos >= 0; pos--) {
                if (extraWidthLeft >= leftWidthNeeded && pos < startPos) {
                    if (ii == null) {
                        break;
                    }
                    if (pos == ii.b && !ii.c) {
                        this.e.remove(itemIndex);
                        this.h.a(this, pos, ii.a);
                        itemIndex--;
                        curIndex--;
                        ii = itemIndex >= 0 ? this.e.get(itemIndex) : null;
                    }
                } else if (ii != null && pos == ii.b) {
                    extraWidthLeft += ii.d;
                    itemIndex--;
                    ii = itemIndex >= 0 ? this.e.get(itemIndex) : null;
                } else {
                    ii = a(pos, itemIndex + 1);
                    extraWidthLeft += ii.d;
                    curIndex++;
                    ii = itemIndex >= 0 ? this.e.get(itemIndex) : null;
                }
            }

            float extraWidthRight = curItem.d;
            itemIndex = curIndex + 1;
            if (extraWidthRight < 2.f) {
                ii = itemIndex < this.e.size() ? this.e.get(itemIndex) : null;
                final float rightWidthNeeded = clientWidth <= 0 ? 0 :
                        (float) getPaddingRight() / (float) clientWidth + 2.f;
                for (int pos = this.i + 1; pos < N; pos++) {
                    if (extraWidthRight >= rightWidthNeeded && pos > endPos) {
                        if (ii == null) {
                            break;
                        }
                        if (pos == ii.b && !ii.c) {
                            this.e.remove(itemIndex);
                            this.h.a(this, pos, ii.a);
                            ii = itemIndex < this.e.size() ? this.e.get(itemIndex) : null;
                        }
                    } else if (ii != null && pos == ii.b) {
                        extraWidthRight += ii.d;
                        itemIndex++;
                        ii = itemIndex < this.e.size() ? this.e.get(itemIndex) : null;
                    } else {
                        ii = a(pos, itemIndex);
                        itemIndex++;
                        extraWidthRight += ii.d;
                        ii = itemIndex < this.e.size() ? this.e.get(itemIndex) : null;
                    }
                }
            }

            a(curItem, curIndex, oldCurInfo);
        }


        this.h.b(this, this.i, curItem != null ? curItem.a : null);

        this.h.b(this);

        // Check width measurement of current pages and drawing sort order.
        // Update LayoutParams as needed.
        final int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = getChildAt(i);
            final v lp = (v) child.getLayoutParams();
            lp.f = i;
            if (!lp.a && lp.c == 0.f) {
                // 0 means requery the adapter for this, it doesn't have a valid width.
                final u ii = a(child);
                if (ii != null) {
                    lp.c = ii.d;
                    lp.e = ii.b;
                }
            }
        }
        h();

        if (hasFocus()) {
            View currentFocused = findFocus();
            u ii = currentFocused != null ? b(currentFocused) : null;
            if (ii == null || ii.b != this.i) {
                for (int i=0; i<getChildCount(); i++) {
                    View child = getChildAt(i);
                    ii = a(child);
                    if (ii != null && ii.b == this.i) {
                        if (child.requestFocus(focusDirection)) {
                            break;
                        }
                    }
                }
            }
        }
    }

    // onPageScrolled
    protected void a(int position, float offset, int offsetPixels) {
        // Offset any decor views if needed - keep them on-screen at all times.
        if (this.V > 0) {
            final int scrollX = getScrollX();
            int paddingLeft = getPaddingLeft();
            int paddingRight = getPaddingRight();
            final int width = getWidth();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final v lp = (v) child.getLayoutParams();
                if (!lp.a) continue;

                final int hgrav = lp.b & Gravity.HORIZONTAL_GRAVITY_MASK;
                int childLeft = 0;
                switch (hgrav) {
                    default:
                        childLeft = paddingLeft;
                        break;
                    case Gravity.LEFT:
                        childLeft = paddingLeft;
                        paddingLeft += child.getWidth();
                        break;
                    case Gravity.CENTER_HORIZONTAL:
                        childLeft = Math.max((width - child.getMeasuredWidth()) / 2,
                                paddingLeft);
                        break;
                    case Gravity.RIGHT:
                        childLeft = width - paddingRight - child.getMeasuredWidth();
                        paddingRight += child.getMeasuredWidth();
                        break;
                }
                childLeft += scrollX;

                final int childOffset = childLeft - child.getLeft();
                if (childOffset != 0) {
                    child.offsetLeftAndRight(childOffset);
                }
            }
        }

        if (this.W != null) {
            this.W.a(position, offset, offsetPixels);
        }
        if (this.aa != null) {
            this.aa.a(position, offset, offsetPixels);
        }

        if (this.ac != null) {
            final int scrollX = getScrollX();
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final v lp = (v) child.getLayoutParams();

                if (lp.a) continue;

                final float transformPos = (float) (child.getLeft() - scrollX) / getClientWidth();
                this.ac.a(child, transformPos);
            }
        }

        this.U = true;
    }

    void a(int var1, int var2, int var3) {
        if (this.getChildCount() == 0) {
            this.setScrollingCacheEnabled(false);
        } else {
            int var7 = this.getScrollX();
            int var8 = this.getScrollY();
            int var9 = var1 - var7;
            var2 -= var8;
            if (var9 == 0 && var2 == 0) {
                this.a(false);
                this.c();
                this.setScrollState(0);
            } else {
                this.setScrollingCacheEnabled(true);
                this.setScrollState(2);
                var1 = this.getClientWidth();
                int var10 = var1 / 2;
                float var6 = Math.min(1.0F, Math.abs(var9) * 1.0F / var1);
                float var5 = var10;
                float var4 = var10;
                var6 = this.a(var6);
                var3 = Math.abs(var3);
                if (var3 > 0) {
                    var1 = Math.round(1000.0F * Math.abs((var4 * var6 + var5) / var3)) * 4;
                } else {
                    var5 = var1;
                    var4 = this.h.a(this.i);
                    var1 = (int)((Math.abs(var9) / (var5 * var4 + this.o) + 1.0F) * 100.0F);
                }

                var1 = Math.min(var1, 600);
                this.m.startScroll(var7, var8, var9, var2, var1);
                c(this);
            }
        }
    }

    // setCurrentItem
    public void a(int item, boolean smoothScroll) {
        this.y = false;
        a(item, smoothScroll, false);
    }

    // setCurrentItemInternal
    void a(int item, boolean smoothScroll, boolean always) {
        a(item, smoothScroll, always, 0);
    }

    // setCurrentItemInternal
    void a(int item, boolean smoothScroll, boolean always, int velocity) {
        if (this.h == null || this.h.a() <= 0) {
            setScrollingCacheEnabled(false);
            return;
        }
        if (!always && this.i == item && this.e.size() != 0) {
            setScrollingCacheEnabled(false);
            return;
        }

        if (item < 0) {
            item = 0;
        } else if (item >= this.h.a()) {
            item = this.h.a() - 1;
        }
        final int pageLimit = this.z;
        if (item > (this.i + pageLimit) || item < (this.i - pageLimit)) {
            // We are doing a jump by more than one page.  To avoid
            // glitches, we want to keep all current pages in the view
            // until the scroll ends.
            for (int i=0; i<this.e.size(); i++) {
                this.e.get(i).c = true;
            }
        }
        final boolean dispatchSelected = this.i != item;

        if (this.S) {
            // We don't have any idea how big we are yet and shouldn't have any pages either.
            // Just set things up and let the pending layout handle things.
            this.i = item;
            if (dispatchSelected && this.W != null) {
                this.W.a(item);
            }
            if (dispatchSelected && this.aa != null) {
                this.aa.a(item);
            }
            requestLayout();
        } else {
            a(item);
            a(item, smoothScroll, velocity, dispatchSelected);
        }
    }

    public boolean a(KeyEvent var1) {
        boolean var3 = false;
        boolean var2 = var3;
        if (var1.getAction() == 0) {
            switch (var1.getKeyCode()) {
                case 21:
                    var2 = this.c(17);
                    break;
                case 22:
                    var2 = this.c(66);
                    break;
                case 61:
                    var2 = var3;
                    if (VERSION.SDK_INT >= 11) {
                        if (var1.hasNoModifiers()) {
                            var2 = this.c(2);
                        } else {
                            var2 = var3;
                            if (var1.hasModifiers(1)) {
                                var2 = this.c(1);
                            }
                        }
                    }
                    break;
                default:
                    var2 = var3;
            }
        }

        return var2;
    }

    protected boolean a(View var1, boolean var2, int var3, int var4, int var5) {
        if (var1 instanceof ViewGroup) {
            ViewGroup var10 = (ViewGroup)var1;
            int var8 = var1.getScrollX();
            int var7 = var1.getScrollY();

            for (int var6 = var10.getChildCount() - 1; var6 >= 0; var6--) {
                View var9 = var10.getChildAt(var6);
                if (var4 + var8 >= var9.getLeft()
                    && var4 + var8 < var9.getRight()
                    && var5 + var7 >= var9.getTop()
                    && var5 + var7 < var9.getBottom()
                    && this.a(var9, true, var3, var4 + var8 - var9.getLeft(), var5 + var7 - var9.getTop())) {
                    var2 = true;
                    return var2;
                }
            }
        }

        if (VERSION.SDK_INT >= 14) {
            if (var2 && var1.canScrollHorizontally(-var3)) {
                var2 = true;
            } else {
                var2 = false;
            }
        } else {
            var2 = false;
        }

        return var2;
    }

    @Override
    public void addFocusables(ArrayList var1, int var2, int var3) {
        int var5 = var1.size();
        int var6 = this.getDescendantFocusability();
        if (var6 != 393216) {
            for (int var4 = 0; var4 < this.getChildCount(); var4++) {
                View var7 = this.getChildAt(var4);
                if (var7.getVisibility() == 0) {
                    u var8 = this.a(var7);
                    if (var8 != null && var8.b == this.i) {
                        var7.addFocusables(var1, var2, var3);
                    }
                }
            }
        }

        if ((var6 != 262144 || var5 == var1.size())
            && this.isFocusable()
            && ((var3 & 1) != 1 || !this.isInTouchMode() || this.isFocusableInTouchMode())
            && var1 != null) {
            var1.add(this);
        }
    }

    @Override
    public void addTouchables(ArrayList var1) {
        for (int var2 = 0; var2 < this.getChildCount(); var2++) {
            View var3 = this.getChildAt(var2);
            if (var3.getVisibility() == 0) {
                u var4 = this.a(var3);
                if (var4 != null && var4.b == this.i) {
                    var3.addTouchables(var1);
                }
            }
        }
    }

    @Override
    public void addView(View var1, int var2, LayoutParams var3) {
        if (!this.checkLayoutParams(var3)) {
            var3 = this.generateLayoutParams(var3);
        }

        v var4 = (v)var3;
        var4.a |= var1 instanceof t;
        if (this.w) {
            if (var4 != null && var4.a) {
                throw new IllegalStateException("Cannot add pager decor view during layout");
            }

            var4.d = true;
            this.addViewInLayout(var1, var2, var3);
        } else {
            super.addView(var1, var2, var3);
        }
    }

    // infoForPosition
    u b(int position) {
        for (int i = 0; i < this.e.size(); i++) {
            u ii = this.e.get(i);
            if (ii.b == position) {
                return ii;
            }
        }
        return null;
    }

    // infoForAnyChild
    u b(View child) {
        ViewParent parent;
        while ((parent=child.getParent()) != this) {
            if (parent == null || !(parent instanceof View)) {
                return null;
            }
            child = (View)parent;
        }
        return a(child);
    }

    // dataSetChanged
    void b() {
        // This method only gets called if our observer is attached, so mAdapter is non-null.

        final int adapterCount = this.h.a();
        this.b = adapterCount;
        boolean needPopulate = this.e.size() < this.z * 2 + 1 &&
                this.e.size() < adapterCount;
        int newCurrItem = this.i;

        boolean isUpdating = false;
        for (int i = 0; i < this.e.size(); i++) {
            final u ii = this.e.get(i);
            final int newPos = this.h.a(ii.a);

            if (newPos == -1) {
                continue;
            }

            if (newPos == -2) {
                this.e.remove(i);
                i--;

                if (!isUpdating) {
                    this.h.a(this);
                    isUpdating = true;
                }

                this.h.a(this, ii.b, ii.a);
                needPopulate = true;

                if (this.i == ii.b) {
                    // Keep the current item in the valid range
                    newCurrItem = Math.max(0, Math.min(this.i, adapterCount - 1));
                    needPopulate = true;
                }
                continue;
            }

            if (ii.b != newPos) {
                if (ii.b == this.i) {
                    // Our current item changed position. Follow it.
                    newCurrItem = newPos;
                }

                ii.b = newPos;
                needPopulate = true;
            }
        }

        if (isUpdating) {
            this.h.b(this);
        }

        Collections.sort(this.e, c);

        if (needPopulate) {
            // Reset our known page widths; populate will recompute them.
            final int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = getChildAt(i);
                final v lp = (v) child.getLayoutParams();
                if (!lp.a) {
                    lp.c = 0.f;
                }
            }

            a(newCurrItem, false, true);
            requestLayout();
        }
    }

    // populate
    void c() {
        a(this.i);
    }

    // arrowScroll
    public boolean c(int direction) {
        View currentFocused = findFocus();
        if (currentFocused == this) {
            currentFocused = null;
        } else if (currentFocused != null) {
            boolean isChild = false;
            for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
                    parent = parent.getParent()) {
                if (parent == this) {
                    isChild = true;
                    break;
                }
            }
            if (!isChild) {
                // This would cause the focus search down below to fail in fun ways.
                final StringBuilder sb = new StringBuilder();
                sb.append(currentFocused.getClass().getSimpleName());
                for (ViewParent parent = currentFocused.getParent(); parent instanceof ViewGroup;
                        parent = parent.getParent()) {
                    sb.append(" => ").append(parent.getClass().getSimpleName());
                }
                Log.e("ViewPager", "arrowScroll tried to find focus based on non-child " +
                        "current focused view " + sb.toString());
                currentFocused = null;
            }
        }

        boolean handled = false;

        View nextFocused = FocusFinder.getInstance().findNextFocus(this, currentFocused,
                direction);
        if (nextFocused != null && nextFocused != currentFocused) {
            if (direction == View.FOCUS_LEFT) {
                // If there is nothing to the left, or this is causing us to
                // jump to the right, then what we really want to do is page left.
                final int nextLeft = a(this.g, nextFocused).left;
                final int currLeft = a(this.g, currentFocused).left;
                if (currentFocused != null && nextLeft >= currLeft) {
                    handled = d();
                } else {
                    handled = nextFocused.requestFocus();
                }
            } else if (direction == View.FOCUS_RIGHT) {
                // If there is nothing to the right, or this is causing us to
                // jump to the left, then what we really want to do is page right.
                final int nextLeft = a(this.g, nextFocused).left;
                final int currLeft = a(this.g, currentFocused).left;
                if (currentFocused != null && nextLeft <= currLeft) {
                    handled = e();
                } else {
                    handled = nextFocused.requestFocus();
                }
            }
        } else if (direction == FOCUS_LEFT || direction == FOCUS_BACKWARD) {
            // Trying to move left and nothing there; try to page.
            handled = d();
        } else if (direction == FOCUS_RIGHT || direction == FOCUS_FORWARD) {
            // Trying to move right and nothing there; try to page.
            handled = e();
        }
        if (handled) {
            playSoundEffect(SoundEffectConstants.getContantForFocusDirection(direction));
        }
        return handled;
    }

    @Override
    public boolean canScrollHorizontally(int direction) {
        if (this.h == null) {
            return false;
        }

        final int width = getClientWidth();
        final int scrollX = getScrollX();
        if (direction < 0) {
            return (scrollX > (int) (width * this.s));
        } else if (direction > 0) {
            return (scrollX < (int) (width * this.t));
        } else {
            return false;
        }
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams var1) {
        boolean var2;
        if (var1 instanceof v && super.checkLayoutParams(var1)) {
            var2 = true;
        } else {
            var2 = false;
        }

        return var2;
    }

    @Override
    public void computeScroll() {
        if (!this.m.isFinished() && this.m.computeScrollOffset()) {
            int oldX = getScrollX();
            int oldY = getScrollY();
            int x = this.m.getCurrX();
            int y = this.m.getCurrY();

            if (oldX != x || oldY != y) {
                scrollTo(x, y);
                if (!d(x)) {
                    this.m.abortAnimation();
                    scrollTo(0, y);
                }
            }

            // Keep on drawing until the animation has finished.
            c(this);
            return;
        }

        // Done with scroll, clean up state.
        a(true);
    }

    // pageLeft
    boolean d() {
        if (this.i > 0) {
            a(this.i-1, true);
            return true;
        }
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Let the focused view and/or our descendants get the key first
        return super.dispatchKeyEvent(event) || a(event);
    }

    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent var1) {
        boolean var5 = false;
        int var3 = this.getChildCount();
        int var2 = 0;

        boolean var4;
        while (true) {
            var4 = var5;
            if (var2 >= var3) {
                break;
            }

            View var7 = this.getChildAt(var2);
            if (var7.getVisibility() == 0) {
                u var6 = this.a(var7);
                if (var6 != null && var6.b == this.i && var7.dispatchPopulateAccessibilityEvent(var1)) {
                    var4 = true;
                    break;
                }
            }

            var2++;
        }

        return var4;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        boolean needsInvalidate = false;

        if (!this.Q.a()) {
            final int restoreCount = canvas.save();
            final int height = getHeight() - getPaddingTop() - getPaddingBottom();
            final int width = getWidth();

            canvas.rotate(270);
            canvas.translate(-height + getPaddingTop(), this.s * width);
            this.Q.a(height, width);
            needsInvalidate |= this.Q.a(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (!this.R.a()) {
            final int restoreCount = canvas.save();
            final int width = getWidth();
            final int height = getHeight() - getPaddingTop() - getPaddingBottom();

            canvas.rotate(90);
            canvas.translate(-getPaddingTop(), -(this.t + 1) * width);
            this.R.a(height, width);
            needsInvalidate |= this.R.a(canvas);
            canvas.restoreToCount(restoreCount);
        }
        if (needsInvalidate) {
            // Keep animating
            c(this);
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        final Drawable d = this.p;
        if (d != null && d.isStateful()) {
            d.setState(getDrawableState());
        }
    }

    // pageRight
    boolean e() {
        if (this.h != null && this.i < (this.h.a()-1)) {
            a(this.i+1, true);
            return true;
        }
        return false;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new v();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new v(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams var1) {
        return this.generateDefaultLayoutParams();
    }

    public j getAdapter() {
        return this.h;
    }

    @Override
    protected int getChildDrawingOrder(int var1, int var2) {
        int var3 = var2;
        if (this.ae == 2) {
            var3 = var1 - 1 - var2;
        }

        return ((v)((View)this.af.get(var3)).getLayoutParams()).f;
    }

    public int getCurrentItem() {
        return this.i;
    }

    public int getOffscreenPageLimit() {
        return this.z;
    }

    public int getPageMargin() {
        return this.o;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.S = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        removeCallbacks(this.ah);
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the margin drawable between pages if needed.
        if (this.o > 0 && this.p != null && this.e.size() > 0 && this.h != null) {
            final int scrollX = getScrollX();
            final int width = getWidth();

            final float marginOffset = (float) this.o / width;
            int itemIndex = 0;
            u ii = this.e.get(0);
            float offset = ii.e;
            final int itemCount = this.e.size();
            final int firstPos = ii.b;
            final int lastPos = this.e.get(itemCount - 1).b;
            for (int pos = firstPos; pos < lastPos; pos++) {
                while (pos > ii.b && itemIndex < itemCount) {
                    ii = this.e.get(++itemIndex);
                }

                float drawAt;
                if (pos == ii.b) {
                    drawAt = (ii.e + ii.d) * width;
                    offset = ii.e + ii.d + marginOffset;
                } else {
                    float widthFactor = this.h.a(pos);
                    drawAt = (offset + widthFactor) * width;
                    offset += widthFactor + marginOffset;
                }

                if (drawAt + this.o > scrollX) {
                    this.p.setBounds((int) drawAt, this.q,
                            (int) (drawAt + this.o + 0.5f), this.r);
                    this.p.draw(canvas);
                }

                if (drawAt > scrollX + width) {
                    break; // No more visible, no sense in continuing
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        /*
         * This method JUST determines whether we want to intercept the motion.
         * If we return true, onMotionEvent will be called and we do the actual
         * scrolling there.
         */

        final int action = ev.getAction() & MotionEvent.ACTION_MASK;

        // Always take care of the touch gesture being complete.
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            // Release the drag.
            this.A = false;
            this.B = false;
            this.J = -1;
            if (this.K != null) {
                this.K.recycle();
                this.K = null;
            }
            return false;
        }

        // Nothing more to do here if we have decided whether or not we
        // are dragging.
        if (action != MotionEvent.ACTION_DOWN) {
            if (this.A) {
                return true;
            }
            if (this.B) {
                return false;
            }
        }

        switch (action) {
            case MotionEvent.ACTION_MOVE: {
                /*
                 * mIsBeingDragged == false, otherwise the shortcut would have caught it. Check
                 * whether the user has moved far enough from his original down touch.
                 */

                /*
                * Locally do absolute value. mLastMotionY is set to the y value
                * of the down event.
                */
                final int activePointerId = this.J;
                if (activePointerId == -1) {
                    // If we don't have a valid id, the touch down wasn't on content.
                    break;
                }

                final int pointerIndex = android.support.v4.a.f.a(ev, activePointerId);
                final float x = android.support.v4.a.f.c(ev, pointerIndex);
                final float dx = x - this.F;
                final float xDiff = Math.abs(dx);
                final float y = android.support.v4.a.f.d(ev, pointerIndex);
                final float yDiff = Math.abs(y - this.I);

                if (dx != 0 && !a(this.F, dx) &&
                        a(this, false, (int) dx, (int) x, (int) y)) {
                    // Nested view has scrollable area under this point. Let it be handled there.
                    this.F = x;
                    this.G = y;
                    this.B = true;
                    return false;
                }
                if (xDiff > this.E && xDiff * 0.5f > yDiff) {
                    this.A = true;
                    c(true);
                    setScrollState(1);
                    this.F = dx > 0 ? this.H + this.E :
                            this.H - this.E;
                    this.G = y;
                    setScrollingCacheEnabled(true);
                } else if (yDiff > this.E) {
                    // The finger has moved enough in the vertical
                    // direction to be counted as a drag...  abort
                    // any attempt to drag horizontally, to work correctly
                    // with children that have scrolling containers.
                    this.B = true;
                }
                if (this.A) {
                    // Scroll to follow the motion event
                    if (b(x)) {
                        c(this);
                    }
                }
                break;
            }

            case MotionEvent.ACTION_DOWN: {
                /*
                 * Remember location of down touch.
                 * ACTION_DOWN always refers to pointer index 0.
                 */
                this.F = this.H = ev.getX();
                this.G = this.I = ev.getY();
                this.J = android.support.v4.a.f.b(ev, 0);
                this.B = false;

                this.m.computeScrollOffset();
                if (this.ai == 2 &&
                        Math.abs(this.m.getFinalX() - this.m.getCurrX()) > this.O) {
                    // Let the user 'catch' the pager as it animates.
                    this.m.abortAnimation();
                    this.y = false;
                    c();
                    this.A = true;
                    c(true);
                    setScrollState(1);
                } else {
                    a(false);
                    this.A = false;
                }

                break;
            }

            case MotionEvent.ACTION_POINTER_UP:
                a(ev);
                break;
        }

        if (this.K == null) {
            this.K = VelocityTracker.obtain();
        }
        this.K.addMovement(ev);

        /*
         * The only time we want to intercept motion events is if we are in the
         * drag mode.
         */
        return this.A;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int width = r - l;
        int height = b - t;
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        final int scrollX = getScrollX();

        int decorCount = 0;

        // First pass - decor views. We need to do this in two passes so that
        // we have the proper offsets for non-decor views later.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final v lp = (v) child.getLayoutParams();
                int childLeft = 0;
                int childTop = 0;
                if (lp.a) {
                    final int hgrav = lp.b & Gravity.HORIZONTAL_GRAVITY_MASK;
                    final int vgrav = lp.b & Gravity.VERTICAL_GRAVITY_MASK;
                    switch (hgrav) {
                        default:
                            childLeft = paddingLeft;
                            break;
                        case Gravity.LEFT:
                            childLeft = paddingLeft;
                            paddingLeft += child.getMeasuredWidth();
                            break;
                        case Gravity.CENTER_HORIZONTAL:
                            childLeft = Math.max((width - child.getMeasuredWidth()) / 2,
                                    paddingLeft);
                            break;
                        case Gravity.RIGHT:
                            childLeft = width - paddingRight - child.getMeasuredWidth();
                            paddingRight += child.getMeasuredWidth();
                            break;
                    }
                    switch (vgrav) {
                        default:
                            childTop = paddingTop;
                            break;
                        case Gravity.TOP:
                            childTop = paddingTop;
                            paddingTop += child.getMeasuredHeight();
                            break;
                        case Gravity.CENTER_VERTICAL:
                            childTop = Math.max((height - child.getMeasuredHeight()) / 2,
                                    paddingTop);
                            break;
                        case Gravity.BOTTOM:
                            childTop = height - paddingBottom - child.getMeasuredHeight();
                            paddingBottom += child.getMeasuredHeight();
                            break;
                    }
                    childLeft += scrollX;
                    child.layout(childLeft, childTop,
                            childLeft + child.getMeasuredWidth(),
                            childTop + child.getMeasuredHeight());
                    decorCount++;
                }
            }
        }

        final int childWidth = width - paddingLeft - paddingRight;
        // Page views. Do this once we have the right padding offsets from above.
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final v lp = (v) child.getLayoutParams();
                u ii;
                if (!lp.a && (ii = a(child)) != null) {
                    int loff = (int) (childWidth * ii.e);
                    int childLeft = paddingLeft + loff;
                    int childTop = paddingTop;
                    if (lp.d) {
                        // This was added during layout and needs measurement.
                        // Do it now that we know what we're working with.
                        lp.d = false;
                        final int widthSpec = MeasureSpec.makeMeasureSpec(
                                (int) (childWidth * lp.c),
                                MeasureSpec.EXACTLY);
                        final int heightSpec = MeasureSpec.makeMeasureSpec(
                                (int) (height - paddingTop - paddingBottom),
                                MeasureSpec.EXACTLY);
                        child.measure(widthSpec, heightSpec);
                    }
                    child.layout(childLeft, childTop,
                            childLeft + child.getMeasuredWidth(),
                            childTop + child.getMeasuredHeight());
                }
            }
        }
        this.q = paddingTop;
        this.r = height - paddingBottom;
        this.V = decorCount;

        if (this.S) {
            a(this.i, false, 0, false);
        }
        this.S = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // For simple implementation, our internal size is always 0.
        // We depend on the container to specify the layout size of
        // our view.  We can't really know what it is since we will be
        // adding and removing different arbitrary views and do not
        // want the layout to change as this happens.
        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec),
                getDefaultSize(0, heightMeasureSpec));

        final int measuredWidth = getMeasuredWidth();
        final int maxGutterSize = measuredWidth / 10;
        this.D = Math.min(maxGutterSize, this.C);

        // Children are just made to fill our space.
        int childWidthSize = measuredWidth - getPaddingLeft() - getPaddingRight();
        int childHeightSize = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();

        /*
         * Make sure all children have been properly measured. Decor views first.
         * Right now we cheat and make this less complicated by assuming decor
         * views won't intersect. We will pin to edges based on gravity.
         */
        int size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                final v lp = (v) child.getLayoutParams();
                if (lp != null && lp.a) {
                    final int hgrav = lp.b & Gravity.HORIZONTAL_GRAVITY_MASK;
                    final int vgrav = lp.b & Gravity.VERTICAL_GRAVITY_MASK;
                    int widthMode = MeasureSpec.AT_MOST;
                    int heightMode = MeasureSpec.AT_MOST;
                    boolean consumeVertical = vgrav == Gravity.TOP || vgrav == Gravity.BOTTOM;
                    boolean consumeHorizontal = hgrav == Gravity.LEFT || hgrav == Gravity.RIGHT;

                    if (consumeVertical) {
                        widthMode = MeasureSpec.EXACTLY;
                    } else if (consumeHorizontal) {
                        heightMode = MeasureSpec.EXACTLY;
                    }

                    int widthSize = childWidthSize;
                    int heightSize = childHeightSize;
                    if (lp.width != LayoutParams.WRAP_CONTENT) {
                        widthMode = MeasureSpec.EXACTLY;
                        if (lp.width != LayoutParams.MATCH_PARENT) {
                            widthSize = lp.width;
                        }
                    }
                    if (lp.height != LayoutParams.WRAP_CONTENT) {
                        heightMode = MeasureSpec.EXACTLY;
                        if (lp.height != LayoutParams.MATCH_PARENT) {
                            heightSize = lp.height;
                        }
                    }
                    final int widthSpec = MeasureSpec.makeMeasureSpec(widthSize, widthMode);
                    final int heightSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                    child.measure(widthSpec, heightSpec);

                    if (consumeVertical) {
                        childHeightSize -= child.getMeasuredHeight();
                    } else if (consumeHorizontal) {
                        childWidthSize -= child.getMeasuredWidth();
                    }
                }
            }
        }

        this.u = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
        this.v = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);

        // Make sure we have created all fragments that we need to have shown.
        this.w = true;
        c();
        this.w = false;

        // Page views next.
        size = getChildCount();
        for (int i = 0; i < size; ++i) {
            final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {

                final v lp = (v) child.getLayoutParams();
                if (lp == null || !lp.a) {
                    final int widthSpec = MeasureSpec.makeMeasureSpec(
                            (int) (childWidthSize * lp.c), MeasureSpec.EXACTLY);
                    child.measure(widthSpec, this.v);
                }
            }
        }
    }

    @Override
    protected boolean onRequestFocusInDescendants(int direction,
            Rect previouslyFocusedRect) {
        int index;
        int increment;
        int end;
        int count = getChildCount();
        if ((direction & FOCUS_FORWARD) != 0) {
            index = 0;
            increment = 1;
            end = count;
        } else {
            index = count - 1;
            increment = -1;
            end = -1;
        }
        for (int i = index; i != end; i += increment) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                u ii = a(child);
                if (ii != null && ii.b == this.i) {
                    if (child.requestFocus(direction, previouslyFocusedRect)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Make sure scroll position is set correctly.
        if (w != oldw) {
            a(w, oldw, this.o, this.o);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (this.P) {
            // A fake drag is in progress already, ignore this real one
            // but still eat the touch events.
            // (It is likely that the user is multi-touching the screen.)
            return true;
        }

        if (ev.getAction() == MotionEvent.ACTION_DOWN && ev.getEdgeFlags() != 0) {
            // Don't handle edge touches immediately -- they may actually belong to one of our
            // descendants.
            return false;
        }

        if (this.h == null || this.h.a() == 0) {
            // Nothing to present or scroll; nothing to touch.
            return false;
        }

        if (this.K == null) {
            this.K = VelocityTracker.obtain();
        }
        this.K.addMovement(ev);

        final int action = ev.getAction();
        boolean needsInvalidate = false;

        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                this.m.abortAnimation();
                this.y = false;
                c();

                // Remember where the motion event started
                this.F = this.H = ev.getX();
                this.G = this.I = ev.getY();
                this.J = android.support.v4.a.f.b(ev, 0);
                break;
            }
            case MotionEvent.ACTION_MOVE:
                if (!this.A) {
                    final int pointerIndex = android.support.v4.a.f.a(ev, this.J);
                    final float x = android.support.v4.a.f.c(ev, pointerIndex);
                    final float xDiff = Math.abs(x - this.F);
                    final float y = android.support.v4.a.f.d(ev, pointerIndex);
                    final float yDiff = Math.abs(y - this.G);
                    if (xDiff > this.E && xDiff > yDiff) {
                        this.A = true;
                        c(true);
                        this.F = x - this.H > 0 ? this.H + this.E :
                                this.H - this.E;
                        this.G = y;
                        setScrollState(1);
                        setScrollingCacheEnabled(true);

                        // Disallow Parent Intercept, just in case
                        ViewParent parent = getParent();
                        if (parent != null) {
                            parent.requestDisallowInterceptTouchEvent(true);
                        }
                    }
                }
                // Not else! Note that mIsBeingDragged can be set above.
                if (this.A) {
                    // Scroll to follow the motion event
                    final int activePointerIndex = android.support.v4.a.f.a(
                            ev, this.J);
                    final float x = android.support.v4.a.f.c(ev, activePointerIndex);
                    needsInvalidate |= b(x);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (this.A) {
                    final VelocityTracker velocityTracker = this.K;
                    velocityTracker.computeCurrentVelocity(1000, this.M);
                    int initialVelocity = (int) android.support.v4.a.k.a(
                            velocityTracker, this.J);
                    this.y = true;
                    final int width = getClientWidth();
                    final int scrollX = getScrollX();
                    final u ii = i();
                    final int currentPage = ii.b;
                    final float pageOffset = (((float) scrollX / width) - ii.e) / ii.d;
                    final int activePointerIndex =
                            android.support.v4.a.f.a(ev, this.J);
                    final float x = android.support.v4.a.f.c(ev, activePointerIndex);
                    final int totalDelta = (int) (x - this.H);
                    int nextPage = a(currentPage, pageOffset, initialVelocity,
                            totalDelta);
                    a(nextPage, true, true, initialVelocity);

                    this.J = -1;
                    j();
                    needsInvalidate = this.Q.b() | this.R.b();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                if (this.A) {
                    a(this.i, true, 0, false);
                    this.J = -1;
                    j();
                    needsInvalidate = this.Q.b() | this.R.b();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN: {
                final int index = android.support.v4.a.f.b(ev);
                final float x = android.support.v4.a.f.c(ev, index);
                this.F = x;
                this.J = android.support.v4.a.f.b(ev, index);
                break;
            }
            case MotionEvent.ACTION_POINTER_UP:
                a(ev);
                this.F = android.support.v4.a.f.c(ev,
                        android.support.v4.a.f.a(ev, this.J));
                break;
        }
        if (needsInvalidate) {
            c(this);
        }
        return true;
    }

    @Override
    public void removeView(View view) {
        if (this.w) {
            removeViewInLayout(view);
        } else {
            super.removeView(view);
        }
    }

    public void setAdapter(j var1) {
        if (this.h != null) {
            this.h.b(this.n);
            this.h.a((ViewGroup)this);

            for (int var2 = 0; var2 < this.e.size(); var2++) {
                u var4 = (u)this.e.get(var2);
                this.h.a((ViewGroup)this, var4.b, var4.a);
            }

            this.h.b((ViewGroup)this);
            this.e.clear();
            this.g();
            this.i = 0;
            this.scrollTo(0, 0);
        }

        j var5 = this.h;
        this.h = var1;
        this.b = 0;
        if (this.h != null) {
            if (this.n == null) {
                this.n = new z(this, null);
            }

            this.h.a((DataSetObserver)this.n);
            this.y = false;
            boolean var3 = this.S;
            this.S = true;
            this.b = this.h.a();
            if (this.j >= 0) {
                this.h.a(this.k, this.l);
                this.a(this.j, false, true);
                this.j = -1;
                this.k = null;
                this.l = null;
            } else if (!var3) {
                this.c();
            } else {
                this.requestLayout();
            }
        }

        if (this.ab != null && var5 != var1) {
            this.ab.a(var5, var1);
        }
    }

    void setChildrenDrawingOrderEnabledCompat(boolean var1) {
        if (VERSION.SDK_INT >= 7) {
            if (this.ad == null) {
                try {
                    this.ad = ViewGroup.class.getDeclaredMethod("setChildrenDrawingOrderEnabled", boolean.class);
                } catch (NoSuchMethodException var4) {
                    Log.e("ViewPager", "Can't find setChildrenDrawingOrderEnabled", var4);
                }
            }

            try {
                this.ad.invoke(this, var1);
            } catch (Exception var3) {
                Log.e("ViewPager", "Error changing children drawing order", var3);
            }
        }
    }

    public void setCurrentItem(int var1) {
        this.y = false;
        boolean var2;
        if (!this.S) {
            var2 = true;
        } else {
            var2 = false;
        }

        this.a(var1, var2, false);
    }

    public void setOffscreenPageLimit(int limit) {
        if (limit < 1) {
            Log.w("ViewPager", "Requested offscreen page limit " + limit + " too small; defaulting to " +
                    1);
            limit = 1;
        }
        if (limit != this.z) {
            this.z = limit;
            c();
        }
    }

    void setOnAdapterChangeListener(w listener) {
        this.ab = listener;
    }

    public void setOnPageChangeListener(x listener) {
        this.W = listener;
    }

    public void setPageMargin(int marginPixels) {
        final int oldMargin = this.o;
        this.o = marginPixels;

        final int width = getWidth();
        a(width, width, marginPixels, oldMargin);

        requestLayout();
    }

    public void setPageMarginDrawable(int var1) {
        this.setPageMarginDrawable(this.getContext().getResources().getDrawable(var1));
    }

    public void setPageMarginDrawable(Drawable d) {
        this.p = d;
        if (d != null) refreshDrawableState();
        setWillNotDraw(d == null);
        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || who == this.p;
    }
}
