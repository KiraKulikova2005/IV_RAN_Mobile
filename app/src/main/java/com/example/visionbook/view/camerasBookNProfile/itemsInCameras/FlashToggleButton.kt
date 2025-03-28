package com.example.visionbook.view.camerasBookNProfile.itemsInCameras

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.visionbook.R

@Composable
fun FlashToggleButton(
    isFlashEnabled: Boolean,
    onFlashToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onFlashToggle,
        modifier = modifier.size(45.dp)
    ) {
        Icon(
            painter = painterResource(
                id = if (isFlashEnabled) R.drawable.flash_on
                else R.drawable.flash_off
            ),
            contentDescription = if (isFlashEnabled) "Выключить вспышку"
            else "Включить вспышку",
            tint = LocalContentColor.current // Автоматически адаптируется к теме
        )
    }
}