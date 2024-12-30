package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookie.components.CardLivroVariante
import com.example.bookie.models.Livro
import com.example.bookie.ui.theme.BookieTheme

private data class TabItem(
    val text: String,
    val icon: ImageVector,
)

private val livro = Livro("", "Teste", "Autor Teste", "Livro de Teste")

@Composable
private fun Lidos() {
    val livros = listOf(livro)

    Row {
        livros.forEach { valor -> CardLivroVariante(valor, mostrarAvaliacao = true) }
    }
}

@Composable
private fun Lendo() {
    val livros = listOf(livro)

    Row {
        livros.forEach { valor -> CardLivroVariante(valor, mostrarPorcentagem = true) }
    }
}

@Composable
private fun QueroLer() {
    val livros = listOf(livro)

    Row {
        livros.forEach { valor -> CardLivroVariante(valor) }
    }
}

@Composable
private fun Favoritos() {
    val livros = listOf(livro)

    Row {
        livros.forEach { valor -> CardLivroVariante(valor, mostrarAvaliacao = true) }
    }
}

@Composable
fun MinhaEstante() {
    var text by remember { mutableStateOf("") }
    var tabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf(
        TabItem("lidos", Icons.Default.Check),
        TabItem("lendo", Icons.Outlined.AccountBox),
        TabItem("quero ler", Icons.Outlined.ShoppingCart),
        TabItem("favoritos", Icons.Outlined.FavoriteBorder)
    )

    Column {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text("Busque por livros na estante...") },
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
            modifier = Modifier.fillMaxWidth(),
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(text = "19.200", style = MaterialTheme.typography.titleLarge)
            Text(text = "páginas lidas", style = MaterialTheme.typography.bodyLarge)
        }

        Column(
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Card(
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                ),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                ),
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
            ) {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, item ->
                        Tab(text = { Text(item.text) },
                            selected = tabIndex == index,
                            icon = {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.text
                                )
                            },
                            onClick = { tabIndex = index }
                        )
                    }
                }
            }
            when (tabIndex) {
                0 -> Lidos()
                1 -> Lendo()
                2 -> QueroLer()
                3 -> Favoritos()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    BookieTheme {
        MinhaEstante()
    }
}