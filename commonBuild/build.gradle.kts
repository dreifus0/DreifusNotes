plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
}

gradlePlugin {
    plugins {
        register("RepositoriesPlugin") {
            id = "repositories-plugin"
            implementationClass = "RepositoriesPlugin"
        }
    }
}
