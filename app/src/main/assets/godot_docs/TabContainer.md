---
class: TabContainer
inherits: Container
verified: 4.3
short: A container that arranges its children as tabbed pages, with a tab bar on top.
---

## Properties
- `current_tab: int` — The index of the visible tab.
- `tab_alignment: TabBar.AlignmentMode` — Left, center, or right alignment of tabs.
- `clip_tabs: bool` — When true, overflowing tabs are scrolled instead of shrunk.
- `tab_close_display_policy: CloseButtonDisplayPolicy` — When the per-tab close (x) button appears.
- `deselect_enabled: bool` — When true, clicking the active tab deselects it (no tab shown).
- `use_hidden_tabs_for_min_size: bool` — When true, hidden tabs count toward the minimum size.
- `drag_to_rearrange_enabled: bool` — When true, tabs can be reordered by dragging.
- `tabs_visible: bool` — When false, the tab bar is hidden entirely.
- `tabs_position: TabContainer.TabsPosition` — Top or bottom placement of the bar.
- `all_tabs_in_front: bool` — When true, all tabs are kept available even when the container scrolls.
- (Layout properties inherited from [[Control]].)

## Methods
- `int get_tab_count()` — Returns the number of tabs.
- `Control get_current_tab_control()` — Returns the visible tab's control.
- `Control get_previous_tab()` — Returns the previously visible tab.
- `int get_tab_idx_at_point(point: Vector2)` — Returns the tab index under a point, or `-1`.
- `int get_tab_idx_from_control(control: Control)` — Returns the index of a tab given its child control.
- `void set_tab_title(tab: int, title: String)` — Sets a tab's label.
- `String get_tab_title(tab: int)` — Returns a tab's label.
- `void set_tab_icon(tab: int, icon: Texture2D)` — Sets a tab's icon.
- `void set_tab_disabled(tab: int, disabled: bool)` — Greys out a tab so it cannot be selected.
- `void set_tab_hidden(tab: int, hidden: bool)` — Hides a tab from the bar.
- `void set_tab_metadata(tab: int, metadata: Variant)` — Stores arbitrary data on a tab.
- `Variant get_tab_metadata(tab: int)` — Reads a tab's metadata.
- `void add_child(node: Node, force_readable_name: bool = false)` — (Inherited) adding a Control child adds a tab.

## Signals
- `tab_changed(tab: int)` — Emitted when the active tab switches.
- `tab_selected(tab: int)` — Emitted when a tab is selected.
- `tab_hovered(tab: int)` — Emitted when the pointer hovers a tab.
- `tab_rmb_clicked(tab: int)` — Emitted on right-click over a tab.
- `pre_popup_pressed()` — Emitted before the overflow popup menu opens.

## Description
`TabContainer` turns its child [[Control]]s into switchable pages, each with its own tab on a bar at the top (or bottom). Adding a child automatically creates a tab whose label is the child's name; rename the child or call `set_tab_title` to change what's shown. `current_tab` selects the page; the others are hidden but kept in the tree. `clip_tabs` keeps tabs readable when there are many by scrolling them, and `drag_to_rearrange_enabled` lets the user reorder.

The tab bar supports icons, per-tab disable and hide, metadata, and an optional close button governed by `tab_close_display_policy`. The `tab_changed` / `tab_selected` signals let other UI react when the page switches — useful to update a contextual toolbar or save the outgoing tab's state.

In the Kadi learning app a `TabContainer` organises the main screens: "Play", "Learn", "Docs", "Glossary". Each tab holds a different [[Control]] subtree, and `tab_changed` triggers lazy-loading of heavier content (such as these doc pages) only when first viewed. The "Learn" tab itself nests another `TabContainer` for the phases, with disabled tabs ahead of the learner's progress so they can see the road ahead but not skip ahead. `tabs_position` is bottom on phones for thumb reachability.
