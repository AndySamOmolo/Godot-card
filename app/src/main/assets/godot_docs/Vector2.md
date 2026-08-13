---
class: Vector2
inherits: null
verified: 4.3
short: A built-in 2D vector of two floats used for positions, sizes, and directions.
---

## Properties
- `x: float` — The vector's X component. Also accessible as `[0]`.
- `y: float` — The vector's Y component. Also accessible as `[1]`.

## Methods
- `float length()` — Returns the Euclidean length (magnitude) of the vector.
- `float length_squared()` — Returns the squared length; cheaper than `length` for comparisons.
- `Vector2 normalized()` — Returns a copy scaled to unit length (or zero if the vector is zero).
- `float distance_to(to: Vector2)` — Returns the distance between this vector and `to`.
- `float distance_squared_to(to: Vector2)` — Returns the squared distance to `to`.
- `float dot(with: Vector2)` — Returns the dot product with `with`.
- `float angle()` — Returns the vector's angle in radians relative to the +X axis.
- `float angle_to_point(to: Vector2)` — Returns the angle from this point toward `to`.
- `Vector2 direction_to(to: Vector2)` — Returns a normalized vector pointing from this vector to `to`.
- `Vector2 lerp(to: Vector2, weight: float)` — Linearly interpolates toward `to` by `weight` (0–1).
- `Vector2 rotated(angle: float)` — Returns the vector rotated by `angle` radians.
- `Vector2 move_toward(to: Vector2, delta: float)` — Moves toward `to` by at most `delta`.
- `Vector2 clamp(min: Vector2, max: Vector2)` — Returns the vector componentwise clamped between `min` and `max`.
- `Vector2 abs()` — Returns a copy with the absolute value of each component.

## Description
`Vector2` is Godot's fundamental 2D value type. It stores two `float`s, `x` and `y`, and is used everywhere in 2D: [[Node2D]] `position`, `scale`, and velocities are all `Vector2`s. Being a value type, it is copied on assignment rather than referenced, so passing one to a function never mutates the original.

The type carries a large math toolkit: `length`, `normalized`, and `dot` for geometry; `distance_to` and `direction_to` for aiming and movement; `lerp` and `move_toward` for smooth animation; and `rotated`/`angle` for orientation. Constants like `Vector2.ZERO`, `Vector2.ONE`, `Vector2.UP`, and `Vector2.RIGHT` provide common values without allocation.

In the Kadi app `Vector2` describes where every card sits: dealing tweens a card's `position` from the draw pile to a slot in the player's hand, and `lerp` smooths the motion. Fan layouts compute each card's offset with `rotated`, and drag-and-drop uses `direction_to` and `distance_to` to snap a released card onto the nearest [[Area2D]] drop zone.
