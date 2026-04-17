pluginManagement {
    includeBuild("../commonBuild")
}

plugins {
    id("repositories-plugin")
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }

    repositories.applyPluginsRepositories()
}

include(
    ":gradle-configs",
    ":shared-consts",
)

rootProject.name = "includedBuild"
