package com.kadicompanion.data.repository

import com.kadicompanion.data.local.db.dao.BookmarkDao
import com.kadicompanion.data.local.db.dao.LessonProgressDao
import com.kadicompanion.data.local.db.entity.BookmarkEntity
import com.kadicompanion.data.local.db.entity.LessonProgressEntity
import com.kadicompanion.domain.model.Bookmark
import com.kadicompanion.domain.model.LessonProgress
import com.kadicompanion.domain.repository.ProgressRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProgressRepositoryImpl @Inject constructor(
    private val lessonProgressDao: LessonProgressDao,
    private val bookmarkDao: BookmarkDao
) : ProgressRepository {

    override fun observeLessonProgress(lessonId: String): Flow<LessonProgress?> =
        lessonProgressDao.observe(lessonId).map { it?.toDomain() }

    override fun observeAllLessonProgress(): Flow<List<LessonProgress>> =
        lessonProgressDao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun observeBookmarkForLesson(lessonId: String): Flow<Bookmark?> =
        bookmarkDao.observeForLesson(lessonId).map { it?.toDomain() }

    override fun observeBookmarkForPhase(phaseId: String): Flow<Bookmark?> =
        bookmarkDao.observeForPhase(phaseId).map { it?.toDomain() }

    override fun observeBookmarkForDoc(className: String): Flow<Bookmark?> =
        bookmarkDao.observeForDoc(className).map { it?.toDomain() }

    override fun observeBookmarkForGlossary(term: String): Flow<Bookmark?> =
        bookmarkDao.observeForGlossary(term).map { it?.toDomain() }

    override fun observeAllBookmarks(): Flow<List<Bookmark>> =
        bookmarkDao.observeAll().map { list -> list.map { it.toDomain() } }

    override suspend fun markLessonComplete(lessonId: String) {
        val now = System.currentTimeMillis()
        val existing = lessonProgressDao.get(lessonId)
        lessonProgressDao.upsert(
            LessonProgressEntity(
                lessonId = lessonId,
                isCompleted = true,
                lastOpenedAt = existing?.lastOpenedAt ?: now,
                completedAt = now
            )
        )
    }

    override suspend fun markLessonOpened(lessonId: String) {
        val now = System.currentTimeMillis()
        val existing = lessonProgressDao.get(lessonId)
        if (existing == null) {
            lessonProgressDao.upsert(
                LessonProgressEntity(
                    lessonId = lessonId,
                    isCompleted = false,
                    lastOpenedAt = now,
                    completedAt = null
                )
            )
        } else {
            lessonProgressDao.touchOpened(lessonId, now)
        }
    }

    override suspend fun getLastOpenedLesson(): LessonProgress? =
        lessonProgressDao.getLastOpened()?.toDomain()

    override suspend fun addBookmark(
        phaseId: String,
        lessonId: String?,
        docClassName: String?,
        glossaryTerm: String?,
        note: String?
    ) {
        when {
            lessonId != null -> bookmarkDao.deleteForLesson(lessonId)
            docClassName != null -> bookmarkDao.deleteForDoc(docClassName)
            glossaryTerm != null -> bookmarkDao.deleteForGlossary(glossaryTerm)
            else -> bookmarkDao.deleteForPhase(phaseId)
        }
        bookmarkDao.insert(
            BookmarkEntity(
                phaseId = phaseId,
                lessonId = lessonId,
                docClassName = docClassName,
                glossaryTerm = glossaryTerm,
                createdAt = System.currentTimeMillis(),
                note = note?.take(2000)
            )
        )
    }

    override suspend fun removeBookmark(id: Long) = bookmarkDao.delete(id)
    override suspend fun removeBookmarkForLesson(lessonId: String) = bookmarkDao.deleteForLesson(lessonId)
    override suspend fun removeBookmarkForPhase(phaseId: String) = bookmarkDao.deleteForPhase(phaseId)
    override suspend fun removeBookmarkForDoc(className: String) = bookmarkDao.deleteForDoc(className)
    override suspend fun removeBookmarkForGlossary(term: String) = bookmarkDao.deleteForGlossary(term)
    override suspend fun clearAllProgress() = lessonProgressDao.deleteAll()
    override suspend fun clearAllBookmarks() = bookmarkDao.deleteAll()
}

private fun LessonProgressEntity.toDomain() =
    LessonProgress(lessonId, isCompleted, lastOpenedAt, completedAt)

private fun BookmarkEntity.toDomain() =
    Bookmark(id, phaseId, lessonId, docClassName, glossaryTerm, createdAt, note)
