package com.kadicompanion.domain.model

data class TutorialPhase(
    val id: String,
    val order: Int,
    val title: String,
    val summary: String,
    val estimatedMinutes: Int,
    val lessons: List<Lesson>,
    val concepts: List<String>,
    val programmingConcepts: List<String>,
    val debugChallenge: DebugChallenge? = null,
    val isSkippableForTrack: Set<LearnerTrack> = emptySet(),
    val verifiedAgainstGodotVersion: String
)
