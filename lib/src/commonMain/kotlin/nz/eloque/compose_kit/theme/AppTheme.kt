package nz.eloque.compose_kit.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    colorScheme: ColorScheme,
    typography: Typography = DefaultTypography,
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = typography,
        content = content,
    )
}
