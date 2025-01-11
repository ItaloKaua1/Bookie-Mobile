package com.example.bookie.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


data class Book(
    val title: String,
    val author: String,
    val description: String
)

val favoriteBooks = listOf(
    Book("1984", "George Orwell", "A dystopian novel about totalitarianism."),
    Book("To Kill a Mockingbird", "Harper Lee", "A story about racial injustice in the Deep South."),
    Book("The Great Gatsby", "F. Scott Fitzgerald", "A novel about the American Dream."),
    Book("Moby Dick", "Herman Melville", "The tale of a captain's obsession with a white whale.")
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteBookScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favorite Books") }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            modifier = Modifier.padding(padding)
        ) {
            items(favoriteBooks) { book ->
                FavoriteBookItem(book)
            }
        }
    }
}


@Composable
fun FavoriteBookItem(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "By ${book.author}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = book.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun FavoriteBookScreenPreview() {
    FavoriteBookScreen()
}