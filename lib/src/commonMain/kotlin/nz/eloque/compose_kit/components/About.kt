package nz.eloque.compose_kit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

sealed interface AboutLink {
    val icon: ImageVector
    val label: String

    /** Opens [url] in the platform browser (via `LocalUriHandler`) when tapped. */
    data class Uri(
        override val icon: ImageVector,
        override val label: String,
        val url: String,
    ) : AboutLink

    /** Runs [onClick] when tapped — e.g. navigating to an in-app "open-source licenses" screen. */
    data class Action(
        override val icon: ImageVector,
        override val label: String,
        val onClick: () -> Unit,
    ) : AboutLink
}

@Composable
fun About(
    appName: String,
    icon: Painter,
    modifier: Modifier = Modifier,
    tagline: String? = null,
    taglineIcon: ImageVector = Icons.Default.Favorite,
    version: String? = null,
    links: List<AboutLink> = emptyList(),
) {
    val uriHandler = LocalUriHandler.current
    Box(modifier = modifier.fillMaxSize()) {
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                Image(
                    painter = icon,
                    contentDescription = appName,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.fillMaxWidth(0.5f),
                )
                Text(
                    text = appName,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.displaySmall,
                )
                if (tagline != null) {
                    AboutRow(
                        icon = taglineIcon,
                        text = tagline,
                        textStyle = MaterialTheme.typography.labelLarge,
                    )
                }
            }
            links.forEach { link ->
                val onClick: () -> Unit =
                    when (link) {
                        is AboutLink.Uri -> ({ uriHandler.openUri(link.url) })
                        is AboutLink.Action -> link.onClick
                    }
                OutlinedButton(
                    onClick = onClick,
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 50.dp),
                ) {
                    AboutRow(icon = link.icon, text = link.label)
                }
            }
            Spacer(Modifier.imePadding())
        }
        if (version != null) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(bottom = 8.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()),
                contentAlignment = Alignment.BottomCenter,
            ) {
                Text(
                    text = version,
                    color = MaterialTheme.colorScheme.secondary,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Composable
private fun AboutRow(
    icon: ImageVector,
    text: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.headlineSmall,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.secondary,
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.secondary,
            style = textStyle,
            textAlign = TextAlign.Center,
        )
    }
}
