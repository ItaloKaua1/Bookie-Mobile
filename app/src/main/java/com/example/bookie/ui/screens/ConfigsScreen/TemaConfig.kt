package com.example.bookie.ui.screens.ConfigsScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.ConfiguracoesViewModel
import com.example.bookie.models.ThemeOption
import com.example.bookie.ui.theme.quaternary
import java.util.Calendar

@Composable
fun TemaConfig(navController: NavController, viewModel: ConfiguracoesViewModel = viewModel()) {

    val themeOption = viewModel.themeOption.collectAsState().value

    val isDarkTheme = when (themeOption) {
        ThemeOption.DARK -> true
        ThemeOption.LIGHT -> false
        ThemeOption.AUTO -> {
            val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            hour < 6 || hour >= 18
        }
    }
    val cores = if (isDarkTheme) darkColorScheme() else lightColorScheme()

    MaterialTheme(colorScheme = cores) {
        LayoutVariant(navController, "Tema", false) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    "Configurações de tema",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = MaterialTheme.colorScheme.quaternary,
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Opção 1: Tema Claro
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { viewModel.setThemeOption(ThemeOption.LIGHT) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = (themeOption == ThemeOption.LIGHT),
                            onClick = { viewModel.setThemeOption(ThemeOption.LIGHT) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tema Claro",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Opção 2: Tema Escuro
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { viewModel.setThemeOption(ThemeOption.DARK) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = (themeOption == ThemeOption.DARK),
                            onClick = { viewModel.setThemeOption(ThemeOption.DARK) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tema Escuro",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Opção 3: Tema Automático (de dia, tema claro; de noite, tema escuro)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable { viewModel.setThemeOption(ThemeOption.AUTO) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        RadioButton(
                            selected = (themeOption == ThemeOption.AUTO),
                            onClick = { viewModel.setThemeOption(ThemeOption.AUTO) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Tema Automático",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
