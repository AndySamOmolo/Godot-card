---
class: ResourceSaver
inherits: Object
verified: 4.3
short: A singleton for saving resources to disk as .tres or .res files.
---

## Properties
- ` saver_count: int` — (Internal) number of registered savers.

## Methods
- `static Error save(resource: Resource, path: String, flags: int = 0)` — Saves `resource` to `path` (extension decides `.tres` text or `.res` binary).
- `static Resource save_unique(resource: Resource)` — Generates a unique path in the cache and saves there, returning the resource.
- `static bool is_bundle(string)` — (Internal helper.)
- `static PackedStringArray get_recognized_extensions(resource: Resource)` — Returns the file extensions this resource can be saved as.
- `static void add_resource_format_saver(format_saver: ResourceFormatSaver, at_front: bool = false)` — Registers a custom saver.
- `static void remove_resource_format_saver(format_saver: ResourceFormatSaver)` — Unregisters a custom saver.
- `static void set_save_callbacks(callbacks: ResourceSaveCallbacks)` — Sets custom save-time callbacks (e.g. for path remapping).

## Signals
- (No signals; `ResourceSaver` is a static utility.)

## Description
`ResourceSaver` is the inverse of [[ResourceLoader]]: it serialises a [[Resource]] to disk. The file extension determines the format: `.tres` is human-readable text (great for version control and small files), `.res` is binary (faster to load, smaller for large data). Pass `ResourceSaver.FLAG_COMPRESS` (or other flags) to `save` for additional control such as compression or replacing sub-resources in place.

Because resources are reference-counted and shared, saving one writes its own data plus the data of any unique sub-resources it owns; resources shared with others are written by reference path rather than duplicated. This keeps `.tres` files compact and consistent. Custom [[Resource]] scripts (like the Kadi card data) are saved with `class_name` intact, so the file round-trips through the editor with full type information.

In the Kadi app `ResourceSaver` is mostly a development-time tool: the 52-card deck is built once by a script and `save`d to `res://data/cards/<suit>_<rank>.tres`, so artists and designers can tweak individual cards as text files without rerunning the deck-builder. The match-replay format (a custom `MatchRecord` resource) is saved to `user://matches/match_<timestamp>.res` with `FLAG_COMPRESS` for compact storage, then loaded back with [[ResourceLoader]] `load` to replay a finished game step by step.
