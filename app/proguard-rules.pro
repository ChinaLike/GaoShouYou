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
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#优化  不优化输入的类文件
-dontoptimize
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
# 混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
#忽略警告
-ignorewarning

#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

################混淆保护自己项目的部分代码以及引用的第三方jar包library#########################
#-libraryjars libs/nineoldandroids-2.4.0.jar
#银联
-keep class com.unionpay.**{*;}
-keep class cn.gov.pbc.tsm.client.mobile.android.bank.service{*;}
-keep class UCMobile.payPlugin{*;}
-keep class unionpay.**{*;}
#点9动画
-keep class com.nineoldandroids.**{*;}
#gson
-keepattributes Signature
-keep class com.google.gson.**{*;}
-keep class sun.misc.Unsafe { *; }
#okhttp
-keep class com.squareup.okhttp.**{*;}
#picasso
-keep class com.squareup.picasso.**{*;}
#glide
-keep class com.github.bumptech.glide.**{*;}
#butterknife
-dontwarn butterknife.internal.**
-keep class **$$ViewInjector { *; }
-keepnames class * { @butterknife.InjectView *;}
#litepal
-dontwarn org.litepal.**
-keep class org.litepal.** { *; }
#支付宝
-keep class com.alipay.android.app.IAliPay{*;}
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.lib.ResourceMap{*;}
# ShareSdk
-keep class com.mob.**{*;}
-keep class cn.sharesdk.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
#友盟
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
# 个推
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}
# rxjava、rxAndroid
-dontwarn rx.**
-keep class rx.**{*;}
#保护MVVM框架
-keep class com.arialyy.frame.**{*;}
-dontwarn com.arialyy.frame.**
-keepclassmembers class * extends com.arialyy.frame.module.AbsModule{
    public <init>(android.content.Context);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsActivity{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsPopupWindow{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsFragment{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsDialogFragment{
    protected void dataCallback(int, java.lang.Object);
}
-keepclassmembers class * extends com.arialyy.frame.core.AbsAlertDialog{
    protected void dataCallback(int, java.lang.Object);
}

-keepclassmembers class * extends com.arialyy.frame.core.AbsDialog{
    protected void dataCallback(int, java.lang.Object);
}

##保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

##不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉，如果使用gson需要打开
-keepattributes Signature

#如果引用了v4或者v7包
-dontwarn android.support.**
# webview + js
-keepattributes *JavascriptInterface*
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
############混淆保护自己项目的部分代码以及引用的第三方jar包library-end##################

###################################混淆保护自己的代码#################################
#gson需要实体不能被混淆
-keep class com.touchrom.gaoshouyou.entity.**{*;}
-keep class com.touchrom.gaoshouyou.entity.sql.**{*;}
-keep public class * extends android.widget.EditText{*;}
-keep class com.lyy.ui.widget.VerticalViewPager{
    *;
}
-keep class com.lyy.momo.service.MMDownloadService{
    protected void dataCallback(int, java.lang.Object);
}
###############################混淆保护自己的代码  end################################