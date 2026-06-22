package nz.eloque.compose_kit.components.internal

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.runtime.Composable

@Composable
internal actual fun shouldClearFocusOnImeHide(): Boolean = !WindowInsets.isImeVisible
