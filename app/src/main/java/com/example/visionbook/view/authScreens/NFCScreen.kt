package com.example.visionbook.view.authScreens

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
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
import java.nio.charset.Charset

@Composable
fun NFCScreen(navController: NavController) {
    val context = LocalContext.current
    val nfcAdapter = remember { NfcAdapter.getDefaultAdapter(context) }
    var nfcMessage by remember { mutableStateOf("Ожидание NFC-метки...") }

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                val activity = context as Activity
                val intent = activity.intent
                if (intent.action == NfcAdapter.ACTION_NDEF_DISCOVERED ||
                    intent.action == NfcAdapter.ACTION_TECH_DISCOVERED ||
                    intent.action == NfcAdapter.ACTION_TAG_DISCOVERED
                ) {
                    val tag: Tag? = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                    tag?.let {
                        nfcMessage = readNfcTag(it)
                    }
                }
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
            text = "Сканирование NFC-метки",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 20.dp),
            textAlign = TextAlign.Center
        )

        Image(
            painter = painterResource(id = R.drawable.nfc_scan), // Добавьте картинку NFC-сканирования
            contentDescription = "NFC Scan",
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 20.dp)
        )

        Text(
            text = "Поднесите телефон к NFC-метке для сканирования",
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

/**
 * Функция для чтения данных с NFC-метки
 */
private fun readNfcTag(tag: Tag): String {
    val ndef = Ndef.get(tag)
    return try {
        ndef?.connect()
        val ndefMessage = ndef?.ndefMessage
        val record = ndefMessage?.records?.get(0)
        val payload = record?.payload
        payload?.let {
            val textEncoding = if ((it[0].toInt() and 128) == 0) "UTF-8" else "UTF-16"
            val languageCodeLength = it[0].toInt() and 51
            String(it, languageCodeLength + 1, it.size - languageCodeLength - 1, Charset.forName(textEncoding))
        } ?: "Пустая метка"
    } catch (e: Exception) {
        "Ошибка чтения NFC"
    } finally {
        ndef?.close()
    }
}
