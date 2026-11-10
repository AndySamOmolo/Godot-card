package com.kadicompanion.ui.docs.doclist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.DataObject
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kadicompanion.R
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.domain.model.GodotDocEntry
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.EmptyState
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.LoadingSkeleton
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.glossary.GlossaryListScreen
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalReduceMotion
import com.kadicompanion.ui.theme.glassBorderColor

private val UI_CLASSES = setOf(
    "Control", "Button", "Label", "RichTextLabel", "Panel", "ColorRect", "TextureRect",
    "TextureButton", "OptionButton", "CheckButton", "LineEdit", "TextEdit", "ProgressBar",
    "Slider", "Container", "HBoxContainer", "VBoxContainer", "GridContainer", "MarginContainer",
    "ScrollContainer", "TabContainer", "Theme", "Window"
)

private val CARD_2D_CLASSES = setOf(
    "Node", "Node2D", "CanvasItem", "CanvasLayer", "Sprite2D", "Area2D", "CollisionShape2D",
    "CharacterBody2D", "RigidBody2D", "StaticBody2D", "Camera2D", "Marker2D", "CPUParticles2D",
    "GPUParticles2D", "Texture2D", "Viewport", "SceneTree", "PackedScene"
)

private val AUDIO_ANIM_CLASSES = setOf(
    "Tween", "Timer", "AnimationPlayer", "AnimationTree", "AudioStreamPlayer",
    "AudioStreamPlayer2D", "AudioStreamPlayer3D"
)

@Composable
fun DocsListScreen(
    onDocClick: (String) -> Unit,
    onOpenGlossary: (String) -> Unit,
    vm: DocsListViewModel = hiltViewModel()
) {
    val state by vm.uiState.collectAsStateWithLifecycle()
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val hPad = studioScreenPadding()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = stringResource(R.string.nav_docs),
                subtitle = "GODOT 4.3 API · COOKBOOK · GLOSSARY",
                actions = {
                    if (state.all.isNotEmpty()) {
                        Pill(
                            text = "${state.all.size} classes · ${COOKBOOK_RECIPES.size} recipes",
                            modifier = Modifier.padding(end = 4.dp),
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            DocsSegmentedTabs(
                selected = selectedTab,
                onSelect = { selectedTab = it },
                modifier = Modifier.padding(horizontal = hPad, vertical = 4.dp)
            )
            when (selectedTab) {
                0 -> ClassesTab(state = state, onQueryChange = vm::setQuery, onDocClick = onDocClick)
                1 -> GDScriptCookbookTab(onDocClick = onDocClick, onOpenGlossary = onOpenGlossary)
                2 -> GlossaryListScreen(onTermClick = onOpenGlossary)
            }
        }
    }
}

@Composable
private fun DocsSegmentedTabs(selected: Int, onSelect: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            .border(BorderStroke(1.dp, glassBorderColor()), MaterialTheme.shapes.medium)
            .padding(4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        SegmentButton(
            label = "Classes",
            icon = Icons.Outlined.DataObject,
            active = selected == 0,
            onClick = { onSelect(0) },
            modifier = Modifier.weight(1f)
        )
        SegmentButton(
            label = "Cookbook",
            icon = Icons.Outlined.Code,
            active = selected == 1,
            onClick = { onSelect(1) },
            modifier = Modifier.weight(1f)
        )
        SegmentButton(
            label = "Glossary",
            icon = Icons.Outlined.Translate,
            active = selected == 2,
            onClick = { onSelect(2) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SegmentButton(
    label: String,
    icon: ImageVector,
    active: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val content = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.16f) else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = content, modifier = Modifier.size(15.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = content,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}

@Composable
private fun ClassesTab(state: DocsListUiState, onQueryChange: (String) -> Unit, onDocClick: (String) -> Unit) {
    var selectedCategory by rememberSaveable { mutableIntStateOf(0) }
    val categories = listOf("All", "UI & Controls", "2D & Scenes", "Audio & Timers", "Core & Data")
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    val categoryFiltered = state.filtered.filter { entry ->
        when (selectedCategory) {
            1 -> entry.className in UI_CLASSES
            2 -> entry.className in CARD_2D_CLASSES
            3 -> entry.className in AUDIO_ANIM_CLASSES
            4 -> entry.className !in UI_CLASSES && entry.className !in CARD_2D_CLASSES && entry.className !in AUDIO_ANIM_CLASSES
            else -> true
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SearchPill(
            query = state.query,
            onQueryChange = onQueryChange,
            placeholder = stringResource(R.string.search_docs_placeholder),
            modifier = Modifier.padding(horizontal = hPad)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = hPad),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEachIndexed { idx, label ->
                val active = selectedCategory == idx
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (active) MaterialTheme.colorScheme.secondary.copy(alpha = 0.20f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        width = if (active) 1.5.dp else 1.dp,
                        color = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.clickable { selectedCategory = idx }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        color = if (active) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
        when {
            state.isLoading -> LoadingSkeleton(lines = 7)
            state.filtered.isEmpty() && state.query.isBlank() -> EmptyState(
                title = "Building search index",
                message = "Bundled Godot docs are being parsed on first launch."
            )
            categoryFiltered.isEmpty() -> EmptyState(
                title = "No matches",
                message = stringResource(R.string.empty_search)
            )
            else -> LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = hPad, end = hPad, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(vGap)
            ) {
                item {
                    SectionHeader(
                        title = categories[selectedCategory],
                        eyebrow = "GODOT 4.3 CLASSES",
                        trailing = { Pill(text = "${categoryFiltered.size}", color = MaterialTheme.colorScheme.secondary) }
                    )
                }
                items(categoryFiltered, key = { it.className }) { entry ->
                    ClassRow(entry = entry, onClick = { onDocClick(entry.className) })
                }
            }
        }
    }
}

private data class CookbookRecipe(
    val id: String,
    val category: String,
    val title: String,
    val question: String,
    val relatedClasses: List<String>,
    val explanation: String,
    val code: String
)

private val COOKBOOK_CATEGORIES = listOf(
    "All Recipes",
    "Movement & Physics",
    "Spawning & Scenes",
    "UI, Input & Signals",
    "Save, Data & Audio",
    "Timers, Tweens & Camera",
    "Architecture & AI"
)

private val COOKBOOK_RECIPES = listOf(
    CookbookRecipe(
        id = "cb_topdown_move",
        category = "Movement & Physics",
        title = "Smooth 8-Way Top-Down RPG / Survivor Movement",
        question = "How do I move a player character in 8 directions with smooth acceleration and collision?",
        relatedClasses = listOf("CharacterBody2D", "Input", "Vector2"),
        explanation = "Use `Input.get_vector()` to read a normalized `Vector2` from four input actions, then interpolate `velocity` with `move_toward()` inside `_physics_process(delta)` before calling `move_and_slide()`.",
        code = """
        extends CharacterBody2D

        @export var max_speed: float = 240.0
        @export var acceleration: float = 1400.0
        @export var friction: float = 1200.0

        func _physics_process(delta: float) -> void:
            var dir: Vector2 = Input.get_vector("move_left", "move_right", "move_up", "move_down")
            if dir != Vector2.ZERO:
                velocity = velocity.move_toward(dir * max_speed, acceleration * delta)
            else:
                velocity = velocity.move_toward(Vector2.ZERO, friction * delta)
            move_and_slide()
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_platformer_jump",
        category = "Movement & Physics",
        title = "2D Platformer Gravity, Jump & Coyote Time",
        question = "How do I build responsive 2D platformer jumping with gravity and a forgiving ledge-jump window?",
        relatedClasses = listOf("CharacterBody2D", "Input"),
        explanation = "Track `coyote_timer` whenever `is_on_floor()` is true so the player can still jump for `0.12s` after walking off a ledge, and cut upward `velocity.y` in half if the jump button is released early.",
        code = """
        extends CharacterBody2D

        const SPEED: float = 260.0
        const JUMP_VELOCITY: float = -440.0
        const GRAVITY: float = 1200.0
        var coyote_timer: float = 0.0

        func _physics_process(delta: float) -> void:
            if is_on_floor():
                coyote_timer = 0.12
            else:
                coyote_timer = maxf(0.0, coyote_timer - delta)
                velocity.y += GRAVITY * delta

            if Input.is_action_just_pressed("jump") and coyote_timer > 0.0:
                velocity.y = JUMP_VELOCITY
                coyote_timer = 0.0
            if Input.is_action_just_released("jump") and velocity.y < 0.0:
                velocity.y *= 0.5

            var axis: float = Input.get_axis("move_left", "move_right")
            velocity.x = axis * SPEED
            move_and_slide()
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_hitbox_hurtbox",
        category = "Movement & Physics",
        title = "Reusable Hitbox & Hurtbox Damage System (Area2D)",
        question = "How do I make bullets, swords, or spikes damage any enemy or player without coupling scripts?",
        relatedClasses = listOf("Area2D", "CollisionShape2D"),
        explanation = "Create a `Hurtbox` (`Area2D`) that listens for `area_entered`. Whenever a `Hitbox` (`Area2D` in group `\"hitbox\"`) overlaps it, read `hitbox.damage` and apply it to your `HealthComponent`.",
        code = """
        # Attach to Hurtbox (Area2D):
        extends Area2D
        signal damage_taken(amount: int)

        func _ready() -> void:
            area_entered.connect(_on_area_entered)

        func _on_area_entered(other: Area2D) -> void:
            if other.is_in_group("hitbox"):
                var dmg: int = other.get("damage") if "damage" in other else 1
                damage_taken.emit(dmg)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_drag_and_drop",
        category = "UI, Input & Signals",
        title = "Drag-and-Drop Any UI Card or Inventory Item",
        question = "How do I let players drag a Card or Item with mouse/finger and drop it onto a target slot?",
        relatedClasses = listOf("Control", "InputEvent", "Rect2"),
        explanation = "Override `_gui_input(event)` on your `Card` `Control`. Track `dragging` on button press/release, follow `get_global_mouse_position()`, and check `drop_slot.get_global_rect().has_point(...)` when released.",
        code = """
        extends Control
        signal card_dropped_on_target(card: Control)

        var dragging: bool = false
        var drag_offset: Vector2 = Vector2.ZERO
        var home_position: Vector2 = Vector2.ZERO
        @export var drop_target: Control

        func _gui_input(event: InputEvent) -> void:
            if event is InputEventMouseButton and event.button_index == MOUSE_BUTTON_LEFT:
                if event.pressed:
                    dragging = true
                    home_position = position
                    drag_offset = get_global_mouse_position() - global_position
                else:
                    dragging = false
                    if drop_target and drop_target.get_global_rect().has_point(get_global_mouse_position()):
                        card_dropped_on_target.emit(self)
                    else:
                        create_tween().tween_property(self, "position", home_position, 0.15)
            elif event is InputEventMouseMotion and dragging:
                global_position = get_global_mouse_position() - drag_offset
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_spawn_scene",
        category = "Spawning & Scenes",
        title = "Spawning Cards, Bullets, or Enemies from a PackedScene",
        question = "How do I instantiate a `.tscn` template in code, position it, and pass data to it safely?",
        relatedClasses = listOf("PackedScene", "Node2D", "Marker2D"),
        explanation = "Preload the `.tscn` as a `const PackedScene`, call `.instantiate()`, add it to the parent node FIRST with `add_child()` (so `@onready` children initialize), then set `global_position` or call `setup()`.",
        code = """
        const ProjectileScene: PackedScene = preload("res://scenes/bullet.tscn")
        @onready var muzzle: Marker2D = ${'$'}Muzzle

        func fire_projectile(direction: Vector2) -> void:
            var b: Node2D = ProjectileScene.instantiate()
            get_tree().current_scene.add_child(b) # 1. Add to tree FIRST
            b.global_position = muzzle.global_position # 2. Position & configure
            b.setup(direction)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_scene_transition",
        category = "Spawning & Scenes",
        title = "Switching Scenes (Main Menu -> Game -> Game Over)",
        question = "How do I switch between a Main Menu `.tscn` and my Gameplay `.tscn` without losing global state?",
        relatedClasses = listOf("SceneTree", "Node"),
        explanation = "Store persistent match settings or scores in an Autoload singleton (like `GameState`), then call `get_tree().change_scene_to_file(\"res://...\")` to swap the active scene cleanly.",
        code = """
        # Inside MainMenu.gd when StartButton is pressed:
        func _on_start_button_pressed() -> void:
            GameState.selected_player_count = 4
            get_tree().change_scene_to_file("res://scenes/table/table.tscn")

        # Inside Table.gd when QuitToMenuButton is pressed:
        func _on_quit_to_menu_pressed() -> void:
            get_tree().change_scene_to_file("res://scenes/ui/main_menu.tscn")
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_save_load_json",
        category = "Save, Data & Audio",
        title = "Saving & Loading Player Progress to Disk (JSON + FileAccess)",
        question = "How do I save high scores, coins, unlocked decks, or settings so they persist when the game closes?",
        relatedClasses = listOf("FileAccess", "JSON"),
        explanation = "Always write save files to `user://` (never `res://`, which is read-only after exporting!). Convert a `Dictionary` with `JSON.stringify()` on save and `JSON.parse_string()` on load.",
        code = """
        const SAVE_PATH: String = "user://save_profile.json"

        func save_profile(wins: int, coins: int) -> void:
            var data: Dictionary = {"wins": wins, "coins": coins}
            var file := FileAccess.open(SAVE_PATH, FileAccess.WRITE)
            if file != null:
                file.store_string(JSON.stringify(data, "\t"))

        func load_profile() -> Dictionary:
            if not FileAccess.file_exists(SAVE_PATH):
                return {"wins": 0, "coins": 100}
            var text: String = FileAccess.get_file_as_string(SAVE_PATH)
            var parsed: Variant = JSON.parse_string(text)
            return parsed if parsed is Dictionary else {"wins": 0, "coins": 100}
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_custom_resources",
        category = "Save, Data & Audio",
        title = "Data-Driven Items, Cards, or Weapons with Custom Resources",
        question = "How do I design cards, RPG items, or enemy stats in the Inspector without hardcoding Dictionaries?",
        relatedClasses = listOf("Resource", "ResourceLoader"),
        explanation = "Create a script extending `Resource` with `class_name` and `@export` variables. You can then right-click in the FileSystem dock -> **New -> Resource** to create `.tres` data files editable in the Inspector!",
        code = """
        # File: res://scripts/item_data.gd
        class_name ItemData
        extends Resource

        @export var item_name: String = "Healing Potion"
        @export var icon: Texture2D
        @export var power: int = 25
        @export_multiline var description: String = "Restores 25 HP."
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_await_timer",
        category = "Timers, Tweens & Camera",
        title = "Pausing Execution in a Function (`await` Coroutines)",
        question = "How do I wait 0.5 seconds between dealing cards or wait for an animation/dialog to finish?",
        relatedClasses = listOf("SceneTree", "Timer", "Signal"),
        explanation = "In Godot 4.3, `await` pauses the current function without freezing the game loop and resumes as soon as the awaited Signal (such as `create_timer(0.5).timeout` or `tween.finished`) emits!",
        code = """
        func deal_cards_animated(cards: Array) -> void:
            for c in cards:
                spawn_card_on_table(c)
                # Pause 0.18s between each card dealt without freezing the UI:
                await get_tree().create_timer(0.18).timeout
            print("All cards dealt!")
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_card_flip_tween",
        category = "Timers, Tweens & Camera",
        title = "Smooth 2D Card Flip & Juicy UI Pop with Tweens",
        question = "How do I animate a card flipping from face-down to face-up in 2D using `create_tween()`?",
        relatedClasses = listOf("Tween", "Control"),
        explanation = "Set `pivot_offset = size / 2.0` so the card scales from its center, shrink `scale.x` from `1.0` to `0.0` over `0.12s`, swap the texture via `tween_callback()`, then expand `scale.x` back to `1.0`!",
        code = """
        func flip_card_to_front(card_node: Control, front_tex: Texture2D) -> void:
            card_node.pivot_offset = card_node.size * 0.5
            var tw: Tween = create_tween()
            tw.tween_property(card_node, "scale:x", 0.0, 0.12).set_trans(Tween.TRANS_SINE)
            tw.tween_callback(func() -> void:
                card_node.get_node("CardArt").texture = front_tex
            )
            tw.tween_property(card_node, "scale:x", 1.0, 0.12).set_trans(Tween.TRANS_BACK)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_camera_shake",
        category = "Timers, Tweens & Camera",
        title = "Juicy 2D Screen Shake on Penalty (+5 Joker) or Explosion",
        question = "How do I shake the screen when a +5 Joker hits the table or an explosion happens?",
        relatedClasses = listOf("Camera2D", "RandomNumberGenerator", "Tween"),
        explanation = "On your `Camera2D` (or root `Table` `Node2D`), animate random `offset` vectors decaying back to `Vector2.ZERO` over `0.25s`.",
        code = """
        func shake_camera(camera: Camera2D, strength: float = 10.0) -> void:
            var tw: Tween = create_tween()
            for i in range(6):
                var rand_offset := Vector2(randf_range(-strength, strength), randf_range(-strength, strength))
                tw.tween_property(camera, "offset", rand_offset, 0.035)
                strength *= 0.75
            tw.tween_property(camera, "offset", Vector2.ZERO, 0.04)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_state_machine",
        category = "Architecture & AI",
        title = "Clean Enum Finite State Machine (Player, Boss, or Turn Phase)",
        question = "How do I prevent messy `if/else` spaghetti when a character or game has multiple states?",
        relatedClasses = listOf("Node"),
        explanation = "Define an `enum State`, store `var current_state: State`, and route logic through a `match current_state:` block with a dedicated `transition_to(new_state)` helper.",
        code = """
        enum State { IDLE, SELECTING_CARDS, AWAITING_SUIT_POPUP, AI_THINKING, MATCH_OVER }
        var state: State = State.IDLE

        func transition_to(next_state: State) -> void:
            state = next_state
            match state:
                State.IDLE:
                    status_label.text = "Your turn — pick a card!"
                State.AWAITING_SUIT_POPUP:
                    declare_suit_dialog.show()
                State.AI_THINKING:
                    ai_turn_timer.start(0.75)
                State.MATCH_OVER:
                    play_button.disabled = true
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_signal_bus",
        category = "Architecture & AI",
        title = "Global Event Bus Autoload (`Events.gd`) for Decoupled UI",
        question = "How do deeply nested nodes (like cards or enemies) notify the HUD or Audio player without fragile `get_parent()` chains?",
        relatedClasses = listOf("Signal", "Node"),
        explanation = "Register an Autoload singleton named `Events` containing only `signal` declarations. Any script can emit `Events.score_changed.emit(new_score)` and any UI node can connect to it!",
        code = """
        # Autoload Singleton: res://scripts/events.gd (Registered as "Events")
        extends Node
        signal card_played_fx(card_data: CardData, global_pos: Vector2)
        signal achievement_unlocked(title: String)

        # In CardScene.gd:
        Events.card_played_fx.emit(data, global_position)

        # In HUD.gd _ready():
        Events.card_played_fx.connect(_on_card_played_fx)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_weighted_loot",
        category = "Architecture & AI",
        title = "Weighted Random Choice (Card Packs, Loot Drops, AI Decisions)",
        question = "How do I pick a random item where Common is 70%, Rare is 25%, and Legendary is 5%?",
        relatedClasses = listOf("RandomNumberGenerator", "Array", "Dictionary"),
        explanation = "Sum all weights, roll `randf_range(0.0, total_weight)`, and subtract each entry's weight until the roll drops below zero.",
        code = """
        func pick_weighted(loot_table: Array[Dictionary]) -> String:
            var total_weight: float = 0.0
            for entry in loot_table:
                total_weight += float(entry["weight"])
            var roll: float = randf_range(0.0, total_weight)
            for entry in loot_table:
                roll -= float(entry["weight"])
                if roll <= 0.0:
                    return entry["id"]
            return loot_table.back()["id"]
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_multiplayer_rpc",
        category = "Architecture & AI",
        title = "LAN / Online Multiplayer Sync with `ENetMultiplayerPeer` & `@rpc`",
        question = "How do I host a 4-player LAN game and sync card plays or player moves across devices in Godot 4.3?",
        relatedClasses = listOf("Node", "SceneTree"),
        explanation = "Use `ENetMultiplayerPeer` to host or join a port, and mark synchronized functions with `@rpc(\"any_peer\", \"call_local\", \"reliable\")` so calling `play_card_Sync.rpc(suit, rank)` executes on every connected peer!",
        code = """
        func host_lan_match(port: int = 8910) -> void:
            var peer := ENetMultiplayerPeer.new()
            peer.create_server(port, 4)
            multiplayer.multiplayer_peer = peer

        func join_lan_match(ip: String, port: int = 8910) -> void:
            var peer := ENetMultiplayerPeer.new()
            peer.create_client(ip, port)
            multiplayer.multiplayer_peer = peer

        @rpc("any_peer", "call_local", "reliable")
        func sync_card_play(player_seat: int, suit: int, rank: int) -> void:
            var played := CardData.make(suit as CardTypes.Suits, rank)
            GameState.discard_pile.append(played)
            GameState.state_updated.emit()
        """.trimIndent()
    )
)

@Composable
private fun GDScriptCookbookTab(
    onDocClick: (String) -> Unit,
    onOpenGlossary: (String) -> Unit
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var selectedCat by rememberSaveable { mutableIntStateOf(0) }
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    val filtered = COOKBOOK_RECIPES.filter { r ->
        val matchesCat = selectedCat == 0 || r.category == COOKBOOK_CATEGORIES[selectedCat]
        val matchesQuery = searchQuery.isBlank() ||
            r.title.contains(searchQuery, ignoreCase = true) ||
            r.question.contains(searchQuery, ignoreCase = true) ||
            r.explanation.contains(searchQuery, ignoreCase = true) ||
            r.code.contains(searchQuery, ignoreCase = true) ||
            r.relatedClasses.any { it.contains(searchQuery, ignoreCase = true) }
        matchesCat && matchesQuery
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        SearchPill(
            query = searchQuery,
            onQueryChange = { searchQuery = it },
            placeholder = "Search recipes (e.g. jump, drag, save, timer, shake, rpc)...",
            modifier = Modifier.padding(horizontal = hPad)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = hPad),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            COOKBOOK_CATEGORIES.forEachIndexed { idx, label ->
                val active = selectedCat == idx
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = if (active) MaterialTheme.colorScheme.primary.copy(alpha = 0.20f)
                    else MaterialTheme.colorScheme.surfaceContainerHigh,
                    border = BorderStroke(
                        width = if (active) 1.5.dp else 1.dp,
                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                    ),
                    modifier = Modifier.clickable { selectedCat = idx }
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (active) FontWeight.Bold else FontWeight.Medium,
                        color = if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        if (filtered.isEmpty()) {
            EmptyState(
                title = "No matching recipes",
                message = "Try searching for movement, save, tween, signal, or spawn."
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = hPad, end = hPad, bottom = 28.dp),
                verticalArrangement = Arrangement.spacedBy(vGap)
            ) {
                item {
                    SectionHeader(
                        title = COOKBOOK_CATEGORIES[selectedCat],
                        eyebrow = "SOLO GAME DEV GDSCRIPT COOKBOOK",
                        trailing = { Pill(text = "${filtered.size} patterns", color = MaterialTheme.colorScheme.primary) }
                    )
                }
                items(filtered, key = { it.id }) { recipe ->
                    KadiCard(accent = MaterialTheme.colorScheme.primary) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Pill(
                                text = recipe.category,
                                color = MaterialTheme.colorScheme.secondary,
                                icon = Icons.Outlined.AutoAwesome
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                recipe.relatedClasses.forEach { cls ->
                                    Pill(
                                        text = cls,
                                        color = MaterialTheme.colorScheme.primary,
                                        onClick = { onDocClick(cls) }
                                    )
                                }
                            }
                        }
                        Text(
                            text = recipe.title,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Q: ${recipe.question}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        RichInlineText(
                            text = recipe.explanation,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            onTermClick = onOpenGlossary
                        )
                        CodeBlock(
                            snippet = CodeSnippet(
                                id = recipe.id,
                                language = "gdscript",
                                code = recipe.code,
                                caption = "Tap classes above to open full Godot 4.3 API docs."
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ClassRow(entry: GodotDocEntry, onClick: () -> Unit) {
    KadiCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            IconBubble(icon = Icons.AutoMirrored.Outlined.MenuBook, size = 42.dp)
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = entry.className,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = CodeFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )
                entry.inherits?.let {
                    Text(
                        text = "extends $it",
                        style = MaterialTheme.typography.labelSmall,
                        fontFamily = CodeFontFamily,
                        color = MaterialTheme.colorScheme.secondary,
                        maxLines = 1
                    )
                }
                RichInlineText(
                    text = entry.shortDescription,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchPill(
    query: String,
    onQueryChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier
) {
    val duration = if (LocalReduceMotion.current) 0 else 150
    val container = MaterialTheme.colorScheme.surfaceContainerHigh
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1
            )
        },
        leadingIcon = {
            Icon(Icons.Outlined.Search, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
        },
        trailingIcon = {
            AnimatedVisibility(
                visible = query.isNotEmpty(),
                enter = fadeIn(tween(duration)),
                exit = fadeOut(tween(duration))
            ) {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        Icons.Outlined.Close,
                        contentDescription = "Clear search",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(50),
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = container,
            unfocusedContainerColor = container,
            disabledContainerColor = container,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )
}
