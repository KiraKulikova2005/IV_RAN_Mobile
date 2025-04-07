package com.example.visionbook.view.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.visionbook.models.NavigationItems
import com.example.visionbook.view.AnimatedBottomNavigationBar
import com.example.visionbook.view.AnimatedTopNavigationBar
import com.example.visionbook.view.authScreens.ForgotScreen
import com.example.visionbook.view.authScreens.LoginScreen
import com.example.visionbook.view.authScreens.NFCScreen
import com.example.visionbook.view.authScreens.RegistrationScreen
import com.example.visionbook.view.camerasBookNProfile.secondCameraScreens.PreMainCameraScreen
import com.example.visionbook.view.mainScreens.Camera
import com.example.visionbook.view.mainScreens.HomeScreen
import com.example.visionbook.view.mainScreens.SettingsProfileScreen
import com.example.visionbook.view.addScreens.Post
import com.example.visionbook.view.addScreens.SearchAndFilters
import com.example.visionbook.view.mainScreens.NFCReadScreen
import com.example.visionbook.view.profileScreens.ProfileScreen
import com.example.visionbook.view.settingsScreen.FAQScreen
import com.example.visionbook.view.settingsScreen.NotificationScreen
import com.example.visionbook.view.settingsScreen.ProfileSettingsScreen
import com.example.visionbook.view.settingsScreen.SecurityScreen

import com.example.visionbook.viewmodels.AuthVM
import com.example.visionbook.viewmodels.SearchAndFiltersVM

@Composable
fun RootNavigation(
    navController: NavHostController = rememberNavController(),
    onThemeUpdated: () -> Unit,
    authViewModel: AuthVM = viewModel()
) {
    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()

    // Обрабатываем все состояния аутентификации
    LaunchedEffect(isAuthenticated) {
        when (isAuthenticated) {
            true -> {
                navController.navigate(GraphRoute.MAIN) {
                    popUpTo(GraphRoute.AUTH) { inclusive = true }
                }
            }
            false -> {
                navController.navigate(GraphRoute.AUTH) {
                    popUpTo(GraphRoute.MAIN) { inclusive = true }
                }
            }
            null -> {} // Или начальный экран загрузки
        }
    }

    NavHost(
        navController = navController,
        startDestination = when (isAuthenticated) {
            true -> GraphRoute.MAIN
            false -> GraphRoute.AUTH
            null -> GraphRoute.AUTH // Или экран загрузки
        }
    ) {
        authNavigation(navController, authViewModel)
        composable(GraphRoute.MAIN) {
            MainAppScreen(
                navController = navController,
                onThemeUpdated = onThemeUpdated,
                authViewModel = authViewModel
            )
        }
    }
}

fun NavHostController.logoutAndNavigateToAuth(authViewModel: AuthVM) {
    authViewModel.logout()
    navigate(GraphRoute.AUTH) {
        popUpTo(GraphRoute.MAIN) { inclusive = true }
        launchSingleTop = true
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainAppScreen(
    navController: NavHostController,
    onThemeUpdated: () -> Unit,
    authViewModel: AuthVM
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val innerNavController = rememberNavController()
    val navBackStackEntry by innerNavController.currentBackStackEntryAsState()
    val topBarState = remember { mutableStateOf(true) }
    val bottomBarState= remember { mutableStateOf(true) }
    val searchVM: SearchAndFiltersVM = viewModel()
    val isSearchActive by searchVM.isSearchActive

    // Ваша оригинальная логика видимости панелей
    when (navBackStackEntry?.destination?.route) {
        "home" -> {
            topBarState.value = true
            bottomBarState.value = true
        }

        "books" -> {
            topBarState.value = true
            bottomBarState.value = true
        }

        "camerainprofile" -> {
            topBarState.value = false
            bottomBarState.value = true
        }

        "camerainmain" -> {
            topBarState.value = false
            bottomBarState.value = true
        }

        "camera" -> {
            topBarState.value = false
            bottomBarState.value = false
        }

        "bookmarks" -> {
            topBarState.value = true
            bottomBarState.value = true
        }

        "profile" -> {
            topBarState.value = false
            bottomBarState.value = true
        }

        "post" -> {
            topBarState.value = false
            bottomBarState.value = true
        }

        "cameraforprofile" -> {
            topBarState.value = false
            bottomBarState.value = false
        }
        else -> {
            topBarState.value = false
            bottomBarState.value = false
        }
    }

    Box(Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                // Показываем TopAppBar только если он должен быть виден И поиск НЕ активен
                if (topBarState.value && !isSearchActive) {
                    AnimatedTopNavigationBar(
                        navController = innerNavController,
                        topAppBarState = topBarState, // Возможно, это состояние больше не нужно? Или нужно для анимации?
                        scrollBehavior = scrollBehavior,
                        viewModel = searchVM // Передаем VM для триггера поиска
                    )
                }
            },
            bottomBar = {
                // Показываем BottomAppBar только если он должен быть виден И поиск НЕ активен
                if (bottomBarState.value && !isSearchActive) {
                    AnimatedBottomNavigationBar(
                        navController = innerNavController,
                        bottomAppBarState = bottomBarState // Аналогично topBarState
                    )
                }
            }
        ) { paddingValues -> // Получаем padding от Scaffold
            // Внутренняя навигация размещается ВНУТРИ Scaffold и использует его padding
            NavHost(
                modifier = Modifier.padding(paddingValues), // Применяем padding здесь!
                navController = innerNavController,
                startDestination = "main"
            ) {
                mainNavigation(
                    navController = innerNavController,
                    onThemeUpdated = onThemeUpdated,
                    authViewModel = authViewModel,
                    rootNavController = navController
                )
            }
        }

        if (isSearchActive) {
            Box(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.TopCenter)
            ) {
                SearchAndFilters(viewModel = searchVM)
            }
        }
    }
}



fun NavGraphBuilder.authNavigation(
    navController: NavHostController, // Изменили тип
    authViewModel: AuthVM
) {
    navigation(
        route = GraphRoute.AUTH,
        startDestination = GraphRoute.LOGIN
    ) {
        composable(GraphRoute.LOGIN) {
            LoginScreen(navController, authViewModel)
        }
        composable(GraphRoute.REGISTRATION) {
            RegistrationScreen(navController, authViewModel)
        }
        composable(GraphRoute.FORGOT) {
            ForgotScreen(navController, authViewModel)
        }
        composable(GraphRoute.NFC) {
            NFCScreen(navController = navController,
                authViewModel = authViewModel)
        }
        composable(com.example.visionbook.models.NavigationItems.Camera.route){
            Camera(LocalContext.current, navController)
        }
    }
}

fun NavGraphBuilder.mainNavigation(
    navController: NavController,
    onThemeUpdated: () -> Unit,
    authViewModel: AuthVM,
    rootNavController: NavHostController
) {
    navigation(
        startDestination = NavigationItems.Home.route,
        route = "main"
    ) {
        composable(NavigationItems.Home.route) {
            HomeScreen(navController, authViewModel)
        }
        composable(NavigationItems.Profile.route) {
            SettingsProfileScreen(
                navController = navController,
                rootNavController = rootNavController,
                onThemeUpdated = onThemeUpdated,
                authViewModel = authViewModel
            )
        }
        composable(NavigationItems.CameraInMain.route) {
            PreMainCameraScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }
        composable(NavigationItems.Camera.route) {
            Camera(LocalContext.current, navController)
        }
        composable(NavigationItems.Post.route) {
            Post(navController = navController)
        }
        composable(GraphRoute.PROFILE_SETTINGS) {
            ProfileSettingsScreen(
                navController = navController, // innerNavController
                rootNavController = rootNavController // Передаем корневой контроллер!
            )
        }
        composable(GraphRoute.NOTIFICATION) {
            NotificationScreen(navController)
        }
        composable(GraphRoute.FAQ) {
            FAQScreen(navController)
        }
        composable(GraphRoute.SECURITY) {
            SecurityScreen(navController)
        }
        composable(GraphRoute.NFC_READ) {
            NFCReadScreen(navController)
        }
        composable(GraphRoute.PROFILE_VIEW) {
            ProfileScreen(navController)
        }
    }
}