package nz.eloque.compose_kit.input

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.delay
import nz.eloque.compose_kit.R

@Composable
fun SimpleTextField(
    title: String,
    imageVector: ImageVector,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    inputValidator: (String) -> Boolean = { true },
    initialValue: String = "",
    submitDelay: Long = 999L,
    contentDescription: String = "",
) {
    var text by rememberSaveable { mutableStateOf(initialValue) }
    var lastValidText by rememberSaveable { mutableStateOf(initialValue) }
    var touched by rememberSaveable { mutableStateOf(false) }

    val interactionSource = remember { MutableInteractionSource() }
    val focused by interactionSource.collectIsFocusedAsState()

    val isError by remember {
        derivedStateOf {
            val trimmed = text.trim()
            touched && (trimmed.isEmpty() || !inputValidator(trimmed))
        }
    }

    val handleSubmit by rememberUpdatedState {
        if (!isError) {
            onSubmit(text)
        }
    }

    LaunchedEffect(focused, text) {
        if (focused) {
            delay(submitDelay)
            handleSubmit()
        } else if (touched) {
            if (isError) {
                text = lastValidText
            } else {
                handleSubmit()
            }
        }
    }

    LaunchedEffect(focused) {
        if (!focused) touched = true
    }

    LaunchedEffect(text) {
        if (!isError) lastValidText = text
    }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        singleLine = singleLine,
        enabled = enabled,
        isError = isError,
        modifier = modifier,
        textStyle = MaterialTheme.typography.bodyLarge,
        interactionSource = interactionSource,
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
        keyboardActions =
            KeyboardActions(
                onDone = { handleSubmit() },
            ),
        supportingText =
            if (isError && text.isNotBlank()) {
                { Text(stringResource(R.string.invalid_input)) }
            } else {
                null
            },
    )
}
