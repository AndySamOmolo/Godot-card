---
class: Window
inherits: Viewport
verified: 4.3
short: A viewport that is also an operating-system window or embedded sub-window with a title and decorations.
---

## Properties
- `title: String` — The text shown in the window's title bar.
- `size: Vector2i` — The window's size in pixels.
- `position: Vector2i` — The window's position on screen in pixels.
- `mode: Mode` — Windowed, minimized, maximized, fullscreen, or exclusive fullscreen.
- `visible: bool` — Whether the window is currently shown.
- `unresizable: bool` — When true, the user cannot resize the window.
- `borderless: bool` — When true, the window has no title bar or borders.
- `always_on_top: bool` — When true, the window stays above other windows.
- `transient: bool` — When true, the window is a child (transient) of its parent window.

## Methods
- `void show()` — Makes the window visible.
- `void hide()` — Hides the window.
- `void popup(rect: Rect2i = Rect2i(0, 0, 0, 0))` — Shows the window as a popup, optionally at a rectangle.
- `void popup_centered(minsize: Vector2i = Vector2i(0, 0))` — Shows the window centered on its parent.
- `void move_to_center()` — Centers the window on the current screen.
- `void request_attention()` — Asks the OS to flash or highlight the window.
- `Vector2i get_size_with_decorations()` — Returns the size including borders and title bar.

## Signals
- `close_requested()` — Emitted when the user tries to close the window.
- `focus_entered()` — Emitted when the window gains focus.
- `focus_exited()` — Emitted when the window loses focus.
- `size_changed()` — Emitted when the window is resized.

## Description
`Window` extends [[Viewport]] with the concept of an actual window: a titled, movable, resizable surface managed by the operating system (or embedded inside another window in the editor). Since it is a viewport, everything you can render and pick in a viewport also applies here, plus decorations and window-manager behaviour.

`Window` is the base of dialogs like `AcceptDialog` and `FileDialog`, and its `popup*` methods make it easy to present modal panels. Signals such as `close_requested` let you intercept the user closing a window — handy for confirmation prompts.

In the Kadi app the game runs inside the main `Window`, and on desktop builds `title` shows "Kadi". The tutorial's "Are you sure you want to quit?" prompt is a child `Window` shown with `popup_centered`, and its `close_requested` signal is trapped so a mid-hand quit asks for confirmation first.
