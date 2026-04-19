plugins {
    id("com.dreifus.kmp-compose-library")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.dreifus.app.features.settings"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.mvucore)
            implementation(libs.metrox.viewmodel.compose)
            implementation(projects.modules.utils.arch)
            implementation(projects.modules.utils.uikit)
            implementation(projects.modules.utils.coreNavigation)
            implementation(projects.modules.data.notes)
        }
    }
}
