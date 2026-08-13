---
class: Node2D
inherits: CanvasItem
verified: 4.3
short: A 2D-positioned node with position, rotation, scale, and a transform.
---

## Properties
- `position: Vector2` — Local position relative to the parent.
- `rotation: float` — Local rotation in radians.
- `rotation_degrees: float` — Same as `rotation` but expressed in degrees.
- `scale: Vector2` — Local scale, default `Vector2(1, 1)`.
- `skew: float` — Shear applied after rotation, in radians.
- `global_position: Vector2` — Position in global 2D space.
- `global_rotation: float` — Rotation in global space (radians).
- `global_scale: Vector2` — Scale in global space.
- `transform: Transform2D` — The combined local transform.
- `global_transform: Transform2D` — The combined global transform.
- `z_index: int` — Inherited draw order (see [[CanvasItem]]).

## Methods
- `void rotate(radians: float)` — Adds to the current rotation.
- `void translate(offset: Vector2)` — Adds to the current position.
- `void apply_scale(ratio: Vector2)` — Multiplies the current scale.
- `float get_angle_to(point: Vector2)` — Returns the angle from this node to `point`.
- `Transform2D get_relative_transform_to_parent(parent: Node)` — Returns the transform from this node to a given parent.
- `Vector2 to_local(global_point: Vector2)` — Converts a global point to this node's local space.
- `Vector2 to_global(local_point: Vector2)` — Converts a local point to global space.
- `void look_at(point: Vector2)` — Rotates so the node's +X axis points at `point`.

## Signals
- (No signals beyond those inherited from [[CanvasItem]].)

## Description
`Node2D` is the standard base for anything that moves, rotates, or scales in 2D. It adds a 2D transform on top of [[CanvasItem]]: a `position`, `rotation`, and `scale` that compose with the parent's to produce a `global_transform`. Most 2D gameplay nodes — players, projectiles, the camera target — derive from `Node2D` or one of its subclasses such as [[Sprite2D]] and [[Camera2D]].

Because transforms compose hierarchically, moving a parent `Node2D` moves all of its children for free. This is exactly how a card fan works: a `Hand` node holds `Card` children whose local positions are simple offsets; rotating or sliding the hand repositions every card automatically. `to_local` and `to_global` convert coordinates between spaces, which is essential when handling pointer input that arrives in screen space.

In the Kadi project the playing field is a `Node2D`. Each card sprite is a [[Sprite2D]] parented under a `PlayerHand` `Node2D`; dealing animations simply tween the hand children's `position`. The active card lifts by raising its `z_index` and slightly increasing `scale`. When a player "Niko Kadi"s, the camera briefly zooms via [[Camera2D]] `zoom` while a `Node2D` pointer bobs over their hand — all driven by composing transforms.
