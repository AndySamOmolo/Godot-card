---
class: Vector2i
inherits: null
verified: 4.3
short: A built-in 2D vector of two integers used for grid coordinates, sizes, and indices.
---

## Properties
- `x: int` — The vector's X component. Also accessible as `[0]`.
- `y: int` — The vector's Y component. Also accessible as `[1]`.

## Methods
- `float length()` — Returns the Euclidean length (magnitude) of the vector.
- `int length_squared()` — Returns the squared length; cheaper than `length` for comparisons.
- `Vector2i abs()` — Returns a copy with the absolute value of each component.
- `Vector2i sign()` — Returns a copy with each component replaced by its sign (-1, 0, or 1).
- `Vector2i clamp(min: Vector2i, max: Vector2i)` — Returns the vector componentwise clamped between `min` and `max`.
- `Vector2i min(with: Vector2i)` — Returns the componentwise minimum of this and `with`.
- `Vector2i max(with: Vector2i)` — Returns the componentwise maximum of this and `with`.
- `float aspect()` — Returns the ratio of `x` to `y`.
- `float distance_to(to: Vector2i)` — Returns the distance between this vector and `to`.
- `Vector2i snapped(step: Vector2i)` — Returns a copy snapped to the nearest multiple of `step`.

## Description
`Vector2i` is the integer sibling of [[Vector2]], storing whole-number `x` and `y` components. It is the natural type for anything discrete: tile coordinates, texture and [[Window]] sizes, pixel positions, and array-style indices, where fractional values would be meaningless. Being a value type, it is copied on assignment.

It provides a focused math toolkit — `abs`, `sign`, `clamp`, and componentwise `min`/`max` — and converts cleanly to and from `Vector2` when floating-point math is needed. Constants like `Vector2i.ZERO` and `Vector2i.ONE` cover common values.

In the Kadi app `Vector2i` addresses the logical card grid: each hand slot and the draw/discard piles have integer `(column, row)` coordinates, and `clamp` keeps a highlighted selection index inside the valid range as the player scrolls left and right through a large hand.
