package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import com.example.bookie.models.ThematicList

@Composable
fun ThematicListsScreen(thematicLists: List<ThematicList>) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        if (thematicLists.isEmpty()) {
            Text(
                text = "Você ainda não possui nenhuma lista.",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(thematicLists) { list ->
                    ThematicListCard(list)
                }
            }
        }
    }
}

@Composable
fun ThematicListCard(list: ThematicList) {
    Card(
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(list.nome, style = MaterialTheme.typography.titleMedium)
            Text(list.descricao, style = MaterialTheme.typography.bodyMedium)
            Text(
                "${list.livros.size} livros adicionados",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
