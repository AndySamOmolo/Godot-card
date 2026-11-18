---
class: CharacterBody3D
inherits: Node3D
verified: 4.3
short: Script-driven 3D physics body with move_and_slide() collision response for 3D players and AI.
---

## Properties
- `velocity: Vector3` — Linear velocity in meters per second (`m/s`).
- `up_direction: Vector3` — Up vector used to distinguish floors from walls (`Vector3.UP` default).
- `floor_max_angle: float` — Maximum walkable slope angle in radians.

## Methods
- `bool move_and_slide()` — Moves the body along `velocity` (applying `delta` automatically) and slides along slopes/walls.
- `bool is_on_floor()` — Returns `true` if the body touched a floor on the last `move_and_slide()`.

## Signals
- (Inherits mouse picking signals from `CollisionObject3D`.)

## Description
`CharacterBody3D` is the exact 3D counterpart of `[[CharacterBody2D]]`. Remember that in 3D, `+Y` is Up (`Vector3.UP`), so downward gravity subtracts from `velocity.y` (`velocity.y -= gravity * delta`).

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`CharacterBody3D`)**: Press **F1** inside the Godot 4.3 editor or tap any `CharacterBody3D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
extends CharacterBody3D

func _physics_process(delta: float) -> void:
	if not is_on_floor():
		velocity.y -= 18.0 * delta
	move_and_slide()
```
