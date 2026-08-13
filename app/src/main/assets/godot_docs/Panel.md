---
class: Panel
inherits: Control
verified: 4.3
short: A Control that draws a styled background rectangle, used to group other UI.
---

## Properties
- `theme_type_variation: StringName` — A theme variation name, e.g. `PanelHeader`, to restyle this panel.
- (Layout properties such as `anchor_*`, `offset_*`, `grow_*`, `size`, `mouse_filter` are inherited from [[Control]].)

## Methods
- (No public methods beyond those inherited from [[Control]].)

## Signals
- (No signals beyond those inherited from [[Control]] such as `resized`, `gui_input`, `mouse_entered`, `mouse_exited`, `focus_entered`, `focus_exited`.)

## Description
`Panel` is the simplest styled container: it draws the theme's `panel` stylebox across its rect and nothing else. It does **not** arrange its children — children are positioned by their own anchors/offsets — so it is the right choice when you want a visually grouped background and manual (or container-driven) child placement. For automatic child layout, use [[VBoxContainer]], [[HBoxContainer]], or [[GridContainer]] inside it.

Because the panel's look is entirely theme-driven, you restyle it with `add_theme_stylebox_override("panel", sb)` or by setting a theme variation. Panels compose well: a background `Panel` holds a header panel and a content panel, each tinted differently, to build a card-like dialog with zero custom drawing.

In the Kadi app `Panel` is the backdrop for the declaration popup (`l_08_01_declaration_popup`), for the "Niko Kadi!" banner, and for the player-status area. The popup panel parents a [[Label]] title and a [[GridContainer]] of four [[Button]]s; the panel's stylebox gives it the card-table felt look, and a second inner panel adds a bevel. During penalty resolution a translucent red `Panel` briefly flashes over the affected player's area to draw attention — done by tweening its `modulate` alpha with a [[Tween]].
