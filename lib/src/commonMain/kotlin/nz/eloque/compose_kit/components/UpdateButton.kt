package nz.eloque.compose_kit.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.compose_kit_update
import org.jetbrains.compose.resources.stringResource

@Composable
fun UpdateButton(
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition(label = "updateButtonAnimation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec =
            infiniteRepeatable(
                animation = tween(1500, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Restart,
            ),
        label = "spin",
    )

    IconButton(
        onClick = { if (!isLoading) onClick() },
        modifier = modifier,
    ) {
        Icon(
            imageVector = Icons.Default.Sync,
            contentDescription = stringResource(Res.string.compose_kit_update),
            modifier =
                Modifier.graphicsLayer(
                    rotationZ = if (isLoading) rotation else 0f,
                ),
        )
    }
}
