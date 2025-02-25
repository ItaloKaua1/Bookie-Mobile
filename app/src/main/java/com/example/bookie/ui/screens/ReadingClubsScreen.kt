package com.example.bookie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.bookie.R
import com.example.bookie.models.ClubeLeitura

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingClubsScreen(
    onCreateClub: () -> Unit,
    onClubClick: (ClubeLeitura) -> Unit,
    clubs: SnapshotStateList<ClubeLeitura>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Clubes de Leitura") },
                navigationIcon = {
                    IconButton(onClick = { /* Navegar para a tela anterior */ }) {
                        Icon(painterResource(id = R.drawable.ic_back), contentDescription = "Voltar")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateClub) {
                Icon(Icons.Default.Add, contentDescription = "Criar Clube")
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = { /* Atualizar estado de busca */ },
                    placeholder = { Text("Descubra mais clubes de leitura") },
                    leadingIcon = {
                        Icon(
                            painterResource(id = R.drawable.ic_search),
                            contentDescription = "Buscar"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                if (clubs.isEmpty()) {
                    Text(
                        "Você não está em nenhum clube de leitura.",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(clubs) { club ->
                            ReadingClubRow(club, onClubClick)
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun ReadingClubRow(club: ClubeLeitura, onClubClick: (ClubeLeitura) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onClubClick(club) }
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_placeholder),
            contentDescription = "Imagem do Clube",
            modifier = Modifier
                .size(48.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = club.nomeClube,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_forward),
            contentDescription = "Ver detalhes",
        )
    }
}
