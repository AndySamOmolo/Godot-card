# Kadi Companion App (v2.2.0) — Mobile UI Studio, Clean Rich Text & Full Customization Edition

An offline-first Android companion app (Kotlin + Jetpack Compose + Material 3 + Room + Hilt) that teaches programming and **Godot 4.3** game development step by step by building the Kenyan card game **Kadi**.

## What's New in v2.2.0

1. **Zero Raw Markdown Artifacts (`''`, `` ` ``, `[[...]]`, `**`) Across Every Screen**:
   - **Recursive `RichInlineText` Engine (`MarkdownText.kt`)**: Completely rewritten recursive inline parser that cleanly formats nested markdown (`**bold**` containing `` `inline_code` `` or `[[GlossaryTerm|Alias]]`), resolves `[[Term|Alias]]` display labels without leaking brackets or pipes, protects `snake_case` GDScript identifiers from accidental italics, and sanitizes any stray punctuation.
   - **Universal `RichInlineText` Adoption**: Applied across Callout banners (`Callout.kt`), Lesson Editor Checklists, Verification Steps, Troubleshooting Cards, Key Takeaways, Lab Launch Cards, Quiz Prompts/Choices/Explanations, Practice Workbench Instructions/Hints/Diagnostics, Debug Challenge Symptoms/Hints, Godot Class Member Descriptions (`DocDetailScreen.kt`), Glossary Definitions, and Daily Pro Tips.
   - **Cleaned JSON & Markdown Assets**: Fixed all nested bold markers, double backticks (` `` `), unescaped table cell pipes, and added 64 missing Godot/GDScript glossary definitions so all 104 `[[...]]` links resolve cleanly.

2. **Phone-First Responsive UI Refinements**:
   - **Smart Phone Tables (`MarkdownTableView`)**: 2-column tables now fit 100% natively within the phone screen width with word wrapping and zero horizontal scrolling; 3+ column tables compute synchronized column widths across all rows AND include a one-tap **"Mobile Cards / Grid View"** switcher bar.
   - **Mobile Code Block Word-Wrap (`CodeBlock.kt`)**: Added a one-tap **Wrap / Scroll** toggle button right in the header bar of every GDScript code block alongside **Copy**.
   - **Compact Phone Tabs & Navigation**: Refined Lesson Studio tabs (`Guide`, `Steps`, `Lab & Quiz` + 1-tap `1-Page / 3-Tab` switcher) and Docs tabs so labels never truncate on narrow 320dp–360dp phone screens.

3. **Live UI Studio Customizer (27 Real-Time Options)**:
   - Tap the **Tune (`Customize UI`)** icon in the top bar of **any screen** to open the **Live UI Studio Bottom Sheet** (`QuickUiCustomizerSheet.kt`), or open **Settings (`SettingsScreen.kt`)** for the full 4-tab customization center:
     - **Theme Modes**: `Studio Dark`, `Pitch Black (AMOLED #000000)`, `Studio Light`, `System Default` + `Material You Dynamic Color`.
     - **7 Accent Color Palettes**: `Godot Sapphire`, `Nairobi Kadi Gold`, `Emerald Card Table`, `Cyber Violet`, `Crimson Ace`, `Savannah Sunset`, and `Monochrome Ink`.
     - **Typography & Sizing**: Independent live sliders for **UI & Lesson Text Size (`85%`–`125%`)** and **GDScript Code Font Size (`85%`–`125%`)**, 4 Font Families (`Modern Sans`, `Editorial Serif`, `Developer Mono`, `Clean Geometric`), 3 Line-Spacing modes (`Compact`, `Comfortable`, `Relaxed`), and Dyslexia-friendly letter tracking.
     - **Inline Code & Term Style**: Switch between `Pill Badge`, `Accent Text` (no background box), or `Clean Prose` (seamless bold text with zero boxes or underlines).
     - **Card Geometry & Surface Style**: Live **Corner Roundness (`0dp`–`28dp`)** slider, 4 Card Surface Styles (`Studio Border`, `Elevated Shadow`, `Frosted Glass`, `Minimal Outline`), and a toggle for colored top accent stripes.
     - **Phone Ergonomics & Layout**: `Compact Phone`, `Balanced`, and `Spacious` screen densities; `Studio Pill`, `Compact Icons Only`, and `Floating Dock` bottom navigation bar styles; `3-Tab Studio` vs. `Single Scroll` lesson layout; `Smart Phone Fit`, `Mobile Cards`, and `Classic Table` display modes; and individual toggles for every Home screen widget plus custom Home Greeting & Studio Subtitle text!
