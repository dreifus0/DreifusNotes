plugins {
    id("com.dreifus.kmp-compose-library")
}

android {
    namespace = "com.dreifus.app.features.stub"
}

kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(projects.modules.utils.uikit)
            implementation(projects.modules.utils.coreNavigation)
        }
    }
}
