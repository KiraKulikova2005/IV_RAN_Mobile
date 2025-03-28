package com.example.visionbook.view.authScreens

import android.app.Activity
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Bundle
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.visionbook.models.NavigationItems
import java.nio.charset.StandardCharsets

@Composable
fun NFCScreen(navController: NavController) {
    val context = LocalContext.current
    val activity = context as Activity
    val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    var nfcMessage by remember { mutableStateOf("Поднесите NFC-метку...") }
    var showDialog by remember { mutableStateOf(false) }

    // Callback для обнаружения меток
    val readerCallback = remember {
        object : NfcAdapter.ReaderCallback {
            override fun onTagDiscovered(tag: Tag) {
                try {
                    val ndef = Ndef.get(tag)
                    if (ndef != null) {
                        ndef.connect()

                        // Создаем NDEF запись с нашим текстом
                        val textRecord = createTextRecord("ИВ РАН") //потом тут будет индекс
                        val message = NdefMessage(arrayOf(textRecord))

                        ndef.writeNdefMessage(message)
                        activity.runOnUiThread {
                            nfcMessage = "Данные успешно записаны!"
                            showDialog = true
                        }
                        ndef.close()
                    } else {
                        activity.runOnUiThread {
                            nfcMessage = "Метка не поддерживает NDEF"
                        }
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
            title = { Text("Создать новую сессию?") },
            text = { Text("Вы хотите создать новую сессию и перейти к камере?") },
            confirmButton = {
                Button(
                    onClick = {
                        showDialog = false
                        navController.navigate(NavigationItems.Camera.route)
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                Button(
                    onClick = { showDialog = false }
                ) {
                    Text("Нет")
                }
            }
        )
    }

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = nfcMessage,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(onClick = { navController.popBackStack() }) {
            Text("Назад")
        }
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