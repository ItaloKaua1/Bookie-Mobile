package com.example.bookie.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.ThematicList
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AdicionarLivrosScreen(navController: NavHostController, nomeLista: String, descricaoLista: String) {
    val context = LocalContext.current
    val db = FirebaseFirestore.getInstance()
    var livros by remember { mutableStateOf<List<Livro>>(emptyList()) }
    var livrosSelecionados by remember { mutableStateOf(mutableSetOf<Livro>()) }

    LaunchedEffect(Unit) {
        db.collection("livros").get().addOnSuccessListener { result ->
            val livrosCarregados = result.documents.mapNotNull { it.toObject(Livro::class.java)?.apply { document = it.id } }
            livros = livrosCarregados
        }
    }

    LayoutVariant(navController, "Adicionar Livros à Lista") {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            LazyColumn {
                items(livros) { livro ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                    ) {
                        AsyncImage(
                            model = livro.getCapa(),
                            contentDescription = "Capa do livro",
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = livro.volumeInfo?.nome ?: "Sem título", modifier = Modifier.weight(1f))

                        Checkbox(
                            checked = livrosSelecionados.contains(livro),
                            onCheckedChange = { isChecked ->
                                livrosSelecionados = livrosSelecionados.toMutableSet().apply {
                                    if (isChecked) add(livro) else remove(livro)
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                if (livrosSelecionados.isEmpty()) {
                    Toast.makeText(context, "Selecione ao menos um livro!", Toast.LENGTH_SHORT).show()
                    return@Button
                }

                val novaLista = ThematicList(
                    id = System.currentTimeMillis().toString(),
                    nome = nomeLista,
                    descricao = descricaoLista,
                    livros = livrosSelecionados.toList()
                )

                db.collection("listasTematicas").document(novaLista.id)
                    .set(novaLista)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Lista salva com sucesso!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack("telaPerfil", false)
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Erro ao salvar lista", Toast.LENGTH_SHORT).show()
                    }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Salvar Lista")
            }

        }
    }
}


@Composable
fun LivroItem(livro: Livro, livrosSelecionados: MutableList<Livro>) {
    val isSelected = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .clickable {
                isSelected.value = !isSelected.value
                if (isSelected.value) livrosSelecionados.add(livro)
                else livrosSelecionados.remove(livro)
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(livro.volumeInfo?.imageLinks?.thumbnail),
                contentDescription = livro.volumeInfo?.nome,
                modifier = Modifier.size(80.dp)
            )
            Text(
                text = livro.volumeInfo?.nome ?: "Título desconhecido",
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2
            )
        }
    }
}
