package com.kadicompanion.domain.usecase

import com.kadicompanion.domain.repository.ProgressRepository
import javax.inject.Inject

class MarkLessonCompleteUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {
    suspend operator fun invoke(lessonId: String) {
        progressRepository.markLessonComplete(lessonId)
    }
}

class MarkLessonOpenedUseCase @Inject constructor(
    private val progressRepository: ProgressRepository
) {
    suspend operator fun invoke(lessonId: String) {
        progressRepository.markLessonOpened(lessonId)
    }
}
