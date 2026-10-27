---
class: Texture2D
inherits: Resource
verified: 4.3
short: A 2D image resource that can be sampled and drawn by CanvasItem, Sprite2D, Control, and others.
---

## Properties
- `width: int` — The texture's width in pixels (read-only).
- `height: int` — The texture's height in pixels (read-only).

## Methods
- `int get_width()` — Returns the texture's width.
- `int get_height()` — Returns the texture's height.
- `Vector2i get_size()` — Returns the size as `Vector2i(width, height)`.
- `bool has_alpha()` — True if the texture contains an alpha channel.
- `Image get_image()` — Returns a copy of the pixel data as an [[Image]] (slow; not for per-frame use).
- `void draw(to_canvas_item: RID, pos: Vector2, modulate: Color = Color(1,1,1,1), transpose: bool = false)` — Draws this texture onto the canvas item identified by its RID.
- `void draw_rect(to_canvas_item: RID, rect: Rect2, tile: bool = false, modulate: Color = Color(1,1,1,1), transpose: bool = false)` — Draws the texture stretched into `rect`.

## Signals
- `changed()` — Inherited from [[Resource]]; emitted (for example by [[ImageTexture]]) when the pixel data changes.

## Description
`Texture2D` is the abstract base class for 2D image data on the GPU. Concrete subclasses include `CompressedTexture2D` (the usual on-disk `.png`/`.webp` import), `ImageTexture` (a runtime-created, mutable texture), `CanvasTexture` (a bundle of diffuse, normal and specular for [[CanvasItem]]s), and `PlaceholderTexture2D` (a stand-in while streaming). You rarely instantiate `Texture2D` directly; you `load()` an image file and Godot hands back the right subclass.

Textures are [[Resource]]s, so they are shared and reference-counted: assigning the same card-back texture to fifty card nodes costs one copy of the data, not fifty. Use `get_width`/`get_height` (or `get_size`) to size layouts to the art, and `has_alpha` to decide whether a card needs transparency-aware blending. `get_image` is available but slow — only call it for one-off operations such as generating a new card atlas at runtime.

In the Kadi project every card face is a `Texture2D` loaded from the `res://art/cards/` folder and stored on the `CardData` [[Resource]]. The [[Sprite2D]] or `TextureRect` that represents the card reads its `texture` straight from that resource, so a change of art is a one-line edit. The card-back is a single shared `Texture2D` reused for every face-down card, which keeps draw calls low and memory small even when the discard pile grows.

### Kadi & Downloaded Assets Example

> [!GODOT]
> **Godot 4.3 Inspector & Downloaded Assets Tip (`Texture2D`)**: When configuring `Texture2D` in the Godot 4.3 Inspector, hover any property name to read its built-in engine tooltip, or press **F1** to jump to `Texture2D` offline. If your `Texture2D` works with downloaded card PNGs (`res://assets/cards/`) or audio (`res://assets/audio/`), always verify paths with `ResourceLoader.exists(path)` so procedural fallbacks remain active while testing.

```gdscript
func load_card_texture(suit_name: String, rank: int) -> Texture2D:
    var path: String = "res://assets/cards/%s_%d.png" % [suit_name.to_lower(), rank]
    if ResourceLoader.exists(path):
        return load(path) as Texture2D
    return null
```

