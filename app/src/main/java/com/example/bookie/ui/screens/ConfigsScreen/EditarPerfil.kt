package com.example.bookie.ui.screens.ConfigsScreen

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.bookie.components.LayoutVariant
import androidx.navigation.NavHostController
import com.example.bookie.UserRepository
import com.example.bookie.ui.theme.PurpleBookie

@Composable
fun EditarPerfil(navController: NavHostController) {
    var bio by remember { mutableStateOf("") }
    var imagemUri by remember { mutableStateOf<Uri?>(null) }

    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val focusManager = LocalFocusManager.current

    val colorScheme = MaterialTheme.colorScheme

    LayoutVariant(navController, "Editar Perfil", false) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable { focusManager.clearFocus() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(colorScheme.secondary)
                            .clickable { /* Abrir seletor de imagem */ },
                        contentAlignment = Alignment.Center
                    ) {
                        if (imagemUri == null) {
                            Text(text = "Adicionar Foto", color = colorScheme.onSecondary)
                        } else {
                            Image(
                                painter = rememberAsyncImagePainter(imagemUri),
                                contentDescription = "Foto do perfil",
                                modifier = Modifier.size(120.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Bio") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.surfaceVariant, // Cor de fundo quando focado
                        unfocusedContainerColor = colorScheme.surface, // Cor de fundo quando não está focado
                        focusedIndicatorColor = Color.Transparent, // Remove a linha inferior quando focado
                        unfocusedIndicatorColor = Color.Transparent // Remove a linha inferior quando não está focado
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            color = colorScheme.surfaceVariant, // Cor de fundo do container
                            shape = RoundedCornerShape(8.dp) // Borda arredondada
                        )
                        .padding(8.dp), // Espaçamento interno
                    shape = RoundedCornerShape(8.dp) // Borda arredondada para o TextField
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = { navController.navigate("configuracoesTela") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(56.dp)
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurpleBookie
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Salvar Alterações",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}