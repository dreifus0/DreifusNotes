plugins {
    id("com.dreifus.kmp-compose-library")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.dreifus.app.features.counter"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.mvucore)
            implementation(projects.modules.utils.arch)
            implementation(projects.modules.utils.uikit)
            implementation(projects.modules.utils.coreNavigation)
            api(projects.modules.data.counter)
        }
    }
}
