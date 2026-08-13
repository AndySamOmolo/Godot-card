package com.kadicompanion.domain.repository

import com.kadicompanion.domain.model.Bookmark
import com.kadicompanion.domain.model.LessonProgress
import kotlinx.coroutines.flow.Flow

interface ProgressRepository {
    fun observeLessonProgress(lessonId: String): Flow<LessonProgress?>
    fun observeAllLessonProgress(): Flow<List<LessonProgress>>
    fun observeBookmarkForLesson(lessonId: String): Flow<Bookmark?>
    fun observeBookmarkForPhase(phaseId: String): Flow<Bookmark?>
    fun observeBookmarkForGlossary(term: String): Flow<Bookmark?>
    fun observeAllBookmarks(): Flow<List<Bookmark>>
    suspend fun markLessonComplete(lessonId: String)
    suspend fun markLessonOpened(lessonId: String)
    suspend fun getLastOpenedLesson(): LessonProgress?
    suspend fun addBookmark(phaseId: String, lessonId: String? = null, glossaryTerm: String? = null, note: String? = null)
    suspend fun removeBookmark(id: Long)
    suspend fun removeBookmarkForLesson(lessonId: String)
    suspend fun removeBookmarkForPhase(phaseId: String)
    suspend fun removeBookmarkForGlossary(term: String)
}
