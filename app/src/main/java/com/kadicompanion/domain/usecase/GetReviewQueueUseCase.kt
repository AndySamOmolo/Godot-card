package com.kadicompanion.domain.usecase

import com.kadicompanion.domain.model.ReviewItem
import com.kadicompanion.domain.model.ReviewReason
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetReviewQueueUseCase @Inject constructor(
    private val tutorialRepository: TutorialRepository,
    private val progressRepository: ProgressRepository,
    private val assessmentRepository: AssessmentRepository
) {
    operator fun invoke() = combine(
        assessmentRepository.observeQuizAttempts(),
        progressRepository.observeAllLessonProgress(),
        tutorialRepository.observePhases()
    ) { attempts, progress, phases ->
        val now = System.currentTimeMillis()
        val dayMs = 24L * 60 * 60 * 1000

        val missedByConcept = attempts
            .groupBy { it.concept }
            .mapValues { (_, rows) -> rows.maxBy { it.attemptedAt } }
            .filter { !it.value.wasCorrect }

        val fromMissed = missedByConcept.map { (concept, attempt) ->
            val lessonId = phases
                .flatMap { it.lessons }
                .firstOrNull { lesson -> lesson.quizQuestions.any { q -> q.concept == concept } }
                ?.id
                ?: attempt.questionId
            ReviewItem(concept, ReviewReason.MISSED_QUIZ, lessonId)
        }

        val fromElapsed = phases
            .flatMap { it.lessons }
            .filter { lesson ->
                val prog = progress.firstOrNull { it.lessonId == lesson.id }
                val opened = prog?.lastOpenedAt ?: 0L
                val completedAt = prog?.completedAt
                opened > 0 && (now - opened) > 2 * dayMs && (completedAt == null || (now - completedAt) > 3 * dayMs)
            }
            .flatMap { lesson -> lesson.quizQuestions.map { it.concept } }
            .map { concept ->
                val lessonId = phases
                    .flatMap { it.lessons }
                    .firstOrNull { lesson -> lesson.quizQuestions.any { q -> q.concept == concept } }
                    ?.id
                    ?: ""
                ReviewItem(concept, ReviewReason.TIME_ELAPSED, lessonId)
            }

        (fromMissed + fromElapsed).distinctBy { it.concept }.take(5)
    }
}
