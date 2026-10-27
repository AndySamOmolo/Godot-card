---
class: FileAccess
inherits: RefCounted
verified: 4.3
short: A static class for reading from and writing to files on disk.
---

## Properties
- `path: String` — (Read via `get_path()`.) The path this FileAccess was opened for.
- `path_absolute: String` — (Read via `get_path_absolute()`.) The OS-absolute path.
- `eof_reached: bool` — (Read via `eof_reached()`.) True once the read cursor is past the end.
- `position: int` — (Read/write via `get_position()`/`seek()`.) The read/write cursor in bytes.
- `length: int` — (Read via `get_length()`.) File size in bytes.

## Methods
- `static FileAccess open(path: String, flags: ModeFlags)` — Opens `path` with READ/WRITE/APPEND/etc.; returns `null` on failure.
- `static FileAccess open_encrypted(path: String, mode: ModeFlags, key: PackedByteArray)` — Opens an AES-encrypted file.
- `static FileAccess open_encrypted_with_pass(path: String, mode: ModeFlags, pass: String)` — Opens an encrypted file using a password.
- `static FileAccess open_compressed(path: String, mode: ModeFlags, compression: CompressionMode = 0)` — Opens a compressed file.
- `String get_as_text(skip_cr: bool = false)` — Reads the whole file as a String.
- `PackedByteArray get_buffer(length: int)` — Reads `length` bytes.
- `String get_line()` — Reads one line up to `\n`.
- `String get_csv_line(delimiter: String = ",")` — Reads a CSV line into a String (use `get_csv_line` from a `PackedStringArray` variant).
- `Variant get_var(allow_objects: bool = false)` — Reads a Variant previously written by `store_var`.
- `String get_16()` / `get_32()` / `get_64()` — Reads an integer of the given byte width.
- `float get_float()` / `double get_double()` — Reads a float or double.
- `void store_string(string: String)` — Writes a String.
- `void store_buffer(buffer: PackedByteArray)` — Writes raw bytes.
- `void store_line(line: String)` — Writes a String followed by a newline.
- `void store_csv_line(values: PackedStringArray, delim: String = ",")` — Writes a CSV line.
- `void store_var(value: Variant, full_objects: bool = false)` — Writes a Variant in binary form.
- `void seek(position: int)` — Moves the cursor to `position`.
- `void seek_end(position: int = 0)` — Moves the cursor to `position` bytes from the end.
- `bool eof_reached()` — True when the cursor has passed the end of file.
- `int get_length()` — Returns the file size in bytes.
- `void flush()` — Flushes pending writes to disk.
- `void close()` — Closes the file.
- `static bool file_exists(path: String)` — True if the file exists.
- `static Error get_open_error()` — Returns the error from the last `open`.
- `static String get_md5(path: String)` — Returns the file's MD5 hash.
- `static PackedByteArray get_file_as_bytes(path: String)` — Reads an entire file as bytes (errors via exception).
- `static String get_file_as_string(path: String)` — Reads an entire file as a String.
- `static Error rename(from: String, to: String)` — Renames or moves a file.
- `static Error remove(path: String)` — Deletes a file.

## Signals
- (No signals; `FileAccess` is a static utility class.)

## Description
`FileAccess` is Godot's file I/O API. You obtain a handle with `FileAccess.open(path, flags)` and then read with `get_*` methods or write with `store_*` methods; the handle is reference-counted, so closing is automatic when it goes out of scope (or call `close()` explicitly). Paths use `res://` (read-only at runtime) for shipped assets and `user://` (writable) for save data and caches. Use [[ProjectSettings]] `globalize_path` to convert these to OS-absolute paths when needed.

For text, `get_as_text`, `get_line`, and `store_string` cover the common cases; for binary, `get_buffer`/`store_buffer` and the typed `get_16/32/64` and `get_float/double` accessors read fixed-width primitives; `get_var`/`store_var` round-trip any [[Variant]]. Convenience helpers like `get_file_as_string` and `get_file_as_bytes` collapse the open/read/close dance into one call, and `file_exists`, `rename`, and `remove` are the file-management primitives.

In the Kadi app `FileAccess` reads the tutorial phase JSON from `res://assets/tutorial/` (shipped with the app, read-only) and writes the learner's progress and high scores to `user://progress.json` and `user://scores.json` (writable). `open_encrypted_with_pass` is used if the developer wants to obscure progress data, and `get_md5` lets the app detect if the shipped glossary or docs have changed between versions so it can refresh caches. `store_csv_line` could export a match log for sharing.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`FileAccess`)**: When configuring `FileAccess` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `FileAccess` offline. If your `FileAccess` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using FileAccess in Godot 4.3 Kadi
func configure_fileaccess_example(target: FileAccess) -> void:
    assert(target != null, "FileAccess reference must be valid")
    print("Configured FileAccess for Kadi table runtime")
```

