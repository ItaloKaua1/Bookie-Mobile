package com.example.bookie.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.Livro
import com.example.bookie.models.ThematicList
import com.google.firebase.firestore.FirebaseFirestore
import android.net.Uri

@Composable
fun CriarListaScreen(navController: NavHostController) {
    var nome by remember { mutableStateOf("") }
    var descricao by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    LayoutVariant(navController, "Criar Lista") {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome da Lista*") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )

            OutlinedTextField(
                value = descricao,
                onValueChange = { descricao = it },
                label = { Text("Descrição (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done)
            )

            Button(
                onClick = {
                    if (nome.isNotBlank()) {
                        val nome = Uri.encode(nome)
                        val descricao = Uri.encode(descricao)

                        navController.navigate("adicionarLivrosScreen/$nome/$descricao")
                    } else {
                        errorMessage = "Por favor, insira o nome da lista."
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Adicionar Livros à Lista")
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
