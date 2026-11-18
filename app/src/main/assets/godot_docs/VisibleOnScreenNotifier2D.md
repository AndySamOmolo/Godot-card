---
class: VisibleOnScreenNotifier2D
inherits: Node2D
verified: 4.3
short: Detects when its 2D bounding rectangle enters or exits the active camera viewport.
---

## Properties
- `rect: Rect2` — Local bounding box tested against the screen viewport.

## Methods
- `bool is_on_screen()` — Returns `true` if `rect` is currently visible inside any `Viewport`.

## Signals
- `screen_entered()` — Emitted when the node becomes visible on screen.
- `screen_exited()` — Emitted when the node leaves the screen.

## Description
Attach a `VisibleOnScreenNotifier2D` to projectiles (`LaserBullet.tscn`) or flying cards and connect `screen_exited` to `queue_free()` so off-screen bullets clean themselves up automatically!

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`VisibleOnScreenNotifier2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `VisibleOnScreenNotifier2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func _ready() -> void:
	$VisibleOnScreenNotifier2D.screen_exited.connect(queue_free)
```
