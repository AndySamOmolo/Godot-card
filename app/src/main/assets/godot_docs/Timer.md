---
class: Timer
inherits: Node
verified: 4.3
short: A node that counts down and emits timeout at a set interval.
---

## Properties
- `wait_time: float` — Seconds between starts (or between `timeout` signals when `one_shot` is false).
- `one_shot: bool` — When true, the timer fires once and stops; when false, it repeats.
- `autostart: bool` — When true, the timer starts automatically on `_ready`.
- `process_callback: TimerProcessCallback` — Whether the timer ticks in `_process` or `_physics_process`.
- `time_left: float` — Read-only seconds remaining until the next `timeout`.
- `paused: bool` — (Inherited from [[Node]]) whether the timer is paused by the SceneTree.

## Methods
- `void start(time_sec: float = -1.0)` — Starts the timer; optionally overrides `wait_time`.
- `void stop()` — Stops the timer and resets `time_left`.
- `bool is_stopped()` — True when the timer is not running.
- `float get_time_left()` — Returns `time_left`.
- `void set_wait_time(time_sec: float)` — Sets `wait_time`.
- `float get_wait_time()` — Returns `wait_time`.
- `bool has_custom_time_assigned()` — True if `start` was called with an explicit time.

## Signals
- `timeout()` — Emitted when `wait_time` elapses (once per cycle when repeating).

## Description
`Timer` is the simplest scheduling node: you give it a `wait_time`, call `start()`, and it emits `timeout` after that many seconds. In `one_shot` mode it fires once; otherwise it loops, emitting `timeout` repeatedly — perfect for tickers, AI think-intervals, and turn limits. The `process_callback` decides whether the timer advances with frames or physics steps; for gameplay timing like AI delays, `_process` (idle) is usually fine.

Because `Timer` is a [[Node]], it lives in the scene tree and is paused with it. Connect `timeout` to a [[Callable]] that performs whatever the delay was for: an AI move, a reveal, a cleanup. `time_left` lets you read progress for a countdown UI, and `stop()` cancels a pending tick cleanly. For sub-frame precision or many simultaneous timers, you might roll your own accumulator, but for the common case `Timer` is exactly right.

In the Kadi app a `Timer` paces the AI: when it becomes a CPU player's turn the game `start`s a `one_shot` timer of, say, 0.8s, and on `timeout` the AI's choose-and-play function runs — giving the human time to see what's happening instead of an instant response. Another timer enforces a hard turn limit, flashing a [[ProgressBar]] as `time_left` drains. During the reshuffle of `l_10_01_reshuffle`, a short `Timer` hides the table behind a "Shuffling…" overlay, then reveals the recycled draw pile.
