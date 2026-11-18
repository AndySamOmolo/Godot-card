package com.kadicompanion

import com.kadicompanion.data.content.GodotDocSource
import com.kadicompanion.data.content.GodotDocParser
import com.kadicompanion.ui.common.cleanPlainText
import com.kadicompanion.ui.tutorial.practice.PracticeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class KadiCompanionAuditVerificationTest {

    @Test
    fun godotDocParser_parsesClassHeaderMethodsSignalsAndProperties() {
        val sampleMarkdown = """
            ---
            class: Node2D
            inherits: CanvasItem < Node < Object
            verified: 4.3
            short: A 2D game object used in Kadi.
            ---

            # Node2D
            A 2D game object, inherited by all 2D-related nodes.

            ## Properties
            - `position`: `Vector2` = `Vector2(0, 0)` — Position, relative to the node's parent.
            - `rotation`: `float` = `0.0` — Rotation in radians.

            ## Methods
            - `look_at(point: Vector2) -> void` — Rotates the node so that its local +X axis points towards the point.
            - `to_local(global_point: Vector2) -> Vector2` — Transforms a global point to local coordinates.

            ## Signals
            - `visibility_changed()` — Emitted when the node's visibility changes.

        """.trimIndent()

        val entry = GodotDocParser.parse(GodotDocSource("Node2D", sampleMarkdown), "4.3")
        assertEquals("Node2D", entry.className)
        assertEquals("CanvasItem < Node < Object", entry.inherits)
        assertTrue(entry.shortDescription.contains("2D game object"))
        assertEquals(2, entry.properties.size)
        assertEquals("position", entry.properties[0].name)
        assertEquals("Vector2", entry.properties[0].type)
        assertEquals(2, entry.methods.size)
        assertEquals("look_at", entry.methods[0].name)
        assertEquals(1, entry.signals.size)
        assertEquals("visibility_changed()", entry.signals[0].name)
    }

    @Test
    fun gdScriptSyntaxDiagnostics_flagsMissingColonAndUnbalancedBrackets() {
        val validCode = """
            extends Node
            func is_playable(suit: int, top_suit: int) -> bool:
                if suit == top_suit:
                    return true
                return false
        """.trimIndent()
        assertTrue(PracticeViewModel.runGDScriptSyntaxDiagnostics(validCode.lines()).isEmpty())

        val missingColonCode = """
            func is_playable(suit: int) -> bool
                return suit == 0
        """.trimIndent()
        val issues = PracticeViewModel.runGDScriptSyntaxDiagnostics(missingColonCode.lines())
        assertFalse(issues.isEmpty())
        assertTrue(issues.first().contains("must end with a colon"))

        val unbalancedCode = """
            func test() -> void:
                var arr = [1, 2, 3
        """.trimIndent()
        val bracketIssues = PracticeViewModel.runGDScriptSyntaxDiagnostics(unbalancedCode.lines())
        assertFalse(bracketIssues.isEmpty())
        assertTrue(bracketIssues.any { it.contains("Unbalanced square brackets") })
    }

    @Test
    fun cleanPlainText_stripsMarkdownEmphasisAndBackticks() {
        val raw = "Use **`CharacterBody2D.move_and_slide()`** inside `_physics_process(delta)`."
        val cleaned = cleanPlainText(raw)
        assertEquals("Use CharacterBody2D.move_and_slide() inside _physics_process(delta).", cleaned)
        assertNotNull(cleaned)
    }
}
