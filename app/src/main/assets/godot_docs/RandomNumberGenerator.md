---
class: RandomNumberGenerator
inherits: RefCounted
verified: 4.3
short: A reference-counted random number generator with an optional seed for reproducibility.
---

## Properties
- `seed: int` — The current seed; setting it resets the generator to a known state.
- `state: int` — The internal generator state (read/write for advanced control).
- `random_seed: int` — (Write only via setter) randomises the seed from the OS entropy source.

## Methods
- `float randf()` — Returns a random float in `[0.0, 1.0)`.
- `float randf_range(from: float, to: float)` — Returns a random float in `[from, to)`.
- `int randi()` — Returns a random non-negative 32-bit integer.
- `int randi_range(from: int, to: int)` — Returns a random integer in `[from, to]` (inclusive).
- `Variant rand_weighted(arr: Array)` — Picks an index from `arr` weighted by its (non-negative) values.
- `float random()` — Legacy alias for `randf()`.
- `void randomize()` — Seeds the generator from the OS entropy source.
- `void set_seed(value: int)` — Sets the seed.
- `int get_seed()` — Returns the current seed.

## Signals
- (No signals; `RandomNumberGenerator` is a passive generator.)

## Description
`RandomNumberGenerator` is Godot's go-to RNG. Create one with `RandomNumberGenerator.new()`, optionally call `randomize()` to seed it from the OS, and then pull values with `randi`, `randf`, and their ranged variants. The class inherits [[RefCounted]], so it is stateful and reference-counted — each instance has its own stream, useful when you want independent, reproducible sequences (one for the deck shuffle, one for AI behaviour, one for audio pitch jitter).

Setting `seed` (or constructing two generators with the same seed) produces identical sequences, which is invaluable for debugging "how did that bug happen?" replays: store the seed alongside the match log and you can rerun the exact shuffle. `randi_range(1, 6)` simulates a die, `randf_range(0.0, 1.0)` a coin flip, and `rand_weighted` picks an index from an [[Array]] of weights (handy for AI move selection). The global `randi()`/`randf()` functions are backed by a shared generator; for per-instance control prefer a dedicated `RandomNumberGenerator`.

In the Kadi app the deck shuffle in `l_01_03_deck_builder` uses a `RandomNumberGenerator` whose seed is stored in the match log so a hand can be replayed identically. The AI's "pick a card to play" uses `rand_weighted` over candidate cards (an [[Array]] of [[Resource]] card data) weighted by heuristic score, giving believable but non-deterministic moves. A tiny per-card pitch jitter on the deal sound (`randf_range(0.95, 1.05)` into an [[AudioStreamPlayer]]'s `pitch_scale`) makes repeated whooshes feel organic instead of mechanical.
