package com.example.bookie.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.bookie.components.LayoutVariant
import com.example.bookie.services.getApiKey
import com.example.bookie.ui.theme.quaternary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

@Composable
fun ResultadosDescScreen(navController: NavController, query: String, context: Context) {
    var books by remember { mutableStateOf<List<Book>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(query) {
        books = fetchBooks(context, query)
        isLoading = false
    }

    LayoutVariant(navController, "Descobrir", false) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                "Melhores resultados para sua busca:",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.quaternary,
                    fontWeight = FontWeight.Bold
                )
            )

            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn {
                    items(books) { book ->
                        BookItem(book)
                    }
                }
            }
        }
    }
}

data class Book(
    val title: String,
    val author: String,
    val thumbnail: String?
)

suspend fun fetchBooks(context: Context, query: String): List<Book> {
    return withContext(Dispatchers.IO) {
        try {
            val apiKey = getApiKey(context)
            val url = "https://www.googleapis.com/books/v1/volumes?q=$query&key=$apiKey"
            val response = URL(url).readText()
            val json = JSONObject(response)
            val items = json.optJSONArray("items") ?: return@withContext emptyList()

            (0 until items.length()).mapNotNull { index ->
                val item = items.optJSONObject(index)
                val volumeInfo = item?.optJSONObject("volumeInfo") ?: return@mapNotNull null
                Book(
                    title = volumeInfo.optString("title", "Sem título"),
                    author = volumeInfo.optJSONArray("authors")?.optString(0) ?: "Desconhecido",
                    thumbnail = volumeInfo.optJSONObject("imageLinks")?.optString("thumbnail")
                )
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

@Composable
fun BookItem(book: Book) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            book.thumbnail?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = book.title,
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
            }
            Column {
                Text(book.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(book.author, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}
