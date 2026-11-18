---
class: Parallax2D
inherits: Node2D
verified: 4.3
short: Streamlined Godot 4.3 node for scrolling 2D background/foreground depth layers relative to Camera2D.
---

## Properties
- `scroll_scale: Vector2` — Multiplier for camera movement (`Vector2(0.3, 0.3)` moves slowly in the distance).
- `repeat_size: Vector2` — Seamless infinite tiling period in pixels (`Vector2(1152, 0)` for horizontal sky loop).
- `autoscroll: Vector2` — Velocity in pixels/second for drifting clouds or starfields.

## Methods
- (Uses standard [[Node2D]] transform methods.)

## Signals
- (No custom signals beyond [[Node2D]].)

## Description
Introduced in Godot 4.3 to replace the legacy `ParallaxBackground` + `ParallaxLayer` pair, `Parallax2D` lets you create infinite scrolling skies, distant mountains, or drifting tabletop dust in a single node.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`Parallax2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `Parallax2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var clouds_layer: Parallax2D = $CloudsParallax2D

func _ready() -> void:
	clouds_layer.scroll_scale = Vector2(0.25, 0.25)
	clouds_layer.autoscroll = Vector2(-18.0, 0.0)
```
