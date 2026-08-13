package com.kadicompanion.domain.model

data class GlossaryEntry(
    val term: String,
    val shortDefinition: String,
    val relatedLessonIds: List<String>,
    val relatedDocClassNames: List<String>
)

data class ReviewItem(
    val concept: String,
    val reasonSurfaced: ReviewReason,
    val sourceLessonId: String
)

enum class ReviewReason { MISSED_QUIZ, TIME_ELAPSED }
