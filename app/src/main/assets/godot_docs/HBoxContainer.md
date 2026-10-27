---
class: HBoxContainer
inherits: BoxContainer
verified: 4.3
short: A container that arranges its children in a horizontal row, left to right.
---

## Properties
- `alignment: BoxContainer.AlignmentMode` — How children are positioned as a group (begin, center, end).
- `vertical_alignment: BoxContainer.VerticalAlignmentMode` — How children align vertically within the row.
- (Layout properties inherited from [[Control]].)

## Methods
- `void add_theme_constant_override(name: StringName, constant: int)` — Overrides the `separation` constant (pixels between children).
- `void move_child(child: Node, to_index: int)` — (Inherited) reorders a child to `to_index`.
- `Control get_drag_prevention()` — (Inherited helper) returns drag-prevention control.

## Signals
- (No signals beyond those inherited from [[Control]] and [[Container]], e.g. `pre_sort_children`, `sort_children`.)

## Description
`HBoxContainer` lays its children out in a single horizontal row. Each child is sized according to its `size_flags_horizontal` and `size_flags_vertical`: a child with `SIZE_EXPAND_FILL` grows to fill leftover space, while a child without expand keeps its minimum size and is placed next to its siblings. The `separation` theme constant controls the pixel gap between children and is overridden per-container with `add_theme_constant_override("separation", n)`.

This is the standard layout for any horizontal arrangement — a row of buttons, a labelled icon, a hand of cards fanned left-to-right. Children keep their relative order based on their index in the tree, so reordering is a `move_child` call. Because it is a [[Container]], it recomputes layout automatically whenever children are added, removed, hidden, or resized.

In the Kadi app `HBoxContainer` is the natural fit for a player's hand of cards. Each card widget is a child; `separation` is set to a negative value to fan them overlapping, and a card lifts by temporarily raising its `size_flags_vertical` and adding a `theme_override` margin rather than fighting the layout. The top bar is another `HBoxContainer` holding the turn [[Label]], a spacer (an empty [[Control]] with `SIZE_EXPAND_FILL`), and a "Menu" [[Button]], so the bar stretches neatly across any screen width.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`HBoxContainer`)**: When configuring `HBoxContainer` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `HBoxContainer` offline. If your `HBoxContainer` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
@onready var player_hand_box: HBoxContainer = $RootVBox/PlayerHandScroll/PlayerHandBox

func rebuild_hand_ui(hand: Array[CardData], card_scene: PackedScene) -> void:
    for child in player_hand_box.get_children():
        child.queue_free()
    for card_data in hand:
        var node: Control = card_scene.instantiate()
        player_hand_box.add_child(node)
        node.setup(card_data)
```

