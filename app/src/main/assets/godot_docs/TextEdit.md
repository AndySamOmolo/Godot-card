---
class: TextEdit
inherits: Control
verified: 4.3
short: A multi-line text editor with syntax highlighting, selection, and clipboard support.
---

## Properties
- `text: String` — The full contents of the document.
- `editable: bool` — When true, the user can modify the text.
- `wrap_mode: LineWrappingMode` — None, boundary, or word-aware wrapping.
- `autowrap_mode: AutowrapMode` — How wrapping breaks long lines.
- `tab_size: int` — How many spaces one tab character represents.
- `indent_size: int` — Spaces inserted per indent level.
- `use_tabs: bool` — When true, Tab inserts a tab character; otherwise spaces.
- `syntax_highlighter: SyntaxHighlighter` — A [[Resource]] that colours the text.
- `show_line_numbers: bool` — When true, a gutter shows line numbers.
- `line_numbers_zero_padded: bool` — When true, line numbers are zero-padded.
- `draw_minimap: bool` — When true, a minimap of the document is shown.
- `minimap_width: int` — Pixel width of the minimap.
- `highlight_current_line: bool` — When true, the line under the caret is highlighted.
- `highlight_all_occurrences: bool` — When true, all matches of the selection are tinted.
- `caret_multiple: bool` — When true, multiple carets are allowed.
- `caret_type: CaretType` — Line or block caret.
- `middle_mouse_paste_enabled: bool` — When true, middle-click pastes the selection.
- `selecting_enabled: bool` — When true, the user can select text.
- `deselect_on_focus_loss_enabled: bool` — When true, focus loss clears the selection.
- `drag_and_drop_selection_enabled: bool` — When true, selected text can be dragged.
- `virtual_keyboard_enabled: bool` — When true, a touch keyboard appears on mobile.

## Methods
- `void insert_text_at_caret(text: String)` — Inserts `text` at every caret.
- `void insert_line(at_line: int, text: String)` — Inserts a new line of text.
- `void delete_selection()` — Deletes the current selection.
- `String get_selected_text()` — Returns the active selection.
- `void select(from_line: int, from_col: int, to_line: int, to_col: int)` — Selects a range.
- `void select_all()` — Selects the whole document.
- `void deselect()` — Clears the selection.
- `void set_caret_line(line: int, adjust_viewport: bool = true)` — Moves the caret to a line.
- `int get_caret_line()` — Returns the caret's line index.
- `int get_caret_column()` — Returns the caret's column index.
- `int get_line_count()` — Returns the number of lines.
- `String get_line(line: int)` — Returns the text of a single line.
- `void set_line(line: int, text: String)` — Replaces a single line.
- `void clear()` — Empties the document.
- `void undo()` — Reverts the last edit.
- `void redo()` — Re-applies an undone edit.
- `void copy()` — Copies the selection to the clipboard.
- `void cut()` — Cuts the selection to the clipboard.
- `void paste()` — Pastes the clipboard at the caret.

## Signals
- `text_changed()` — Emitted when the text is modified.
- `lines_edited_from(from_line: int, to_line: int)` — Emitted with the affected line range after an edit.
- `caret_changed()` — Emitted when the caret moves.
- `text_set()` — Emitted when `text` is reassigned programmatically.

## Description
`TextEdit` is a full multi-line editor with caret handling, multiple selections, undo/redo, drag-and-drop, a minimap, line numbers, and pluggable syntax highlighting. It is a [[Control]], so it lays out inside containers and can be scrolled with a [[ScrollContainer]]. For a single line, prefer the lighter [[LineEdit]].

A `SyntaxHighlighter` resource drives code colouring; ship a `CodeHighlighter` configured for [[GDScript]] keywords and the editor instantly looks familiar. `wrap_mode`, `tab_size`, and `indent_size` let you match the learner's expected formatting, and `show_line_numbers` plus `highlight_current_line` make it easy to point at a specific line in a tutorial step.

In the Kadi learning app `TextEdit` is the surface for showing and editing small [[GDScript]] snippets the learner can read or tweak — for example the `func deal_cards():` body in `l_02_02_ready_dealing`. The highlighter colours `var`, `func`, and `signal` keywords so the example matches the docs. `text_changed` lets the app validate the snippet live, and `caret_changed` powers a small "you are here" gutter that maps the caret to a phase-step explanation. It is read-only (`editable = false`) when the app just wants to display code.
