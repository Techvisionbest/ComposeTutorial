package com.vision.composetutorial.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NiaToggleButton(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: @Composable () -> Unit,
    checkedBackgroundRadius: Dp = NiaToggleButtonDefaults.ToggleButtonSize / 2,
    checkedIcon: @Composable () -> Unit = icon
) {
    val checkedColor = MaterialTheme.colorScheme.primaryContainer
    val checkedRadius = with(LocalDensity.current) {
        checkedBackgroundRadius.toPx()
    }
    IconButton(
        onClick = { onCheckedChange(!checked) },
        modifier = Modifier
            .toggleable(value = checked, enabled = enabled, role = Role.Button, onValueChange = {})
            .drawBehind {
                if (checked) drawCircle(
                    color = checkedColor,
                    radius = checkedRadius
                )
            }
            .then(modifier),
        enabled = enabled,
        content = {
            Box(
                modifier = Modifier.sizeIn(
                    maxWidth = NiaToggleButtonDefaults.ToggleButtonIconSize,
                    maxHeight = NiaToggleButtonDefaults.ToggleButtonIconSize
                )
            ) {
                if (checked) checkedIcon() else icon()
            }
        }
    )
}

/**
 * Now in Android toggle button default values.
 */
object NiaToggleButtonDefaults {
    val ToggleButtonSize = 40.dp
    val ToggleButtonIconSize = 18.dp
}