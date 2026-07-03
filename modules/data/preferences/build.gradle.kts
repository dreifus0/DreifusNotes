plugins {
    id("com.dreifus.kmp-library")
}

android {
    namespace = "com.dreifus.app.data.preferences"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
        }
    }
}
