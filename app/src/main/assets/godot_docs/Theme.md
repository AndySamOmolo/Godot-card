---
class: Theme
inherits: Resource
verified: 4.3
short: A resource that stores reusable styling — colors, fonts, sizes, and styleboxes — for Control nodes.
---

## Properties
- `default_font: Font` — Fallback font applied to controls that do not specify their own.
- `default_font_size: int` — Fallback font size in pixels used when a control has none.
- `default_base_scale: float` — Global scale multiplier applied to theme values.

## Methods
- `void set_color(name: StringName, theme_type: StringName, color: Color)` — Stores a named color for a control type.
- `Color get_color(name: StringName, theme_type: StringName)` — Returns a stored color, or a fallback if missing.
- `bool has_color(name: StringName, theme_type: StringName)` — True if the named color exists for the type.
- `void set_font(name: StringName, theme_type: StringName, font: Font)` — Stores a named font for a control type.
- `void set_font_size(name: StringName, theme_type: StringName, font_size: int)` — Stores a named font size.
- `void set_constant(name: StringName, theme_type: StringName, constant: int)` — Stores a named integer constant (spacing, margins).
- `void set_stylebox(name: StringName, theme_type: StringName, texture: StyleBox)` — Stores a named [[StyleBox]] for a control type.
- `void set_icon(name: StringName, theme_type: StringName, texture: Texture2D)` — Stores a named [[Texture2D]] icon.
- `void clear()` — Removes all stored theme items.
- `void merge_with(other: Theme)` — Copies all items from `other` into this theme.

## Description
`Theme` is a [[Resource]] that centralizes the look of your user interface. Instead of styling each [[Control]] individually, you define named colors, fonts, font sizes, constants, icons, and [[StyleBox]]es keyed by control type (such as `Button` or `Label`), then assign the theme once so every matching control inherits it.

Themes cascade: a control uses the nearest theme found up the tree, and per-control theme overrides can still tweak individual items. Because it is a resource, a theme can be saved to disk, shared across scenes, and swapped at runtime to reskin the whole app.

In the Kadi app a single `Theme` defines the card-table aesthetic — the felt-green panel styleboxes, the gold accent color on the active player, and the rounded [[Button]] look of the "Draw", "Play", and "Kadi!" actions — so every menu and dialog stays visually consistent, and a "high-contrast" theme can be merged in for accessibility.
