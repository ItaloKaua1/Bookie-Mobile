package com.example.bookie.ui.screens.ConfigsScreen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.UserRepository
import com.example.bookie.components.LayoutVariant
import com.example.bookie.ui.theme.PurpleBookie
import kotlinx.coroutines.launch

@Composable
fun EditEmail(navController: NavController) {
    val context = LocalContext.current
    val userRepo = UserRepository(context)

    // Recupera o email atual do usuário
    val currentEmail by userRepo.currentUserEmail.collectAsState(initial = "")

    // Variável mutável para armazenar o valor temporário do email
    var email by remember { mutableStateOf(currentEmail) }

    // Estado para controlar a exibição do diálogo de reautenticação
    var showReauthDialog by remember { mutableStateOf(false) }

    // Escopo de coroutine para operações assíncronas
    val coroutineScope = rememberCoroutineScope()

    val focusManager = LocalFocusManager.current
    val colorScheme = MaterialTheme.colorScheme

    // Exibe o diálogo de reautenticação se necessário
    if (showReauthDialog) {
        ReauthenticateDialog(
            onConfirm = { password ->
                coroutineScope.launch {
                    val success = userRepo.reauthenticateAndUpdateEmail(email, password)
                    if (success) {
                        // Navega de volta para a tela de configurações após salvar
                        navController.navigate("configuracoesTela")
                    } else {
                        // Mostrar uma mensagem de erro ao usuário
                        // (pode ser um Snackbar ou um Toast)
                    }
                    showReauthDialog = false
                }
            },
            onDismiss = { showReauthDialog = false }
        )
    }

    MaterialTheme {
        LayoutVariant(navController, "Editar Email", false) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { focusManager.clearFocus() }
                    .padding(16.dp)
            ) {
                Text(
                    text = "Defina seu email",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.surfaceVariant,
                        unfocusedContainerColor = colorScheme.surface,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        showReauthDialog = true
                    },
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
                        text = "Salvar",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}