package com.example.visionbook.models

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.visionbook.data.BookListItemData
import com.example.visionbook.view.navigation.GraphRoute


@Composable
fun BookListItem(
    item: BookListItemData,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                // При клике переходим на экран Post (детали книги)
                // Позже сюда можно будет передать item.id
                navController.navigate(GraphRoute.POST)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp), // Отступы для строки
            verticalAlignment = Alignment.Top // Выравниваем по верху для разных высот текста
        ) {
            // Обложка
            Image(
                painter = painterResource(id = item.coverResId),
                contentDescription = item.title,
                modifier = Modifier
                    .height(110.dp) // Задаем высоту обложки
                    .width(75.dp)   // Задаем ширину обложки
                    .clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop // Или Fit
            )

            Spacer(modifier = Modifier.width(16.dp)) // Отступ между обложкой и текстом

            // Колонка с текстом
            Column(
                modifier = Modifier.weight(1f) // Занимает оставшееся место
            ) {
                Text(
                    text = item.type, // "Книга" или "Журнал"
                    style = MaterialTheme.typography.titleLarge, // Стиль для типа
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge, // Стиль для заголовка
                    maxLines = 3, // Ограничиваем кол-во строк заголовка
                    overflow = TextOverflow.Ellipsis // Добавляем троеточие, если не влезает
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.author,
                    style = MaterialTheme.typography.titleMedium // Стиль для автора
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = item.year,
                    style = MaterialTheme.typography.titleSmall, // Маленький стиль для года
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Менее яркий цвет
                )
            }
        }
        // Разделитель между элементами
        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.onBackground.copy()
        )
    }
}