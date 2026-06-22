# compose-kit

A Compose Multiplatform component library targeting Android and iOS.

## Installation

Artifacts are published to a static Maven repository hosted on this repo's `maven` branch.

### 1. Add the repository

In `settings.gradle.kts`, under `dependencyResolutionManagement`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://raw.githubusercontent.com/SeineEloquenz/compose-kit/maven/")
    }
}
```

### 2. Declare the dependency in your version catalog

In `gradle/libs.versions.toml`:

```toml
[versions]
compose-kit = "x.y.z"

[libraries]
compose-kit = { module = "nz.eloque.compose-kit:lib", version.ref = "compose-kit" }
```

### 3. Add the dependency

In your module's `build.gradle.kts`:

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.kit)
        }
    }
}
```

Gradle module metadata resolves the correct Android and iOS variants automatically, so the single
`commonMain` dependency above is all you need.
