---
class: ScrollContainer
inherits: Container
verified: 4.3
short: A container that scrolls a single child control that is larger than the viewport.
---

## Properties
- `horizontal_scroll_mode: ScrollMode` — When the horizontal scrollbar appears (auto, always, never, show-on-focus).
- `vertical_scroll_mode: ScrollMode` — When the vertical scrollbar appears.
- `horizontal_scroll_deadzone: float` — Dead-zone fraction for horizontal drag-scroll.
- `vertical_scroll_deadzone: float` — Dead-zone fraction for vertical drag-scroll.
- `follow_focus: bool` — When true, the container auto-scrolls to the focused child.
- (Layout properties inherited from [[Control]].)

## Methods
- `void set_h_scroll(value: float)` — Sets the horizontal scroll offset.
- `void set_v_scroll(value: float)` — Sets the vertical scroll offset.
- `float get_h_scroll()` — Returns the current horizontal offset.
- `float get_v_scroll()` — Returns the current vertical offset.
- `void ensure_control_visible(control: Control)` — Scrolls so that `control` is fully in view.
- `HScrollBar get_h_scroll_bar()` — Returns the horizontal scrollbar node.
- `VScrollBar get_v_scroll_bar()` — Returns the vertical scrollbar node.

## Signals
- `scroll_started()` — Emitted when scrolling begins.
- `scroll_ended()` — Emitted when scrolling stops.

## Description
`ScrollContainer` hosts exactly one child (usually a [[VBoxContainer]] or a large [[Control]]) and lets the user scroll it when it is bigger than the container's visible area. It manages its own `HScrollBar` and `VScrollBar`, and the `*_scroll_mode` properties control when each appears. Because the child can be any control, you can build arbitrarily long content — logs, docs, option lists — without writing your own scroll math.

`follow_focus = true` is invaluable on touch and keyboard UIs: when a child gains focus (a [[Button]] is tabbed to, say), the container scrolls it into view automatically. For programmatic control, `ensure_control_visible(child)` does the same on demand, and `set_v_scroll` jumps to an exact offset — useful for "scroll to top" buttons.

In the Kadi learning app a `ScrollContainer` wraps the long-form tutorial text for each phase so it reads comfortably on a phone, and another scrolls the play log during a match (newest entry revealed via `ensure_control_visible`). The in-app docs viewer (the one that renders these very pages) uses a `ScrollContainer` over a [[VBoxContainer]] of heading [[Label]]s and code [[TextEdit]]s, so the same documentation scales from a phone to a tablet without rewriting layout.
