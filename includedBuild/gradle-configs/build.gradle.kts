plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.plugin.agp)
    compileOnly(libs.plugin.kotlin)
    compileOnly(libs.plugin.compose.multiplatform)
    compileOnly(libs.plugin.compose.compiler)
    implementation(projects.sharedConsts)
}

gradlePlugin {
    plugins {
        register("kmpComposeLibrary") {
            id = "com.dreifus.kmp-compose-library"
            implementationClass = "com.dreifus.gradle.plugins.KmpComposeLibraryConventionPlugin"
        }
        register("kmpComposeApplication") {
            id = "com.dreifus.kmp-compose-application"
            implementationClass = "com.dreifus.gradle.plugins.KmpComposeApplicationConventionPlugin"
        }
        register("kmpLibrary") {
            id = "com.dreifus.kmp-library"
            implementationClass = "com.dreifus.gradle.plugins.KmpLibraryConventionPlugin"
        }
    }
}
