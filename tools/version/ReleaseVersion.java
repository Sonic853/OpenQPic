import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.openqpic.version.GalleryVersion;

/** stdin: one tag/release name per line. stdout: the version to use before building. */
public final class ReleaseVersion {
    static String next(String date, BufferedReader occupied) throws Exception {
        int base = GalleryVersion.code(date);
        if (date.length() != 10) throw new IllegalArgumentException("Expected a Beijing date without a patch");
        int max = -1;
        for (String tag; (tag = occupied.readLine()) != null;) {
            if (!tag.equals(date) && !tag.matches(java.util.regex.Pattern.quote(date) + "p[1-9][0-9]*")) continue;
            max = Math.max(max, GalleryVersion.code(tag) - base);
        }
        String version = max < 0 ? date : date + "p" + (max + 1);
        GalleryVersion.code(version); // Fail rather than wrap when the day's Android slots are exhausted.
        return version;
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) throw new IllegalArgumentException("Expected one Beijing date");
        System.out.println(next(args[0],
                new BufferedReader(new InputStreamReader(System.in, "UTF-8"))));
    }
}
