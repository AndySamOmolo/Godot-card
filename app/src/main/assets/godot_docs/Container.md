---
class: Container
inherits: Control
verified: 4.3
short: A base control that automatically positions and resizes its child controls.
---

## Methods
- `void fit_child_in_rect(child: Control, rect: Rect2)` — Positions and sizes `child` to fit within `rect`.
- `void queue_sort()` — Requests a re-layout of the children on the next frame.
- `PackedInt32Array get_allowed_size_flags_horizontal()` — Returns which horizontal size flags this container honors.
- `PackedInt32Array get_allowed_size_flags_vertical()` — Returns which vertical size flags this container honors.

## Signals
- `pre_sort_children()` — Emitted just before the container arranges its children.
- `sort_children()` — Emitted when the container sorts (re-lays out) its children.

## Description
`Container` is the abstract base for every layout control in Godot. Instead of you setting anchors and offsets on each child, a `Container` takes ownership of its children's positions and sizes and arranges them according to a rule. You never place a bare `Container` directly; you use a subclass such as [[VBoxContainer]], [[HBoxContainer]], [[GridContainer]], [[MarginContainer]], or [[ScrollContainer]].

Children influence the layout through their size flags and minimum size, and a container re-sorts whenever those change or when you call `queue_sort`. To build a custom layout you extend `Container`, listen for `sort_children`, and call `fit_child_in_rect` for each child.

In the Kadi app containers structure the whole interface: the player's hand is an [[HBoxContainer]] so cards space themselves evenly no matter how many are held, the suit-declaration popup uses a [[GridContainer]] of four buttons, and the scrolling rules screen wraps a [[VBoxContainer]] in a [[ScrollContainer]]. Because the container does the math, adding or removing a card reflows the hand automatically.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Container`)**: When configuring `Container` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Container` offline. If your `Container` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Container in Godot 4.3 Kadi
func configure_container_example(target: Container) -> void:
    assert(target != null, "Container reference must be valid")
    print("Configured Container for Kadi table runtime")
```

