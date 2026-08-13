---
class: Button
inherits: BaseButton
verified: 4.3
short: A pressable UI control that shows text and/or an icon and emits pressed when clicked.
---

## Properties
- `text: String` — The label shown on the button.
- `icon: Texture2D` — An optional [[Texture2D]] drawn alongside the text.
- `alignment: HorizontalAlignment` — Horizontal placement of the text/icon (left, center, right).
- `icon_alignment: HorizontalAlignment` — Horizontal placement of the icon.
- `expand_icon: bool` — When true, the icon grows to fill available space.
- `flat: bool` — When true, the button has no visible background until hovered/pressed.
- `clip_text: bool` — When true, the text is clipped to the button's width instead of resizing it.
- `language: String` — Language code used for text shaping/breaking.
- `structured_text_bidi_override: StructuredTextParser` — BiDi override for the text.
- `structured_text_bidi_override_options: Array` — Options for the BiDi override.

## Methods
- (No public methods beyond those inherited from [[Control]] and `BaseButton`.)

## Signals
- `pressed()` — Emitted when the button is activated by a click or keyboard.
- `button_down()` — Emitted when the button is pressed down.
- `button_up()` — Emitted when the button is released.
- `toggled(button_pressed: bool)` — Emitted (in toggle mode) when the pressed state changes.
- `pressed()` — (Inherited alias) standard click activation.

## Description
`Button` is the workhorse interactive control. It inherits layout and theming from [[Control]] and click-state behaviour from `BaseButton`, adding a `text` label and optional `icon`. When the user clicks it (or activates it with the keyboard while it has focus) the button emits `pressed`; connect that signal to a [[Callable]] to trigger an action. Set `toggle_mode = true` (inherited) to make it behave like a check button that stays down.

A `Button` is fully themable: every colour, font, and style for each state (normal, hover, pressed, disabled, focused) lives in the theme, and you can override any of them with the inherited `add_theme_*_override` methods. `flat` buttons are useful for toolbar-style controls that should blend into a [[Panel]], while the default styled button is the right choice for primary actions like "Confirm".

In the Kadi app `Button` drives every confirmation. The declaration popup from `l_08_01_declaration_popup` has four buttons labelled "Clubs", "Diamonds", "Hearts", "Spades"; each is connected via its `pressed` signal to a function that records the chosen [[Suit]]. A "Draw" button lets the active player pull from the draw pile, and an "End Turn" button appears when a play is validated. Disabled buttons (via the inherited `disabled` property) grey out automatically when the player cannot perform that action, giving clear visual feedback without extra code.
