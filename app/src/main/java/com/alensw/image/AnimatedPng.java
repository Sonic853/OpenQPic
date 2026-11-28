package com.alensw.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import com.alensw.io.OpenFileHandle;
import com.alensw.jni.JniUtils;

/** Composes APNG frames repacked by JNI and decoded by Android's PNG decoder. */
public final class AnimatedPng extends Picture {
    private static final Handler UI = new Handler(Looper.getMainLooper());
    private static final String MIME = "image/apng";
    private static final int BACKGROUND = 1;
    private static final int PREVIOUS = 2;

    private final int sample, width, height, frameCount, firstDelay;
    private final long playCount;
    private final Canvas canvas;
    private final Canvas savedCanvas = new Canvas();
    private final Paint over = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final Paint source = new Paint(Paint.FILTER_BITMAP_FLAG);
    private final Rect area = new Rect(), previousArea = new Rect();
    private final Rect bounds;
    private int handle, previousDisposal;
    private Bitmap saved;
    private boolean started;
    private Playback playback;
    private Paint background;

    private AnimatedPng(int handle, Uri uri, int sample, int[] info, Bitmap bitmap, Frame first) {
        super(bitmap, 4);
        h = MIME;
        i = uri;
        this.handle = handle;
        this.sample = sample;
        width = info[0];
        height = info[1];
        frameCount = info[2];
        playCount = info[3] & 0xffffffffL;
        firstDelay = first.delay;
        canvas = new Canvas(bitmap);
        bounds = new Rect(0, 0, e(), f());
        source.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        compose(first, 0);
    }

    public static boolean isPng(String mime) {
        return "image/png".equals(mime) || MIME.equals(mime);
    }

    public static boolean mayAnimate(OpenFileHandle file) {
        return JniUtils.apngHasAnimation(file.c());
    }

    public static AnimatedPng open(OpenFileHandle file, Uri uri) {
        int handle = 0;
        Bitmap canvas = null;
        Frame first = null;
        boolean opened = false;
        try {
            int[] info = new int[4];
            handle = JniUtils.apngOpenFD(file.c(), info);
            if (handle == 0) return null;
            int limit = Math.max(1, BitmapUtils.d / 3);
            int sample = 1;
            while (scaled(info[0], sample) > 4096 || scaled(info[1], sample) > 4096
                    || (long) scaled(info[0], sample) * scaled(info[1], sample) > limit) sample *= 2;
            first = Frame.decode(handle, 0, sample);
            if (first == null) return null;
            canvas = Bitmap.createBitmap(scaled(info[0], sample), scaled(info[1], sample), Bitmap.Config.ARGB_8888);
            AnimatedPng animation = new AnimatedPng(handle, uri, sample, info, canvas, first);
            opened = true;
            return animation;
        } catch (RuntimeException | OutOfMemoryError unavailable) {
            return null;
        } finally {
            if (first != null) first.bitmap.recycle();
            if (!opened) {
                if (canvas != null) canvas.recycle();
                if (handle != 0) JniUtils.apngClose(handle);
            }
        }
    }

    private static int scaled(int length, int sample) {
        return (length - 1) / sample + 1;
    }

    private void compose(Frame frame, int number) {
        // Map a boundary consistently for adjacent frame rects, including the last partial sample.
        int right = frame.x + frame.width, bottom = frame.y + frame.height;
        area.set(frame.x / sample, frame.y / sample,
                right == width ? e() : right / sample, bottom == height ? f() : bottom / sample);
        // At the start of a pass the original canvas is transparent.
        int disposal = number == 0 && frame.dispose == PREVIOUS ? BACKGROUND : frame.dispose;
        if (disposal == PREVIOUS && !area.isEmpty() && saved == null) {
            saved = Bitmap.createBitmap(e(), f(), Bitmap.Config.ARGB_8888);
            savedCanvas.setBitmap(saved);
        }
        if (number == 0) {
            d().eraseColor(0);
        } else if (!previousArea.isEmpty()) {
            if (previousDisposal == PREVIOUS) {
                canvas.drawBitmap(saved, previousArea, previousArea, source);
            } else if (previousDisposal == BACKGROUND) {
                int state = canvas.save();
                canvas.clipRect(previousArea);
                canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                canvas.restoreToCount(state);
            }
        }
        if (!area.isEmpty()) {
            if (disposal == PREVIOUS) savedCanvas.drawBitmap(d(), area, area, source);
            canvas.drawBitmap(frame.bitmap, null, area, frame.blend == 0 ? source : over);
        }
        previousArea.set(area);
        previousDisposal = disposal;
    }

    @Override public synchronized boolean a(View view, Paint background) {
        if (handle == 0 || view.getWindowVisibility() != View.VISIBLE) return false;
        this.background = background;
        if (playback == null || playback.view != view) {
            a(true);
            playback = new Playback(view, handle);
            if (frameCount > 1) {
                if (started) playback.decode(0, SystemClock.uptimeMillis());
                else playback.advance(0, firstDelay);
            }
            started = true;
        }
        return true;
    }

    @Override public synchronized void a(boolean stop) {
        if (playback != null) {
            UI.removeCallbacksAndMessages(playback);
            if (playback.pending != null) playback.pending.bitmap.recycle();
            playback.pending = null;
            playback = null;
        }
    }

    @Override protected synchronized void a() {
        a(true);
        JniUtils.apngClose(handle);
        handle = 0;
        canvas.setBitmap(null);
        savedCanvas.setBitmap(null);
        if (saved != null) saved.recycle();
        saved = null;
        super.a();
    }

    @Override public void a(Canvas output, Matrix matrix, Picture preview, Matrix previewMatrix, Paint paint, boolean moving) {
        int state = output.save();
        try {
            output.concat(matrix);
            if (background != null) output.drawRect(0, 0, e(), f(), background);
            output.drawBitmap(d(), null, bounds, paint);
        } finally {
            output.restoreToCount(state);
        }
    }

    private final class Playback {
        final View view;
        final int handle;
        long plays = playCount;
        Frame pending;

        Playback(View view, int handle) {
            this.view = view;
            this.handle = handle;
        }

        void advance(int number, int delay) {
            int next = number + 1;
            if (next == frameCount) {
                if (plays == 1) return;
                if (plays > 1) plays--;
                next = 0;
            }
            decode(next, SystemClock.uptimeMillis() + Math.max(10, delay));
        }

        void decode(int number, long when) {
            c.execute(() -> {
                synchronized (AnimatedPng.this) {
                    if (playback != this) return;
                }
                Frame frame = Frame.decode(handle, number, sample);
                if (frame == null) return;
                synchronized (AnimatedPng.this) {
                    if (playback == this && UI.postAtTime(() -> present(number), this, when)) {
                        pending = frame;
                    } else {
                        frame.bitmap.recycle();
                    }
                }
            });
        }

        private void present(int number) {
            synchronized (AnimatedPng.this) {
                if (playback != this || pending == null) return;
                Frame frame = pending;
                pending = null;
                try {
                    compose(frame, number);
                    view.invalidate();
                    advance(number, frame.delay);
                } catch (RuntimeException | OutOfMemoryError unavailable) {
                    // Keep the last good frame if a later frame fails.
                } finally {
                    frame.bitmap.recycle();
                }
            }
        }
    }

    private static final class Frame {
        final Bitmap bitmap;
        final int x, y, width, height, delay, dispose, blend;

        Frame(Bitmap bitmap, int[] info) {
            this.bitmap = bitmap;
            x = info[0];
            y = info[1];
            width = info[2];
            height = info[3];
            delay = info[4];
            dispose = info[5];
            blend = info[6];
        }

        static Frame decode(int handle, int number, int sample) {
            Bitmap bitmap = null;
            try {
                int[] info = new int[7];
                byte[] encoded = JniUtils.apngReadFrame(handle, number, info);
                if (encoded == null) return null;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                options.inSampleSize = sample;
                bitmap = BitmapFactory.decodeByteArray(encoded, 0, encoded.length, options);
                return bitmap == null ? null : new Frame(bitmap, info);
            } catch (RuntimeException | OutOfMemoryError unavailable) {
                if (bitmap != null) bitmap.recycle();
                return null;
            }
        }
    }
}
