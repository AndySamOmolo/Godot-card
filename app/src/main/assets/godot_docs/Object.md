---
class: Object
inherits:
verified: 4.3
short: The root class of almost every Godot type; provides properties, methods, signals, and metadata.
---

## Properties
- `script: Script` — The [[GDScript]] (or other script) attached to this object.
- `meta: Dictionary` — Read-only view of all metadata set with `set_meta`.

## Methods
- `Variant get(property: StringName)` — Returns the value of a property by name.
- `void set(property: StringName, value: Variant)` — Sets the value of a property by name.
- `Variant call(method: StringName, ...)` — Calls a method by name with the given args.
- `Variant call_deferred(method: StringName, ...)` — Calls a method at the end of the frame, on the main thread.
- `bool has_method(method: StringName)` — True if the object implements this method.
- `bool has_signal(signal: StringName)` — True if the object declares or inherits this signal.
- `Error connect(signal: StringName, callable: Callable, flags: int = 0)` — Connects a signal to a [[Callable]].
- `void emit_signal(signal: StringName, ...)` — Emits a signal with the given args.
- `void disconnect(signal: StringName, callable: Callable)` — Removes a previously made connection.
- `Array get_signal_list()` — Returns the signals this object can emit.
- `Array get_property_list()` — Returns the full list of properties including inherited ones.
- `Array get_method_list()` — Returns the object's methods.
- `void set_meta(name: StringName, value: Variant)` — Stores an arbitrary tagged value on the object.
- `Variant get_meta(name: StringName, default: Variant = null)` — Reads a meta value, or `default` if missing.
- `bool has_meta(name: StringName)` — True if the meta key exists.
- `void remove_meta(name: StringName)` — Deletes a meta entry.
- `String to_string()` — Returns a readable representation of the object.

## Signals
- `property_list_changed()` — Emitted when the object's property list changes.
- `script_changed()` — Emitted when the attached `script` changes.

## Description
`Object` is the universal ancestor of Godot's class hierarchy. [[Node]], [[Resource]], and every built-in type ultimately derive from it. It provides the dynamic features that make Godot flexible: properties you can read and write by name, methods you can invoke by name (`call`, `call_deferred`), [[Signals]] you can `connect` and `emit_signal`, and a free-form `meta` dictionary for tagging data onto any object.

Because `Object` itself does **not** use reference counting, instances created with `MyClass.new()` will leak unless you call `free()` on them. Reference-counted subclasses such as [[RefCounted]] and [[Resource]] free themselves automatically when their last reference goes away; [[Node]] is usually managed by the scene tree and freed with `queue_free()`.

In the Kadi project you rarely subclass `Object` directly. Instead you extend [[Node]] for things that live in the tree (the game table, the discard pile node) and [[Resource]] for pure data (a card's suit, rank, and effect). The `meta` API is occasionally handy for attaching debug info — for example tagging each card node with `set_meta("original_index", i)` so a reshuffle can restore order. The `connect`/`emit_signal` machinery underpins every [[Signals]]-based turn event the game broadcasts.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Object`)**: When configuring `Object` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Object` offline. If your `Object` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Object in Godot 4.3 Kadi
func configure_object_example(target: Object) -> void:
    assert(target != null, "Object reference must be valid")
    print("Configured Object for Kadi table runtime")
```

