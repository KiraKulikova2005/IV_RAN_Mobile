package com.example.visionbook.view.mainScreens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.visionbook.data.BookListItemData
import com.example.visionbook.models.BookListItem
import com.example.visionbook.view.mainScreens.itemsInLists.HomeScreenItems
import com.example.visionbook.viewmodels.AuthVM
import com.example.visionbook.R



@Composable
fun HomeScreen(navController: NavController, authViewModel: AuthVM = viewModel()) {

    // --- Список заглушек ---
    val dummyBookList = remember { // remember, чтобы список не создавался заново при рекомпозиции
        listOf(
            BookListItemData(R.drawable.book_cover_placeholder, "Книга", "Боги и демоны Древнего Египта", "Ксения Карлова", "2012 г.", "1"),
            BookListItemData(R.drawable.book_cover_placeholder, "Книга", "Бродячие сюжеты в китайской средневековой прозе", "А.Б. Старостина", "2022 г.", "2"),
            BookListItemData(R.drawable.book_cover_placeholder, "Книга", "О:та Гю:ити. «Синтё:-ко: ки» «Записи о князе Нобунага»", "Е.У. Ванина", "2010 г.", "3"),
            BookListItemData(R.drawable.book_cover_placeholder, "Журнал", "Южно-Тихоокеанский регион", "С.А. Полхов", "2014 г.", "4"),
            BookListItemData(R.drawable.book_cover_placeholder, "Журнал", "Учение Ван Янмина и классическая китайская философия", "Т.В. Степанова", "2015 г.", "5"),
            // Добавьте еще или повторите для длинного списка
            BookListItemData(R.drawable.book_cover_placeholder, "Книга", "Боги и демоны Древнего Египта", "Ксения Карлова", "2012 г.", "6"),
            BookListItemData(R.drawable.book_cover_placeholder, "Книга", "Бродячие сюжеты в китайской средневековой прозе", "А.Б. Старостина", "2022 г.", "7"),
        )
    }
    // -----------------------

    LazyColumn(
        modifier = Modifier.fillMaxSize()
        // Не нужно доп. padding, если BookListItem сам управляет отступами
    ) {
        // Отображаем элементы списка
        items(dummyBookList) { bookItem ->
            BookListItem(item = bookItem, navController = navController)
        }
    }
}