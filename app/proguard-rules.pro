# Kotlinx Serialization
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt

-keepclassmembers class com.kadicompanion.** {
    *** Companion;
}
-keepclasseswithmembers class com.kadicompanion.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Room entities & FTS tables
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Fts4 class *
-dontwarn androidx.room.paging.**

# Hilt
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
