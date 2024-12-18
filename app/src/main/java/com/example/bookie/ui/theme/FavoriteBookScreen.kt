package com.example.bookie.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookie.R

data class Book(
    val title: String,
    val author: String,
    val description: String
)

val favoriteBooks = listOf(
    Book("1984", "George Orwell", "Um romance distópico sobre o totalitarismo."),
    Book("O Sol é Para Todos", "Harper Lee", "Uma história sobre injustiça racial."),
    Book("O Grande Gatsby", "F. Scott Fitzgerald", "Um romance sobre o sonho americano."),
    Book("Moby Dick", "Herman Melville", "A história da obsessão de um capitão por uma baleia branca."),
    Book("Orgulho e Preconceito", "Jane Austen", "Um romance romântico sobre boas maneiras e casamento.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteBookScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Livros Favoritos", fontSize = 20.sp) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(favoriteBooks) { book ->
                FavoriteBookCard(book = book)
            }
        }
    }
}

@Composable
fun FavoriteBookCard(book: Book) {
    var isBookmarked by remember { mutableStateOf(true) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Book Cover",
                modifier = Modifier
                    .size(56.dp)
                    .padding(end = 12.dp)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black
                )
                Text(
                    text = "Autor: ${book.author}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = book.description,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.DarkGray
                )
            }

            IconButton(onClick = { isBookmarked = !isBookmarked }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_bookmark_filled),
                    contentDescription = "Toggle Bookmark"
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FavoriteBookScreenPreview() {
    FavoriteBookScreen()
}