---
class: OS
inherits: Object
verified: 4.3
short: An autoload singleton exposing the operating system, device, and process environment.
---

## Methods
- `String get_name()` — Returns the host OS name, such as "Android", "Windows", or "macOS".
- `String get_model_name()` — Returns the device model name where available.
- `bool has_feature(tag_name: String)` — True if the build/platform provides the given feature tag.
- `int get_processor_count()` — Returns the number of logical CPU cores.
- `Array[String] get_cmdline_args()` — Returns the command-line arguments passed to the executable.
- `String get_environment(variable: String)` — Returns the value of an environment variable.
- `Error shell_open(uri: String)` — Opens a URI (web page, file) with the system's default handler.
- `void alert(text: String, title: String = "Alert!")` — Shows a blocking native alert dialog.
- `String get_unique_id()` — Returns a unique device identifier where permitted.
- `int get_static_memory_usage()` — Returns the engine's current static memory usage in bytes.
- `void set_low_processor_usage_mode(enable: bool)` — Enables a mode that reduces CPU usage when idle.

## Description
`OS` is an autoload singleton (accessed globally as `OS`), providing a portable window onto the platform your game runs on. You never create it; you call its methods to query the operating system name, hardware details, environment variables, and command-line arguments, or to trigger platform actions like opening a URL with `shell_open`.

`has_feature` is the standard way to branch on platform or build type — for example checking for `"mobile"`, `"android"`, or `"debug"` — so the same project can adapt its behaviour per device. Note that many methods return limited or empty data on sandboxed platforms for privacy reasons.

In the Kadi app `OS.has_feature("mobile")` switches the layout to touch-friendly card sizes and enlarges the "Kadi!" button, while `get_name` tailors the back-button behaviour for Android. The "Rate this game" and "Report a bug" menu entries use `shell_open` to launch the store page and an email link in the system browser.
