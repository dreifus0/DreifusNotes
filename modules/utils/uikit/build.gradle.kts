plugins {
    id("com.dreifus.kmp-compose-library")
}

android {
    namespace = "com.dreifus.template.uikit"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            api(libs.compose.runtime)
            api(libs.compose.ui)
            api(libs.compose.uiToolingPreview)
            api(libs.compose.foundation)
            api(libs.compose.material3)
            api(libs.androidx.lifecycle.runtimeCompose)
            api(libs.kotlinx.datetime)
        }
        androidMain.dependencies {
            api(libs.compose.uiTooling)
        }
    }
}
