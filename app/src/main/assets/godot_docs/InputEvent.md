---
class: InputEvent
inherits: Resource
verified: 4.3
short: The base class for all input events such as key presses, mouse clicks, and touch.
---

## Properties
- `device: int` — The device index that produced the event (-1 = all).
- (Concrete subclasses add their own: `pressed`, `keycode`, `position`, `button_mask`, etc.)

## Methods
- `bool is_pressed()` — True if the event represents a press (down) rather than a release.
- `bool is_echo()` — True if the event is a repeat echo (key held down generating repeats).
- `bool is_action(action: StringName, exact_match: bool = false)` — True if this event matches the named input action.
- `bool is_action_pressed(action: StringName, allow_echo: bool = false)` — True for a press of the action.
- `bool is_action_released(action: StringName)` — True for a release of the action.
- `float get_action_strength(action: StringName, exact_match: bool = false)` — Returns analog strength (0–1) for the action.
- `bool is_match(event: InputEvent, exact_match: bool = true)` — True if this event matches another.
- `InputEvent xformed_by(transform: Transform2D, local_ofs: Vector2 = Vector2(0, 0))` — Returns a transformed copy (for 2D coordinate spaces).
- `String as_text()` — Returns a human-readable description.

## Signals
- (No signals; input events are passed to [[Node]] virtual methods and `gui_input`.)

## Description
`InputEvent` is the abstract parent of every input Godot dispatches: `InputEventKey`, `InputEventMouseButton`, `InputEventMouseMotion`, `InputEventScreenTouch`, `InputEventScreenDrag`, `InputEventJoypadButton`, `InputEventJoypadMotion`, and `InputEventAction`. Each is a [[Resource]] carrying the details of one discrete input. The engine pushes them to [[Node]]s through `_input`, `_unhandled_input`, and the `gui_input` signal of [[Control]].

The `is_action*` family is the high-level way to handle input: you define actions like `"play_card"` or `"draw"` in the [[InputMap]] (via [[ProjectSettings]] or [[Input]]) and then ask an event `is_action_pressed("play_card")`. This decouples the physical input (a tap, a key, a gamepad button) from the gameplay response, so the same code works on touch and keyboard.

In the Kadi app every tap on a card arrives as an `InputEventScreenTouch` (on Android) or `InputEventMouseButton` (in the editor), wrapped into the card widget's `gui_input` signal. The handler checks `event.is_pressed()` to fire only on the initial touch (not the release), then dispatches the play. Action-based input (`"ui_accept"`, `"ui_cancel"`) drives keyboard navigation of menus so the app is usable with a Bluetooth keyboard, mirroring the accessibility the docs viewer itself aims for.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`InputEvent`)**: When configuring `InputEvent` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `InputEvent` offline. If your `InputEvent` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using InputEvent in Godot 4.3 Kadi
func configure_inputevent_example(target: InputEvent) -> void:
    assert(target != null, "InputEvent reference must be valid")
    print("Configured InputEvent for Kadi table runtime")
```

