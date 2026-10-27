package com.kadicompanion.data.content

import kotlinx.serialization.Serializable

@Serializable
data class PhaseDto(
    val id: String,
    val order: Int,
    val title: String,
    val summary: String,
    val estimatedMinutes: Int,
    val concepts: List<String>,
    val programmingConcepts: List<String>,
    val isSkippableForTrack: List<String> = emptyList(),
    val verifiedAgainstGodotVersion: String,
    val projectFiles: List<String> = emptyList(),
    val sceneTreeBlueprint: String? = null,
    val learningObjectives: List<String> = emptyList(),
    val lessons: List<LessonDto>,
    val debugChallenge: DebugChallengeDto? = null
)

@Serializable
data class LessonDto(
    val id: String,
    val order: Int,
    val title: String,
    val targetFile: String? = null,
    val sceneTree: String? = null,
    val editorSteps: List<String> = emptyList(),
    val verificationSteps: List<String> = emptyList(),
    val troubleshootingTips: List<String> = emptyList(),
    val keyTakeaways: List<String> = emptyList(),
    val bodyMarkdown: String,
    val codeSnippets: List<CodeSnippetDto> = emptyList(),
    val practicePrompt: PracticePromptDto? = null,
    val quizQuestions: List<QuizQuestionDto> = emptyList()
)

@Serializable
data class CodeSnippetDto(
    val id: String,
    val language: String = "gdscript",
    val code: String,
    val caption: String? = null
)

@Serializable
data class PracticePromptDto(
    val instructions: String,
    val hint: String? = null,
    val starterCode: String? = null,
    val requiredTokens: List<String> = emptyList(),
    val expectedOutput: String? = null,
    val solutionCode: String? = null
)

@Serializable
data class QuizQuestionDto(
    val id: String,
    val prompt: String,
    val choices: List<String>,
    val correctChoiceIndex: Int,
    val explanation: String,
    val concept: String
)

@Serializable
data class DebugChallengeDto(
    val id: String,
    val brokenCode: String,
    val bugDescription: String,
    val fixedCode: String,
    val explanation: String,
    val buggyLineNumber: Int? = null,
    val hint: String? = null
)

@Serializable
data class GlossaryEntryDto(
    val term: String,
    val shortDefinition: String,
    val relatedLessonIds: List<String> = emptyList(),
    val relatedDocClassNames: List<String> = emptyList()
)

@Serializable
data class GlossaryFileDto(
    val terms: List<GlossaryEntryDto>
)
