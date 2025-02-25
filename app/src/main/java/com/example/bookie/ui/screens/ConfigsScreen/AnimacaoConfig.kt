package com.example.bookie.ui.screens.ConfigsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
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
import com.example.bookie.components.LayoutVariant
import com.example.bookie.ui.theme.quaternary

@Composable
fun AnimacaoConfig(navController: NavController, viewModel: ConfiguracoesViewModel = viewModel()) {

    val animacoesAtivadas = viewModel.animacoesAtivadas.collectAsState().value

    MaterialTheme {
        LayoutVariant(navController, "Animações", false) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Configurações de Animações",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.quaternary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Opção 1: Animações Ativadas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { viewModel.alternarAnimacoes(true) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = animacoesAtivadas,
                            onClick = { viewModel.alternarAnimacoes(true) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ativar Animações",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Opção 2: Animações Desativadas
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { viewModel.alternarAnimacoes(false) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = !animacoesAtivadas,
                            onClick = { viewModel.alternarAnimacoes(false) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Desativar Animações",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}