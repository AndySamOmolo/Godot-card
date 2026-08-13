package com.kadicompanion.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kadicompanion.data.local.db.dao.BookmarkDao
import com.kadicompanion.data.local.db.dao.DebugChallengeAttemptDao
import com.kadicompanion.data.local.db.dao.GodotDocDao
import com.kadicompanion.data.local.db.dao.LearnerProfileDao
import com.kadicompanion.data.local.db.dao.LessonProgressDao
import com.kadicompanion.data.local.db.dao.QuizAttemptDao
import com.kadicompanion.data.local.db.entity.BookmarkEntity
import com.kadicompanion.data.local.db.entity.DebugChallengeAttemptEntity
import com.kadicompanion.data.local.db.entity.GodotDocEntity
import com.kadicompanion.data.local.db.entity.GodotDocFtsEntity
import com.kadicompanion.data.local.db.entity.LearnerProfileEntity
import com.kadicompanion.data.local.db.entity.LessonProgressEntity
import com.kadicompanion.data.local.db.entity.QuizAttemptEntity

@Database(
    entities = [
        LessonProgressEntity::class,
        BookmarkEntity::class,
        GodotDocEntity::class,
        GodotDocFtsEntity::class,
        QuizAttemptEntity::class,
        DebugChallengeAttemptEntity::class,
        LearnerProfileEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun lessonProgressDao(): LessonProgressDao
    abstract fun bookmarkDao(): BookmarkDao
    abstract fun godotDocDao(): GodotDocDao
    abstract fun quizAttemptDao(): QuizAttemptDao
    abstract fun debugChallengeAttemptDao(): DebugChallengeAttemptDao
    abstract fun learnerProfileDao(): LearnerProfileDao
    companion object {
        const val NAME = "kadi_companion.db"
    }
}
