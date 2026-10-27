---
class: JSON
inherits: RefCounted
verified: 4.3
short: A utility class for parsing JSON text into native Variant data and back.
---

## Properties
- `parsed: Variant` — (Legacy, via `get_data()`.) The result of the last `parse`.
- `error: Error` — (Read via `get_error()`.) `OK` if the last parse succeeded.
- `error_line: int` — (Read via `get_error_line()`.) The 1-based line of a parse error.
- `error_string: String` — (Read via `get_error_message()`.) Human-readable error text.

## Methods
- `Error parse(json_string: String, keep_text: bool = false)` — Parses `json_string` into `parsed`; returns `OK` on success.
- `Variant get_data()` — Returns the parsed value (`Dictionary`, `Array`, or scalar) from the last `parse`.
- `Error get_error()` — Returns the error code from the last parse.
- `int get_error_line()` — Returns the error line, or `-1`.
- `String get_error_message()` — Returns the error message, or an empty string.
- `String stringify(value: Variant, indent: String = "", sort_keys: bool = true, full_precision: bool = false)` — Serialises a Variant to JSON text, optionally indented.
- `Variant parse_string(json_string: String)` — Static helper: parse and return the value directly, or `null` on failure.
- `Variant from_native(variant: Variant, full_precision: bool = false, allow_inf: bool = false)` — Static helper: convert a Variant to a JSON-compatible form.
- `Variant to_native(json: Variant, allow_objects: bool = false)` — Static helper: convert JSON data back into richer Variants.

## Signals
- (No signals; `JSON` is a passive parser.)

## Description
`JSON` is Godot's bridge between text and structured data. `parse(json_string)` reads JSON into a [[Variant]] — typically a [[Dictionary]] for objects or an [[Array]] for lists — and you then read the result with `get_data()` or check `get_error()`/`get_error_message()` for failures. `stringify(value)` goes the other way, turning a Variant into JSON text, with `indent` for pretty-printing and `sort_keys` for stable output. For one-liners, the static `parse_string(s)` returns the value directly.

The parser handles the standard JSON types: null, bool, int, float, String, Array, and object (Dictionary). Godot-specific types like [[Vector2]] or [[Node]] are not valid JSON, so use `from_native`/`to_native` if you need to round-trip them with custom encoding. Floating point is preserved with `full_precision = true` to avoid precision loss.

In the Kadi app `JSON` persists the learner's progress and settings: a `Dictionary` of completed phase ids is `stringify`d and written to `user://progress.json` via [[FileAccess]], then `parse_string`d back on launch. The tutorial phase files themselves (these very `phase_XX.json` documents) are loaded with `JSON.parse_string` and turned into `Dictionary`/`Array` objects the UI renders. On parse failure, `get_error_line()` points the author at the typo, which is why strict, well-formed JSON — no trailing commas, double-quoted strings — matters so much here.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`JSON`)**: When configuring `JSON` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `JSON` offline. If your `JSON` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using JSON in Godot 4.3 Kadi
func configure_json_example(target: JSON) -> void:
    assert(target != null, "JSON reference must be valid")
    print("Configured JSON for Kadi table runtime")
```

