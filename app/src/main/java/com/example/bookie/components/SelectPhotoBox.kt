package com.seuprojeto.ui.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import java.io.File
import java.io.FileOutputStream

@Composable
fun SelectPhotoBox(
    modifier: Modifier = Modifier,
    onPhotoSelected: (Uri?) -> Unit = {}
) {
    var profilePicture by remember { mutableStateOf<Uri?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Launcher para selecionar imagem da galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePicture = uri
        onPhotoSelected(uri)
    }

    // Launcher para capturar foto com a câmera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            val uri = saveBitmapToUri(context, bitmap)
            profilePicture = uri
            onPhotoSelected(uri)
        }
    }

    // Launcher para solicitar permissão da câmera
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            // Aqui você pode adicionar uma mensagem ao usuário informando que a permissão foi negada
        }
    }

    // Lógica para lidar com o clique na opção "Câmera"
    fun handleCameraClick() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraLauncher.launch(null)
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    // Caixa para exibição da imagem ou mensagem inicial
    Box(
        modifier = modifier
            .size(100.dp)
            .border(2.dp, Color(0xFF6A1B9A))
            .clickable { isDialogOpen = true },
        contentAlignment = Alignment.Center
    ) {
        if (profilePicture == null) {
            Text("Adicionar Foto")
        } else {
            Image(
                painter = rememberAsyncImagePainter(profilePicture),
                contentDescription = null,
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Crop
            )
        }
    }

    // Exibição do diálogo para escolher entre galeria e câmera
    if (isDialogOpen) {
        showPhotoSourceDialog(
            galleryLauncher = galleryLauncher,
            onCameraClick = { handleCameraClick() },
            onClose = { isDialogOpen = false }
        )
    }
}

@Composable
fun showPhotoSourceDialog(
    galleryLauncher: ActivityResultLauncher<String>,
    onCameraClick: () -> Unit,
    onClose: () -> Unit
) {
    val options = listOf("Galeria", "Câmera")
    AlertDialog(
        onDismissRequest = { onClose() },
        confirmButton = {
            Text(
                text = "Fechar",
                modifier = Modifier.clickable { onClose() }
            )
        },
        text = {
            Column {
                options.forEach { option ->
                    Text(
                        text = option,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                if (option == "Galeria") {
                                    galleryLauncher.launch("image/*")
                                } else {
                                    onCameraClick()
                                }
                                onClose() // Fecha o diálogo após a seleção
                            }
                            .padding(16.dp)
                    )
                }
            }
        }
    )
}

fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri? {
    val filename = "photo_${System.currentTimeMillis()}.jpg"
    val file = File(context.cacheDir, filename)
    FileOutputStream(file).use {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
    }
    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.fileprovider",
        file
    )
}
