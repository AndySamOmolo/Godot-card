# Solo Game Developer Handbook (Godot 4.3)
*Bundled with Kadi Companion v3.0.0 — Build New Games & Upgrade Kadi Without Tutorials!*

## 1. The 5-Question Architecture Blueprint for Starting Any New Game Solo

Whenever you open a blank Godot 4.3 project to build a **2D Platformer**, **Top-Down RPG**, **Space Shooter**, **Deckbuilder**, or **Puzzle Game**, answer these 5 questions first:

1. **Pure Data (`Resource`)**: What custom `Resource` class (`extends Resource`) holds the stats for my cards, items, weapons, or enemies?
   - *In Kadi*: `res://scripts/card_data.gd` (`class_name CardData`)
   - *In an RPG / Platformer*: `class_name WeaponData extends Resource` (`@export var damage: int`, `@export var icon: Texture2D`)
2. **Reusable Actor Templates (`PackedScene`)**: Which `.tscn` scenes get spawned repeatedly at runtime via `.instantiate()`?
   - *In Kadi*: `res://scenes/card/card_scene.tscn`
   - *In an Action Game*: `bullet.tscn`, `coin.tscn`, `goblin_enemy.tscn`
3. **Global Memory (`Autoload`)**: Which state needs to survive across scene transitions (`MainMenu.tscn` -> `Level1.tscn`)?
   - *In Kadi*: `GameState`, `TurnManager`, `SaveManager`
   - *In an Action Game*: `GameManager` (score, current level, unlocked upgrades), `Events` (global signal bus)
4. **Decoupled Communication (`Signal`)**: Where do child nodes need to notify parents (**Call Down, Signal Up**)?
   - *In Kadi*: `CardScene.card_clicked.emit(self)`, `DeclareSuitDialog.suit_declared.emit(suit)`
   - *In an Action Game*: `HealthComponent.died.emit()`, `Coin.collected.emit(10)`
5. **World vs. HUD (`Node2D` + `CanvasLayer`)**: What belongs in the scrolling 2D world (`Node2D` / `TileMapLayer` / `CharacterBody2D`) vs. pinned to the screen (`HUD: CanvasLayer`)?

## 2. How to Figure Out Any Godot 4.3 Node Without Watching a Tutorial

1. **Press `F1` (Search Help)** in the Godot Editor (or `Ctrl+Click` any class or method name inside your script).
2. Read the **Properties**, **Methods**, and **Signals** tables at the top of that class's offline page.
3. While your game is running (**F5**), switch the **Scene dock** (top-left) from **Local** to **Remote** to inspect the live nodes, positions, visibility, and mouse filters in memory!
