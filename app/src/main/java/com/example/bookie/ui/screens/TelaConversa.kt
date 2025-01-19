package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.components.CardConversa
import com.example.bookie.components.CardMensagem
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Mensagem

@Composable
fun TelaConversa(navController: NavController) {
    var text by remember { mutableStateOf("") }
    var mensagens by remember { mutableStateOf(listOf<Mensagem>()) }

    LayoutVariant(navController, "Nome") {
        Column {
            LazyColumn(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                if (mensagens.isEmpty()) {
                    item {
                        Text(text = "Ainda não há mensagens", style = MaterialTheme.typography.bodyMedium)
                    }
                } else {
                    mensagens.forEach { mensagem ->
                        item {
                            CardMensagem()
                        }
                    }
                }
            }
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Procure pessoas ou chats...") },
                leadingIcon = { Icon(Icons.Filled.Send, contentDescription = "Enviar") },
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}