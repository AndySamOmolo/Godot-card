---
class: Camera2D
inherits: Node2D
verified: 4.3
short: A 2D camera that scrolls, zooms, and limits the visible region of the scene.
---

## Properties
- `offset: Vector2` — Offset applied on top of the camera's position.
- `zoom: Vector2` — Zoom factor; values above `Vector2(1, 1)` zoom in.
- `position_smoothing_enabled: bool` — When true, the camera eases toward its target instead of snapping.
- `position_smoothing_speed: float` — How quickly the camera reaches the target position.
- `rotation_smoothing_enabled: bool` — When true, rotation eases toward the node's rotation.
- `rotation_smoothing_speed: float` — How quickly rotation catches up.
- `process_callback: Camera2DProcessCallback` — Whether smoothing runs in `_process` or `_physics_process`.
- `enabled: bool` — When true this camera is the active one for its viewport.
- `limit_left: int`, `limit_right: int`, `limit_top: int`, `limit_bottom: int` — Pixel bounds the camera will not scroll past.
- `limit_smoothed: bool` — When true, hitting a limit is eased rather than snapped.
- `anchor_mode: Camera2DAnchorMode` — Whether the camera's anchor follows fixed drag or the centre.

## Methods
- `void make_current()` — Makes this the active camera for the current viewport.
- `void clear_current()` — Stops this camera being the active one.
- `bool is_current()` — True if this camera is currently active.
- `Vector2 get_camera_screen_center_position()` — The world position currently at the centre of the screen.
- `Vector2 get_camera_target_position()` — The position the camera is smoothing toward.
- `int get_camera_zoom()` — (Legacy helper) returns the zoom as an int-equivalent; prefer reading `zoom` directly.

## Signals
- (No signals of its own beyond those inherited from [[Node2D]].)

## Description
`Camera2D` defines which part of the 2D world is visible. As a [[Node2D]] it has a position, rotation, and scale you can animate just like any other node; the camera renders whatever is around that position, scaled by `zoom`. By default it snaps to the node's `position`, but enabling `position_smoothing_enabled` eases it toward the target — the standard way to make the camera follow a moving subject without jitter.

The `limit_*` properties clamp the camera inside the playfield so the player never sees the empty void beyond the table edge. `offset` nudges the framing (useful to push the camera slightly ahead of the active player), and `zoom` above 1 is a clean way to do a "Niko Kadi" close-up or a penalty reveal. Because a `Camera2D` is just a node, you can also tween its properties directly with [[Tween]].

In the Kadi app the camera stays mostly static — the table fits on screen — but a `Camera2D` zooms in briefly when a player calls [[Niko Kadi]] or when a big penalty card is played, to draw the eye. `position_smoothing_enabled` keeps these zooms gentle, and `limit_*` values match the table rect so the camera never drifts off the felt. At low player counts the same camera can pan between seats on each turn, easing with `position_smoothing_speed` tuned to feel cinematic.
