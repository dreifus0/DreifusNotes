plugins {
    id("com.dreifus.kmp-compose-library")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.dreifus.app.features.notes"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.mvucore)
            implementation(libs.metrox.viewmodel.compose)
            implementation(libs.kotlinx.datetime)
            implementation(projects.modules.utils.arch)
            implementation(projects.modules.utils.uikit)
            implementation(projects.modules.utils.coreNavigation)
            implementation(projects.modules.data.notes)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}
