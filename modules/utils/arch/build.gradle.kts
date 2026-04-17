plugins {
    id("com.dreifus.kmp-compose-library")
}

android {
    namespace = "com.dreifus.arch"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
        }
    }
}
