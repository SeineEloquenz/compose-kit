package nz.eloque.compose_kit.input

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun SubmittableTextField(
    label: String,
    imageVector: ImageVector,
    onSubmit: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    onValueChange: (String) -> Unit = {},
    enabled: Boolean = true,
    inputValidator: (String) -> Boolean = { true },
    initialValue: String = "",
    clearOnSubmit: Boolean = true,
    contentDescription: String = "",
    hidden: Boolean = false,
) {
    var text by rememberSaveable { mutableStateOf(initialValue) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val trimmed = text.trim()

    val isError by remember {
        derivedStateOf {
            trimmed.isNotBlank() && !inputValidator(trimmed)
        }
    }

    val buttonEnabled by remember {
        derivedStateOf {
            enabled &&
                trimmed.isNotEmpty() &&
                !isError &&
                trimmed != initialValue
        }
    }

    fun submit() {
        if (!buttonEnabled) return

        onSubmit(trimmed)

        if (clearOnSubmit) {
            text = ""
        }
    }

    OutlinedTextField(
        value = text,
        onValueChange = {
            text = it
            onValueChange(it)
        },
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = singleLine,
        label = { Text(label) },
        textStyle = MaterialTheme.typography.bodyLarge,
        isError = isError,
        visualTransformation =
            if (hidden && !passwordVisible) {
                PasswordVisualTransformation()
            } else {
                VisualTransformation.None
            },
        keyboardOptions =
            KeyboardOptions(
                keyboardType = if (hidden) KeyboardType.Password else KeyboardType.Text,
                imeAction = ImeAction.Done,
            ),
        keyboardActions =
            KeyboardActions(
                onDone = { submit() },
            ),
        supportingText = {
            if (isError) {
                Text(
                    text = "Invalid input",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
        trailingIcon = {
            Row {
                if (hidden) {
                    IconButton(
                        onClick = {
                            passwordVisible = !passwordVisible
                        },
                    ) {
                        Icon(
                            imageVector =
                                if (passwordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                            contentDescription =
                                if (passwordVisible) {
                                    "Hide password"
                                } else {
                                    "Show password"
                                },
                        )
                    }
                }

                IconButton(
                    onClick = { submit() },
                    enabled = buttonEnabled,
                ) {
                    Icon(
                        imageVector = imageVector,
                        contentDescription = contentDescription,
                        tint =
                            if (buttonEnabled) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                    )
                }
            }
        },
    )
}
