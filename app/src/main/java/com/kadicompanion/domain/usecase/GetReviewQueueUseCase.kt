package com.kadicompanion.domain.usecase

import com.kadicompanion.domain.model.ReviewItem
import com.kadicompanion.domain.model.ReviewReason
import com.kadicompanion.domain.repository.AssessmentRepository
import com.kadicompanion.domain.repository.ProgressRepository
import com.kadicompanion.domain.repository.TutorialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
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

        val allLessons = phases.flatMap { it.lessons }
        val lessonByConcept = buildMap<String, String> {
            for (lesson in allLessons) {
                for (q in lesson.quizQuestions) {
                    putIfAbsent(q.concept, lesson.id)
                }
            }
        }
        val progressById = progress.associateBy { it.lessonId }

        val missedByConcept = attempts
            .groupBy { it.concept }
            .mapValues { (_, rows) -> rows.maxBy { it.attemptedAt } }
            .filter { !it.value.wasCorrect }

        val fromMissed = missedByConcept.keys.mapNotNull { concept ->
            val lessonId = lessonByConcept[concept] ?: return@mapNotNull null
            ReviewItem(concept, ReviewReason.MISSED_QUIZ, lessonId)
        }

        val fromElapsed = allLessons
            .filter { lesson ->
                val prog = progressById[lesson.id]
                val opened = prog?.lastOpenedAt ?: 0L
                val completedAt = prog?.completedAt
                opened > 0 && (now - opened) > 2 * dayMs && (completedAt == null || (now - completedAt) > 3 * dayMs)
            }
            .flatMap { lesson -> lesson.quizQuestions.map { it.concept } }
            .mapNotNull { concept ->
                val lessonId = lessonByConcept[concept] ?: return@mapNotNull null
                ReviewItem(concept, ReviewReason.TIME_ELAPSED, lessonId)
            }

        (fromMissed + fromElapsed).distinctBy { it.concept }.take(5)
    }.flowOn(Dispatchers.Default)
}
