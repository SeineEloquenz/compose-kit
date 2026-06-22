package nz.eloque.compose_kit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Circular monogram avatar: the first letter(s) of [text] on a colored disc. When [color] is
 * unspecified, falls back to the theme's `secondaryContainer`. The label color is chosen for
 * contrast against the background luminance.
 */
@Composable
fun MonogramAvatar(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    size: Dp = 40.dp,
) {
    val background = if (color == Color.Unspecified) MaterialTheme.colorScheme.secondaryContainer else color
    val onBackground = if (background.luminance() > 0.5f) Color.Black else Color.White
    val initials = monogram(text)

    Surface(
        modifier = modifier.size(size),
        shape = CircleShape,
        color = background,
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = initials,
                color = onBackground,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
            )
        }
    }
}

private fun monogram(text: String): String {
    val words = text.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
    return when {
        words.isEmpty() -> "?"
        words.size == 1 -> words[0].take(1).uppercase()
        else -> (words[0].take(1) + words[1].take(1)).uppercase()
    }
}
