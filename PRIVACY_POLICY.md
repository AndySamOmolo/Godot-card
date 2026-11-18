# Privacy Policy — Kadi Companion

**Effective Date:** November 18, 2026

Kadi Companion is a **100% offline-first** educational Android application for learning **Godot 4.3**, **GDScript**, and **Computer Science** fundamentals.

## 1. Zero Network Data Collection
- Kadi Companion does **not** request the `android.permission.INTERNET` permission.
- The app contains **zero** analytics SDKs, crash-reporting trackers, advertising identifiers, or third-party telemetry scripts.
- No personal data, code snippets, quiz scores, or usage metrics ever leave your device over the network.

## 2. Local On-Device Storage
All user state is stored strictly on your device inside the Android application sandbox:
- **Room SQLite Database (`kadi_companion.db`)**: Stores completed lesson IDs, quiz and debug challenge attempts, learner track selection, bookmarks (including optional personal study notes), and the local offline Godot documentation search index.
- **Jetpack DataStore (`files/datastore/kadi_prefs.preferences_pb`)**: Stores your UI studio customization preferences (theme, accent palette, font scale, layout density, and optional custom Home greeting labels).

## 3. Device-to-Device Transfer & Cloud Backup
- Android's built-in device-to-device transfer is supported so you do not lose your study progress when switching phones.
- Automated cloud backup (`<cloud-backup>`) is disabled by default so your study notes and progress remain strictly on your hardware unless you explicitly transfer devices.

## 4. User Control & Complete Data Erasure
- You can reset all UI preferences at any time via **Settings → Reset UI**.
- You can permanently erase all learning progress, quiz attempts, debug challenge logs, bookmarks, and profile data at any time via **Settings → Course & About → Reset All Learning Data**.
