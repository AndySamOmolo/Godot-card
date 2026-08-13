---
class: Slider
inherits: Range
verified: 4.3
short: Abstract base for draggable value selectors like HSlider and VSlider.
---

## Properties
- `value: float` — The current value (inherited from [[Range]]).
- `min_value: float` — The minimum value (inherited from [[Range]]).
- `max_value: float` — The maximum value (inherited from [[Range]]).
- `step: float` — Snapping increment (inherited from [[Range]]).
- `editable: bool` — When true, the user can move the slider.
- `scrollable: bool` — When true, the mouse wheel changes the value.
- `tick_count: int` — Number of ticks drawn along the track (0 = none).
- `ticks_on_borders: bool` — When true, ticks are drawn at min and max.
- `bigger_inside_slider: bool` — When true, the grabber enlarges while being dragged.
- `show_percentage: bool` — (Not on Slider; inherited concept) whether to display a value label.
- (Layout properties inherited from [[Control]].)

## Methods
- (No public methods beyond those inherited from [[Control]] and `Range`; concrete subclasses `HSlider`/`VSlider` are what you instantiate.)

## Signals
- `value_changed(value: float)` — Emitted when the value changes (dragging or programmatic).
- `drag_started(value: float)` — Emitted when the user begins dragging the grabber.
- `drag_ended(value_changed: bool)` — Emitted when the user releases the grabber.

## Description
`Slider` is the abstract parent of `HSlider` (horizontal) and `VSlider` (vertical). Like [[ProgressBar]] it derives from `Range`, so `value`, `min_value`, `max_value`, and `step` define the range; the user drags the grabber (or clicks the track, or scrolls if `scrollable`) to set `value`. `drag_started` / `drag_ended` signals let you distinguish live dragging from a final commit, which matters when a continuously-changing value would be expensive to apply.

`tick_count` plus `ticks_on_borders` draws reference marks; `step` snaps the value to clean increments. For precise numeric entry pair a slider with a [[LineEdit]] or [[SpinBox]]: drag for fast approximate changes, type for exact ones.

In the Kadi app `Slider` is rarely a gameplay element — cards are discrete, not continuous — but the learning side uses an `HSlider` for the animation-speed scrubber that lets a learner step through the dealing animation frame by frame, and another sets the AI think-delay. `drag_ended` is the signal used to commit a new value (re-running the deal preview) so the app doesn't rebuild the layout sixty times a second while the learner drags. `step` snaps to 0.05 for a controlled feel.
