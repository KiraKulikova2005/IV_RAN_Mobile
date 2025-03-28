package com.example.visionbook.models

import com.example.visionbook.R

sealed class NavigationItems(var route: String, var icon: Int, var title: String) {
    object Home : NavigationItems("home", R.drawable.home, "Home")
    object Books : NavigationItems("books", R.drawable.book, "Books")
    object NFC:NavigationItems("nfc", R.drawable.theme, "NFC")
    object Camera : NavigationItems("camera", R.drawable.camera, "Camera")
    object QrCamera: NavigationItems("qrcamera", R.drawable.camera, "QrCamera")
    object Bookmarks : NavigationItems("bookmarks", R.drawable.bookmark, "Bookmarks")
    object Profile : NavigationItems("profile", R.drawable.profile, "Profile")
    object Post : NavigationItems("post", R.drawable.profile, "Post")
    object CameraInProfile : NavigationItems("camerainprofile", R.drawable.camera, "CameraInProfile")
    object CameraInMain : NavigationItems("camerainmain", R.drawable.camera, "CameraInMain")
    object Gallery : NavigationItems("gallery", R.drawable.camera, "Gallery")
}
