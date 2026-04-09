# Wave AI – Jetpack Compose Android App

Konvertiert aus dem Gemini Canvas React-Design.

## Dateistruktur

```
app/src/main/java/com/example/waveai/
├── MainActivity.kt          ← Einstiegspunkt
├── WaveAiApp.kt             ← Root-Composable (Header, Pill-Nav, Overlays)
├── WaveViewModel.kt         ← Gesamter State + Logik
├── DataModels.kt            ← Stations, Podcasts, EQ-Presets, Enums
├── GeminiService.kt         ← Gemini API (HTTP + Retry)
└── ui/
    ├── HomeScreen.kt        ← Carousel + Liste + AI-Panel + Visualizer
    ├── StationCard.kt       ← Carousel-Karte + Listen-Item
    ├── PlayerBar.kt         ← Footer: Cover, Controls, Bottom-Nav
    ├── SettingsScreen.kt    ← EQ, Stream-Qualität, Sleep-Timer
    └── theme/
        └── Theme.kt         ← Farben, Typografie, Dark-Theme
```

## Setup in Android Studio

### 1. Neues Projekt erstellen
- File → New → New Project
- Template: **Empty Activity** (Compose)
- Package: `com.example.waveai`
- Min SDK: **API 26**
- Sprache: **Kotlin**

### 2. Dateien kopieren
Alle `.kt`-Dateien in den entsprechenden Package-Ordner kopieren.

### 3. build.gradle.kts anpassen
Den Inhalt von `build.gradle.kts` in deine App-Modul-Gradle-Datei einfügen.

### 4. Gemini API-Key eintragen
In `local.properties` (nicht in Git!):
```
GEMINI_API_KEY=dein_api_key_hier
```
Oder direkt in `GeminiService.kt`:
```kotlin
private const val API_KEY = "dein_api_key_hier"
```

### 5. Internet-Permission in AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### 6. Gradle Sync → Run

## Features (1:1 vom React-Design)
- ✅ Radio / Podcast / Spotify Modi
- ✅ Carousel + Listen-Ansicht
- ✅ AI DJ Talk (Gemini API)
- ✅ Visualizer-Overlay
- ✅ Equalizer mit 8 Presets + Custom
- ✅ Stream-Qualität HD / Spar
- ✅ Sleep-Timer (15 / 30 / 60 min)
- ✅ Edge-to-Edge Dark Theme
- ✅ Animierter Carousel (Spring-Animation)
