---
class: CharacterBody2D
inherits: PhysicsBody2D
verified: 4.3
short: A physics body specialized for player- or code-controlled movement with built-in collision sliding.
---

## Properties
- `velocity: Vector2` — The body's current velocity, read and written each frame before `move_and_slide`.
- `motion_mode: MotionMode` — `MOTION_MODE_GROUNDED` for platformers or `MOTION_MODE_FLOATING` for top-down.
- `up_direction: Vector2` — The direction treated as "up" when classifying floors and walls.
- `slide_on_ceiling: bool` — When true, the body slides along ceilings instead of stopping.
- `floor_stop_on_slope: bool` — When true, the body does not slide down slopes while idle.
- `floor_max_angle: float` — Maximum slope angle (radians) still considered a floor.
- `floor_snap_length: float` — Distance the body snaps down to stay attached to the floor.
- `max_slides: int` — Maximum number of collision-and-slide iterations per move.

## Methods
- `bool move_and_slide()` — Moves using `velocity`, sliding along collisions; returns true if it collided.
- `bool is_on_floor()` — True if the last `move_and_slide` ended on a floor.
- `bool is_on_wall()` — True if the last move ended touching a wall.
- `bool is_on_ceiling()` — True if the last move ended touching a ceiling.
- `Vector2 get_floor_normal()` — Returns the normal of the floor last collided with.
- `Vector2 get_real_velocity()` — Returns the actual velocity after the last slide.
- `int get_slide_collision_count()` — Number of collisions during the last `move_and_slide`.
- `KinematicCollision2D get_slide_collision(slide_idx: int)` — Returns details of a specific slide collision.
- `KinematicCollision2D get_last_slide_collision()` — Returns the last slide collision, or null.

## Description
`CharacterBody2D` is the body you use when you want to move something precisely by code but still respect collisions. You set `velocity` each frame and call `move_and_slide`, which moves the body, resolves collisions, and slides it neatly along walls and slopes instead of stopping dead. The `is_on_floor` / `is_on_wall` helpers make ground checks and jumping trivial.

Unlike a [[StaticBody2D]] it moves, and unlike a `RigidBody2D` it is not tossed around by forces — its motion is entirely up to you. Choose `MOTION_MODE_FLOATING` for top-down games where there is no gravity direction, or `MOTION_MODE_GROUNDED` for side-view platforming.

In the Kadi app most cards are laid out by tweens, but `CharacterBody2D` powers the free-moving table cursor and the celebratory tokens that scatter when someone wins: each token sets a `velocity`, calls `move_and_slide`, and bounces off the table edges built from [[StaticBody2D]] walls.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`CharacterBody2D`)**: When configuring `CharacterBody2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `CharacterBody2D` offline. If your `CharacterBody2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using CharacterBody2D in Godot 4.3 Kadi
func configure_characterbody2d_example(target: CharacterBody2D) -> void:
    assert(target != null, "CharacterBody2D reference must be valid")
    print("Configured CharacterBody2D for Kadi table runtime")
```

