package com.kadicompanion.domain.model

data class Lesson(
    val id: String,
    val phaseId: String,
    val order: Int,
    val title: String,
    val bodyMarkdown: String,
    val codeSnippets: List<CodeSnippet>,
    val practicePrompt: PracticePrompt? = null,
    val quizQuestions: List<QuizQuestion> = emptyList()
)
