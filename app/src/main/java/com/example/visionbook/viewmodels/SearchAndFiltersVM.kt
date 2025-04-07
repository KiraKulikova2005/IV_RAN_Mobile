package com.example.visionbook.viewmodels

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class SearchAndFiltersVM : ViewModel() {
    private val _showSearchAndFilters = mutableStateOf(false)

    val showSearchAndFilters: MutableState<Boolean>
        get() = _showSearchAndFilters

    fun toggleSearchAndFilters() {
        _showSearchAndFilters.value = !_showSearchAndFilters.value
    }

    // Новый метод для сброса состояния поиска
    fun resetSearchState() {
        _showSearchAndFilters.value = false
    }

    // Используем MutableState для простоты интеграции с Compose
    val isSearchActive = mutableStateOf(false) // Изначально поиск не активен

    fun setSearchActive(isActive: Boolean) {
        isSearchActive.value = isActive
    }

    val searchQuery = mutableStateOf("")
}