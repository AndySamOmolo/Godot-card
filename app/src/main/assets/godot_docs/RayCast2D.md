---
class: RayCast2D
inherits: Node2D
verified: 4.3
short: Casts a 2D line segment to detect the closest intersecting physics body or Area2D.
---

## Properties
- `target_position: Vector2` — The ray's destination offset relative to this node's local origin.
- `collision_mask: int` — Physics layers scanned by the ray.
- `collide_with_areas: bool` — When `true`, detects `Area2D` nodes as well as physics bodies.
- `enabled: bool` — Whether the ray queries collisions each physics frame.

## Methods
- `bool is_colliding()` — Returns `true` if any object intersected the ray.
- `Object get_collider()` — Returns the first `Object` hit by the ray.
- `Vector2 get_collision_point()` — Returns the world-space intersection point.
- `void force_raycast_update()` — Recalculates collisions immediately in the current frame.

## Signals
- (No custom signals beyond [[Node2D]].)

## Description
`RayCast2D` is the workhorse for enemy line-of-sight checks, platformer ledge sensors, laser sights, and ground slope checks. Because `target_position` is in local space, always convert world targets via `to_local(target.global_position)`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`RayCast2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `RayCast2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var ray: RayCast2D = $RayCast2D

func can_see_player(player: Node2D) -> bool:
	ray.target_position = ray.to_local(player.global_position)
	ray.force_raycast_update()
	return ray.is_colliding() and ray.get_collider() == player
```
