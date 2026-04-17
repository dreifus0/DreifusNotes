plugins {
    id("com.dreifus.kmp-compose-library")
}

android {
    namespace = "com.dreifus.core.extensions"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
    }
}
