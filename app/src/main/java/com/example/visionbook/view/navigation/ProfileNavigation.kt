package com.example.visionbook.view.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.visionbook.view.profileScreens.ProfileScreen

fun NavGraphBuilder.profileNavigation(navController: NavHostController){
    navigation(
        route = GraphRoute.PROFILE,
        startDestination = ProfileScreen.Profile.route
    ){
        composable(ProfileScreen.Profile.route){
            ProfileScreen(navController)
        }
    }
}

sealed class ProfileScreen(val route: String) {
    object Profile : ProfileScreen("PROFILE")
}