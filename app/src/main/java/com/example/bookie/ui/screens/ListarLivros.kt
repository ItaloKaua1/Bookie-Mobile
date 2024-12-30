package com.example.bookie.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.magnifier
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.bookie.R
import com.example.bookie.Screen
import com.example.bookie.components.BottomBar
import com.example.bookie.components.CardLivro
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.models.Livro
import com.example.bookie.ui.theme.BookieTheme

val livros: List<Livro> = listOf(
//    Livro("capa", "Livro de Teste", "Autor Teste", "Sinopse Teste"),
//    Livro("capa", "Livro de Teste 2", "Autor Teste 2", "Sinopse Teste 2")
)

@Composable
fun ListarLivros(modifier: Modifier = Modifier) {
    var text by remember { mutableStateOf("") }

    Column {
        Column (
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier.size(24.dp).fillMaxWidth(),
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Voltar",
                    )
                }
                OutlinedTextField(
                    value = text,
                    onValueChange = { text = it },
                    placeholder = { Text("Digite sua busca aqui...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            TextButton (onClick = {}) {
                Icon(
                    modifier = Modifier.size(24.dp).fillMaxWidth(),
                    imageVector = Icons.Outlined.List,
                    contentDescription = "Minha Biblioteca",
                )
                Text(text = "Filtrar")
            }
        }

        if (livros.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
            ) {

                livros.forEach { livro ->
                    item {
                        CardLivro(livro, modifier = Modifier.fillMaxWidth())
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

@Preview(showBackground = true)
@Composable
private fun GreetingPreview() {
    BookieTheme {
        ListarLivros()
    }
}