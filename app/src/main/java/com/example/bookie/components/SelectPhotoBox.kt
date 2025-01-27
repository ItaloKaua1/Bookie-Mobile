package com.example.bookie.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter

@Composable
fun SelectPhotoBox(
    modifier: Modifier = Modifier,
    onPhotoSelected: (Uri?) -> Unit = {}
) {
    var profilePicture by remember { mutableStateOf<Uri?>(null) }
    var isDialogOpen by remember { mutableStateOf(false) }

    // Launcher para selecionar imagem da galeria
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        profilePicture = uri
        onPhotoSelected(uri)
    }

    // Caixa para exibição da imagem ou mensagem inicial
    Box(
        modifier = modifier
            .size(150.dp) // Tamanho maior para facilitar a visualização da imagem
            .clip(CircleShape)
            .border(2.dp, Color(0xFF6A1B9A), CircleShape)
            .clickable { isDialogOpen = true },
        contentAlignment = Alignment.Center
    ) {
        if (profilePicture == null) {
            Text(
                "Adicionar Foto",
                textAlign = TextAlign.Center
            )
        } else {
            Image(
                painter = rememberAsyncImagePainter(profilePicture),
                contentDescription = "Foto do usuário",
                modifier = Modifier.size(150.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }

    // Exibição do diálogo para escolher entre galeria
    if (isDialogOpen) {
        showPhotoSourceDialog(
            galleryLauncher = galleryLauncher,
            onClose = { isDialogOpen = false }
        )
    }
}

@Composable
fun showPhotoSourceDialog(
    galleryLauncher: ActivityResultLauncher<String>,
    onClose: () -> Unit
) {
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
                Text(
                    text = "Galeria",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            galleryLauncher.launch("image/*")
                            onClose()
                        }
                        .padding(16.dp)
                )
            }
        }
    )
}