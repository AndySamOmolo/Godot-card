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
import androidx.compose.foundation.selection.selectable
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
import androidx.compose.ui.semantics.Role
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
    "ScrollContainer", "TabContainer", "Theme", "Window", "StyleBoxFlat"
)

private val CARD_2D_CLASSES = setOf(
    "Node", "Node2D", "CanvasItem", "CanvasLayer", "Sprite2D", "AnimatedSprite2D",
    "TileMapLayer", "TileSet", "Camera2D", "Marker2D", "CPUParticles2D", "GPUParticles2D",
    "Texture2D", "Viewport", "SubViewport", "SceneTree", "PackedScene", "VisibleOnScreenNotifier2D",
    "Parallax2D", "PointLight2D", "CanvasModulate", "Line2D", "Polygon2D", "ShaderMaterial"
)

private val PHYSICS_NAV_CLASSES = setOf(
    "Area2D", "CollisionShape2D", "CharacterBody2D", "RigidBody2D", "StaticBody2D",
    "RayCast2D", "ShapeCast2D", "NavigationAgent2D", "NavigationRegion2D", "AStarGrid2D",
    "Path2D", "PathFollow2D"
)

private val SPATIAL_3D_CLASSES = setOf(
    "Node3D", "Camera3D", "MeshInstance3D", "CharacterBody3D", "RigidBody3D",
    "Area3D", "CollisionShape3D", "DirectionalLight3D", "WorldEnvironment",
    "RayCast3D", "SpringArm3D", "NavigationAgent3D", "AudioStreamPlayer3D",
    "Vector3", "Basis", "Transform3D"
)

private val AUDIO_ANIM_CLASSES = setOf(
    "Tween", "Timer", "AnimationPlayer", "AnimationTree", "AudioStreamPlayer",
    "AudioStreamPlayer2D", "AudioServer"
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
                            text = "${state.all.size} · ${COOKBOOK_RECIPES.size}",
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
            .selectable(
                selected = active,
                onClick = onClick,
                role = Role.Tab
            )
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
    val categories = listOf(
        "All",
        "UI & Controls",
        "2D & Visuals",
        "Physics & Nav",
        "3D & Spatial",
        "Audio & Timers",
        "Core & Data"
    )
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    val categoryFiltered = state.filtered.filter { entry ->
        when (selectedCategory) {
            1 -> entry.className in UI_CLASSES
            2 -> entry.className in CARD_2D_CLASSES
            3 -> entry.className in PHYSICS_NAV_CLASSES
            4 -> entry.className in SPATIAL_3D_CLASSES
            5 -> entry.className in AUDIO_ANIM_CLASSES
            6 -> entry.className !in UI_CLASSES &&
                entry.className !in CARD_2D_CLASSES &&
                entry.className !in PHYSICS_NAV_CLASSES &&
                entry.className !in SPATIAL_3D_CLASSES &&
                entry.className !in AUDIO_ANIM_CLASSES
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
                    modifier = Modifier.selectable(
                        selected = active,
                        onClick = { selectedCategory = idx },
                        role = Role.Tab
                    )
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
    "Architecture & AI",
    "3D & Shaders"
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
        title = "Saving & Loading Validated Player Progress (JSON + FileAccess)",
        question = "How do I save high scores, coins, or settings to `user://` and validate types safely on load?",
        relatedClasses = listOf("FileAccess", "JSON", "DirAccess"),
        explanation = "Write to a `.tmp` file in `user://` first and rename it atomically so a power loss never corrupts the save, then validate expected numeric keys when loading untrusted JSON.",
        code = """
        const SAVE_PATH: String = "user://save_profile.json"
        const TMP_PATH: String = "user://save_profile.json.tmp"

        func save_profile(wins: int, coins: int) -> void:
            var data: Dictionary = {"wins": maxi(0, wins), "coins": maxi(0, coins)}
            var file := FileAccess.open(TMP_PATH, FileAccess.WRITE)
            if file != null:
                file.store_string(JSON.stringify(data, "\t"))
                file.close()
                DirAccess.rename_absolute(TMP_PATH, SAVE_PATH)

        func load_profile() -> Dictionary:
            var defaults: Dictionary = {"wins": 0, "coins": 100}
            if not FileAccess.file_exists(SAVE_PATH):
                return defaults
            var parsed: Variant = JSON.parse_string(FileAccess.get_file_as_string(SAVE_PATH))
            if parsed is Dictionary:
                for key in defaults.keys():
                    var v: Variant = parsed.get(key)
                    if v is int or v is float:
                        defaults[key] = maxi(0, int(v))
            return defaults
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
        title = "Server-Authoritative LAN / Online Multiplayer with `ENetMultiplayerPeer` & `@rpc`",
        question = "How do I host a 4-player LAN game and prevent clients from forging illegal card plays?",
        relatedClasses = listOf("MultiplayerAPI", "ENetMultiplayerPeer", "MultiplayerSpawner"),
        explanation = "Clients request a move via `request_play_card.rpc_id(1, suit, rank)`. The host (`multiplayer.is_server()`) verifies `multiplayer.get_remote_sender_id()` and `RuleValidator` before broadcasting `commit_card_play.rpc(...)` (`\"authority\"`).",
        code = """
        var peer_to_seat: Dictionary = {1: 0}

        @rpc("any_peer", "call_local", "reliable")
        func request_play_card(suit: int, rank: int) -> void:
            if not multiplayer.is_server():
                return
            var sender_peer: int = multiplayer.get_remote_sender_id()
            var seat: int = int(peer_to_seat.get(sender_peer, -1))
            if seat != TurnManager.current_player_index:
                return
            var candidate := CardData.make(suit as CardTypes.Suits, rank)
            if RuleValidator.is_single_card_playable(candidate, GameState.top_discard(), GameState.penalty_stack, GameState.declared_suit):
                commit_card_play.rpc(seat, suit, rank)

        @rpc("authority", "call_local", "reliable")
        func commit_card_play(seat: int, suit: int, rank: int) -> void:
            var played := CardData.make(suit as CardTypes.Suits, rank)
            GameState.discard_pile.append(played)
            GameState.state_updated.emit()
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_hand_sort_custom",
        category = "Architecture & AI",
        title = "Custom Comparator Sorting (`Array.sort_custom`) for Hands & Leaderboards",
        question = "How do I sort a player's hand by Suit first and then by Rank ascending using a lambda comparator?",
        relatedClasses = listOf("Array", "Callable"),
        explanation = "Pass a two-argument lambda `func(a, b) -> bool:` to `Array.sort_custom()`. Return `true` whenever `a` should appear strictly before `b`.",
        code = """
        func sort_hand_by_suit_then_rank(hand: Array[CardData]) -> void:
            hand.sort_custom(func(a: CardData, b: CardData) -> bool:
                if a.suit != b.suit:
                    return int(a.suit) < int(b.suit)
                return a.rank < b.rank
            )
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_functional_filter_map",
        category = "Architecture & AI",
        title = "Declarative Pipelines with `Array.filter()`, `map()`, `reduce()`, and `any()`",
        question = "How do I query legal cards or sum tournament points in one readable expression without nested loops?",
        relatedClasses = listOf("Array", "Callable"),
        explanation = "Godot 4.3 arrays support functional higher-order methods (`filter`, `map`, `reduce`, `any`, `all`) that take lambdas or `Callable` references.",
        code = """
        func get_playable_cards(hand: Array[CardData], top: CardData) -> Array[CardData]:
            return hand.filter(func(c: CardData) -> bool:
                return RuleValidator.is_single_card_playable(c, top, GameState.penalty_stack, GameState.declared_suit)
            )

        func sum_hand_points(hand: Array[CardData]) -> int:
            return hand.reduce(func(acc: int, c: CardData) -> int:
                return acc + c.tournament_points(), 0
            )
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_tilemap_custom_data",
        category = "Movement & Physics",
        title = "Reading TileMapLayer Custom Data (Ice, Lava, Footstep SFX, Movement Cost)",
        question = "How do I check what type of ground tile the player is standing on in Godot 4.3 `TileMapLayer`?",
        relatedClasses = listOf("TileMapLayer", "TileSet", "Vector2i"),
        explanation = "Convert the player's `global_position` into tile coordinates via `local_to_map(to_local(pos))`, fetch `get_cell_tile_data(cell)`, and read your custom data layer with `get_custom_data(\"surface_type\")`.",
        code = """
        @onready var ground_layer: TileMapLayer = ${'$'}GroundTileMap

        func get_surface_friction_at(world_pos: Vector2) -> float:
            var cell: Vector2i = ground_layer.local_to_map(ground_layer.to_local(world_pos))
            var tile_data: TileData = ground_layer.get_cell_tile_data(cell)
            if tile_data == null:
                return 1.0
            return float(tile_data.get_custom_data("friction_mult"))
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_animtree_blendspace",
        category = "Timers, Tweens & Camera",
        title = "Driving an `AnimationTree` `BlendSpace2D` for 8-Way Character Sprites",
        question = "How do I automatically blend between Idle and Walk animations in 4 or 8 directions?",
        relatedClasses = listOf("AnimationTree", "AnimationPlayer", "Vector2"),
        explanation = "Update your `BlendSpace2D` blend position only when `input_dir != Vector2.ZERO` so the character remembers the last facing direction when returning to `Idle`.",
        code = """
        @onready var anim_tree: AnimationTree = ${'$'}AnimationTree
        @onready var playback: AnimationNodeStateMachinePlayback = anim_tree.get("parameters/playback")

        func update_animation(input_dir: Vector2) -> void:
            if input_dir != Vector2.ZERO:
                anim_tree.set("parameters/Idle/blend_position", input_dir)
                anim_tree.set("parameters/Walk/blend_position", input_dir)
                playback.travel("Walk")
            else:
                playback.travel("Idle")
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_nav_agent_chase",
        category = "Movement & Physics",
        title = "Smart Pathfinding Around Walls with `NavigationAgent2D`",
        question = "How do I make an enemy chase the player around obstacles without getting stuck on walls?",
        relatedClasses = listOf("NavigationAgent2D", "NavigationRegion2D", "CharacterBody2D"),
        explanation = "Bake a `NavigationPolygon` inside `NavigationRegion2D`, set `nav_agent.target_position = player.global_position` on a 0.2s timer, and steer toward `nav_agent.get_next_path_position()`.",
        code = """
        extends CharacterBody2D

        @export var speed: float = 135.0
        @onready var nav_agent: NavigationAgent2D = ${'$'}NavigationAgent2D

        func set_chase_target(target_pos: Vector2) -> void:
            nav_agent.target_position = target_pos

        func _physics_process(_delta: float) -> void:
            if nav_agent.is_navigation_finished():
                velocity = Vector2.ZERO
                return
            var next_pos: Vector2 = nav_agent.get_next_path_position()
            velocity = global_position.direction_to(next_pos) * speed
            move_and_slide()
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_astargrid_tactics",
        category = "Architecture & AI",
        title = "Turn-Based Grid Pathfinding with `AStarGrid2D` (Tactics / Roguelike)",
        question = "How do I highlight shortest grid paths and movement range on a 2D tile board?",
        relatedClasses = listOf("AStarGrid2D", "Vector2i", "Rect2i"),
        explanation = "`AStarGrid2D` computes A* paths over a 2D grid without needing physics nodes. Mark walls with `set_point_solid(cell, true)` and query `get_id_path(start, goal)`.",
        code = """
        var astar := AStarGrid2D.new()

        func setup_grid(width: int, height: int) -> void:
            astar.region = Rect2i(0, 0, width, height)
            astar.cell_size = Vector2(64, 64)
            astar.diagonal_mode = AStarGrid2D.DIAGONAL_MODE_NEVER
            astar.update()

        func find_grid_path(from_cell: Vector2i, to_cell: Vector2i) -> Array[Vector2i]:
            return astar.get_id_path(from_cell, to_cell)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_raycast_los",
        category = "Movement & Physics",
        title = "Line-of-Sight Vision Check with `RayCast2D`",
        question = "How do I check if an enemy can see the player without a wall blocking their view?",
        relatedClasses = listOf("RayCast2D", "Node2D"),
        explanation = "Point `raycast.target_position = to_local(player.global_position)`, call `force_raycast_update()` for an immediate same-frame check, and verify `get_collider() == player`.",
        code = """
        @onready var sight_ray: RayCast2D = ${'$'}SightRayCast2D

        func has_line_of_sight(player: Node2D) -> bool:
            sight_ray.target_position = to_local(player.global_position)
            sight_ray.force_raycast_update()
            return sight_ray.is_colliding() and sight_ray.get_collider() == player
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_object_pool",
        category = "Spawning & Scenes",
        title = "Zero-Allocation Node Pool for Cards, Bullets, or Damage Numbers",
        question = "How do I reuse nodes instead of calling `instantiate()` and `queue_free()` hundreds of times per minute?",
        relatedClasses = listOf("PackedScene", "Node"),
        explanation = "Keep inactive nodes hidden (`visible = false`, `process_mode = PROCESS_MODE_DISABLED`) in a pool array and reactivate them on demand to eliminate frame-time spikes.",
        code = """
        var _pool: Array[Control] = []
        const CardSceneRes: PackedScene = preload("res://scenes/card/card_scene.tscn")

        func acquire_card(parent: Node) -> Control:
            var card: Control = _pool.pop_back() if not _pool.is_empty() else CardSceneRes.instantiate()
            if card.get_parent() == null:
                parent.add_child(card)
            card.visible = true
            return card

        func release_card(card: Control) -> void:
            card.visible = false
            _pool.append(card)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_command_undo_redo",
        category = "Architecture & AI",
        title = "Command Pattern Undo / Redo History Stack for Puzzle & Card Games",
        question = "How do I let players undo and redo moves cleanly in a strategy, puzzle, or card game?",
        relatedClasses = listOf("RefCounted", "Array"),
        explanation = "Encapsulate each move as a `MoveCommand` (`RefCounted`) with `execute()` and `undo()` methods, and push executed commands onto an `undo_stack`.",
        code = """
        class MoveCommand extends RefCounted:
            var card: CardData
            var from_hand: Array
            var to_pile: Array

            func execute() -> void:
                from_hand.erase(card)
                to_pile.append(card)

            func undo() -> void:
                to_pile.erase(card)
                from_hand.append(card)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_audio_bus_settings",
        category = "Save, Data & Audio",
        title = "Master / Music / SFX Volume Sliders with `AudioServer` & `ConfigFile`",
        question = "How do I wire a 0.0–1.0 UI `Slider` to an `AudioServer` bus in decibels and save it?",
        relatedClasses = listOf("AudioServer", "ConfigFile", "Slider"),
        explanation = "Convert linear slider values (`0.0..1.0`) to decibels using `linear_to_db()` and mute the bus when `linear <= 0.001`.",
        code = """
        func set_bus_volume(bus_name: String, linear_value: float) -> void:
            var idx: int = AudioServer.get_bus_index(bus_name)
            if idx == -1:
                return
            AudioServer.set_bus_mute(idx, linear_value <= 0.001)
            AudioServer.set_bus_volume_db(idx, linear_to_db(clampf(linear_value, 0.001, 1.0)))
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_input_remap",
        category = "UI, Input & Signals",
        title = "Runtime Key / Gamepad Remapping with `InputMap`",
        question = "How do I let players rebind keys or controller buttons in an Options menu?",
        relatedClasses = listOf("InputMap", "InputEvent"),
        explanation = "Listen for the next `InputEventKey` or `InputEventJoypadButton` in `_unhandled_input()`, clear old events with `InputMap.action_erase_events(action)`, and register the new event with `InputMap.action_add_event(action, event)`.",
        code = """
        var awaiting_rebind_action: String = ""

        func _unhandled_input(event: InputEvent) -> void:
            if awaiting_rebind_action.is_empty():
                return
            if (event is InputEventKey or event is InputEventJoypadButton) and event.is_pressed():
                InputMap.action_erase_events(awaiting_rebind_action)
                InputMap.action_add_event(awaiting_rebind_action, event)
                awaiting_rebind_action = ""
                get_viewport().set_input_as_handled()
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_async_scene_loader",
        category = "Spawning & Scenes",
        title = "Background Loading Screen with Progress Bar (`ResourceLoader` Threaded)",
        question = "How do I load a large 2D/3D level in the background while animating a loading bar?",
        relatedClasses = listOf("ResourceLoader", "ProgressBar", "SceneTree"),
        explanation = "Call `ResourceLoader.load_threaded_request(path)` once, poll `load_threaded_get_status(path, progress)` in `_process()`, and instantiate the `PackedScene` when `THREAD_LOAD_LOADED` returns.",
        code = """
        var target_scene_path: String = "res://scenes/table/table.tscn"

        func start_background_load() -> void:
            ResourceLoader.load_threaded_request(target_scene_path)

        func _process(_delta: float) -> void:
            var progress: Array = []
            var status := ResourceLoader.load_threaded_get_status(target_scene_path, progress)
            if not progress.is_empty():
                loading_bar.value = float(progress[0]) * 100.0
            if status == ResourceLoader.THREAD_LOAD_LOADED:
                var packed: PackedScene = ResourceLoader.load_threaded_get(target_scene_path)
                get_tree().change_scene_to_packed(packed)
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_tool_script_preview",
        category = "UI, Input & Signals",
        title = "Live In-Editor Card & Level Preview with `@tool` Scripts",
        question = "How do I make my custom `CardScene` update its rank/suit preview live inside the Godot Editor?",
        relatedClasses = listOf("Engine", "Resource"),
        explanation = "Place `@tool` at the very top of your script and guard gameplay-only code with `if not Engine.is_editor_hint():`.",
        code = """
        @tool
        extends Control

        @export var preview_rank: int = 14:
            set(value):
                preview_rank = value
                if is_node_ready():
                    _update_preview()

        func _process(delta: float) -> void:
            if Engine.is_editor_hint():
                return # Skip gameplay logic while inside the Editor!
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_3d_fps_controller",
        category = "3D & Shaders",
        title = "3D Character Controller with `CharacterBody3D` & `Basis` Camera-Relative Movement",
        question = "How do I move a 3D character relative to where the `Camera3D` is looking?",
        relatedClasses = listOf("CharacterBody3D", "Node3D", "Camera3D", "Vector3", "Basis"),
        explanation = "Transform the 2D `Input.get_vector()` into 3D world space using `transform.basis * Vector3(input.x, 0, input.y)` so `-Z` is forward (`Basis.z` points backward in Godot's right-handed 3D space).",
        code = """
        extends CharacterBody3D

        const SPEED: float = 6.0
        const GRAVITY: float = 18.0

        func _physics_process(delta: float) -> void:
            if not is_on_floor():
                velocity.y -= GRAVITY * delta
            var input_2d: Vector2 = Input.get_vector("move_left", "move_right", "move_forward", "move_back")
            var move_dir: Vector3 = (transform.basis * Vector3(input_2d.x, 0.0, input_2d.y)).normalized()
            velocity.x = move_dir.x * SPEED
            velocity.z = move_dir.z * SPEED
            move_and_slide()
        """.trimIndent()
    ),
    CookbookRecipe(
        id = "cb_3d_mouse_ray_pick",
        category = "3D & Shaders",
        title = "Clicking 3D Cards or Board Pieces with `Camera3D.project_ray_origin()`",
        question = "How do I detect which 3D card mesh or tabletop piece the player clicked in a 3D scene?",
        relatedClasses = listOf("Camera3D", "RayCast3D", "Area3D", "Vector3"),
        explanation = "Project a 3D ray from the `Camera3D` through the 2D screen mouse coordinates using `project_ray_origin()` and `project_ray_normal()`, then query `PhysicsRayQueryParameters3D`.",
        code = """
        func pick_3d_card_under_mouse(camera: Camera3D, mouse_pos: Vector2) -> Object:
            var from: Vector3 = camera.project_ray_origin(mouse_pos)
            var to: Vector3 = from + camera.project_ray_normal(mouse_pos) * 50.0
            var query := PhysicsRayQueryParameters3D.create(from, to)
            var hit: Dictionary = camera.get_world_3d().direct_space_state.intersect_ray(query)
            return hit.get("collider", null)
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
            placeholder = "Search 30 recipes (e.g. jump, drag, save, nav, 3d, rpc, pool)...",
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
                    modifier = Modifier.selectable(
                        selected = active,
                        onClick = { selectedCat = idx },
                        role = Role.Tab
                    )
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
                message = "Try searching for movement, save, tween, signal, nav, 3d, or spawn."
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
                                recipe.relatedClasses.take(3).forEach { cls ->
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
