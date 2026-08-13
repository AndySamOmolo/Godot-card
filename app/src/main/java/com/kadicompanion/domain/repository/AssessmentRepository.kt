package com.kadicompanion.domain.repository

import com.kadicompanion.domain.model.DebugChallenge
import kotlinx.coroutines.flow.Flow

interface AssessmentRepository {
    fun observeDebugChallenges(): Flow<Map<String, DebugChallenge>>
    suspend fun getDebugChallenge(phaseId: String): DebugChallenge?
    suspend fun recordQuizAttempt(questionId: String, concept: String, wasCorrect: Boolean)
    suspend fun markDebugFixViewed(challengeId: String)
    suspend fun markDebugChallengeCompleted(challengeId: String)
    fun observeQuizAttempts(): Flow<List<QuizAttemptRecord>>
    fun observeDebugAttempts(): Flow<List<DebugAttemptRecord>>
}

data class QuizAttemptRecord(
    val questionId: String,
    val concept: String,
    val wasCorrect: Boolean,
    val attemptedAt: Long
)

data class DebugAttemptRecord(
    val challengeId: String,
    val viewedFix: Boolean,
    val completedAt: Long?
)
