---
class: Viewport
inherits: Node
verified: 4.3
short: A node that renders its child scene, handling its own drawing, input, and camera.
---

## Properties
- `world_2d: World2D` — The 2D world (physics and canvas space) used by this viewport.
- `canvas_transform: Transform2D` — Transform applied to all canvas items drawn in this viewport.
- `global_canvas_transform: Transform2D` — The global canvas transform, combining camera and viewport.
- `gui_disable_input: bool` — When true, the viewport ignores GUI input events.
- `physics_object_picking: bool` — When true, mouse/touch events are checked against 2D/3D physics objects.
- `transparent_bg: bool` — When true, the viewport clears to a transparent background.
- `msaa_2d: MSAA` — Multisample anti-aliasing level for 2D rendering.
- `snap_2d_transforms_to_pixel: bool` — When true, 2D transforms are snapped to whole pixels.

## Methods
- `Vector2 get_mouse_position()` — Returns the mouse position within this viewport.
- `Rect2 get_visible_rect()` — Returns the visible rectangle of the viewport.
- `Control gui_get_focus_owner()` — Returns the [[Control]] that currently has keyboard focus, if any.
- `Camera2D get_camera_2d()` — Returns the active [[Camera2D]] for this viewport, if any.
- `Texture2D get_texture()` — Returns the viewport's rendered contents as a texture.
- `World2D get_world_2d()` — Returns the viewport's [[World2D]].
- `void set_input_as_handled()` — Marks the current input event as handled, stopping propagation.
- `void push_input(event: InputEvent, in_local_coords: bool = false)` — Feeds an input event into this viewport.

## Signals
- `size_changed()` — Emitted when the viewport's size changes.
- `gui_focus_changed(node: Control)` — Emitted when GUI focus moves to a different [[Control]].

## Description
`Viewport` is the [[Node]] responsible for actually rendering a slice of the scene and routing input into it. The main window is itself a viewport (see [[Window]]), and you can nest a `SubViewport` to render a scene into a texture — a mini-map, a portal, or an off-screen buffer read back with `get_texture`.

Each viewport owns a [[World2D]], a canvas transform, and its own input handling, so it decides which [[Camera2D]] is active and how coordinates map to the screen. `physics_object_picking` connects pointer events to physics objects, which underpins clickable 2D scenes.

In the Kadi app the root `Viewport` handles all card input and hosts the table's [[Camera2D]]; `get_mouse_position` converts a touch into table space so the correct card is picked up. A `SubViewport` also renders a live preview of the top discard card into the rules-help panel using `get_texture`.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Viewport`)**: When configuring `Viewport` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Viewport` offline. If your `Viewport` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Viewport in Godot 4.3 Kadi
func configure_viewport_example(target: Viewport) -> void:
    assert(target != null, "Viewport reference must be valid")
    print("Configured Viewport for Kadi table runtime")
```

