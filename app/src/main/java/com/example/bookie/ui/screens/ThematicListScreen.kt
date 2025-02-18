package com.example.bookie.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.models.ThematicList
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ThematicListsScreen(navController: NavHostController, thematicLists: List<ThematicList>) {
    val db = FirebaseFirestore.getInstance()
    var thematicLists by remember { mutableStateOf<List<ThematicList>>(emptyList()) }

    LaunchedEffect(Unit) {
        db.collection("listasTematicas").addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) return@addSnapshotListener

            val listasCarregadas = snapshot.documents.mapNotNull { it.toObject(ThematicList::class.java) }
            thematicLists = listasCarregadas
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Text(
                text = "Minhas Listas Temáticas",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (thematicLists.isEmpty()) {
            item {
                Text(
                    text = "Você ainda não possui nenhuma lista.",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            items(thematicLists) { list ->
                ThematicListCard(navController, list)
            }
        }

        item {
            Button(
                onClick = { navController.navigate("criarLista") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Criar Nova Lista +")
            }
        }
    }
}


@Composable
fun ThematicListCard(navController: NavHostController, list: ThematicList) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .clickable {
                // Passa a lista completa de livros para a tela de detalhes
                navController.navigate("detalhesListas/${list.id}/${list.nome}/${list.descricao}/${list.livros.size}")
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = list.nome,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = list.descricao,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = 1
            )
            Text(
                text = "${list.livros.size} livros adicionados",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

