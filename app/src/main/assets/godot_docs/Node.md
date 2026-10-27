---
class: Node
inherits: Object
verified: 4.3
short: Base class for all Godot nodes. Everything in a scene tree is a Node.
---

## Properties
- `name: StringName` — The node's name. Must be unique among siblings.
- `process_mode: ProcessMode` — Controls whether `_process` and `_physics_process` run (inherit, always, when paused, disabled).
- `process_priority: int` — Lower values run first in `_process`.
- `process_physics_priority: int` — Lower values run first in `_physics_process`.
- `owner: Node` — The node that owns this one (usually the scene root), for saving/loading.
- `scene_file_path: String` — The `.tscn` file this node was loaded from, if any.
- `unique_name_in_owner: bool` — When true, the node can be fetched with `%Name`.
- `editor_description: String` — A developer-only note shown in the editor.

## Methods
- `void add_child(node: Node, force_readable_name: bool = false)` — Adds a child node. The child must not already have a parent.
- `void remove_child(node: Node)` — Detaches a child but does not free it.
- `Node get_child(index: int, include_internal: bool = false)` — Returns the child at the given index.
- `Array[Node] get_children(include_internal: bool = false)` — Returns all children.
- `Node get_node(path: NodePath)` — Resolves a relative or absolute path; errors if missing.
- `Node get_node_or_null(path: NodePath)` — Like `get_node` but returns `null` instead of erroring.
- `bool has_node(path: NodePath)` — True if the path resolves to a node.
- `Node get_parent()` — Returns the parent, or `null` if this is a root.
- `SceneTree get_tree()` — Returns the [[SceneTree]] this node belongs to.
- `bool is_inside_tree()` — True once the node is part of the active scene tree.
- `bool is_ancestor_of(node: Node)` — True if `node` is a descendant of this node.
- `void queue_free()` — Schedules the node for deletion at the end of the frame.
- `void reparent(new_parent: Node, keep_global_transform: bool = true)` — Moves this node under a new parent.
- `void print_tree()` — Prints the subtree rooted at this node for debugging.
- `void _ready()` — Virtual method called once when the node enters the tree and is ready.
- `void _process(delta: float)` — Virtual method called every frame.
- `void _physics_process(delta: float)` — Virtual method called every physics step.
- `void _input(event: InputEvent)` — Virtual method called for every input event before UI.

## Signals
- `ready()` — Emitted when the node enters the tree and is ready.
- `tree_entered()` — Emitted when the node enters the scene tree.
- `tree_exiting()` — Emitted just before the node leaves the tree.
- `tree_exited()` — Emitted right after the node has left the tree.
- `child_entered_tree(node: Node)` — Emitted when a child is added and enters the tree.
- `child_exiting_tree(node: Node)` — Emitted when a child is about to leave the tree.

## Description
`Node` is the fundamental building block of Godot. Every game object — a card, a player area, the table itself — is a `Node` or a subclass of one. Nodes are organised into a tree: each node has at most one parent, and the `SceneTree` owns the single root. You traverse the tree with [[NodePath]]-based methods such as `get_node("Hand/Card3")` or fetch uniquely-named nodes with `%Hand`.

A node's lifecycle is driven by virtual methods. Override `_ready()` to do one-time setup (deal cards, connect signals), `_process(delta)` for frame-by-frame updates such as animations, and `_physics_process(delta)` for deterministic movement. The tree emits the `ready` and `tree_entered` [[Signals]] so other nodes can react without polling.

In the Kadi game, each card on screen is a `Node` (a `TextureRect` or `Sprite2D`), the player areas are nodes, and the whole game lives under a `Game` root node. When a card is played you `remove_child` it from the hand [[Container]] and `add_child` it to the discard pile, then `queue_free` it when it should disappear. The `unique_name_in_owner` flag lets the game state fetch `%DrawPile` and `%DiscardPile` quickly from anywhere.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Node`)**: When configuring `Node` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Node` offline. If your `Node` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Node in Godot 4.3 Kadi
func configure_node_example(target: Node) -> void:
    assert(target != null, "Node reference must be valid")
    print("Configured Node for Kadi table runtime")
```

