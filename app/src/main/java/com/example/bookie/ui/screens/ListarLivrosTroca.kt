package com.example.bookie.ui.screens


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.components.CardLivro
import com.example.bookie.components.CardLivroTroca
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.TrocaDisponivel
import com.example.bookie.models.Usuario
import com.example.bookie.services.BooksRepositorio
import com.google.firebase.firestore.Filter
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@Composable
fun ListarLivrosTroca(navController: NavController, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
//    var livros: SnapshotStateList<Livro> = mutableStateListOf()
    var trocasDisponiveis by remember { mutableStateOf(listOf<TrocaDisponivel>()) }
    var minhasTrocasDisponiveis by remember { mutableStateOf(listOf<TrocaDisponivel>()) }
    val appData = AppData.getInstance()
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val userRepo = UserRepository(context)

    val itemClick = { troca: TrocaDisponivel -> navController.navigate("telaLivroTroca/${troca.document}")}

    LaunchedEffect(text) {
        launch {
            val userIdLocal = userRepo.currentUserId.first()

            db.collection("trocas_disponiveis")
                .whereNotEqualTo("usuario.id", userIdLocal)
                .whereEqualTo("finalizada", false)
                .get()
                .addOnSuccessListener { documents ->
                    val trocas: ArrayList<TrocaDisponivel> = arrayListOf()
                    for (document in documents) {
                        val troca = document.toObject(TrocaDisponivel::class.java)
                        troca.document = document.id
                        trocas.add(troca)
                    }
                    trocasDisponiveis = trocas
                    appData.setTrocasDisponiveis(trocas)
                }
        }
    }

    LaunchedEffect(text) {
        launch {
            val userIdLocal = userRepo.currentUserId.first()

            db.collection("trocas_disponiveis")
                .whereEqualTo("usuario.id", userIdLocal)
                .whereEqualTo("finalizada", false)
                .get()
                .addOnSuccessListener { documents ->
                    val trocas: ArrayList<TrocaDisponivel> = arrayListOf()
                    for (document in documents) {
                        val troca = document.toObject(TrocaDisponivel::class.java)
                        troca.document = document.id
                        trocas.add(troca)
                    }
                    minhasTrocasDisponiveis = trocas
                    appData.setMinhasTrocasDisponiveis(trocas)
                }
        }
    }


    LayoutVariant(navController, "Buscar Livros Disponíveis para Troca") {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Column (
                horizontalAlignment = Alignment.End,
                modifier = Modifier.fillMaxWidth(),
            ) {
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Digite sua busca aqui...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                    modifier = Modifier.fillMaxWidth(),
                )
                TextButton (onClick = {}) {
                    Icon(
                        modifier = Modifier.size(24.dp).fillMaxWidth(),
                        imageVector = Icons.Outlined.List,
                        contentDescription = "Minha Biblioteca",
                    )
                    Text(text = "Filtrar")
                }
            }


            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 22.dp, start = 16.dp, end = 16.dp, bottom = 4.dp).weight(1f)
            ) {
                if (minhasTrocasDisponiveis.isNotEmpty()) {
                    Text(text = "Meus Livros para Troca", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(8.dp))
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {


                        minhasTrocasDisponiveis.forEach { troca ->
                            item{
                                CardLivroTroca(troca, itemClick)
                            }
                        }


                    }
                    Spacer(Modifier.height(16.dp))
                }
                Text(text = "Trocas Disponíveis", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(8.dp))
                if (trocasDisponiveis.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {


                        trocasDisponiveis.forEach { troca ->
                            item{
                                CardLivroTroca(troca, itemClick)
                            }
                        }


                    }
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.empty_state),
                        contentDescription = stringResource(id = R.string.capa_livro),
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    BookieTheme {
//        ListarLivros()
//    }
//}
