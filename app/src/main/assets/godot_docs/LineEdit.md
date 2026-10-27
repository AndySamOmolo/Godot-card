---
class: LineEdit
inherits: Control
verified: 4.3
short: A single-line text input field the user can type into.
---

## Properties
- `text: String` — The current contents of the field.
- `placeholder_text: String` — Greyed-out hint shown when `text` is empty.
- `alignment: HorizontalAlignment` — Text alignment.
- `max_length: int` — Maximum number of characters the user can enter (0 = unlimited).
- `editable: bool` — When true, the user can modify the text.
- `secret: bool` — When true, characters are displayed as dots (for passwords).
- `secret_character: String` — The mask character used when `secret` is on.
- `select_all_on_focus: bool` — When true, focusing selects all the text.
- `deselect_on_focus_loss: bool` — When true, leaving the field clears the selection.
- `caret_blink_enabled: bool` — When true, the caret blinks.
- `caret_blink_interval: float` — Seconds between caret blinks.
- `caret_column: int` — Caret position measured in characters.
- `context_menu_enabled: bool` — When true, right-click shows cut/copy/paste.
- `virtual_keyboard_enabled: bool` — When true, a touch keyboard appears on mobile.
- `clear_button_enabled: bool` — When true, a clear (x) button is shown.
- `selecting_enabled: bool` — When true, the user can select text with the mouse.

## Methods
- `void clear()` — Empties the field.
- `bool has_selected_text()` — True if there is a selection.
- `String get_selected_text()` — Returns the currently selected substring.
- `void select(from: int = 0, to: int = -1)` — Selects a range of characters.
- `void select_all()` — Selects the whole text.
- `void deselect()` — Removes the selection.
- `void delete_char_at_caret()` — Deletes the character after the caret.
- `void delete_selection()` — Deletes the selected text.
- `void set_caret_column(column: int, always_emit: bool = false)` — Moves the caret.
- `void append_text(text: String)` — Appends `text` to the end.
- `void grab_focus()` — (Inherited) gives the field keyboard focus.

## Signals
- `text_changed(new_text: String)` — Emitted whenever the text is edited.
- `text_submitted(new_text: String)` — Emitted when the user presses Enter.
- `text_change_rejected(rejected_substring: String)` — Emitted when input violates `max_length`.

## Description
`LineEdit` is the standard single-line text input. The user types into it, the field emits `text_changed` on every keystroke and `text_submitted` when Enter is pressed, and you read or set the value through `text`. Built-in niceties include a placeholder hint, a clear button, an optional secret mode for sensitive input, and full caret/selection control via the methods.

On mobile, `virtual_keyboard_enabled` brings up the on-screen keyboard automatically, which is essential for an Android learning app. `max_length` enforces a hard cap, and `select_all_on_focus` makes re-entering a value faster. For multi-line or rich text, use [[TextEdit]] instead.

The Kadi app uses `LineEdit` for naming players before a match, and in the learning side for the in-app code-exercise prompts where the learner types a short expression (such as `"if hand.size() == 1:"`) and submits it with Enter. `text_submitted` triggers the answer check, and the field's `placeholder_text` shows a faded hint like `type your condition here`. The clear button lets learners wipe an attempt and try again without fiddling with selection.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`LineEdit`)**: When configuring `LineEdit` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `LineEdit` offline. If your `LineEdit` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
# Using LineEdit in Godot 4.3 Kadi
func configure_lineedit_example(target: LineEdit) -> void:
    assert(target != null, "LineEdit reference must be valid")
    print("Configured LineEdit for Kadi table runtime")
```

