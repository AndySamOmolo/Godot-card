---
class: Resource
inherits: RefCounted
verified: 4.3
short: A reference-counted, shareable data object that can be saved to disk as a .tres file.
---

## Properties
- `resource_name: String` — A human-readable name for the resource.
- `resource_path: String` — The file path this resource was loaded from, or assigned via `take_over_path`.
- `resource_local_to_scene: bool` — When true, each scene instance gets its own copy of the resource.
- `script: Script` — The [[GDScript]] attached to this resource.

## Methods
- `Resource duplicate(subresources: bool = false)` — Returns a copy; pass `true` to also clone nested resources.
- `void _setup_local_to_scene()` — Virtual method called for `resource_local_to_scene` resources when a scene is instantiated.
- `void take_over_path(path: String)` — Registers this resource as the one at `path`, replacing any cached version.
- `void emit_changed()` — Emits the `changed` signal; call this after editing a custom resource so listeners refresh.

## Signals
- `changed()` — Emitted when the resource's content changes (you call `emit_changed` from your script).
- `setup_local_to_scene_requested()` — Emitted before `_setup_local_to_scene` is called.

## Description
`Resource` is Godot's standard container for shareable data. Like its parent [[RefCounted]], it cleans itself up automatically when nothing references it, so you never call `free()` on one. Resources can be saved to disk as `.tres` (text) or `.res` (binary) files and loaded back with [[ResourceLoader]] `load()` or the `preload()` statement, which makes them ideal for data-driven design.

Because resources are shared by default, assigning the same card data to several nodes costs nothing extra — they all point at one object. Set `resource_local_to_scene` to `true` when each instance needs its own mutable copy, and override `_setup_local_to_scene` to per-instance customisation.

In the Kadi app, every card is a custom resource — a `CardData` script extending `Resource` with `suit: Suit`, `rank: Rank`, and an `effect` enum. You build the 52-card deck once in `l_01_03_deck_builder`, store each card as a resource, and reference them from [[Array]]s that represent the draw pile and each player's hand. When the deck is reshuffled in `l_10_01_reshuffle` the same resource instances are simply reordered, never duplicated. Because [[Resource]] is reference-counted you can pass cards around freely without leaking memory.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Resource`)**: When configuring `Resource` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Resource` offline. If your `Resource` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
class_name CardData
extends Resource

@export var suit: int = 0
@export var rank: int = 4
@export var front_texture: Texture2D
```

