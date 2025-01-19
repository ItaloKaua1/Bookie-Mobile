package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.models.Usuario

private val mockFriends = mutableStateListOf<Usuario>()

@Composable
fun FriendsScreen(
    navController: NavController,
    mockFriends: SnapshotStateList<Usuario>
) {
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
                FriendsSolicitationScreen(
                    navController = TODO()
                )
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