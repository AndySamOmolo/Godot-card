---
class: RigidBody2D
inherits: PhysicsBody2D
verified: 4.3
short: A 2D physics body simulated by the engine, driven by forces, impulses, and collisions.
---

## Properties
- `mass: float` — The body's mass in kilograms; affects how forces accelerate it.
- `gravity_scale: float` — Multiplier applied to the world gravity for this body.
- `linear_velocity: Vector2` — Current linear velocity in pixels per second.
- `angular_velocity: float` — Current rotational velocity in radians per second.
- `linear_damp: float` — Damping applied to linear motion, slowing the body over time.
- `angular_damp: float` — Damping applied to rotation.
- `freeze: bool` — When true, the body stops being simulated (can act static or kinematic).
- `contact_monitor: bool` — When true, the body reports contacts via signals.
- `max_contacts_reported: int` — Maximum number of simultaneous contacts tracked when monitoring.

## Methods
- `void apply_central_impulse(impulse: Vector2 = Vector2(0, 0))` — Applies an instantaneous impulse at the center of mass.
- `void apply_impulse(impulse: Vector2, position: Vector2 = Vector2(0, 0))` — Applies an impulse at an offset, adding spin.
- `void apply_central_force(force: Vector2)` — Applies a continuous force at the center of mass.
- `void apply_torque_impulse(torque: float)` — Applies an instantaneous rotational impulse.
- `void apply_force(force: Vector2, position: Vector2 = Vector2(0, 0))` — Applies a continuous force at an offset.
- `Array[Node2D] get_colliding_bodies()` — Returns the bodies currently in contact (requires `contact_monitor`).
- `int get_contact_count()` — Returns the number of contacts currently reported.

## Signals
- `body_entered(body: Node)` — Emitted when another body starts colliding (needs `contact_monitor`).
- `body_exited(body: Node)` — Emitted when another body stops colliding.
- `sleeping_state_changed()` — Emitted when the body falls asleep or wakes up.

## Description
`RigidBody2D` is a [[PhysicsBody2D]] whose motion is fully handled by the physics engine. You do not set its `position` directly each frame; instead you push it with forces and impulses, and gravity, friction, and collisions do the rest. To read or nudge its state safely, override `_integrate_forces` and work through the physics state object.

It pairs with a [[CollisionShape2D]] that defines its solid extent and reacts to [[StaticBody2D]] and [[CharacterBody2D]] obstacles. Enabling `contact_monitor` lets it emit `body_entered`/`body_exited`, while `freeze` temporarily removes it from the simulation.

In the Kadi app `RigidBody2D` is not used for normal card handling — that is done with tweened [[Node2D]]s — but it powers the celebratory "scatter" when a round is won: the winner's cards briefly become rigid bodies, an `apply_impulse` tosses them into the air, and they tumble under gravity before the next hand resets the table.
