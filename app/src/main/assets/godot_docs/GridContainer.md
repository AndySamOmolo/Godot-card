---
class: GridContainer
inherits: Container
verified: 4.3
short: A container that arranges its children into a grid with a fixed number of columns.
---

## Properties
- `columns: int` — The number of columns; rows are added automatically as children are added.
- `vertical_separator: int` — (Theme) vertical spacing, set via override.
- `horizontal_separator: int` — (Theme) horizontal spacing, set via override.
- (Layout properties inherited from [[Control]].)

## Methods
- `void add_theme_constant_override(name: StringName, constant: int)` — Overrides `h_separation` or `v_separation` (pixels between cells).
- (Other layout methods inherited from [[Container]].)

## Signals
- (No signals beyond those inherited from [[Control]] and [[Container]].)

## Description
`GridContainer` lays its children out left-to-right, then top-to-bottom, wrapping into a new row every `columns` items. Each cell is sized to the largest child in its column and row, giving a tidy aligned grid. Set the spacing with `add_theme_constant_override("h_separation", n)` and `add_theme_constant_override("v_separation", n)`, or via the theme's `h_separation`/`v_separation` constants.

Unlike a manual anchor grid, `GridContainer` recomputes itself whenever children are added, removed, hidden, or resized, so dynamic UIs stay tidy with no bookkeeping. Children are placed in tree order, so reordering is a `move_child` call. For unequal cell sizes or spanning, a manual layout is still needed, but for uniform grids — option menus, icon pickers, popup buttons — `GridContainer` is the right tool.

In the Kadi app `GridContainer` lays out the four [[Suit]] buttons in the declaration popup (`l_08_01_declaration_popup`) as a clean 2×2 grid of [[Button]]s, and is reused to show the four-player seating arrangement with 2 columns. The card-back picker (when learners customise art) is a `GridContainer` of [[TextureRect]] cells with `columns = 4`, one per available back. Because the grid auto-sizes its rows, adding a fifth card back at runtime simply wraps into a new row.
