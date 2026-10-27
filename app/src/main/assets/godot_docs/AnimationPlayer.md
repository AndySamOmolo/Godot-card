---
class: AnimationPlayer
inherits: Node
verified: 4.3
short: A node that stores and plays named animations authored in the animation editor.
---

## Properties
- `current_animation: String` — The name of the animation currently selected to play.
- `current_animation_length: float` — Read-only length of the current animation.
- `current_animation_position: float` — Read-only playback position within the current animation.
- `assigned_animation: String` — The animation set programmatically (independent of autoplay).
- `autoplay: String` — Name of an animation to play on `_ready`.
- `speed_scale: float` — Multiplier on playback speed (1.0 = normal).
- `active: bool` — When true, the player processes animations.
- `playback_default_blend_time: float` — Default cross-fade when switching animations.
- `playback_process_mode: AnimationProcessCallback` — Process, physics, or manual.
- `method_call_mode: AnimationMethodCallMode` — Deferred or immediate method calls in animations.
- `reset_on_save: bool` — When true, a "RESET" track is saved with the scene.
- `animation_set: AnimationLibrary` — The default library (`""`) of animations.
- `movie_quit_on_finish: bool` — When true, the app quits when the movie-mode animation ends.

## Methods
- `Error add_animation_library(name: StringName, library: AnimationLibrary)` — Registers a named library.
- `AnimationLibrary get_animation_library(name: StringName)` — Returns a library by name.
- `bool has_animation(name: StringName)` — True if an animation exists in the default library.
- `void play(name: StringName = &"", custom_blend: float = -1.0, custom_speed: float = 1.0, from_end: bool = false)` — Plays an animation by name.
- `void play_backwards(name: StringName = &"", custom_blend: float = -1.0)` — Plays an animation in reverse.
- `void pause()` — Pauses playback.
- `void stop()` — Stops and rewinds the current animation.
- `void seek(time: float, update: bool = false, only_update: bool = false)` — Jumps to `time` in the animation.
- `float get_current_animation_position()` — Returns the current playback time.
- `float get_current_animation_length()` — Returns the current animation's length.
- `bool is_playing()` — True if an animation is active.
- `void queue(name: StringName)` — Queues the next animation after the current one finishes.
- `Animation find_animation(animation: Animation)` — Returns the name of a given `Animation`.

## Signals
- `animation_started(anim_name: StringName)` — Emitted when an animation begins.
- `animation_finished(anim_name: StringName)` — Emitted when an animation reaches its end.
- `animation_changed(old_name: StringName, new_name: StringName)` — Emitted when playback switches animations.
- `caches_cleared()` — Emitted when internal caches are cleared.

## Description
`AnimationPlayer` is Godot's timeline-based animation tool. Each animation is a `Animation` resource holding tracks that animate properties, call methods, play sounds, or trigger other animations. The player plays these by name, supports blending and speed scaling, and can queue the next animation. Animations are authored in the editor's animation dock and stored in an `AnimationLibrary` resource on the player.

Unlike [[Tween]] (which is built in code for one-shot interpolations), `AnimationPlayer` shines for reusable, authored sequences: a card flip, a dealing sweep, a victory dance. `seek` lets you scrub to an exact time, useful for previews, and `speed_scale` can slow a replay for cinematic moments. Method-call tracks let an animation trigger gameplay code at precise frames — for example, playing a "snap" sound when a card lands.

In the Kadi app an `AnimationPlayer` on the table node drives the dealing montage (cards fly out one by one from the deck) and the win-celebration sequence. A `deal` animation's method track calls `Deck.pop_card()` exactly when each card visually leaves the pile, so audio, logic, and visuals stay in sync. The "Niko Kadi" reveal is another authored animation with a `tween`-like scale-up plus a `Label.visible_characters` track for the typewriter text.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`AnimationPlayer`)**: When configuring `AnimationPlayer` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `AnimationPlayer` offline. If your `AnimationPlayer` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using AnimationPlayer in Godot 4.3 Kadi
func configure_animationplayer_example(target: AnimationPlayer) -> void:
    assert(target != null, "AnimationPlayer reference must be valid")
    print("Configured AnimationPlayer for Kadi table runtime")
```

