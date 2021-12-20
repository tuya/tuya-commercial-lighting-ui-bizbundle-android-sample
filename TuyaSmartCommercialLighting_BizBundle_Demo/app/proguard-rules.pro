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
#fastJson
#fastJson


-dontusemixedcaseclassnames
-keepattributes Exceptions,InnerClasses,Deprecated,LocalVariable*Table,Synthetic,EnclosingMethod
-repackageclasses ''
-allowaccessmodification
-printconfiguration proguard_test.txt
-optimizationpasses 5
-dontoptimize
-verbose
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers
-dontpreverify
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepclasseswithmembernames class * {
    native <methods>;
}
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keepclassmembers class * extends java.lang.Enum {
        public static **[] values();
        public static ** valueOf(java.lang.String);
        **[] $VALUES;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

#java
-keep class java.awt.**{*;}
-dontwarn java.awt.**
-dontwarn junit.**
-dontwarn javax.microedition.khronos.opengles.GL10.**
-keep interface *
-keep class javax.jmdns.**{*;}

-dontwarn javax.annotation.**

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembernames class * {
	public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class org.json.**{*;}

-keep,allowobfuscation @interface android.support.annotation.Keep
-keep @android.support.annotation.Keep class *
-keepclassmembers class * {
    @android.support.annotation.Keep *;
}


-dontwarn com.squareup.okhttp.**
-dontwarn android.net.http.HttpResponseCache
#EventBus
-keepclassmembers class ** {
    public void onEvent*(**);
}
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}



-keep class com.alibaba.fastjson.**{*;}
-dontwarn com.alibaba.fastjson.**

#mqtt
-keep class com.tuya.smart.mqttclient.mqttv3.** { *; }
-dontwarn com.tuya.smart.mqttclient.mqttv3.**

#OkHttp3
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

#okio
-keep class okio.** { *; }
-dontwarn okio.**


-keep class com.tuya.**{*;}
-dontwarn com.tuya.**
