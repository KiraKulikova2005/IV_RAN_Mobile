package com.example.visionbook.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.visionbook.data.ProfileItem
import com.example.visionbook.data.DataProfileScreen
class ProfileScreenVM: ViewModel() {
    // Список профилей
    private val _profileList: MutableState<List<ProfileItem>> = mutableStateOf(generateProfileList())
    val profileList: MutableState<List<ProfileItem>> = _profileList

    // Генерация списка профилей
    private fun generateProfileList(): List<ProfileItem> {
        val profiles = mutableListOf<ProfileItem>()
        val count = 10
        repeat(count) { index ->
            val id = index.toString()
            val role = "Лаборант"
            val booksCount = (1..100).random() // Для примера, случайное количество подписчиков
            val nickname = "Иванов И.И."
            val email = "example@exaple.ru"
            val dep = "ИВ РАН"
            val profile = ProfileItem(id, nickname, role, booksCount, email, dep)
            profiles.add(profile)
        }
        return profiles
    }
}