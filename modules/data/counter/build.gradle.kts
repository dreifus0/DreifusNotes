plugins {
    id("com.dreifus.kmp-library")
    alias(libs.plugins.metro)
}

android {
    namespace = "com.dreifus.app.data.counter"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
        }
    }
}
