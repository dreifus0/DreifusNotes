plugins {
    id("com.dreifus.kmp-library")
    alias(libs.plugins.metro)
    alias(libs.plugins.sqldelight)
}

android {
    namespace = "com.dreifus.app.data.notes"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.sqldelight.coroutines)
            implementation(libs.kotlinx.datetime)
        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android.driver)
            implementation(libs.sqlcipher.android)
            implementation(libs.androidx.security.crypto)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native.driver)
        }
    }
}

sqldelight {
    databases {
        create("NotesDatabase") {
            packageName.set("com.dreifus.app.data.notes.db")
        }
    }
}
