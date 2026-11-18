---
class: StyleBoxFlat
inherits: Resource
verified: 4.3
short: Procedural vector UI styling resource with rounded corners, borders, and drop shadows for Control nodes.
---

## Properties
- `bg_color: Color` — Fill color of the panel or button.
- `border_color: Color` — Color of the stroke border.
- `shadow_size: int` — Blur radius of the drop shadow in pixels.

## Methods
- `void set_corner_radius_all(radius: int)` — Sets all 4 corner radii at once.
- `void set_border_width_all(width: int)` — Sets all 4 border widths at once.

## Signals
- `changed()` — Emitted when any visual property changes.

## Description
`StyleBoxFlat` lets you skin `[[Panel]]`, `[[Button]]`, and `[[ProgressBar]]` nodes with crisp rounded corners, gold selection borders, and soft shadows without needing external PNG textures.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`StyleBoxFlat`)**: Press **F1** inside the Godot 4.3 editor or tap any `StyleBoxFlat` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func make_selected_card_style() -> StyleBoxFlat:
	var box := StyleBoxFlat.new()
	box.bg_color = Color("#182638")
	box.border_color = Color("#FACC15")
	box.set_border_width_all(2)
	box.set_corner_radius_all(10)
	return box
```
