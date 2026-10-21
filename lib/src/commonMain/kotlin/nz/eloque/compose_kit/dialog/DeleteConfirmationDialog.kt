package nz.eloque.compose_kit.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.back
import nz.eloque.compose_kit.resources.delete
import nz.eloque.compose_kit.resources.delete_confirmation_message
import nz.eloque.compose_kit.resources.dont_ask_again
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteConfirmationDialog(
    deleteConfirmationEnabled: Boolean,
    onDeleteConfirmationEnabledChange: (Boolean) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!deleteConfirmationEnabled) {
        LaunchedEffect(Unit) {
            onConfirm()
        }
        return
    }

    var dontAskAgain by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.delete)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(Res.string.delete_confirmation_message))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Checkbox(
                        checked = dontAskAgain,
                        onCheckedChange = { dontAskAgain = it },
                    )
                    Text(stringResource(Res.string.dont_ask_again))
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onDeleteConfirmationEnabledChange(!dontAskAgain)
                    onConfirm()
                },
            ) {
                Text(stringResource(Res.string.delete))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.back))
            }
        },
    )
}
