package com.example.visionbook.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.visionbook.view.addScreens.Post
import com.example.visionbook.view.mainScreens.Camera
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.visionbook.models.NavigationItems
import com.example.visionbook.view.navigation.RootNavigation
import com.example.visionbook.ui.theme.MyApplicationTheme
import com.example.visionbook.view.addScreens.SearchAndFilters
import com.example.visionbook.view.mainScreens.BookmarksScreen
import com.example.visionbook.view.mainScreens.HomeScreen
import com.example.visionbook.view.mainScreens.NFCReadScreen
import com.example.visionbook.view.mainScreens.SettingsProfileScreen
import com.example.visionbook.viewmodels.AuthVM
import com.example.visionbook.viewmodels.SearchAndFiltersVM
import com.example.visionbook.view.camerasBookNProfile.secondCameraScreens.PreMainCameraScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkThemeNow = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(darkThemeNow) }
            val navController = rememberNavController()

            MyApplicationTheme(darkTheme = darkTheme) {
                RootNavigation(
                    navController = navController, // Используем navController вместо rootNavController
                    onThemeUpdated = { darkTheme = !darkTheme }
                )
            }
        }
    }
}

