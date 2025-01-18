package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.models.Livro
import com.example.bookie.models.VolumeInfo
import com.example.bookie.models.ImageLinks

private val mockClassifyBooks = listOf(
    Livro(
        id = "1",
        volumeInfo = VolumeInfo(
            nome = "O Nome do Vento",
            autor = listOf("Patrick Rothfuss"),
            sinopse = "Uma jornada épica cheia de mistério e magia.",
            paginas = 662,
            imageLinks = ImageLinks(
                smallThumbnail = "https://example.com/small_thumbnail1.jpg",
                thumbnail = "https://example.com/thumbnail1.jpg"
            )
        )
    ),
    Livro(
        id = "2",
        volumeInfo = VolumeInfo(
            nome = "A Guerra dos Tronos",
            autor = listOf("George R. R. Martin"),
            sinopse = "Uma história envolvente de poder, guerra e intrigas.",
            paginas = 835,
            imageLinks = ImageLinks(
                smallThumbnail = "https://example.com/small_thumbnail2.jpg",
                thumbnail = "https://example.com/thumbnail2.jpg"
            )
        )
    )
)

@Composable
fun ClassifyBookScreen(navController: NavController) {
    NavigationDrawer(navController) {
        Box(modifier = Modifier.padding()) {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = "Meus Livros Favoritos",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                )
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    items(mockClassifyBooks) { livro ->
                        FavoriteBookCard(livro = livro)
                    }
                }
            }
        }
    }
}

@Composable
fun FavoriteBookCard(livro: Livro) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = livro.volumeInfo?.nome ?: "Título Desconhecido",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Autor: ${livro.volumeInfo?.autor?.joinToString() ?: "Autor Desconhecido"}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = livro.volumeInfo?.sinopse ?: "Sinopse não disponível",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}