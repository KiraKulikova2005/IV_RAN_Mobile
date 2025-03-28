package com.example.visionbook.view.camerasBookNProfile

import android.content.Context
import android.net.Uri
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.visionbook.R
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.visionbook.models.FaceAnalyser
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.BackButton
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.ButtonCaptureImage
import com.example.visionbook.view.camerasBookNProfile.itemsInCameras.FlashToggleButton
import com.example.visionbook.view.camerasBookNProfile.secondCameraScreens.CanceledPermissonScreen
import java.io.File
import java.util.concurrent.ExecutionException
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
        CanceledPermissonScreen()
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

    // Camera Provider
    val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

    val cameraProvider by produceState<ProcessCameraProvider?>(initialValue = null) {
        value = try {
            cameraProviderFuture.get()
        } catch (e: ExecutionException) {
            null
        }
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
                            setAnalyzer(executor, FaceAnalyser())
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

        // В верхней панели
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

        // Нижняя панель с кнопкой съемки
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopStart)
        ) {
            BackButton(navController = navController)
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp)
                .align(Alignment.BottomCenter)
        ) {
            ButtonCaptureImage(
                context,
                outputDirectory,
                onMediaCaptured,
                imageCapture,
                executor
            )
        }
    }
}