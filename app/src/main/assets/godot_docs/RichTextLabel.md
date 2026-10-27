---
class: RichTextLabel
inherits: Control
verified: 4.3
short: A label that renders multi-styled text with BBCode, links, and inline formatting.
---

## Properties
- `text: String` — The raw text; interpreted as BBCode when `bbcode_enabled` is true.
- `bbcode_enabled: bool` — When true, tags like `[b]` and `[color]` are parsed as formatting.
- `fit_content: bool` — When true, the label's minimum size grows to fit its content.
- `scroll_active: bool` — When true, the label can scroll its overflowing content.
- `scroll_following: bool` — When true, new text auto-scrolls to the bottom.
- `selection_enabled: bool` — When true, the user can select text with the pointer.
- `meta_underlined: bool` — When true, `[url]` links are drawn underlined.
- `visible_characters: int` — Number of characters shown, useful for typewriter effects (-1 shows all).
- `visible_ratio: float` — Fraction (0–1) of characters shown; an alternative to `visible_characters`.

## Methods
- `void append_text(bbcode: String)` — Parses and appends BBCode-formatted text.
- `void add_text(text: String)` — Appends plain text without parsing tags.
- `void clear()` — Removes all content.
- `void push_color(color: Color)` — Begins a colored span until the matching `pop`.
- `void push_bold()` — Begins a bold span.
- `void push_italics()` — Begins an italic span.
- `void pop()` — Ends the most recently pushed formatting span.
- `void newline()` — Adds a line break.
- `int get_total_character_count()` — Returns the total number of visible characters.

## Description
`RichTextLabel` extends [[Control]] to display text with mixed styling in a single node. With `bbcode_enabled`, you write markup like `[b]bold[/b]` and `[color=red]red[/color]` directly in `text`; or you can build content programmatically with the `push_*` / `pop` stack and `append_text`. Unlike a plain [[Label]], it supports links, colors, fonts, and inline images all at once.

It also drives text reveal effects through `visible_characters` and `visible_ratio`, and can scroll long content when `scroll_active` is set. Clickable `[url]` regions emit `meta_clicked`, which is handy for interactive help.

In the Kadi app `RichTextLabel` powers the rules and tutorial panels: special-card explanations use `[color]` to highlight the suit that a Jack skips or an Ace declares, and a typewriter effect driven by `visible_ratio` reveals hints one line at a time. The end-of-turn log appends colored entries with `append_text` so penalties stand out in red.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`RichTextLabel`)**: When configuring `RichTextLabel` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `RichTextLabel` offline. If your `RichTextLabel` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using RichTextLabel in Godot 4.3 Kadi
func configure_richtextlabel_example(target: RichTextLabel) -> void:
    assert(target != null, "RichTextLabel reference must be valid")
    print("Configured RichTextLabel for Kadi table runtime")
```

