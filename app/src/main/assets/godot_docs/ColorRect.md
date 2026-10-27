---
class: ColorRect
inherits: Control
verified: 4.3
short: A control that fills its rectangle with a single flat color.
---

## Properties
- `color: Color` — The [[Color]] used to fill the control's entire rect.

## Methods
- `void set_color(color: Color)` — Sets the fill color.
- `Color get_color()` — Returns the current fill color.

## Description
`ColorRect` is the simplest drawable [[Control]]: it paints its whole rectangle with one solid `color`, alpha included. It has no texture and no border — just a flat fill sized by the usual anchor/offset layout it inherits from `Control`. Because it is so lightweight, it is the go-to node for backgrounds, dividers, and quick overlays.

Setting `color` with an alpha below 1 makes a translucent panel, which is perfect for dimming the screen behind a popup. It is also a convenient target for a custom `ShaderMaterial` when you want a full-screen visual effect.

In the Kadi app `ColorRect` provides the felt-green table background and the dark semi-transparent scrim that appears behind the suit-declaration popup. A small `ColorRect` also acts as the active-suit indicator: after an Ace is played, its `color` is set to match the declared suit so every player can see what must be followed.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`ColorRect`)**: When configuring `ColorRect` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `ColorRect` offline. If your `ColorRect` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using ColorRect in Godot 4.3 Kadi
func configure_colorrect_example(target: ColorRect) -> void:
    assert(target != null, "ColorRect reference must be valid")
    print("Configured ColorRect for Kadi table runtime")
```

