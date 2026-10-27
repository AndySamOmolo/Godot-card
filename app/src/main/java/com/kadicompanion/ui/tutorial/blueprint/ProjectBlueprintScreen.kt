package com.kadicompanion.ui.tutorial.blueprint

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountTree
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Collections
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Hub
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
 * 4-Tab Blueprint, Downloaded Assets Studio & Kadi Rule Simulator:
 * - Tab 0: Downloaded Assets (Interactive Asset Pack Naming & GDScript Loader Generator + Import Dock Guide)
 * - Tab 1: Project Map       (Complete res:// folder structure including res://assets/ & file breakdown)
 * - Tab 2: Scene & Signals   (Exact Node Hierarchy with TextureRect / TextureButton for downloaded sprites)
 * - Tab 3: Rule Simulator    (Interactive Kenyan Kadi rule sandbox)
 */
@Composable
fun ProjectBlueprintScreen(
    onBack: () -> Unit,
    onNavigateToPhase: (String) -> Unit
) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }
    val tabs = listOf("Downloaded Assets", "Project Map", "Scene & Signals", "Rule Simulator")
    val hPad = studioScreenPadding()
    val vGap = studioCardSpacing()

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            KadiTopBar(
                title = "Blueprint & Assets Studio",
                subtitle = "DOWNLOADED ASSETS · ARCHITECTURE",
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
                    0 -> DownloadedAssetsTab()
                    1 -> ProjectMapTab(onNavigateToPhase = onNavigateToPhase)
                    2 -> SceneAndSignalsTab()
                    3 -> RuleSimulatorTab()
                }
                Spacer(Modifier.height(20.dp))
            }
        }
    }
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
        path = "res://scripts/card_types.gd",
        phaseId = "phase_01_project_deck",
        phaseLabel = "Phase 1",
        role = "Global enum definitions (`class_name CardTypes`) for Suits, Ranks (2..15), and CardKind.",
        keyContents = "enum Suits { HEARTS, DIAMONDS, CLUBS, SPADES }\nenum Ranks { TWO = 2, ..., ACE = 14, JOKER = 15 }"
    ),
    BlueprintFileEntry(
        path = "res://scripts/card_data.gd",
        phaseId = "phase_01_project_deck",
        phaseLabel = "Phase 1",
        role = "Custom Resource (`class_name CardData`) storing a single card's suit, rank, downloaded texture path (`get_texture_path()`), and rule helpers.",
        keyContents = "func get_texture_path() -> String\nfunc is_penalty() -> bool\nfunc is_question() -> bool\nfunc is_answer() -> bool"
    ),
    BlueprintFileEntry(
        path = "res://scripts/game_state.gd",
        phaseId = "phase_01_project_deck",
        phaseLabel = "Phases 1, 5, 8, 9, 10",
        role = "Autoload Singleton (`GameState`) holding the live 54-card draw pile, discard pile, 4 player hands, penalty stack, declared suit, and Niko Kadi flags.",
        keyContents = "var deck: Array[CardData]\nvar discard_pile: Array[CardData]\nvar hands: Array\nvar penalty_stack: int = 0\nvar declared_suit: int = -1\nvar announced_kadi: Array[bool]"
    ),
    BlueprintFileEntry(
        path = "res://scenes/card/card_scene.tscn + card_scene.gd",
        phaseId = "phase_02_dealing_scenes",
        phaseLabel = "Phases 2 & 4",
        role = "Reusable visual playing card scene (`Control` + `CardArt: TextureRect` + fallback `Label`s + `ClickButton`) that displays your downloaded card PNG and emits `card_clicked(card_node)`.",
        keyContents = "signal card_clicked(card_node: Control)\n@onready var card_art: TextureRect = \$CardArt\nfunc setup(data: CardData) -> void\nfunc set_selected(selected: bool) -> void"
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
        role = "Pure rule engine (`class_name RuleValidator`) that checks single-card and multi-card moves (Q+A chains, penalty counters, Ace declarations, and 4-clause win checks).",
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
        path = "res://scenes/table/table.tscn + table.gd",
        phaseId = "phase_02_dealing_scenes",
        phaseLabel = "Phases 2–11",
        role = "Main gameplay scene combining the Draw/Discard piles, Player Hand HBoxContainer, AI Turn Timer, Play Selected / Draw / Niko Kadi buttons, Tweens, and downloaded Audio.",
        keyContents = "func _ready() -> void\nfunc _on_play_selected_pressed() -> void\nfunc _on_draw_pressed() -> void\nfunc _on_niko_pressed() -> void\nfunc _execute_ai_turn(ai_seat: int) -> void"
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
            text = "Your project combines 2 Autoload scripts, 3 helper classes, 3 scenes, and your `res://assets/` folder for downloaded sprites, fonts, and audio. Tap any file card below to open its phase.",
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
                ├── project.godot                 # Registers GameState & TurnManager Autoloads
                ├── assets/                       # Drop your downloaded assets here!
                │   ├── cards/                    # 52 card PNGs + joker PNGs + card_back.png
                │   ├── table/                    # table_bg.png (felt or wood background)
                │   ├── ui/                       # Button & badge textures
                │   ├── fonts/                    # Custom .ttf / .otf fonts
                │   └── audio/                    # card_slap.ogg, card_shuffle.ogg, niko_kadi.ogg
                ├── scripts/
                │   ├── card_types.gd             # Phase 1: Suits & Ranks enums (class_name CardTypes)
                │   ├── card_data.gd              # Phase 1: CardData Resource + get_texture_path()
                │   ├── game_state.gd             # Phase 1+: Autoload singleton holding deck, hands, piles
                │   ├── turn_manager.gd           # Phase 3+: Autoload singleton for turn order & direction
                │   └── rule_validator.gd         # Phases 4-9: Pure Kadi rule & move validation helpers
                └── scenes/
                    ├── card/
                    │   ├── card_scene.tscn       # Phase 2: Card Control + CardArt (TextureRect) + Button
                    │   └── card_scene.gd         # Phase 2: Loads downloaded PNG (or fallback labels)
                    ├── ui/
                    │   ├── declare_suit_dialog.tscn  # Phase 8: Ace suit-picker popup
                    │   └── declare_suit_dialog.gd    # Phase 8: Emits suit_declared(suit)
                    └── table/
                        ├── table.tscn            # Phases 2-11: Main game board, HUD, AI timer, sounds
                        └── table.gd              # Phases 2-11: Orchestrates dealing, human/AI turns, wins
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
                    ├── Background (ColorRect)             # Fallback felt color
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
                headline = "Does not match the Declared Suit!",
                explanation = "While an Ace suit demand is active, you must play a card of the demanded suit (or another Ace/Joker), regardless of the top card's rank.",
                gdscriptTrace = "if GameState.declared_suit != -1:\n    return card.suit == GameState.declared_suit or card.rank == Ranks.ACE\n    # -> Returns FALSE (suit mismatch)"
            )
        }
    }

    val isWild = playRank == 14 || playRank == 15
    val matchesSuitOrRank = (playSuit == topSuit) || (playRank == topRank)
    return if (isWild || matchesSuitOrRank) {
        val effectNote = when (playRank) {
            11 -> "**Jump Card**: skips the next player (`TurnManager.advance_turn(1)`)."
            13 -> "**Kickback Card**: reverses turn direction (`TurnManager.reverse_direction()`)."
            2, 3, 15 -> "**Penalty Card**: adds to `GameState.penalty_stack` for the next player."
            8, 12 -> "**Question + Answer combo**: both cards land on the discard pile in one move."
            14 -> "**Ace played**: opens `DeclareSuitDialog` so you can demand the next suit."
            else -> "**Answer Card**: lands cleanly on the discard pile and advances the turn."
        }
        SimEvaluation(
            isLegal = true,
            headline = "Matches ${if (isWild) "as Wild (Ace/Joker)" else if (playSuit == topSuit) "by Suit" else "by Rank"}!",
            explanation = effectNote,
            gdscriptTrace = "func is_playable(card: CardData, top: CardData) -> bool:\n    if card.rank == Ranks.ACE or card.rank == Ranks.JOKER:\n        return true\n    return card.suit == top.suit or card.rank == top.rank # -> TRUE"
        )
    } else {
        SimEvaluation(
            isLegal = false,
            headline = "Neither Suit nor Rank matches the top card!",
            explanation = "In Kadi, playing a non-matching card costs you a 1-card penalty draw from the draw pile and passes the turn.",
            gdscriptTrace = "if not is_playable(card, top):\n    draw_card(current_player) # 1-card penalty for invalid play\n    TurnManager.advance_turn()\n    return false"
        )
    }
}
