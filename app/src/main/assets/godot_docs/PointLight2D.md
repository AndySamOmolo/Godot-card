---
class: PointLight2D
inherits: Node2D
verified: 4.3
short: Positional 2D light source that illuminates nearby CanvasItems and casts 2D shadows.
---

## Properties
- `texture: Texture2D` — Radial gradient or spotlight cookie texture defining the light shape.
- `color: Color` — Light tint color.
- `energy: float` — Brightness multiplier (`1.0` default).
- `shadow_enabled: bool` — Enables 2D shadow casting against `LightOccluder2D` nodes.

## Methods
- (Inherits `Light2D` and [[Node2D]] methods.)

## Signals
- (No custom signals beyond [[Node2D]].)

## Description
Pair `PointLight2D` with `[[CanvasModulate]]` to create glowing torches, warm tabletop spotlights over `DiscardSlot`, or neon spell explosions in 2D games.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`PointLight2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `PointLight2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var lamp: PointLight2D = $TableSpotlight

func pulse_lamp(time_sec: float) -> void:
	lamp.energy = 1.1 + sin(time_sec * 4.0) * 0.1
```
