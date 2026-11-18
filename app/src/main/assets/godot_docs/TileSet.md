---
class: TileSet
inherits: Resource
verified: 4.3
short: Resource library of tiles, atlas textures, physics polygons, navigation meshes, and custom metadata for TileMapLayer.
---

## Properties
- `tile_size: Vector2i` — Dimensions of a single grid cell in pixels (default `Vector2i(16, 16)`).

## Methods
- `int get_custom_data_layers_count()` — Returns the number of custom metadata layers.
- `String get_custom_data_layer_name(layer_index: int)` — Returns the name of a custom data layer.

## Signals
- `changed()` — Emitted when tile definitions change.

## Description
`TileSet` is a `[[Resource]]` shared by one or more `[[TileMapLayer]]` nodes. It slices sprite sheets into `TileSetAtlasSource` grids and stores per-tile collision shapes, navigation polygons, and custom data layers (like movement cost or footstep sound tags).

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`TileSet`)**: Press **F1** inside the Godot 4.3 editor or tap any `TileSet` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func print_tile_grid_size(tile_set: TileSet) -> void:
	print("Grid cell size: ", tile_set.tile_size)
```
