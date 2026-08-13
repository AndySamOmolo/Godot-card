---
class: PackedScene
inherits: Resource
verified: 4.3
short: The in-memory representation of a saved scene; instantiate() spawns a fresh copy of its node tree.
---

## Properties
- `path: String` — The `.tscn` file this packed scene was loaded from (read-only).
- `state: SceneState` — The internal state object holding the scene's nodes and resources.

## Methods
- `Node instantiate(edit_state: int = 1)` — Returns a new instance of the scene's node tree.
- `Node instantiate(edit_state: int = 1)` — (Override note: pass `GEN_EDIT_STATE_INSTANCE` for tool scripts, `GEN_EDIT_STATE_MAIN`/`GEN_EDIT_STATE_MAIN_INNER` rarely from runtime.)
- `bool can_instantiate()` — True if the scene has a valid root node.
- `Error pack(path: Node)` — Packs `path` and its subtree into this PackedScene.
- `SceneState get_state()` — Returns the underlying `SceneState`.
- `void replace_state(state: SceneState)` — Replaces the scene state (advanced).

## Signals
- (No signals of its own beyond those inherited from [[Resource]] such as `changed`.)

## Description
`PackedScene` is what a `.tscn` file becomes once loaded: a serialised description of a node tree that you can stamp out with `instantiate()`. Each call returns an independent `Node` (with its own subtree, signals, and properties) that you then `add_child` into the live tree. Because it is a [[Resource]], a `PackedScene` can be stored in a property, preloaded, or loaded at runtime and reused for as many instances as you need.

The `pack(node)` method goes the other way: it captures an existing subtree into a `PackedScene` (and you can then `ResourceSaver.save()` it to disk). This is the foundation of Godot's scene-based composition — a card scene, a player-area scene, a popup scene, each saved once and instanced many times.

In the Kadi app `PackedScene` is the engine behind dealing. The `card.tscn` scene is loaded once (its `PackedScene` resource is held by the game autoload), and for every card the dealer calls `card_scene.instantiate()`, configures the resulting node's suit/rank from the card's [[Resource]], and `add_child`s it to the active player's hand. The declaration popup from `l_08_01_declaration_popup` is likewise a `PackedScene` instanced on demand and freed after a suit is chosen. This pattern keeps memory predictable and code small.
