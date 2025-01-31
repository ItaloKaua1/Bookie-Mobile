package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookie.models.ClubeLeitura
import com.example.bookie.models.Livro
import com.example.bookie.models.VolumeInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClubScreen(onClubCreated: (ClubeLeitura) -> Unit) {
    var nomeClubeLeitura by remember { mutableStateOf("") }
    var descricaoClubeLeitura by remember { mutableStateOf("") }
    var publico by remember { mutableStateOf(true) }
    var livroSelecionado by remember { mutableStateOf<Livro?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Criar Novo Clube") }) },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                OutlinedTextField(
                    value = nomeClubeLeitura,
                    onValueChange = { nomeClubeLeitura = it },
                    label = { Text("Nome do Clube") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                OutlinedTextField(
                    value = descricaoClubeLeitura,
                    onValueChange = { descricaoClubeLeitura = it },
                    label = { Text("Descrição do Clube") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Público")
                    Switch(checked = publico, onCheckedChange = { publico = it })
                }
                Button(
                    onClick = {
                        livroSelecionado = Livro(
                            id = "1",
                            volumeInfo = VolumeInfo(
                                nome = "Livro Exemplo",
                                autor = listOf("Autor Exemplo"),
                                imageLinks = null
                            )
                        )
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Selecionar Livro")
                }
                Button(
                    onClick = {
                        if (nomeClubeLeitura.isNotBlank() && livroSelecionado != null) {
                            val club = ClubeLeitura(
                                id = "1",
                                nomeClube = nomeClubeLeitura,
                                descricaoClube = descricaoClubeLeitura,
                                publico = publico,
                                Livro = livroSelecionado!!,
                                membros = mutableListOf()
                            )
                            onClubCreated(club)
                        }
                    },
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text("Criar Clube")
                }
            }
        }
    )
}
