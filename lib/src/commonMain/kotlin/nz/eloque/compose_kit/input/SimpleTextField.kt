package nz.eloque.compose_kit.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.compose_kit_invalid_input
import org.jetbrains.compose.resources.stringResource

@Composable
fun SimpleTextField(
    onSubmit: (String) -> Unit,
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    value: String? = null,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    inputValidator: (String) -> Boolean = { true },
    initialValue: String = "",
    contentDescription: String = "",
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    var localText by rememberSaveable { mutableStateOf(initialValue) }
    var lastValidText by rememberSaveable { mutableStateOf(initialValue) }
    var touched by rememberSaveable { mutableStateOf(false) }

    val value = text ?: localText

    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    val isError by remember {
        derivedStateOf {
            val trimmed = value.trim()
            touched && (trimmed.isEmpty() || !inputValidator(trimmed))
        }
    }

    LaunchedEffect(value) {
        if (!isError) {
            lastValidText = value
            onSubmit(value)
        }
    }

    LaunchedEffect(focused) {
        if (!focused) {
            touched = true
            if (isError) {
                if (value == null) localText = lastValidText
            }
        }
    }

    TextField(
        value = value,
        onValueChange = {
            if (value == null) localText = it
            onValueChange(it)
        },
        singleLine = singleLine,
        enabled = enabled,
        isError = isError,
        modifier = modifier,
        textStyle = textStyle,
        interactionSource = interactionSource,
        keyboardOptions = keyboardOptions,
        leadingIcon = {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint =
                    if (!enabled) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
                    } else if (isError) {
                        MaterialTheme.colorScheme.error
                    } else if (focused) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
            )
        },
        colors =
            TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
            ),
        supportingText =
            if (isError && value.isNotBlank()) {
                { Text(stringResource(Res.string.compose_kit_invalid_input)) }
            } else {
                null
            },
    )
}
