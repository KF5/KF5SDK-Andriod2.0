# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

-dontwarn
-dontoptimize
-optimizationpasses 5
-dontskipnonpubliclibraryclassmembers
-printmapping proguardMapping.txt
-optimizations !code/simplification/cast,!field/*,!class/merging/*
-keepattributes *Annotation*,InnerClasses
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable
#----------------------------------------------------------------------------

#---------------------------------默认保留区---------------------------------
#继承activity,application,service,broadcastReceiver,contentprovider....不进行混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.support.multidex.MultiDexApplication
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.support.v4.**

-dontwarn android.annotation
#本地集成的v4包
#-dontwarn org.support.v4.**
-dontwarn butterknife.**

#-keep class org.support.v4.app.** { *; }
#-keep interface org.support.v4.app.** { *; }
#-keep class org.support.v4.** {*;}
#
##本地集成的okhttp包
#-dontwarn org.support.okhttp.**
#-keep interface org.support.okhttp.** { *; }
#-keep class org.support.okhttp.** {*;}
#-keep public class * extends org.support.okhttp
##本地集成的socket包
#-dontwarn org.support.socket.**
#-keep class org.support.socket.** {*;}
##本地集成的okio包
#-dontwarn org.support.okio.**
#-keep class org.support.okio.** {*;}
##本地集成的imageloader包
#-dontwarn org.support.imageloader.**
#-keep class org.support.imageloader.** {*;}
##本地集成的gson包
#-dontwarn org.support.gson.**
#-keep class org.support.gson.** {*;}
##本地集成的event包
#-dontwarn org.support.event.**
#-keep class org.support.event.** {*;}
##sdk包中Activity相关代码
#-dontwarn com.chosen.kf5sdk.**
#-keep class com.chosen.kf5sdk.** {*;}
##sdk包中chat相关代码
#-dontwarn com.kf5chat.**
#-keep class com.kf5chat.** {*;}
##sdk中其他模块代码
#-dontwarn com.kf5sdk.**
#-keep class com.kf5sdk.** {*;}

-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#这个主要是在layout 中写的onclick方法android:onclick="onClick"，不进行混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-keep class **.R$* {
 *;
}

-keepclassmembers class * {
    void *(*Event);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#// natvie 方法不混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

##glide
#-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
#  **[] $VALUES;
#  public *;
#}
#
#################gson##################
#-keep class com.google.gson.** {*;}
#-keep class com.google.**{*;}
#-keep class sun.misc.Unsafe { *; }
#-keep class com.google.gson.stream.** { *; }
#-keep class com.google.gson.examples.android.model.** { *; }

#-keep enum com.kf5.sdk.ticket.entity.MessageStatus { *; }
#-keep enum com.kf5.sdk.im.entity.MessageType { *; }
#-keep enum com.kf5.sdk.im.entity.Status { *; }
#-keep enum com.kf5.sdk.im.expression.utils.ImageBase.Scheme { *; }
#-keep enum com.kf5.sdk.im.keyboard.data.EmoticonPageEntity.DelBtnStatus { *; }
#        -keepclassmembers class * {
#        void onEvent*(**);
#        }
#-keep class com.kf5.sdk.im.expression.utils.ExpressionCommonUtils {
#                *;
#        }
#

        -keep class com.kf5.sdk.im.entity.**{*;}
        -keep class com.kf5.sdk.helpcenter.entity.**{*;}
        -keep class com.kf5.sdk.system.entity.**{*;}
        -keep class com.kf5.sdk.ticket.entity.**{*;}
