---
class: TranslationServer
inherits: Object
verified: 4.3
short: Singleton managing runtime localization locales (e.g. 'en', 'sw') and string translation tables.
---

## Properties
- (Managed via singleton methods.)

## Methods
- `void set_locale(locale: String)` — Switches the active language locale (e.g. `"en"`, `"sw"`) across the entire game.
- `String get_locale()` — Returns the currently active locale string.
- `StringName translate(message: StringName)` — Translates a key using the active locale (called by `Object.tr()`).

## Signals
- (No signals; `Control` nodes receive `NOTIFICATION_TRANSLATION_CHANGED` automatically.)

## Description
`TranslationServer` powers multi-language games. Call `TranslationServer.set_locale("sw")` to switch between English and Kiswahili at runtime, and use `tr("KEY")` in GDScript.

### Kadi & Solo Game Dev Example

> [!GODOT]
> **Godot 4.3 Inspector & Reference Tip (`TranslationServer`)**: Press **F1** inside the Godot 4.3 editor or tap any `TranslationServer` pill in Kadi Companion to inspect its properties, methods, and signals offline.

```gdscript
func toggle_english_swahili() -> void:
	var next_loc: String = "sw" if TranslationServer.get_locale().begins_with("en") else "en"
	TranslationServer.set_locale(next_loc)
```
