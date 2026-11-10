# Kadi Companion App (v3.0.0) — Solo Game Architect, Complete Godot 4.3 Mastery & Playable Kadi Edition

An offline-first Android companion app (Kotlin + Jetpack Compose + Material 3 + Room + Hilt) that teaches programming and **Godot 4.3** game development step by step by building the Kenyan card game **Kadi** — and equips you to **improve Kadi or build a brand-new game in Godot 4.3 completely on your own without needing tutorials!**

## What's New in v3.0.0 (Solo Game Developer Mastery Edition)

1. **14-Phase Curriculum (53 Deep Lessons) — From First Card to Solo Game Creation**:
   - **Phases 00–11 (Complete Kadi Game)**: Visual GDScript Playground, Tangible Quickstart (`card_scene.tscn` & `table.tscn`), 54-Card `GameState` Engine, `TurnManager` & AI Opponents, `RuleValidator`, Penalty Stacking (`+2, +3, +5`), Question+Answer Chains (`Q`/`8`), Jump (`J`) & Kickback (`K`), Ace Suit Declarations, *"Niko Kadi!"* 5-Clause Win Detection, Automated `assert()` Self-Tests, Tweens, Audio & Exporting.
   - **NEW Phase 12 (`Expanding Kadi On Your Own`)**:
     - `l_12_01_save_load`: Persistent Player Profiles, Win Streaks & Coins (`FileAccess`, `JSON` & `user://`)
     - `l_12_02_drag_drop_shaders`: Interactive Drag-and-Drop Card Physics (`_gui_input`), `CPUParticles2D` Victory Confetti & 2D Foil Shaders (`.gdshader`)
     - `l_12_03_menus_tournament`: Main Menu Scene Switching (`change_scene_to_file`) & Kenyan 100-Point Tournament Elimination Mode
     - `l_12_04_multiplayer_lan`: Local Pass-and-Play Privacy Curtain & Wi-Fi LAN Multiplayer (`ENetMultiplayerPeer` & `@rpc`)
   - **NEW Phase 13 (`Escape Tutorial Hell: Build Any New Game in Godot 4.3 Solo!`)**:
     - `l_13_01_blank_canvas_framework`: The 5-Question Game Architecture Blueprint (`Resource`, `PackedScene`, `Autoload`, `Signal`, `CanvasLayer`) + Reusable `HealthComponent`
     - `l_13_02_realtime_loop_physics`: From Turn-Based to 60 FPS Real-Time (`_physics_process(delta)`, `Input.get_vector()`, `CharacterBody2D`, Platformer Jump & Coyote Time)
     - `l_13_03_world_combat_camera`: World Building (`Area2D` Hitboxes/Pickups, `TileMapLayer`, `Camera2D` Smoothing/Shake & Collision Layers vs. Masks)
     - `l_13_04_solo_mastery_docs`: Solo Developer Mastery (`F1` Offline Docs, Finite State Machines & Live **Remote Scene Tree** Debugging)

2. **Cross-Genre Transfer & Solo Builder Challenges in Every Single Lesson**:
   - Every lesson across all 14 phases now includes:
     - **🌍 Beyond Kadi: Cross-Genre Game Dev** — shows how the exact concept in that lesson is used in **2D Platformers, Top-Down RPGs, Space Shooters, Deckbuilders, and Puzzle Games**.
     - **🚀 Solo Builder Challenge (No Hand-Holding)** — a concrete independent feature prompt with a completion tracker so you practice building without step-by-step instructions.

3. **Interactive `"New Game Architect"` & `"Upgrade Kadi Lab"` Studio (`ProjectBlueprintScreen.kt`)**:
   - **New Game Architect Tab**: Pick any of **5 game genres** (*2D Action Platformer*, *Top-Down RPG / Survivor*, *Roguelike Deckbuilder*, *Arcade Space Shooter*, *Grid Puzzle / Match-3*) to get a Kadi-to-Genre Concept Bridge, complete Godot 4.3 Scene Tree, copy-ready core starter script, and 3 solo expansion missions!
   - **Upgrade Kadi Lab Tab**: **6 self-guided Kadi expansion blueprints** (*Drag-and-Drop Cards*, *JSON Save/Load Profiles*, *100-Point Tournament Mode*, *Smart AI Personalities*, *3D-Illusion Card Flip Tween*, and *Holographic Foil `.gdshader`*) with No-Spoiler Architecture Specs, `F1` classes to look up, and toggleable reference code.

4. **3-Tab Reference Desk with `"GDScript Cookbook"`, 74 Upgraded Class Docs & 127 Rich Glossary Terms**:
   - **GDScript Cookbook Tab (`DocsListScreen.kt`)**: 15 copy-ready solo game dev recipes (8-Way RPG Movement, Platformer Coyote Jump, Hitbox/Hurtbox `Area2D`, Drag-and-Drop UI, `PackedScene` Spawning, `JSON` Save/Load, `await` Coroutines, 2D Card Flip Tween, `Camera2D` Shake, Enum State Machine, Global Signal Bus, Weighted Loot Drops, LAN `@rpc`).
   - **74 Godot 4.3 Class Docs (`app/src/main/assets/godot_docs/*.md`)**: Every class now features real, copy-ready Kadi + Cross-Genre Solo Game Dev code examples.
   - **127 Enriched Glossary Terms (`app/src/main/assets/glossary/terms.json`)**: Every term includes a runnable GDScript 4.3 pattern and solo game-dev transfer note.

5. **Upgraded Practice Workbench (`Solo Mode` + GDScript 4.3 Syntax Diagnostics) & Windows JDK 26 Build Fix**:
   - Switch between **Guided Mode** and **Solo Scratchpad Mode** (write code from scratch without starter scaffolding), backed by a structural GDScript linter that checks missing colons (`:`), unbalanced brackets/parentheses, and `=` vs `==` inside `if` conditions.
   - `gradlew.bat` automatically detects Android Studio's bundled JBR (JDK 17/21) when system `JAVA_HOME` points to JDK 25+ (fixing the `JavaVersion.parse("26.0.1")` crash on Windows).
