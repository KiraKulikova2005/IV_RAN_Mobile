package com.example.visionbook.view.addScreens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.visionbook.R
import com.example.visionbook.models.AutoresizedText
import com.example.visionbook.models.NoRippleInteractionSource
import com.example.visionbook.models.PostScreenVMFactory
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import com.example.visionbook.viewmodels.AuthVM
import com.example.visionbook.viewmodels.BooksScreenVM
import com.example.visionbook.viewmodels.ExoPlayerVM
import com.example.visionbook.viewmodels.PostScreenVM
import com.example.visionbook.viewmodels.ProfileScreenVM
import com.google.android.exoplayer2.ui.PlayerView


@OptIn(ExperimentalMaterial3Api::class) // Для TopAppBar
@Composable
fun Post(
    navController: NavController, // NavController нужен для BackButton
    // ViewModel пока убираем, т.к. используем заглушки
    authViewModel: AuthVM = viewModel()
    // userViewModel: ProfileScreenVM = viewModel(),
    // bookViewModel: BooksScreenVM = viewModel(),
    // postViewModel: PostScreenVM = viewModel(...)
) {
    // --- Заглушки данных ---
    val bookCoverResId = R.drawable.book_cover_placeholder // ЗАМЕНИТЕ на ваш placeholder drawable
    val bookTitle = "Вехи конституционного пути Австралии (1788 - 2000 гг.)"
    val bookAuthor = "Скоробогатых Н.С."
    val bookStatus = "На месте"
    val bookId = "73946295"
    val bookIsbn = "5-89282-266-4"
    val publisher = "Институт востоковедения РАН"
    val year = "2006"
    val pages = "240"
    val address = "Москва"
    val department = "ИВ РАН"
    val floor = "2"
    val cabinet = "219"
    val bookcase = "2"
    val shelf = "3"
    // -----------------------

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Книга") },
                navigationIcon = {
                    BackButton(navController = navController)
                },
                colors = TopAppBarDefaults.topAppBarColors( // Опционально: настройте цвета AppBar
                    containerColor = MaterialTheme.colorScheme.background, // Или другой цвет
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Применяем отступы от Scaffold
                .padding(horizontal = 16.dp) // Добавляем горизонтальные отступы для контента
                .verticalScroll(rememberScrollState()) // Делаем колонку скроллящейся
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // Обложка книги
            Image(
                painter = painterResource(id = bookCoverResId),
                contentDescription = bookTitle,
                modifier = Modifier
                    .height(200.dp) // Задайте желаемую высоту
                    .fillMaxWidth(0.6f) // Занимает 60% ширины
                    .align(Alignment.CenterHorizontally) // Центрируем по горизонтали
                    .clip(RoundedCornerShape(8.dp)), // Слегка скругляем углы
                contentScale = ContentScale.Fit // Или ContentScale.Crop, смотря что лучше
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Название книги
            Text(
                text = bookTitle,
                style = MaterialTheme.typography.titleLarge, // Крупный шрифт для заголовка
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Автор книги
            Text(
                text = bookAuthor,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Чуть менее яркий цвет
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка статуса
            Button(
                onClick = { /* TODO: Действие для кнопки статуса */ },
                modifier = Modifier
                    .fillMaxWidth(0.7f) // Занимает 70% ширины
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(15.dp)
            ) {
                Text(bookStatus)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // --- Детали книги ---
            Text("id: $bookId", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Название: $bookTitle", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Автор: $bookAuthor", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("ISBN: $bookIsbn", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Издательство: $publisher", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Год: $year", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Страницы: $pages", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Адрес: $address", style = MaterialTheme.typography.titleMedium)
            // ---------------------

            Spacer(modifier = Modifier.height(32.dp))

            // --- Место ---
            Text(
                text = "Место",
                style = MaterialTheme.typography.titleLarge,// Заголовок секции
                fontWeight = FontWeight.Bold // Делаем жирным
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text("Подразделение: $department", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Этаж: $floor", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Кабинет: $cabinet", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Шкаф: $bookcase", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Полка: $shelf", style = MaterialTheme.typography.titleMedium)
            // -------------

            Spacer(modifier = Modifier.height(16.dp)) // Отступ снизу
        }
    }
}
