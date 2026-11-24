package com.alensw.compat;

import android.graphics.Canvas;
import android.os.Build;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/** Public APIs on older supported releases that are absent from the current SDK stubs. */
public final class LegacyPlatform {
    private LegacyPlatform() {}

    private static final class CanvasFlags {
        static final Method SAVE = find();
        private static Method find() {
            try { return Canvas.class.getMethod("save", int.class); }
            catch (NoSuchMethodException e) { return null; }
        }
    }

    public static int save(Canvas canvas, int flags) {
        // Modern Canvas saves both the matrix and the clip. On M–O keep the caller's flags.
        if (Build.VERSION.SDK_INT < 28 && CanvasFlags.SAVE != null) {
            try { return (Integer) CanvasFlags.SAVE.invoke(canvas, flags); }
            catch (InvocationTargetException e) { throw propagate(e.getCause()); }
            catch (IllegalAccessException e) { throw new IllegalStateException(e); }
        }
        return canvas.save();
    }

    /** Preserve the original exception even at an Android callback with no throws clause. */
    public static RuntimeException propagate(Throwable cause) {
        return LegacyPlatform.<RuntimeException>rethrow(cause);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> RuntimeException rethrow(Throwable cause) throws T { throw (T) cause; }
}
