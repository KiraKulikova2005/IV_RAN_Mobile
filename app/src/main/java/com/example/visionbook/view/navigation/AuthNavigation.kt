package com.example.visionbook.view.navigation

import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.visionbook.models.NavigationItems
import com.example.visionbook.view.authScreens.ForgotScreen
import com.example.visionbook.view.authScreens.LoginScreen
import com.example.visionbook.view.authScreens.NFCScreen
import com.example.visionbook.view.authScreens.RegistrationScreen
import com.example.visionbook.view.mainScreens.Camera
import com.example.visionbook.view.mainScreens.HomeScreen
import com.example.visionbook.viewmodels.AuthVM

fun NavGraphBuilder.authNavigation(navController: NavHostController, authViewModel: AuthVM) {
    navigation(
        route = GraphRoute.AUTHENTICATION,
        startDestination = AuthScreen.Login.route
    ) {
        composable(AuthScreen.Login.route) {
            LoginScreen(navController, authViewModel)
        }
        composable(AuthScreen.Registration.route) {
            RegistrationScreen(navController, authViewModel)
        }
        composable(AuthScreen.Forgot.route) {
            ForgotScreen(navController, authViewModel)
        }
        composable(AuthScreen.NFC.route) {
            NFCScreen(navController)
        }
        composable(NavigationItems.Camera.route){
            Camera(LocalContext.current, navController)
        }
    }
}

sealed class AuthScreen(val route: String) {
    object Login : AuthScreen("LOGIN")
    object Registration : AuthScreen("REGISTRATION")
    object Forgot : AuthScreen("FORGOT")
    object NFC : AuthScreen("NFC")
    object QrCamera : AuthScreen("qrcamera")
}