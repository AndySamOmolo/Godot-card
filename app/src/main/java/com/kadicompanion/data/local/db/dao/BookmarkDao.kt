package com.kadicompanion.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kadicompanion.data.local.db.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookmarkDao {
    @Query("SELECT * FROM bookmarks ORDER BY createdAt DESC")
    fun observeAll(): Flow<List<BookmarkEntity>>

    @Query("SELECT * FROM bookmarks WHERE lessonId = :lessonId LIMIT 1")
    fun observeForLesson(lessonId: String): Flow<BookmarkEntity?>

    @Query("SELECT * FROM bookmarks WHERE phaseId = :phaseId AND lessonId IS NULL AND glossaryTerm IS NULL LIMIT 1")
    fun observeForPhase(phaseId: String): Flow<BookmarkEntity?>

    @Query("SELECT * FROM bookmarks WHERE glossaryTerm = :term LIMIT 1")
    fun observeForGlossary(term: String): Flow<BookmarkEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: BookmarkEntity): Long

    @Query("DELETE FROM bookmarks WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("DELETE FROM bookmarks WHERE lessonId = :lessonId")
    suspend fun deleteForLesson(lessonId: String)

    @Query("DELETE FROM bookmarks WHERE phaseId = :phaseId AND lessonId IS NULL AND glossaryTerm IS NULL")
    suspend fun deleteForPhase(phaseId: String)

    @Query("DELETE FROM bookmarks WHERE glossaryTerm = :term")
    suspend fun deleteForGlossary(term: String)
}
