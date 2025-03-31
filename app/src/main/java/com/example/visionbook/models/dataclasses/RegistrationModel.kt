package com.example.visionbook.models.dataclasses

import android.nfc.NdefMessage

data class RegistrationModel(
    val fio: String,
    val dep: String,
    val email: String,
    val password: String
)
