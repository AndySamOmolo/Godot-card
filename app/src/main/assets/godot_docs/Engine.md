---
class: Engine
inherits: Object
verified: 4.3
short: A singleton exposing engine version, frames, time scale, and run-mode information.
---

## Properties
- `version: String` — Human-readable engine version (read-only).
- `version_info: Dictionary` — Structured version (major, minor, patch, status, build).
- `frames_per_second: float` — Current measured FPS (read-only).
- `physics_ticks_per_second: int` — Target physics tick rate (default 60).
- `max_fps: int` — Frame-rate cap (0 = unlimited).
- `time_scale: float` — Multiplier on real time applied to _process/_physics deltas (1.0 = normal).
- `pause: bool` — Whether the SceneTree is paused.
- `is_editor_hint: bool` — True when running inside the editor (read-only).
- `fixed_fps: int` — Optional fixed-frame cap (0 = no fixed cap).
- `max_physics_steps_per_frame: int` — Caps physics catch-up to avoid spiral-of-death.

## Methods
- `String get_version()` — Returns the engine version string.
- `Dictionary get_version_info()` — Returns structured version info.
- `String get_author_info()` — Returns the engine's authorship string.
- `String get_donate_url()` — Returns the donation URL.
- `Dictionary get_license_info()` — Returns third-party licence texts.
- `bool is_editor_hint()` — True when running inside the editor.
- `void set_time_scale(scale: float)` — Sets `time_scale`.
- `float get_frames_per_second()` — Returns the current FPS.
- `int get_physics_frames()` — Returns the total physics frame count.
- `int get_process_frames()` — Returns the total process frame count.
- `void set_physics_ticks_per_second(ticks: int)` — Sets the physics tick rate.
- `void set_max_fps(fps: int)` — Sets the frame-rate cap.
- `Object get_main_loop()` — Returns the running [[MainLoop]] (usually a SceneTree).
- `void set_frame_stepped(frame_stepped: bool)` — Enables single-frame stepping (debugging).
- `String get_write_movie_path()` — Returns the movie output path if running in movie-write mode.

## Signals
- (No signals; `Engine` is a passive global queried from code.)

## Description
`Engine` is the global singleton (accessed as `Engine`) that reports what the engine is doing. The most-used members are `is_editor_hint()` (skip side-effects when the script runs inside the editor), `time_scale` (slow-motion or fast-forward), `frames_per_second` (perf HUD), and `physics_ticks_per_second` (tune simulation rate). It also exposes version info, useful for showing "Made with Godot 4.3" credits or gating features by version.

`time_scale` multiplies the `delta` passed to `_process` and `_physics_process`, so the whole simulation (and any [[Tween]] not marked `set_ignore_time_scale`) slows or speeds up. `max_fps` caps rendering, helpful on laptops to save power; `physics_ticks_per_second` controls simulation granularity.

In the Kadi app `Engine.is_editor_hint()` guards the dealer so cards aren't actually dealt while editing the scene (which would corrupt the tree). `time_scale` powers a "slow-mo" learning mode that halves the dealing animation so learners can study it, and a "fast-forward" replay for skipping ahead. `frames_per_second` is shown in a debug overlay, and `physics_ticks_per_second` is left at 60 — the Kadi UI is driven by `_process`, not physics, so it stays smooth regardless.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Engine`)**: When configuring `Engine` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Engine` offline. If your `Engine` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Engine in Godot 4.3 Kadi
func configure_engine_example(target: Engine) -> void:
    assert(target != null, "Engine reference must be valid")
    print("Configured Engine for Kadi table runtime")
```

