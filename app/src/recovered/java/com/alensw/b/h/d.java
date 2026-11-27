package com.alensw.b.h;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import com.alensw.jni.JniUtils;

public class d extends j {
    public static final boolean a = Build.VERSION.SDK_INT >= 8
            && (Build.VERSION.SDK_INT < 11 || Build.VERSION.SDK_INT > 13);
    private static final String MIME = "image/gif";
    private final int duration;
    final int frameCount;
    final int repeatCount;
    private final Handler handler;
    private final int[] buffers = new int[2];
    private int handle;
    private int sample;
    private int[] pixels;
    private boolean hasFrame;
    private g playback;
    private View owner;

    public d(int handle, Uri uri, Handler handler, Bitmap bitmap) {
        super(bitmap, 4);
        h = MIME;
        i = uri;
        this.duration = JniUtils.gifGetDuration(handle);
        this.frameCount = JniUtils.gifGetFrameCount(handle);
        this.repeatCount = JniUtils.gifGetRepeatCount(handle);
        this.handler = handler == null ? new Handler(Looper.getMainLooper()) : handler;
        allocateBuffers(handle);
        this.handle = handle;
    }

    public d(int handle, Uri uri, Handler handler, int width, int height, int[] pixels) {
        super(width, height, 4, uri, MIME);
        this.duration = JniUtils.gifGetDuration(handle);
        this.frameCount = JniUtils.gifGetFrameCount(handle);
        this.repeatCount = JniUtils.gifGetRepeatCount(handle);
        this.handler = handler == null ? new Handler(Looper.getMainLooper()) : handler;
        this.sample = JniUtils.gifGetImageWidth(handle) / width;
        this.pixels = pixels;
        allocateBuffers(handle);
        this.handle = handle;
    }

    public static d a(com.alensw.b.c.f file, Uri uri, Handler handler) {
        int handle = JniUtils.gifOpenFD(file.c(), a);
        if (handle == 0) return null;
        Bitmap bitmap = null;
        boolean attached = false;
        try {
            if (JniUtils.gifGetFrameCount(handle) <= 1) return null;
            int width = JniUtils.gifGetImageWidth(handle);
            int height = JniUtils.gifGetImageHeight(handle);
            for (; width > 0 && height > 0; width >>= 1, height >>= 1) {
                int count = width * height;
                if (count > com.alensw.b.h.b.d) continue;
                d animation;
                if (a) {
                    bitmap = com.alensw.b.h.b.a(width, height, Bitmap.Config.ARGB_8888);
                    if (bitmap == null) continue;
                    animation = new d(handle, uri, handler, bitmap);
                } else {
                    int[] pixels;
                    try {
                        pixels = new int[count];
                    } catch (OutOfMemoryError unavailable) {
                        continue;
                    }
                    animation = new d(handle, uri, handler, width, height, pixels);
                }
                attached = true;
                return animation;
            }
            return null;
        } catch (RuntimeException | OutOfMemoryError error) {
            Log.e("GifMovie", "load GIF: " + uri, error);
            return null;
        } finally {
            if (!attached) {
                if (bitmap != null) bitmap.recycle();
                JniUtils.gifClose(handle);
            }
        }
    }

    private void allocateBuffers(int handle) {
        int area = JniUtils.gifGetImageWidth(handle) * JniUtils.gifGetImageHeight(handle);
        boolean allocated = false;
        try {
            for (int i = 0; i < buffers.length; i++) {
                buffers[i] = JniUtils.gifAllocBuffer(area);
                if (buffers[i] == 0) throw new IllegalStateException("GIF buffer allocation failed");
            }
            allocated = true;
        } finally {
            if (!allocated) freeBuffers();
        }
    }

    private void freeBuffers() {
        for (int i = 0; i < buffers.length; i++) {
            if (buffers[i] != 0) {
                JniUtils.gifFreeBuffer(buffers[i]);
                buffers[i] = 0;
            }
        }
    }

    synchronized int decode(int number, int buffer, int source) {
        return handle == source && handle != 0 ? JniUtils.gifDecodeFrame(handle, number, buffer) : 0;
    }

    synchronized boolean present(g job, int number, int buffer, int source) {
        if (playback != job || handle != source || handle == 0) return false;
        if (a ? !JniUtils.gifDrawFrame(handle, number, buffer, d())
              : !JniUtils.gifDrawFrame2(handle, number, buffer, pixels, sample)) return false;
        hasFrame = true;
        owner.invalidate();
        return true;
    }

    @Override protected void a() {
        a(true);
        synchronized (this) {
            freeBuffers();
            if (handle != 0) {
                JniUtils.gifClose(handle);
                handle = 0;
            }
            pixels = null;
        }
        super.a();
    }

    @Override public void a(Canvas canvas, Matrix matrix, j preview, Matrix previewMatrix, Paint paint, boolean moving) {
        if (hasFrame && a && j()) {
            canvas.drawBitmap(d(), matrix, paint);
        } else if (hasFrame && pixels != null) {
            int saved = canvas.save();
            try {
                canvas.concat(matrix);
                canvas.drawBitmap(pixels, 0, e(), 0, 0, e(), f(), false, paint);
            } finally {
                canvas.restoreToCount(saved);
            }
        } else if (preview != null && preview.j()) {
            canvas.drawBitmap(preview.d(), previewMatrix, paint);
        }
    }

    @Override public synchronized void a(boolean stop) {
        if (playback != null) {
            playback.a();
            playback = null;
        }
        owner = null;
    }

    @Override public synchronized boolean a(View view, Paint background) {
        if (handle == 0 || view.getWindowVisibility() != View.VISIBLE) return false;
        JniUtils.gifSetBkColor(handle, background.getColor());
        if (owner != view) {
            a(true);
            owner = view;
            playback = new g(this, handler);
            for (int buffer : buffers) playback.recycle(new f(playback, buffer, handle));
            c.a(playback);
        }
        return true;
    }

    @Override public long b() {
        return duration & 0xffffffffL;
    }
}
