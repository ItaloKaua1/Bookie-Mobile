package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.ThematicList
import com.google.firebase.firestore.FirebaseFirestore

private fun onCreateList(newList: ThematicList) {

}

@Composable
fun CriarListaScreen(navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var livros by remember { mutableStateOf(listOf<Any>()) }
    var errorMessage by remember { mutableStateOf("") }
    val db = FirebaseFirestore.getInstance()

    LaunchedEffect(Unit) {
        db.collection("livros").get().addOnSuccessListener { documents ->
            val localLivros: ArrayList<Livro> = arrayListOf()
            for (document in documents) {
                Log.e("dados", "${document.id} -> ${document.data}")
                val localLivro = document.toObject(Livro::class.java)
                localLivro.document = document.id
                localLivros.add(localLivro)
            }
            livros = localLivros.toList()
        }
    }

    LayoutVariant(navController, "Criar Lista"){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Criar Nova Lista",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da Lista") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = {
                    livros = livros + "Livro Exemplo ${livros.size + 1}"
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Livro")
            }

            Column {
                livros.forEachIndexed { index, livro ->
                    Text(text = "Livro $index: $livro", style = MaterialTheme.typography.bodyMedium)
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Button(
                onClick = {
                    if (nome.isNotBlank() && descricao.isNotBlank()) {
                        // Criar nova lista
                        val newList = ThematicList(
                            id = System.currentTimeMillis().toString(),
                            nome = nome,
                            descricao = descricao,
                            livros = livros
                        )
                        onCreateList(newList)
                        navController.popBackStack()
                    } else {
                        errorMessage = "Por favor, preencha todos os campos."
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Criar Lista")
            }
        }
    }
}


