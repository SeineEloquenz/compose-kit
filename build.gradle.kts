import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin
import org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.wasm.nodejs.WasmNodeJsRootPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.yarn.WasmYarnPlugin
import org.jetbrains.kotlin.gradle.targets.wasm.yarn.WasmYarnRootEnvSpec

plugins {
    alias(libs.plugins.android.kotlin.multiplatform.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.jetbrains.kotlin.plugin.compose) apply false
    alias(libs.plugins.ktlint)
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    outputToConsole.set(true)
    outputColorName.set("RED")
}

// When KOTLIN_NODE_HOME is set (by shell.nix) we pin the node version and disable the download;
// shell.nix symlinks the matching `node-v<KOTLIN_NODE_VERSION>-<os>-<arch>` dir at the Nix node
// (which, unlike the binary Kotlin would download, is runnable on NixOS). The version is cosmetic
// (it only names that dir) but pinning keeps the symlink stable across KGP upgrades. Without the env
// var (e.g. CI) Kotlin downloads node as usual.
val nodeHome = providers.environmentVariable("KOTLIN_NODE_HOME").orNull?.takeIf { it.isNotBlank() }
if (nodeHome != null) {
    val pinnedNode = providers.environmentVariable("KOTLIN_NODE_VERSION").orNull?.takeIf { it.isNotBlank() } ?: "25.0.0"
    plugins.withType<WasmNodeJsRootPlugin> {
        the<WasmNodeJsEnvSpec>().apply {
            download.set(false)
            version.set(pinnedNode)
        }
    }
    plugins.withType<NodeJsRootPlugin> {
        the<NodeJsEnvSpec>().apply {
            download.set(false)
            version.set(pinnedNode)
        }
    }
    plugins.withType<WasmYarnPlugin> { the<WasmYarnRootEnvSpec>().download.set(false) }
    plugins.withType<YarnPlugin> { the<YarnRootEnvSpec>().download.set(false) }
}
