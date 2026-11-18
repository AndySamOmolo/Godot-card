---
class: NavigationAgent2D
inherits: Node
verified: 4.3
short: 2D pathfinding helper that queries NavigationServer2D to steer a parent Node2D around obstacles.
---

## Properties
- `target_position: Vector2` — World-space destination point for pathfinding.
- `path_desired_distance: float` — Distance threshold to advance to the next waypoint.
- `target_desired_distance: float` — Distance threshold to consider navigation finished.
- `avoidance_enabled: bool` — Enables RVO dynamic crowd avoidance.

## Methods
- `Vector2 get_next_path_position()` — Returns the next waypoint in world coordinates.
- `bool is_navigation_finished()` — Returns `true` when the agent reaches `target_position`.
- `void set_velocity(velocity: Vector2)` — Submits desired velocity for RVO avoidance calculation.

## Signals
- `velocity_computed(safe_velocity: Vector2)` — Emitted when RVO avoidance computes a collision-free velocity.
- `navigation_finished()` — Emitted when the path completes.

## Description
`NavigationAgent2D` attaches to a `[[CharacterBody2D]]` inside a scene with a baked `[[NavigationRegion2D]]`. Set `target_position` periodically and move toward `get_next_path_position()` inside `_physics_process()`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`NavigationAgent2D`)**: Press **F1** inside the Godot 4.3 editor or tap any `NavigationAgent2D` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var nav: NavigationAgent2D = $NavigationAgent2D

func get_steering_velocity(origin: Vector2, speed: float) -> Vector2:
	if nav.is_navigation_finished():
		return Vector2.ZERO
	return origin.direction_to(nav.get_next_path_position()) * speed
```
