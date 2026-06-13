import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.jetbrains.kotlin.plugin.compose)
    alias(libs.plugins.ktlint)
    `maven-publish`
}

group = "nz.eloque"
version = project.findProperty("VERSION") as String? ?: "0.0.0-SNAPSHOT"

kotlin {
    android {
        namespace = "nz.eloque.compose_kit"
        compileSdk = 37
        buildToolsVersion = "37.0.0"
        minSdk = 28

        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        androidResources {
            enable = true
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.material.icons.extended)
            implementation(libs.compose.ui)
            implementation(libs.compose.components.resources)
            implementation(libs.compose.components.ui.tooling.preview)
        }
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            // Material3 Expressive (FloatingActionButtonMenu etc.) used by FabMenu — Android-only
            // for now, as Compose Multiplatform 1.11 doesn't yet expose these APIs.
            implementation(libs.androidx.material3)
            implementation(libs.coil.compose)
            implementation(libs.accompanist.permissions)
        }
    }
}

// Compose Multiplatform resources: generates a public `Res` class so composables can use
// `stringResource(Res.string.compose_kit_*)` across all targets.
compose.resources {
    publicResClass = true
    packageOfResClass = "nz.eloque.compose_kit.resources"
}

// Don't lint generated sources (e.g. the Compose resource accessors).
configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    filter {
        exclude { it.file.path.contains("generated") }
    }
}
