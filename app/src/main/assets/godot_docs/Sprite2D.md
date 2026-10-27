---
class: Sprite2D
inherits: Node2D
verified: 4.3
short: A 2D node that draws a Texture2D, optionally split into frames or a region.
---

## Properties
- `texture: Texture2D` — The [[Texture2D]] to draw.
- `centered: bool` — When true, the texture is drawn centred on the node's origin.
- `offset: Vector2` — Pixel offset applied after centring.
- `flip_h: bool` — Flips the texture horizontally.
- `flip_v: bool` — Flips the texture vertically.
- `hframes: int` — Number of horizontal frames in a sprite sheet.
- `vframes: int` — Number of vertical frames in a sprite sheet.
- `frame: int` — The currently visible frame (0 to `hframes * vframes - 1`).
- `frame_coords: Vector2i` — The frame expressed as (column, row) coordinates.
- `region_enabled: bool` — When true, only `region_rect` of the texture is drawn.
- `region_rect: Rect2` — The sub-rectangle of the texture used when `region_enabled` is on.
- `texture_filter: CanvasItem.TextureFilter` — Inherited filtering override.
- `texture_repeat: CanvasItem.TextureRepeat` — Inherited repeat override.

## Methods
- `Rect2 get_rect()` — Returns the sprite's local rect, accounting for centring and frames.
- `bool is_pixel_opaque(position: Vector2)` — True if the pixel at `position` (local) is non-transparent.

## Signals
- `frame_changed()` — Emitted when `frame` changes.
- `texture_changed()` — Emitted when `texture` is reassigned.

## Description
`Sprite2D` is the simplest way to draw an image in 2D. Give it a [[Texture2D]], drop it in the tree, and it renders every frame, inheriting its [[Node2D]] transform so you can move, rotate, and scale it freely. The `hframes`/`vframes`/`frame` trio turns one sprite-sheet texture into many independent images — handy for card backs, suit icons, or a row of effect glyphs — without loading separate files.

For more complex needs, `region_enabled` plus `region_rect` lets you crop a sub-image on the fly, and `flip_h`/`flip_v` mirror it. Because `Sprite2D` is a [[CanvasItem]], it participates fully in z-ordering, modulation, and the `draw_*` pipeline of its parent. For UI that needs to receive clicks and lay out responsively, prefer a `TextureRect` (a [[Control]] subclass); for free-floating game art, `Sprite2D` is the lighter choice.

In the Kadi app a card can be drawn either as a `Sprite2D` (in a free-roaming table view) or a `TextureRect` (in the strict UI layout). With `Sprite2D`, the deck's artwork atlas is split with `hframes = 13` and `vframes = 5` (one row per suit plus a back), and each card node picks its face by setting `frame_coords = Vector2i(rank, suit)`. Flipping a card over is just `texture = card_back`. The penalty animation tweens `scale` and `modulate` for a satisfying pop when a draw-two lands.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Sprite2D`)**: When configuring `Sprite2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Sprite2D` offline. If your `Sprite2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Sprite2D in Godot 4.3 Kadi
func configure_sprite2d_example(target: Sprite2D) -> void:
    assert(target != null, "Sprite2D reference must be valid")
    print("Configured Sprite2D for Kadi table runtime")
```

