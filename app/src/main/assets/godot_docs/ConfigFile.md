---
class: ConfigFile
inherits: RefCounted
verified: 4.3
short: Helper class for reading and writing structured INI-style settings files (user://settings.cfg).
---

## Properties
- (Managed via singleton methods.)

## Methods
- `void set_value(section: String, key: String, value: Variant)` — Assigns a setting inside `[section]`.
- `Variant get_value(section: String, key: String, default: Variant = null)` — Reads a setting or returns `default`.
- `Error save(path: String)` — Writes the configuration file to disk (`user://settings.cfg`).
- `Error load(path: String)` — Loads the configuration file from disk.

## Signals
- (Inherits [[RefCounted]] — no signals.)

## Description
`ConfigFile` is Godot's built-in serializer for `[audio]`, `[display]`, and `[input]` preferences. Unlike raw JSON, it preserves native Godot types (`Vector2`, `Color`, `InputEventKey`) automatically!

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`ConfigFile`)**: Press **F1** inside the Godot 4.3 editor or tap any `ConfigFile` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func save_audio_pref(master_vol: float) -> void:
	var cfg := ConfigFile.new()
	cfg.set_value("audio", "master", master_vol)
	cfg.save("user://settings.cfg")
```
