package com.example.bookie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.ThematicList
import com.google.firebase.firestore.FirebaseFirestore

val db = FirebaseFirestore.getInstance()

@Composable
fun ThematicListDetailsScreen(
    navController: NavHostController,
    nome: String,
    descricao: String,
    quantidadeLivros: Int,
    id: String
) {
    var livros by remember { mutableStateOf<List<Livro>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    LaunchedEffect(id) {
        db.collection("listasTematicas")
            .document(id)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val lista = document.toObject(ThematicList::class.java)
                    livros = lista?.livros ?: emptyList()
                }
                isLoading = false
            }
            .addOnFailureListener {
                livros = emptyList()
                isLoading = false
            }
    }

    LayoutVariant(navController, "Detalhes da Lista") {
        ThematicListDetailsContent(
            nome = nome,
            descricao = descricao,
            quantidadeLivros = quantidadeLivros,
            livros = livros,
            isLoading = isLoading
        )
    }
}

@Composable
fun ThematicListDetailsContent(
    nome: String,
    descricao: String,
    quantidadeLivros: Int,
    livros: List<Livro>,
    isLoading: Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = nome, style = MaterialTheme.typography.titleLarge)
        Text(text = descricao, style = MaterialTheme.typography.bodyMedium)
        Text(text = "Livros (${livros.size})", style = MaterialTheme.typography.titleMedium)

        if (isLoading) {
            Text(text = "Carregando livros...", style = MaterialTheme.typography.bodyMedium)
        } else {
            LazyColumn {
                items(livros) { livro ->
                    cardLivro(livro)
                }
            }
        }
    }
}

@Composable
fun cardLivro(livro: Livro) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AsyncImage(
                model = livro.getCapa(),
                contentDescription = "Capa do livro",
                modifier = Modifier.size(64.dp)
            )
            Column {
                Text(
                    text = livro.volumeInfo?.nome ?: "Título desconhecido",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = livro.getAutor().ifEmpty { "Autor desconhecido" },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
