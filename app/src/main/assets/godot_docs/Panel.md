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

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Panel`)**: When configuring `Panel` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Panel` offline. If your `Panel` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
@onready var card_bg: Panel = $CardBg

func set_selected_border(selected: bool) -> void:
    var style := StyleBoxFlat.new()
    style.bg_color = Color("#182232")
    style.border_color = Color("#38BDF8") if selected else Color("#334155")
    style.set_border_width_all(3 if selected else 1)
    style.set_corner_radius_all(8)
    card_bg.add_theme_stylebox_override("panel", style)
```

