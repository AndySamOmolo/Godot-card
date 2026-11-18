---
class: NavigationRegion2D
inherits: Node2D
verified: 4.3
short: Defines a traversable 2D navigation mesh region that NavigationAgent2D can pathfind across.
---

## Properties
- `navigation_polygon: NavigationPolygon` — The baked walkable polygon resource.
- `enabled: bool` — Toggles whether this region is active in `NavigationServer2D`.
- `travel_cost: float` — Multiplier for pathfinding cost across this region (`1.0` default).

## Methods
- `void bake_navigation_polygon(on_thread: bool = true)` — Re-bakes the navigation polygon around child obstacles.

## Signals
- `bake_finished()` — Emitted when runtime navmesh baking completes.

## Description
`NavigationRegion2D` holds the `NavigationPolygon` that defines where AI characters can walk. You can bake it in the editor toolbar or re-bake it at runtime with `bake_navigation_polygon()` when doors open or walls are destroyed.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`NavigationRegion2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `NavigationRegion2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var nav_region: NavigationRegion2D = $NavigationRegion2D

func rebuild_navmesh() -> void:
	nav_region.bake_navigation_polygon(true)
```
