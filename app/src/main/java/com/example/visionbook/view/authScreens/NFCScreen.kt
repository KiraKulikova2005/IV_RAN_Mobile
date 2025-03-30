package com.example.visionbook.view.authScreens

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.visionbook.R
import com.example.visionbook.models.NavigationItems
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import java.nio.charset.StandardCharsets

@Composable
fun NFCScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    var nfcMessage by remember { mutableStateOf("Поднесите телефон к NFC-метке для сканирования") }
    var showDialog by remember { mutableStateOf(false) }
    var scanCompleted by remember { mutableStateOf(false) }

    // Новое состояние для заголовка
    var headerText by remember {
        mutableStateOf("Сканирование NFC-метки")
    }

    Row(
        modifier = Modifier.padding(bottom = 80.dp)
    ) { BackButton(navController) }

    val readerCallback = remember {
        object : NfcAdapter.ReaderCallback {
            override fun onTagDiscovered(tag: Tag) {
                try {
                    val ndef = Ndef.get(tag)
                    if (ndef != null) {
                        ndef.connect()
                        val textRecord = createTextRecord("ИВ РАН")
                        val message = NdefMessage(arrayOf(textRecord))
                        ndef.writeNdefMessage(message)
                        activity.runOnUiThread {
                            nfcMessage = "Сканирование успешно завершено!"
                            headerText = "Сканирование завершено" // Меняем заголовок
                            scanCompleted = true
                            showDialog = true
                        }
                        ndef.close()
                    }
                } catch (e: Exception) {
                    activity.runOnUiThread {
                        nfcMessage = "Ошибка: ${e.localizedMessage}"
                    }
                    Log.e("NFC", "Ошибка записи", e)
                }
            }
        }
    }

    // Включаем режим чтения/записи
    DisposableEffect(Unit) {
        if (nfcAdapter == null) {
            nfcMessage = "NFC не поддерживается"
            return@DisposableEffect onDispose {}
        }

        if (!nfcAdapter.isEnabled) {
            nfcMessage = "Включите NFC в настройках"
            return@DisposableEffect onDispose {}
        }

        val options = Bundle().apply {
            putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 250)
        }

        nfcAdapter.enableReaderMode(
            activity,
            readerCallback,
            NfcAdapter.FLAG_READER_NFC_A or
                    NfcAdapter.FLAG_READER_NFC_B or
                    NfcAdapter.FLAG_READER_NFC_F or
                    NfcAdapter.FLAG_READER_NFC_V or
                    NfcAdapter.FLAG_READER_NFC_BARCODE,
            options
        )

        onDispose {
            nfcAdapter.disableReaderMode(activity)
        }
    }

    // Диалоговое окно для подтверждения создания новой сессии
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.White, // Белый фон диалога
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(
                        onClick = { showDialog = false }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.Black // Чёрный цвет иконки
                        )
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Создать новую сессию и перейти к сканированию QR-кода?",
                        textAlign = TextAlign.Center,
                        color = Color.Black, // Чёрный текст
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontStyle = FontStyle.Normal // Убираем курсив
                        )
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            showDialog = false
                            navController.navigate(NavigationItems.Camera.route)
                        }
                    ) {
                        Text(
                            "Да",
                            color = Color.White, // Чёрный текст
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontStyle = FontStyle.Normal // Убираем курсив
                            )
                        )
                    }
                }
            }
        )
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Большой жирный заголовок
        Text(
            text = headerText,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            ),
            modifier = Modifier.padding(top = 100.dp, bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        // Картинка NFC
        Image(
            painter = painterResource(
                id = if (scanCompleted) R.drawable.nfc_done else R.drawable.nfc_scan
            ),
            contentDescription = "NFC Status",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 24.dp)
        )

        // Описательный текст
        Text(
            text = nfcMessage,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )
    }
}

private fun createTextRecord(text: String): NdefRecord {
    val lang = "en".toByteArray(StandardCharsets.US_ASCII)
    val textBytes = text.toByteArray(StandardCharsets.UTF_8)
    val payload = ByteArray(1 + lang.size + textBytes.size)

    payload[0] = lang.size.toByte()
    System.arraycopy(lang, 0, payload, 1, lang.size)
    System.arraycopy(textBytes, 0, payload, 1 + lang.size, textBytes.size)

    return NdefRecord(
        NdefRecord.TNF_WELL_KNOWN,
        NdefRecord.RTD_TEXT,
        ByteArray(0),
        payload
    )
}