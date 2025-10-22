# (# Persona optimization & keep rules
-dontwarn org.jetbrains.**
-dontwarn androidx.**
-keep class com.example.persona.** { *; }
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-dontnote kotlinx.coroutines.**
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
