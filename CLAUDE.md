# DreifusNotes

KMP app (Android + iOS) — notes with PIN protection.

## Stack

- Kotlin 2.3.0, Compose Multiplatform 1.10.0
- Metro 0.10.2 (DI)
- mvucore 0.3.0 (MVU architecture)
- Navigation3 1.0.0-alpha06
- Haze 1.7.2 (glassmorphism — TabBar)
- Napier 2.7.1 (logging, debug-only)
- kotlinx.serialization
- Targets: Android (min 24, target 36), iOS (arm64, simulatorArm64)

## Module structure

```
composeApp/                         — app entry point
modules/
├── data/
│   └── counter/                    — data layer (placeholder)
├── features/
│   ├── counter/                    — placeholder screen (future: Notes)
│   └── stub/                       — placeholder screen (future: Settings)
└── utils/
    ├── arch/                       — LceState (Loading/Content/Error)
    ├── helpers/                    — utilities
    ├── core-extensions/            — extension functions
    ├── uikit/                      — theme, icons, shared UI components
    └── core-navigation/            — navigation (NavController, screens, TabBar)
includedBuild/
├── gradle-configs/                 — convention plugins
└── shared-consts/                  — Packages.app = "com.dreifus.app"
```

## Convention plugins

- `com.dreifus.kmp-compose-library` — for feature/utils modules
- `com.dreifus.kmp-compose-application` — for composeApp

Both apply: kotlin-multiplatform, compose, compose-compiler. Targets: androidTarget (JVM 11), iosArm64, iosSimulatorArm64.

## Creating a new feature module

1. Create `modules/features/<name>/build.gradle.kts`:
   ```kotlin
   plugins {
       id("com.dreifus.kmp-compose-library")
       alias(libs.plugins.metro)
   }
   android { namespace = "com.dreifus.app.features.<name>" }
   kotlin {
       sourceSets {
           commonMain.dependencies {
               implementation(libs.mvucore)
               implementation(projects.modules.utils.arch)
               implementation(projects.modules.utils.uikit)
               implementation(projects.modules.utils.coreNavigation)
           }
       }
   }
   ```
2. Add to `settings.gradle.kts`: `include(":modules:features:<name>")`
3. Internal structure:
   ```
   src/commonMain/kotlin/com/dreifus/app/features/<name>/
   ├── data/           — repositories, mapping
   └── presentation/   — Screen, ViewModel (MVU), UI components
   ```
4. Add dependency to `composeApp/build.gradle.kts`

## MVU pattern (mvucore)

Each feature is implemented through MVU (Model-View-Update):

- **State** — immutable data class, current screen state
- **Event** — sealed interface, user actions (click, input)
- **Command** — sealed interface, side-effect requests (data loading, navigation)
- **Effect** — sealed interface, one-shot UI effects (toast, scroll)
- **Update** — pure function `(State, Event) → Next<State, Command, Effect>`. No side-effects
- **CommandHandler** — command processor, replaces usecases. Performs IO and returns Events

No domain layer. CommandHandler replaces usecases.

## DI (Metro)

### Graph and scopes

- `@DependencyGraph(AppScope::class)` — root graph (`AppGraph`)
- `AppGraph.Factory` with `@Provides` — platform dependencies only
- `AppGraph : ViewModelGraph` — wires Metro ViewModel factory
- `AppViewModelFactory` (`@Inject @ContributesBinding @SingleIn(AppScope)`) — MetroViewModelFactory implementation

### Annotations

| Annotation | Purpose | Example |
|-----------|---------|---------|
| `@Inject` | Constructor injection | `@Inject class MyRepo(...)` |
| `@SingleIn(AppScope::class)` | Singleton in AppScope | Repositories, services |
| `@ContributesBinding(AppScope::class)` | Bind impl to interface | `@ContributesBinding(...) class RepoImpl : Repo` |
| `@Provides` | Manual dependency creation | Only in AppGraph.Factory |

### Rules

1. **Repositories always through DI.** `@Inject @SingleIn(AppScope::class)`. NEVER `val repo = SomeRepository()`.
2. **All ViewModels (except RootViewModel) through Metro.** Required: `@Inject`, `@ViewModelKey(FeatureVM::class)`, `@ContributesIntoMap(AppScope::class)`. Created via `metroViewModel<T>()`.
3. **RootViewModel is the only exception.** Creates the DI graph. Not through Metro.
4. **No prop drilling of DI dependencies through composables.**
5. **Metro plugin** (`alias(libs.plugins.metro)`) — required in any module with `@Inject` classes.

## ViewModel architecture

| Level | VM | Creation | Responsibility |
|-------|-----|----------|----------------|
| App | RootViewModel | `viewModel { RootViewModel() }` | DI graph + routing |
| Feature | FeatureVM | `metroViewModel<T>()` | MVU: state, commands, effects |

## Navigation

Three stacks via `NavControllersHolder`:
- `regular` — main screen stack
- `dialog` — dialogs (overlay)
- `bottomSheet` — bottom sheets (overlay)

Screen types: `RegularScreen`, `DialogScreen`, `BottomSheetScreen`.

Navigation: `navController.navigate(MyScreen())`, `navController.pop()`, `navController.replaceAll(...)`.

Tab navigation: `TabNavState` manages the active tab and preserves each tab's back stack. Tabs are defined in `composeApp/.../navigation/MainTabs.kt` and provided via `LocalTabs`.

## Design system

### Color palette (DefaultAppTheme)

| Role | Light | Dark |
|------|-------|------|
| backgroundBase | `#FFFFFF` | `#0E0E10` |
| backgroundSecondary | `#F1EFE8` | `#1E1E22` |
| contentPrimary | `#0B0B0C` | `#EDEDEE` |
| contentSecondary | `#5A5A5F` | `#9898A0` |
| contentTertiary | `#9A9A9F` | `#6A6A70` |
| contentDividers / contentBorder | `#E5E3DC` | `#2A2A2E` |
| accentPrimary / accentSecondary | `#534AB7` | `#7F77DD` |
| accentError | `#A32D2D` | `#E24B4A` |
| accentOnPrimary / accentOnSecondary | `#FFFFFF` | `#0E0E10` |

### Note card colors (NoteCardColor)

Enum: `Purple`, `Pink`, `Green`, `Orange`. Each returns `NoteCardPalette(background, title, body, date)` via `@Composable fun palette()` — automatically switches between light/dark. `NoteCardColor.Default = Purple`.

### Typography (AppTypography)

| Group | Styles |
|-------|--------|
| `heading1–5` | semiBold, 40/32/28/24/20sp |
| `headlineLarge/Medium/Small` | semiBold, 16/14/12sp |
| `bodyLarge/Medium/Small` | regular, 16/14/12sp |

### Shapes (AppShapes)

| Name | Value | Usage |
|------|-------|-------|
| `card` | 14dp | AppCard |
| `bar` | 10dp | search bar adjacent elements |
| `group` | 12dp | AppGroup |
| `rowIcon` | 6dp | icon in AppRow |
| `key` | 10dp | PinKeypad keys |
| `dialog` | 20dp | dialogs |
| `bottomSheet` | 24dp top | bottom sheets |
| `fullRounded` | 100% | Fab, Toggle |
| `dropdown` | 8dp | dropdown menus |

## UI Kit — component catalog

All components in `modules/utils/uikit/src/commonMain/`.

### Buttons (`button/`)
- `IconButton(onClick, style, size, shape, enabled, content)` — styles: `Ghost`, `Surface`, `Primary`
- `Fab(onClick, size, enabled, content)` — 56dp circle, `accentPrimary` background

### Toggle (`toggle/`)
- `Toggle(checked, onCheckedChange, enabled)` — Material Switch styled with `AppColors`

### Search (`search/`)
- `SearchBar(query, onQueryChange, placeholder, onSearch)` — active text input
- `SearchBarReadOnly(placeholder, onClick)` — tappable non-editable bar

### Note card (`card/`)
- `AppCard(title, body, date, color, isLocked, onClick)` — `isLocked = true` blurs the body text

### Top bar (`toolbar/`)
- `TopBar(title, navigationIcon, actions)` — title on the left, `actions` row on the right

### Rows (`row/`)
- `AppSectionLabel(text)` — uppercase section label
- `AppGroup(content)` — `backgroundSecondary` + `AppShapes.group` container
- `AppRow(label, iconBackground, icon, subtitle, showDivider, onClick, trailing)` — 22dp icon box + trailing slot

### PIN (`pin/`)
- `PinDots(filledCount, totalCount)` — filled dots = `accentPrimary`, empty = outlined
- `PinKeypad(onKey, onBackspace)` — 3×4 grid, ghost keys are transparent

### TabBar (`tabs/`)
- `TabBar(tabs, hazeState, currentTab, onTabClick)` — glassmorphism bar; `HazeState` comes from `.haze()` on the Surface above

### Icons (`icon/IconsPack.kt`)
Extension properties on `AppIcons` via `cachedIcon { }`. All 24px:
`ArrowLeft24`, `Close24`, `Plus24`, `Search24`, `Lock24`, `ChevronRight24`, `MoreHoriz24`, `Edit24`, `Settings24`, `Notes24`, `Fingerprint24`, `Clock24`, `Theme24`, `Palette24`, `Download24`, `Trash24`, `Backspace24`.

## String resources

KMP resources in `composeResources/values/strings.xml` (in each module's commonMain). Access via `stringResource(Res.string.key)`. Translations go in `values-<lang>/strings.xml`.

Current strings in composeApp: `tab_notes`, `tab_settings`.

## Theme rules

- All colors through `AppTheme.colors.*` only — never hardcode values
- All shapes through `AppTheme.shapes.*`
- Always use `AppTheme.*`, never `MaterialTheme.*`

## Build

```bash
./gradlew :composeApp:assembleDebug    # Android debug APK
./gradlew build                         # Full build
```

iOS: open `iosApp/iosApp.xcodeproj` in Xcode.

## Kotlin code style

- Coroutines: always rethrow `CancellationException` before a general `catch`
- Idiomatic Kotlin: scope functions, trailing commas, `?.let`
- Composable functions don't use the `View` suffix — this is Compose, not Android Views
- Only `import` at the top + short name in the body — no fully-qualified names inside files
- Previews (`@Preview`) live in the same file as the component, in commonMain

## Key files

- `composeApp/src/commonMain/.../App.kt` — root Composable
- `composeApp/src/commonMain/.../navigation/MainTabs.kt` — tab definitions
- `modules/utils/uikit/` — theme, icons, UI components
- `modules/utils/core-navigation/` — navigation, TabBar scaffold
- `modules/utils/arch/` — LceState
- `gradle/libs.versions.toml` — dependency versions
- `includedBuild/gradle-configs/` — convention plugins
- `settings.gradle.kts` — module registration
