package com.kadicompanion.data.local.db.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "bookmarks",
    indices = [
        Index(value = ["lessonId"]),
        Index(value = ["phaseId"]),
        Index(value = ["docClassName"]),
        Index(value = ["glossaryTerm"]),
        Index(value = ["createdAt"])
    ]
)
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val phaseId: String,
    val lessonId: String? = null,
    val docClassName: String? = null,
    val glossaryTerm: String? = null,
    val createdAt: Long,
    val note: String? = null
)
