package com.kadicompanion.ui.tutorial.blueprint

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Campaign
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.LockOpen
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kadicompanion.domain.model.CodeSnippet
import com.kadicompanion.ui.common.CLUB
import com.kadicompanion.ui.common.Callout
import com.kadicompanion.ui.common.CalloutType
import com.kadicompanion.ui.common.CodeBlock
import com.kadicompanion.ui.common.DIAMOND
import com.kadicompanion.ui.common.HEART
import com.kadicompanion.ui.common.HeroPanel
import com.kadicompanion.ui.common.IconBubble
import com.kadicompanion.ui.common.KadiCard
import com.kadicompanion.ui.common.KadiTopBar
import com.kadicompanion.ui.common.Pill
import com.kadicompanion.ui.common.RichInlineText
import com.kadicompanion.ui.common.SPADE
import com.kadicompanion.ui.common.SectionHeader
import com.kadicompanion.ui.common.studioCardSpacing
import com.kadicompanion.ui.common.studioScreenPadding
import com.kadicompanion.ui.theme.CodeFontFamily
import com.kadicompanion.ui.theme.LocalUiPreferences
import com.kadicompanion.ui.theme.SyntaxColors
import com.kadicompanion.ui.theme.heroTextPrimary
import com.kadicompanion.ui.theme.heroTextSecondary
import com.kadicompanion.ui.theme.heroTextTertiary
import com.kadicompanion.ui.theme.isDarkScheme
import com.kadicompanion.ui.theme.successColor

/**
 * 7-Tab Solo Game Developer Studio, Playable Kadi Table & Architecture Blueprint:
 * - Tab 0: Play Kadi Live     (Play a real interactive Kadi table right on your phone + see the live GDScript trace!)
 * - Tab 1: New Game Architect (Deconstruct & generate starter architecture for ANY new Godot 4.3 game genre solo!)
 * - Tab 2: Upgrade Kadi Lab   (6 Self-Guided Kadi Expansion Blueprints with no-spoiler hints & reference code)
 * - Tab 3: Downloaded Assets  (Interactive Asset Pack Naming & GDScript Loader Generator + Import Dock Guide)
 * - Tab 4: Project Map        (Complete res:// folder structure across Phases 00–13)
 * - Tab 5: Scene & Signals    (Exact Node Hierarchy with TextureRect / TextureButton for downloaded sprites)
 * - Tab 6: Rule Simulator     (Interactive Kenyan Kadi rule sandbox)
 */
@Composable
fun ProjectBlueprintScreen(
    onBack: () -> Unit,
    onNavigateToPhase: (String) -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf(
        "Play Kadi Live",
        "New Game Architect",
        "Upgrade Kadi Lab",
        "Downloaded Assets",
        "Project Map",
        "Scene & Signals",
        "Rule Simulator"
    )
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = "Live Kadi & Solo Dev Studio",
                subtitle = "PLAYABLE TABLE · NEW GAME ARCHITECT · UPGRADES",
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = hPad, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                tabs.forEachIndexed { idx, title ->
                    val selected = selectedTab == idx
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer,
                        border = BorderStroke(
                            1.dp,
                            if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        modifier = Modifier.clickable { selectedTab = idx }
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = hPad, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(vGap)
            ) {
                when (selectedTab) {
                    0 -> PlayableKadiMiniTableTab(onNavigateToPhase = onNavigateToPhase)
                    1 -> NewGameArchitectTab(onNavigateToPhase = onNavigateToPhase)
                    2 -> UpgradeKadiLabTab(onNavigateToPhase = onNavigateToPhase)
                    3 -> DownloadedAssetsTab()
                    4 -> ProjectMapTab(onNavigateToPhase = onNavigateToPhase)
                    5 -> SceneAndSignalsTab()
                    6 -> RuleSimulatorTab()
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

private data class GenreBlueprint(
    val id: Int,
    val label: String,
    val tagline: String,
    val kadiToGenreBridge: List<Pair<String, String>>,
    val sceneTree: String,
    val coreScriptTitle: String,
    val coreScriptCode: String,
    val soloMissions: List<String>
)

private val GENRE_BLUEPRINTS = listOf(
    GenreBlueprint(
        id = 0,
        label = "2D Action Platformer",
        tagline = "Celeste / Hollow Knight / Mario style side-scroller with responsive jump, coins & enemies",
        kadiToGenreBridge = listOf(
            "CardData (Resource)" to "WeaponStats / LevelData (Resource) storing jump height, dash cooldown, or enemy HP",
            "CardScene.tscn (Template)" to "Coin.tscn / EnemySlime.tscn instantiated across levels with PackedScene.instantiate()",
            "GameState (Autoload)" to "GameManager (Autoload) tracking player lives, collected coins, and active checkpoint",
            "card_clicked (Signal)" to "Coin.body_entered / Hurtbox.area_entered signals notifying HUD when score or HP changes",
            "Card Lift Tween" to "Squash-and-stretch jump Tween (scale = Vector2(0.8, 1.25)) and coin pickup pop"
        ),
        sceneTree = """
        Level01 (Node2D)  [script: res://scenes/level_01.gd]
        ├── WorldTileMap (TileMapLayer)             # Platforms, walls & spikes (Physics Layer 1)
        ├── Player (CharacterBody2D)                # [script: res://scenes/player/player.gd]
        │   ├── Sprite2D                            # Player sprite sheet
        │   ├── CollisionShape2D                    # Capsule / Rectangle physics collider
        │   ├── Camera2D                            # Position Smoothing = On, Drag Horizontal/Vertical
        │   └── Hurtbox (Area2D)                    # Detects enemy & hazard Hitboxes
        ├── Coins (Node2D)                          # Holds instanced Coin.tscn nodes
        ├── Enemies (Node2D)                        # Holds instanced PatrolEnemy.tscn nodes
        └── HUD (CanvasLayer)
            ├── HeartsContainer (HBoxContainer)     # Same HBoxContainer as Kadi's PlayerHandContainer!
            └── CoinCountLabel (Label)              # Connected to GameManager.coins_changed signal
        """.trimIndent(),
        coreScriptTitle = "res://scenes/player/player.gd (Platformer Controller with Coyote Jump & Dash)",
        coreScriptCode = """
        extends CharacterBody2D

        const SPEED: float = 250.0
        const JUMP_VELOCITY: float = -430.0
        const GRAVITY: float = 1180.0
        const DASH_SPEED: float = 560.0

        var coyote_time: float = 0.0
        var dash_time: float = 0.0
        @onready var sprite: Sprite2D = ${'$'}Sprite2D

        func _physics_process(delta: float) -> void:
            if is_on_floor():
                coyote_time = 0.12
            else:
                coyote_time = maxf(0.0, coyote_time - delta)
                if dash_time <= 0.0:
                    velocity.y += GRAVITY * delta

            if dash_time > 0.0:
                dash_time -= delta
                move_and_slide()
                return

            if Input.is_action_just_pressed("jump") and coyote_time > 0.0:
                velocity.y = JUMP_VELOCITY
                coyote_time = 0.0
                # Squash-and-stretch using the exact Tween pattern from Kadi Phase 11!
                sprite.scale = Vector2(0.8, 1.25)
                create_tween().tween_property(sprite, "scale", Vector2.ONE, 0.18)

            var dir: float = Input.get_axis("move_left", "move_right")
            velocity.x = move_toward(velocity.x, dir * SPEED, 1600.0 * delta)
            if dir != 0.0:
                sprite.flip_h = (dir < 0.0)

            if Input.is_action_just_pressed("dash") and dir != 0.0:
                dash_time = 0.14
                velocity = Vector2(dir * DASH_SPEED, 0.0)

            move_and_slide()
        """.trimIndent(),
        soloMissions = listOf(
            "**Mission 1 (Double Jump)**: Add `var jumps_left: int = 2` that resets whenever `is_on_floor()` is `true`, and decrement it on each jump so the player can jump once in mid-air.",
            "**Mission 2 (Collectible Coin Scene)**: Build `coin.tscn` (`Area2D` + `Sprite2D` + `CollisionShape2D`). Connect `body_entered` in `_ready()`, check `if body is CharacterBody2D:`, increment `GameManager.coins += 1`, and call `queue_free()`.",
            "**Mission 3 (Moving Platform)**: Use `create_tween().set_loops()` on an `AnimatableBody2D` to ping-pong `position.x` by `+160px` and back over `2.0` seconds."
        )
    ),
    GenreBlueprint(
        id = 1,
        label = "Top-Down RPG / Survivor",
        tagline = "Zelda / Vampire Survivors / Hades style 8-way exploration, wave spawner, inventory & loot",
        kadiToGenreBridge = listOf(
            "CardData (Resource)" to "ItemData / SpellData (Resource) with @export var damage, cooldown, icon, rarity",
            "PlayerHandContainer (HBoxContainer)" to "HotbarContainer (HBoxContainer) displaying your equipped weapons/potions",
            "AITurnTimer (Timer)" to "WaveSpawnTimer (Timer) spawning enemies around a Marker2D / PathFollow2D ring",
            "RuleValidator (Static Engine)" to "CombatCalculator (RefCounted) computing elemental weaknesses & critical hits",
            "GameState.deck.shuffle()" to "LootTable array shuffling & weighted random chest drops"
        ),
        sceneTree = """
        ArenaWorld (Node2D)  [y_sort_enabled = true, script: res://scenes/arena_world.gd]
        ├── GroundTileMap (TileMapLayer)            # Grass, dungeon floor & obstacle walls
        ├── Player (CharacterBody2D)                # [motion_mode = Floating, script: player_topdown.gd]
        │   ├── Sprite2D
        │   ├── CollisionShape2D
        │   ├── Camera2D
        │   └── AutoWeaponPivot (Node2D)            # Aims at nearest enemy in group "enemies"
        ├── EnemyContainer (Node2D)                 # [y_sort_enabled = true]
        ├── WaveSpawnTimer (Timer)                  # wait_time = 1.5, autostart = true
        └── HUD (CanvasLayer)
            ├── XPBar (ProgressBar)                 # Level-up bar
            ├── Hotbar (HBoxContainer)              # Equipped ItemSlot.tscn instances
            └── UpgradeDraftPopup (Control)         # Same modal pattern as Kadi's DeclareSuitDialog!
        """.trimIndent(),
        coreScriptTitle = "res://scenes/arena_world.gd (8-Way Movement + Wave Spawner + Nearest-Enemy Targeting)",
        coreScriptCode = """
        extends Node2D

        const EnemyScene: PackedScene = preload("res://scenes/enemy_goblin.tscn")
        @onready var player: CharacterBody2D = ${'$'}Player
        @onready var enemy_container: Node2D = ${'$'}EnemyContainer
        @onready var spawn_timer: Timer = ${'$'}WaveSpawnTimer

        func _ready() -> void:
            spawn_timer.timeout.connect(_on_spawn_timer_timeout)

        func _on_spawn_timer_timeout() -> void:
            var angle: float = randf() * TAU
            var spawn_pos: Vector2 = player.global_position + Vector2.from_angle(angle) * 420.0
            var enemy: CharacterBody2D = EnemyScene.instantiate()
            enemy_container.add_child(enemy)
            enemy.global_position = spawn_pos
            enemy.add_to_group("enemies")

        func find_nearest_enemy(from_pos: Vector2) -> Node2D:
            var nearest: Node2D = null
            var best_dist_sq: float = INF
            for node in get_tree().get_nodes_in_group("enemies"):
                var d_sq: float = from_pos.distance_squared_to(node.global_position)
                if d_sq < best_dist_sq:
                    best_dist_sq = d_sq
                    nearest = node
            return nearest
        """.trimIndent(),
        soloMissions = listOf(
            "**Mission 1 (Chasing AI Enemy)**: In `enemy_goblin.gd`, set `var dir = global_position.direction_to(player.global_position)` inside `_physics_process(delta)`, set `velocity = dir * 95.0`, and call `move_and_slide()`.",
            "**Mission 2 (XP Gem & Level-Up Popup)**: When an enemy's HP reaches `0`, instantiate an `xp_gem.tscn` (`Area2D`). When `xp >= xp_to_next_level`, pause the tree (`get_tree().paused = true`) and show a 3-card upgrade picker modal just like `DeclareSuitDialog`!",
            "**Mission 3 (Custom Resource Inventory)**: Create `ItemData` (`extends Resource`) with `@export var name: String` and `@export var icon: Texture2D`, and display `Array[ItemData]` inside `Hotbar (HBoxContainer)`."
        )
    ),
    GenreBlueprint(
        id = 2,
        label = "Roguelike Deckbuilder",
        tagline = "Slay the Spire / Balatro / Monster Train style tactical card battler with energy, relics & synergies",
        kadiToGenreBridge = listOf(
            "CardData (suit, rank)" to "BattleCardData (mana_cost, damage, block, status_effect, description)",
            "GameState.deck & discard_pile" to "draw_pile, hand, discard_pile, and exhaust_pile (exact samereshuffle_if_empty()!)",
            "RuleValidator.validate_move()" to "can_play_card(card): checks current_energy >= card.mana_cost",
            "TurnManager.advance_turn()" to "Phase State Machine: PLAYER_START -> PLAYER_ACTION -> ENEMY_INTENT -> ENEMY_ATTACK",
            "Penalty Stack (+2/+3/+5)" to "Status Effect stacks (Poison, Vulnerable, Strength, Burn) stored in a Dictionary"
        ),
        sceneTree = """
        BattleArena (Control)  [script: res://scenes/battle_arena.gd]
        ├── CombatantsRow (HBoxContainer)
        │   ├── HeroActor (Control)                 # HP Bar, Block Badge, Status Icons
        │   └── BossActor (Control)                 # HP Bar, IntentLabel ("⚡ Attacking for 14")
        ├── HandContainer (HBoxContainer)           # Exact same CardScene instancing as Kadi!
        ├── HUD (CanvasLayer)
        │   ├── EnergyOrbLabel (Label)              # "Energy: 3 / 3"
        │   ├── DrawPileCount (Label)               # Uses Kadi's exact deck.size()
        │   ├── DiscardPileCount (Label)            # Uses Kadi's exact discard_pile.size()
        │   └── EndTurnButton (Button)              # Discards remaining hand & triggers Boss turn
        """.trimIndent(),
        coreScriptTitle = "res://scripts/deckbuilder_combat.gd (Energy Pool, Card Execution & Enemy Intents)",
        coreScriptCode = """
        extends Node

        var max_energy: int = 3
        var current_energy: int = 3
        var player_hp: int = 60
        var player_block: int = 0
        var boss_hp: int = 120
        var boss_statuses: Dictionary = {"poison": 0, "vulnerable": 0}

        func can_play_card(card: BattleCardData) -> bool:
            return current_energy >= card.mana_cost

        func play_card(card: BattleCardData) -> void:
            if not can_play_card(card):
                return
            current_energy -= card.mana_cost
            var mult: float = 1.5 if boss_statuses["vulnerable"] > 0 else 1.0
            boss_hp -= int(round(card.damage * mult))
            player_block += card.block
            if card.poison_stacks > 0:
                boss_statuses["poison"] += card.poison_stacks

        func end_player_turn(boss_attack_damage: int) -> void:
            # 1. Tick boss poison at start of boss turn
            if boss_statuses["poison"] > 0:
                boss_hp -= boss_statuses["poison"]
                boss_statuses["poison"] -= 1
            # 2. Boss attacks player through block
            var unblocked: int = maxi(0, boss_attack_damage - player_block)
            player_hp -= unblocked
            # 3. Reset block & energy for next player turn
            player_block = 0
            current_energy = max_energy
        """.trimIndent(),
        soloMissions = listOf(
            "**Mission 1 (Synergy Multipliers — Balatro Style)**: Add `var base_chips: int` and `var mult: int` to your turn evaluator so playing matching suits or sequences multiplies the score before hitting the Boss HP!",
            "**Mission 2 (Relic Passives)**: Create `RelicData` (`extends Resource`) and store `var active_relics: Array[RelicData]` in `GameState` (e.g., *\"Whenever you play 3 cards in a single turn, gain +4 Block\"*).",
            "**Mission 3 (Post-Battle 3-Card Reward Draft)**: Reuse Kadi's `HBoxContainer` card spawning to offer 3 random new `BattleCardData` cards to add to the player's permanent deck after winning a fight."
        )
    ),
    GenreBlueprint(
        id = 3,
        label = "Arcade Space / Twin-Stick Shooter",
        tagline = "Geometry Wars / Asteroids / Space Invaders style fast-paced projectile action with waves & bosses",
        kadiToGenreBridge = listOf(
            "CardScene.instantiate()" to "LaserBullet.instantiate() & ExplosionParticles.instantiate()",
            "DiscardSlot / PlayerHandContainer" to "ProjectileContainer & AsteroidContainer nodes keeping SceneTree organized",
            "GameState.penalty_stack" to "Combo Multiplier meter & Overheat weapon gauge",
            "AITurnTimer (Timer)" to "FireRateTimer (0.12s cooldown) & WaveDirectorTimer",
            "54-Card Conservation assert()" to "Screen-bounds cleanup (queue_free() bullets via VisibleOnScreenNotifier2D)"
        ),
        sceneTree = """
        SpaceShooter (Node2D)  [script: res://scenes/space_shooter.gd]
        ├── StarfieldBg (CPUParticles2D)            # Scrolling star particles
        ├── Ship (CharacterBody2D)                  # [script: res://scenes/ship.gd]
        │   ├── Sprite2D
        │   ├── CollisionPolygon2D
        │   └── Muzzle (Marker2D)                   # Bullet spawn point at nose of ship
        ├── Projectiles (Node2D)                    # Holds active LaserBullet.tscn instances
        ├── Enemies (Node2D)                        # Holds active Asteroid/Alien instances
        └── HUD (CanvasLayer)
            ├── ScoreLabel (Label)
            └── ShieldBar (ProgressBar)
        """.trimIndent(),
        coreScriptTitle = "res://scenes/ship.gd (Mouse Aiming, Rapid-Fire Cooldown & Bullet Spawning)",
        coreScriptCode = """
        extends CharacterBody2D

        const BulletScene: PackedScene = preload("res://scenes/laser_bullet.tscn")
        @export var speed: float = 320.0
        @export var fire_interval: float = 0.14
        var fire_cooldown: float = 0.0
        @onready var muzzle: Marker2D = ${'$'}Muzzle

        func _physics_process(delta: float) -> void:
            # Rotate ship smoothly toward mouse cursor or right thumbstick:
            look_at(get_global_mouse_position())
            var move_dir: Vector2 = Input.get_vector("move_left", "move_right", "move_up", "move_down")
            velocity = move_dir * speed
            move_and_slide()

            fire_cooldown = maxf(0.0, fire_cooldown - delta)
            if Input.is_action_pressed("fire") and fire_cooldown <= 0.0:
                fire_cooldown = fire_interval
                _spawn_bullet()

        func _spawn_bullet() -> void:
            var b: Area2D = BulletScene.instantiate()
            get_tree().current_scene.get_node("Projectiles").add_child(b)
            b.global_position = muzzle.global_position
            b.rotation = global_rotation
        """.trimIndent(),
        soloMissions = listOf(
            "**Mission 1 (Bullet Movement & Auto-Cleanup)**: In `laser_bullet.gd` (`Area2D`), move forward each frame with `position += Vector2.RIGHT.rotated(rotation) * 750.0 * delta`, and connect a `VisibleOnScreenNotifier2D.screen_exited` signal to `queue_free()`.",
            "**Mission 2 (Splitting Asteroids)**: When a large asteroid (`scale = Vector2.ONE`) is hit by a bullet, spawn 2 medium asteroids at its `global_position` with `scale = Vector2(0.55, 0.55)` and random velocities before calling `queue_free()`.",
            "**Mission 3 (Spread-Shot Powerup)**: When `spread_shot_active == true`, loop `for angle_deg in [-15.0, 0.0, 15.0]:` inside `_spawn_bullet()` to fire a 3-beam fan!"
        )
    ),
    GenreBlueprint(
        id = 4,
        label = "Grid Puzzle / Tactical Board Game",
        tagline = "Match-3 / 2048 / Into the Breach / Chess style 2D grid logic with Tween cascades",
        kadiToGenreBridge = listOf(
            "2-Nested Loops in build_deck()" to "for x in width: for y in height: building a 2D board grid",
            "CardData (suit, rank)" to "TileData (tile_type, grid_coords: Vector2i, is_locked)",
            "RuleValidator.validate_question_chain()" to "find_connected_matches(grid) scanning rows/columns for 3+ matching tiles",
            "CardScene.set_selected(-14px)" to "Highlighting selected board tile & valid movement/swap squares",
            "Tween animations (Phase 11)" to "Sliding tiles between grid coordinates (grid_to_pixel(coords))"
        ),
        sceneTree = """
        PuzzleBoard (Node2D)  [script: res://scenes/puzzle_board.gd]
        ├── BoardFrame (Panel)
        ├── GridContainerOrRoot (Node2D)            # Holds TileScene.tscn nodes positioned by grid_to_pixel()
        └── HUD (CanvasLayer)
            ├── MovesLeftLabel (Label)
            ├── ScoreLabel (Label)
            └── RestartButton (Button)
        """.trimIndent(),
        coreScriptTitle = "res://scenes/puzzle_board.gd (2D Grid Coordinate Math, Tile Swapping & Match Scan)",
        coreScriptCode = """
        extends Node2D

        const COLS: int = 6
        const ROWS: int = 6
        const CELL_SIZE: int = 72
        var board: Array = [] # 2D Array: board[x][y] stores tile_id (0..4)

        func grid_to_pixel(cell: Vector2i) -> Vector2:
            return Vector2(cell.x * CELL_SIZE + 36, cell.y * CELL_SIZE + 36)

        func setup_board() -> void:
            board.clear()
            for x in range(COLS):
                var col: Array[int] = []
                for y in range(ROWS):
                    col.append(randi() % 5)
                board.append(col)

        func has_horizontal_match_at(x: int, y: int) -> bool:
            if x < 2:
                return false
            var t: int = board[x][y]
            return t != -1 and board[x - 1][y] == t and board[x - 2][y] == t
        """.trimIndent(),
        soloMissions = listOf(
            "**Mission 1 (Prevent Starting Matches)**: In `setup_board()`, use a `while has_horizontal_match_at(x, y):` loop when picking a tile color—just like Kadi's `while not is_valid_starter(card):` loop in Phase 2!",
            "**Mission 2 (Animated Tile Swap)**: Write `swap_tiles(a: Vector2i, b: Vector2i)` that swaps `board[a.x][a.y]` and `board[b.x][b.y]` and uses `create_tween().set_parallel(true)` to slide both tile nodes to their new `grid_to_pixel()` positions.",
            "**Mission 3 (Gravity Cascade)**: Scan each column from bottom (`ROWS - 1`) to top (`0`), shifting non-empty tiles down into empty (`-1`) slots and spawning fresh tiles at the top."
        )
    )
)

@Composable
private fun NewGameArchitectTab(onNavigateToPhase: (String) -> Unit) {
    var selectedGenreId by rememberSaveable { mutableIntStateOf(0) }
    val genre = GENRE_BLUEPRINTS.firstOrNull { it.id == selectedGenreId } ?: GENRE_BLUEPRINTS.first()
    val isDark = isDarkScheme()
    val uiPrefs = LocalUiPreferences.current
    val codeBg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground

    HeroPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.AutoAwesome, contentDescription = null, tint = Color(0xFF38BDF8), modifier = Modifier.size(18.dp))
                Text(
                    text = "SOLO GAME ARCHITECT · ESCAPE TUTORIAL HELL",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF38BDF8)
                )
            }
            Pill(
                text = "Phase 13 Guide",
                color = Color(0xFF34D399),
                onClick = { onNavigateToPhase("phase_13_new_games_solo") }
            )
        }
        Text(
            text = "Build Any New Game in Godot 4.3 On Your Own",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = heroTextPrimary()
        )
        Text(
            text = "Every game in Godot 4.3 is built from the exact same 5 building blocks you mastered in Kadi: **Data Resources**, **Template Scenes (`PackedScene`)**, **Autoload State**, **Signals**, and **Rule/Physics Validators**. Pick any genre below to see how your Kadi skills translate directly into a brand-new game!",
            style = MaterialTheme.typography.bodySmall,
            color = heroTextSecondary()
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.primary) {
        Text(
            text = "SELECT THE GAME GENRE YOU WANT TO BUILD NEXT:",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        ChipSelectorRow(
            items = GENRE_BLUEPRINTS.map { it.id to it.label },
            selected = selectedGenreId,
            onSelect = { selectedGenreId = it }
        )
        Text(
            text = genre.label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = genre.tagline,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.secondary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.Explore, tint = MaterialTheme.colorScheme.secondary, size = 34.dp)
            Column {
                Text(
                    text = "1. CONCEPT BRIDGE · KADI -> ${genre.label.uppercase()}",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "You Already Know the Architecture!",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            genre.kadiToGenreBridge.forEach { (inKadi, inNewGame) ->
                Surface(
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.surfaceContainerLow,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(10.dp),
                        verticalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Text(
                            text = "In Kadi: $inKadi",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "➔ In ${genre.label}: $inNewGame",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }

    KadiCard(accent = MaterialTheme.colorScheme.primary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.AccountTree, tint = MaterialTheme.colorScheme.primary, size = 34.dp)
            Column {
                Text(
                    text = "2. GODOT 4.3 SCENE TREE FOR ${genre.label.uppercase()}",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "How to Set Up Your Nodes Without a Tutorial",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            color = codeBg,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                Text(
                    text = genre.sceneTree,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = CodeFontFamily,
                        fontSize = (12f * uiPrefs.codeFontScale).sp,
                        lineHeight = (19f * uiPrefs.codeFontScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    KadiCard(accent = MaterialTheme.colorScheme.secondary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.Bolt, tint = MaterialTheme.colorScheme.secondary, size = 34.dp)
            Column {
                Text(
                    text = "3. CORE STARTER SCRIPT (COPY-READY GDSCRIPT)",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = genre.coreScriptTitle,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        CodeBlock(
            snippet = CodeSnippet(
                id = "genre_starter_${genre.id}",
                language = "gdscript",
                code = genre.coreScriptCode,
                caption = "Paste this into a new Godot 4.3 project to have a playable ${genre.label} foundation in 2 minutes!"
            )
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.tertiary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.AutoAwesome, tint = MaterialTheme.colorScheme.tertiary, size = 34.dp)
            Column {
                Text(
                    text = "4. SOLO EXPANSION MISSIONS (NO TUTORIAL NEEDED!)",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "3 Features to Build Completely On Your Own",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            genre.soloMissions.forEach { mission ->
                RichInlineText(
                    text = "• $mission",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

private data class KadiUpgradeBlueprint(
    val id: Int,
    val title: String,
    val badge: String,
    val goal: String,
    val classesToLookUp: List<String>,
    val architectureSteps: List<String>,
    val referenceCode: String
)

private val KADI_UPGRADES = listOf(
    KadiUpgradeBlueprint(
        id = 0,
        title = "Upgrade 1: Drag-and-Drop Cards onto the Discard Pile",
        badge = "INPUT & UI PHYSICS",
        goal = "Let players physically drag a card from `PlayerHandContainer` across the emerald felt table and drop it onto `DiscardSlot` to play it!",
        classesToLookUp = listOf("Control", "InputEvent", "Rect2", "Tween"),
        architectureSteps = listOf(
            "In `card_scene.gd`, override `func _gui_input(event: InputEvent) -> void:` (and set `ClickButton.mouse_filter = MOUSE_FILTER_PASS` so `Card` receives mouse drag events).",
            "When `event is InputEventMouseButton` (`MOUSE_BUTTON_LEFT`), record `drag_start_pos = position` and `drag_offset = get_global_mouse_position() - global_position` on press.",
            "While dragging (`event is InputEventMouseMotion`), update `global_position = get_global_mouse_position() - drag_offset` and set `z_index = 20` so the dragged card floats above all other cards.",
            "On release, check if `discard_slot.get_global_rect().has_point(get_global_mouse_position())` is `true`: if so, emit `card_dropped_on_discard(self)`; otherwise Tween `position` smoothly back to `drag_start_pos`!"
        ),
        referenceCode = """
        # Add inside res://scenes/card/card_scene.gd:
        signal card_dropped_on_discard(card_node: Control)

        var is_dragging: bool = false
        var drag_offset: Vector2 = Vector2.ZERO
        var home_pos: Vector2 = Vector2.ZERO

        func _gui_input(event: InputEvent) -> void:
            if event is InputEventMouseButton and event.button_index == MOUSE_BUTTON_LEFT:
                if event.pressed:
                    is_dragging = true
                    home_pos = position
                    drag_offset = get_global_mouse_position() - global_position
                    z_index = 20
                else:
                    is_dragging = false
                    z_index = 0
                    var table := get_tree().current_scene
                    var slot: Control = table.get_node_or_null("DiscardSlot")
                    if slot and slot.get_global_rect().has_point(get_global_mouse_position()):
                        card_dropped_on_discard.emit(self)
                    else:
                        create_tween().tween_property(self, "position", home_pos, 0.14)
            elif event is InputEventMouseMotion and is_dragging:
                global_position = get_global_mouse_position() - drag_offset
        """.trimIndent()
    ),
    KadiUpgradeBlueprint(
        id = 1,
        title = "Upgrade 2: Persistent Save File (Win Streak, Coins & Stats)",
        badge = "FILEACCESS & JSON",
        goal = "Save the player's total matches played, wins, current win streak, and Kadi Coins to `user://kadi_save.json` so progress is kept forever!",
        classesToLookUp = listOf("FileAccess", "JSON", "Dictionary"),
        architectureSteps = listOf(
            "Create `res://scripts/save_manager.gd` and register it as an Autoload singleton (`SaveManager`) in **Project Settings -> Globals -> Autoload**.",
            "Define `const SAVE_PATH = \"user://kadi_save.json\"` and a `var profile: Dictionary = {\"wins\": 0, \"matches\": 0, \"streak\": 0, \"coins\": 100}`.",
            "In `save_to_disk()`, open `FileAccess.open(SAVE_PATH, FileAccess.WRITE)` and call `file.store_string(JSON.stringify(profile, \"\\t\"))`.",
            "In `load_from_disk()`, check `FileAccess.file_exists(SAVE_PATH)`, read with `FileAccess.get_file_as_string(SAVE_PATH)`, and parse with `JSON.parse_string()`."
        ),
        referenceCode = """
        # File: res://scripts/save_manager.gd (Autoload: SaveManager)
        extends Node

        const SAVE_PATH: String = "user://kadi_save.json"
        var profile: Dictionary = {
            "wins": 0,
            "matches": 0,
            "streak": 0,
            "coins": 100
        }

        func _ready() -> void:
            load_profile()

        func record_match_result(human_won: bool) -> void:
            profile["matches"] = int(profile.get("matches", 0)) + 1
            if human_won:
                profile["wins"] = int(profile.get("wins", 0)) + 1
                profile["streak"] = int(profile.get("streak", 0)) + 1
                profile["coins"] = int(profile.get("coins", 0)) + 50
            else:
                profile["streak"] = 0
            save_profile()

        func save_profile() -> void:
            var f := FileAccess.open(SAVE_PATH, FileAccess.WRITE)
            if f != null:
                f.store_string(JSON.stringify(profile, "\t"))

        func load_profile() -> void:
            if not FileAccess.file_exists(SAVE_PATH):
                return
            var parsed: Variant = JSON.parse_string(FileAccess.get_file_as_string(SAVE_PATH))
            if parsed is Dictionary:
                profile.merge(parsed, true)
        """.trimIndent()
    ),
    KadiUpgradeBlueprint(
        id = 2,
        title = "Upgrade 3: Kenyan Tournament Elimination Mode (100-Point Cap)",
        badge = "SCORING & ELIMINATION",
        goal = "Implement official Kenyan Kadi multi-round tournament scoring where remaining cards in losers' hands add penalty points (Joker=50, A=20, 2=20, 3=30, J/Q/K=10, 8=8), eliminating anyone who hits 100 points!",
        classesToLookUp = listOf("Array", "Dictionary", "RefCounted"),
        architectureSteps = listOf(
            "Add a helper `card_tournament_points() -> int` on `CardData` using a `match rank:` block.",
            "In `GameState`, store `var tournament_scores: Array[int] = [0, 0, 0, 0]` and `const ELIMINATION_LIMIT: int = 100`.",
            "When a round ends, sum `card.card_tournament_points()` for each player's remaining hand and add it to `tournament_scores[seat]`.",
            "Display the tournament scoreboard in an overlay and mark any seat with `tournament_scores[seat] >= 100` as eliminated!"
        ),
        referenceCode = """
        # Add to res://scripts/card_data.gd:
        func tournament_points() -> int:
            match rank:
                CardTypes.Ranks.JOKER: return 50
                CardTypes.Ranks.THREE: return 30
                CardTypes.Ranks.TWO, CardTypes.Ranks.ACE: return 20
                CardTypes.Ranks.JACK, CardTypes.Ranks.QUEEN, CardTypes.Ranks.KING: return 10
                _: return rank # Ranks 4..10 count as face value

        # Add to res://scripts/game_state.gd:
        var tournament_scores: Array[int] = [0, 0, 0, 0]

        func score_round_end() -> Dictionary:
            for seat in range(hands.size()):
                var penalty_pts: int = 0
                for card in hands[seat]:
                    penalty_pts += card.tournament_points()
                tournament_scores[seat] += penalty_pts
            return {"scores": tournament_scores.duplicate()}
        """.trimIndent()
    ),
    KadiUpgradeBlueprint(
        id = 3,
        title = "Upgrade 4: Smart AI Personalities (Aggressive vs. Ace-Saver)",
        badge = "AI HEURISTIC SCORING",
        goal = "Upgrade the AI from playing the first legal card in its loop to scoring every legal candidate move based on an AI Personality (`AGGRESSIVE`, `DEFENSIVE`, `COMBO_MASTER`)!",
        classesToLookUp = listOf("Array", "RandomNumberGenerator"),
        architectureSteps = listOf(
            "Instead of returning the first legal card in `_choose_ai_move()`, collect all legal candidate moves into `var candidates: Array = []`.",
            "Write `score_candidate_move(move: Array[CardData], personality: int) -> int`.",
            "If `GameState.penalty_stack == 0`, give Aces a `-30` score for `DEFENSIVE` AI so it saves its Ace to block a future `+5` Joker penalty!",
            "Give multi-card Q+A chains and high-point cards (`Joker`, `3`, `2`, `K`, `J`) bonus score for `AGGRESSIVE` AI so it sheds dangerous tournament points fast."
        ),
        referenceCode = """
        enum AIPersonality { BALANCED, AGGRESSIVE, ACE_SAVER }

        func score_ai_card(card: CardData, personality: AIPersonality, penalty_active: bool) -> int:
            var score: int = card.rank
            if card.rank == CardTypes.Ranks.ACE and not penalty_active:
                # Save Aces for emergencies unless it's the AI's last 2 cards!
                score -= 40 if personality == AIPersonality.ACE_SAVER else 15
            if personality == AIPersonality.AGGRESSIVE and card.is_penalty():
                score += 35 # Prioritize dropping +2, +3, and +5 Jokers!
            return score
        """.trimIndent()
    ),
    KadiUpgradeBlueprint(
        id = 4,
        title = "Upgrade 5: 3D-Illusion Card Flip Tween & Victory Confetti",
        badge = "TWEENS & PARTICLES",
        goal = "Animate drawn cards flipping from `card_back.png` to their face-up rank/suit using a `scale.x` Tween, and burst `CPUParticles2D` gold confetti on victory!",
        classesToLookUp = listOf("Tween", "CPUParticles2D", "Control"),
        architectureSteps = listOf(
            "In `card_scene.gd`, add `func animate_flip_reveal() -> void:` and set `pivot_offset = size * 0.5` so scaling happens around the center of the card.",
            "Start with `top_rank_label.visible = false` and `card_bg` tinted navy blue (card back). Tween `scale.x` from `1.0` to `0.0` over `0.11s`.",
            "In `tween.tween_callback(_refresh_visuals)`, switch to the front face, then Tween `scale.x` from `0.0` back to `1.0` over `0.13s`!",
            "Add a `CPUParticles2D` node named `WinConfetti` on `Table.tscn` (`one_shot = true`, `explosiveness = 0.9`, `amount = 64`) and call `win_confetti.restart()` when `RuleValidator.has_won()` returns `true`."
        ),
        referenceCode = """
        # Add to res://scenes/card/card_scene.gd:
        func animate_flip_reveal() -> void:
            pivot_offset = size * 0.5
            top_rank_label.visible = false
            center_suit_label.visible = false
            var tw: Tween = create_tween()
            tw.tween_property(self, "scale:x", 0.0, 0.11).set_trans(Tween.TRANS_QUAD)
            tw.tween_callback(_refresh_visuals)
            tw.tween_property(self, "scale:x", 1.0, 0.13).set_trans(Tween.TRANS_BACK)
        """.trimIndent()
    ),
    KadiUpgradeBlueprint(
        id = 5,
        title = "Upgrade 6: Holographic Foil Joker & Ace Card Shader (.gdshader)",
        badge = "2D CANVAS SHADER",
        goal = "Write a custom Godot 4.3 2D `canvas_item` shader that sweeps a shimmering gold/rainbow foil sheen across Jokers and Aces!",
        classesToLookUp = listOf("CanvasItem", "Color", "TextureRect"),
        architectureSteps = listOf(
            "In the Inspector for `CardBg` or `CardArt`, expand **CanvasItem -> Material -> New ShaderMaterial -> New Shader** and save it as `res://assets/shaders/foil_card.gdshader`.",
            "Begin the shader with `shader_type canvas_item;` so it runs on 2D UI/Sprite nodes.",
            "In `void fragment()`, compute a diagonal wave using `sin((UV.x + UV.y) * 8.0 + TIME * 3.0)` and mix a warm gold highlight (`vec3(1.0, 0.88, 0.35)`) into `COLOR.rgb` when `is_foil` uniform is enabled!"
        ),
        referenceCode = """
        // File: res://assets/shaders/foil_card.gdshader
        shader_type canvas_item;

        uniform bool is_foil = true;
        uniform float sheen_strength : hint_range(0.0, 0.6) = 0.22;

        void fragment() {
            vec4 tex = texture(TEXTURE, UV) * COLOR;
            if (is_foil) {
                float wave = sin((UV.x * 1.4 + UV.y) * 10.0 - TIME * 3.2);
                float band = smoothstep(0.75, 0.98, wave);
                vec3 gold = vec3(1.0, 0.85, 0.32);
                tex.rgb = mix(tex.rgb, gold, band * sheen_strength);
            }
            COLOR = tex;
        }
        """.trimIndent()
    )
)

@Composable
private fun UpgradeKadiLabTab(onNavigateToPhase: (String) -> Unit) {
    var selectedUpgrade by rememberSaveable { mutableIntStateOf(0) }
    var showSolution by rememberSaveable(selectedUpgrade) { mutableStateOf(false) }
    val item = KADI_UPGRADES.firstOrNull { it.id == selectedUpgrade } ?: KADI_UPGRADES.first()

    HeroPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.Build, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(18.dp))
                Text(
                    text = "UPGRADE KADI LAB · SELF-GUIDED EXPANSIONS",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFBBF24)
                )
            }
            Pill(
                text = "Open Phase 12",
                color = Color(0xFF38BDF8),
                onClick = { onNavigateToPhase("phase_12_expand_kadi") }
            )
        }
        Text(
            text = "6 Pro Upgrades to Build Into Kadi On Your Own",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = heroTextPrimary()
        )
        Text(
            text = "Ready to improve Kadi without watching a tutorial? Pick a feature upgrade below, read the **No-Spoiler Architecture Spec**, look up the listed Godot 4.3 classes in the Reference Desk (or with **F1**), and code it yourself—then tap **Reveal Reference Code** to compare!",
            style = MaterialTheme.typography.bodySmall,
            color = heroTextSecondary()
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.primary) {
        Text(
            text = "PICK AN UPGRADE PROJECT:",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        ChipSelectorRow(
            items = KADI_UPGRADES.map { it.id to "Upgrade ${it.id + 1}" },
            selected = selectedUpgrade,
            onSelect = { selectedUpgrade = it }
        )
        Pill(text = item.badge, color = MaterialTheme.colorScheme.secondary)
        Text(
            text = item.title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        RichInlineText(
            text = item.goal,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.secondary) {
        Text(
            text = "GODOT 4.3 CLASSES TO LOOK UP (F1 OR REFERENCE DESK):",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item.classesToLookUp.forEach { cls ->
                Pill(text = cls, color = MaterialTheme.colorScheme.primary)
            }
        }

        Text(
            text = "NO-SPOILER ARCHITECTURE BLUEPRINT (TRY BUILDING FROM THIS FIRST!):",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 6.dp)
        )
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            item.architectureSteps.forEachIndexed { idx, step ->
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${idx + 1}.",
                        style = MaterialTheme.typography.bodyMedium.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    RichInlineText(
                        text = step,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        OutlinedButton(
            onClick = { showSolution = !showSolution },
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp)
        ) {
            Icon(Icons.Outlined.LockOpen, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text(
                text = if (showSolution) "Hide Reference Implementation" else "Reveal Reference Implementation (Compare Your Code)",
                fontWeight = FontWeight.Bold
            )
        }

        AnimatedVisibility(visible = showSolution) {
            Column(modifier = Modifier.padding(top = 8.dp)) {
                CodeBlock(
                    snippet = CodeSnippet(
                        id = "kadi_upgrade_${item.id}",
                        language = "gdscript",
                        code = item.referenceCode,
                        caption = "Reference GDScript / Shader for ${item.title}"
                    )
                )
            }
        }
    }
}

private data class LiveKadiCard(
    val id: Int,
    val suit: Int, // 0=♥, 1=♦, 2=♣, 3=♠
    val rank: Int  // 2..14, 15=JOK
) {
    val suitSymbol: String
        get() = when {
            rank == 15 -> "★"
            suit == 0 -> HEART
            suit == 1 -> DIAMOND
            suit == 2 -> CLUB
            else -> SPADE
        }

    val rankLabel: String
        get() = when (rank) {
            11 -> "J"
            12 -> "Q"
            13 -> "K"
            14 -> "A"
            15 -> "JOK"
            else -> rank.toString()
        }

    val displayName: String
        get() = if (rank == 15) "JOK ★" else "$rankLabel$suitSymbol"

    val isRed: Boolean
        get() = suit == 0 || suit == 1

    val isPenalty: Boolean
        get() = rank == 2 || rank == 3 || rank == 15

    val penaltyValue: Int
        get() = when (rank) {
            2 -> 2
            3 -> 3
            15 -> 5
            else -> 0
        }

    val isQuestion: Boolean
        get() = rank == 8 || rank == 12

    val isAnswer: Boolean
        get() = rank in listOf(4, 5, 6, 7, 9, 10, 14)

    val roleBadge: String
        get() = when {
            isPenalty -> "+$penaltyValue Penalty"
            isQuestion -> "Question"
            rank == 11 -> "Jump"
            rank == 13 -> "Kickback"
            rank == 14 -> "Wild Ace"
            else -> "Answer"
        }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun PlayableKadiMiniTableTab(onNavigateToPhase: (String) -> Unit) {
    var matchSeed by rememberSaveable { mutableIntStateOf(1) }
    var topCard by remember(matchSeed) { mutableStateOf(LiveKadiCard(100, 0, 6)) } // 6♥ starter
    val playerHand = remember(matchSeed) {
        mutableStateListOf(
            LiveKadiCard(1, 0, 12), // Q♥ (Question)
            LiveKadiCard(2, 0, 7),  // 7♥ (Answer)
            LiveKadiCard(3, 3, 7),  // 7♠ (Answer — matches rank 7)
            LiveKadiCard(4, 1, 2),  // 2♦ (+2 Penalty)
            LiveKadiCard(5, 2, 14)  // A♣ (Wild Ace / Penalty Blocker)
        )
    }
    val stagedIds = remember(matchSeed) { mutableStateListOf<Int>() }
    var aiCardCount by rememberSaveable(matchSeed) { mutableIntStateOf(4) }
    var deckCount by rememberSaveable(matchSeed) { mutableIntStateOf(36) }
    var penaltyStack by rememberSaveable(matchSeed) { mutableIntStateOf(0) }
    var declaredSuit by rememberSaveable(matchSeed) { mutableIntStateOf(-1) }
    var directionCw by rememberSaveable(matchSeed) { mutableStateOf(true) }
    var announcedNikoKadi by rememberSaveable(matchSeed) { mutableStateOf(false) }
    var awaitingSuitChoice by rememberSaveable(matchSeed) { mutableStateOf(false) }
    var matchWon by rememberSaveable(matchSeed) { mutableStateOf(false) }
    var nextCardId by rememberSaveable(matchSeed) { mutableIntStateOf(10) }

    var feedbackBanner by rememberSaveable(matchSeed) {
        mutableStateOf("Karibu Kadi! Tap cards in your hand below to lift them (-14px), then tap Play Selected!")
    }
    var gdscriptTrace by rememberSaveable(matchSeed) {
        mutableStateOf(
            "# Live Godot 4.3 Signal & Rule Trace:\n" +
                "GameState.start_new_match(4, 4) -> Top Starter: 6♥\n" +
                "# Try staging Q♥ + 7♥ together for a Question+Answer combo!"
        )
    }

    val suitNames = mapOf(0 to "$HEART Hearts", 1 to "$DIAMOND Diamonds", 2 to "$CLUB Clubs", 3 to "$SPADE Spades")

    fun triggerAiTurnResponse(humanMoveSummary: String, skippedAi: Boolean) {
        if (matchWon) return
        if (skippedAi) {
            feedbackBanner = "$humanMoveSummary · ⏭ Jump skipped AI Opponent — it's your turn again!"
            gdscriptTrace = "TurnManager.advance_turn(extra_skips = 1)\n# AI Seat skipped! Current turn returns to Player 0 (You)."
            return
        }
        if (penaltyStack > 0) {
            aiCardCount += penaltyStack
            deckCount = (deckCount - penaltyStack).coerceAtLeast(10)
            val absorbed = penaltyStack
            penaltyStack = 0
            feedbackBanner = "$humanMoveSummary · 🤖 AI Opponent couldn't counter and drew +$absorbed penalty cards!"
            gdscriptTrace = "GameState.consume_penalty_stack(ai_seat)\n# AI drew +$absorbed cards (AI hand: $aiCardCount) -> Turn: Player 0 (You)"
        } else {
            val aiSuit = if (declaredSuit != -1) declaredSuit else topCard.suit
            val aiRanks = listOf(4, 5, 6, 7, 9, 10)
            val chosenRank = aiRanks[(matchSeed + deckCount + aiCardCount) % aiRanks.size]
            val aiPlayed = LiveKadiCard(nextCardId++, aiSuit, chosenRank)
            topCard = aiPlayed
            declaredSuit = -1
            if (aiCardCount > 1) {
                aiCardCount -= 1
                feedbackBanner = "$humanMoveSummary · 🤖 AI played ${aiPlayed.displayName} (AI has $aiCardCount cards left)."
                gdscriptTrace = "_execute_ai_turn(1) -> AI played ${aiPlayed.displayName} onto DiscardSlot\nTurnManager.advance_turn() -> Turn: Player 0 (You)"
            } else {
                aiCardCount += 1
                deckCount = (deckCount - 1).coerceAtLeast(10)
                feedbackBanner = "$humanMoveSummary · 🤖 AI had no winning play and drew 1 card."
                gdscriptTrace = "_execute_ai_turn(1) -> GameState.draw_cards(1, 1)\nTurnManager.advance_turn() -> Turn: Player 0 (You)"
            }
        }
    }

    HeroPanel {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Outlined.SportsEsports, contentDescription = null, tint = Color(0xFF34D399), modifier = Modifier.size(18.dp))
                Text(
                    text = "TANGIBLE PREVIEW · PLAYABLE KADI TABLE",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF34D399)
                )
            }
            Pill(
                text = "Reset Table",
                color = Color(0xFF38BDF8),
                icon = Icons.Outlined.Refresh,
                onClick = { matchSeed += 1 }
            )
        }
        Text(
            text = "Experience What You're Building Right Now!",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = heroTextPrimary()
        )
        Text(
            text = "This interactive tabletop runs the exact Godot 4.3 scene layout (`Table.tscn` + `CardScene.tscn`) and `RuleValidator` logic you build in Phase 1 & Phase 2. Tap cards below to lift them `-14px`, stage Question+Answer combos (`Q♥ + 7♥`), and watch the live GDScript trace!",
            style = MaterialTheme.typography.bodySmall,
            color = heroTextSecondary()
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        color = Color(0xFF0E231C),
        border = BorderStroke(1.5.dp, Color(0xFF10B981).copy(alpha = 0.55f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF081812), Color(0xFF0E291F), Color(0xFF091D16))
                    )
                )
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.Black.copy(alpha = 0.38f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.14f))
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "HUD/StatusLabel · ${if (directionCw) "↻ CW" else "↺ CCW"} · Deck: $deckCount",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF6EE7B7)
                        )
                        Text(
                            text = "AI Opponent: $aiCardCount cards",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFDE047)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (penaltyStack > 0) {
                            Pill(text = "Penalty Stack: +$penaltyStack", color = Color(0xFFF87171))
                        }
                        if (declaredSuit != -1) {
                            Pill(text = "Demanded Suit: ${suitNames[declaredSuit]}", color = Color(0xFF38BDF8))
                        }
                        if (announcedNikoKadi) {
                            Pill(text = "📢 NIKO KADI ANNOUNCED!", color = Color(0xFFFBBF24))
                        }
                    }
                    Text(
                        text = feedbackBanner,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFFF8FAFC),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Draw Pile",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = Color(0xFFA7F3D0)
                    )
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF1E3A8A),
                        border = BorderStroke(1.5.dp, Color(0xFF60A5FA)),
                        modifier = Modifier.size(width = 74.dp, height = 104.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(6.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "KADI",
                                style = MaterialTheme.typography.labelMedium.copy(fontFamily = CodeFontFamily),
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF93C5FD)
                            )
                            Text(
                                text = "$deckCount",
                                style = MaterialTheme.typography.titleMedium.copy(fontFamily = CodeFontFamily),
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(Modifier.width(24.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "DiscardSlot (Top Card)",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = Color(0xFFFDE047)
                    )
                    VisualGodotCardWidget(
                        card = topCard,
                        isSelected = false,
                        orderBadge = null,
                        onClick = null
                    )
                }
            }

            if (awaitingSuitChoice) {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.5.dp, Color(0xFF38BDF8))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "DeclareSuitDialog.tscn · ACE PLAYED! PICK NEXT SUIT:",
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF38BDF8)
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            suitNames.forEach { (suitIdx, suitLabel) ->
                                Button(
                                    onClick = {
                                        declaredSuit = suitIdx
                                        awaitingSuitChoice = false
                                        gdscriptTrace = "DeclareSuitDialog.suit_declared.emit($suitIdx)\nGameState.declared_suit = $suitIdx # Demanded $suitLabel"
                                        triggerAiTurnResponse("You declared $suitLabel", skippedAi = false)
                                    },
                                    modifier = Modifier.weight(1f),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = suitLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "PlayerHandContainer (HBoxContainer) — Tap cards to stage:",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFA7F3D0)
                    )
                    Text(
                        text = "${stagedIds.size} staged",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        color = Color(0xFFFDE047)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState())
                        .padding(top = 16.dp, bottom = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    playerHand.forEach { card ->
                        val stagedIdx = stagedIds.indexOf(card.id)
                        val isSel = stagedIdx != -1
                        VisualGodotCardWidget(
                            card = card,
                            isSelected = isSel,
                            orderBadge = if (isSel) "${stagedIdx + 1}" else null,
                            onClick = {
                                if (!awaitingSuitChoice && !matchWon) {
                                    if (isSel) {
                                        stagedIds.remove(card.id)
                                        gdscriptTrace = "CardScene.card_clicked.emit(${card.displayName})\n" +
                                            "staged_cards.erase(${card.displayName}) -> card_node.set_selected(false) # position.y = 0.0"
                                    } else {
                                        stagedIds.add(card.id)
                                        gdscriptTrace = "CardScene.card_clicked.emit(${card.displayName})\n" +
                                            "staged_cards.append(${card.displayName}) -> card_node.set_selected(true) # position.y = -14.0"
                                    }
                                }
                            }
                        )
                    }
                }
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        val stagedCards = stagedIds.mapNotNull { id -> playerHand.firstOrNull { it.id == id } }
                        if (stagedCards.isEmpty()) return@Button
                        val validation = validateLiveMove(
                            cards = stagedCards,
                            top = topCard,
                            penaltyStack = penaltyStack,
                            declaredSuit = declaredSuit
                        )
                        if (!validation.first) {
                            val drawn = randomLiveCard(nextCardId++, matchSeed + deckCount)
                            playerHand.add(drawn)
                            deckCount = (deckCount - 1).coerceAtLeast(1)
                            stagedIds.clear()
                            announcedNikoKadi = false
                            feedbackBanner = "❌ Illegal move (${validation.second})! +1 penalty card (${drawn.displayName}) added to your hand."
                            gdscriptTrace = "RuleValidator.validate_move(...) == false\n" +
                                "GameState.draw_cards(0, 1) # +1 Penalty for invalid move!"
                        } else {
                            val hadPenalty = penaltyStack > 0
                            var skips = 0
                            stagedCards.forEach { c ->
                                playerHand.removeAll { it.id == c.id }
                                topCard = c
                                if (declaredSuit != -1 && c.suit == declaredSuit && c.rank != 14) {
                                    declaredSuit = -1
                                }
                                if (c.rank == 11) skips += 1
                                if (c.rank == 13) directionCw = !directionCw
                                if (c.isPenalty) penaltyStack += c.penaltyValue
                                if (c.rank == 14 && hadPenalty) penaltyStack = 0
                            }
                            stagedIds.clear()
                            val playedStr = stagedCards.joinToString(", ") { it.displayName }

                            val allWinningTypes = stagedCards.all { it.isAnswer || it.isQuestion }
                            if (playerHand.isEmpty() && announcedNikoKadi && allWinningTypes && aiCardCount > 0) {
                                matchWon = true
                                feedbackBanner = "🏆 YOU WIN THE KADI MATCH! You announced Niko Kadi and played out with $playedStr!"
                                gdscriptTrace = "RuleValidator.has_won(0, [$playedStr]) == true\n" +
                                    "🏆 MATCH VICTORY! Tap 'Reset Table' above to play again."
                                return@Button
                            }

                            if (playerHand.isEmpty() && !announcedNikoKadi) {
                                val drawn = randomLiveCard(nextCardId++, matchSeed + deckCount)
                                playerHand.add(drawn)
                                feedbackBanner = "⚠️ You emptied your hand without calling 'Niko Kadi!' first — drew ${drawn.displayName} to continue!"
                                gdscriptTrace = "RuleValidator.has_won(0) == false (announced_kadi[0] was false!)\n" +
                                    "# Remember to tap 'Niko Kadi!' the round BEFORE playing your last Answer card."
                                return@Button
                            }

                            val lastCard = stagedCards.last()
                            if (lastCard.rank == 14 && !hadPenalty) {
                                awaitingSuitChoice = true
                                feedbackBanner = "🌟 Wild Ace played! Choose the suit to demand in DeclareSuitDialog above."
                                gdscriptTrace = "RuleValidator.validate_move([$playedStr]) == true\n" +
                                    "declare_suit_dialog.show() # Waiting for suit_declared(suit) signal..."
                            } else {
                                gdscriptTrace = "RuleValidator.validate_move([$playedStr]) == true\n" +
                                    "GameState.discard_pile.append(${lastCard.displayName}) -> GameState.state_updated.emit()"
                                triggerAiTurnResponse("You played $playedStr", skippedAi = skips > 0)
                            }
                        }
                    },
                    enabled = stagedIds.isNotEmpty() && !awaitingSuitChoice && !matchWon,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF10B981),
                        contentColor = Color(0xFF042F2E)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1.2f)
                ) {
                    Icon(Icons.Outlined.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Play Selected (${stagedIds.size})",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                FilledTonalButton(
                    onClick = {
                        stagedIds.clear()
                        announcedNikoKadi = false
                        if (penaltyStack > 0) {
                            val count = penaltyStack
                            penaltyStack = 0
                            repeat(count) {
                                playerHand.add(randomLiveCard(nextCardId++, matchSeed + deckCount + it))
                            }
                            deckCount = (deckCount - count).coerceAtLeast(1)
                            gdscriptTrace = "GameState.consume_penalty_stack(0) -> Drew +$count penalty cards!"
                            triggerAiTurnResponse("You accepted +$count penalty cards", skippedAi = false)
                        } else {
                            val drawn = randomLiveCard(nextCardId++, matchSeed + deckCount)
                            playerHand.add(drawn)
                            deckCount = (deckCount - 1).coerceAtLeast(1)
                            gdscriptTrace = "GameState.draw_cards(0, 1) -> Drew ${drawn.displayName} into PlayerHandContainer"
                            triggerAiTurnResponse("You drew ${drawn.displayName}", skippedAi = false)
                        }
                    },
                    enabled = !awaitingSuitChoice && !matchWon,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = if (penaltyStack > 0) "Accept +$penaltyStack" else "Draw Card",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = {
                        announcedNikoKadi = true
                        feedbackBanner = "📢 You announced: NIKO KADI! Win on your next move by playing all your Answer cards!"
                        gdscriptTrace = "_on_niko_pressed() -> GameState.announced_kadi[0] = true\n" +
                            "niko_label.text = \"📢 You announced: NIKO KADI!\""
                    },
                    enabled = !awaitingSuitChoice && !matchWon && !announcedNikoKadi,
                    border = BorderStroke(1.dp, Color(0xFFFBBF24)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Outlined.Campaign, contentDescription = null, tint = Color(0xFFFBBF24), modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Niko Kadi!",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFBBF24)
                    )
                }
            }
        }
    }

    KadiCard(accent = MaterialTheme.colorScheme.secondary) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                IconBubble(icon = Icons.Outlined.Bolt, tint = MaterialTheme.colorScheme.secondary, size = 32.dp)
                Column {
                    Text(
                        text = "LIVE GODOT 4.3 EXECUTION TRACE",
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = "Exact GDScript Functions Triggered by Your Action",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Pill(
                text = "Build Phase 1",
                color = MaterialTheme.colorScheme.primary,
                onClick = { onNavigateToPhase("phase_01_project_deck") }
            )
        }
        CodeBlock(
            snippet = CodeSnippet(
                id = "live_table_trace",
                language = "gdscript",
                code = gdscriptTrace,
                caption = "You build this exact visual table in Phase 1 and the rule engine in Phases 2–9!"
            )
        )
    }
}

@Composable
private fun VisualGodotCardWidget(
    card: LiveKadiCard,
    isSelected: Boolean,
    orderBadge: String?,
    onClick: (() -> Unit)?
) {
    val liftDp by animateDpAsState(
        targetValue = if (isSelected) (-14).dp else 0.dp,
        animationSpec = tween(durationMillis = 150),
        label = "cardLift"
    )
    val suitColor = if (card.isRed) Color(0xFFE11D48) else Color(0xFF0F172A)
    val borderColor = if (isSelected) Color(0xFFFACC15) else Color(0xFFCBD5E1)
    val bgColor = if (isSelected) Color(0xFFFEFCE8) else Color(0xFFF8FAFC)

    Box(
        modifier = Modifier
            .offset(y = liftDp)
            .width(78.dp)
            .height(112.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(
                width = if (isSelected) 2.5.dp else 1.2.dp,
                color = borderColor,
                shape = RoundedCornerShape(10.dp)
            )
            .let { mod -> if (onClick != null) mod.clickable(onClick = onClick) else mod }
            .padding(7.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = card.displayName,
                    style = MaterialTheme.typography.labelLarge.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.ExtraBold,
                    color = suitColor
                )
                if (orderBadge != null) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFF59E0B)
                    ) {
                        Text(
                            text = orderBadge,
                            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily, fontSize = 10.sp),
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                        )
                    }
                }
            }

            Text(
                text = card.suitSymbol,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = suitColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = card.roleBadge,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF475569),
                maxLines = 1,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

private fun validateLiveMove(
    cards: List<LiveKadiCard>,
    top: LiveKadiCard,
    penaltyStack: Int,
    declaredSuit: Int
): Pair<Boolean, String> {
    if (cards.isEmpty()) return false to "No cards selected"
    val first = cards.first()
    val firstLegal = when {
        penaltyStack > 0 -> first.rank == 14 || (first.isPenalty && first.rank == top.rank)
        declaredSuit != -1 -> first.suit == declaredSuit || first.rank == 14 || first.rank == 15
        first.rank == 14 || first.rank == 15 -> true
        else -> first.suit == top.suit || first.rank == top.rank
    }
    if (!firstLegal) {
        return when {
            penaltyStack > 0 -> false to "${first.displayName} cannot counter a +$penaltyStack penalty (need Ace or matching penalty rank)"
            declaredSuit != -1 -> false to "${first.displayName} does not match the demanded suit"
            else -> false to "${first.displayName} does not match ${top.displayName}'s suit or rank"
        }
    }
    for (i in cards.indices) {
        val cur = cards[i]
        if (cur.isQuestion) {
            if (i + 1 >= cards.size) {
                return false to "Question card ${cur.displayName} must be paired with an Answer card in the same move"
            }
            val nxt = cards[i + 1]
            if (!nxt.isQuestion && (!nxt.isAnswer || (nxt.suit != cur.suit && nxt.rank != 14))) {
                return false to "Answer card ${nxt.displayName} must match ${cur.displayName}'s suit"
            }
        } else if (i + 1 < cards.size) {
            val nxt = cards[i + 1]
            if (cur.rank != nxt.rank) {
                return false to "Multi-card combo requires matching ranks (${cur.displayName} vs ${nxt.displayName})"
            }
        }
    }
    return true to "Legal move"
}

private fun randomLiveCard(id: Int, seed: Int): LiveKadiCard {
    val pool = listOf(
        0 to 4, 0 to 7, 0 to 8, 0 to 11,
        1 to 5, 1 to 9, 1 to 12, 1 to 13,
        2 to 3, 2 to 6, 2 to 10, 2 to 14,
        3 to 2, 3 to 7, 3 to 8, 3 to 11, 0 to 15
    )
    val pick = pool[(id + seed).mod(pool.size)]
    return LiveKadiCard(id = id, suit = pick.first, rank = pick.second)
}

@Composable
private fun DownloadedAssetsTab() {
    var selectedConvention by rememberSaveable { mutableIntStateOf(0) }
    val conventions = listOf(
        0 to "suit_rank.png (hearts_7.png)",
        1 to "rank_of_suit.png (7_of_hearts.png)",
        2 to "Kenney Pack (cardHearts7.png)",
        3 to "Sprite Sheet Grid (AtlasTexture)"
    )

    HeroPanel {
        Text(
            text = "USING YOUR DOWNLOADED ASSETS IN GODOT 4.3",
            style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
            fontWeight = FontWeight.Bold,
            color = heroTextTertiary()
        )
        Text(
            text = "Plug In Any Downloaded Card Pack, Table Felt & Audio",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = heroTextPrimary()
        )
        Text(
            text = "Downloaded a 2D playing card pack, UI buttons, or sound effects from itch.io, Kenney.nl, or OpenGameArt? Select your asset pack format below to get the exact Godot 4.3 folder setup, Inspector settings, and copy-ready GDScript loader.",
            style = MaterialTheme.typography.bodySmall,
            color = heroTextSecondary()
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.secondary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.Collections, tint = MaterialTheme.colorScheme.secondary, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "STEP 1 · WHERE TO DROP DOWNLOADED FILES",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "Recommended res://assets/ Folder Layout",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        RichInlineText(
            text = "Drag and drop your unzipped files straight from your computer's file explorer into Godot's **FileSystem dock** under `res://assets/`. Godot 4.3 automatically generates `.import` files for every `.png`, `.svg`, `.ttf`, `.wav`, and `.ogg` file.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        CodeBlock(
            snippet = CodeSnippet(
                id = "asset_folders",
                language = "text",
                code = """
                res://assets/
                ├── cards/
                │   ├── card_back.png           # Draw pile & AI opponent card backs
                │   ├── joker_red.png           # Red Joker (+5 Penalty)
                │   ├── joker_black.png         # Black Joker (+5 Penalty)
                │   └── ... (52 suit/rank PNGs OR 1 deck_sheet.png sprite sheet)
                ├── table/
                │   └── table_bg.png            # Downloaded green felt / wood tabletop texture
                ├── ui/
                │   ├── button_normal.png       # Optional 9-patch / StyleBoxTexture button skin
                │   └── suit_icons.png          # Hearts, Diamonds, Clubs, Spades icons for Ace popup
                ├── fonts/
                │   └── game_font.ttf           # Custom downloaded .ttf or .otf font
                └── audio/
                    ├── card_slap.ogg           # Played when a card hits the discard pile
                    ├── card_shuffle.ogg        # Played on deal & draw-pile reshuffle
                    └── niko_kadi.ogg           # Played when announcing "Niko Kadi!"
                """.trimIndent(),
                caption = "Drop your downloaded files into these folders inside res://assets/."
            )
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.primary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.Style, tint = MaterialTheme.colorScheme.primary, size = 34.dp)
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "STEP 2 · PICK YOUR DOWNLOADED CARD PACK FORMAT",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Interactive GDScript Asset Loader Generator",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Text(
            text = "Tap the filename style that matches the card pack you downloaded:",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        ChipSelectorRow(
            items = conventions,
            selected = selectedConvention,
            onSelect = { selectedConvention = it }
        )

        val generatedCode = remember(selectedConvention) {
            when (selectedConvention) {
                0 -> """
                # Format 1: suit_rank.png (e.g. res://assets/cards/hearts_7.png, spades_14.png)
                # Add this helper inside res://scripts/card_data.gd:
                func get_texture_path() -> String:
                    if rank == CardTypes.Ranks.JOKER:
                        return "res://assets/cards/joker_red.png" if is_red_suit() else "res://assets/cards/joker_black.png"
                    var suit_folder_name: String = SUIT_NAMES.get(suit, "Hearts").to_lower()
                    return "res://assets/cards/%s_%d.png" % [suit_folder_name, rank]

                # Inside res://scenes/card/card_scene.gd (_refresh_visuals):
                @onready var card_art: TextureRect = ${'$'}CardArt

                func _refresh_visuals() -> void:
                    if data == null:
                        return
                    var path: String = data.get_texture_path()
                    if ResourceLoader.exists(path):
                        card_art.texture = load(path)
                        card_art.visible = true
                        top_rank_label.visible = false
                        center_suit_label.visible = false
                """.trimIndent()
                1 -> """
                # Format 2: rank_of_suit.png (e.g. res://assets/cards/7_of_hearts.png, ace_of_spades.png)
                # Standard naming used by SVG/PNG playing-card packs on itch.io & OpenGameArt:
                const ASSET_RANKS: Dictionary = {
                    11: "jack", 12: "queen", 13: "king", 14: "ace"
                }

                func get_texture_path() -> String:
                    if rank == CardTypes.Ranks.JOKER:
                        return "res://assets/cards/red_joker.png" if is_red_suit() else "res://assets/cards/black_joker.png"
                    var r_str: String = ASSET_RANKS.get(rank, str(rank))
                    var s_str: String = SUIT_NAMES.get(suit, "hearts").to_lower()
                    return "res://assets/cards/%s_of_%s.png" % [r_str, s_str]

                # Inside res://scenes/card/card_scene.gd:
                @onready var card_art: TextureRect = ${'$'}CardArt

                func _refresh_visuals() -> void:
                    var path: String = data.get_texture_path()
                    if ResourceLoader.exists(path):
                        card_art.texture = load(path)
                        card_art.show()
                """.trimIndent()
                2 -> """
                # Format 3: Kenney.nl Playing Cards Pack (e.g. res://assets/cards/cardHearts7.png, cardSpadesA.png)
                const KENNEY_RANKS: Dictionary = {
                    11: "J", 12: "Q", 13: "K", 14: "A"
                }

                func get_texture_path() -> String:
                    if rank == CardTypes.Ranks.JOKER:
                        return "res://assets/cards/cardJoker.png"
                    var suit_cap: String = SUIT_NAMES.get(suit, "Hearts") # "Hearts", "Diamonds", "Clubs", "Spades"
                    var rank_cap: String = KENNEY_RANKS.get(rank, str(rank))
                    return "res://assets/cards/card%s%s.png" % [suit_cap, rank_cap]

                # Inside res://scenes/card/card_scene.gd:
                @onready var card_art: TextureRect = ${'$'}CardArt

                func _refresh_visuals() -> void:
                    var path: String = data.get_texture_path()
                    if ResourceLoader.exists(path):
                        card_art.texture = load(path)
                        card_art.show()
                """.trimIndent()
                else -> """
                # Format 4: Single Sprite Sheet Image (res://assets/cards/deck_sheet.png)
                # Slices 1 card out of a 13-column x 4-row sprite sheet using AtlasTexture!
                const SHEET_TEX: Texture2D = preload("res://assets/cards/deck_sheet.png")
                const CARD_W: int = 140 # Width of 1 card in pixels on your sprite sheet
                const CARD_H: int = 190 # Height of 1 card in pixels on your sprite sheet

                func make_atlas_texture(card: CardData) -> AtlasTexture:
                    var atlas := AtlasTexture.new()
                    atlas.atlas = SHEET_TEX
                    # Column 0 = Ace (or 2), Row 0..3 = Hearts, Diamonds, Clubs, Spades
                    var col: int = 0 if card.rank == 14 else (card.rank - 1)
                    var row: int = int(card.suit)
                    atlas.region = Rect2(col * CARD_W, row * CARD_H, CARD_W, CARD_H)
                    return atlas
                """.trimIndent()
            }
        }

        CodeBlock(
            snippet = CodeSnippet(
                id = "asset_loader_gen",
                language = "gdscript",
                code = generatedCode,
                caption = "Copy this into card_data.gd and card_scene.gd to load your downloaded card sprites!"
            )
        )
    }

    Callout(
        type = CalloutType.GODOT,
        body = "**Crucial Godot 4.3 Inspector Settings for `CardArt (TextureRect)`**:\n" +
            "1. Set **Expand Mode** to `Ignore Size` (so a high-res `500×726` downloaded card PNG shrinks cleanly into your `84×120` card node instead of blowing up the `HBoxContainer`!).\n" +
            "2. Set **Stretch Mode** to `Keep Aspect Centered`.\n" +
            "3. Set **Mouse -> Filter** to `Ignore` (or place `ClickButton` last in the Scene Tree) so `CardArt` never blocks finger taps on `ClickButton`!"
    )

    Callout(
        type = CalloutType.TIP,
        body = "**Pixel Art vs. HD Vector Card Packs**:\n" +
            "- If you downloaded a **Pixel Art** card pack, go to **Project -> Project Settings -> Rendering -> Textures -> Canvas Textures** and set **Default Texture Filter** to `Nearest` so pixels stay razor-sharp.\n" +
            "- If you downloaded an **HD PNG or SVG** card pack, keep **Default Texture Filter** on `Linear` (or select the PNGs in the FileSystem dock -> **Import tab** -> check **Mipmaps -> Generate** and click **Reimport**) so scaled-down cards look silky smooth on phones!"
    )
}

private data class BlueprintFileEntry(
    val path: String,
    val phaseId: String,
    val phaseLabel: String,
    val role: String,
    val keyContents: String
)

private val PROJECT_FILES = listOf(
    BlueprintFileEntry(
        path = "res://scenes/card/card_scene.tscn + card_scene.gd",
        phaseId = "phase_01_project_deck",
        phaseLabel = "Phase 1 (Quickstart)",
        role = "Reusable visual playing card scene (`Control` + `CardArt: TextureRect` + fallback `Label`s + `ClickButton`) that lifts `-14px` when clicked and emits `card_clicked(card_node)`.",
        keyContents = "signal card_clicked(card_node: Control)\n@onready var card_art: TextureRect = \$CardArt\nfunc setup(data: Variant) -> void\nfunc set_selected(selected: bool) -> void"
    ),
    BlueprintFileEntry(
        path = "res://scenes/table/table.tscn + table.gd",
        phaseId = "phase_01_project_deck",
        phaseLabel = "Phases 1–12",
        role = "Main gameplay scene built right in Phase 1 combining the green felt tabletop, `DiscardSlot`, `PlayerHandContainer`, HUD buttons, AI Turn Timer, Tweens, and downloaded Audio.",
        keyContents = "func _ready() -> void\nfunc _on_play_selected_pressed() -> void\nfunc _on_draw_pressed() -> void\nfunc _on_niko_pressed() -> void\nfunc _execute_ai_turn(ai_seat: int) -> void"
    ),
    BlueprintFileEntry(
        path = "res://scripts/card_types.gd",
        phaseId = "phase_02_dealing_scenes",
        phaseLabel = "Phase 2",
        role = "Global enum definitions (`class_name CardTypes`) for Suits, Ranks (2..15), and CardKind.",
        keyContents = "enum Suits { HEARTS, DIAMONDS, CLUBS, SPADES }\nenum Ranks { TWO = 2, ..., ACE = 14, JOKER = 15 }"
    ),
    BlueprintFileEntry(
        path = "res://scripts/card_data.gd",
        phaseId = "phase_02_dealing_scenes",
        phaseLabel = "Phase 2",
        role = "Custom Resource (`class_name CardData`) storing a single card's suit, rank, downloaded texture resolver (`resolve_downloaded_texture()`), and Kadi rule helpers.",
        keyContents = "func resolve_downloaded_texture() -> Texture2D\nfunc is_penalty() -> bool\nfunc is_question() -> bool\nfunc is_answer() -> bool"
    ),
    BlueprintFileEntry(
        path = "res://scripts/game_state.gd",
        phaseId = "phase_02_dealing_scenes",
        phaseLabel = "Phases 2, 5, 8, 9, 10",
        role = "Autoload Singleton (`GameState`) holding the live 54-card draw pile, discard pile, 4 player hands, penalty stack, declared suit, and Niko Kadi flags.",
        keyContents = "var deck: Array[CardData]\nvar discard_pile: Array[CardData]\nvar hands: Array\nvar penalty_stack: int = 0\nvar declared_suit: int = -1\nvar announced_kadi: Array[bool]"
    ),
    BlueprintFileEntry(
        path = "res://scripts/turn_manager.gd",
        phaseId = "phase_03_turn_signals",
        phaseLabel = "Phases 3 & 7",
        role = "Autoload Singleton (`TurnManager`) controlling seat index (0..3), clockwise/counter-clockwise direction (`1` or `-1`), Jump skips, and Kickback reversals.",
        keyContents = "signal turn_changed(player_index: int)\nfunc advance_turn(extra_skips: int = 0) -> void\nfunc reverse_direction() -> void"
    ),
    BlueprintFileEntry(
        path = "res://scripts/rule_validator.gd",
        phaseId = "phase_04_play_validation",
        phaseLabel = "Phases 4, 5, 6, 8, 9",
        role = "Pure rule engine (`class_name RuleValidator`) that checks single-card and multi-card moves (Q+A chains, penalty counters, Ace declarations, and 5-clause win checks).",
        keyContents = "static func is_single_card_playable(card: CardData, top: CardData, ...) -> bool\nstatic func validate_move(cards: Array, ...) -> bool"
    ),
    BlueprintFileEntry(
        path = "res://scenes/ui/declare_suit_dialog.tscn + .gd",
        phaseId = "phase_08_ace_declarations",
        phaseLabel = "Phase 8",
        role = "Popup dialog shown whenever Player 0 plays an Ace to demand Hearts, Diamonds, Clubs, or Spades.",
        keyContents = "signal suit_declared(suit: int)"
    ),
    BlueprintFileEntry(
        path = "res://scripts/save_manager.gd + tournament_rules.gd",
        phaseId = "phase_12_expand_kadi",
        phaseLabel = "Phase 12 (Expansions)",
        role = "Persistent JSON save/load system (`user://kadi_profile.json`), Kenyan 100-point Tournament Elimination scorer, and LAN multiplayer `@rpc` sync.",
        keyContents = "func save_profile() -> void\nfunc load_profile() -> void\nstatic func compute_hand_points(hand: Array) -> int"
    )
)

@Composable
private fun ProjectMapTab(onNavigateToPhase: (String) -> Unit) {
    val isDark = isDarkScheme()
    val uiPrefs = LocalUiPreferences.current
    val codeBg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground

    HeroPanel {
        Text(
            text = "HOW ALL FILES FIT TOGETHER",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = heroTextTertiary()
        )
        Text(
            text = "Complete Godot 4.3 Folder Structure",
            style = MaterialTheme.typography.headlineSmall,
            color = heroTextPrimary()
        )
        Text(
            text = "You build the visual scenes (`card_scene.tscn` & `table.tscn`) first in Phase 1 so your game is immediately tangible, wire the 54-card deck (`GameState`) in Phase 2, add full Kadi rules in Phases 3–11, and master solo expansions & new game genres in Phases 12–13.",
            style = MaterialTheme.typography.bodyMedium,
            color = heroTextSecondary()
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = codeBg,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.7f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(14.dp)
        ) {
            Text(
                text = """
                res:// (Your Godot 4.3 Kadi Project Root)
                ├── project.godot                 # Registers GameState, TurnManager & SaveManager Autoloads
                ├── assets/                       # Drop your downloaded assets here!
                │   ├── cards/                    # 52 card PNGs + joker PNGs + card_back.png
                │   ├── table/                    # table_bg.png (felt or wood background)
                │   ├── ui/                       # Button & badge textures
                │   ├── fonts/                    # Custom .ttf / .otf fonts
                │   └── audio/                    # card_slap.ogg, card_shuffle.ogg, niko_kadi.ogg
                ├── scenes/
                │   ├── card/
                │   │   ├── card_scene.tscn       # Phase 1: Visual 84x120 Card Control + CardArt + Button
                │   │   └── card_scene.gd         # Phase 1: Click-to-lift (-14px) & downloaded sprite loader
                │   ├── table/
                │   │   ├── table.tscn            # Phase 1+: Main green felt board, HUD, AI timer, sounds
                │   │   └── table.gd              # Phases 1-12: Orchestrates dealing, human/AI turns, wins
                │   └── ui/
                │       ├── declare_suit_dialog.tscn  # Phase 8: Ace suit-picker popup
                │       └── declare_suit_dialog.gd    # Phase 8: Emits suit_declared(suit)
                └── scripts/
                    ├── card_types.gd             # Phase 2: Suits & Ranks enums (class_name CardTypes)
                    ├── card_data.gd              # Phase 2: CardData Resource + resolve_downloaded_texture()
                    ├── game_state.gd             # Phase 2+: Autoload singleton holding 54-card deck & hands
                    ├── turn_manager.gd           # Phase 3+: Autoload singleton for turn order & direction
                    ├── rule_validator.gd         # Phases 4-9: Pure Kadi rule & move validation helpers
                    ├── kadi_self_test.gd         # Phase 10: Automated edge-case & 54-card invariant suite
                    ├── save_manager.gd           # Phase 12: Persistent JSON save/load (user://kadi_profile.json)
                    └── tournament_rules.gd       # Phase 12: Kenyan 100-pt Tournament Elimination & AI scorer
                """.trimIndent(),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontFamily = CodeFontFamily,
                    fontSize = (12f * uiPrefs.codeFontScale).sp,
                    lineHeight = (19f * uiPrefs.codeFontScale).sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    SectionHeader(title = "File-by-File Breakdown", eyebrow = "TAP TO OPEN PHASE")

    PROJECT_FILES.forEach { entry ->
        KadiCard(onClick = { onNavigateToPhase(entry.phaseId) }) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconBubble(icon = Icons.Outlined.Description, tint = MaterialTheme.colorScheme.primary, size = 34.dp)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = entry.path,
                        style = MaterialTheme.typography.titleSmall.copy(fontFamily = CodeFontFamily),
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Pill(text = entry.phaseLabel, color = MaterialTheme.colorScheme.secondary)
            }
            RichInlineText(
                text = entry.role,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            CodeBlock(
                snippet = CodeSnippet(
                    id = entry.path,
                    language = "gdscript",
                    code = entry.keyContents,
                    caption = null
                )
            )
        }
    }
}

@Composable
private fun SceneAndSignalsTab() {
    val isDark = isDarkScheme()
    val uiPrefs = LocalUiPreferences.current
    val codeBg = if (isDark) SyntaxColors.darkBackground else SyntaxColors.lightBackground

    KadiCard(accent = MaterialTheme.colorScheme.primary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.AccountTree, tint = MaterialTheme.colorScheme.primary, size = 34.dp)
            Column {
                Text(
                    text = "CARD & TABLE SCENES WITH DOWNLOADED ASSETS",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = CodeFontFamily),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Exact Node Hierarchies in Godot 4.3",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            color = codeBg,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(12.dp)
            ) {
                Text(
                    text = """
                    1. CARD SCENE (res://scenes/card/card_scene.tscn)
                    Card (Control)  [custom_minimum_size = Vector2(84, 120), script: card_scene.gd]
                    ├── CardBg (Panel)                     # Fallback card body & border
                    ├── CardArt (TextureRect)              # Displays downloaded PNG (Expand: Ignore Size, Mouse: Ignore)
                    ├── TopRankLabel (Label)               # Fallback rank label (hidden automatically when CardArt loads)
                    ├── CenterSuitLabel (Label)            # Fallback suit label (hidden automatically when CardArt loads)
                    └── ClickButton (Button)               # Flat = true, Full Rect (captures taps on the card)

                    2. MAIN TABLE SCENE (res://scenes/table/table.tscn)
                    Table (Node2D)  [script: res://scenes/table/table.gd]
                    ├── Background (ColorRect)             # Fallback felt color (#0E231C)
                    ├── TableArt (TextureRect)             # Displays res://assets/table/table_bg.png if present
                    ├── DiscardSlot (Control)              # Holds the top face-up CardScene instance
                    ├── PlayerHandContainer (HBoxContainer)# Fans Player 0's CardScene nodes automatically
                    ├── HUD (CanvasLayer)
                    │   ├── StatusLabel (Label)            # Turn, Deck count, Penalty stack, Demanded suit
                    │   ├── FeedbackLabel (Label)          # Rule feedback ("Player 1 played: J♥")
                    │   ├── NikoKadiLabel (Label)          # "📢 You announced: NIKO KADI!"
                    │   ├── PlaySelectedButton (Button)    # Plays staged card(s)
                    │   ├── DrawButton (Button)            # Draws card / accepts penalty (can show card_back.png icon)
                    │   ├── NikoKadiButton (Button)        # Announces "Niko Kadi!" for next round
                    │   └── DeclareSuitDialog (Control)    # Instance of declare_suit_dialog.tscn (hidden)
                    ├── AITurnTimer (Timer)                # 0.75s one-shot pause so AI turns feel natural
                    └── Sounds (Node)
                        ├── SlapSound (AudioStreamPlayer)    # Plays res://assets/audio/card_slap.ogg
                        ├── ShuffleSound (AudioStreamPlayer) # Plays res://assets/audio/card_shuffle.ogg
                        └── NikoSound (AudioStreamPlayer)    # Plays res://assets/audio/niko_kadi.ogg
                    """.trimIndent(),
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontFamily = CodeFontFamily,
                        fontSize = (12f * uiPrefs.codeFontScale).sp,
                        lineHeight = (19f * uiPrefs.codeFontScale).sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }

    KadiCard(accent = MaterialTheme.colorScheme.secondary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.Hub, tint = MaterialTheme.colorScheme.secondary, size = 34.dp)
            Column {
                Text(
                    text = "SIGNAL FLOW CHEAT SHEET",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = "How Clicking a Card Updates the Game",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        CodeBlock(
            snippet = CodeSnippet(
                id = "signal_flow",
                language = "text",
                code = """
                1. Human clicks a card in PlayerHandContainer:
                   CardScene.ClickButton.pressed -> emits CardScene.card_clicked(self)
                
                2. Table._on_hand_card_clicked(card_node) toggles card selection:
                   Lifts the card up by 14px (position.y = -14.0) and stages it in staged_cards!
                
                3. Human clicks PlaySelectedButton:
                   Table validates staged cards via RuleValidator.validate_move(...)
                   - Legal: moves cards to GameState.discard_pile, plays SlapSound, applies effects
                            (Penalty +2/+3/+5, Jump skip, Kickback reverse, or opens DeclareSuitDialog)
                   - Illegal: draws 1 penalty card and advances turn
                
                4. TurnManager.advance_turn() emits turn_changed(next_player):
                   - If next_player != 0 (AI Opponent): Table starts AITurnTimer (0.75s),
                     then AI picks a legal move or draws automatically!
                   - If next_player == 0 (Human): HUD enables buttons for your turn!
                """.trimIndent(),
                caption = "Follow these 4 steps whenever you trace a turn in Kadi."
            )
        )
    }
}

private val SIM_SUITS = listOf(
    0 to "$HEART Hearts",
    1 to "$DIAMOND Diamonds",
    2 to "$CLUB Clubs",
    3 to "$SPADE Spades"
)

private val SIM_RANKS = listOf(
    2 to "2 (Penalty +2)",
    3 to "3 (Penalty +3)",
    4 to "4 (Answer)",
    7 to "7 (Answer)",
    8 to "8 (Question)",
    11 to "J (Jump)",
    12 to "Q (Question)",
    13 to "K (Kickback)",
    14 to "A (Ace / Wild)",
    15 to "JOK (Joker +5)"
)

@Composable
private fun RuleSimulatorTab() {
    var topSuit by remember { mutableIntStateOf(0) }
    var topRank by remember { mutableIntStateOf(7) }
    var playSuit by remember { mutableIntStateOf(0) }
    var playRank by remember { mutableIntStateOf(11) }
    var penaltyStack by remember { mutableIntStateOf(0) }
    var declaredSuit by remember { mutableIntStateOf(-1) }
    var hasAnswerCompanion by remember { mutableStateOf(true) }

    val eval = remember(topSuit, topRank, playSuit, playRank, penaltyStack, declaredSuit, hasAnswerCompanion) {
        evaluateSimulatedPlay(
            topSuit = topSuit,
            topRank = topRank,
            playSuit = playSuit,
            playRank = playRank,
            penaltyStack = penaltyStack,
            declaredSuit = declaredSuit,
            hasAnswerCompanion = hasAnswerCompanion
        )
    }

    KadiCard(accent = MaterialTheme.colorScheme.primary) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(icon = Icons.Outlined.Style, tint = MaterialTheme.colorScheme.primary, size = 34.dp)
            Column {
                Text(
                    text = "INTERACTIVE KADI RULE TESTER",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Test any card play and see the GDScript branch",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        Text("1. Top Card on Discard Pile:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        ChipSelectorRow(items = SIM_SUITS, selected = topSuit, onSelect = { topSuit = it })
        ChipSelectorRow(items = SIM_RANKS, selected = topRank, onSelect = { topRank = it })

        Text("2. Active Table Modifiers:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        ChipSelectorRow(
            items = listOf(0 to "No Penalty (0)", 2 to "Penalty +2", 3 to "Penalty +3", 5 to "Joker Penalty +5"),
            selected = penaltyStack,
            onSelect = { penaltyStack = it }
        )
        ChipSelectorRow(
            items = listOf(-1 to "No Suit Demand") + SIM_SUITS.map { it.first to "Demand ${it.second}" },
            selected = declaredSuit,
            onSelect = { declaredSuit = it }
        )

        Text("3. Card You Want to Play:", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        ChipSelectorRow(items = SIM_SUITS, selected = playSuit, onSelect = { playSuit = it })
        ChipSelectorRow(items = SIM_RANKS, selected = playRank, onSelect = { playRank = it })

        if (playRank == 8 || playRank == 12) {
            ChipSelectorRow(
                items = listOf(1 to "Paired with matching Answer Card", 0 to "Played ALONE (No Answer Card)"),
                selected = if (hasAnswerCompanion) 1 else 0,
                onSelect = { hasAnswerCompanion = (it == 1) }
            )
        }
    }

    val resultAccent = if (eval.isLegal) successColor() else MaterialTheme.colorScheme.error
    KadiCard(accent = resultAccent) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            IconBubble(
                icon = if (eval.isLegal) Icons.Outlined.CheckCircle else Icons.Outlined.Cancel,
                tint = resultAccent,
                size = 36.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (eval.isLegal) "LEGAL PLAY IN KADI" else "ILLEGAL PLAY — PENALTY DRAW!",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = resultAccent
                )
                RichInlineText(
                    text = eval.headline,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        RichInlineText(
            text = eval.explanation,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        CodeBlock(
            snippet = CodeSnippet(
                id = "sim_gdscript",
                language = "gdscript",
                code = eval.gdscriptTrace,
                caption = "Exact GDScript rule branch triggered by this move"
            )
        )
    }
}

@Composable
private fun ChipSelectorRow(
    items: List<Pair<Int, String>>,
    selected: Int,
    onSelect: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { (value, label) ->
            val isSel = value == selected
            Surface(
                shape = MaterialTheme.shapes.extraSmall,
                color = if (isSel) MaterialTheme.colorScheme.primary.copy(alpha = 0.22f)
                else MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.6f),
                border = BorderStroke(
                    width = if (isSel) 1.5.dp else 1.dp,
                    color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                ),
                modifier = Modifier.clickable { onSelect(value) }
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}

private data class SimEvaluation(
    val isLegal: Boolean,
    val headline: String,
    val explanation: String,
    val gdscriptTrace: String
)

private fun evaluateSimulatedPlay(
    topSuit: Int,
    topRank: Int,
    playSuit: Int,
    playRank: Int,
    penaltyStack: Int,
    declaredSuit: Int,
    hasAnswerCompanion: Boolean
): SimEvaluation {
    if (penaltyStack > 0) {
        if (playRank == 14) {
            return SimEvaluation(
                isLegal = true,
                headline = "Ace cancels the +$penaltyStack penalty stack!",
                explanation = "Per Kadi rules, playing an Ace blocks an active penalty stack outright and resets `penalty_stack` to `0`.",
                gdscriptTrace = "if GameState.penalty_stack > 0:\n    if card.rank == CardTypes.Ranks.ACE:\n        GameState.penalty_stack = 0  # Penalty cancelled!\n        return true"
            )
        }
        if (playRank == topRank && (playRank == 2 || playRank == 3 || playRank == 15)) {
            val added = if (playRank == 15) 5 else playRank
            return SimEvaluation(
                isLegal = true,
                headline = "Same-rank Penalty Card forwards stack (+${penaltyStack + added} total)!",
                explanation = "Playing a matching Penalty rank (`$playRank`) adds `+$added` and forwards the obligation to the next player.",
                gdscriptTrace = "if GameState.penalty_stack > 0:\n    if card.rank == top.rank:\n        GameState.penalty_stack += card.penalty_value() # Now ${penaltyStack + added}\n        return true"
            )
        }
        return SimEvaluation(
            isLegal = false,
            headline = "Cannot play this card while facing a +$penaltyStack penalty!",
            explanation = "When `penalty_stack > 0`, you must either play a Penalty Card of the **same rank** or play an **Ace**, otherwise you must draw `$penaltyStack` cards.",
            gdscriptTrace = "if GameState.penalty_stack > 0:\n    # Card is neither an Ace nor matching penalty rank!\n    pick_up(GameState.penalty_stack)\n    GameState.penalty_stack = 0\n    return false"
        )
    }

    if ((playRank == 8 || playRank == 12) && !hasAnswerCompanion) {
        return SimEvaluation(
            isLegal = false,
            headline = "Question Card (Q or 8) played without an Answer Card!",
            explanation = "In Kadi, a Question Card must be accompanied in the same move by a matching Answer Card (**4, 5, 6, 7, 9, 10, or Ace**). Without one, you must draw from the pile.",
            gdscriptTrace = "if is_question_card(card.rank) and companion_answer == null:\n    draw_card(current_player) # Must accompany Question with Answer!\n    return false"
        )
    }

    if (declaredSuit != -1) {
        val suitMatch = playSuit == declaredSuit || playRank == 14 || playRank == 15
        return if (suitMatch) {
            SimEvaluation(
                isLegal = true,
                headline = "Satisfies the active Suit Declaration!",
                explanation = "Your card matches the demanded suit (or is a wild Ace/Joker). Once played, `declared_suit` resets to `-1`.",
                gdscriptTrace = "if GameState.declared_suit != -1:\n    if card.suit == GameState.declared_suit or card.rank == Ranks.ACE:\n        GameState.declared_suit = -1 # Demand satisfied!\n        return true"
            )
        } else {
            SimEvaluation(
                isLegal = false,
                headline = "Does not match the declared suit!",
                explanation = "An Ace previously demanded a specific suit. Playing a non-matching suit incurs a 1-card penalty draw.",
                gdscriptTrace = "if GameState.declared_suit != -1 and card.suit != GameState.declared_suit:\n    draw_card(current_player) # 1-card penalty\n    return false"
            )
        }
    }

    if (playRank == 14 || playRank == 15) {
        return SimEvaluation(
            isLegal = true,
            headline = "Wild Card (Ace / Joker) is playable on any suit!",
            explanation = "An Ace lets you declare the next suit, while a Joker adds a **+5 penalty** while preserving the active suit.",
            gdscriptTrace = "if card.rank == CardTypes.Ranks.ACE or card.rank == CardTypes.Ranks.JOKER:\n    return true"
        )
    }

    val matchesSuit = playSuit == topSuit
    val matchesRank = playRank == topRank
    return if (matchesSuit || matchesRank) {
        val reason = when {
            matchesSuit && matchesRank -> "matches both suit and rank"
            matchesSuit -> "matches the suit"
            else -> "matches the rank"
        }
        SimEvaluation(
            isLegal = true,
            headline = "Legal move — $reason of the top card!",
            explanation = "Because `card.suit == top.suit or card.rank == top.rank` evaluates to `true`, the card moves to the discard pile.",
            gdscriptTrace = "func is_playable(card: CardData, top: CardData) -> bool:\n    return card.suit == top.suit or card.rank == top.rank # -> true"
        )
    } else {
        SimEvaluation(
            isLegal = false,
            headline = "Neither suit nor rank matches!",
            explanation = "In Kadi, playing a card that matches neither suit nor rank penalizes the player with **+1 card from the draw pile** and ends their turn.",
            gdscriptTrace = "if not (card.suit == top.suit or card.rank == top.rank):\n    draw_card(current_player) # +1 penalty card!\n    TurnManager.advance_turn()\n    return false"
        )
    }
}
