package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bookie.components.ScreenTopBar
import com.example.bookie.models.ThemeViewModel

@Composable
fun ConfiguracoesScreen(themeViewModel: ThemeViewModel, onBackClick: () -> Unit) {
    val isDarkTheme = themeViewModel.isDarkTheme.collectAsState() // Observa o estado do tema

    Column {
        ScreenTopBar(
            title = "Configurações",
            onBackClick = onBackClick // Ação para voltar
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Tema", fontSize = 18.sp, modifier = Modifier.padding(bottom = 8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Claro")
                RadioButton(
                    selected = !isDarkTheme.value,
                    onClick = { themeViewModel.toggleTheme(false) },
                    colors = RadioButtonDefaults.colors()
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Escuro")
                RadioButton(
                    selected = isDarkTheme.value,
                    onClick = { themeViewModel.toggleTheme(true) },
                    colors = RadioButtonDefaults.colors()
                )
            }
        }
    }
}
