# Kadi Companion App (v2.1.0) — Deep-Dive Tutorial & GitHub Actions CI/CD Edition

An offline-first Android companion app (Kotlin + Jetpack Compose + Material 3 + Room + Hilt) that teaches programming and **Godot 4.3** game development step by step by building the Kenyan card game **Kadi**.

## What's New in v2.1.0

1. **Deeply Expanded 47-Lesson Curriculum (`app/src/main/assets/tutorial/phase_00.json` – `phase_11.json`)**:
   - Every lesson now includes:
     - **How This Lesson Fits Into the Full Kadi Game** summary table (Phase, Target `res://` File, Scene/Node Context, and Problem Solved).
     - **Detailed Godot 4.3 Editor & Inspector Guide** explaining every click, dock, and property.
     - **Mental Models & Concrete Kadi Table Walkthroughs** tracing how variables and arrays change step by step with real card examples.
     - **Line-by-Line GDScript Code Walkthrough Tables** breaking down every single line of code in plain English so no keyword, annotation, or symbol is ever left unexplained.
     - **"Verify It Works Right Now (`F5` / `F6`)" Cards (`verificationSteps`)** showing how to test each lesson immediately in Godot.
     - **"Stuck? Common Errors & Instant Fixes" Cards (`troubleshootingTips`)** explaining the exact compiler/runtime errors beginners encounter and how to fix them in seconds.
2. **Markdown Table Rendering (`MarkdownText.kt`) & Enhanced Lesson Cards (`LessonDetailScreen.kt`)**:
   - `MarkdownText.kt` now parses and renders styled, horizontally scrollable Markdown tables (`| Col 1 | Col 2 |`) alongside syntax-highlighted `CodeBlock` snippets and 5 callout types (`[!GODOT]`, `[!KADI]`, `[!WHY]`, `[!MISTAKE]`, `[!TIP]`).
   - `LessonDetailScreen.kt` renders dedicated **Target File (`res://...`)**, **Scene Tree Hierarchy**, **Interactive Step-by-Step Editor Checklist**, **Verify It Works Right Now**, **Troubleshooting & Error Fixes**, and **Key Takeaways** cards.
3. **Interactive GDScript Practice Workbench (`PracticeScreen.kt`) & 12 Line-Tap Bug Hunts (`DebugChallengeScreen.kt`)**:
   - Live GDScript editor with starter code scaffolds, mobile quick-insert token bar, and a **"Check My Code"** validator.
   - Interactive line-tap bug hunt on all 12 phases.
4. **Interactive Project Blueprint & Kadi Rule Simulator (`ProjectBlueprintScreen.kt`)**:
   - 3-tab architectural guide (`Project Map`, `Scene & Signals`, and `Rule Simulator`).
5. **Automated GitHub Actions APK Build & Release Workflow (`.github/workflows/build-apk.yml`)**:
   - Automatically builds both **Debug (`kadi-companion-debug.apk`)** and **Signed Release (`kadi-companion-release-signed.apk`)** APKs on every push, pull request, or manual **Run workflow** dispatch using JDK 17 (Temurin) and uploads them as downloadable artifacts!
   - Pushing a version tag (e.g., `git tag v2.1.0 && git push origin v2.1.0`) automatically publishes a GitHub Release with both APKs attached.
6. **Complete Ready-to-Run Godot 4.3 Reference Project (`godot_kadi_reference/`)**:
   - Open `godot_kadi_reference/project.godot` in **Godot 4.3 Standard** and press **F5** to play or compare against your own files anytime.

---

## Building the APK via GitHub Actions (Zero Local Setup Required)

1. Push this repository to GitHub.
2. Open the **Actions** tab in your GitHub repository and select **Build Android APK**.
3. Every push (or clicking **Run workflow**) automatically runs `./gradlew :app:assembleDebug :app:assembleRelease` on Ubuntu with JDK 17, signs the release APK, and uploads:
   - `kadi-companion-debug-apk` (`kadi-companion-debug.apk`)
   - `kadi-companion-release-signed-apk` (`kadi-companion-release-signed.apk`)
4. *(Optional)* To sign release APKs with your own production keystore instead of the automatic CI keystore, add these 4 repository secrets under **Settings → Secrets and variables → Actions**:
   - `KEYSTORE_BASE64` (base64-encoded `.jks` file)
   - `KEYSTORE_PASSWORD`
   - `KEY_ALIAS`
   - `KEY_PASSWORD`

---

## Building the APK Locally

Gradle 8.7 and Kotlin 1.9.24 require **JDK 17** or **JDK 21**. (Note: We also removed the hardcoded Windows `org.gradle.java.home` path from `gradle.properties` so both Linux CI and local `JAVA_HOME` work seamlessly.)

```bash
export JAVA_HOME=/path/to/jdk-17
./gradlew :app:assembleDebug
```
