---
class: Rect2
inherits: null
verified: 4.3
short: A built-in axis-aligned 2D rectangle defined by a position and a size.
---

## Properties
- `position: Vector2` — The top-left corner of the rectangle.
- `size: Vector2` — The width and height of the rectangle.
- `end: Vector2` — The bottom-right corner (`position + size`); settable, which adjusts `size`.

## Methods
- `Vector2 get_center()` — Returns the center point of the rectangle.
- `float get_area()` — Returns the rectangle's area.
- `bool has_point(point: Vector2)` — True if `point` lies inside the rectangle.
- `bool intersects(b: Rect2, include_borders: bool = false)` — True if this rectangle overlaps `b`.
- `bool encloses(b: Rect2)` — True if this rectangle fully contains `b`.
- `Rect2 intersection(b: Rect2)` — Returns the overlapping rectangle of this and `b`.
- `Rect2 merge(b: Rect2)` — Returns the smallest rectangle enclosing both this and `b`.
- `Rect2 expand(to: Vector2)` — Returns a copy grown to include `to`.
- `Rect2 grow(amount: float)` — Returns a copy grown by `amount` on all sides.
- `Rect2 abs()` — Returns a copy with a non-negative `size` (normalizes a flipped rect).

## Description
`Rect2` is a built-in value type describing an axis-aligned rectangle by its top-left `position` and its `size`, both [[Vector2]]s. As a value type it is copied on assignment, so passing it into a function never mutates the caller's copy. A related `Rect2i` uses integer components.

Its methods answer the common spatial questions: `has_point` for hit-testing, `intersects` and `encloses` for overlap checks, and `merge`/`expand` for growing a bounding box to fit more content. `get_center` and `get_area` are handy for layout math.

In the Kadi app `Rect2` describes card and drop-zone bounds: `has_point` decides whether a touch landed on a specific card in the fanned hand, and `intersects` checks whether a dragged card sufficiently overlaps the central discard pile before it snaps home. A running `merge` also computes the bounding box needed to frame all cards when the table [[Camera2D]] auto-fits the layout.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Rect2`)**: When configuring `Rect2` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Rect2` offline. If your `Rect2` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Rect2 in Godot 4.3 Kadi
func configure_rect2_example(target: Rect2) -> void:
    assert(target != null, "Rect2 reference must be valid")
    print("Configured Rect2 for Kadi table runtime")
```

