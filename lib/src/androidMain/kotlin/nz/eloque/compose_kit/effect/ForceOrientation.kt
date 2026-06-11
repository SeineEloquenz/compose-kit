package nz.eloque.compose_kit.effect

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember

enum class Orientation {
    Locked,
    Portrait,
    Landscape,
}

@Composable
fun ForceOrientation(orientation: Orientation) {
    val activity = LocalActivity.current

    val formerOrientation = remember { activity?.requestedOrientation }

    val newOrientation =
        when (orientation) {
            Orientation.Locked -> ActivityInfo.SCREEN_ORIENTATION_LOCKED
            Orientation.Portrait -> ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            Orientation.Landscape -> ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }

    DisposableEffect(activity) {
        activity?.requestedOrientation = newOrientation
        onDispose {
            formerOrientation?.let {
                activity?.requestedOrientation = it
            }
        }
    }
}
