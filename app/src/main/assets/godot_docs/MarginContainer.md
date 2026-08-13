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
