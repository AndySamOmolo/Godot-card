---
class: Color
inherits: null
verified: 4.3
short: A built-in RGBA color with floating-point channels in the 0–1 range.
---

## Properties
- `r: float` — Red channel, normally in the range 0–1.
- `g: float` — Green channel, normally in the range 0–1.
- `b: float` — Blue channel, normally in the range 0–1.
- `a: float` — Alpha (opacity) channel, 0 transparent to 1 opaque.
- `r8: int` — Red channel as an integer 0–255.
- `g8: int` — Green channel as an integer 0–255.
- `b8: int` — Blue channel as an integer 0–255.
- `a8: int` — Alpha channel as an integer 0–255.
- `h: float` — Hue of the color in HSV space (0–1).
- `s: float` — Saturation of the color in HSV space (0–1).
- `v: float` — Value/brightness of the color in HSV space (0–1).

## Methods
- `Color lerp(to: Color, weight: float)` — Linearly interpolates toward `to` by `weight` (0–1).
- `Color lightened(amount: float)` — Returns a copy lightened toward white by `amount`.
- `Color darkened(amount: float)` — Returns a copy darkened toward black by `amount`.
- `Color inverted()` — Returns the inverted color (1 minus each RGB channel).
- `Color blend(over: Color)` — Returns this color alpha-blended over `over`.
- `Color clamp(min: Color = Color(0, 0, 0, 0), max: Color = Color(1, 1, 1, 1))` — Clamps each channel between `min` and `max`.
- `String to_html(with_alpha: bool = true)` — Returns a hex string like `ff0000ff`.
- `bool is_equal_approx(to: Color)` — True if this color is approximately equal to `to`.

## Description
`Color` represents an RGBA value with `float` channels, so operations blend smoothly and can exceed 1 for HDR effects. It is a value type: assigning a `Color` copies it. You can build one from floats (`Color(1, 0, 0)`), from a hex string (`Color("ff0000")`), or from named constants such as `Color.RED` and `Color.TRANSPARENT`.

Colors expose both linear (`r`, `g`, `b`, `a`), 8-bit (`r8`…), and HSV (`h`, `s`, `v`) views of the same value, plus helpers like `lightened`, `darkened`, `inverted`, and `lerp` for tinting and fades. They are consumed all over the engine — [[CanvasItem]] `modulate`, [[ColorRect]] `color`, and theme overrides on [[Control]] all take a `Color`.

In the Kadi app `Color` drives visual feedback: a legal card is tinted white while an illegal one is `darkened` grey, and the active suit shows as a [[ColorRect]] whose `color` matches the declared suit. When a player calls "Niko Kadi" their name label pulses by `lerp`ing its `modulate` between `Color.WHITE` and a warm gold.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Color`)**: When configuring `Color` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Color` offline. If your `Color` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Color in Godot 4.3 Kadi
func configure_color_example(target: Color) -> void:
    assert(target != null, "Color reference must be valid")
    print("Configured Color for Kadi table runtime")
```

