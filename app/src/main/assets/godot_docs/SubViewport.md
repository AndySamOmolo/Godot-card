---
class: SubViewport
inherits: Viewport
verified: 4.3
short: Off-screen 2D or 3D rendering surface used for minimaps, split-screen, and 3D-in-2D UI previews.
---

## Properties
- `size: Vector2i` — Resolution in pixels of the off-screen render target.
- `transparent_bg: bool` — Renders with a transparent background so 3D models composite cleanly over 2D UI.
- `render_target_update_mode: UpdateMode` — Controls when the viewport redraws.

## Methods
- `ViewportTexture get_texture()` — Returns the live `Texture2D` rendered by this `SubViewport`.

## Signals
- `size_changed()` — Emitted when `size` changes.

## Description
`SubViewport` renders its own child 2D/3D camera to a texture. Wrap it in a `SubViewportContainer` (or assign `get_texture()` to a `[[TextureRect]]`) to build RPG minimaps, security monitors, or 3D spinning trophy previews inside a 2D menu!

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`SubViewport`)**: Press **F1** inside the Godot 4.3 editor or tap any `SubViewport` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
@onready var preview_vp: SubViewport = $SubViewport
@onready var minimap_rect: TextureRect = $HUD/MinimapRect

func _ready() -> void:
	preview_vp.transparent_bg = true
	minimap_rect.texture = preview_vp.get_texture()
```
