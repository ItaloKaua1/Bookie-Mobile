package com.example.bookie.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.components.CardLivro
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.services.BooksRepositorio


private suspend fun getLivros(text: String, livros: List<Livro>): List<Livro> {
    if (text.length >= 3) {
        val apiService = BooksRepositorio()


        val parsedText = text.split(" ").joinToString("+")
        val response = apiService.buscarLivros(parsedText)


        return response.items.map { item -> item }
    } else if (text.length in 1..2) {
        return livros
    }


    return listOf();
}


@Composable
fun ListarLivros(navController: NavController, modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }
//    var livros: SnapshotStateList<Livro> = mutableStateListOf()
    var livros by remember { mutableStateOf(listOf<Livro>()) }
    val appData = AppData.getInstance()


    val itemClick = { livro: Livro -> navController.navigate("telaLivro/${livro.id}/${false}")}


    LaunchedEffect(text) {
        livros = getLivros(text, livros)
        appData.setLivrosBusca(livros)
    }


    LayoutVariant(navController, "Buscar Livros") {
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
                if (livros.isNotEmpty()) {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth(),
                    ) {


                        livros.forEach { livro ->
                            item{
                                CardLivro(livro, itemClick)
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
