---
class: ProjectSettings
inherits: Object
verified: 4.3
short: The singleton holding all project-wide settings: rendering, physics, input map, and autoloads.
---

## Properties
- (ProjectSettings is a property bag; values are read and set by string key via `get_setting`/`set_setting`.)
- `config_version: int` — Internal schema version (read-only).
- `display/window/size/viewport_width: int` — Default window width.
- `display/window/size/viewport_height: int` — Default window height.
- `application/run/main_scene: String` — Path to the scene loaded on launch.
- `application/config/name: String` — The app's display name.
- `application/config/icon: String` — Path to the app icon.
- `physics/common/physics_ticks_per_second: int` — Physics tick rate (mirrors [[Engine]]).
- `rendering/vsync/mode: VSyncMode` — VSync mode.
- `input/*` — The input action map (each action is an array of [[InputEvent]]s).

## Methods
- `Variant get_setting(name: String, default: Variant = null)` — Returns a setting value, or `default` if it doesn't exist.
- `void set_setting(name: String, value: Variant)` — Sets a setting (persists to `project.godot` in editor; runtime changes are not saved).
- `bool has_setting(name: String)` — True if the setting exists.
- `String globalize_path(path: String)` — Converts a `res://` path to an OS-absolute path.
- `String localize_path(path: String)` — Converts an OS-absolute path to a `res://` path.
- `Error save()` — Writes all settings to `project.godot`.
- `void set_initial_value(name: String, value: Variant)` — Records the editor default for a custom setting (for the "revert" button).
- `void set_as_basic(name: String, basic: bool)` — Marks a setting as basic (shown by default in the editor).
- `String get_setting_with_override(name: String)` — Returns a setting, applying feature overrides (e.g. Android-specific).
- `Error load_resource_pack(pack: String, replace_files: bool = true, offset: int = 0)` — Loads a `.pck` file's contents.

## Signals
- `settings_changed()` — Emitted when settings change (mostly from the editor).

## Description
`ProjectSettings` is the global store for everything in `project.godot`: window size, main scene, app name and icon, physics rate, rendering options, the input action map, and — crucially — the list of autoloads. It is a singleton accessed as `ProjectSettings`, and its settings are read with `get_setting` and written with `set_setting`. Custom settings you add in the editor become first-class properties too.

Autoloads are registered here: each entry pairs a name (the global variable name, e.g. `GameState`) with a script or scene path, and the engine instantiates it on launch and keeps it alive for the whole run. This is how the Kadi app's game-state singleton from `l_01_04_autoloads` is created. `globalize_path`/`localize_path` convert between `res://` paths and absolute filesystem paths, essential when working with external files via [[FileAccess]].

In the Kadi app `ProjectSettings` carries the app's display name ("Kadi Tutor"), icon, default window size, and the autoload list — `GameState`, `Settings`, `AudioBus` — that the rest of the code reaches as globals. The input map's `"play_card"` and `"draw"` actions live here too. `globalize_path` is used when the learning app writes a learner-progress JSON to the user data directory: it builds the absolute path for [[FileAccess]] from a `user://` path.
