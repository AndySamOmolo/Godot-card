---
class: ShaderMaterial
inherits: Resource
verified: 4.3
short: Material resource that runs a custom .gdshader program on 2D CanvasItems or 3D meshes.
---

## Properties
- `shader: Shader` — The `.gdshader` resource (`shader_type canvas_item;` or `shader_type spatial;`).

## Methods
- `void set_shader_parameter(param: StringName, value: Variant)` — Updates a `uniform` variable in the shader at runtime.
- `Variant get_shader_parameter(param: StringName)` — Reads the current value of a shader uniform.

## Signals
- `changed()` — Emitted when the shader or its parameters change.

## Description
`ShaderMaterial` connects your GDScript code to custom GPU shaders (like holographic foil Jokers, white hit-flashes on enemies, or water ripples) via `set_shader_parameter("is_foil", true)`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`ShaderMaterial`)**: Press **F1** inside the Godot 4.3 editor or tap any `ShaderMaterial` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func set_card_foil_sheen(card_art: TextureRect, active: bool) -> void:
	var mat := card_art.material as ShaderMaterial
	if mat != null:
		mat.set_shader_parameter("is_foil", active)
```
