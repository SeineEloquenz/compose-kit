pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    // No FAIL_ON_PROJECT_REPOS: the Kotlin/Wasm tooling adds an Ivy repository at project scope when
    // downloading the Node.js/Yarn distributions, which a strict mode would reject ("repository '...'
    // was added by unknown code"). Leaving the default (PREFER_PROJECT) lets it do so.
    repositories {
        google {
            mavenContent {
                includeGroupByRegex(".*google.*")
                includeGroupByRegex(".*android.*")
            }
        }
        mavenCentral()
    }
}

rootProject.name = "compose-kit"
include(":lib")
