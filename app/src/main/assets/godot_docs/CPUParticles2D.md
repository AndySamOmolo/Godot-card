---
class: CPUParticles2D
inherits: Node2D
verified: 4.3
short: A 2D particle emitter simulated on the CPU, configurable entirely from properties without a shader.
---

## Properties
- `emitting: bool` — When true, the system emits particles; set false to stop.
- `amount: int` — Number of particles in the system.
- `lifetime: float` — How long each particle lives, in seconds.
- `one_shot: bool` — When true, emits a single burst instead of looping.
- `texture: Texture2D` — The [[Texture2D]] drawn for each particle.
- `explosiveness: float` — 0 spreads emission over lifetime; 1 emits all particles at once.
- `direction: Vector2` — Base direction in which particles are launched.
- `spread: float` — Angular spread, in degrees, around `direction`.
- `gravity: Vector2` — Constant acceleration applied to every particle.
- `color: Color` — Base color multiplied into each particle.

## Methods
- `void restart()` — Restarts the emission from the beginning.
- `void set_emitting(emitting: bool)` — Starts or stops emission.
- `void convert_from_particles(particles: Node)` — Copies configuration from a [[GPUParticles2D]] node.
- `void set_amount(amount: int)` — Sets the number of particles.
- `void set_color(color: Color)` — Sets the base particle `color`.

## Signals
- `finished()` — Emitted when a `one_shot` (or a stopped looping) system finishes emitting.

## Description
`CPUParticles2D` is a [[Node2D]] that simulates particles on the CPU and exposes every setting directly as a property — direction, spread, gravity, scale curves, and color ramps — so you can build effects without writing a particle shader. It mirrors [[GPUParticles2D]] but trades raw throughput for portability and easy tweaking.

It is the safer choice on lower-end hardware or platforms with limited GPU particle support, and because the work happens on the CPU it stays predictable across devices. Use `one_shot` with the `finished` signal for one-off bursts and `explosiveness` to control whether particles trickle out or explode all at once.

In the Kadi app `CPUParticles2D` handles the subtle, always-on flourishes that must look identical on every Android device: a gentle shimmer beneath the active player's cards and a small tap-spark when a card is legally played. Because it runs on the CPU, it behaves consistently even on budget phones where GPU particles might be throttled.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`CPUParticles2D`)**: When configuring `CPUParticles2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `CPUParticles2D` offline. If your `CPUParticles2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using CPUParticles2D in Godot 4.3 Kadi
func configure_cpuparticles2d_example(target: CPUParticles2D) -> void:
    assert(target != null, "CPUParticles2D reference must be valid")
    print("Configured CPUParticles2D for Kadi table runtime")
```

