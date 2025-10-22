# (existing rules preserved if any were present previously)

-keep class ** extends androidx.work.ListenableWorker
-keep class ** extends androidx.work.CoroutineWorker
-keep class androidx.work.impl.WorkManagerInitializer { *; }
-keep class kotlinx.coroutines.** { *; }
-dontwarn kotlinx.coroutines.**
-keep class kotlin.Metadata { *; }
-dontwarn androidx.compose.**
-dontwarn kotlin.reflect.**

# Navigation minimal keep
-keep class androidx.navigation.** { *; }
-keep class com.persona.app.nav.** { *; }

# Remove logs
-assumenosideeffects class android.util.Log {
    public static *** d(...);
    public static *** v(...);
    public static *** i(...);
}
