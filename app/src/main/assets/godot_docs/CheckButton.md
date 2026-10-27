---
class: CheckButton
inherits: BaseButton
verified: 4.3
short: A toggle button that displays a checkmark and a text label, like a checkbox.
---

## Properties
- `text: String` — The label shown next to the checkmark.
- `alignment: HorizontalAlignment` — Horizontal placement of the label/check.
- `icon: Texture2D` — (Inherited from [[Button]]) optional icon.
- `toggle_mode: bool` — (Inherited from `BaseButton`) always `true` for CheckButton.
- `button_pressed: bool` — (Inherited from `BaseButton`) the on/off state.
- `action_mode: ActionMode` — (Inherited) when the signal fires on press or release.
- `disabled: bool` — (Inherited) when true, the control is greyed out and inactive.
- (Layout properties inherited from [[Control]].)

## Methods
- (No public methods beyond those inherited from [[Control]] and `BaseButton`.)

## Signals
- `toggled(button_pressed: bool)` — Emitted when the on/off state changes.
- `pressed()` — (Inherited from `BaseButton`) emitted on activation.

## Description
`CheckButton` is a [[Button]]-style toggle that shows a check indicator and a text label. Unlike [[Button]] it is in toggle mode by default (`toggle_mode = true`), so `button_pressed` flips between `true` and `false` each click and the `toggled` signal fires with the new state. Visually it looks like a checkbox row rather than a pressable slab, which reads better for persistent settings.

For an on/off switch that looks like a sliding toggle, set the theme variation to use the `CheckBox` look, or just use `CheckBox` — both share the same `BaseButton` API. Because state is held in `button_pressed`, you can read or force the value at any time and the visual updates automatically.

In the Kadi app `CheckButton`s populate the settings panel: "Auto-call Niko Kadi", "Highlight playable cards", "Confirm before quitting", and "Sound on" from `l_11_01_tweens`. Each `toggled` signal writes straight to the autoloaded settings resource, so changes apply immediately. A `CheckButton` also gates the "Show card suggestions" hint that dims unplayable cards in the hand — flipping it instantly retints every card widget without restarting the match.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`CheckButton`)**: When configuring `CheckButton` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `CheckButton` offline. If your `CheckButton` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using CheckButton in Godot 4.3 Kadi
func configure_checkbutton_example(target: CheckButton) -> void:
    assert(target != null, "CheckButton reference must be valid")
    print("Configured CheckButton for Kadi table runtime")
```

