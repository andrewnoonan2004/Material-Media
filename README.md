# Material Media 🎬🎵

[![Android Min SDK](https://img.shields.io/badge/minSdk-26-blue.svg)](https://developer.android.com/about/versions/oreo)
[![Target SDK](https://img.shields.io/badge/targetSdk-36-green.svg)](https://developer.android.com)
[![Material Components](https://img.shields.io/badge/Material_Components-1.14.0-blueviolet.svg)](https://github.com/material-components/material-components-android)
[![Material 3 Expressive](https://img.shields.io/badge/Material_3-Expressive_%2B_Material_You-orange.svg)](https://m3.material.io)
[![LibVLC Engine](https://img.shields.io/badge/LibVLC-3.7.7-red.svg)](https://code.videolan.org/videolan/libvlcjni)
[![License](https://img.shields.io/badge/License-GPL_v2-blue.svg)](COPYING)

**Material Media** (`app.material.media`) is an open-source, full-featured universal media player for Android built with **Material 3 Expressive** (`com.google.android.material:material:1.14.0`) and **Material You Wallpaper Dynamic Color** adaptation. Powered by the LibVLC (`3.7.7`) and Medialibrary (`0.13.24`) engines, Material Media plays virtually every video and audio codec, container format, subtitle track, and network stream while adapting its entire visual identity—surfaces, active navigation indicators, sliders, cards, and iconography—to your Android wallpaper.

---

## 🌟 Highlights & Features

### 1. 🎨 Material 3 Expressive Design System
- **Official Material 3 Expressive Theme Hierarchy**: Built on `Theme.Material3Expressive.Light.NoActionBar` and `Theme.Material3Expressive.Dark.NoActionBar` from Material Components `1.14.0`.
- **Expressive Navigation & Controls**:
  - `Widget.Material3Expressive.BottomNavigationView` and `Widget.Material3Expressive.NavigationRailView` with pill-shaped active indicators (`Widget.Material3Expressive.*.ActiveIndicator`).
  - `Widget.Material3Expressive.FloatingActionButton` with tonal primary container elevation and dynamic ripple feedback.
  - `Widget.Material3Expressive.Slider.Xsmall` for media seekbars, audio/video controls, and equalizer band faders.
  - Expressive `16dp` rounded media cards (`VLCCardView` and audio browser cover cards) and `28dp` modal bottom sheets (`AppBottomSheetDialogTheme`).
- **Spatial Spring Physics**: Standardized Material 3 Expressive spatial spring specs (`stiffness = 380f, dampingRatio = 0.8f` for default spatial motion; `stiffness = 800f, dampingRatio = 0.6f` for fast spatial motion) exposed via `Material3ExpressiveThemeManager`.

### 2. 🌈 Material You Wallpaper Dynamic Color Adaptation
- **Full System Accent & Surface Extraction (Android 12+ / API 31+)**:
  - Dynamically applies `ThemeOverlay.Material3Expressive.DynamicColors.Light` and `ThemeOverlay.Material3Expressive.DynamicColors.Dark` across all activities via `DynamicColors.applyToActivitiesIfAvailable` and `Material3ExpressiveThemeManager`.
  - Bridges all custom theme attributes (`?attr/background_default`, `?attr/background_actionbar`, `?attr/bottom_navigation_background`, `?attr/bottom_sheet_background`, `?attr/card_background`, `?attr/audio_header_background`, `?attr/primary_focus`, etc.) to Material 3 dynamic color tokens (`?attr/colorSurface`, `?attr/colorSurfaceContainer*`, `?attr/colorPrimary`, `?attr/colorSecondaryContainer`).
  - Maps `@color/orange50`–`@color/orange900` in `values-v31/colors.xml` and `values-night-v31/colors.xml` directly to `@android:color/system_accent1_*` and `@android:color/system_neutral1_*` so every vector drawable, progress bar, waveform, and state selector automatically reflects your system wallpaper palette.
- **Instant User Preference Toggle**:
  - Includes a **Material You wallpaper colors** toggle under **Settings → Interface** (`enable_dynamic_color`, enabled by default) to switch between your live wallpaper palette and classic high-contrast static accents at any time.

### 3. 🎬 Universal Video Library & Hardware-Accelerated Player
- **All-Codec Playback**: Plays MKV, MP4, AVI, MOV, WebM, FLV, TS, FLAC, ALAC, OPUS, MP3, AAC, OGG, and more out of the box with hardware decoding and multi-threaded software fallback.
- **Advanced Video Player Controls**:
  - Gesture controls for brightness, volume, double-tap seeking, and pinch-to-zoom.
  - Multi-track audio & subtitle selection (including ASS/SSA, SRT, VobSub, and online subtitle downloads).
  - Picture-in-Picture (PiP) mode, background audio-only video playback, variable playback speed (`0.25x`–`4.00x`), A-B repeat, and sleep timer.

### 4. 🎵 Audio Library, Mini-Player & Multi-Band Equalizer
- **Organized Audio Browser**: Browse by Artists, Albums, Tracks, Genres, and Playlists with responsive grid cards and fast scrollers.
- **Persistent Expandable Mini-Player**: Seamless transition between the bottom mini-player bar and the full-screen Material 3 Expressive audio player with play queue management.
- **Built-In Equalizer & Audio DSP**: Multi-band graphic equalizer with preamp control, built-in acoustic presets, custom preset persistence, and audio ducking options.

### 5. 🌐 File Browser, Network Streams & Remote Access
- **Integrated File & Storage Browser**: Browse internal storage, SD cards, USB OTG drives, and favorite folders.
- **Network & Stream Playback**: Built-in support for SMBv2/v3, FTP, SFTP, NFS, UPnP/DLNA media servers, and direct HTTP/RTSP/HLS/DASH network stream URLs.

### 6. ✨ Clean "Material Media" Identity
- **Independent Package ID**: Configured with `appId = "app.material.media"` (`app.material.media.debug` for debug builds) so it installs cleanly alongside any other media player on your device.
- **Material 3 Expressive Play & Waveform Emblem**: Custom adaptive launcher icon (foreground, dynamic gradient background, and Android 13+ monochrome themed icon) and unified in-app vector iconography with zero legacy traffic cone branding.

---

## 🏗️ Architecture & Project Structure

```text
Material-Media/
├── build.gradle                         # Root build configuration (appId = "app.material.media", Material 1.14.0)
├── settings.gradle                      # Module definitions & SDK levels (minSdk = 26, targetSdk = 36)
└── application/
    ├── app/                             # Main Android application packaging module
    │   ├── build.gradle                 # Variant configuration & Material-Media-*.apk output naming
    │   └── src/main/java/org/videolan/mobile/app/
    │       └── AppSetupDelegate.kt      # Application init & DynamicColors lifecycle registration
    ├── resources/                       # Shared vector drawables, colors, and localized strings
    │   └── src/main/res/
    │       ├── drawable/                # Material Media Play & Waveform vector emblems & icons
    │       ├── values/
    │       │   ├── colors.xml           # Base palette & static fallback tokens
    │       │   └── strings.xml          # Rebranded Material Media strings & UI labels
    │       ├── values-v31/
    │       │   └── colors.xml           # Android 12+ Light dynamic wallpaper color mappings (system_accent1_*)
    │       └── values-night-v31/
    │           └── colors.xml           # Android 12+ Dark dynamic wallpaper color mappings (system_accent1_*)
    ├── tools/                           # Shared preferences & coroutine utilities
    │   └── src/main/java/org/videolan/tools/
    │       └── Settings.kt              # KEY_DYNAMIC_COLOR ("enable_dynamic_color") preference management
    ├── mediadb/                         # Room database for playlists, history, and equalizer presets
    ├── live-plot-graph/                 # Real-time graph plotting view module
    ├── television/                      # Android TV / Leanback UI module
    ├── remote-access-server/            # Web/remote playback control server module
    └── vlc-android/                     # Core mobile UI, playback service, and ViewModels
        ├── res/
        │   ├── layout/
        │   │   ├── main.xml             # M3 Expressive BottomNavigationView, NavigationRailView & FAB
        │   │   ├── navigation_rail_fab.xml
        │   │   └── audio_browser_card_item.xml
        │   ├── values/
        │   │   └── styles.xml           # Theme.Material3Expressive.* hierarchy & dynamic color overlays
        │   ├── values-night/
        │   │   └── styles.xml           # Dark M3 Expressive theme & surface container mappings
        │   └── xml/
        │       └── preferences_ui.xml   # Interface settings with Material You wallpaper colors toggle
        ├── src/org/videolan/vlc/
        │   └── gui/
        │       ├── helpers/
        │       │   ├── Material3ExpressiveThemeManager.kt  # Dynamic color & M3 Expressive theme engine
        │       │   └── UiTools.kt                          # Activity theme application & UI binding adapters
        │       └── preferences/
        │           └── PreferencesUi.kt                    # Live preference change handling & restart dialog
        └── test/org/videolan/vlc/gui/helpers/
            └── Material3ExpressiveThemeManagerTest.kt      # JVM unit tests for theme, spring & brand rules
```

---

## 🛠️ Prerequisites & Requirements

- **Android Device**: Android 8.0 Oreo (API 26) or higher; Android 12+ (API 31+) for live Material You wallpaper color extraction (tested through Android 16 / API 36).
- **Development Toolchain**:
  - **JDK**: Java 21 or Java 17.
  - **Android SDK**: Platform `android-36`, Build-Tools `35.0.0+`.
  - **Gradle**: `9.8.1` with Android Gradle Plugin (AGP) `9.4.1` and Kotlin `2.2.10`.
- **Prebuilt Native Dependencies**:
  - Automatically resolved from Maven Central (`org.videolan.android:libvlc-all:3.7.7` and `org.videolan.android:medialibrary-all:0.13.24`)—no NDK C/C++ cross-compilation required for UI builds.

---

## 🚀 Building & Testing

### 1. Configure `local.properties`
Ensure `local.properties` in the project root points to your Android SDK:
```properties
sdk.dir=/path/to/your/android/sdk
```

### 2. Run Unit Tests
Run the JVM unit test suite verifying `Material3ExpressiveThemeManager` (dynamic color eligibility, night mode resolution, Material 3 Expressive overlay selection, spatial spring physics, and Material Media brand identity):
```bash
./gradlew :application:vlc-android:testDebugUnitTest \
  --tests "org.videolan.vlc.gui.helpers.Material3ExpressiveThemeManagerTest" \
  --project-cache-dir="$HOME/.cache/projects/$(basename "$PWD")-$(pwd -P | sha1sum | cut -c1-8)/gradle"
```

### 3. Build the Debug APK
```bash
./gradlew :application:app:assembleDebug \
  --project-cache-dir="$HOME/.cache/projects/$(basename "$PWD")-$(pwd -P | sha1sum | cut -c1-8)/gradle"
```
> **Note on Build Cache Redirection**: To maximize build speed on shared/FUSE storage, `build.gradle` redirects intermediate build outputs (`layout.buildDirectory`) to `$HOME/.cache/projects/vlc-android-build/`.

The compiled universal APK (`Material-Media-3.7.2-debug-all.apk`) is generated at:
```text
$HOME/.cache/projects/vlc-android-build/_application_app/outputs/apk/debug/Material-Media-3.7.2-debug-all.apk
```
and copied to:
```text
application/app/build/outputs/apk/debug/Material-Media-3.7.2-debug-all.apk
```

---

## 💡 How to Use & Customize Theme

1. **Install & Launch**:
   - Open `Material-Media-3.7.2-debug-all.apk` from your file manager to install **Material Media** (`app.material.media.debug`).
   - Grant media permissions on first launch to scan your video and audio libraries, or browse files directly via the **Browse** tab.
2. **Material You Wallpaper Adaptation**:
   - On Android 12+, **Material Media** automatically adapts its primary accent, active navigation pill, floating action button, sliders, and surface containers to your current wallpaper.
   - Change your system wallpaper or system color palette and reopen the app to see the entire UI update automatically.
3. **Toggle Wallpaper Colors or Day/Night Mode**:
   - Go to **More → Settings → Interface**.
   - Use **DayNight mode** (`Follow system`, `Light`, or `Dark`) to control the surface brightness.
   - Toggle **Material You wallpaper colors** (`enable_dynamic_color`) to switch between your dynamic wallpaper palette and the classic static theme.

---

## 📜 License & Attribution

**Material Media** is free and open-source software licensed under the **GNU General Public License v2.0 (or later)**, built upon the open-source [VLC for Android / LibVLC](https://code.videolan.org/videolan/vlc-android) project by VideoLAN and its contributors. See [COPYING](COPYING) for the full license text.
