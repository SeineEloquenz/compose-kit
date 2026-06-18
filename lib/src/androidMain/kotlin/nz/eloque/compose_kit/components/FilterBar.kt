package nz.eloque.compose_kit.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.compose_kit_delete
import nz.eloque.compose_kit.resources.compose_kit_search
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterBar(
    onSearch: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    var isFocused by rememberSaveable { mutableStateOf(false) }
    var query by rememberSaveable { mutableStateOf("") }

    BackHandler(enabled = isFocused) { focusManager.clearFocus() }
    SearchBar(
        windowInsets = WindowInsets(0.dp),
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(Res.string.compose_kit_search),
                    )
                },
                placeholder = { Text(stringResource(Res.string.compose_kit_search)) },
                onQueryChange = {
                    query = it
                    onSearch.invoke(it)
                },
                onSearch = {
                    focusManager.clearFocus()
                },
                onExpandedChange = {},
                expanded = false,
                modifier =
                    Modifier
                        .fillMaxHeight()
                        .onFocusChanged { isFocused = it.isFocused },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                query = ""
                                onSearch.invoke("")
                            },
                        ) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = stringResource(Res.string.compose_kit_delete))
                        }
                    }
                },
            )
        },
        expanded = false,
        onExpandedChange = {},
        modifier = modifier,
    ) {
    }
}

@Preview
@Composable
private fun FilterBarPreview() {
    FilterBar({})
}
