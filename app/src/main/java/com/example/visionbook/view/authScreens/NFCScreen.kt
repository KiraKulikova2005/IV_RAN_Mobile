package com.example.visionbook.view.authScreens

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavController
import com.example.visionbook.R
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import java.nio.charset.StandardCharsets
import java.util.*

@Composable
fun NFCScreen(navController: NavController) {
    val context = LocalContext.current
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }
    var nfcMessage by remember { mutableStateOf("Ожидание NFC-метки...") }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Foreground Dispatch Setup
    DisposableEffect(Unit) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val intent = Intent(activity, activity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        val pendingIntent = PendingIntent.getActivity(
            activity,
            0,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

        val ndefFilter = IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED).apply {
            try {
                addDataType("application/com.example.visionbook")
            } catch (e: IntentFilter.MalformedMimeTypeException) {
                e.printStackTrace()
            }
        }

        val techFilter = IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED)
        val tagFilter = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)

        val filters = arrayOf(ndefFilter, techFilter, tagFilter)

        Log.d("NFCScreen", "Foreground dispatch настроен")
        nfcAdapter?.enableForegroundDispatch(
            activity,
            pendingIntent,
            filters,
            arrayOf(arrayOf(Ndef::class.java.name))
        )

        onDispose {
            Log.d("NFCScreen", "Foreground dispatch отключен")
            nfcAdapter?.disableForegroundDispatch(activity)
        }
    }

    // Обработка новых Intent через LifecycleObserver
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val currentIntent = (context as? Activity)?.intent
                if (currentIntent != null && isNfcIntent(currentIntent)) {
                    Log.d("NFCScreen", "Обнаружен NFC Intent: ${currentIntent.action}")
                    nfcMessage = writeTextToTag(currentIntent, "ИВ РАН")
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            Log.d("NFCScreen", "LifecycleObserver удален")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.padding(bottom = 20.dp)) {
            BackButton(navController)
        }

        Text(
            text = "Запись текста на NFC-метку",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.nfc_scan),
            contentDescription = "NFC Scan",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 20.dp)
        )

        Text(
            text = "Поднесите телефон к NFC-метке для записи",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Text(
            text = nfcMessage,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 10.dp)
        )

        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.padding(top = 40.dp),
            shape = RoundedCornerShape(30)
        ) {
            Text(text = "Назад")
        }
    }
}

// Проверка, является ли Intent NFC-событием
private fun isNfcIntent(intent: Intent): Boolean {
    return when (intent.action) {
        NfcAdapter.ACTION_NDEF_DISCOVERED,
        NfcAdapter.ACTION_TECH_DISCOVERED,
        NfcAdapter.ACTION_TAG_DISCOVERED -> {
            Log.d("NFCScreen", "NFC Intent обнаружен: ${intent.action}")
            true
        }
        else -> {
            Log.d("NFCScreen", "Неизвестный Intent: ${intent.action}")
            false
        }
    }
}

// Запись текста на NFC-метку
private fun writeTextToTag(intent: Intent, text: String): String {
    val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
    if (tag == null) {
        Log.e("NFCScreen", "Ошибка: метка не найдена")
        return "Ошибка: метка не найдена"
    }

    val ndef = Ndef.get(tag)
    if (ndef == null) {
        Log.e("NFCScreen", "Метка не поддерживает NDEF")
        return "Метка не поддерживает NDEF"
    }

    return try {
        ndef.connect()
        Log.d("NFCScreen", "Соединение с меткой установлено")

        if (!ndef.isWritable) {
            Log.e("NFCScreen", "Метка защищена от записи")
            return "Метка защищена от записи"
        }

        // Создаем NdefRecord с текстом
        val textBytes = text.toByteArray(StandardCharsets.UTF_8)
        val language = "en" // Язык текста
        val languageBytes = language.toByteArray(StandardCharsets.US_ASCII)
        val statusByte = (languageBytes.size and 0x1F).toByte() // Первый байт содержит длину языка
        val payload = ByteArray(1 + languageBytes.size + textBytes.size)
        payload[0] = statusByte
        System.arraycopy(languageBytes, 0, payload, 1, languageBytes.size)
        System.arraycopy(textBytes, 0, payload, 1 + languageBytes.size, textBytes.size)

        val record = NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, null, payload)
        val message = NdefMessage(arrayOf(record))

        // Записываем данные на метку
        ndef.writeNdefMessage(message)
        Log.d("NFCScreen", "Текст успешно записан: $text")
        "Текст успешно записан: $text"
    } catch (e: Exception) {
        Log.e("NFCScreen", "Ошибка записи: ${e.localizedMessage}", e)
        "Ошибка записи: ${e.localizedMessage}"
    } finally {
        ndef.close()
        Log.d("NFCScreen", "Соединение с меткой закрыто")
    }
}