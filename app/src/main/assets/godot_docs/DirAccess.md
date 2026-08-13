---
class: DirAccess
inherits: RefCounted
verified: 4.3
short: A static class for listing, creating, copying, and removing directories.
---

## Properties
- `current_dir: String` — (Read via `get_current_dir()`.) The directory this DirAccess is anchored to.

## Methods
- `static DirAccess open(path: String)` — Opens `path` for navigation; returns `null` on failure.
- `static Error make_dir(path: String)` — Creates a directory (parent must exist).
- `static Error make_dir_recursive(path: String)` — Creates a directory and any missing parents.
- `static Error make_dir_absolute(path: String)` — Like `make_dir` but for an OS-absolute path.
- `static bool dir_exists(path: String)` — True if the directory exists.
- `static bool file_exists(path: String)` — True if the file exists.
- `static Error copy(from: String, to: String)` — Copies a file.
- `static Error rename(from: String, to: String)` — Renames or moves a file or directory.
- `static Error remove(path: String)` — Removes an empty directory or a file.
- `static Error remove_absolute(path: String)` — Removes an OS-absolute path.
- `String get_current_dir()` — Returns the directory this accessor is open on.
- `String get_current_dir_without_drive()` — Like `get_current_dir` but omits the drive prefix (Windows).
- `Error list_dir_begin()` — Starts iterating the current directory.
- `String get_next()` — Returns the next entry name, or `""` when finished.
- `bool current_is_dir()` — True if the current entry is a directory.
- `void list_dir_end()` — Finishes iteration.
- `int get_space_left()` — Returns free space in bytes on the current drive.
- `static bool is_bundle(string)` — (Internal helper.)
- `static String get_filesystem_type()` — Returns the filesystem type as a String.
- `static String get_user_data_dir()` — Returns the absolute path of `user://`.
- `static String get_os_user_dir()` — Returns the OS user home directory.

## Signals
- (No signals; `DirAccess` is a static utility class.)

## Description
`DirAccess` complements [[FileAccess]] with directory-level operations: creating and deleting folders, listing their contents, copying and renaming entries, and checking existence. Like `FileAccess`, it is reference-counted and obtained through `DirAccess.open(path)`. Listing a directory is a begin/get_next/end dance: call `list_dir_begin()`, then repeatedly `get_next()` until it returns an empty String, and finally `list_dir_end()`.

`get_next` returns both files and directories (including the special `.` and `..` entries, which you usually skip), and `current_is_dir()` tells you which. `make_dir_recursive` is the safest way to create a nested path; `get_user_data_dir()` resolves the writable `user://` location to an OS-absolute path, which varies per platform — essential for an Android app where the path is buried in the sandbox.

In the Kadi app `DirAccess` is used to ensure `user://` subdirectories exist before writing — `make_dir_recursive("user://matches")` before saving a replay, `make_dir_recursive("user://exports")` before writing a CSV log. Listing `res://assets/godot_docs/` with `list_dir_begin`/`get_next` is exactly how the in-app docs viewer discovers these class files at runtime: it enumerates `*.md`, sorts them, and builds the docs index dynamically so adding a new doc requires no code change.
