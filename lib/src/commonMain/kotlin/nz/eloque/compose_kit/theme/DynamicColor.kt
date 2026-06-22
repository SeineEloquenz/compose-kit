package nz.eloque.compose_kit.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

/**
 * Platform-provided "Material You" dynamic color scheme, or `null` when unavailable.
 *
 * - Android 12+ returns the wallpaper-derived scheme; older Android returns `null`.
 * - iOS always returns `null` (no system dynamic color).
 *
 * Apps fall back to their own color scheme when this is `null`.
 */
@Composable
expect fun dynamicColorSchemeOrNull(dark: Boolean): ColorScheme?
