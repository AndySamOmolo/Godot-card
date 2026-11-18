---
class: Vector3
inherits: Variant
verified: 4.3
short: 3D floating-point vector (x, y, z) used for 3D positions, velocities, directions, and Euler angles.
---

## Properties
- `x: float` — X component (`+X` is Right).
- `y: float` — Y component (`+Y` is Up).
- `z: float` — Z component (`-Z` is Forward in Godot 3D).

## Methods
- `Vector3 normalized()` — Returns a unit-length vector pointing in the same direction.
- `float distance_to(to: Vector3)` — Returns the 3D Euclidean distance between two points.
- `Vector3 direction_to(to: Vector3)` — Returns the normalized direction vector toward `to`.
- `float dot(with: Vector3)` — Returns the dot product (`cos(angle)` for unit vectors).
- `Vector3 cross(with: Vector3)` — Returns the perpendicular 3D cross product vector.

## Signals
- (Value type — no signals.)

## Description
`Vector3` is the 3D counterpart of `[[Vector2]]`. Remember that in Godot's right-handed Y-Up 3D coordinate system, `Vector3.UP` is `(0, 1, 0)` and `Vector3.FORWARD` is `(0, 0, -1)`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`Vector3`)**: Press **F1** inside the Godot 4.3 editor or tap any `Vector3` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func horizontal_direction(from_pos: Vector3, to_pos: Vector3) -> Vector3:
	var delta := to_pos - from_pos
	delta.y = 0.0
	return delta.normalized()
```
