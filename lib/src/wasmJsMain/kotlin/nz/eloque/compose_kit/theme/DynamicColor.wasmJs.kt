package nz.eloque.compose_kit.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

// The browser exposes no system dynamic-color palette, so fall back to the app's own scheme.
@Composable
actual fun dynamicColorSchemeOrNull(dark: Boolean): ColorScheme? = null
