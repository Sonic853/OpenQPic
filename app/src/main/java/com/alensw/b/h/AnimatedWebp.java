package com.alensw.b.h;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ImageDecoder;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.AnimatedImageDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.system.ErrnoException;
import android.system.Os;
import android.util.Size;
import android.view.View;
import com.alensw.b.c.f;
import java.io.File;
import java.io.IOException;

/** System WebP decoder and viewer rendering adapter. Callers gate it on API 28. */
@TargetApi(28)
public final class AnimatedWebp extends j implements Drawable.Callback {
    private static final String MIME = "image/webp";
    private static final Handler FRAMES = new Handler(Looper.getMainLooper());

    private final AnimatedImageDrawable animation;
    private View owner;
    private Paint background;

    private AnimatedWebp(AnimatedImageDrawable animation, Uri uri) {
        super(animation.getIntrinsicWidth(), animation.getIntrinsicHeight(), 4, uri, MIME);
        this.animation = animation;
        animation.setBounds(0, 0, e(), f());
    }

    /** Extended WebP has a VP8X chunk first; bit 1 of its feature flags denotes animation. */
    public static boolean mayAnimate(f file) {
        byte[] header = new byte[21];
        try {
            int read = Os.pread(file.b(), header, 0, header.length, 0);
            return read == header.length
                    && header[0] == 'R' && header[1] == 'I' && header[2] == 'F' && header[3] == 'F'
                    && header[8] == 'W' && header[9] == 'E' && header[10] == 'B' && header[11] == 'P'
                    && header[12] == 'V' && header[13] == 'P'
                    && header[14] == '8' && header[15] == 'X' && (header[20] & 2) != 0;
        } catch (ErrnoException | IOException unavailable) {
            // Let ImageDecoder inspect the header when the provider supplies a non-seekable stream.
            return true;
        }
    }

    public static AnimatedWebp open(ContentResolver resolver, f file, Uri uri) {
        try {
            Drawable drawable = ImageDecoder.decodeDrawable(source(resolver, file.g()), (decoder, info, src) -> {
                if (!MIME.equals(info.getMimeType()) || !info.isAnimated()) throw new HeaderRead();
                decoder.setTargetSampleSize(sampleSize(info.getSize(), 1, com.alensw.b.h.b.d));
            });
            return drawable instanceof AnimatedImageDrawable
                    ? new AnimatedWebp((AnimatedImageDrawable) drawable, uri) : null;
        } catch (IOException | RuntimeException | OutOfMemoryError unavailable) {
            return null;  // The existing bitmap path can still offer a preview.
        }
    }

    static boolean readBounds(ContentResolver resolver, Uri uri, a options) {
        try {
            ImageDecoder.decodeBitmap(source(resolver, uri), (decoder, info, src) -> {
                if (MIME.equals(info.getMimeType())) {
                    options.outWidth = info.getSize().getWidth();
                    options.outHeight = info.getSize().getHeight();
                    options.outMimeType = MIME;
                }
                throw new HeaderRead();
            });
        } catch (HeaderRead done) {
            return MIME.equals(options.outMimeType) && options.outWidth > 0 && options.outHeight > 0;
        } catch (IOException | RuntimeException unavailable) {
            // Keep the existing unsupported-image result.
        }
        return false;
    }

    static Bitmap firstFrame(ContentResolver resolver, Uri uri, int sample) {
        return firstFrame(source(resolver, uri), sample, 0);
    }

    public static boolean readable(ContentResolver resolver, Uri uri) {
        Bitmap sample = firstFrame(source(resolver, uri), 1, 128 * 128);
        if (sample == null) return false;
        sample.recycle();
        return true;
    }

    private static Bitmap firstFrame(ImageDecoder.Source source, int sample, int maxPixels) {
        try {
            return ImageDecoder.decodeBitmap(source, (decoder, info, src) -> {
                if (!MIME.equals(info.getMimeType())) throw new HeaderRead();
                decoder.setAllocator(ImageDecoder.ALLOCATOR_SOFTWARE);
                decoder.setTargetSampleSize(sampleSize(info.getSize(), sample, maxPixels));
            });
        } catch (IOException | RuntimeException unavailable) {
            return null;
        }
    }

    private static ImageDecoder.Source source(ContentResolver resolver, Uri uri) {
        return "file".equals(uri.getScheme()) ? ImageDecoder.createSource(new File(uri.getPath()))
                : ImageDecoder.createSource(resolver, uri);
    }

    private static int sampleSize(Size size, int requested, int maxPixels) {
        int sample = Math.max(1, requested);
        while (maxPixels > 0 && (long) ceilDivide(size.getWidth(), sample) * ceilDivide(size.getHeight(), sample) > maxPixels) {
            sample *= 2;
        }
        return sample;
    }

    private static int ceilDivide(int size, int sample) {
        return (size - 1) / sample + 1;
    }

    private static final class HeaderRead extends RuntimeException {
        HeaderRead() { super(null, null, false, false); }
    }

    @Override public boolean a(View view, Paint background) {
        if (view.getWindowVisibility() != View.VISIBLE) return false;
        this.background = background;
        if (owner != view) {
            a(true);
            owner = view;
            animation.setCallback(this);
            animation.start();
        }
        return true;
    }

    @Override public void a(boolean stop) {
        if (owner == null) return;
        animation.stop();
        animation.setCallback(null);
        FRAMES.removeCallbacksAndMessages(animation);
        owner = null;
    }

    @Override protected void a() {
        a(true);
        super.a();
    }

    @Override public void a(Canvas canvas, Matrix matrix, j preview, Matrix previewMatrix, Paint paint, boolean moving) {
        int saved = canvas.save();
        try {
            canvas.concat(matrix);
            if (background != null) canvas.drawRect(0, 0, e(), f(), background);
            animation.draw(canvas);
        } finally {
            canvas.restoreToCount(saved);
        }
    }

    @Override public void invalidateDrawable(Drawable drawable) {
        if (owner != null) owner.postInvalidateOnAnimation();
    }

    @Override public void scheduleDrawable(Drawable drawable, Runnable nextFrame, long when) {
        if (owner != null) {
            FRAMES.removeCallbacks(nextFrame, drawable);
            FRAMES.postAtTime(nextFrame, drawable, when);
        }
    }

    @Override public void unscheduleDrawable(Drawable drawable, Runnable nextFrame) {
        FRAMES.removeCallbacks(nextFrame, drawable);
    }
}
