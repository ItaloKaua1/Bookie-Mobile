package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.ClubeLeitura
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ClubesScreen(navController: NavHostController) {
    var clubes by remember { mutableStateOf<List<ClubeLeitura>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("clubes")
            .get()
            .addOnSuccessListener { result ->
                val listaClubes = result.documents.mapNotNull { doc ->
                    val clube = doc.toObject(ClubeLeitura::class.java)
                    clube
                }
                clubes = listaClubes
            }
    }


    LayoutVariant(navController, "Clubes", true) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Meus clubes do livro", style = MaterialTheme.typography.titleLarge)

            clubes.forEach { clube ->
                Text(
                    text = clube.nomeClube,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable {
                            clube.id?.let { id ->
                                navController.navigate("clube/$id")
                            }
                        }
                    ,
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Button(onClick = { navController.navigate("createClubScreen") }, modifier = Modifier.fillMaxWidth()) {
                Text("Criar clube +")
            }
        }
    }
}

@Composable
fun TelaClubeDetalhes(clubeId: String, navController: NavHostController) {
    var clube by remember { mutableStateOf<ClubeLeitura?>(null) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(clubeId) {
        db.collection("clubes").document(clubeId)
            .get()
            .addOnSuccessListener { document ->
                clube = document.toObject(ClubeLeitura::class.java)
            }
    }

    clube?.let {
        var tabIndex by remember { mutableStateOf(0) }
        val tabs = listOf("Informações Gerais", "Tópicos de Discussão")

        LayoutVariant(navController, "${it.nomeClube}", true) {
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Text(text = it.nomeClube, style = MaterialTheme.typography.titleLarge)

                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) }, selected = tabIndex == index, onClick = { tabIndex = index })
                    }
                }

                when (tabIndex) {
                    0 -> InformacoesGeraisClube(it)
                    1 -> TopicosDiscussao(
                        navController,
                        clubeId = clubeId
                    )
                }
            }
        }
    }
}


@Composable
fun InformacoesGeraisClube(clube: ClubeLeitura) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Livro do Mês: ${clube.Livro?.volumeInfo?.nome ?: "Nenhum livro selecionado"}", style = MaterialTheme.typography.bodyLarge)
            Text("Metas e Sprints: Defina suas metas!", style = MaterialTheme.typography.bodyLarge)
            Text("Regras de Convivência: ${clube.descricaoClube}", style = MaterialTheme.typography.bodyLarge)
        }
}

@Composable
fun TopicosDiscussao(navController: NavHostController, clubeId: String) {
    var topicos by remember { mutableStateOf<List<String>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(clubeId) {
        db.collection("clubes").document(clubeId)
            .collection("topicos")
            .get()
            .addOnSuccessListener { result ->
                topicos = result.documents.mapNotNull { it.getString("titulo") }
            }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        topicos.forEach { topico ->
            Text(
                text = topico,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .clickable { navController.navigate("discussao/$topico") },
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Button(
            onClick = { navController.navigate("criarTopico/$clubeId") },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Criar Novo Tópico")
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarTopicoScreen(clubeId: String, navController: NavHostController) {
    var novoTopico by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()
    Log.d("CriarTopicoScreen", "clubeId: $clubeId")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Criar Novo Tópico") }) }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            OutlinedTextField(
                value = novoTopico,
                onValueChange = { novoTopico = it },
                label = { Text("Qual é o Novo Tópico") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    if (novoTopico.isNotBlank()) {
                        db.collection("clubes").document(clubeId)
                            .collection("topicos")
                            .add(mapOf("titulo" to novoTopico))

                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
            ) {
                Text("Criar Tópico")
            }
        }
    }
}

