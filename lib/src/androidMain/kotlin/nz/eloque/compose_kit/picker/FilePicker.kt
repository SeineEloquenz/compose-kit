package nz.eloque.compose_kit.picker

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.compose_kit_choose_image
import org.jetbrains.compose.resources.stringResource

@Composable
fun FilePicker(
    label: String,
    labelIcon: ImageVector,
    onChoose: (String?, Uri) -> Unit,
    modifier: Modifier = Modifier,
    mimeTypes: Array<String> = arrayOf("image/png", "image/jpeg", "application/pdf"),
) {
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            uri?.let { onChoose(getFileName(context, uri), uri) }
        }

    TextButton(
        onClick = { launcher.launch(mimeTypes) },
        modifier = modifier,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(label)
            Icon(
                imageVector = labelIcon,
                contentDescription = stringResource(Res.string.compose_kit_choose_image),
            )
        }
    }
}

fun getFileName(
    context: Context,
    uri: Uri,
): String? {
    var name: String? = null

    val cursor =
        context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null,
        )

    cursor?.use {
        if (it.moveToFirst()) {
            val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (index >= 0) {
                name = it.getString(index)
            }
        }
    }

    return name
}
