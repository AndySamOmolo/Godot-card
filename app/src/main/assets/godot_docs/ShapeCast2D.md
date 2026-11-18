---
class: ShapeCast2D
inherits: Node2D
verified: 4.3
short: Sweeps a 2D CollisionShape2D along a vector to detect wide or multi-object collisions.
---

## Properties
- `shape: Shape2D` — The 2D shape (`CircleShape2D`, `RectangleShape2D`) swept along `target_position`.
- `target_position: Vector2` — Local sweep vector.
- `max_results: int` — Maximum number of simultaneous colliders reported.

## Methods
- `bool is_colliding()` — Returns `true` if the swept shape hit any object.
- `int get_collision_count()` — Number of objects hit in the sweep.
- `Object get_collider(index: int)` — Returns collider at `index`.
- `void force_shapecast_update()` — Updates the sweep immediately.

## Signals
- (No custom signals beyond [[Node2D]].)

## Description
Where `[[RayCast2D]]` is infinitely thin, `ShapeCast2D` sweeps an entire `Shape2D` (such as a circle or capsule) forward. Use it for thick laser beams, melee cleave checks, or verifying whether a character can uncrouch without clipping into a low ceiling.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`ShapeCast2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `ShapeCast2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var sweep: ShapeCast2D = $ShapeCast2D

func get_all_hit_targets() -> Array[Object]:
	sweep.force_shapecast_update()
	var hits: Array[Object] = []
	for i in range(sweep.get_collision_count()):
		hits.append(sweep.get_collider(i))
	return hits
```
