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
