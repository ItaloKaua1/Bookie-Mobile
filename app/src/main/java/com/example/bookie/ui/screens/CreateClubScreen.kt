package com.example.bookie.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.ClubeLeitura
import com.example.bookie.models.Livro
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.saveable.rememberSaveable
import org.slf4j.MDC.put

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClubScreen(navController: NavHostController) {
    val context = LocalContext.current

    var nomeClubeLeitura by rememberSaveable { mutableStateOf("") }
    var descricaoClubeLeitura by rememberSaveable { mutableStateOf("") }
    var publico by rememberSaveable { mutableStateOf(true) }
    var livroSelecionado by rememberSaveable { mutableStateOf<Livro?>(null) }
    var metas by rememberSaveable { mutableStateOf("") }
    var etapa by rememberSaveable { mutableStateOf(1) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Criar Novo Clube") }) }
    ) { padding ->
        LayoutVariant(navController, "Criar Clube", true) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                when (etapa) {
                    1 -> {
                        OutlinedTextField(
                            value = nomeClubeLeitura,
                            onValueChange = { nomeClubeLeitura = it },
                            label = { Text("Nome do Clube") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Button(
                            onClick = { if (nomeClubeLeitura.isNotBlank()) etapa = 2 },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Continuar")
                        }
                    }
                    2 -> {
                        Text("Defina os detalhes do clube", style = MaterialTheme.typography.titleLarge)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Clube Público")
                            Switch(checked = publico, onCheckedChange = { publico = it })
                        }

                        OutlinedTextField(
                            value = descricaoClubeLeitura,
                            onValueChange = { descricaoClubeLeitura = it },
                            label = { Text("Descrição do Clube (opcional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = metas,
                            onValueChange = { metas = it },
                            label = { Text("Metas e Sprints (opcional)") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Button(
                            onClick = { etapa = 3 },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Selecionar Livro do Mês")
                        }
                    }
                    3 -> {
                        Button(
                            onClick = {
                                navController.currentBackStackEntry?.savedStateHandle?.set(
                                    "livroSelecionado", livroSelecionado
                                )
                                navController.navigate("selecionarLivroScreen")
                            },
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text("Selecionar Livro do Mês")
                        }

                        livroSelecionado?.let {
                            Text("Livro Selecionado: ${it.volumeInfo?.nome ?: "Nenhum livro selecionado"}", style = MaterialTheme.typography.bodyLarge)
                        }

                        Button(
                            onClick = {
                                if (livroSelecionado != null) {
                                    val club = ClubeLeitura(
                                        id = FirebaseFirestore.getInstance().collection("clubes").document().id,
                                        nomeClube = nomeClubeLeitura,
                                        descricaoClube = descricaoClubeLeitura,
                                        publico = publico,
                                        Livro = null,
                                        membros = mutableListOf()
                                    )
                                    val clubData = mutableMapOf(
                                        "id" to club.id,
                                        "nomeClube" to club.nomeClube,
                                        "descricaoClube" to club.descricaoClube,
                                        "publico" to club.publico,
                                        "Livro" to livroSelecionado?.let { livro ->
                                            mapOf(
                                                "id" to livro.id,
                                                "volumeInfo" to mapOf(
                                                    "nome" to livro.volumeInfo?.nome,
                                                    "autor" to livro.volumeInfo?.autor
                                                )
                                            )
                                        },
                                        "membros" to club.membros
                                    )

                                    FirebaseFirestore.getInstance().collection("clubes")
                                        .document(club.id).set(clubData)
                                    navController.popBackStack()
                                } else {
                                    Toast.makeText(context, "Nenhum livro selecionado", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.padding(top = 16.dp)
                        ) {
                            Text("Criar Clube")
                        }
                    }
                }
            }
        }
    }

    LaunchedEffect(navController.currentBackStackEntry?.savedStateHandle) {
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Livro>("livroSelecionado")
            ?.observeForever { livroRetornado ->
                if (livroRetornado != null) {
                    livroSelecionado = livroRetornado
                }
            }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelecionarLivroScreen(navController: NavHostController) {
    var livros by remember { mutableStateOf<List<Livro>>(emptyList()) }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("livros")
            .get()
            .addOnSuccessListener { result ->
                val listaLivros = result.documents.mapNotNull { it.toObject(Livro::class.java) }
                livros = listaLivros
            }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Selecionar Livro") }) }
    ) { padding ->
        LayoutVariant(navController, "Selecionar Livro", true) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                livros.forEach { livro ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable {
                                navController.previousBackStackEntry
                                    ?.savedStateHandle
                                    ?.set("livroSelecionado", livro)
                                navController.popBackStack()
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            livro.volumeInfo?.nome?.let { Text(text = it, style = MaterialTheme.typography.bodyLarge) }
                            Text(
                                text = "Autor: ${livro.volumeInfo?.autor?.joinToString() ?: "Autor desconhecido"}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }
    }
}
