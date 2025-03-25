package com.example.visionbook.view.settingsScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.visionbook.R
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton

@Composable
fun NotificationScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        BackButton(navController = navController)

        Text(
            text = stringResource(R.string.support_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxWidth()
        )

        Text(
            text = stringResource(R.string.support_content),
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 20.dp)
        )
    }
}
