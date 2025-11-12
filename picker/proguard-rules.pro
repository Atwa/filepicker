
-keep class com.atwa.filepicker.FilePicker
-keep class com.atwa.filepicker.core.FileConverter
-keep class com.atwa.filepicker.result.*

-keep class com.atwa.filepicker.FilePicker$Companion { *; }
-keep class com.atwa.filepicker.core.FileConverter$Companion { *; }

-keepclassmembers class com.atwa.filepicker.FilePicker {
    *;
 }
-keepclassmembers class com.atwa.filepicker.core.FileConverter {
     *;
 }
 -keepclassmembers class com.atwa.filepicker.result.* {
     *;
  }

-keepclasseswithmembernames class * {
    native <methods>;
}

-renamesourcefileattribute SourceFile