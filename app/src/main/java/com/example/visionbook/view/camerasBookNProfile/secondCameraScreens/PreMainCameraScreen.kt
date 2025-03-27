package com.example.visionbook.view.camerasBookNProfile.secondCameraScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.visionbook.R
import com.example.visionbook.models.AutoresizedText
import com.example.visionbook.models.NavigationItems
import com.example.visionbook.viewmodels.AuthVM
import com.example.visionbook.viewmodels.BooksScreenVM
import com.example.visionbook.viewmodels.RetrofitVM

@Composable
fun PreMainCameraScreen(
    booksViewModel: BooksScreenVM = viewModel(),
    retrofitViewModel: RetrofitVM = viewModel(),
    navController: NavController,
    authViewModel: AuthVM
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, start = 22.dp, end = 22.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.padding(bottom = 10.dp),
            text = stringResource(R.string.premaincamera_newbook),
            style = MaterialTheme.typography.headlineLarge
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Кнопка "Сканировать книгу"
            Button(
                onClick = {
                    // Прямой переход к экрану камеры без ввода данных
                    navController.navigate(NavigationItems.Camera.route)
                }
            ) {
                AutoresizedText(
                    text = stringResource(R.string.premaincamera_newbook_bt),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}