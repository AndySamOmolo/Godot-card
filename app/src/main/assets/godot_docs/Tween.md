---
class: Tween
inherits: RefCounted
verified: 4.3
short: A code-created interpolator that animates properties of objects over time.
---

## Properties
- (Tween holds no inspectable properties in the editor; state is set through chained methods.)
- `is_running: bool` — (Read via `is_running()`.)
- `is_valid: bool` — (Read via `is_valid()`.)
- `total_time: float` — (Read via `get_total_elapsed_time()`.) Total elapsed time since the tween started.

## Methods
- `Tween tween_property(object: Object, property: NodePath, final_val: Variant, duration: float)` — Animates `property` of `object` toward `final_val` over `duration` seconds.
- `Tween tween_interval(time: float)` — Pauses the tween for `time` seconds (no property change).
- `Tween tween_callback(callback: Callable)` — Calls `callback` when the tween reaches this point.
- `Tween tween_method(method: Callable, from: Variant, to: Variant, duration: float)` — Calls `method(value)` each frame, interpolating `value` from `to` over `duration`.
- `Tween set_parallel(parallel: bool = true)` — Makes subsequent tweens run at the same time instead of in sequence.
- `Tween set_trans(trans: TransitionType)` — Sets the easing curve (LINEAR, SINE, QUAD, CUBIC, …).
- `Tween set_ease(ease: EaseType)` — Sets ease in, out, in-out, or out-in.
- `Tween set_delay(delay: float)` — Adds a delay before this tween starts.
- `Tween set_speed_scale(speed: float)` — Multiplies this tween's speed.
- `Tween set_ignore_time_scale(ignore: bool = true)` — When true, runs even while the SceneTree is paused.
- `Tween chain()` — Forces the next appended tween to wait for this one.
- `Tween parallel()` — Forces the next appended tween to run alongside this one.
- `bool is_running()` — True if the tween is currently animating.
- `bool is_valid()` — True if the tween has not been killed or finished.
- `void stop()` — Stops the tween (it stays valid and can be replayed).
- `void pause()` — Pauses the tween.
- `void play()` — Resumes a paused tween.
- `void kill()` — Cancels and frees the tween.
- `float get_total_elapsed_time()` — Total seconds the tween has run.

## Signals
- (No signals; chain `tween_callback` onto the end of a sequence instead, or check `is_running()` from the loop.)

## Description
`Tween` is Godot's code-driven animation tool. You create one with `create_tween()` (a method of [[Node]]), then chain `tween_property`, `tween_callback`, `tween_method`, and `tween_interval` calls. By default each step runs in sequence; `set_parallel(true)` (or `parallel()`) makes steps overlap, so a card can scale, rotate, and move at the same time. `set_trans` and `set_ease` pick the easing curve, giving a bounce, overshoot, or smooth glide.

Unlike [[AnimationPlayer]], a `Tween` is built in code for ad-hoc, one-shot animations — flying a card from the hand to the discard pile, flashing a panel red, popping a score. Because `Tween` is a [[RefCounted]], the `SceneTree` owns it and frees it when it finishes, so you rarely manage its lifetime. Kill it early with `kill()` if you need to cancel (for example, when a new animation supersedes an old one).

In the Kadi app `Tween` is the backbone of `l_11_01_tweens`. Playing a card tweens its `position` from the hand slot to the discard pile with `set_trans(TRANS_CUBIC)` and `set_ease(EASE_OUT)`, runs a brief scale-up in parallel, and calls `tween_callback` at the end to register the play in the log. A penalty reveal tweens the [[Label]]'s `modulate` from white to red and back. `set_ignore_time_scale(true)` is set on the "Niko Kadi" banner so it animates even while the game is paused for the declaration.
