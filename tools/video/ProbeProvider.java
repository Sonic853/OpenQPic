package org.openqpic.videoprobe;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/** Emulator-only fixtures, not part of the gallery. */
public final class ProbeProvider extends ContentProvider {
    private static final String CLIP = "clip + demo.mp4", SUB = "clip + demo.srt", PIPE = "pipe.mp4";
    private static final int PREFIX = 37;
    private static final String[] COLUMNS = {OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE};
    private static final byte[] SUBTITLE =
            "1\n00:00:00,000 --> 00:00:01,000\nLoopback fixture\n".getBytes(StandardCharsets.UTF_8);
    private byte[] movie;
    private File slice, subtitle;

    @Override public boolean onCreate() {
        try (InputStream in = getContext().getAssets().open("clip.mp4")) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            for (int n; (n = in.read(buf)) != -1;) out.write(buf, 0, n);
            movie = out.toByteArray();
            slice = new File(getContext().getFilesDir(), "slice.bin");
            subtitle = new File(getContext().getFilesDir(), "subtitle.srt");
            try (FileOutputStream file = new FileOutputStream(slice)) {
                file.write(new byte[PREFIX]);
                file.write(movie);
                file.write(new byte[43]);
            }
            try (FileOutputStream file = new FileOutputStream(subtitle)) { file.write(SUBTITLE); }
            return true;
        } catch (IOException e) { throw new IllegalStateException(e); }
    }

    private static String name(Uri uri) {
        String id = uri.getLastPathSegment();
        if (id == null || !id.startsWith("/fixture/")) return "";
        return id.substring("/fixture/".length());
    }

    @Override public String getType(Uri uri) {
        switch (name(uri)) {
            case SUB: return "application/x-subrip";
            case CLIP:
            case PIPE: return "video/mp4";
            default: return null;
        }
    }

    @Override public Cursor query(Uri uri, String[] projection, String s, String[] args, String order) {
        String[] cols = projection == null ? COLUMNS : projection;
        MatrixCursor cursor = new MatrixCursor(cols);
        String name = name(uri);
        if (getType(uri) == null) return cursor;
        // Intentionally report the backing length: the declared asset must cap the response.
        long size = name.equals(CLIP) ? slice.length() : name.equals(SUB) ? SUBTITLE.length : movie.length;
        Object[] row = new Object[cols.length];
        for (int i = 0; i < cols.length; i++) {
            if (OpenableColumns.DISPLAY_NAME.equals(cols[i])) row[i] = name;
            if (OpenableColumns.SIZE.equals(cols[i])) row[i] = size;
        }
        cursor.addRow(row);
        return cursor;
    }

    @Override public AssetFileDescriptor openAssetFile(Uri uri, String mode) throws FileNotFoundException {
        if (!"r".equals(mode)) throw new FileNotFoundException("Read only");
        switch (name(uri)) {
            case CLIP: return new AssetFileDescriptor(ParcelFileDescriptor.open(slice, ParcelFileDescriptor.MODE_READ_ONLY),
                    PREFIX, movie.length);
            case SUB: return new AssetFileDescriptor(ParcelFileDescriptor.open(subtitle, ParcelFileDescriptor.MODE_READ_ONLY),
                    0, SUBTITLE.length);
            case PIPE:
                try {
                    ParcelFileDescriptor[] pipe = ParcelFileDescriptor.createPipe();
                    new Thread(() -> {
                        try (FileOutputStream out = new ParcelFileDescriptor.AutoCloseOutputStream(pipe[1])) {
                            out.write(movie);
                        } catch (IOException ignored) { /* HEAD or client closed early. */ }
                    }, "Video fixture pipe").start();
                    return new AssetFileDescriptor(pipe[0], 0, AssetFileDescriptor.UNKNOWN_LENGTH);
                } catch (IOException e) { throw new FileNotFoundException(e.getMessage()); }
            default: throw new FileNotFoundException("No fixture");
        }
    }

    @Override public Uri insert(Uri uri, ContentValues v) { throw new UnsupportedOperationException(); }
    @Override public int update(Uri uri, ContentValues v, String s, String[] a) { throw new UnsupportedOperationException(); }
    @Override public int delete(Uri uri, String s, String[] a) { throw new UnsupportedOperationException(); }
}
