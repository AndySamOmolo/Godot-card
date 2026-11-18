---
class: AStarGrid2D
inherits: RefCounted
verified: 4.3
short: Fast built-in 2D grid A* pathfinder for tile-based tactics, puzzle boards, and roguelikes.
---

## Properties
- `region: Rect2i` — The integer grid bounds (e.g. `Rect2i(0, 0, 16, 16)`).
- `cell_size: Vector2` — Pixel size of each grid cell.
- `diagonal_mode: DiagonalMode` — Controls whether diagonal steps are allowed (`DIAGONAL_MODE_NEVER`, `DIAGONAL_MODE_ALWAYS`).

## Methods
- `void update()` — Allocates internal grid data after changing `region` or settings.
- `void set_point_solid(id: Vector2i, solid: bool = true)` — Marks a grid cell as a wall (`true`) or walkable (`false`).
- `Array[Vector2i] get_id_path(from_id: Vector2i, to_id: Vector2i)` — Returns the shortest path of grid cells.

## Signals
- (Inherits [[RefCounted]] — no signals.)

## Description
`AStarGrid2D` computes shortest paths on a 2D grid in microseconds without needing physics or `NavigationRegion2D` nodes. It is the ideal choice for turn-based tactics, roguelikes, and board games.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`AStarGrid2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `AStarGrid2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func solve_grid_path(start: Vector2i, goal: Vector2i) -> Array[Vector2i]:
	var grid := AStarGrid2D.new()
	grid.region = Rect2i(0, 0, 8, 8)
	grid.diagonal_mode = AStarGrid2D.DIAGONAL_MODE_NEVER
	grid.update()
	return grid.get_id_path(start, goal)
```
