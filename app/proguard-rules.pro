# Keep Kotlin metadata, annotations, signatures & inner classes
-keepattributes *Annotation*, Signature, InnerClasses, EnclosingMethod, SourceFile, LineNumberTable
-dontnote kotlinx.serialization.AnnotationsKt

# Keep entry points
-keep class com.kadicompanion.KadiCompanionApp { *; }
-keep class com.kadicompanion.MainActivity { *; }

# Kotlinx Serialization (full R8 protection for DTOs, Companion & $$serializer)
-keep class com.kadicompanion.data.content.** { *; }
-keep,includedescriptorclasses class com.kadicompanion.**$$serializer { *; }
-keepclassmembers class com.kadicompanion.** {
    *** Companion;
}
-keepclasseswithmembers class com.kadicompanion.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Room Database, generated _Impl classes, DAOs & Entities (CRITICAL for R8 full mode)
-keep class * extends androidx.room.RoomDatabase { *; }
-keep class com.kadicompanion.data.local.db.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Fts4 class * { *; }
-keep @androidx.room.Dao class * { *; }
-dontwarn androidx.room.paging.**

# DataStore & Preference Enums (valueOf / entries)
-keep class androidx.datastore.** { *; }
-keep class com.kadicompanion.data.local.datastore.** { *; }
-keep class com.kadicompanion.domain.model.** { *; }
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Hilt & Jetpack ViewModels
-keep class dagger.hilt.** { *; }
-keep class javax.inject.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class * { *; }
-keep class * extends androidx.lifecycle.ViewModel {
    <init>(...);
}
-keep class * extends androidx.lifecycle.AndroidViewModel {
    <init>(...);
}
