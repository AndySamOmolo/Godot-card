---
class: Time
inherits: Object
verified: 4.3
short: An autoload singleton providing clocks, timestamps, and date/time conversion helpers.
---

## Methods
- `int get_ticks_msec()` — Returns milliseconds elapsed since the engine started.
- `int get_ticks_usec()` — Returns microseconds elapsed since the engine started.
- `int get_unix_time_from_system()` — Returns the current Unix timestamp from the system clock.
- `Dictionary get_datetime_dict_from_system(utc: bool = false)` — Returns the current date and time as a [[Dictionary]].
- `Dictionary get_time_dict_from_system(utc: bool = false)` — Returns the current time (hour/minute/second) as a dictionary.
- `String get_datetime_string_from_system(utc: bool = false, use_space: bool = false)` — Returns an ISO 8601 date-time string.
- `String get_time_string_from_system(utc: bool = false)` — Returns the current time as an `HH:MM:SS` string.
- `Dictionary get_datetime_dict_from_unix_time(unix_time_val: int)` — Converts a Unix timestamp to a date-time dictionary.
- `int get_unix_time_from_datetime_dict(datetime: Dictionary)` — Converts a date-time dictionary to a Unix timestamp.
- `String get_datetime_string_from_unix_time(unix_time_val: int, use_space: bool = false)` — Formats a Unix timestamp as a string.

## Description
`Time` is an autoload singleton (accessed globally as `Time`), so you never instantiate it — you call its methods directly from anywhere. It groups two kinds of clocks: high-resolution engine timers (`get_ticks_msec`, `get_ticks_usec`) for measuring durations, and wall-clock helpers that read the system's real date and time.

It also converts freely between representations: Unix timestamps, `HH:MM:SS` strings, and [[Dictionary]] forms with `year`, `month`, `hour`, and so on. For scheduling gameplay events on the scene tree you would still use a [[Timer]] node; `Time` is for measurement and formatting rather than firing callbacks.

In the Kadi app `Time` stamps every saved game and high-score entry with `get_datetime_string_from_system`, and `get_ticks_msec` measures how long a learner takes to make each move so the tutorial can gently speed up hints for confident players and slow down for those who hesitate.
