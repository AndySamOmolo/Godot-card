---
class: VBoxContainer
inherits: BoxContainer
verified: 4.3
short: A container that arranges its children in a vertical column, top to bottom.
---

## Properties
- `alignment: BoxContainer.AlignmentMode` — How children are grouped vertically (begin, center, end).
- `horizontal_alignment: BoxContainer.HorizontalAlignmentMode` — How children align horizontally within the column.
- (Layout properties inherited from [[Control]].)

## Methods
- `void add_theme_constant_override(name: StringName, constant: int)` — Overrides the `separation` constant (pixels between children).
- `void move_child(child: Node, to_index: int)` — (Inherited) reorders a child to `to_index`.

## Signals
- (No signals beyond those inherited from [[Control]] and [[Container]], e.g. `pre_sort_children`, `sort_children`.)

## Description
`VBoxContainer` is the vertical twin of [[HBoxContainer]]: it stacks its children top-to-bottom, sizing each according to its size flags and spacing them by the `separation` theme constant. A child flagged `SIZE_EXPAND_FILL` absorbs leftover vertical space, which is the standard way to make one element grow while the rest stay fixed (for example, a scrollable log that fills the column while the input row stays at the bottom).

Like all [[Container]]s it relays out automatically when children change, so adding or hiding elements requires no manual positioning. Use a `Control` with `SIZE_EXPAND_FILL` as a flexible spacer to push other elements apart — a handy trick for top-and-bottom anchored UIs inside a single column.

In the Kadi app a `VBoxContainer` stacks the player areas on the side of the screen: a [[Label]] name, an [[HBoxContainer]] of card widgets, then a small status row. The whole status overlay (turn label, penalty total, last-play label) is itself a `VBoxContainer`, so the labels stack neatly and reflow when a longer message wraps. The declaration popup's body is a `VBoxContainer` holding the title [[Label]] followed by a [[GridContainer]] of suit buttons, centred with `alignment = ALIGNMENT_CENTER`.
