---
class: OptionButton
inherits: Button
verified: 4.3
short: A button that opens a dropdown list of selectable items.
---

## Properties
- `selected: int` — The index of the currently selected item (-1 if none).
- `item_count: int` — Read-only count of items in the list.
- `fit_to_longest_item: bool` — When true, the button width follows the longest item text.
- `longest_item: int` — Read-only index of the longest item (after measurement).
- `allow_reselect: bool` — When true, selecting the already-selected item re-emits `item_selected`.
- `text: String` — (Inherited from [[Button]]) shows the selected item's label.
- `icon: Texture2D` — (Inherited) optional icon shown next to the text.
- `flat: bool` — (Inherited) when true, no background until interacted with.

## Methods
- `void add_item(label: String, id: int = -1)` — Appends an item, optionally with a custom id.
- `void add_icon_item(icon: Texture2D, label: String, id: int = -1)` — Appends an item with an icon.
- `int get_item_id(index: int)` — Returns the id of the item at `index`.
- `int get_item_index(id: int)` — Returns the index of the item with `id`.
- `String get_item_text(index: int)` — Returns the label of an item.
- `Texture2D get_item_icon(index: int)` — Returns the icon of an item.
- `void set_item_text(index: int, text: String)` — Sets an item's label.
- `void set_item_icon(index: int, icon: Texture2D)` — Sets an item's icon.
- `void set_item_disabled(index: int, disabled: bool)` — Greys out an item.
- `void set_item_metadata(index: int, metadata: Variant)` — Stores arbitrary data on an item.
- `Variant get_item_metadata(index: int)` — Reads an item's metadata.
- `void remove_item(index: int)` — Removes an item.
- `void clear()` — Removes all items.
- `void select(index: int)` — Sets the selection programmatically.

## Signals
- `item_selected(index: int)` — Emitted when the user picks an item.
- `item_focused(index: int)` — Emitted when a popup item gains focus.

## Description
`OptionButton` is a [[Button]] that, when clicked, opens a `PopupMenu` of items. It is the standard dropdown selector. Each item has a label, optional icon, optional integer `id`, optional metadata [[Variant]], and can be disabled. `selected` gives the index of the current choice; `get_item_id` is preferred over the index when items can be reordered, because the id is stable.

Because it inherits [[Button]], `OptionButton` gets all the button theming, focus, and signal machinery. `fit_to_longest_item` keeps the whole dropdown readable by sizing the button to its widest entry. For a multi-select or richer list, a `PopupMenu` or a custom [[GridContainer]] may suit better.

In the Kadi app an `OptionButton` selects the number of players (2–6), the AI difficulty, and the card-back theme from `l_11_01_tweens`. The items' metadata hold structured config (e.g. `{"players": 4, "ai_skill": 0.7}`) read back with `get_item_metadata(selected)`. `item_selected` triggers a settings change and disables the "Start" [[Button]] until a valid configuration is chosen. The same control surfaces a "jump to lesson" dropdown in the Learn tab.
