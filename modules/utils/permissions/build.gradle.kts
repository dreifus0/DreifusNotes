plugins {
    id("com.dreifus.kmp-compose-library")
}

android {
    namespace = "com.dreifus.permissions"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
        }
    }
}
