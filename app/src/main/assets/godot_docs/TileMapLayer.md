---
class: TileMapLayer
inherits: Node2D
verified: 4.3
short: Node for rendering 2D tile-based maps from a shared TileSet resource (replaces legacy TileMap layers in Godot 4.3).
---

## Properties
- `tile_set: TileSet` — The shared `TileSet` resource containing tile atlases, physics, and custom data layers.
- `y_sort_enabled: bool` — Sorts tiles and child nodes by Y coordinate for top-down depth.
- `collision_enabled: bool` — Enables physics collision shapes painted on tiles.

## Methods
- `Vector2i local_to_map(local_position: Vector2)` — Converts local pixel coordinates into integer `(x, y)` grid coordinates.
- `Vector2 map_to_local(map_position: Vector2i)` — Returns the centered local pixel position of a grid cell.
- `TileData get_cell_tile_data(coords: Vector2i)` — Returns the `TileData` for the cell (used to read custom metadata via `get_custom_data()`).
- `void set_cell(coords: Vector2i, source_id: int = -1, atlas_coords: Vector2i = Vector2i(-1, -1))` — Places or erases a tile at `coords`.

## Signals
- `changed()` — Emitted when the layer's tile data or settings change.

## Description
In Godot 4.3, `TileMapLayer` replaces the old multi-layer `TileMap` node. Each layer of your 2D game world (`GroundLayer`, `WallsLayer`, `CanopyLayer`) is an independent `TileMapLayer` node in the SceneTree sharing a `[[TileSet]]` resource.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`TileMapLayer`)**: Press **F1** inside the Godot 4.3 editor or tap any `TileMapLayer` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var ground: TileMapLayer = $GroundLayer

func get_surface_name(world_pos: Vector2) -> String:
	var cell: Vector2i = ground.local_to_map(ground.to_local(world_pos))
	var data: TileData = ground.get_cell_tile_data(cell)
	return str(data.get_custom_data("surface")) if data != null else "none" 
```
