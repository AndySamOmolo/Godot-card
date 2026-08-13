---
class: CollisionShape2D
inherits: Node2D
verified: 4.3
short: A node that gives a collision object its 2D shape via a Shape2D resource.
---

## Properties
- `shape: Shape2D` — The collision shape resource (e.g. rectangle, circle, capsule).
- `disabled: bool` — When true, the shape is ignored by the physics engine.
- `one_way_collision: bool` — When true, collisions are only detected from one side.
- `one_way_collision_margin: float` — Margin in pixels for one-way collision detection.
- `debug_color: Color` — Color used to draw the shape when collision debug is enabled.

## Methods
- `void set_shape(shape: Shape2D)` — Assigns the collision shape resource.
- `Shape2D get_shape()` — Returns the current shape resource.
- `void set_disabled(disabled: bool)` — Enables or disables the shape.
- `bool is_disabled()` — True when the shape is disabled.
- `void set_one_way_collision(enabled: bool)` — Toggles one-way collision.
- `bool is_one_way_collision_enabled()` — True when one-way collision is on.

## Description
`CollisionShape2D` is a helper [[Node2D]] that supplies the geometry a collision object uses. On its own it does nothing; you parent it under an [[Area2D]], [[StaticBody2D]], [[CharacterBody2D]], or other `CollisionObject2D` and assign a `Shape2D` resource such as a `RectangleShape2D` or `CircleShape2D`. Its transform positions and rotates the shape relative to the body.

You can toggle it at runtime with `disabled` to switch a hitbox off without freeing it, and `one_way_collision` makes platforms you can jump up through. Enabling collision debug draws the shape in `debug_color` so you can see it in a running game.

In the Kadi app each card scene carries a `CollisionShape2D` with a `RectangleShape2D` sized to the card art, parented under the card's [[Area2D]]. That is what lets the discard pile detect a dropped card. Disabled cards in a locked hand simply set `disabled = true` so they cannot be picked up until it is the player's turn.
