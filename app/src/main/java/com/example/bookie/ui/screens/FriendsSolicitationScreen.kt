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
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FriendsSolicitationScreen(navController: NavController) {
    var confirmationMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val currentUserId = auth.currentUser?.uid ?: ""
    val friends = remember { mutableStateListOf<Usuario>() }
    val users = remember { mutableStateListOf<Usuario>() }

    LaunchedEffect(Unit) {
        db.collection("usuarios")
            .get()
            .addOnSuccessListener { result ->
                val userList = result.documents.mapNotNull { it.toObject(Usuario::class.java) }
                    .filter { it.id != currentUserId }
                users.addAll(userList)
                isLoading = false
            }
            .addOnFailureListener { e ->
                Log.e("FriendsSolicitation", "Erro ao carregar usuários", e)
                isLoading = false
            }
    }

    LayoutVariant(navController, "Solicitações de Amigos") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(users) { user ->
                        if (friends.none { it.id == user.id }) {
                            UserCard(
                                user = user,
                                onAddFriend = { addFriend(user, friends, db, currentUserId) { confirmationMessage = it } },
                                onRejectFriend = { confirmationMessage = "Você rejeitou a solicitação de ${user.nome}." }
                            )
                        }
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
}

fun addFriend(user: Usuario, friends: MutableList<Usuario>, db: FirebaseFirestore, currentUserId: String, onResult: (String) -> Unit) {
    val friendRef = db.collection("usuarios").document(currentUserId).collection("amigos").document(user.id ?: "")

    friendRef.set(
        mapOf(
            "id" to (user.id ?: ""),
            "nome" to (user.nome ?: "Usuário Desconhecido"),
            "email" to (user.email ?: "")
        )
    )
        .addOnSuccessListener {
            Log.d("FriendsSolicitation", "Amigo adicionado: ${user.nome}")
            friends.add(user)
            onResult("${user.nome} foi adicionado como amigo!")
        }
        .addOnFailureListener { e ->
            Log.e("FriendsSolicitation", "Erro ao adicionar amigo", e)
            onResult("Erro ao adicionar ${user.nome}. Tente novamente.")
        }
}


@Composable
fun UserCard(user: Usuario, onAddFriend: () -> Unit, onRejectFriend: () -> Unit) {
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
                Button(onClick = onAddFriend) { Text("Adicionar") }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onRejectFriend) { Text("Rejeitar") }
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
            TextButton(onClick = onDismiss) { Text("OK") }
        }
    )
}
