import org.gradle.api.Plugin
import org.gradle.api.artifacts.dsl.RepositoryHandler
import org.gradle.api.initialization.Settings
import org.gradle.kotlin.dsl.maven

class RepositoriesPlugin : Plugin<Settings> {
    override fun apply(target: Settings) {
        target.pluginManagement.apply {
            repositories.applyPluginsRepositories()
        }
    }
}

fun RepositoryHandler.applyPluginsRepositories() {
    gradlePlugins()
    jitpack()
    mavenCentralAndGoogle()
}

fun RepositoryHandler.applyMainRepositories() {
    mavenLocal()
    mavenCentralAndGoogle()
    jitpack()
}

private interface MavenRepos {
    fun RepositoryHandler.mavenCentralAndGoogle()
    fun RepositoryHandler.gradlePlugins()

    fun RepositoryHandler.jitpack()
}

private object DirectMavenRepos : MavenRepos {
    override fun RepositoryHandler.mavenCentralAndGoogle() {
        mavenCentral()
        google()
    }

    override fun RepositoryHandler.gradlePlugins() {
        gradlePluginPortal()
    }

    override fun RepositoryHandler.jitpack() {
        maven("https://jitpack.io")
    }

}
private fun selectMavenRepos(action: MavenRepos.() -> Unit) {
//    val isCi: Boolean = System.getenv("CI").orEmpty().toBooleanStrictOrNull() == true
//    if (isCi) CachedMavenRepos else
    val repos =  DirectMavenRepos
    return action.invoke(repos)
}

fun RepositoryHandler.mavenCentralAndGoogle() = selectMavenRepos { mavenCentralAndGoogle() }
fun RepositoryHandler.jitpack() = selectMavenRepos { jitpack() }
fun RepositoryHandler.gradlePlugins() = selectMavenRepos { gradlePlugins() }
