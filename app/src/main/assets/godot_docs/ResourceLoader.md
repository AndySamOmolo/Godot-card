---
class: ResourceLoader
inherits: Object
verified: 4.3
short: A singleton for loading resources at runtime, optionally in the background thread.
---

## Properties
- `waiter: Mutex` — (Internal) used by the loader's thread-queuing subsystem.

## Methods
- `static Resource load(path: String, type_hint: String = "", cache_mode: int = 1)` — Loads and returns a [[Resource]] from `path`, or `null` on failure.
- `static Resource load_threaded_request(path: String, type_hint: String = "", cache_mode: int = 1)` — Queues `path` for background loading; returns `OK`.
- `static int load_threaded_get_status(path: String, progress: Array = [])` — Returns `THREAD_LOAD_*` (e.g. `THREAD_LOAD_IN_PROGRESS`), filling `progress` with a 0..1 float.
- `static Resource load_threaded_get(path: String)` — Returns the resource loaded by an earlier `load_threaded_request` (blocks if not done).
- `static bool has(path: String)` — True if `path` is cached or loadable.
- `static bool exists(path: String, type_hint: String = "")` — Like `has` but also accepts a type filter.
- `static Array get_dependencies(path: String)` — Returns the list of dependencies of `path` without loading it.
- `static bool is_known_loader_load(path: String)` — (Internal) loader-registry check.
- `static PackedStringArray list_directory(path: String)` — (Editor-only) lists resources in a directory.
- `static void add_resource_format_loader(format_loader: ResourceFormatLoader, at_front: bool = false)` — Registers a custom loader.
- `static void remove_resource_format_loader(format_loader: ResourceFormatLoader)` — Unregisters a custom loader.
- `static void add_resource_format_saver(format_saver: ResourceFormatSaver, at_front: bool = false)` — Registers a custom saver.
- `static void remove_resource_format_saver(format_saver: ResourceFormatSaver)` — Unregisters a custom saver.

## Signals
- (No signals; `ResourceLoader` is a static utility.)

## Description
`ResourceLoader` is the runtime side of Godot's resource system. `load(path)` synchronously loads and returns a [[Resource]] (cached by default, so subsequent loads of the same path return the same instance). The `preload("path")` statement is the compile-time equivalent — it loads at parse time and bakes the resource into the script. Use `load` for paths known only at runtime, `preload` for fixed paths.

`load_threaded_request` / `load_threaded_get_status` / `load_threaded_get` is the background-loading trio: request a load, poll its progress (a 0..1 float), and finally fetch the result on the main thread. This keeps loading large assets off the main loop so the UI stays responsive — essential on mobile. `has` and `get_dependencies` let you check or inspect a resource without committing to a full load.

In the Kadi app `ResourceLoader.load` fetches each card's [[Texture2D]] from `res://art/cards/<suit>_<rank>.png` lazily the first time that card is dealt, and the card-back `Texture2D` is `preload`d because it is always needed. The declaration popup's `PackedScene` and the four suit icon [[Resource]]s are background-loaded with `load_threaded_request` on the title screen, so by the time the learner reaches a declaration the assets are ready and the popup opens instantly.
