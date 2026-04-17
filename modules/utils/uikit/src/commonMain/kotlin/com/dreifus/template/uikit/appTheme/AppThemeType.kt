package com.dreifus.template.uikit.appTheme

enum class AppThemeType {
    SYSTEM,
    LIGHT,
    DARK;

    companion object {
        fun fromString(value: String?): AppThemeType = value?.let { valueOf(it) } ?: SYSTEM
    }
}
