---
class: Basis
inherits: Variant
verified: 4.3
short: 3x3 orthogonal matrix representing 3D rotation and scale (the orientation part of Transform3D).
---

## Properties
- `x: Vector3` — Local Right axis vector.
- `y: Vector3` — Local Up axis vector.
- `z: Vector3` — Local Back axis vector (`-z` is local Forward).

## Methods
- `Basis rotated(axis: Vector3, angle: float)` — Returns the basis rotated around `axis` by `angle` radians.
- `Basis orthonormalized()` — Re-orthogonalizes and normalizes the axes to eliminate floating-point drift.
- `Quaternion get_rotation_quaternion()` — Converts the orientation to a unit `Quaternion`.

## Signals
- (Value type — no signals.)

## Description
Stored inside `[[Transform3D]].basis`, `Basis` defines which way a `[[Node3D]]` is facing. Multiplying `transform.basis * local_vec` transforms a local direction into world space, and `-global_transform.basis.z` always points along the node's forward nose!

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`Basis`)**: Press **F1** inside the Godot 4.3 editor or tap any `Basis` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func move_along_heading(node: Node3D, speed: float, delta: float) -> void:
	var forward: Vector3 = -node.global_transform.basis.z
	node.global_position += forward * speed * delta
```
