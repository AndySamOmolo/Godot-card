---
class: CanvasLayer
inherits: Node
verified: 4.3
short: A node that draws its children on an independent 2D rendering layer with its own transform.
---

## Properties
- `layer: int` — Draw order of this layer; higher layers render on top of lower ones.
- `visible: bool` — When false, the whole layer and its children are hidden.
- `offset: Vector2` — Translation applied to everything drawn on the layer.
- `rotation: float` — Rotation in radians applied to the layer.
- `scale: Vector2` — Scale applied to the layer.
- `transform: Transform2D` — The combined transform built from `offset`, `rotation`, and `scale`.
- `follow_viewport_enabled: bool` — When true, the layer follows the viewport, useful for parallax.
- `custom_viewport: Node` — Renders this layer into a specific [[Viewport]] instead of the default one.

## Methods
- `void show()` — Makes the layer visible.
- `void hide()` — Hides the layer.
- `Transform2D get_final_transform()` — Returns the transform actually used for rendering, including viewport scaling.
- `void set_offset(offset: Vector2)` — Sets the layer `offset`.
- `Vector2 get_offset()` — Returns the layer `offset`.
- `void set_layer(layer: int)` — Sets the draw-order `layer`.

## Signals
- `visibility_changed()` — Emitted when the layer's `visible` state changes.

## Description
`CanvasLayer` is a [[Node]] that establishes a separate 2D drawing layer, independent of the camera and of other layers. Any [[CanvasItem]] placed beneath it (for example a [[Control]] HUD or a [[Sprite2D]]) is rendered using the layer's own `transform` and `layer` order, so it can float above or behind the rest of the scene regardless of world position.

Because layers are drawn strictly by `layer` number, `CanvasLayer` is the standard way to build heads-up displays, pause menus, and backgrounds that must ignore the [[Camera2D]]. A layer with `follow_viewport_enabled` can also scroll at its own rate to create simple parallax.

In the Kadi app a `CanvasLayer` at a high `layer` holds the persistent HUD — the score, the current-color indicator, and the "your turn" banner — so it stays fixed on screen while cards below pan and zoom with the table camera. A separate low-`layer` `CanvasLayer` renders the felt-table background art.
