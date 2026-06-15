package nz.eloque.compose_kit.input

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.compose_kit_search
import org.jetbrains.compose.resources.stringResource

/**
 * Opens a bottom sheet with a searchable picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SearchablePickerField(
    label: String,
    selected: T,
    selectedLabel: (T) -> String,
    onSelected: (T) -> Unit,
    search: (query: String) -> List<T>,
    itemKey: (T) -> Any,
    itemLabel: (T) -> String,
    modifier: Modifier = Modifier,
    searchLabel: String = "Search",
    emptyLabel: String = "No match",
) {
    var open by remember { mutableStateOf(false) }

    Box(modifier) {
        OutlinedTextField(
            value = selectedLabel(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription =
                        stringResource(
                            Res.string.compose_kit_search,
                        ),
                )
            },
            modifier = Modifier.fillMaxWidth(),
        )
        // A read-only text field still swallows taps, so overlay a transparent click target.
        Box(Modifier.matchParentSize().clickable { open = true })
    }

    if (open) {
        val sheetState = rememberStandardBottomSheetState(initialValue = SheetValue.PartiallyExpanded, skipHiddenState = false)
        ModalBottomSheet(onDismissRequest = { open = false }, sheetState = sheetState) {
            var query by remember { mutableStateOf("") }
            val results = remember(query) { search(query) }

            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp)) {
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text(searchLabel) },
                    singleLine = true,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription =
                                stringResource(
                                    Res.string.compose_kit_search,
                                ),
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                LazyColumn(Modifier.fillMaxWidth().heightIn(max = 480.dp)) {
                    items(results, key = itemKey) { item ->
                        Text(
                            itemLabel(item),
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelected(item)
                                    open = false
                                }.padding(vertical = 14.dp),
                        )
                    }
                    if (results.isEmpty()) {
                        item {
                            Text(emptyLabel, Modifier.padding(vertical = 14.dp), color = MaterialTheme.colorScheme.outline)
                        }
                    }
                }
            }
        }
    }
}
