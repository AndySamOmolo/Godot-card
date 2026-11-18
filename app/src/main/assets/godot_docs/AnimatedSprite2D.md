---
class: AnimatedSprite2D
inherits: Node2D
verified: 4.3
short: 2D sprite node that plays frame-by-frame animations from a SpriteFrames resource.
---

## Properties
- `sprite_frames: SpriteFrames` — Resource containing named animations (`"idle"`, `"run"`, `"jump"`) and their textures.
- `animation: StringName` — Currently selected animation name.
- `flip_h: bool` — Flips the sprite horizontally when facing left.
- `speed_scale: float` — Playback speed multiplier (`1.0` default).

## Methods
- `void play(name: StringName = &"", custom_speed: float = 1.0, from_end: bool = false)` — Plays the named animation.
- `void stop()` — Stops playback.
- `bool is_playing()` — Returns `true` if an animation is currently playing.

## Signals
- `animation_finished()` — Emitted when a non-looping animation reaches its final frame.

## Description
`AnimatedSprite2D` is ideal for frame-by-frame 2D character sprites, spinning coins, flickering torches, and card burst effects when you don't need multi-property keyframing from `[[AnimationPlayer]]`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`AnimatedSprite2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `AnimatedSprite2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var anim_sprite: AnimatedSprite2D = $AnimatedSprite2D

func update_walk_visuals(velocity_x: float) -> void:
	if absf(velocity_x) > 5.0:
		anim_sprite.flip_h = velocity_x < 0.0
		anim_sprite.play("run")
	else:
		anim_sprite.play("idle")
```
