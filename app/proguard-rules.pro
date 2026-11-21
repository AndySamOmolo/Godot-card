# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class com.kadicompanion.** {
    *** Companion;
}
-keepclasseswithmembers class com.kadicompanion.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.kadicompanion.**$$serializer { *; }

# Room entities & FTS tables
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { <fields>; <init>(...); }
-keep @androidx.room.Fts4 class * { <fields>; <init>(...); }
-dontwarn androidx.room.paging.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
