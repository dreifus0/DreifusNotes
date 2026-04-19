# Dreifus Notes

A cross-platform notes app for Android and iOS, built with Kotlin Multiplatform. Supports PIN-protected notes, block-based content (text, photos, checklists), and biometric-confirmed data reset.

Built as a real-world example of the [KMP App Template](https://github.com/dreifus0/KmpAppTemplate).

## Features

- **Notes list** — card grid with color coding
- **Note detail** — block-based editor (text, photo, checklist)
- **PIN protection** — per-note 4-digit PIN lock with biometric unlock
- **Settings** — appearance, data reset with biometric confirmation
- **Encrypted storage** — SQLCipher on Android, SQLite on iOS
- **100% shared UI** — Compose Multiplatform for both platforms

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Kotlin | 2.3.0 | Language |
| Compose Multiplatform | 1.10.0 | Shared UI (Android + iOS) |
| Material3 | 1.10.0-alpha05 | Design system |
| Navigation 3 | 1.0.0-alpha06 | Screen navigation |
| Metro DI | 0.10.2 | Dependency injection |
| MetroX ViewModel | 0.10.2 | Metro + ViewModel integration |
| MVU Core | 0.3.0 | Model-View-Update architecture |
| SQLDelight | 2.0.2 | Local database |
| SQLCipher | 4.5.4 | Encrypted database (Android) |
| Coroutines | 1.10.1 | Async / concurrency |
| kotlinx-datetime | 0.6.1 | Date/time handling |
| Kotlin Serialization | 1.8.0 | JSON serialization |
| Napier | 2.7.1 | KMP logging |
| KSP | 2.3.0-2.0.2 | Kotlin Symbol Processing |
| AGP | 9.1.1 | Android build |
| Gradle | 9.3.1 | Build system |

## Project Structure

```
DreifusNotes/
├── composeApp/                         # App entry point
│   ├── src/commonMain/                 #   App.kt, DI graph, tab setup
│   ├── src/androidMain/               #   MainActivity, splash, icons
│   └── src/iosMain/                   #   MainViewController
│
├── modules/
│   ├── data/
│   │   └── notes/                     # Notes data layer
│   │       ├── sqldelight/            #   SQL schema (Note, NoteBlock)
│   │       └── kotlin/                #   NotesRepository, models
│   │
│   ├── features/
│   │   ├── notes/                     # Notes feature
│   │   │   ├── main/                  #   NotesListScreen
│   │   │   ├── create/                #   CreateNoteScreen
│   │   │   ├── detail/                #   NoteDetailScreen (blocks, photo, checklist)
│   │   │   └── pin/
│   │   │       ├── setup/             #   PinSetupScreen
│   │   │       └── lock/              #   PinLockScreen
│   │   │
│   │   └── settings/                  # Settings feature
│   │       ├── main/                  #   SettingsScreen
│   │       └── biometric/             #   BiometricAuthEffect (expect/actual)
│   │
│   └── utils/
│       ├── arch/                      # LceState (Loading/Content/Error)
│       ├── helpers/                   # Utilities
│       ├── core-extensions/           # Kotlin/Compose extensions
│       ├── uikit/                     # Design system (theme, icons, components)
│       └── core-navigation/           # NavController, tabs, screen types
│
├── includedBuild/
│   ├── gradle-configs/                # Convention plugins (kmp-compose-library/application)
│   └── shared-consts/                 # Shared build constants
│
└── gradle/libs.versions.toml          # Version catalog
```

## Architecture

**Maximize shared code** — UI, navigation, DI, and business logic all live in `commonMain`. Platform-specific code is limited to entry points and `expect/actual` declarations.

**MVU (Model-View-Update)** — unidirectional data flow per screen:

```
Event → Update (pure) → State + Command + Effect
                Command → CommandHandler → Event
```

Each screen has: `State`, `Event`, `Command`, `Effect`, `Update`, `CommandHandler`. No domain layer — `CommandHandler` replaces use cases.

**Metro DI** — `AppGraph` with constructor injection via `@Inject`. ViewModels registered with `@ViewModelKey` and retrieved with `metroViewModel<T>()`.

**Navigation 3** — three stacks: `regular`, `dialog`, `bottomSheet`. Tab navigation preserves per-tab backstack via `TabNavState`.

## Getting Started

### Requirements

- JDK 17+
- Android Studio Meerkat or later
- Xcode 16+ (for iOS)
- Android SDK 24+

### Build

```bash
# Android debug APK
./gradlew :composeApp:assembleDebug

# Verify both platforms compile
./gradlew :composeApp:compileDebugKotlinAndroid :composeApp:compileKotlinIosSimulatorArm64
```

For iOS: open `iosApp/iosApp.xcodeproj` in Xcode and run on a simulator or device.

## License

Apache 2.0 — see [LICENSE](LICENSE)

## Privacy Policy

See [PRIVACY_POLICY.md](PRIVACY_POLICY.md)
