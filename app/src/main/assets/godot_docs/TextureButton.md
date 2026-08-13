---
class: TextureButton
inherits: BaseButton
verified: 4.3
short: A button drawn entirely from textures for its normal, hover, pressed, and disabled states.
---

## Properties
- `texture_normal: Texture2D` — Texture shown in the default (idle) state.
- `texture_pressed: Texture2D` — Texture shown while the button is held down.
- `texture_hover: Texture2D` — Texture shown when the pointer is over the button.
- `texture_disabled: Texture2D` — Texture shown when the button is disabled.
- `texture_focused: Texture2D` — Texture shown when the button has keyboard focus.
- `texture_click_mask: BitMap` — A bitmap defining which pixels are clickable.
- `ignore_texture_size: bool` — When true, textures are stretched to the control size instead of setting it.
- `stretch_mode: StretchMode` — How the texture is scaled to fit (e.g. scale, keep, tile).
- `flip_h: bool` — When true, the texture is mirrored horizontally.
- `flip_v: bool` — When true, the texture is mirrored vertically.

## Methods
- `void set_texture_normal(texture: Texture2D)` — Sets the normal-state texture.
- `Texture2D get_texture_normal()` — Returns the normal-state texture.
- `void set_texture_pressed(texture: Texture2D)` — Sets the pressed-state texture.
- `void set_stretch_mode(mode: StretchMode)` — Sets how the texture is stretched.
- `void set_click_mask(mask: BitMap)` — Sets the pixel-accurate click mask.

## Signals
- (No signals of its own; use [[BaseButton]]'s `pressed`, `button_down`, `button_up`, and `toggled`.)

## Description
`TextureButton` is a [[BaseButton]] whose appearance comes entirely from images rather than a theme. You assign a `Texture2D` for each state — normal, hover, pressed, disabled, focused — and the button swaps between them automatically. With a `texture_click_mask` the clickable area follows the artwork's shape instead of a plain rectangle, so oddly-shaped buttons only respond where they are actually opaque.

Because it inherits from `BaseButton`, all the interaction logic — `pressed`, toggling, disabling, and grouping — is shared with [[Button]] and [[CheckButton]]. `TextureButton` simply changes how that button looks.

In the Kadi app `TextureButton` renders the fully-illustrated action controls: the "Draw" pile button uses a card-back texture that darkens on `texture_pressed`, and the "Niko Kadi" call is a golden badge with a distinct hover texture. Their pixel-accurate `texture_click_mask` means taps only register on the visible artwork, not the transparent corners.
