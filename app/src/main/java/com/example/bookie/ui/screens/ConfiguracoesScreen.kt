package com.example.bookie.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookie.components.ConfiguracoesViewModel
import com.example.bookie.components.LayoutVariant

@Composable
fun ConfiguracoesTela(navController: NavController, viewModel: ConfiguracoesViewModel = viewModel()) {
    val temaEscuro = viewModel.temaEscuro.collectAsState().value
    val notificacoesAtivadas = viewModel.notificacoesAtivadas.collectAsState().value
    val animacoesAtivadas = viewModel.animacoesAtivadas.collectAsState().value
    val cores = if (temaEscuro) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = cores) {
        LayoutVariant(navController, "Configurações", false) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Seção Geral
                Text("Geral", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
                ConfigItem(
                    icon = Icons.Default.Person,
                    text = "Nome",
                    onClick = { navController.navigate("editNome")}
                )
                ConfigItem(
                    icon = Icons.Default.Email,
                    text = "Email",
                    onClick = { navController.navigate("editEmail")}
                )
                ConfigItem(
                    icon = Icons.Default.Lock,
                    text = "Senha",
                    onClick = { navController.navigate("editSenha") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seção Exibição
                Text("Exibição", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
                ConfigItem(
                    icon = Icons.Default.Palette,
                    text = "Tema",
                    onClick = { navController.navigate("temaConfig") }
                )
                ConfigItem(
                    icon = Icons.Default.Animation,
                    text = "Animações",
                    onClick = { navController.navigate("animacaoConfig") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Seção Notificações
                Text("Notificações", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(8.dp))
                ConfigItem(
                    icon = Icons.Default.Notifications,
                    text = "Notificações",
                    onClick = { navController.navigate("notiConfig") }
                )
            }
        }
    }
}

@Composable
fun ConfigItem(icon: ImageVector, text: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyMedium)
    }
}