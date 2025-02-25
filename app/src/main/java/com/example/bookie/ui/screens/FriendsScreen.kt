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
import coil3.compose.AsyncImage
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.models.Usuario
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FriendsScreen(navController: NavController) {
    var amigos by remember { mutableStateOf<List<Usuario>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var visualizandoAmigos by remember { mutableStateOf(true) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                val listaAmigos = result.documents.mapNotNull { it.toObject(Usuario::class.java) }
                amigos = listaAmigos
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.e("FriendsScreen", "Erro ao carregar amigos", e)
                isLoading = false
            }
    }

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
                    onClick = { visualizandoAmigos = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (visualizandoAmigos) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Meus Amigos")
                }
                Button(
                    onClick = { visualizandoAmigos = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!visualizandoAmigos) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Text("Adicionar Amigos")
                }
            }

            if (isLoading) {
                CircularProgressIndicator()
            } else {
                if (visualizandoAmigos) {
                    FriendsList(friends = amigos)
                } else {
                    FriendsSolicitationScreen(navController)
                }
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
            Spacer(modifier = Modifier.width(16.dp))
            Text(user.nome ?: "Nome desconhecido", style = MaterialTheme.typography.bodyLarge)
        }
    }
}
