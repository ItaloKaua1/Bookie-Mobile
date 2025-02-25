package com.example.bookie.ui.screens.ConfigsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookie.models.ConfiguracoesViewModel
import com.example.bookie.models.ThemeOption
import com.example.bookie.components.LayoutVariant
import com.example.bookie.ui.theme.quaternary
import java.util.Calendar

@Composable
fun NotificacoesConfig(navController: NavController, viewModel: ConfiguracoesViewModel = viewModel()) {
    val notificacoesAtivadas = viewModel.notificacoesAtivadas.collectAsState().value
    // Coleta a opção de tema (agora do tipo ThemeOption)
    val themeOption = viewModel.themeOption.collectAsState().value

    // Define se o tema é escuro com base na opção selecionada
    val isDarkTheme = when (themeOption) {
        ThemeOption.DARK -> true
        ThemeOption.LIGHT -> false
        ThemeOption.AUTO -> {
            // Exemplo: tema claro entre 6h e 18h; caso contrário, escuro
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            hour < 6 || hour >= 18
        }
    }
    val cores = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = cores) {
        LayoutVariant(navController, "Notificações", false) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Configurações de notificações",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.quaternary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Opção 1: Ativar Notificações
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { viewModel.alternarNotificacoes(true) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = notificacoesAtivadas,
                            onClick = { viewModel.alternarNotificacoes(true) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ativar Notificações",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Opção 2: Desativar Notificações
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { viewModel.alternarNotificacoes(false) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = !notificacoesAtivadas,
                            onClick = { viewModel.alternarNotificacoes(false) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Desativar Notificações",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
