package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
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
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Tema Escuro", modifier = Modifier.padding(end = 16.dp))
                    Switch(
                        checked = temaEscuro,
                        onCheckedChange = { viewModel.alternarTema() }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Ativar Notificações", modifier = Modifier.padding(end = 16.dp))
                    Switch(
                        checked = notificacoesAtivadas,
                        onCheckedChange = { viewModel.alternarNotificacoes(it) }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text("Habilitar Animações", modifier = Modifier.padding(end = 16.dp))
                    Switch(
                        checked = animacoesAtivadas,
                        onCheckedChange = { viewModel.alternarAnimacoes(it) }
                    )
                }
            }
        }
    }
}
