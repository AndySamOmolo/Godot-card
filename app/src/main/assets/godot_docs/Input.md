---
class: Input
inherits: Object
verified: 4.3
short: A singleton providing global, polled access to the current input state and action strength.
---

## Properties
- `use_accumulated_input: bool` — When true, input is accumulated and flushed at the end of _process (avoids mid-frame flicker).
- `emulate_mouse_from_touch: bool` — When true, touch events also generate synthetic mouse events.
- `emulate_touch_from_mouse: bool` — When true, mouse events also generate synthetic touch events.
- `mouse_mode: MouseMode` — Visible, hidden, captured, confined, or confined-hidden.

## Methods
- `bool is_action_pressed(action: StringName, allow_echo: bool = false)` — True if the action is currently held.
- `bool is_action_just_pressed(action: StringName)` — True on the frame the action was first pressed.
- `bool is_action_just_released(action: StringName)` — True on the frame the action was released.
- `float get_axis(negative_action: StringName, positive_action: StringName)` — Returns -1..1 from two opposing actions.
- `Vector2 get_vector(negative_x: StringName, positive_x: StringName, negative_y: StringName, positive_y: StringName, deadzone: float = 0.5)` — Returns a 2D vector from four actions.
- `float get_action_strength(action: StringName, exact_match: bool = false)` — Returns analog 0..1 strength of an action.
- `float get_action_raw_strength(action: StringName, exact_match: bool = false)` — Returns raw strength without deadzone applied.
- `bool is_anything_pressed()` — True if any key/button is currently held.
- `bool is_physical_key_pressed(keycode: Key)` — True if the physical key is held.
- `bool is_key_pressed(keycode: Key)` — True if the keycode is held.
- `bool is_mouse_button_pressed(button: MouseButton)` — True if the mouse button is held.
- `bool is_joy_button_pressed(device: int, button: JoyButton)` — True if a gamepad button is held.
- `Vector2 get_vector2()` — (Helper) returns the mouse position via `get_mouse_position`.
- `Vector2 get_mouse_position()` — Returns the mouse position in screen space.
- `void warp_mouse(position: Vector2)` — Moves the mouse cursor.
- `void flush_buffered_events()` — Forces pending input events to be processed now.
- `InputEvent[] get_connected_joypads()` — Returns the indices of connected gamepads.
- `void set_custom_mouse_cursor(cursor: Resource, shape: Input.CursorShape = 0, hotspot: Vector2 = Vector2(0, 0))` — Sets a custom cursor image.
- `void start_joy_vibration(device: int, weak_magnitude: float, strong_magnitude: float, duration: float = 0.0)` — Triggers gamepad rumble.

## Signals
- `joy_connection_changed(device: int, connected: bool)` — Emitted when a gamepad connects or disconnects.

## Description
`Input` is the engine's input singleton (accessed globally as `Input`). Where `InputEvent` (see [[InputEvent]]) describes a single discrete event delivered to a node, `Input` gives you a polled view: ask "is this action pressed right now?" from anywhere, including inside `_process` and `_physics_process`. The two models complement each other — events are best for precise reactions like UI clicks, polling is best for continuous state like movement.

`is_action_just_pressed` / `is_action_just_released` are the polled equivalents of the event-based `_input` for one-frame reactions. `get_axis` and `get_vector` cleanly combine opposing actions into signed values, ideal for movement. `mouse_mode` controls pointer visibility and capture (captured is great for first-person look), and the `emulate_*` flags smooth cross-platform input by synthesising mouse↔touch.

In the Kadi app `Input` is used for continuous checks rather than per-event handling: a held "draw" keyboard key, an analog drag for rearranging the hand, and gamepad rumble (`start_joy_vibration`) when a heavy penalty lands. `get_action_strength` lets a learner set the AI think-delay via the keyboard's up/down arrows, and `is_action_just_pressed("ui_cancel")` provides the back button on Android hardware. Actions are defined in [[ProjectSettings]] input map.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Input`)**: When configuring `Input` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Input` offline. If your `Input` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Input in Godot 4.3 Kadi
func configure_input_example(target: Input) -> void:
    assert(target != null, "Input reference must be valid")
    print("Configured Input for Kadi table runtime")
```

