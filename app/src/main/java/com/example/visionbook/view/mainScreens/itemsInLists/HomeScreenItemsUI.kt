package com.example.visionbook.view.mainScreens.itemsInLists

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.visionbook.R
import com.example.visionbook.data.DataBooksScreen
import com.example.visionbook.models.NavigationItems
import com.example.visionbook.models.NoRippleInteractionSource
import com.example.visionbook.viewmodels.AuthVM

@Composable
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier
) {
    // Получаем текущую тему
    val colors = MaterialTheme.colors

    // Определяем цвет текста и полосок в зависимости от темы
    val textColor = if (colors.isLight) Color.Black else Color.White
    val dividerColor = if (colors.isLight) Color.White else Color.Black

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(colors.surface),
        horizontalAlignment = Alignment.Start
    ) {
        // Верхняя полоска
        Divider(color = dividerColor, thickness = 1.dp)

        // Содержимое карточки
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Обложка книги
            AsyncImage(
                model = book.coverUrl,
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp)) // Отступ между обложкой и текстом

            // Текстовая часть
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Тип книги
                Text(
                    text = "Книга",
                    style = MaterialTheme.typography.titleMedium.copy(color = textColor)
                )

                // Название книги
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.bodyLarge.copy(color = textColor)
                )

                // Автор
                Text(
                    text = book.author,
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                )

                // Год издания
                Text(
                    text = "${book.year} г.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = textColor)
                )
            }
        }

        // Нижняя полоска
        Divider(color = dividerColor, thickness = 1.dp)
    }
}

// Модель книги
data class Book(
    val coverUrl: String,
    val title: String,
    val author: String,
    val year: Int
)

// Пример использования
@Composable
fun HomeScreenItems(navController: NavController, authViewModel: AuthVM) {
    val books = listOf(
        Book(
            coverUrl = "https://example.com/book1.jpg",
            title = "Боги и демоны Древнего Египта",
            author = "Ксения Карлова",
            year = 2012
        ),
        Book(
            coverUrl = "https://example.com/book2.jpg",
            title = "Бродячие сюжеты в китайской средневековой прозе",
            author = "А.Б. Старостина",
            year = 2022
        ),
        Book(
            coverUrl = "https://example.com/book3.jpg",
            title = "О:та Го:йти. «Синтё:-ко: ки». «Записи о князе Нобунага»",
            author = "Е.У. Ванина",
            year = 2010
        )
    )

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        books.forEach { book ->
            BookCard(
                book = book,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .clickable {
                        navController.navigate("${NavigationItems.Post.route}/${book.title}")
                    }
            )
        }
    }
}