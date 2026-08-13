# Kadi Companion App — Source Code

Android app that teaches programming + Godot game development by building the Kenyan card game Kadi.

## What's inside

- **Language:** Kotlin 1.9.24
- **UI:** Jetpack Compose (Material 3, dark-mode-first)
- **Architecture:** 3-layer clean (UI/Domain/Data) + MVVM + StateFlow
- **DI:** Hilt 2.51.1
- **DB:** Room 2.6.1 (with FTS4 for full-text search)
- **Navigation:** Navigation Compose
- **Persistence:** DataStore
- **Serialization:** kotlinx.serialization

## Project layout

```
kadi-app/
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
├── gradle/
│   ├── libs.versions.toml      # version catalog
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew                      # 8.7
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── assets/
│       │   ├── content_version.txt
│       │   ├── tutorial/          # 12 phase JSONs
│       │   ├── glossary/terms.json   # 46 terms
│       │   └── godot_docs/          # 74 Godot class docs (.md)
│       ├── res/
│       │   ├── drawable/          # launcher foreground vector
│       │   ├── mipmap-anydpi-v26/ # adaptive launcher icon
│       │   ├── values/            # strings, colors, themes
│       │   ├── values-night/      # dark theme override
│       │   └── xml/               # backup rules
│       └── java/com/kadicompanion/
│           ├── KadiCompanionApp.kt
│           ├── MainActivity.kt
│           ├── RootViewModel.kt
│           ├── di/                # Hilt modules
│           ├── data/
│           │   ├── content/       # DTOs, ContentLoader, GodotDocParser
│           │   ├── local/
│           │   │   ├── db/        # entities, DAOs, AppDatabase
│           │   │   └── datastore/ # PreferencesRepository
│           │   └── repository/    # 6 repository impls
│           ├── domain/
│           │   ├── model/         # 8 domain models
│           │   ├── repository/    # 6 repository interfaces
│           │   └── usecase/       # 4 use cases
│           └── ui/
│               ├── theme/         # Color, Type, Theme (dark-first)
│               ├── navigation/    # Routes, BottomBar
│               ├── common/        # CodeBlock, MarkdownText, Callout, etc.
│               ├── onboarding/    # track selection
│               ├── home/
│               ├── tutorial/
│               │   ├── phaselist/
│               │   ├── lessondetail/
│               │   ├── quiz/
│               │   ├── practice/
│               │   └── debugchallenge/
│               ├── docs/
│               │   ├── doclist/
│               │   └── docdetail/
│               ├── glossary/
│               ├── bookmarks/
│               ├── insights/      # Learning Insights / My Stats
│               └── settings/
```

## How to build

### Requirements
- JDK 17 (Temurin 17.0.13+ used during development)
- Android SDK with:
  - `platform-tools`
  - `platforms;android-34`
  - `build-tools;34.0.0`

### Build commands

```bash
cd kadi-app
export JAVA_HOME=/path/to/jdk-17
export ANDROID_HOME=/path/to/android-sdk
./gradlew :app:assembleDebug       # produces app/build/outputs/apk/debug/app-debug.apk
./gradlew :app:assembleRelease    # produces release APK (needs signing config)
```

### Sign the APK

The debug APK from `assembleDebug` is signed with the Android debug keystore by default. To sign with your own keystore:

```bash
apksigner sign \
  --ks your.keystore \
  --ks-pass pass:YOUR_PASSWORD \
  --key-pass pass:YOUR_KEY_PASSWORD \
  --ks-key-alias your_alias \
  app/build/outputs/apk/debug/app-debug.apk
```

## Content authoring

All tutorial content, glossary terms, and Godot docs are in `app/src/main/assets/`. They're plain JSON/Markdown — edit them and rebuild to update the app. No code changes required for content updates.

### Schema (per phase JSON)

See `assets/tutorial/phase_00.json` for a complete reference. Key fields:
- Every lesson needs ≥ 1 `[!WHY]` callout in `bodyMarkdown`
- Phases 4, 7, 9 need a `debugChallenge` object
- Every quiz `correctChoiceIndex` must be in range of `choices.length`

## Privacy

No permissions, no network calls, no telemetry. All progress/bookmark/quiz data lives on-device in Room + DataStore.
