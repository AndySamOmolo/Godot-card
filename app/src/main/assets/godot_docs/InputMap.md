---
class: InputMap
inherits: Object
verified: 4.3
short: Global singleton that manages InputAction names and their bound keyboard, mouse, and gamepad InputEvents.
---

## Properties
- (Managed via singleton methods.)

## Methods
- `bool has_action(action: StringName)` — Returns `true` if the action exists in the InputMap.
- `void action_erase_events(action: StringName)` — Clears all current key/button bindings for `action`.
- `void action_add_event(action: StringName, event: InputEvent)` — Binds a new `InputEvent` to `action`.
- `Array[InputEvent] action_get_events(action: StringName)` — Returns all events currently bound to `action`.

## Signals
- (No signals.)

## Description
`InputMap` powers custom keybinding and controller remapping screens in your Options Menu. Erase the old binding with `action_erase_events()` and register the player's newly pressed `[[InputEvent]]` with `action_add_event()`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`InputMap`)**: Press **F1** inside the Godot 4.3 editor or tap any `InputMap` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func rebind_action(action_name: StringName, new_event: InputEvent) -> void:
	if InputMap.has_action(action_name):
		InputMap.action_erase_events(action_name)
		InputMap.action_add_event(action_name, new_event)
```
