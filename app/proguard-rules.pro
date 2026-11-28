# Shrink unused library code only. Keep gallery state, reflection and JNI entry points intact.
-dontobfuscate
-dontoptimize
-keepattributes *Annotation*,Signature,InnerClasses,EnclosingMethod
-keep class com.alensw.** { *; }
-keep class com.b.** { *; }
-keep class org.openqpic.** { *; }

-keep class androidx.documentfile.** { *; }
-keep class androidx.drawerlayout.** { *; }
-keep class androidx.viewpager.** { *; }

# The manifest keeps the provider; URI creation also has two public static entry points.
-keepclassmembers class androidx.core.content.FileProvider {
    public static android.net.Uri getUriForFile(...);
}
