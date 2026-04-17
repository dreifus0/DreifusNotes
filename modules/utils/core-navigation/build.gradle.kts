plugins {
    id("com.dreifus.kmp-compose-library")
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.metro)
}

android {
    namespace = "com.dreifus.template.core.navigation"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.navigation3.ui)
            api(libs.lifecycle.viewmodel.navigation3)
            api(libs.metrox.viewmodel.compose)
            implementation(projects.modules.utils.uikit)
            implementation(libs.kotlin.serialization)
            implementation(libs.haze)
        }
    }
}
