import java.io.BufferedReader;
import java.io.StringReader;
import java.time.Instant;
import org.openqpic.version.GalleryVersion;

public final class VersionTest {
    static String next(String occupied) throws Exception {
        return ReleaseVersion.next("2026.11.24", new BufferedReader(new StringReader(occupied)));
    }

    public static void main(String[] args) throws Exception {
        assert GalleryVersion.beijingDate(Instant.parse("2026-11-23T15:59:59Z").toEpochMilli()).equals("2026.11.23");
        assert GalleryVersion.beijingDate(Instant.parse("2026-11-23T16:00:00Z").toEpochMilli()).equals("2026.11.24");
        assert GalleryVersion.code("2020.01.01") > 9700000;
        assert GalleryVersion.code("2024.03.01") == GalleryVersion.code("2024.02.29p65535") + 1;
        assert GalleryVersion.code("2026.11.24p10") > GalleryVersion.code("2026.11.24p9");
        assert next("v9.7\n2026.11.23p9\n2026.11.24p01").equals("2026.11.24");
        assert next("2026.11.24\n2026.11.24").equals("2026.11.24p1");
        assert next("2026.11.24p10\nlegacy\n2026.11.24p9\n").equals("2026.11.24p11");
        try { next("2026.11.24p65535"); throw new AssertionError("wrapped patch"); }
        catch (IllegalArgumentException expected) {}
        for (String bad : new String[]{"9.7", "2025.02.29", "2026.1.01", "2026.00.01", "2026.11.24p0",
                "2026.11.24p01", "2026.11.24p65536", "2999.01.01", "2019.12.31", "2026.11.24/url"}) {
            try { GalleryVersion.code(bad); throw new AssertionError(bad); }
            catch (IllegalArgumentException expected) {}
        }
        System.out.println("Beijing rollover, date tags, Android upgrades and release allocation: OK");
    }
}
