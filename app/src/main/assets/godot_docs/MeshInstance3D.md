---
class: MeshInstance3D
inherits: Node3D
verified: 4.3
short: 3D node that renders a Mesh resource (BoxMesh, QuadMesh, SphereMesh, or imported .glb geometry).
---

## Properties
- `mesh: Mesh` — The 3D geometry resource rendered by this node.
- `material_override: Material` — Optional `StandardMaterial3D` or `ShaderMaterial` overriding all surfaces.
- `cast_shadow: ShadowCastingSetting` — Controls whether this mesh casts 3D shadows.

## Methods
- `void set_surface_override_material(surface: int, material: Material)` — Overrides the material for a specific surface slot.

## Signals
- (No custom signals beyond [[Node3D]].)

## Description
`MeshInstance3D` is the 3D counterpart of `[[Sprite2D]]`. In a 3D Kadi tabletop (`Table3D.tscn`), each card is a `MeshInstance3D` with a `QuadMesh` and a `StandardMaterial3D` displaying `CardData.resolve_downloaded_texture()`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`MeshInstance3D`)**: Press **F1** inside the Godot 4.3 editor or tap any `MeshInstance3D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var card_mesh: MeshInstance3D = $MeshInstance3D

func set_card_albedo(tex: Texture2D) -> void:
	var mat := StandardMaterial3D.new()
	mat.albedo_texture = tex
	card_mesh.material_override = mat
```
