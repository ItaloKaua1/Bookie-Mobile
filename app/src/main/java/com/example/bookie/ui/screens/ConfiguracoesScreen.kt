package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import androidx.compose.material.icons.outlined.Brightness4
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults

@ExperimentalMaterial3Api
@Composable
fun ConfiguracoesScreen(){
    val isDarkMode = remember { mutableStateOf(false) }

    Scaffold (
        topBar = {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Icon(
                    imageVector = Icons.Outlined.Brightness4,
                    contentDescription = "Confições de Tema",
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

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ){
                Text(text = "Claro")
                RadioButton(
                    selected = !isDarkMode.value,
                    onClick = {isDarkMode.value = false},
                    colors = RadioButtonDefaults.colors()
                )
            }

            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Escuro")
                RadioButton(
                    selected = !isDarkMode.value,
                    onClick = {isDarkMode.value = true},
                    colors = RadioButtonDefaults.colors()
                )
            }
        }

    }
}