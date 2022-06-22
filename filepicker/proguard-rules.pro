
-keep class com.atwa.filepicker.core.FilePicker
-keep class com.atwa.filepicker.core.FileConverter

-keep class com.atwa.filepicker.core.FilePicker$Companion { *; }
-keep class com.atwa.filepicker.core.FileConverter$Companion { *; }

-keepclassmembers class com.atwa.filepicker.core.FilePicker {
    *;
 }
-keepclassmembers class com.atwa.filepicker.core.FileConverter {
     *;
 }

-keepclasseswithmembernames class * {
    native <methods>;
}

-renamesourcefileattribute SourceFile