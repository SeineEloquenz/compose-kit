package nz.eloque.compose_kit.chip

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import nz.eloque.compose_kit.color.darken
import nz.eloque.compose_kit.resources.Res
import nz.eloque.compose_kit.resources.compose_kit_selected
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> ChipSelector(
    options: Collection<T>,
    selectedOptions: Collection<T>,
    onOptionSelected: (T) -> Unit,
    onOptionDeselected: (T) -> Unit,
    optionLabel: (T) -> String,
    optionColor: ((T) -> Color)? = null,
    modifier: Modifier = Modifier,
    selectedIcon: ImageVector = Icons.Default.Check,
) {
    val defaultColor = MaterialTheme.colorScheme.secondaryContainer

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier =
            modifier
                .horizontalScroll(rememberScrollState()),
    ) {
        options.forEach { option ->
            val selected = selectedOptions.contains(option)
            val chipColor = optionColor?.invoke(option) ?: defaultColor
            val containerColor by animateColorAsState(
                targetValue =
                    if (!selected && selectedOptions.isNotEmpty()) chipColor.darken() else chipColor,
            )
            val labelColor = if (chipColor.luminance() > 0.5f) Color.Black else Color.White

            FilterChip(
                selected = selected,
                colors =
                    FilterChipDefaults.filterChipColors(
                        containerColor = containerColor,
                        selectedContainerColor = containerColor,
                        labelColor = labelColor,
                        selectedLabelColor = labelColor,
                    ),
                leadingIcon = {
                    if (selected) {
                        Icon(
                            imageVector = selectedIcon,
                            contentDescription = stringResource(Res.string.compose_kit_selected),
                        )
                    }
                },
                onClick = {
                    if (selected) {
                        onOptionDeselected(option)
                    } else {
                        onOptionSelected(option)
                    }
                },
                label = { Text(optionLabel(option)) },
            )
        }
    }
}
