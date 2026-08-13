package com.kadicompanion.domain.model

data class LessonProgress(
    val lessonId: String,
    val isCompleted: Boolean,
    val lastOpenedAt: Long,
    val completedAt: Long? = null
)

data class Bookmark(
    val id: Long,
    val phaseId: String,
    val lessonId: String? = null,
    val glossaryTerm: String? = null,
    val createdAt: Long,
    val note: String? = null
)
