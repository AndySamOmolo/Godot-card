---
class: Control
inherits: CanvasItem
verified: 4.3
short: Base class for all UI nodes; provides anchors, offsets, sizing, theming, and focus.
---

## Properties
- `anchor_left: float` — Left anchor (0–1) relative to the parent rect.
- `anchor_right: float` — Right anchor (0–1) relative to the parent rect.
- `anchor_top: float` — Top anchor (0–1) relative to the parent rect.
- `anchor_bottom: float` — Bottom anchor (0–1) relative to the parent rect.
- `offset_left: float` — Distance from the left anchor to the left edge, in pixels.
- `offset_right: float` — Distance from the right anchor to the right edge, in pixels.
- `offset_top: float` — Distance from the top anchor to the top edge, in pixels.
- `offset_bottom: float` — Distance from the bottom anchor to the bottom edge, in pixels.
- `grow_horizontal: GrowDirection` — How the control grows horizontally when there is extra space.
- `grow_vertical: GrowDirection` — How the control grows vertically when there is extra space.
- `size: Vector2` — The control's size in pixels (read-only at runtime; set via offsets/anchors).
- `position: Vector2` — Top-left position relative to the parent.
- `rotation: float` — Rotation in radians around `pivot_offset`.
- `pivot_offset: Vector2` — Pivot point for rotation and scaling, in local pixels.
- `mouse_filter: MouseFilter` — Whether the control receives mouse events (`STOP`, `PASS`, `IGNORE`).
- `theme: Theme` — A custom theme overriding the default for this subtree.
- `theme_type_variation: StringName` — A theme variation name for styling.

## Methods
- `void set_anchors_preset(preset: LayoutPreset, keep_offsets: bool = true)` — Applies a preset like full-rect or top-right.
- `void set_anchors_and_offsets_preset(preset: LayoutPreset, resize_mode: LayoutPresetMode = 0, margin: int = 0)` — Sets anchors and offsets from a preset.
- `void set_position(position: Vector2, keep_offsets: bool = false)` — Sets the position.
- `void set_size(size: Vector2, keep_offsets: bool = false)` — Sets the size.
- `Vector2 get_position()` — Returns the position.
- `Vector2 get_size()` — Returns the size.
- `Vector2 get_minimum_size()` — Returns the minimum size the control wants, based on its content.
- `Rect2 get_rect()` — Returns the local rect as a [[Rect2]].
- `Rect2 get_global_rect()` — Returns the rect in global (screen) coordinates.
- `void grab_focus()` — Gives keyboard focus to this control.
- `void release_focus()` — Removes keyboard focus from this control.
- `bool has_focus()` — True if this control currently has focus.
- `void add_theme_icon_override(name: StringName, texture: Texture2D)` — Overrides a theme icon.
- `void add_theme_color_override(name: StringName, color: Color)` — Overrides a theme color.
- `void add_theme_font_override(name: StringName, font: Font)` — Overrides a theme font.
- `void add_theme_font_size_override(name: StringName, size: int)` — Overrides a theme font size.
- `void add_theme_stylebox_override(name: StringName, stylebox: StyleBox)` — Overrides a theme stylebox.
- `void add_theme_constant_override(name: StringName, constant: int)` — Overrides a theme constant (e.g. spacing).

## Signals
- `resized()` — Emitted when the control's size changes.
- `gui_input(event: InputEvent)` — Emitted when an [[InputEvent]] is received while the control has mouse focus.
- `minimum_size_changed()` — Emitted when `get_minimum_size` would return a different value.
- `size_flags_changed()` — Emitted when size flags change.
- `focus_entered()` — Emitted when the control gains keyboard focus.
- `focus_exited()` — Emitted when the control loses keyboard focus.
- `mouse_entered()` — Emitted when the pointer enters the control's rect.
- `mouse_exited()` — Emitted when the pointer leaves the control's rect.

## Description
`Control` is the base class for Godot's 2D user-interface nodes — [[Button]], [[Label]], [[Panel]], and every container all derive from it. It introduces the anchor/offset layout system: each edge is anchored to a point on the parent (0–1) and then offset by a pixel distance, which lets a single control stretch or pin itself to any side. Containers such as [[VBoxContainer]] and [[HBoxContainer]] manage these offsets for you, so most of the time you set size flags and let layout do the work.

`Control` also owns theming and focus. Override theme properties with `add_theme_*_override` to restyle one node, or assign a `Theme` resource to restyle a whole subtree. `grab_focus` / `release_focus` drive keyboard navigation, and the `gui_input` signal gives you raw [[InputEvent]]s scoped to the control — the cleanest way to handle clicks on a card widget.

In the Kadi app every on-screen element is a `Control`. The player's hand is a [[HBoxContainer]] of `Control`-based card widgets; the penalty total and turn indicator are [[Label]]s anchored to the top; the suit-declaration popup from `l_08_01_declaration_popup` is a [[Panel]] with four [[Button]]s laid out in a [[GridContainer]]. The cards use `mouse_filter = STOP` and listen for `gui_input` so a click is detected even when the card is rotated by `rotation`. Theming overrides tint the focused card gold and the disabled ones grey.
