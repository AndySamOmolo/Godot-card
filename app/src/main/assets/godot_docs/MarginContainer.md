---
class: MarginContainer
inherits: Container
verified: 4.3
short: A container that adds configurable margins around a single child.
---

## Properties
- (No dedicated properties; margins are set via theme constants.)
- (Layout properties inherited from [[Control]].)

## Methods
- `void add_theme_constant_override(name: StringName, constant: int)` — Overrides one of `margin_left`, `margin_top`, `margin_right`, `margin_bottom`, `margin_left` (pixel insets).

## Signals
- (No signals beyond those inherited from [[Control]] and [[Container]].)

## Description
`MarginContainer` is the simplest layout helper: it resizes its (single) child to fill the container minus the four `margin_*` theme constants. It is the canonical way to pad content — wrap a [[Panel]] in a `MarginContainer` with `margin_*` set to 16 and the panel floats 16px inside whatever the container's own rect is. Unlike hand-set anchors, the margins track the container automatically as it resizes.

Although it accepts multiple children, `MarginContainer` is designed for one child; multiple children overlap because each is told to fill the same inset rect. Set margins through the editor's theme-override panel or in code with `add_theme_constant_override("margin_left", 24)` (and top/right/bottom equivalents).

In the Kadi app a `MarginContainer` pads the screen edges so the table never touches the bezel on phones, and another pads the inside of the declaration popup so its [[GridContainer]] of buttons does not touch the [[Panel]] border. The phase-content area of the learning screens is wrapped in a `MarginContainer` with `margin_left`/`margin_right` set to 24 so long-form text has comfortable gutters, mirroring the layout of this very documentation.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`MarginContainer`)**: When configuring `MarginContainer` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `MarginContainer` offline. If your `MarginContainer` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using MarginContainer in Godot 4.3 Kadi
func configure_margincontainer_example(target: MarginContainer) -> void:
    assert(target != null, "MarginContainer reference must be valid")
    print("Configured MarginContainer for Kadi table runtime")
```

