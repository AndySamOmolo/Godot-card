---
class: RefCounted
inherits: Object
verified: 4.3
short: An Object that frees itself automatically once no references to it remain.
---

## Properties
- `reference_count: int` — Read-only count of strong references currently held.

## Methods
- `bool init_ref()` — Initialises reference counting and returns whether this is the first reference.
- `bool reference()` — Increments the reference count; returns whether the object is still alive.
- `bool unreference()` — Decrements the count; the object is freed when it reaches zero.
- `Variant _get_configuration_warning()` — Virtual method returning a warning string shown in the editor.

## Signals
- `_ref_count_changed()` — Internal hint emitted when the reference count changes (rarely used directly).

## Description
`RefCounted` is the base class for objects that manage their own lifetime through reference counting. Unlike a plain [[Object]], you create one with `MyClass.new()` and never need to call `free()`: as long as at least one variable, array entry, or property holds a reference the object stays alive; the moment the last reference is dropped, Godot deletes it automatically. This makes it safe to return `RefCounted` objects from functions and store them in collections.

Most data types in Godot — [[Resource]], [[Dictionary]], [[Array]], [[Callable]], [[Tween]] — derive from `RefCounted`, so memory management for game data is effectively invisible. The trade-off is that `RefCounted` objects cannot be added to the scene tree as nodes; if you need tree membership, use [[Node]] instead.

In the Kadi project you usually extend `RefCounted` only when you need a lightweight, non-node helper that should clean up after itself. The deck shuffler, a `Move` value object representing one play, or a `JumpChain` resolver built during `l_07_01_chain_resolution` are good candidates. The custom [[Resource]] used for card data inherits `RefCounted` indirectly, which is why passing a card from the draw pile into a player's hand never leaks — Godot's refcounting hands ownership over for free.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`RefCounted`)**: When configuring `RefCounted` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `RefCounted` offline. If your `RefCounted` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using RefCounted in Godot 4.3 Kadi
func configure_refcounted_example(target: RefCounted) -> void:
    assert(target != null, "RefCounted reference must be valid")
    print("Configured RefCounted for Kadi table runtime")
```

