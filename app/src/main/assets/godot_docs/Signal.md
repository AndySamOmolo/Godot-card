---
class: Signal
inherits: RefCounted
verified: 4.3
short: A first-class reference to a signal of an Object, connectable to Callables and emittable directly.
---

## Properties
- `object: Object` — (Read via `get_object()`.) The object this signal belongs to.
- `name: StringName` — (Read via `get_name()`.) The signal's name.
- `object_id: int` — (Read via `get_object_id()`.) The owning object's id (0 if none).
- `is_null: bool` — (Read via `is_null()`.) True if this Signal refers to nothing.

## Methods
- `Error connect(callable: Callable, flags: int = 0)` — Connects `callable` to fire when this signal is emitted.
- `void disconnect(callable: Callable)` — Removes a previously-made connection.
- `bool is_connected(callable: Callable)` — True if `callable` is connected to this signal.
- `Array get_connections()` — Returns the list of `Dictionary` connections.
- `bool has_connections()` — True if any Callable is connected.
- `void emit(varargs: Variant)` — Emits the signal, passing the varargs to connected Callables.
- `StringName get_name()` — Returns the signal name.
- `Object get_object()` — Returns the owning object, or `null`.
- `int get_object_id()` — Returns the owning object's id (0 if none).
- `bool is_null()` — True if this Signal is empty.
- `static Signal from(...)` — (Editor-only) constructs a Signal from object+name for tooling.

## Signals
- (A `Signal` itself is not an emitter of further signals; it represents one.)

## Description
`Signal` is Godot's first-class reference to an [[Object]]'s signal — the value you get when you write `button.pressed` in GDScript. You `connect(callable)` to subscribe, `emit(...)` to fire it manually, and `disconnect(callable)` or `is_connected(callable)` to manage subscriptions. The `flags` argument to `connect` supports `CONNECT_DEFERRED` (fire at end of frame), `CONNECT_PERSIST` (saved with the scene), `CONNECT_ONE_SHOT` (auto-disconnects after one fire), and `CONNECT_REFERENCE_COUNTED` (allow the same callable to be connected multiple times).

Signals decouple the emitter from the listener: a [[Button]] emits `pressed` knowing nothing about what happens next, and a receiver reacts without polling. This is the cleanest way to broadcast game events — a turn changed, a card played, a penalty applied — to any number of interested nodes. `emit` lets code trigger a signal just like the engine would, which is useful for testing and for reusing signal-driven logic.

In the Kadi app [[Signals]] are the spine of the turn structure from `l_03_01_signals`. The `GameState` autoload declares `signal turn_changed(player_id)`, `signal card_played(card)`, `signal penalty_resolved(total)`, and `signal niko_kadi(player_id)`. The UI connects to `turn_changed` to refresh the highlighted player, to `penalty_resolved` to update the red penalty [[Label]], and to `niko_kadi` to trigger the celebratory animation. `CONNECT_ONE_SHOT` is used on a "deal complete" signal so the dealer initialisation runs exactly once, then un-wires itself.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Signal`)**: When configuring `Signal` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Signal` offline. If your `Signal` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Signal in Godot 4.3 Kadi
func configure_signal_example(target: Signal) -> void:
    assert(target != null, "Signal reference must be valid")
    print("Configured Signal for Kadi table runtime")
```

