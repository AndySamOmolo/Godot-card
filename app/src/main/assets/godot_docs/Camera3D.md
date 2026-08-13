---
class: Camera3D
inherits: Node3D
verified: 4.3
short: A 3D camera that defines the perspective, field of view, and clip planes of a viewport.
---

## Properties
- `fov: float` — Field of view in degrees (perspective mode).
- `size: float` — The visible height in metres (orthographic mode).
- `near: float` — Distance to the near clip plane.
- `far: float` — Distance to the far clip plane.
- `keep_aspect: KeepAspect` — Whether to preserve width or height when the viewport resizes.
- `cull_mask: int` — Bitmask of which visual layers this camera renders.
- `projection: ProjectionType` — Perspective, orthographic, frustum, or a custom matrix.
- `current: bool` — True when this camera is the active one for its viewport.
- `h_offset: float` — Horizontal offset of the rendered image.
- `v_offset: float` — Vertical offset of the rendered image.
- `doppler_tracking: DopplerTracking` — Whether audio doppler follows the camera's motion.
- `attributes: CameraAttributes` — Exposure / auto-exposure settings resource.
- `environment: Environment` — World environment override used when this camera is active.

## Methods
- `Vector3 project_position(screen_point: Vector2, z_depth: float)` — Converts a 2D screen point to a 3D world position at the given depth.
- `Vector2 unproject_position(world_point: Vector3)` — Converts a 3D world point to a 2D screen coordinate.
- `Vector3 project_ray_normal(screen_point: Vector2)` — Returns the direction of a ray from the camera through `screen_point`.
- `Vector3 project_ray_origin(screen_point: Vector2)` — Returns the world-space origin of that ray.
- `Vector3 project_local_ray_normal(screen_point: Vector2)` — Like `project_ray_normal` but in camera-local space.
- `bool is_position_behind(world_point: Vector3)` — True if `world_point` is behind the camera.
- `void make_current()` — Makes this camera the active one for its viewport.
- `void clear_current()` — Stops this camera being active.
- `bool is_current()` — True if this camera is currently active.
- `RID get_camera_rid()` — Returns the renderer RID of the camera.
- `void set_frustum(offset: Vector2, z_near: float, z_far: float)` — Sets a frustum projection.

## Signals
- (No signals of its own beyond those inherited from [[Node3D]].)

## Description
`Camera3D` is the 3D counterpart of [[Camera2D]]. It is a [[Node3D]], so it has a `transform` you can position and rotate, and it defines how the 3D world is projected onto the 2D screen: a perspective `fov`, orthographic `size`, near/far clip planes, aspect handling, and a `cull_mask` that selects which visual layers are rendered. Only one `Camera3D` per viewport is `current` at a time.

The `project_*` / `unproject_position` family converts between screen pixels and world space, which is the backbone of 3D picking — clicking a card on a 3D table means unprojecting the mouse position into a ray and testing it against the card meshes. `cull_mask` lets you render a separate "UI in 3D" layer (the score popups, for instance) only on specific cameras.

The Kadi app is built in 2D, so `Camera3D` is shown here for completeness. Learners who extend the project into a 3D table — tilting cards in real space, dealing with depth — will swap the [[Camera2D]] for a `Camera3D` mounted above the table and use `project_ray_origin` + `project_ray_normal` to translate taps into card picks. The same animation concepts apply: tween `fov` for a dramatic zoom on a penalty, or tween `transform` to orbit the table between turns.
