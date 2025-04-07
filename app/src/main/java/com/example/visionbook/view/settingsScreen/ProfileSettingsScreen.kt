package com.example.visionbook.view.settingsScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.visionbook.R
import com.example.visionbook.data.ProfileSettingsItem
import com.example.visionbook.models.AutoresizedText
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import com.example.visionbook.view.navigation.GraphRoute
import com.example.visionbook.view.navigation.logoutAndNavigateToAuth
import com.example.visionbook.viewmodels.AuthVM

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSettingsScreen(
    navController: NavController,
    rootNavController: NavHostController,
    authViewModel: AuthVM = viewModel()  // Добавляем параметр
) {
    // Temporary mock data - replace with real data from database
    var profileData by remember {
        mutableStateOf(
            mapOf(
                "ФИО" to "Иванов Иван Иванович",
                "Роль" to "Администратор",
                "Подразделение" to "Отдел разработки",
                "Адресс эл. почты" to "ivanov@example.com",
                "Пароль" to "********"
            )
        )
    }

    var editedValue by remember { mutableStateOf("") }
    var currentField by remember { mutableStateOf("") }
    var showViewDialog by remember { mutableStateOf(false) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showSaveConfirmation by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BackButton(navController = navController)
        Text(
            "Настройки профиля",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(top = 20.dp)
        )
        Image(
            painter = painterResource(R.drawable.profile),
            contentDescription = "Null Avatar",
            modifier = Modifier
                .size(200.dp)
                .padding(top = 20.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        val profileSettingstItems = listOf(
            ProfileSettingsItem("ФИО", stringResource(R.string.theme)),
            ProfileSettingsItem("Роль", stringResource(R.string.profile_settings)),
            ProfileSettingsItem("Подразделение", stringResource(R.string.notification)),
            ProfileSettingsItem("Адресс эл. почты", stringResource(R.string.exit)),
            ProfileSettingsItem("Пароль", stringResource(R.string.faq)),
            ProfileSettingsItem("Выход из профиля", stringResource(R.string.faq)),
        )
        LazyColumn(
            Modifier
                .fillMaxWidth()
                .padding(top = 50.dp)
        ) {
            items(profileSettingstItems) { item ->
                ProfileSettingsButton(
                    contentDescription = item.contentDescription,
                    text = item.text,
                    navController = navController,
                    onClick = { field ->
                        if (field == "Выход из профиля") {
                            rootNavController.logoutAndNavigateToAuth(authViewModel)
                        } else {
                            currentField = field
                            editedValue = profileData[field] ?: ""
                            showViewDialog = true
                        }
                    }
                )
            }
        }
    }

    // View Dialog
    if (showViewDialog) {
        AlertDialog(
            onDismissRequest = { showViewDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(currentField)
                    IconButton(
                        onClick = { showViewDialog = false }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть"
                        )
                    }
                }
            },
            text = { Text(profileData[currentField] ?: "") },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            showViewDialog = false
                            showEditDialog = true
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Редактировать")
                    }
                }
            },
            dismissButton = {}
        )
    }

// Edit Dialog
    if (showEditDialog) {
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Редактировать $currentField")
                    IconButton(
                        onClick = { showEditDialog = false }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть"
                        )
                    }
                }
            },
            text = {
                OutlinedTextField(
                    value = editedValue,
                    onValueChange = { editedValue = it },
                    label = { Text(currentField) },
                    keyboardOptions = when (currentField) {
                        "Адресс эл. почты" -> KeyboardOptions(keyboardType = KeyboardType.Email)
                        "Пароль" -> KeyboardOptions(keyboardType = KeyboardType.Password)
                        else -> KeyboardOptions.Default
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            if (editedValue != profileData[currentField]) {
                                showSaveConfirmation = true
                            }
                            showEditDialog = false
                        },
                        enabled = editedValue.isNotEmpty()
                    ) {
                        Text("Сохранить")
                    }
                }
            },
            dismissButton = {}
        )
    }

// Save Confirmation Dialog
    if (showSaveConfirmation) {
        AlertDialog(
            onDismissRequest = { showSaveConfirmation = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Подтверждение")
                    IconButton(
                        onClick = { showSaveConfirmation = false }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.Black
                        )
                    }
                }
            },
            text = { Text("Применить изменения?") },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            profileData = profileData.toMutableMap().apply {
                                this[currentField] = editedValue
                            }
                            showSaveConfirmation = false
                        },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text("Да")
                    }
                    Button(
                        onClick = { showSaveConfirmation = false }
                    ) {
                        Text("Нет")
                    }
                }
            },
            dismissButton = {}
        )
    }
}

@Composable
fun ProfileSettingsButton(
    contentDescription: String,
    text: String,
    navController: NavController,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AutoresizedText(
            text = contentDescription,
            modifier = Modifier.padding(start = 20.dp),
            style = MaterialTheme.typography.headlineMedium
        )
        IconButton(onClick = { onClick(contentDescription) }) {
            Icon(
                painter = painterResource(com.google.android.exoplayer2.R.drawable.exo_ic_chevron_right),
                contentDescription = "right arrow"
            )
        }
    }
}