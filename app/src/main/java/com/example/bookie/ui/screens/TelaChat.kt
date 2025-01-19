package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
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
import com.example.bookie.components.AvatarChat
import com.example.bookie.components.CardConversa
import com.example.bookie.components.CardPost
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Conversa
import com.example.bookie.models.Usuario

@Composable
fun TelaChat(navController: NavController) {
    var text by remember { mutableStateOf("") }
    var amigos by remember { mutableStateOf(listOf<Usuario>()) }
    var conversas by remember { mutableStateOf(listOf<Conversa>()) }

    LayoutVariant(navController, "Chat") {
        Column(
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Procure pessoas ou chats...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                modifier = Modifier.fillMaxWidth(),
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().height(48.dp).padding(bottom = 32.dp),
        ) {
            if (amigos.isEmpty()) {
                item {
                    Text(text = "Ainda não há amigos", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                amigos.forEach { amigo ->
                    item {
                        AvatarChat()
                    }
                }
            }
        }

        Text(text = "Mensagens", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
        LazyColumn {
            if (conversas.isEmpty()) {
                item {
                    Text(text = "Ainda não há conversas", style = MaterialTheme.typography.bodyMedium)
                }
            } else {
                conversas.forEach { conversa ->
                    item {
                        CardConversa()
                    }
                }
            }
        }
    }
}