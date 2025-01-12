package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.models.Usuario

private val mockUsers = listOf(
    Usuario("1", "Alice", "alice@example.com", "https://example.com/alice_profile.jpg"),
    Usuario("2", "Bob", "bob@example.com", "https://example.com/bob_profile.jpg"),
    Usuario("3", "Charlie", "charlie@example.com", "https://example.com/charlie_profile.jpg"),
    Usuario("4", "Diana", "diana@example.com", "https://example.com/diana_profile.jpg")
)

private val mockFriends = mutableStateListOf<Usuario>()

@Composable
fun FriendsScreen(navController: NavController) {
    var isViewingFriends by remember { mutableStateOf(true) }

    NavigationDrawer(navController) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                Button(
                    onClick = { isViewingFriends = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isViewingFriends) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Meus Amigos")
                }
                Button(
                    onClick = { isViewingFriends = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isViewingFriends) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Adicionar Amigos")
                }
            }

            if (isViewingFriends) {
                FriendsList(friends = mockFriends)
            } else {
                AddFriends(usuarios = mockUsers, friends = mockFriends)
            }
        }
    }
}

@Composable
fun FriendsList(friends: List<Usuario>) {
    if (friends.isEmpty()) {
        Text("Você ainda não adicionou nenhum amigo.", style = MaterialTheme.typography.bodyMedium)
    } else {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(friends) { friend ->
                FriendCard(user = friend)
            }
        }
    }
}

@Composable
fun AddFriends(usuarios: List<Usuario>, friends: MutableList<Usuario>) {
    var confirmationMessage by remember { mutableStateOf<String?>(null) }

    Column {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(usuarios) { user ->
                if (!friends.contains(user)) {
                    UserCard(
                        user = user,
                        onAddFriend = {
                            friends.add(it)
                            confirmationMessage = "${it.nome} foi adicionado como amigo!"
                        },
                        onRejectFriend = {
                            confirmationMessage = "Você rejeitou a solicitação de amizade de ${it.nome}."
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
fun FriendCard(user: Usuario) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = user.fotoPerfil,
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(user.nome ?: "Nome desconhecido", style = MaterialTheme.typography.bodyLarge)
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