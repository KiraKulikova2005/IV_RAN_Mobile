package com.example.visionbook.view.camerasBookNProfile

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.visionbook.R
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.FlashToggleButton
import com.example.visionbook.view.navigation.GraphRoute
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.io.File
import java.util.concurrent.Executor

@Composable
fun CameraQr(
    directory: File,
    navController: NavController,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    isCameraPermissionGranted: MutableState<Boolean>
) {
    val camera: Camera? = null
    val executor = ContextCompat.getMainExecutor(context)
    if (isCameraPermissionGranted.value) {
        QrCameraPreview(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
            context = context,
            lifecycleOwner = lifecycleOwner,
            outputDirectory = directory,
            onMediaCaptured = { url -> },
            camera = camera,
            executor = executor
        )
    } else {
        BackButton(navController = navController)
    }
}

@Composable
fun QrCameraPreview(
    navController: NavController,
    modifier: Modifier = Modifier,
    context: Context,
    lifecycleOwner: LifecycleOwner,
    outputDirectory: File,
    onMediaCaptured: (Uri?) -> Unit,
    camera: Camera?,
    executor: Executor,
) {
    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }
    var preview by remember { mutableStateOf<Preview?>(null) }
    var cameraControl: CameraControl? by remember { mutableStateOf(null) }
    var isFlashEnabled by remember { mutableStateOf(false) }
    var hasFlash by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
    val cameraProvider by produceState<ProcessCameraProvider?>(initialValue = null) {
        value = cameraProviderFuture.get()
    }
    val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)
                cameraProviderFuture.addListener({
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .apply {
                            setAnalyzer(executor, BarcodeAnalyzer { barcodeValue ->
                                // Только показываем диалог, не навигируем сразу
                                showSuccessDialog = true
                            })
                        }

                    imageCapture = ImageCapture.Builder()
                        .setFlashMode(if (isFlashEnabled) ImageCapture.FLASH_MODE_ON else ImageCapture.FLASH_MODE_OFF)
                        .setTargetRotation(previewView.display.rotation)
                        .build()

                    cameraProvider?.unbindAll()
                    val camera = cameraProvider?.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        imageCapture,
                        preview,
                        imageAnalysis
                    )

                    camera?.let {
                        cameraControl = it.cameraControl
                        hasFlash = it.cameraInfo.hasFlashUnit()
                        if (isFlashEnabled) {
                            it.cameraControl.enableTorch(isFlashEnabled)
                        }
                    }
                }, executor)

                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )

        // Заголовок "Сканирование QR-кода"
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 90.dp)
                .padding(horizontal = 30.dp)
                .background(Color.White, shape = RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Сканирование QR-кода",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(10.dp),
                color = Color.Black
            )
        }

        // Рамка области сканирования
        Icon(
            painter = painterResource(id = R.drawable.frame),
            contentDescription = "Рамка сканирования",
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center)
        )

        // Верхняя панель с кнопкой вспышки
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            if (hasFlash) {
                FlashToggleButton(
                    isFlashEnabled = isFlashEnabled,
                    onFlashToggle = {
                        isFlashEnabled = !isFlashEnabled
                        cameraControl?.enableTorch(isFlashEnabled)
                        imageCapture?.flashMode = if (isFlashEnabled) ImageCapture.FLASH_MODE_ON
                        else ImageCapture.FLASH_MODE_OFF
                    }
                )
            }
        }

        // Кнопка "Назад"
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            BackButton(navController = navController)
        }

        // Диалог успешного сканирования
        if (showSuccessDialog) {
            AlertDialog(
                onDismissRequest = {
                    showSuccessDialog = false
                },
                confirmButton = {
                    Button(onClick = {
                        showSuccessDialog = false
                        // Переход на главную страницу при нажатии OK
                        navController.navigate(GraphRoute.MAIN) {
                            popUpTo(GraphRoute.MAIN) {
                                inclusive = true
                            }
                        }
                    }) {
                        Text("OK")
                    }
                },
                title = { Text("Успех!") },
                text = { Text("Сканирование прошло успешно!") }
            )
        }
    }
}

// Анализатор QR-кодов
class BarcodeAnalyzer(private val onBarcodeDetected: (String) -> Unit) : ImageAnalysis.Analyzer {
    private val scanner = BarcodeScanning.getClient()

    override fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image ?: return
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                barcodes.firstOrNull()?.rawValue?.let {
                    onBarcodeDetected(it)
                }
            }
            .addOnFailureListener {
                // Ошибка сканирования
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }
}