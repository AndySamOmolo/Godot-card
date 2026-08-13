---
class: CanvasItem
inherits: Node
verified: 4.3
short: Base class for anything 2D that can be drawn to the screen, such as Node2D and Control.
---

## Properties
- `visible: bool` — Whether the item is drawn and receives input.
- `modulate: Color` — Tints the item and all its children.
- `self_modulate: Color` — Tints only this item, not its children.
- `z_index: int` — Draw order; higher values are drawn on top.
- `z_as_relative: bool` — When true, the effective z-index is the parent's plus this one's.
- `y_sort_enabled: bool` — When true, siblings are sorted by their Y position.
- `texture_filter: CanvasItem.TextureFilter` — Default filtering for child textures.
- `texture_repeat: CanvasItem.TextureRepeat` — Default repeat mode for child textures.
- `clip_children: bool` — When true, children are clipped to this item's rect.
- `light_mask: int` — Bitmask controlling which [[Light2D]]s affect this item.

## Methods
- `void queue_redraw()` — Schedules a `_draw` call for the next frame.
- `void draw_line(from: Vector2, to: Vector2, color: Color, width: float = -1.0, antialiased: bool = false)` — Draws a straight line in local space.
- `void draw_rect(rect: Rect2, color: Color, filled: bool = true, width: float = -1.0)` — Draws a rectangle.
- `void draw_circle(position: Vector2, radius: float, color: Color)` — Draws a filled circle.
- `void draw_texture(texture: Texture2D, position: Vector2, modulate: Color = Color(1,1,1,1))` — Draws a texture at a position.
- `void draw_string(font: Font, pos: Vector2, text: String, alignment: HorizontalAlignment = 0, width: float = -1.0, font_size: int = 16, modulate: Color = Color(1,1,1,1))` — Draws text with a font.
- `Transform2D get_global_transform()` — Returns the global transform matrix.
- `Transform2D get_global_transform_with_canvas()` — Returns the transform relative to the canvas.
- `RID get_canvas()` — Returns the RID of the canvas this item is on.
- `void _draw()` — Virtual method called when the item should redraw itself.

## Signals
- `draw()` — Emitted right before `_draw` is called.
- `visibility_changed()` — Emitted when `visible` changes.
- `hidden()` — Emitted when the item becomes hidden (deprecated in favour of `visibility_changed`).

## Description
`CanvasItem` is the common parent of [[Node2D]] and [[Control]], giving both 2D coordinate handling, modulation, z-ordering, and a `draw_*` API for custom rendering. Any node that wants to be visible in 2D — sprites, panels, cards, the whole table — is ultimately a `CanvasItem`. Setting `visible = false` removes it from drawing and input without removing it from the tree.

The `draw_*` family lets you render procedural shapes and text by overriding `_draw()` and calling the helpers, then calling `queue_redraw()` whenever the picture should change. Z-indexing controls overlap: cards drawn later or with a higher `z_index` appear on top of others, which matters when fans of cards overlap.

In the Kadi app the playing area is a `CanvasItem` subtree. Each card node's `modulate` is tinted red briefly when a penalty lands, and `self_modulate` is used to dim a card that isn't currently playable. `z_index` rises on the card under the cursor so it lifts out of the fan. The discard pile's top card calls `queue_redraw` after each play so the `draw_texture` of the new top card is repainted, and a `draw_string` shows the active penalty total floating above it.
