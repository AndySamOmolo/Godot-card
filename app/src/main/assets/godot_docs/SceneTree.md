---
class: SceneTree
inherits: MainLoop
verified: 4.3
short: The main loop that owns the scene, groups, and the root window.
---

## Properties
- `root: Window` — The root [[Node]] (a `Window`) that every scene is parented under.
- `current_scene: Node` — The node instantiated as the active main scene.
- `paused: bool` — When true, pauses processing and physics per each node's process mode.
- `auto_accept_quit: bool` — When true, the OS quit request quits automatically.
- `quit_on_go_back: bool` — When true, the Android "back" action quits the app.

## Methods
- `SceneTreeTimer create_timer(time_sec: float, process_always: bool = true, process_in_physics: bool = false, ignore_time_scale: bool = false)` — Returns a one-shot timer that emits `timeout`.
- `Tween create_tween()` — Creates a [[Tween]] bound to the tree.
- `Error change_scene_to_file(path: String)` — Switches the main scene to the scene at `path`.
- `Error change_scene_to_packed(packed_scene: PackedScene)` — Switches to an already-loaded [[PackedScene]].
- `void reload_current_scene()` — Reloads the current main scene.
- `Array[Node] get_nodes_in_group(group: StringName)` — Returns all nodes in the given group.
- `void call_group(group: StringName, method: StringName, ...)` — Calls `method` on every node in `group`.
- `void quit(exit_code: int = 0)` — Requests the application to quit.

## Signals
- `process_frame()` — Emitted once per idle (rendered) frame.
- `physics_frame()` — Emitted once per physics frame.
- `tree_changed()` — Emitted whenever the node hierarchy changes.
- `node_added(node: Node)` — Emitted when a node enters the tree.
- `node_removed(node: Node)` — Emitted when a node leaves the tree.

## Description
`SceneTree` is the runtime that drives a running Godot game. It descends from `MainLoop` and manages the tree of [[Node]]s, dispatching `_process` and `_physics_process`, handling pausing, and owning the `root` [[Window]]. Any node reaches it with `get_tree()`.

Beyond bookkeeping it offers convenient services: `create_timer` for quick one-shot delays without a [[Timer]] node, `create_tween` for animations, group calls via `call_group` and `get_nodes_in_group`, and scene switching with `change_scene_to_file` / `change_scene_to_packed`. The `process_frame` and `physics_frame` signals let systems hook the frame loop without overriding node callbacks.

In the Kadi app `SceneTree` ties the flow together: the main menu calls `change_scene_to_file` to enter a match, `create_timer` gives the AI its short "thinking" pause, and every card belongs to a `"cards"` group so a rules refresh can `call_group("cards", "update_playable")` in one line. When the match ends, a results screen is reached with `change_scene_to_packed`.
