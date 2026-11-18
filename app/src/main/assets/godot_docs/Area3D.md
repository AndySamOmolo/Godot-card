---
class: Area3D
inherits: Node3D
verified: 4.3
short: 3D trigger volume and ray-pickable region that detects overlapping 3D bodies, areas, and mouse clicks.
---

## Properties
- `monitoring: bool` — When `true`, emits signals when bodies or areas enter/exit.
- `input_ray_pickable: bool` — When `true`, receives mouse hover and click events from `Camera3D`.

## Methods
- `Array[Node3D] get_overlapping_bodies()` — Returns all physics bodies currently inside the `Area3D`.
- `Array[Area3D] get_overlapping_areas()` — Returns all `Area3D`s currently overlapping.

## Signals
- `body_entered(body: Node3D)` — Emitted when a 3D physics body enters.
- `area_entered(area: Area3D)` — Emitted when another `Area3D` enters.
- `mouse_entered()` — Emitted when the mouse cursor hovers over this 3D shape.
- `input_event(camera: Node, event: InputEvent, event_position: Vector3, normal: Vector3, shape_idx: int)` — Emitted on mouse click/drag over the 3D shape.

## Description
`Area3D` is the 3D counterpart of `[[Area2D]]`. In a 3D card or board game, wrapping each 3D card mesh in an `Area3D` + `CollisionShape3D` gives you `mouse_entered` hover lift and `input_event` click selection automatically!

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`Area3D`)**: Press **F1** inside the Godot 4.3 editor or tap any `Area3D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
extends Area3D

func _ready() -> void:
	mouse_entered.connect(func() -> void: position.y = 0.1)
	mouse_exited.connect(func() -> void: position.y = 0.0)
```
