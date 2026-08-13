---
class: StaticBody2D
inherits: PhysicsBody2D
verified: 4.3
short: An immovable 2D physics body used for floors, walls, and other fixed collision.
---

## Properties
- `physics_material_override: PhysicsMaterial` — Overrides friction and bounce for this body.
- `constant_linear_velocity: Vector2` — A surface velocity that pushes touching bodies without moving this one.
- `constant_angular_velocity: float` — A surface angular velocity applied to touching bodies.

## Methods
- `void set_constant_linear_velocity(vel: Vector2)` — Sets the conveyor-belt-style surface velocity.
- `Vector2 get_constant_linear_velocity()` — Returns the constant linear velocity.
- `void set_constant_angular_velocity(vel: float)` — Sets the constant angular surface velocity.
- `float get_constant_angular_velocity()` — Returns the constant angular velocity.
- `void set_physics_material_override(material: PhysicsMaterial)` — Assigns the physics material override.
- `PhysicsMaterial get_physics_material_override()` — Returns the physics material override.

## Description
`StaticBody2D` is a physics body that never moves on its own. It is the cheapest and most common choice for level geometry — the ground, walls, and platforms that other bodies collide with. Give it one or more [[CollisionShape2D]] children to define its outline; the engine treats it as having infinite mass.

Although it stays put, it is not entirely passive: `constant_linear_velocity` and `constant_angular_velocity` let it act like a conveyor belt or rotating surface, imparting motion to bodies that touch it, and a `physics_material_override` tunes how bouncy or slippery it feels.

In the Kadi app `StaticBody2D` forms the invisible boundary of the play table. The scattering victory tokens (which are [[CharacterBody2D]] nodes) bounce off these static walls instead of flying off-screen, keeping the celebration contained within the felt.
