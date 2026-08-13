---
class: Label
inherits: Control
verified: 4.3
short: A control that displays non-editable, optionally wrapped text.
---

## Properties
- `text: String` — The text to display.
- `label_settings: LabelSettings` — A [[Resource]] holding font, size, colour, and shadow settings.
- `horizontal_alignment: HorizontalAlignment` — Left, center, or right alignment.
- `vertical_alignment: VerticalAlignment` — Top, center, or bottom alignment.
- `autowrap_mode: AutowrapMode` — How text wraps when it overflows the width.
- `clip_text: bool` — When true, text is clipped to the control rect instead of resizing.
- `ellipsis_char: String` — Character used to indicate truncated text.
- `text_overrun_behavior: OverrunBehavior` — What happens when text doesn't fit.
- `uppercase: bool` — When true, text is rendered in capitals.
- `visible_characters: float` — How many characters to show (for typewriter effects); `-1` shows all.
- `visible_ratio: float` — Fraction of characters shown (0–1).
- `visible_characters_behavior: VisibleCharactersBehavior` — How `visible_characters` counts glyphs.
- `structured_text_bidi_override: StructuredTextParser` — BiDi override for complex scripts.
- `tab_stops: PackedFloat32Array` — Tab stop positions for tab-aligned text.
- `language: String` — Language code for text shaping.

## Methods
- `int get_character_line(pos: int)` — Returns the line index of the character at `pos`.
- `int get_line_count()` — Returns the number of wrapped lines.
- `int get_visible_line_count()` — Returns how many lines are currently shown (respects `clip_text`).
- `int get_total_character_count()` — Returns the total number of glyphs.
- `void reset_size()` — Recomputes the minimum size from the current text and font.

## Signals
- `texture_changed()` — Emitted (rarely) when a texture-based label resource changes.

## Description
`Label` shows text the player can read but not edit. It is a [[Control]], so it participates fully in anchor/container layout, and it supports rich alignment, wrapping, and "overrun" behaviour that gracefully truncates text that does not fit. For styled text, a `LabelSettings` resource bundles font, size, colour, outline, and shadow into one sharable object.

Two properties are especially useful for game feedback: `visible_characters` and `visible_ratio` reveal the text progressively, which combined with a [[Timer]] produces a typewriter effect for status messages. `autowrap_mode` keeps long messages inside their panel, and `uppercase` is a quick way to shout "NIKO KADI" headers without editing the source string.

In the Kadi app `Label` is everywhere: the turn indicator ("Player 2's turn"), the penalty total ("Penalty: 6"), each player's name and hand count, and the log of recent plays. The "Niko Kadi!" warning uses a themed `Label` with `uppercase = true` whose `visible_characters` is tweened up by a [[Tween]] for a punchy reveal. [[Suit]] icons sit next to labels using a small [[HBoxContainer]] so a glyph and its text stay aligned.
