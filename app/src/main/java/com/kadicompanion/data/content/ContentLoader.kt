package com.kadicompanion.data.content

import android.content.Context
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.domain.model.DebugChallenge
import com.kadicompanion.domain.model.GlossaryEntry
import com.kadicompanion.domain.model.LearnerTrack
import com.kadicompanion.domain.model.Lesson
import com.kadicompanion.domain.model.PracticePrompt
import com.kadicompanion.domain.model.QuizQuestion
import com.kadicompanion.domain.model.TutorialPhase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContentLoader @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) {
    suspend fun loadPhases(): List<TutorialPhase> = withContext(Dispatchers.IO) {
        val paths = context.assets.list("tutorial")?.filter { it.endsWith(".json") } ?: emptyList()
        paths.sorted().mapNotNull { path ->
            runCatching {
                val text = context.assets.open("tutorial/$path").bufferedReader().use { it.readText() }
                val dto = json.decodeFromString<PhaseDto>(text)
                dto.toDomain()
            }.getOrElse { e ->
                android.util.Log.w("ContentLoader", "Failed to load phase $path", e)
                null
            }
        }
    }

    suspend fun loadGlossary(): List<GlossaryEntry> = withContext(Dispatchers.IO) {
        runCatching {
            val text = context.assets.open("glossary/terms.json").bufferedReader().use { it.readText() }
            val dto = json.decodeFromString<GlossaryFileDto>(text)
            dto.terms.map { it.toDomain() }
        }.getOrElse {
            android.util.Log.w("ContentLoader", "Failed to load glossary", it)
            emptyList()
        }
    }

    suspend fun loadGodotDocSources(): List<GodotDocSource> = withContext(Dispatchers.IO) {
        val paths = context.assets.list("godot_docs")?.filter { it.endsWith(".md") } ?: emptyList()
        paths.map { path ->
            val className = path.removeSuffix(".md")
            val markdown = context.assets.open("godot_docs/$path").bufferedReader().use { it.readText() }
            GodotDocSource(className, markdown)
        }
    }

    suspend fun loadContentVersion(): String = withContext(Dispatchers.IO) {
        runCatching {
            context.assets.open("content_version.txt").bufferedReader().use { it.readText().trim() }
        }.getOrElse { "0.0.0-unknown" }
    }
}

data class GodotDocSource(val className: String, val markdown: String)

private fun PhaseDto.toDomain(): TutorialPhase = TutorialPhase(
    id = id,
    order = order,
    title = title,
    summary = summary,
    estimatedMinutes = estimatedMinutes,
    lessons = lessons.sortedBy { it.order }.map { it.toDomain(id) },
    concepts = concepts,
    programmingConcepts = programmingConcepts,
    debugChallenge = debugChallenge?.toDomain(id),
    isSkippableForTrack = isSkippableForTrack.mapNotNull { name ->
        runCatching { LearnerTrack.valueOf(name) }.getOrNull()
    }.toSet(),
    verifiedAgainstGodotVersion = verifiedAgainstGodotVersion,
    projectFiles = projectFiles,
    sceneTreeBlueprint = sceneTreeBlueprint,
    learningObjectives = learningObjectives
)

private fun LessonDto.toDomain(phaseId: String): Lesson = Lesson(
    id = id,
    phaseId = phaseId,
    order = order,
    title = title,
    bodyMarkdown = bodyMarkdown,
    codeSnippets = codeSnippets.map { it.toDomain() },
    practicePrompt = practicePrompt?.toDomain(),
    quizQuestions = quizQuestions.map { it.toDomain(id) },
    targetFile = targetFile,
    sceneTree = sceneTree,
    editorSteps = editorSteps,
    verificationSteps = verificationSteps,
    troubleshootingTips = troubleshootingTips,
    keyTakeaways = keyTakeaways
)

private fun CodeSnippetDto.toDomain(): CodeSnippet = CodeSnippet(id, language, code, caption)

private fun PracticePromptDto.toDomain(): PracticePrompt = PracticePrompt(
    instructions = instructions,
    hint = hint,
    solutionCode = solutionCode,
    starterCode = starterCode,
    requiredTokens = requiredTokens,
    expectedOutput = expectedOutput
)

private fun QuizQuestionDto.toDomain(lessonId: String): QuizQuestion = QuizQuestion(
    id = id,
    lessonId = lessonId,
    prompt = prompt,
    choices = choices,
    correctChoiceIndex = correctChoiceIndex,
    explanation = explanation,
    concept = concept
)

private fun DebugChallengeDto.toDomain(phaseId: String): DebugChallenge = DebugChallenge(
    id = id,
    phaseId = phaseId,
    brokenCode = brokenCode,
    bugDescription = bugDescription,
    fixedCode = fixedCode,
    explanation = explanation,
    buggyLineNumber = buggyLineNumber,
    hint = hint
)

private fun GlossaryEntryDto.toDomain(): GlossaryEntry = GlossaryEntry(
    term = term,
    shortDefinition = shortDefinition,
    relatedLessonIds = relatedLessonIds,
    relatedDocClassNames = relatedDocClassNames
)
