package com.example.bookie.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.components.TopBar
import com.example.bookie.models.Livro
import com.example.bookie.models.VolumeInfo
import com.example.bookie.models.ImageLinks

private val mockBooks = listOf(
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
fun ClassifyBooksScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("Todos os livros") }
    val categories = listOf("Todos os livros", "Livros lidos", "Quero ler", "Lendo", "Favoritos")

    var filteredBooks by remember { mutableStateOf(mockBooks) }

    Scaffold(
        topBar = { TopBar { /* Ação da TopBar */ } }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                DropdownMenuComponent(
                    categories = categories,
                    selectedCategory = selectedCategory,
                    onCategorySelected = {
                        selectedCategory = it
                        filteredBooks = when (it) {
                            "Todos os livros" -> mockBooks
                            "Favoritos" -> mockBooks.filter { livro -> livro.id == "1" }
                            else -> mockBooks
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredBooks) { livro ->
                        CardLivro(
                            livro = livro,
                            category = selectedCategory,
                            onCardClick = { navController.navigate("bookDetail/${livro.id}") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DropdownMenuComponent(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            Text(text = selectedCategory)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { category ->
                DropdownMenuItem(
                    text = { Text(text = category) },
                    onClick = {
                        onCategorySelected(category)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun CardLivro(livro: Livro, category: String, onCardClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            livro.volumeInfo?.nome?.let { Text(text = it, style = MaterialTheme.typography.titleMedium) }
            Text(
                text = "Autor: ${livro.volumeInfo?.autor?.joinToString(", ")}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Categoria: $category",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}