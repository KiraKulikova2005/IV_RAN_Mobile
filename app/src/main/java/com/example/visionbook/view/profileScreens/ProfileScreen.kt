package com.example.visionbook.view.profileScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.visionbook.R
import com.example.visionbook.models.AutoresizedText
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import com.example.visionbook.viewmodels.ProfileScreenVM

@Composable
fun ProfileScreen(navController: NavController, viewModel: ProfileScreenVM = viewModel()) {
    val profileList = viewModel.profileList.value
    val firstProfile = profileList.firstOrNull()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Основной контент
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 60.dp)
            ) {
                BackButton(navController = navController)
            }
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Avatar",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(180.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Блок с nickname, id и role
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                firstProfile?.nickname?.let {
                    AutoresizedText(
                        stringResource(R.string.prof_name),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Отступ между nickname и id

                firstProfile?.id?.let {
                    AutoresizedText(
                        stringResource(R.string.user_id),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                Spacer(modifier = Modifier.height(10.dp)) // Отступ между id и role

                Row(
                    modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {}) {
                        AutoresizedText(
                            stringResource(R.string.user_role),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp)) // Отступ перед booksCount

            // Блок с booksCount
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AutoresizedText(
                        firstProfile?.booksCount.toString(),
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    AutoresizedText(
                        stringResource(R.string.user_proc),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }

        // Нижний контент (email и dep)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            firstProfile?.email?.let {
                AutoresizedText(
                    stringResource(R.string.user_email),
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Spacer(modifier = Modifier.height(15.dp)) // Увеличенный отступ между email и dep

            firstProfile?.dep?.let {
                AutoresizedText(
                    stringResource(R.string.user_dep),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}