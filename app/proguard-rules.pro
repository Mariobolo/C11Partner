# Add project specific ProGuard rules here.

# ============ WebView JS Bridge ============
# 保留所有 Bridge 类的 @JavascriptInterface 方法
-keepclassmembers class com.c11partner.desktop.bridge.** {
    @android.webkit.JavascriptInterface <methods>;
}
-keep class com.c11partner.desktop.bridge.** { *; }

# 保留 MainActivity 中 WebView 相关的 public 方法
-keepclassmembers class com.c11partner.desktop.MainActivity {
    public <fields>;
    public <methods>;
}

# 保留 CarStateListener 接口
-keep class com.c11partner.desktop.CarStateListener { *; }
-keep class com.c11partner.desktop.LeapMotorCarState { *; }

# ============ 调试信息 ============
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile

# ============ 第三方库 ============
# pinyin4j
-keep class net.sourceforge.pinyin4j.** { *; }
-dontwarn net.sourceforge.pinyin4j.**

# fastjson (如果保留的话)
-keep class com.alibaba.fastjson.** { *; }
-dontwarn com.alibaba.fastjson.**

# adblib
-keep class com.tananaev.adblib.** { *; }
-dontwarn com.tananaev.adblib.**

# OkHttp
-dontwarn okhttp3.**
-dontwarn okio.**