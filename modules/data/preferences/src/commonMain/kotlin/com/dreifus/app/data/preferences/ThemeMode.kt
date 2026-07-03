package com.dreifus.app.data.preferences

/** How the app resolves light vs. dark colors. */
enum class ThemeMode {
    /** Follow the device system setting. */
    System,

    /** Always use the light theme. */
    Light,

    /** Always use the dark theme. */
    Dark,
}
