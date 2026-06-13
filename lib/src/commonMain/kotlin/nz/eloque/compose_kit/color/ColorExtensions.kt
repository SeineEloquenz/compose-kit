package nz.eloque.compose_kit.color

import androidx.compose.ui.graphics.Color

fun Color.darken(factor: Float = 0.3f): Color =
    copy(
        red = red * factor,
        green = green * factor,
        blue = blue * factor,
        alpha = alpha,
    )
