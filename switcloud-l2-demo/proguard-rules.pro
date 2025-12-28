# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#=============== Keep this rule, it's a good default =================
-keep class com.google.android.material.internal.CheckableImageButton { *; }

#========================= Androidx / Jetpack ========================
# This is a general rule that helps with many AndroidX libraries that
# might use reflection or have dynamic dependencies.
-keep class androidx.core.app.** { *; }
-keep class androidx.lifecycle.** { *; }

#========================= Jetpack Compose ===========================
# Compose uses reflection and code generation extensively. These rules are
# essential to prevent crashes in release builds.
-keep class androidx.compose.runtime.** { *; }
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keepclassmembers class * {
    @androidx.compose.runtime.Composable <fields>;
}

#========================= Moshi (for JSON Serialization) ============
# Moshi uses code generation (kapt/ksp) but also relies on reflection.
# It is critical to keep the classes that Moshi needs to serialize/deserialize.
# Replace 'io.switstack.switcloud.switcloud_l2_demo.data.models' with your actual package for data/model classes.
-keep class io.switstack.switcloud.switcloud_l2_demo.data.** { *; }
-keep @com.squareup.moshi.Json class *
-keep class com.squareup.moshi.*

#======================== Kotlin Coroutines & Reflection =============
# These rules ensure that Kotlin's coroutines and reflection mechanisms
# are not stripped out by ProGuard, which can cause subtle runtime crashes.
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory
-keepnames class kotlinx.coroutines.flow.**
-keep class kotlin.reflect.KClass
-keep class kotlin.reflect.KFunction
-keep class kotlin.reflect.KProperty*

# If you use kotlin.Result, it's good to keep its members
-keepclassmembers class kotlin.Result {
    *;
}

#======================= OkHttp / Retrofit (if used) =================
# Even if you don't use Retrofit directly, Moshi might depend on OkHttp/Okio.
# These are the standard consumer rules for these libraries.
-keep,allowobfuscation,allowshrinking class okhttp3.**
-keep,allowobfuscation,allowshrinking class okio.**
-keep,allowobfuscation,allowshrinking class retrofit2.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn retrofit2.**

#======================= BER-TLV Library ===============================
# Keep classes from the ber-tlv library to prevent issues with reflection.
-keep class io.switstack.switcloud.switcloudl2.** { *; }
-keep class io.switstack.switcloud.switcloudapi.** { *; }
-keep class com.payneteasy.tlv.** { *; }