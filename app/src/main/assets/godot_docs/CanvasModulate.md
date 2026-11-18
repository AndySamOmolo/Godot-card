---
class: CanvasModulate
inherits: Node2D
verified: 4.3
short: Tints the entire 2D canvas layer with a multiplicative ambient Color (for night, caves, or mood lighting).
---

## Properties
- `color: Color` — Multiplicative ambient color applied to all `CanvasItem`s on the same `CanvasLayer`.

## Methods
- (Uses standard [[Node2D]] methods.)

## Signals
- (No custom signals beyond [[Node2D]].)

## Description
`CanvasModulate` darkens or tints the entire 2D game world in one node so `[[PointLight2D]]` nodes stand out dramatically. Because `HUD` lives in its own `[[CanvasLayer]]`, your UI buttons stay crisp and unaffected!

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`CanvasModulate`)**: Press **F1** inside the Godot 4.3 editor or tap any `CanvasModulate` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var ambient: CanvasModulate = $CanvasModulate

func set_night_mode(enabled: bool) -> void:
	ambient.color = Color(0.18, 0.22, 0.35) if enabled else Color.WHITE
```
