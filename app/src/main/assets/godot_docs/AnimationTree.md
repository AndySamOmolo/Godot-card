---
class: AnimationTree
inherits: Node
verified: 4.3
short: A node that blends and transitions between animations via a state-machine graph.
---

## Properties
- `tree_root: AnimationRootNode` — The root of the blend tree or state machine.
- `animation_player: NodePath` — Path to the [[AnimationPlayer]] this tree drives.
- `active: bool` — When true, the tree processes.
- `process_callback: AnimationProcessCallback` — Process, physics, or manual.
- `deterministic: bool` — When true, playback is reproducible (important for networked games).
- `advanced_expression_base_node: NodePath` — Base node for Expression nodes' `self`.

## Methods
- `void set(parameter: StringName, value: Variant)` — Sets a blend-tree parameter (float, bool, int).
- `Variant get(parameter: StringName)` — Reads a parameter.
- `void set_active(active: bool)` — Enables or disables the tree.
- `void rename_parameter(old_name: StringName, new_name: StringName)` — Renames a parameter everywhere.
- `StringName[] get_root_motion_track()` — Returns the track used for root-motion extraction.
- `void set_root_motion_track(track_path: NodePath)` — Sets the track used for root-motion extraction.
- `Vector3 get_root_motion_position()` — Returns the root-motion delta for the current frame.
- `Quaternion get_root_motion_rotation()` — Returns the root-motion rotation delta.
- `Vector3 get_root_motion_scale()` — Returns the root-motion scale delta.
- `Transform3D get_root_motion_transform()` — Returns the combined root-motion transform.

## Signals
- (No signals of its own beyond those inherited from [[Node]].)

## Description
`AnimationTree` is the higher-level sibling of [[AnimationPlayer]]. Instead of playing one animation at a time, it builds a graph: blend trees for smooth mixing (e.g. 50% idle + 50% walk based on speed) or a `AnimationNodeStateMachine` for state transitions with guards. The tree reads animations from a connected [[AnimationPlayer]] and exposes parameters you set from code to drive the blending and transitions.

This is the right tool when you need reactive, continuous animation — a character whose idle, walk, and run blend by speed, or whose attack animation cancels into a hit-reaction based on a parameter. `AnimationPlayer` alone can't blend; `AnimationTree` can. For a one-shot, code-driven interpolation, [[Tween]] is simpler still.

The Kadi app is mostly UI, so `AnimationTree` is used sparingly — a 3D card-flip variant could blend a card's "face-up idle", "flipping", and "face-down idle" states via a `AnimationNodeStateMachine`, transitioning on a `is_flipping` bool parameter. Root motion isn't relevant here, but the same `set("speed", x)` pattern that drives a walking character could drive a "deal speed" blend so a learner can scrub from slow-mo to normal dealing.
