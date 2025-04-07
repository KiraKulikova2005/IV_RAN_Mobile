package com.example.visionbook.data

import androidx.annotation.DrawableRes

data class BookListItemData(
    @DrawableRes val coverResId: Int,
    val type: String, // "Книга" или "Журнал"
    val title: String,
    val author: String,
    val year: String,
    val id: String // Добавим ID для потенциального использования в будущем
)