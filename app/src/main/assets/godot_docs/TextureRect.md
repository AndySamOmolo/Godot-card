---
class: TextureRect
inherits: Control
verified: 4.3
short: A Control that draws a Texture2D, with stretching and flipping options.
---

## Properties
- `texture: Texture2D` — The [[Texture2D]] to draw.
- `expand_mode: ExpandMode` — How the texture behaves when the rect is larger than the image (ignore size, fit width, fit width/proportional).
- `stretch_mode: StretchMode` — Scale, tile, keep, keep-centered, keep-aspect, or keep-aspect-centered.
- `flip_h: bool` — Flips the texture horizontally.
- `flip_v: bool` — Flips the texture vertically.
- `texture_filter: CanvasItem.TextureFilter` — Inherited filtering override.
- `texture_repeat: CanvasItem.TextureRepeat` — Inherited repeat override.
- `draw_center: bool` — When true, the texture is drawn; when false, only the border (for nine-patch) is drawn.
- `patch_margin_left: int`, `patch_margin_right: int`, `patch_margin_top: int`, `patch_margin_bottom: int` — Nine-patch margins.

## Methods
- (No public methods beyond those inherited from [[Control]].)

## Signals
- `texture_changed()` — Emitted when `texture` is reassigned.

## Description
`TextureRect` is the UI-friendly way to display an image. Unlike [[Sprite2D]] (a [[Node2D]]), `TextureRect` is a [[Control]], so it lays out inside containers and can receive `gui_input`. The `stretch_mode` decides how the image fills its rect — `STRETCH_KEEP` keeps native size, `STRETCH_KEEP_ASPECT_CENTERED` letterboxes, and `STRETCH_SCALE` (the default) fills the rect. `expand_mode` controls whether the rect grows to fit the image or vice-versa.

The nine-patch margins (`patch_margin_*`) let a single texture act as a stretchable border panel whose corners stay crisp while the middle scales — perfect for buttons, frames, and the card-back border that should not distort when a card is resized for accessibility. Flip flags mirror the art without editing the source, useful for left/right facing icons.

In the Kadi app a card in a player's hand is a `TextureRect`: it lays out cleanly inside the hand [[HBoxContainer]] and still receives clicks. Each card's `texture` is swapped between its face and the shared card-back `Texture2D` to flip it. The suit icons next to the penalty label are small `TextureRect`s, and the declaration popup's suit buttons show their glyph via a `TextureRect` child. `STRETCH_KEEP_ASPECT_CENTERED` keeps every card art undistorted regardless of the hand's slot size.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`TextureRect`)**: When configuring `TextureRect` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `TextureRect` offline. If your `TextureRect` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
@onready var card_art: TextureRect = $CardArt

func apply_downloaded_card(data: CardData) -> void:
    card_art.expand_mode = TextureRect.EXPAND_IGNORE_SIZE
    card_art.stretch_mode = TextureRect.STRETCH_KEEP_ASPECT_CENTERED
    card_art.mouse_filter = Control.MOUSE_FILTER_IGNORE
    var tex: Texture2D = data.resolve_downloaded_texture()
    card_art.texture = tex
    card_art.visible = (tex != null)
```

