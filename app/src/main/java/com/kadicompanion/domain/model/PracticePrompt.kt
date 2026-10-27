package com.kadicompanion.domain.model

data class PracticePrompt(
    val instructions: String,
    val hint: String? = null,
    val solutionCode: String? = null,
    val starterCode: String? = null,
    val requiredTokens: List<String> = emptyList(),
    val expectedOutput: String? = null
)
