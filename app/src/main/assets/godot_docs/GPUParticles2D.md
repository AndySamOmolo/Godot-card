---
class: GPUParticles2D
inherits: Node2D
verified: 4.3
short: A 2D particle emitter simulated on the GPU for large, cheap bursts and continuous effects.
---

## Properties
- `emitting: bool` — When true, the system emits particles; set false to stop.
- `amount: int` — Number of particles allocated for the system.
- `process_material: Material` — The `ParticleProcessMaterial` (or shader) that drives particle behaviour.
- `texture: Texture2D` — The [[Texture2D]] drawn for each particle.
- `lifetime: float` — How long each particle lives, in seconds.
- `one_shot: bool` — When true, emits a single burst instead of looping.
- `preprocess: float` — Seconds of simulation to run before the first frame is shown.
- `speed_scale: float` — Multiplier on simulation speed.
- `explosiveness: float` — 0 spreads emission over lifetime; 1 emits all particles at once.
- `local_coords: bool` — When true, particles follow the node; when false, they stay in world space.

## Methods
- `void restart()` — Restarts the emission from the beginning.
- `void set_emitting(emitting: bool)` — Starts or stops emission.
- `Rect2 capture_rect()` — Returns a rectangle enclosing the currently visible particles.
- `void set_amount(amount: int)` — Sets the number of allocated particles.
- `void convert_from_particles(particles: Node)` — Copies configuration from another particle node.

## Signals
- `finished()` — Emitted when a `one_shot` (or a stopped looping) system finishes emitting.

## Description
`GPUParticles2D` is a [[Node2D]] that offloads particle simulation to the graphics card, letting you render thousands of particles cheaply. Its behaviour is defined almost entirely by a `ParticleProcessMaterial` assigned to `process_material` — direction, gravity, color ramps, and scale over life — while `texture` supplies each particle's image.

Because the simulation runs on the GPU, per-particle logic cannot be read back on the CPU; for that you would use [[CPUParticles2D]] instead. Use `explosiveness` for bursts, `one_shot` with the `finished` signal for fire-and-forget effects, and `preprocess` to start mid-effect.

In the Kadi app `GPUParticles2D` provides the flashy feedback: a golden sparkle burst when a player shouts "Kadi!", a puff of dust as the deck is shuffled, and a cascade of confetti on victory. Each is a `one_shot` emitter whose `finished` signal frees the effect node once the animation completes.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`GPUParticles2D`)**: When configuring `GPUParticles2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `GPUParticles2D` offline. If your `GPUParticles2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using GPUParticles2D in Godot 4.3 Kadi
func configure_gpuparticles2d_example(target: GPUParticles2D) -> void:
    assert(target != null, "GPUParticles2D reference must be valid")
    print("Configured GPUParticles2D for Kadi table runtime")
```

