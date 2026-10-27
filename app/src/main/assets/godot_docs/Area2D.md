---
class: Area2D
inherits: CollisionObject2D
verified: 4.3
short: A 2D region that detects overlapping bodies and areas and can override physics like gravity.
---

## Properties
- `monitoring: bool` — When true, the area detects other bodies and areas.
- `monitorable: bool` — When true, other areas can detect this one.
- `priority: int` — Processing order among overlapping areas (higher wins for overrides).
- `gravity_space_override: SpaceOverride` — How this area combines its gravity with the default space.
- `gravity: float` — Gravity strength applied to bodies inside the area.
- `gravity_point: bool` — When true, gravity pulls toward `gravity_point_center` instead of a direction.
- `linear_damp: float` — Extra linear damping applied to bodies inside.
- `angular_damp: float` — Extra angular damping applied to bodies inside.

## Methods
- `Array[Node2D] get_overlapping_bodies()` — Returns the physics bodies currently overlapping.
- `Array[Area2D] get_overlapping_areas()` — Returns the areas currently overlapping.
- `bool overlaps_body(body: Node)` — True if `body` is currently overlapping.
- `bool overlaps_area(area: Node)` — True if `area` is currently overlapping.
- `bool has_overlapping_bodies()` — True if any body is overlapping.
- `bool has_overlapping_areas()` — True if any area is overlapping.

## Signals
- `body_entered(body: Node2D)` — Emitted when a physics body enters the area.
- `body_exited(body: Node2D)` — Emitted when a physics body leaves the area.
- `area_entered(area: Area2D)` — Emitted when another area enters this one.
- `area_exited(area: Area2D)` — Emitted when another area leaves this one.
- `body_shape_entered(body_rid: RID, body: Node2D, body_shape_index: int, local_shape_index: int)` — Emitted per-shape when a body enters.

## Description
`Area2D` is a [[Node2D]]-based sensor: it does not push things around, it reports when [[CollisionShape2D]]-shaped regions overlap. Give it one or more collision shapes and it fires `body_entered` / `area_entered` (and their exit counterparts) as other objects move in and out. It can also softly influence physics inside its bounds by overriding gravity and damping.

Detection is gated by physics layers and masks and by the `monitoring` / `monitorable` flags. For polling instead of signals, `get_overlapping_bodies` and `overlaps_area` report the current state. Because it derives from `CollisionObject2D`, it shares the same collision-layer machinery as bodies.

In the Kadi app `Area2D` marks the interactive zones of the table: the central discard pile is an `Area2D`, and when a dragged card's own area overlaps it, `area_entered` highlights the pile as a valid drop target. Each player's hand slot is an `Area2D` too, so releasing a card over the wrong zone triggers `area_exited` and snaps the card back.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Area2D`)**: When configuring `Area2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Area2D` offline. If your `Area2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Area2D in Godot 4.3 Kadi
func configure_area2d_example(target: Area2D) -> void:
    assert(target != null, "Area2D reference must be valid")
    print("Configured Area2D for Kadi table runtime")
```

