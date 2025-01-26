package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.models.Usuario

private val mockUsers = listOf(
    Usuario("1", "Alice", "alice@example.com", "https://cdn-icons-png.flaticon.com/512/1373/1373255.png"),
    Usuario("2", "Bob", "bob@example.com", "https://cdn-icons-png.flaticon.com/512/3135/3135768.png"),
    Usuario("3", "Charlie", "charlie@example.com", "https://cdn-icons-png.flaticon.com/512/2920/2920072.png"),
    Usuario("4", "Diana", "diana@example.com", "https://cdn-icons-png.flaticon.com/512/3106/3106921.png")
)

@Composable
fun FriendsSolicitationScreen(
    navController: NavController
) {
    var confirmationMessage by remember { mutableStateOf<String?>(null) }
    val friends = remember { mutableStateListOf<Usuario>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(mockUsers) { user ->
                if (friends.none { it.id == user.id }) {
                    UserCard(
                        user = user,
                        onAddFriend = {
                            Log.d("FriendsSolicitation", "Amigo adicionado: ${it.nome}")
                            friends.add(it)
                            confirmationMessage = "${it.nome ?: "Desconhecido"} foi adicionado como amigo!"
                        },
                        onRejectFriend = {
                            Log.d("FriendsSolicitation", "Amigo rejeitado: ${it.nome}")
                            confirmationMessage = "Você rejeitou a solicitação de amizade de ${it.nome ?: "Desconhecido"}."
                        }
                    )
                }
            }
        }

        confirmationMessage?.let { message ->
            showConfirmationDialog(
                message = message,
                onDismiss = { confirmationMessage = null }
            )
        }
    }
}

@Composable
fun UserCard(user: Usuario, onAddFriend: (Usuario) -> Unit, onRejectFriend: (Usuario) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(user.nome ?: "Nome desconhecido", style = MaterialTheme.typography.bodyLarge)
            Row {
                Button(onClick = { onAddFriend(user) }) {
                    Text("Adicionar")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { onRejectFriend(user) }) {
                    Text("Rejeitar")
                }
            }
        }
    }
}

@Composable
fun showConfirmationDialog(message: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Confirmação") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
}