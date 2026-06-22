package nz.eloque.compose_kit.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.compose_kit_more_options
import nz.eloque.compose_kit.resources.compose_kit_selected
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T, F> ExtendedSelectionMenu(
    multiOptions: Collection<F>,
    singleOptions: List<T>,
    multiOptionLabel: (F) -> String,
    singleOptionLabel: (T) -> String,
    selectedMultiOptions: Collection<F>,
    selectedSingleOption: T,
    onMultiOptionSelected: (F) -> Unit,
    onSingleOptionSelected: (T) -> Unit,
    onMultiOptionDeselected: (F) -> Unit,
    modifier: Modifier = Modifier,
    contentDescription: String = stringResource(Res.string.compose_kit_more_options),
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(Icons.Default.FilterList, contentDescription = contentDescription)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            singleOptions.forEach {
                DropdownMenuItem(
                    text = { Text(singleOptionLabel(it)) },
                    leadingIcon = {
                        if (it == selectedSingleOption) {
                            Icon(Icons.Default.RadioButtonChecked, stringResource(Res.string.compose_kit_selected))
                        }
                    },
                    onClick = { onSingleOptionSelected(it) },
                )
            }

            HorizontalDivider()

            multiOptions.forEach {
                val selected = selectedMultiOptions.contains(it)
                DropdownMenuItem(
                    text = { Text(multiOptionLabel(it)) },
                    leadingIcon = {
                        if (selected) {
                            Icon(Icons.Default.CheckBox, contentDescription = stringResource(Res.string.compose_kit_selected))
                        }
                    },
                    onClick = { if (selected) onMultiOptionDeselected(it) else onMultiOptionSelected(it) },
                )
            }
        }
    }
}
