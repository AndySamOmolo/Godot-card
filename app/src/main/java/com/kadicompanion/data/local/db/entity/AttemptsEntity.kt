package com.kadicompanion.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "quiz_attempts")
data class QuizAttemptEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val questionId: String,
    val concept: String,
    val wasCorrect: Boolean,
    val attemptedAt: Long
)

@Entity(tableName = "debug_challenge_attempts")
data class DebugChallengeAttemptEntity(
    @PrimaryKey val challengeId: String,
    val viewedFix: Boolean,
    val completedAt: Long?
)

@Entity(tableName = "learner_profile")
data class LearnerProfileEntity(
    @PrimaryKey val id: Int = 0,
    val track: String,
    val onboardedAt: Long
)
