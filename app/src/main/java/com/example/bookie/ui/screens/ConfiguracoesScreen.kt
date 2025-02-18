package com.example.bookie.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookie.UserRepository
import com.example.bookie.components.ConfiguracoesViewModel
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.AuthManager.logout
import com.example.bookie.models.NavigationItem
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

private fun logout(navController: NavController, context: Context) {
    FirebaseAuth.getInstance().signOut()
    Toast.makeText(context, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
    navController.navigate("loginScreen")
}

@Composable
fun ConfiguracoesTela(navController: NavController, viewModel: ConfiguracoesViewModel = viewModel()) {
    val temaEscuro = viewModel.temaEscuro.collectAsState().value
    val cores = if (temaEscuro) darkColorScheme() else lightColorScheme()

    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val userName by userRepo.currentUserName.collectAsState(initial = "")
    val email by userRepo.currentUserEmail.collectAsState(initial = "")

    MaterialTheme(colorScheme = cores) {
        LayoutVariant(navController, "Configurações", false) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Seção do Perfil do Usuário
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate("editarPerfil") },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Foto do usuário (pode ser um ícone ou uma imagem carregada)
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto do usuário",
                        modifier = Modifier.size(64.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = userName,
                            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp)
                        )
                        Text(
                            text = "Editar foto e bio",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Seção Geral
                Text(
                    "Geral",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )
                ConfigItem(
                    icon = Icons.Default.Person,
                    text = "Nome",
                    description = userName,
                    action = { navController.navigate("editNome") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                )
                ConfigItem(
                    icon = Icons.Default.Email,
                    text = "Email",
                    description = email,
                    action = { navController.navigate("editEmail") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
                ConfigItem(
                    icon = Icons.Default.Lock,
                    text = "Senha",
                    description = "●●●●●●",
                    action = { navController.navigate("editSenha") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seção Exibição
                Text(
                    "Exibição",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )
                ConfigItem(
                    icon = Icons.Default.Palette,
                    text = "Tema",
                    action = { navController.navigate("temaConfig") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
                ConfigItem(
                    icon = Icons.Default.Animation,
                    text = "Animações",
                    action = { navController.navigate("animacaoConfig") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seção Notificações
                Text(
                    "Comunicações",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )
                ConfigItem(
                    icon = Icons.Default.Notifications,
                    text = "Notificações",
                    action = { navController.navigate("notiConfig") },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seção Conta
                Text(
                    "Conta",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(8.dp),
                    fontSize = 20.sp
                )
                ConfigItem(
                    icon = Icons.Outlined.ExitToApp,
                    text = "Sair",
                    description = "Encerrar sessão",
                    action = {
                        com.example.bookie.ui.screens.logout(navController, context)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp)
                )
            }
        }
    }
}

@Composable
fun ConfigItem(
    icon: ImageVector,
    text: String,
    description: String? = null,
    action: () -> Unit, // Renomeado de onClick para action
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { action() }
            .padding(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text, style = textStyle)
                description?.let {
                    Text(
                        text = it,
                        style = textStyle.copy(fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    )
                }
            }
        }
    }
}