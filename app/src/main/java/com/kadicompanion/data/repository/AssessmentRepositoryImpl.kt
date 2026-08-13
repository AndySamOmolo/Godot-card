package com.kadicompanion.data.repository

import com.kadicompanion.data.local.db.dao.DebugChallengeAttemptDao
import com.kadicompanion.data.local.db.dao.QuizAttemptDao
import com.kadicompanion.data.local.db.entity.DebugChallengeAttemptEntity
import com.kadicompanion.data.local.db.entity.QuizAttemptEntity
import com.kadicompanion.domain.model.DebugChallenge
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.DebugAttemptRecord
import com.kadicompanion.domain.repository.QuizAttemptRecord
import com.kadicompanion.domain.repository.TutorialRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssessmentRepositoryImpl @Inject constructor(
    private val quizAttemptDao: QuizAttemptDao,
    private val debugChallengeAttemptDao: DebugChallengeAttemptDao,
    private val tutorialRepository: TutorialRepository
) : AssessmentRepository {

    override fun observeDebugChallenges(): Flow<Map<String, DebugChallenge>> =
        tutorialRepository.observePhases().map { phases ->
            phases.mapNotNull { phase -> phase.debugChallenge?.let { phase.id to it } }.toMap()
        }

    override suspend fun getDebugChallenge(phaseId: String): DebugChallenge? {
        val phase = tutorialRepository.getPhase(phaseId) ?: return null
        return phase.debugChallenge
    }

    override suspend fun recordQuizAttempt(questionId: String, concept: String, wasCorrect: Boolean) {
        quizAttemptDao.insert(
            QuizAttemptEntity(
                questionId = questionId,
                concept = concept,
                wasCorrect = wasCorrect,
                attemptedAt = System.currentTimeMillis()
            )
        )
    }

    override suspend fun markDebugFixViewed(challengeId: String) {
        val existing = debugChallengeAttemptDao.get(challengeId)
        debugChallengeAttemptDao.upsert(
            DebugChallengeAttemptEntity(
                challengeId = challengeId,
                viewedFix = true,
                completedAt = existing?.completedAt
            )
        )
    }

    override suspend fun markDebugChallengeCompleted(challengeId: String) {
        val existing = debugChallengeAttemptDao.get(challengeId)
        debugChallengeAttemptDao.upsert(
            DebugChallengeAttemptEntity(
                challengeId = challengeId,
                viewedFix = existing?.viewedFix ?: false,
                completedAt = System.currentTimeMillis()
            )
        )
    }

    override fun observeQuizAttempts(): Flow<List<QuizAttemptRecord>> =
        quizAttemptDao.observeAll().map { list ->
            list.map { QuizAttemptRecord(it.questionId, it.concept, it.wasCorrect, it.attemptedAt) }
        }

    override fun observeDebugAttempts(): Flow<List<DebugAttemptRecord>> =
        debugChallengeAttemptDao.observeAll().map { list ->
            list.map { DebugAttemptRecord(it.challengeId, it.viewedFix, it.completedAt) }
        }
}
