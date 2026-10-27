---
class: Callable
inherits: RefCounted
verified: 4.3
short: A first-class reference to a method or function, callable later with its arguments.
---

## Properties
- `object: Object` — (Read via `get_object()`.) The object bound to this Callable, or `null`.
- `method: StringName` — (Read via `get_method()`.) The method name.
- `object_id: int` — (Read via `get_object_id()`.) The RID-like id of the bound object.
- `is_valid: bool` — (Read via `is_valid()`.) True if the target still exists.
- `is_standard: bool` — (Read via `is_standard()`.) True if this wraps a built-in method.

## Methods
- `Variant call(arg0: Variant = null, ...)` — Invokes the callable with up to 10 args; returns its result.
- `Variant callv(arguments: Array)` — Invokes with an array of arguments.
- `Variant call_deferred(arg0: Variant = null, ...)` — Invokes at the end of the frame on the main thread.
- `Callable bind(arg0: Variant = null, ...)` — Returns a new Callable with these args pre-bound to the front.
- `Callable bindv(arguments: Array)` — Returns a new Callable with an array of pre-bound args.
- `Callable unbind(argcount: int)` — Returns a Callable that ignores `argcount` trailing args when called.
- `Object get_object()` — Returns the bound object, or `null`.
- `int get_object_id()` — Returns the id of the bound object (0 if none).
- `StringName get_method()` — Returns the method name.
- `int get_argument_count()` — Returns the number of bound arguments.
- `Array get_bound_arguments()` — Returns the bound arguments as an Array.
- `bool is_valid()` — True if the callable points to an existing object and method.
- `bool is_null()` — True if the callable is unset.
- `bool is_standard()` — True for built-in method callables.
- `bool is_custom()` — True for script-function callables (lambdas and `Callable` from `func`).
- `Callable rpc_id(peer_id: int, ...)` — Calls over the network to `peer_id` (multiplayer).
- `static Callable from_object_method(object: Object, method: StringName)` — Builds a Callable from `object.method`.
- `static Callable from_scene_path(path: String, method: StringName)` — (Editor-only) helper.

## Signals
- (No signals; `Callable` is a function reference, not an emitter.)

## Description
`Callable` is Godot's first-class function reference: it bundles an object and a method (or just a script function) into a value you can pass around and invoke later. You build one implicitly whenever you connect a signal — `button.pressed.connect(self._on_pressed)` — or explicitly with `Callable(self, "_on_pressed")` and lambdas `Callable(func(): print("hi"))`. Call with `call(...)` or `callv(args)`, or use `call_deferred` to run it on the main thread at the end of the frame.

`bind` returns a new Callable with arguments pre-filled on the left, so a generic handler can be specialised for one button without writing a separate function. This is the cleanest way to wire many buttons to one method: `button.pressed.connect(play_card.bind(card_data))`. `unbind` is the reverse — useful when an emitter passes args you want to ignore. Callables are reference-counted and survive even if you store them, but `is_valid()` lets you check that a bound [[Object]] still exists before calling.

In the Kadi app `Callable` is the connective tissue for [[Signals]]. The declaration popup's four [[Button]]s each `connect` their `pressed` signal to `declare_suit.bind(suit)` so one method handles all four. The AI's move function is passed as a [[Callable]] to a [[Timer]]'s `timeout`, letting the same timer schedule different actions. The `Array` functional helpers (`map`, `filter`, `sort_custom`, `reduce`) take `Callable`s, so `hand.filter(func(c): return is_playable(c))` reads almost like English.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Callable`)**: When configuring `Callable` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Callable` offline. If your `Callable` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using Callable in Godot 4.3 Kadi
func configure_callable_example(target: Callable) -> void:
    assert(target != null, "Callable reference must be valid")
    print("Configured Callable for Kadi table runtime")
```

