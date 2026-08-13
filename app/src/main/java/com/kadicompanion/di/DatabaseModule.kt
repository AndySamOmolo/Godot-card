package com.kadicompanion.di

import android.content.Context
import androidx.room.Room
import com.kadicompanion.data.local.db.AppDatabase
import com.kadicompanion.data.local.db.dao.BookmarkDao
import com.kadicompanion.data.local.db.dao.DebugChallengeAttemptDao
import com.kadicompanion.data.local.db.dao.GodotDocDao
import com.kadicompanion.data.local.db.dao.LearnerProfileDao
import com.kadicompanion.data.local.db.dao.LessonProgressDao
import com.kadicompanion.data.local.db.dao.QuizAttemptDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, AppDatabase.NAME)
            .fallbackToDestructiveMigrationOnDowngrade()
            .build()

    @Provides fun provideLessonProgressDao(db: AppDatabase): LessonProgressDao = db.lessonProgressDao()
    @Provides fun provideBookmarkDao(db: AppDatabase): BookmarkDao = db.bookmarkDao()
    @Provides fun provideGodotDocDao(db: AppDatabase): GodotDocDao = db.godotDocDao()
    @Provides fun provideQuizAttemptDao(db: AppDatabase): QuizAttemptDao = db.quizAttemptDao()
    @Provides fun provideDebugChallengeAttemptDao(db: AppDatabase): DebugChallengeAttemptDao = db.debugChallengeAttemptDao()
    @Provides fun provideLearnerProfileDao(db: AppDatabase): LearnerProfileDao = db.learnerProfileDao()
}
