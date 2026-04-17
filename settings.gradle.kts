pluginManagement {
    includeBuild("commonBuild")
    includeBuild("includedBuild")
}

plugins {
    id("repositories-plugin")
}

dependencyResolutionManagement {
    repositories.applyMainRepositories()
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
rootProject.name = "KmpTemplateApp"

include(
    ":composeApp",
    ":modules:utils:arch",
    ":modules:utils:helpers",
    ":modules:utils:core-extensions",
    ":modules:utils:uikit",
    ":modules:utils:core-navigation",
    ":modules:data:counter",
    ":modules:features:counter",
    ":modules:features:stub",
)
