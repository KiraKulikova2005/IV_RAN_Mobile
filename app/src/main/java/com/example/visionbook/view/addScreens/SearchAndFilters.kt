package com.example.visionbook.view.addScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.visionbook.R
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.visionbook.viewmodels.SearchAndFiltersVM


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAndFilters(viewModel: SearchAndFiltersVM) {
    var text by viewModel.searchQuery // Связываем с VM
    val active by viewModel.isSearchActive // Используем состояние из VM
    if (active) { // Доп. проверка, если SearchAndFilters рендерится всегда
        SearchBar(
            query = text,
            onQueryChange = { text = it },
            onSearch = {
                // Логика поиска... возможно, обновить список items
                viewModel.setSearchActive(false) // Закрыть после поиска
                // text = "" // Очищать или нет - по логике приложения
            },
            active = active, // Используем состояние из VM
            onActiveChange = { isActive ->
                // Этот колбэк вызывается SearchBar'ом, например, при клике вне поля
                viewModel.setSearchActive(isActive)
            },
            placeholder = { Text(stringResource(R.string.search)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }, // Обычно добавляют иконку слева
            trailingIcon = {
                Icon(
                    imageVector = Icons.Default.Close, // Используем стандартную иконку
                    contentDescription = "Close Search",
                    modifier = Modifier.clickable {
                        if (text.isNotEmpty()) {
                            text = "" // Очистить текст
                        } else {
                            viewModel.setSearchActive(false) // Закрыть поиск
                        }
                    }
                )
            }
        ) {
            // История поиска или предложения
            // Пример:
            val historyItems = remember { mutableListOf("VisionBook", "Iglobruuuh") }
            historyItems.forEach { item ->
                Row(modifier = Modifier.padding(14.dp).clickable { text = item }) { // Кликабельно для выбора
                    Icon(
                        painter = painterResource(R.drawable.history), "History",
                        modifier = Modifier.padding(end = 10.dp).size(24.dp) // Меньше размер
                    )
                    Text(text = item)
                }
            }
        }
    }
}