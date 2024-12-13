package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.bookie.models.ThemeViewModel

@Composable
fun ConfiguracoesScreen(themeViewModel: ThemeViewModel) {
    val isDarkTheme = themeViewModel.isDarkTheme.collectAsState() // Observa o estado do tema

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp, bottom = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Brightness4,
                    contentDescription = "Configurações de Tema",
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(text = "Configurações", fontSize = 20.sp)
            }
        }
    ) { contentPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(contentPadding)
                .padding(16.dp)
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
