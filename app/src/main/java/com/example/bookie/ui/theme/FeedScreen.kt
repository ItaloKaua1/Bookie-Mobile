package com.example.bookie.ui.theme

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookie.ui.theme.BookieTheme

data class FeedItem(
    val title: String,
    val author: String,
    val content: String
)

@Composable
fun FeedScreen(modifier: Modifier = Modifier) {
    val feedItems = listOf(
        FeedItem(
            title = "A revolução dos livros",
            author = "Maria",
            content = "Este livro discute a importância da leitura na sociedade moderna."
        ),
        FeedItem(
            title = "O futuro da IA na literatura",
            author = "Carlos",
            content = "Uma análise das interações entre inteligência artificial e literatura."
        ),
        FeedItem(
            title = "Clube do Livro de Janeiro",
            author = "Admin",
            content = "Participe do clube do livro e discuta sua obra favorita!"
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(feedItems) { item ->
            FeedCard(feedItem = item)
        }
    }
}

@Composable
fun FeedCard(feedItem: FeedItem) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = feedItem.title, style = androidx.compose.material3.MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Por: ${feedItem.author}", style = androidx.compose.material3.MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = feedItem.content, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeedScreenPreview() {
    BookieTheme {
        FeedScreen()
    }
}