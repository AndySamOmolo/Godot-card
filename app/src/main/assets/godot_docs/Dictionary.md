---
class: Dictionary
inherits: RefCounted
verified: 4.3
short: An associative container mapping hashable keys to values, like a hash map.
---

## Properties
- `size: int` — (Read via `size()`.) Number of key/value pairs.
- `is_empty: bool` — (Read via `is_empty()`.) True when there are no entries.
- `is_read_only: bool` — (Read via `is_read_only()`.) True when frozen.

## Methods
- `Variant get(key: Variant, default: Variant = null)` — Returns the value for `key`, or `default` if missing.
- `void set(key: Variant, value: Variant)` — Sets (or inserts) `value` at `key`.
- `Variant get_or_add(key: Variant, default: Variant = null)` — Returns the value, inserting `default` if `key` is absent.
- `bool has(key: Variant)` — True if `key` exists.
- `bool has_all(keys: Array)` — True if every key in `keys` exists.
- `void erase(key: Variant)` — Removes the entry for `key`; returns whether it existed.
- `void merge(dictionary: Dictionary, overwrite: bool = false)` — Copies entries from `dictionary`, optionally overwriting existing keys.
- `Array keys()` — Returns a new array of all keys.
- `Array values()` — Returns a new array of all values.
- `Dictionary duplicate(deep: bool = false)` — Returns a shallow or deep copy.
- `void clear()` — Removes all entries.
- `int find_key(value: Variant)` — Returns a key whose value equals `value`, or `null`.
- `void make_read_only()` — Freezes the dictionary so it cannot be modified.
- `void recursive_equal(other: Dictionary, recursion_count: int)` — (Internal) deep-equality helper.

## Signals
- (No signals; `Dictionary` is a plain data container.)

## Description
`Dictionary` is Godot's hash map: any hashable key (String, int, [[Vector2]], [[Node]], etc.) maps to any [[Variant]] value. Insertion order is preserved in Godot 4, so `keys()` returns keys in the order they were added — handy when you want deterministic iteration for saving/loading. Lookup, insert, and delete are O(1) on average. Access a missing key with `[]` returns `null` (or inserts `null` on write); `get` lets you supply a non-null default instead.

`merge` combines two dictionaries, `has_all` checks a batch of keys, `find_key` does a reverse lookup, and `duplicate(true)` gives a deep copy. `make_read_only` freezes a dictionary so you can hand it around knowing it won't be mutated — useful for configuration tables loaded once at startup.

In the Kadi app `Dictionary` holds lookup tables and per-match state that isn't ordered. A `{Suit: Color}` mapping tells whether a card is red or black without a `match` statement; a `{player_id: Array}` holds transient per-player data; the settings JSON is a `Dictionary` serialised via [[JSON]]. The win-detection conditions in `l_09_01_win_detection` can be expressed as a `Dictionary` of bool flags checked with `has_all`. Because insertion order is preserved, the play-log `Dictionary` of turn-number-to-description renders deterministically.
