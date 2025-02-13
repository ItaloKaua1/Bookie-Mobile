package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.ThematicList

@Composable
fun ThematicListDetailsScreen(
    navController: NavHostController,
    nome: String,
    descricao: String,
    quantidadeLivros: Int
) {
    LayoutVariant(navController, "Detalhes da Lista") {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(text = nome, style = MaterialTheme.typography.titleLarge)
            Text(text = descricao, style = MaterialTheme.typography.bodyMedium)
            Text(text = "Livros ($quantidadeLivros)", style = MaterialTheme.typography.titleMedium)
        }
    }
}


@Composable
fun BookCard(livro: Livro) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val bookName = livro.volumeInfo?.nome ?: "Título indisponível"
            Text(
                text = bookName,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
        }
    }
}
