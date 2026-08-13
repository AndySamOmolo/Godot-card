package com.kadicompanion.domain.repository

import com.kadicompanion.domain.model.TutorialPhase
import com.kadicompanion.domain.model.Lesson
import kotlinx.coroutines.flow.Flow

interface TutorialRepository {
    fun observePhases(): Flow<List<TutorialPhase>>
    suspend fun getPhase(phaseId: String): TutorialPhase?
    suspend fun getLesson(lessonId: String): Lesson?
    suspend fun getNextLesson(lessonId: String): Lesson?
    suspend fun getFirstLessonOfNextPhase(phaseId: String): Lesson?
}
