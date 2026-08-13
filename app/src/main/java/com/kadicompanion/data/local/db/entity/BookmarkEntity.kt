package com.kadicompanion.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookmarks")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val phaseId: String,
    val lessonId: String?,
    val glossaryTerm: String?,
    val createdAt: Long,
    val note: String?
)
