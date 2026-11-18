---
class: AudioServer
inherits: Object
verified: 4.3
short: Low-level singleton managing audio buses (Master, Music, SFX), decibel volumes, muting, and audio effects.
---

## Properties
- (Managed via singleton methods.)

## Methods
- `int get_bus_index(bus_name: StringName)` — Returns the integer index of a named audio bus (e.g. `"Master"`, `"SFX"`).
- `void set_bus_volume_db(bus_idx: int, volume_db: float)` — Sets the bus volume in decibels (`0.0 dB` is unity gain).
- `void set_bus_mute(bus_idx: int, enable: bool)` — Mutes or unmutes the specified audio bus.

## Signals
- `bus_layout_changed()` — Emitted when buses are added, moved, or renamed.

## Description
`AudioServer` is the global singleton that powers every Options Menu volume slider. Always convert linear `0.0..1.0` slider values to decibels with `linear_to_db()` before calling `set_bus_volume_db()`.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`AudioServer`)**: Press **F1** inside the Godot 4.3 editor or tap any `AudioServer` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func set_sfx_volume(linear_0_to_1: float) -> void:
	var bus: int = AudioServer.get_bus_index("SFX")
	if bus != -1:
		AudioServer.set_bus_mute(bus, linear_0_to_1 <= 0.001)
		AudioServer.set_bus_volume_db(bus, linear_to_db(maxf(linear_0_to_1, 0.001)))
```
