package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.AppData
import com.example.bookie.components.CardLivroVariante
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.google.firebase.firestore.FirebaseFirestore

private data class TabItem(
    val text: String,
    val icon: ImageVector,
)

@Composable
private fun Todos(livros: List<Livro>, onClick: (Livro) -> Unit = {}) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(livros) { item ->
            CardLivroVariante(item, mostrarAvaliacao = true, onClick = onClick)
        }
    }
}

@Composable
private fun Lidos() {
    var livrosLidos by remember { mutableStateOf(listOf<Livro>()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("livros")
            .whereEqualTo("status", "lido")
            .get()
            .addOnSuccessListener { result ->
                livrosLidos = result.documents.mapNotNull { it.toObject(Livro::class.java) }
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(livrosLidos) { livro ->
            CardLivroVariante(livro, mostrarAvaliacao = true)
        }
    }
}

@Composable
private fun Lendo() {
    var livrosLendo by remember { mutableStateOf(listOf<Livro>()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("livros")
            .whereEqualTo("status", "lendo")
            .get()
            .addOnSuccessListener { result ->
                livrosLendo = result.documents.mapNotNull { it.toObject(Livro::class.java) }
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(livrosLendo) { livro ->
            CardLivroVariante(livro, mostrarPorcentagem = true)
        }
    }
}

@Composable
private fun QueroLer() {
    var livrosQueroLer by remember { mutableStateOf(listOf<Livro>()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("livros")
            .whereEqualTo("status", "quero ler")
            .get()
            .addOnSuccessListener { result ->
                livrosQueroLer = result.documents.mapNotNull { it.toObject(Livro::class.java) }
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(livrosQueroLer) { livro ->
            CardLivroVariante(livro)
        }
    }
}

@Composable
private fun Favoritos(livros: List<Livro>, onClick: (Livro) -> Unit = {}) {
    var livrosFavoritos by remember { mutableStateOf(listOf<Livro>()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("livros")
            .whereEqualTo("favorito", true)
            .get()
            .addOnSuccessListener { result ->
                livrosFavoritos = result.documents.mapNotNull { it.toObject(Livro::class.java) }
            }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(livrosFavoritos) { livro ->
            CardLivroVariante(livro, mostrarAvaliacao = true, onClick = onClick)
        }
    }
}

@Composable
fun MinhaEstante(navController: NavHostController) {
    var text by remember { mutableStateOf("") }
    var tabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf(
        TabItem("todos", Icons.Default.Menu),
        TabItem("lidos", Icons.Default.Check),
        TabItem("lendo", Icons.Outlined.AccountBox),
        TabItem("quero ler", Icons.Outlined.ShoppingCart),
        TabItem("favoritos", Icons.Outlined.FavoriteBorder)
    )
    val appData = AppData.getInstance()
    var livros by remember { mutableStateOf(listOf<Livro>()) }
    val db = FirebaseFirestore.getInstance()

    val itemClick = { livro: Livro -> navController.navigate("telaLivro/${livro.id}/${true}") }

    fun atualizarEstante() {
        db.collection("livros").get().addOnSuccessListener { documents ->
            val localLivros = documents.mapNotNull { doc ->
                doc.toObject(Livro::class.java).apply { document = doc.id }
            }
            livros = localLivros
            appData.setLivrosEstante(localLivros)
        }
    }

    LaunchedEffect(Unit) { atualizarEstante() }

    LayoutVariant(navController, "Minha estante") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Busque por livros na estante...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                modifier = Modifier.fillMaxWidth(),
            )

            Column(modifier = Modifier.padding(top = 24.dp)) {
                Text(text = "19.200", style = MaterialTheme.typography.titleLarge)
                Text(text = "páginas lidas", style = MaterialTheme.typography.bodyLarge)
            }

            Column(modifier = Modifier.padding(top = 24.dp)) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                ) {
                    TabRow(selectedTabIndex = tabIndex) {
                        tabs.forEachIndexed { index, item ->
                            Tab(
                                text = { Text(item.text) },
                                selected = tabIndex == index,
                                icon = { Icon(imageVector = item.icon, contentDescription = item.text) },
                                onClick = { tabIndex = index }
                            )
                        }
                    }
                }

                when (tabIndex) {
                    0 -> Todos(livros, itemClick)
                    1 -> Lidos()
                    2 -> Lendo()
                    3 -> QueroLer()
                    4 -> Favoritos(livros, itemClick)
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    BookieTheme {
//        MinhaEstante(navController)
//    }
//}