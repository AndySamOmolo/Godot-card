---
class: Marker2D
inherits: Node2D
verified: 4.3
short: A simple 2D point node used to mark positions in a scene.
---

## Properties
- `gizmo_extents: float` — Size of the cross gizmo drawn in the editor to visualize the marker.

## Methods
- `void set_gizmo_extents(extents: float)` — Sets the editor gizmo size.
- `float get_gizmo_extents()` — Returns the editor gizmo size.

## Description
`Marker2D` is a bare [[Node2D]] that exists purely to mark a location. It draws a small cross in the editor (sized by `gizmo_extents`) so you can see and place it, but it is invisible at runtime and has no logic of its own. It is the 2D equivalent of dropping a labeled pin on the scene.

Because it is a `Node2D`, it carries a full transform, so scripts can read its `global_position` to know where something should go. This keeps hard-coded coordinates out of code: designers move the marker in the editor and the game follows.

In the Kadi app `Marker2D` nodes define every meaningful spot on the table: the draw-pile origin, the discard-pile center, and each player's hand anchor are all markers. Dealing animations tween a card from the draw marker's `global_position` to a hand marker, and the reshuffle uses the discard marker as the pivot — so tweaking the table layout never requires touching a script.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Marker2D`)**: When configuring `Marker2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Marker2D` offline. If your `Marker2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Marker2D in Godot 4.3 Kadi
func configure_marker2d_example(target: Marker2D) -> void:
    assert(target != null, "Marker2D reference must be valid")
    print("Configured Marker2D for Kadi table runtime")
```

