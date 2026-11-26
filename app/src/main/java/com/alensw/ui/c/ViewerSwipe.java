package com.alensw.ui.c;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ScrollView;
import android.widget.TableLayout;
import com.alensw.PicFolder.R;
import com.alensw.ui.view.PictureView;
import com.alensw.ui.view.w;

final class ViewerSwipe implements w.VerticalDragListener {
    interface DetailsSource {
        df currentPictureDetails();
    }

    private static final int WAITING_FOR_DIRECTION = 1;
    private static final int CLOSING_DRAG = 2;
    private static final int PAGING_DRAG = 3;
    private static final String SHOW_DETAILS = "details";
    private static final String RETURN_TO_LIST = "back";

    private final cu viewer;
    private final PictureView picture;
    private final DetailsSource source;
    private final SharedPreferences preferences;
    private final View overlay;
    private final View sheet;
    private final ScrollView scrollView;
    private final TableLayout rows;
    private final float density;
    private final int touchSlop;
    private int width;
    private int height;
    private int sheetHeight;
    private float offsetY;
    private ValueAnimator animation;
    private boolean returningFromSwipe;
    private boolean detailsOpen;
    private boolean detailsClosing;
    private Uri pictureUri = Uri.EMPTY;
    private String dragAction;
    private boolean dragUp;
    private int detailsGesture;
    private float detailsStartX;
    private float detailsStartY;
    private float pageDragX;

    ViewerSwipe(cu viewer, PictureView picture, DetailsSource source) {
        this.viewer = viewer;
        this.picture = picture;
        this.source = source;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(picture.getContext());
        this.density = picture.getResources().getDisplayMetrics().density;
        this.touchSlop = ViewConfiguration.get(picture.getContext()).getScaledTouchSlop();
        this.overlay = viewer.f(R.id.viewer_details_overlay);
        this.sheet = overlay.findViewById(R.id.viewer_details_sheet);
        this.rows = (TableLayout) sheet.findViewById(R.id.items);
        this.scrollView = (ScrollView) rows.getParent();
        overlay.setSaveFromParentEnabled(false);
        picture.setVerticalDragListener(this);
        picture.setOnTouchListener((view, event) -> {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN && animation != null) {
                reset();
            }
            return false;
        });
        overlay.setOnTouchListener(this::onDetailsTouch);
        overlay.setOnClickListener(view -> closeDetails());
        sheet.findViewById(android.R.id.title).setOnTouchListener(this::onDetailsTouch);
        sheet.findViewById(android.R.id.button1).setOnClickListener(view -> closeDetails());
    }

    void layout(int width, int height, Rect insets) {
        if (this.width != width || this.height != height) {
            this.width = width;
            this.height = height;
            sheetHeight = height / 2;
            ViewGroup.LayoutParams layout = sheet.getLayoutParams();
            layout.height = sheetHeight;
            sheet.setLayoutParams(layout);
            if (detailsOpen && !detailsClosing) {
                cancelDetailsGesture();
                cancelAnimation();
                setOffset(openOffset());
            } else {
                reset();
            }
        }
        sheet.setPadding(insets.left, 0, insets.right, insets.bottom);
    }

    void pictureChanged(Uri uri) {
        if (uri.equals(pictureUri)) {
            return;
        }
        pictureUri = uri;
        df details = detailsOpen && !detailsClosing ? source.currentPictureDetails() : null;
        if (details != null) {
            cancelDetailsGesture();
            displayDetails(details);
        } else {
            reset();
        }
    }

    void reset() {
        if (returningFromSwipe) {
            return; // The return animation still needs the last drag position.
        }
        cancelAnimation();
        picture.clearVerticalDrag();
        cancelDetailsGesture();
        dragAction = null;
        hideDetails();
        setOffset(0.0F);
    }

    @Override
    public boolean onVerticalDragStart(float distanceY) {
        boolean up = distanceY < 0.0F;
        String action = preferences.getString(up ? "viewer_swipe_up" : "viewer_swipe_down", "none");
        if ((!SHOW_DETAILS.equals(action) && !RETURN_TO_LIST.equals(action))
                || sheetHeight <= 0 || !picture.isPictureAtFit() || currentDetails() == null) {
            return false;
        }
        dragAction = action;
        dragUp = up;
        return true;
    }

    @Override
    public void onVerticalDrag(float distanceY) {
        if (dragUp) {
            float limit = SHOW_DETAILS.equals(dragAction) ? openOffset() : -height;
            setOffset(Math.max(limit, Math.min(0.0F, distanceY)));
        } else {
            setOffset(Math.max(0.0F, distanceY));
        }
    }

    @Override
    public void onVerticalDragEnd(float distanceY) {
        onVerticalDrag(distanceY);
        df details = currentDetails();
        float travel = dragUp ? -distanceY : distanceY;
        if (details == null || travel < swipeThreshold()) {
            settleTo(0.0F, null);
        } else if (SHOW_DETAILS.equals(dragAction)) {
            detailsOpen = true;
            viewer.C.setKeepNavigationButtonsVisible(true);
            displayDetails(details);
            settleTo(openOffset(), null);
        } else if (viewer.C.d() > 1) {
            returnFromViewer();
        } else {
            settleTo(dragUp ? -height : height, this::returnFromViewer);
        }
    }

    @Override
    public void onVerticalDragCancel(boolean animate) {
        if (animate) {
            settleTo(0.0F, null);
        } else {
            setOffset(0.0F); // A second finger can now zoom about the original image.
        }
    }

    boolean closeDetails() {
        if (!detailsOpen) {
            return false;
        }
        if (!detailsClosing) {
            detailsClosing = true;
            cancelDetailsGesture();
            settleTo(0.0F, this::hideDetails);
        }
        return true;
    }

    private boolean onDetailsTouch(View view, MotionEvent event) {
        float distanceX = event.getRawX() - detailsStartX;
        float distanceY = event.getRawY() - detailsStartY;
        boolean moved = Math.max(Math.abs(distanceX), Math.abs(distanceY)) > touchSlop;
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                detailsStartX = event.getRawX();
                detailsStartY = event.getRawY();
                detailsGesture = detailsOpen && !detailsClosing ? WAITING_FOR_DIRECTION : 0;
                break;
            case MotionEvent.ACTION_MOVE:
                if (detailsGesture == WAITING_FOR_DIRECTION && moved) {
                    detailsGesture = 0;
                    if (animation == null) {
                        if (distanceY > 1.4F * Math.abs(distanceX)) {
                            detailsGesture = CLOSING_DRAG;
                        } else if (view == overlay && Math.abs(distanceX) > 1.4F * Math.abs(distanceY)
                                && picture.beginDetailsPageDrag()) {
                            detailsGesture = PAGING_DRAG;
                            pageDragX = 0.0F;
                        }
                    }
                }
                if (detailsGesture == CLOSING_DRAG) {
                    // Keep receiving the gesture even after the panel has slid off screen.
                    setOffset(openOffset() + Math.max(0.0F, Math.min(-openOffset(), distanceY)));
                } else if (detailsGesture == PAGING_DRAG) {
                    dragPageTo(distanceX);
                }
                break;
            case MotionEvent.ACTION_UP:
                int completedGesture = detailsGesture;
                detailsGesture = 0;
                if (completedGesture == CLOSING_DRAG) {
                    if (distanceY >= swipeThreshold()) {
                        closeDetails();
                    } else {
                        settleTo(openOffset(), null);
                    }
                } else if (completedGesture == PAGING_DRAG) {
                    dragPageTo(distanceX);
                    float threshold = Math.max(2.0F * touchSlop, Math.min(width / 5.0F, 80.0F * density));
                    picture.finishDetailsPageDrag(Math.abs(pageDragX) >= threshold ? pageDragX : 0.0F);
                } else if (completedGesture == WAITING_FOR_DIRECTION && !moved && view == overlay) {
                    view.performClick();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (detailsGesture == CLOSING_DRAG) {
                    settleTo(openOffset(), null);
                }
                cancelDetailsGesture();
                break;
        }
        return true;
    }

    private void dragPageTo(float distanceX) {
        float previous = pageDragX;
        pageDragX = Math.max(-width, Math.min(width, distanceX));
        picture.c(pageDragX - previous, 0.0F);
    }

    private void cancelDetailsGesture() {
        if (detailsGesture == PAGING_DRAG) {
            picture.finishDetailsPageDrag(0.0F);
        }
        detailsGesture = 0;
    }

    private df currentDetails() {
        return viewer.H == 2 && viewer.C.c() == viewer && !viewer.C.q() && !viewer.C.isFinishing()
                && pictureUri.equals(picture.getPictureUri())
                ? source.currentPictureDetails() : null;
    }

    private void returnFromViewer() {
        returningFromSwipe = true;
        try {
            if (viewer.H == 2 && viewer.C.c() == viewer) {
                viewer.C.onBackPressed();
            }
        } finally {
            // A standalone viewer stays off screen until its window closes.
            returningFromSwipe = viewer.C.isFinishing();
            reset();
        }
    }

    private float openOffset() {
        return -sheetHeight / 2.0F;
    }

    private float swipeThreshold() {
        return Math.min(height / 3.0F, Math.max(64.0F * density, Math.min(144.0F * density, height * 0.17F)));
    }

    private void setOffset(float offsetY) {
        this.offsetY = offsetY;
        boolean showPanel = detailsOpen || (dragUp && SHOW_DETAILS.equals(dragAction));
        float reveal = showPanel ? Math.max(0.0F, -2.0F * offsetY) : 0.0F;
        picture.setVerticalDragPosition(offsetY, reveal);
        sheet.setTranslationY(sheetHeight - reveal);
        int visibility = reveal > 0.0F || detailsOpen ? View.VISIBLE : View.INVISIBLE;
        if (overlay.getVisibility() != visibility) {
            overlay.setVisibility(visibility);
        }
    }

    private void displayDetails(df details) {
        details.populateDetails(rows);
        scrollView.scrollTo(0, 0);
    }

    private void hideDetails() {
        boolean wasOpen = detailsOpen;
        detailsOpen = false;
        detailsClosing = false;
        rows.removeAllViews();
        overlay.setVisibility(View.INVISIBLE);
        if (wasOpen) {
            viewer.C.setKeepNavigationButtonsVisible(false);
        }
    }

    private void settleTo(float target, Runnable completion) {
        cancelAnimation();
        if (offsetY == target) {
            if (completion != null) {
                completion.run();
            }
            return;
        }
        ValueAnimator next = ValueAnimator.ofFloat(offsetY, target);
        animation = next;
        next.setDuration(200L);
        next.setInterpolator(new DecelerateInterpolator(1.5F));
        next.addUpdateListener(value -> setOffset((Float) value.getAnimatedValue()));
        next.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator ended) {
                if (animation == ended) {
                    animation = null;
                    if (completion != null) {
                        completion.run();
                    }
                }
            }
        });
        next.start();
    }

    private void cancelAnimation() {
        if (animation != null) {
            ValueAnimator previous = animation;
            animation = null;
            previous.cancel();
        }
    }
}
