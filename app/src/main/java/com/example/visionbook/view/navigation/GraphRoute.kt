package com.example.visionbook.view.navigation

object GraphRoute {
    // Root graphs
    const val ROOT = "root_graph"
    const val AUTH = "auth_graph"
    const val MAIN = "main_graph"
    const val PROFILE = "profile_graph"
    const val SETTINGS = "settings_graph"

    // Auth flows
    const val LOGIN = "login"
    const val REGISTRATION = "registration"
    const val FORGOT = "forgot"
    const val NFC = "nfc"
    const val QR_CAMERA = "qr_camera"

    // Main tabs
    const val HOME = "home"
    const val CAMERA = "camera"
    const val PROFILE_TAB = "profile_tab"

    // Profile flows
    const val PROFILE_VIEW = "profile_view"
    const val PROFILE_SETTINGS = "profile_settings"
    const val POST = "post"
    const val CAMERA_IN_PROFILE = "camera_in_profile"

    // Camera flows
    const val CAMERA_IN_MAIN = "camera_in_main"
    const val GALLERY = "gallery"
    const val NFC_READ = "nfc_read"

    // Settings flows
    const val NOTIFICATION = "notification"
    const val FAQ = "faq"
    const val SECURITY = "security"
}