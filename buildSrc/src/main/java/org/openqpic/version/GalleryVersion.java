package org.openqpic.version;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Shared by Gradle, the release allocator and the API 23 updater. */
public final class GalleryVersion {
    private static final Pattern TAG = Pattern.compile("([0-9]{4})\\.([0-9]{2})\\.([0-9]{2})(?:p([1-9][0-9]{0,4}))?");
    private static final long EPOCH = 1577836800000L; // 2020-01-01T00:00:00Z
    public static final int PATCHES_PER_DAY = 65536;

    private GalleryVersion() {}

    public static String beijingDate(long instant) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("GMT+08:00"));
        return format.format(new Date(instant));
    }

    public static int code(String name) {
        Matcher m = TAG.matcher(name);
        if (!m.matches()) throw new IllegalArgumentException("Expected yyyy.MM.dd or yyyy.MM.ddpN: " + name);
        GregorianCalendar day = new GregorianCalendar(TimeZone.getTimeZone("UTC"), Locale.ROOT);
        day.clear();
        day.setLenient(false);
        day.set(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)) - 1, Integer.parseInt(m.group(3)));
        long days = (day.getTimeInMillis() - EPOCH) / 86400000L;
        int patch = m.group(4) == null ? 0 : Integer.parseInt(m.group(4));
        long code = 10000000L + days * PATCHES_PER_DAY + patch;
        if (days < 0 || patch >= PATCHES_PER_DAY || code > 2100000000L)
            throw new IllegalArgumentException("Date or patch outside Android versionCode range: " + name);
        return (int) code;
    }
}
