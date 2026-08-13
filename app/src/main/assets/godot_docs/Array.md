---
class: Array
inherits: RefCounted
verified: 4.3
short: An ordered, dynamic, mutable list of values accessed by index.
---

## Properties
- `size: int` — (Read via `size()`.) Number of elements.
- `is_empty: bool` — (Read via `is_empty()`.) True when there are no elements.
- `is_read_only: bool` — (Read via `is_read_only()`.) True when the array is frozen.

## Methods
- `void append(value: Variant)` — Adds `value` to the end.
- `void append_array(array: Array)` — Concatenates another array onto the end.
- `void push_back(value: Variant)` — Same as `append`.
- `void push_front(value: Variant)` — Inserts at index 0.
- `Variant pop_back()` — Removes and returns the last element.
- `Variant pop_front()` — Removes and returns the first element.
- `Variant pop_at(position: int)` — Removes and returns the element at `position`.
- `void insert(position: int, value: Variant)` — Inserts `value` at `position`.
- `void erase(value: Variant)` — Removes the first element equal to `value`.
- `void fill(value: Variant)` — Sets every element to `value`.
- `void resize(size: int)` — Sets the length, padding with `null` or truncating.
- `void clear()` — Removes all elements.
- `bool has(value: Variant)` — True if `value` is present.
- `int count(value: Variant)` — Number of occurrences of `value`.
- `int find(value: Variant, from: int = 0)` — Returns the first index of `value`, or `-1`.
- `int rfind(value: Variant, from: int = -1)` — Returns the last index of `value`, or `-1`.
- `Array duplicate(deep: bool = false)` — Returns a shallow (or deep) copy.
- `Array slice(begin: int = 0, end: int = 2147483647, step: int = 1, deep: bool = false)` — Returns a sub-array.
- `void sort()` — Sorts in place (ascending, by default comparison).
- `void sort_custom(callable: Callable)` — Sorts using a [[Callable]] comparator returning `-1/0/1`.
- `Variant min()` — Returns the smallest element.
- `Variant max()` — Returns the largest element.
- `void shuffle()` — Randomly reorders the elements in place.
- `Array map(method: Callable)` — Returns a new array of `method(elem)` results.
- `Array filter(method: Callable)` — Returns a new array of elements for which `method(elem)` is true.
- `Variant reduce(method: Callable, accum: Variant = null)` — Folds the array through `method`.
- `bool any(method: Callable)` — True if `method(elem)` is true for any element.
- `bool all(method: Callable)` — True if `method(elem)` is true for every element.
- `void reverse()` — Reverses in place.

## Signals
- (No signals; `Array` is a plain data container.)

## Description
`Array` is Godot's workhorse ordered collection. It is dynamically typed by default (holds any mix of [[Variant]] types) and grows or shrinks as needed. Typed arrays `Array[CardData]` enforce one element type and give better performance and editor autocomplete. Elements are addressed by integer index, `array[0]` being the first; out-of-range reads return `null` and out-of-range writes resize the array.

Beyond add/remove, `Array` carries a rich functional toolkit: `map`, `filter`, `reduce`, `any`, `all`, plus `sort`/`sort_custom`, `shuffle`, `min`/`max`, `find`/`rfind`, `slice`, and `duplicate`. These let you express common operations in one line — perfect for shuffling a deck, finding the next player, or summing penalty values.

In the Kadi app `Array` is the universal container for ordered game data: the draw pile, the discard pile, and each player's hand are `Array[CardData]`. Building the deck in `l_01_03_deck_builder` uses nested loops and `append`; shuffling calls `shuffle()`; drawing is `pop_back()`; placing a card on the discard pile is `append`. The penalty accumulator from `l_05_01_accumulator` reduces the pile of pending penalties with `reduce`. The reshuffle in `l_10_01_reshuffle` slices off the top discard, `clear()`s the rest into the draw pile, and `shuffle()`s — all in a handful of lines.
