# Kadi Companion App (v2.3.0) — Tangible-First Curriculum & Live Playable Kadi Table Edition

An offline-first Android companion app (Kotlin + Jetpack Compose + Material 3 + Room + Hilt) that teaches programming and **Godot 4.3** game development step by step by building the Kenyan card game **Kadi** — now restructured so you build and interact with a **visible, clickable Kadi tabletop and playing card from your very first lessons**!

## What's New in v2.3.0 (Tangible-First Overhaul)

1. **Tangible-First Curriculum Progression (Visual Card & Table on Screen Immediately!)**:
   - **Phase 0 (`Visual GDScript Playground: Animate a Live Kadi Card`)**: No more 8 lessons of dry console `print()` statements before installing Godot! Lesson 1 puts a real **Emerald Felt Kadi Table and an `84×120` visual playing card (`7♥`)** on screen in 60 seconds, and every GDScript concept (`var`, `bool`, `if/elif/else`, `for`, `func`, `Array`, `Dictionary`, `class_name`) visually colors, lifts (`-14px`), fans, and deals cards on screen!
   - **Phase 1 (`Tangible Quickstart: Visual Card Scene & Playable Table`)**: Flipped the old "backend data first" order into **"Visual Scene First!"** — you build `res://scenes/card/card_scene.tscn` (`84×120` playing card with `-14px` click-to-lift) and `res://scenes/table/table.tscn` (Emerald Felt tabletop, `DiscardSlot`, `PlayerHandContainer`, and `PlaySelectedButton`) **first**, so by the end of Phase 1 you can click cards in your hand and play them onto the center discard pile!
   - **Phase 2 (`54-Card Kadi Deck Engine & Live 4-Player Dealing`)**: Powers your visible Phase 1 table with `CardTypes`, `CardData` (including automatic downloaded sprite loading), and the `GameState` Autoload singleton so every press of **F5** deals a live 54-card match with a guaranteed legal opening card (`4, 5, 6, 7, 9, 10`).
   - **Concrete `tangibleOutcome` & Visual Verification Across All 45 Lessons**: Every phase and lesson now highlights **What You'll See on Screen** and replaces generic boilerplate checklists with concrete visual tests in the running Godot 4.3 window.

2. **Interactive Playable Kadi Mini-Table & Live Card Preview Inside the App**:
   - **New `"Play Kadi Live"` Tab (`ProjectBlueprintScreen.kt`)**: Play a real interactive mini-match of Kenyan Kadi right on your phone! Tap cards in your hand to watch them lift `-14dp`, stage multi-card Question+Answer combos (`Q♥ + 7♥`), counter penalties with Aces, call *"Niko Kadi!"*, and watch the **Live Godot 4.3 GDScript Execution Trace** below every move.
   - **In-Lesson `"Try Live Card"` Preview (`LessonDetailScreen.kt`)**: Every lesson includes an expandable interactive `84×120` Godot card preview at the top of the guide so you can tap the card, test `-14px` selection lift, cycle Kadi card roles, and see the live signal trace while reading.
   - **Seamless Completion & Next-Lesson Flow**: Passing a lesson's **Practice Workbench** check or finishing its **Knowledge Quiz** now automatically marks the lesson complete and provides a one-tap **Continue to Next Lesson** button.

3. **Zero Raw Markdown Artifacts & Full Live UI Studio (27 Real-Time Options)**:
   - Recursive `RichInlineText` parser, smart responsive tables, one-tap GDScript code wrap/copy, and 27 live theme, palette, font, and layout customizations.
