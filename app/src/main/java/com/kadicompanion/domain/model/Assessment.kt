package com.kadicompanion.domain.model

data class QuizQuestion(
    val id: String,
    val lessonId: String,
    val prompt: String,
    val choices: List<String>,
    val correctChoiceIndex: Int,
    val explanation: String,
    val concept: String
)

data class DebugChallenge(
    val id: String,
    val phaseId: String,
    val brokenCode: String,
    val bugDescription: String,
    val fixedCode: String,
    val explanation: String,
    val buggyLineNumber: Int? = null,
    val hint: String? = null
)
