---
class: Transform2D
inherits: null
verified: 4.3
short: A built-in 2D affine transform holding rotation, scale, skew, and translation as three vectors.
---

## Properties
- `x: Vector2` — The first basis column, the transformed X axis.
- `y: Vector2` — The second basis column, the transformed Y axis.
- `origin: Vector2` — The translation component (position) of the transform.

## Methods
- `Transform2D inverse()` — Returns the inverse transform (valid for orthonormal transforms).
- `Transform2D affine_inverse()` — Returns the inverse including scale and skew.
- `float get_rotation()` — Returns the rotation of the transform in radians.
- `Vector2 get_scale()` — Returns the scale encoded in the basis.
- `Vector2 get_origin()` — Returns the `origin` (translation).
- `float get_skew()` — Returns the skew angle in radians.
- `Transform2D rotated(angle: float)` — Returns a copy rotated by `angle` about the origin.
- `Transform2D scaled(scale: Vector2)` — Returns a copy scaled by `scale`.
- `Transform2D translated(offset: Vector2)` — Returns a copy translated by `offset`.
- `Transform2D orthonormalized()` — Returns a copy with normalized, perpendicular axes.
- `Transform2D interpolate_with(xform: Transform2D, weight: float)` — Interpolates toward `xform` by `weight`.

## Description
`Transform2D` is the built-in value type that encodes a full 2D affine transform: two basis vectors `x` and `y` describe rotation, scale, and skew, while `origin` stores translation. It is what [[Node2D]] uses internally for `global_transform`, and it maps points from local space to parent space when multiplied with a [[Vector2]].

Being a value type, it is copied on assignment. Compose transforms by multiplying them, and reverse one with `inverse` or `affine_inverse`. Helpers like `rotated`, `scaled`, and `interpolate_with` build or blend transforms without touching the raw basis by hand.

In the Kadi app `Transform2D` drives card placement math: converting a touch from screen space into a card's local space uses a card's `affine_inverse`, and `interpolate_with` smoothly blends a card between its fanned hand pose and a highlighted "selected" pose as the player considers which card to play.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Transform2D`)**: When configuring `Transform2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Transform2D` offline. If your `Transform2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Transform2D in Godot 4.3 Kadi
func configure_transform2d_example(target: Transform2D) -> void:
    assert(target != null, "Transform2D reference must be valid")
    print("Configured Transform2D for Kadi table runtime")
```

