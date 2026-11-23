import com.android.builder.model.v2.models.AndroidProject;
import com.android.builder.model.v2.models.BasicAndroidProject;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.gradle.tooling.BuildAction;
import org.gradle.tooling.BuildController;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.model.gradle.BasicGradleProject;

/** Requests the Android Studio V2 sync models, without opening the IDE. */
public final class StudioModelProbe {
    private static final class Probe implements BuildAction<List<String>>, Serializable {
        public List<String> execute(BuildController controller) {
            // Gradle ships this client action with a classpath inferred from
            // its bytecode. V2 enum adaptation also needs Kotlin's runtime.
            kotlin.enums.EnumEntries.class.getName();
            var build = controller.getBuildModel();
            BasicGradleProject app = build.getProjects().stream()
                    .filter(p -> p.getPath().equals(":app")).findFirst().orElseThrow();
            var base = controller.getModel(app, BasicAndroidProject.class);
            var android = controller.getModel(app, AndroidProject.class);
            var main = base.getMainSourceSet().getSourceProvider();
            var result = new ArrayList<String>();
            result.add("module=" + base.getPath() + " type=" + base.getProjectType());
            result.add("namespace=" + android.getNamespace());
            base.getVariants().forEach(v -> result.add("variant=" + v.getName()));
            result.add("manifest=" + main.getManifestFile());
            main.getJavaDirectories().forEach(d -> result.add("java=" + d));
            main.getResDirectories().forEach(d -> result.add("res=" + d));
            main.getJniLibsDirectories().forEach(d -> result.add("jniLibs=" + d));
            main.getCustomDirectories().forEach(d -> result.add(d.getSourceTypeName() + "=" + d.getDirectory()));
            return result;
        }
    }

    public static void main(String[] args) {
        var connector = GradleConnector.newConnector().forProjectDirectory(new File(args[0]))
                .useInstallation(new File(args[1])).useGradleUserHomeDir(new File(args[2]));
        try (var connection = connector.connect()) {
            connection.action(new Probe()).withArguments("--offline", "-Pandroid.injected.build.model.only.v2=true")
                    .run().forEach(System.out::println);
        }
    }
}
