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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.visionbook.R
import com.example.visionbook.models.NavigationItems
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import com.example.visionbook.viewmodels.AuthVM
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import kotlin.experimental.and

@Composable
fun NFCScreen(navController: NavHostController,
              authViewModel: AuthVM = viewModel()) {
    val context = LocalContext.current
    val activity = context as Activity
    val nfcAdapter = NfcAdapter.getDefaultAdapter(context)
    var nfcMessage by remember { mutableStateOf("Поднесите телефон к NFC-метке для сканирования") }
    var showDialog by remember { mutableStateOf(false) }
    var scanCompleted by remember { mutableStateOf(false) }
    var headerText by remember { mutableStateOf("Сканирование NFC-метки") }
    var tagContent by remember { mutableStateOf("") }

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
                        val ndefMessage = ndef.ndefMessage

                        if (ndefMessage != null) {
                            val records = ndefMessage.records
                            val contentBuilder = StringBuilder()

                            for (record in records) {
                                if (record.tnf == NdefRecord.TNF_WELL_KNOWN &&
                                    Arrays.equals(record.type, NdefRecord.RTD_TEXT)) {
                                    // Декодируем текст с учетом языка
                                    val payload = record.payload
                                    val textEncoding = if ((payload[0] and 0x80.toByte()) == 0.toByte()) "UTF-8" else "UTF-16"
                                    val languageCodeLength = payload[0] and 0x3F
                                    val text = String(
                                        payload,
                                        languageCodeLength + 1,
                                        payload.size - languageCodeLength - 1,
                                        Charset.forName(textEncoding)
                                    )
                                    contentBuilder.append(text)
                                }
                                // Обработка других типов записей (URI и т.д.)
                                else if (record.toUri() != null) {
                                    contentBuilder.append(record.toUri().toString())
                                }
                            }

                            val content = contentBuilder.toString().trim()
                            activity.runOnUiThread {
                                tagContent = content
                                nfcMessage = if (content.isNotEmpty()) {
                                    "id: $content"
                                } else {
                                    "Метка пуста или содержит неподдерживаемые данные"
                                }
                                headerText = "Сканирование завершено"
                                scanCompleted = true
                                showDialog = true
                            }
                        } else {
                            activity.runOnUiThread {
                                nfcMessage = "Метка не содержит данных"
                                headerText = "Сканирование завершено"
                                scanCompleted = true
                                showDialog = true
                            }
                        }
                        ndef.close()
                    }
                } catch (e: Exception) {
                    activity.runOnUiThread {
                        nfcMessage = "Ошибка: ${e.localizedMessage}"
                        Log.e("NFC", "Ошибка чтения", e)
                    }
                }
            }
        }
    }


    // Включаем режим чтения
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
                    NfcAdapter.FLAG_READER_NFC_V,
            options
        )

        onDispose {
            nfcAdapter.disableReaderMode(activity)
        }
    }

    // Диалоговое окно после успешного считывания
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            containerColor = Color.White,
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopEnd
                ) {
                    IconButton(onClick = { showDialog = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Закрыть",
                            tint = Color.Black
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
                        text = if (tagContent.isNotEmpty()) {
                            "id:\n$tagContent\n\nПерейти к сканированию QR-кода?"
                        } else {
                            "Метка не содержит данных\n\nПерейти к сканированию QR-кода?"
                        },
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyLarge
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
                        Text("Да", color = Color.White)
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
        Text(
            text = headerText,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            ),
            modifier = Modifier.padding(top = 100.dp, bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(
                id = if (scanCompleted) R.drawable.nfc_done else R.drawable.nfc_scan
            ),
            contentDescription = "NFC Status",
            modifier = Modifier
                .size(300.dp)
                .padding(bottom = 24.dp)
        )

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