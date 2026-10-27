---
class: Node3D
inherits: Node
verified: 4.3
short: A 3D-positioned node with transform, rotation, scale, and a basis; base for 3D scenes.
---

## Properties
- `transform: Transform3D` — Local transform (basis + origin).
- `position: Vector3` — Local position relative to the parent.
- `rotation: Vector3` — Local Euler rotation in radians (X, Y, Z).
- `rotation_degrees: Vector3` — Same as `rotation` but in degrees.
- `rotation_mode: EulerOrder` — Order in which Euler rotations are applied.
- `scale: Vector3` — Local scale, default `Vector3(1, 1, 1)`.
- `quaternion: Quaternion` — Local rotation as a quaternion (avoids gimbal lock).
- `basis: Basis` — The 3x3 rotation/scale part of the transform.
- `global_transform: Transform3D` — Combined transform in world space.
- `global_position: Vector3` — Position in world space.
- `top_level: bool` — When true, the node ignores its parent's transform.

## Methods
- `void rotate(axis: Vector3, angle: float)` — Rotates around an axis by `angle` radians.
- `void rotate_x(angle: float)` — Rotates around the local X axis.
- `void rotate_y(angle: float)` — Rotates around the local Y axis.
- `void rotate_z(angle: float)` — Rotates around the local Z axis.
- `void translate(offset: Vector3)` — Adds to the current position.
- `void look_at(target: Vector3, up: Vector3 = Vector3(0, 1, 0), use_model_front: bool = false)` — Rotates to face `target`.
- `Vector3 to_local(global_point: Vector3)` — Converts a world point to local space.
- `Vector3 to_global(local_point: Vector3)` — Converts a local point to world space.
- `void set_identity()` — Resets the transform to identity.

## Signals
- (No signals beyond those inherited from [[Node]].)

## Description
`Node3D` is the 3D counterpart of [[Node2D]]: it gives a node a `Transform3D` made of a `Basis` (rotation and scale) and an `origin` (position). Every 3D-visible thing in Godot — meshes, lights, the [[Camera3D]] — derives from `Node3D`. Transforms compose hierarchically just like in 2D, so a moving parent moves its children, and `to_local`/`to_global` convert coordinates between spaces for input and AI math.

Rotation can be set as Euler angles (`rotation_degrees`), a [[Quaternion]] (for smooth interpolation without gimbal lock), or directly via the `basis`. Use `look_at` to aim a node at a target, common for cameras and turrets. Setting `top_level = true` detaches a node from its parent's transform while keeping it as a child in the tree — handy for cameras that should follow a target's position without inheriting its rotation.

The Kadi app is primarily 2D, so `Node3D` is rarely used directly. It is documented here because some learners graduate to a 3D table view or a 3D card-flip effect. When you do model cards in 3D, each card is a `Node3D` with a `MeshInstance3D`; the same `position`/`rotation` tweening patterns you learned for [[Node2D]] carry over, and a [[Camera3D]] replaces the [[Camera2D]].

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Node3D`)**: When configuring `Node3D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Node3D` offline. If your `Node3D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Node3D in Godot 4.3 Kadi
func configure_node3d_example(target: Node3D) -> void:
    assert(target != null, "Node3D reference must be valid")
    print("Configured Node3D for Kadi table runtime")
```

