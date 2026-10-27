---
class: ProgressBar
inherits: Range
verified: 4.3
short: A control that visualises progress from min to max as a filled bar.
---

## Properties
- `progress: float` — The current value (inherited from [[Range]]).
- `min_value: float` — The minimum value (inherited from [[Range]]).
- `max_value: float` — The maximum value (inherited from [[Range]]).
- `step: float` — Increment granularity (inherited from [[Range]]).
- `show_percentage: bool` — When true, the bar shows a percentage label.
- `percentage: float` — Read-only value from 0 to 1.
- `indeterminate: bool` — When true, the bar animates as a busy indicator with no fixed value.
- `editor_preview_indeterminate: bool` — Whether the editor preview plays the indeterminate animation.
- `fill_color: Color` — (Theme) colour of the filled portion; set via override.
- `background_color: Color` — (Theme) colour of the unfilled portion; set via override.
- `font_color: Color` — (Theme) colour of the percentage text; set via override.
- (Layout properties inherited from [[Control]].)

## Methods
- (No public methods beyond those inherited from [[Control]] and `Range`.)

## Signals
- `value_changed(value: float)` — Emitted (via [[Range]]) when `progress` changes.

## Description
`ProgressBar` displays a value between `min_value` and `max_value` as a filled horizontal bar. It inherits from `Range`, so the familiar `value`, `min_value`, `max_value`, and `step` properties drive it; setting `progress = 7` with `max_value = 10` shows a 70% fill. Enable `show_percentage` to print the percentage, or leave it off for a clean bar.

`indeterminate = true` turns the bar into a pulsing "busy" indicator with no known completion — the right choice when you are loading assets and do not know how long it will take. The fill, background, and text colours are themeable via `add_theme_color_override`, so a single bar can be retinted red for danger or green for healthy progress.

In the Kadi app `ProgressBar` shows each player's hand size relative to their starting hand — a quick visual of who is close to winning ("Niko Kadi"). The penalty accumulator's running total is mirrored on a red-tinted bar so opponents can see the pressure build. A long shuffle of the deck displays an `indeterminate` bar while the [[Array]] is being randomised, and the learning-app section progress uses one too, advancing as the learner completes each phase.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`ProgressBar`)**: When configuring `ProgressBar` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `ProgressBar` offline. If your `ProgressBar` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using ProgressBar in Godot 4.3 Kadi
func configure_progressbar_example(target: ProgressBar) -> void:
    assert(target != null, "ProgressBar reference must be valid")
    print("Configured ProgressBar for Kadi table runtime")
```

