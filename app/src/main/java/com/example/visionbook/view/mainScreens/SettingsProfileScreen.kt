package com.example.visionbook.view.mainScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.visionbook.R
import com.example.visionbook.data.MenuItem
import com.example.visionbook.models.AutoresizedText
import com.example.visionbook.view.navigation.GraphRoute
import com.example.visionbook.view.navigation.logoutAndNavigateToAuth
import com.example.visionbook.viewmodels.AuthVM
import com.example.visionbook.viewmodels.ProfileScreenVM

@Composable
fun SettingsProfileScreen(
    navController: NavController,
    rootNavController: NavHostController,
    onThemeUpdated: () -> Unit,
    viewModel: ProfileScreenVM = viewModel(),
    authViewModel: AuthVM = viewModel()
) {
    val profileList = viewModel.profileList.value
    val firstProfile = profileList.firstOrNull()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 22.dp, end = 22.dp)
    ) {
        Button(
            onClick = { navController.navigate(GraphRoute.PROFILE_VIEW) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            elevation = ButtonDefaults.elevatedButtonElevation(0.dp),
            shape = RoundedCornerShape(30.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 10.dp, start = 15.dp)
                        .size(65.dp)
                        .clip(CircleShape)
                )

                firstProfile?.nickname?.let {
                    AutoresizedText(
                        stringResource(R.string.prof_name),
                        modifier = Modifier.padding(start = 50.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 44.dp)
                .clip(RoundedCornerShape(15))
        ) {
            val menuItems = listOf(
                MenuItem(R.drawable.theme, "Theme", stringResource(R.string.theme)),
                MenuItem(
                    R.drawable.profile_settings,
                    "Profile Settings",
                    stringResource(R.string.profile_settings)
                ),
                MenuItem(R.drawable.faq, "FAQ", stringResource(R.string.faq)),
                MenuItem(R.drawable.safety, "Security", stringResource(R.string.security)),
                MenuItem(
                    R.drawable.notification,
                    "Notification",
                    stringResource(R.string.notification)
                ),
                MenuItem(R.drawable.language, "Exit", stringResource(R.string.exit)),
            )

            menuItems.forEach { menuItem ->
                MenuButton(
                    iconId = menuItem.iconId,
                    contentDescription = menuItem.contentDescription,
                    text = menuItem.text,
                    onThemeUpdated,
                    navController,
                    rootNavController,
                    authViewModel
                )
            }
        }
    }
}

@Composable
fun MenuButton(
    iconId: Int,
    contentDescription: String,
    text: String,
    onThemeUpdated: () -> Unit,
    navController: NavController,
    rootNavController: NavHostController,
    authViewModel: AuthVM
) {
    Button(
        onClick = {
            when (contentDescription) {
                "Theme" -> {
                    onThemeUpdated()
                }

                "Profile Settings" -> {
                    navController.navigate(GraphRoute.PROFILE_SETTINGS)
                }

                "Notification" -> {
                    navController.navigate(GraphRoute.NOTIFICATION)
                }

                "Security" -> {
                    navController.navigate(GraphRoute.SECURITY)
                }

                "Exit" -> {
                    rootNavController.logoutAndNavigateToAuth(authViewModel)
                }

                "FAQ" -> {
                    navController.navigate(GraphRoute.FAQ)
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(0)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(iconId),
                contentDescription = contentDescription,
                modifier = Modifier
                    .padding(start = 25.dp)
                    .size(35.dp)
            )
            AutoresizedText(
                text = text,
                modifier = Modifier.padding(start = 20.dp),
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}