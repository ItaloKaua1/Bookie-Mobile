package com.example.bookie.ui.screens.CadastroScreens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun CadastroScreen3(navController: NavHostController) {
    val genres = listOf("Ação", "Comédia", "Drama", "Terror", "Romance", "Fantasia", "Ficção Científica")
    val selectedGenres = remember { mutableStateOf(setOf<String>()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Escolha seus gêneros favoritos", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Selecione no mínimo 3 gêneros para finalizar seu cadastro")
            Spacer(modifier = Modifier.height(16.dp))
            genres.forEach { genre ->
                Button(
                    onClick = {
                        selectedGenres.value = if (selectedGenres.value.contains(genre)) {
                            selectedGenres.value - genre
                        } else {
                            selectedGenres.value + genre
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedGenres.value.contains(genre)) Color.Blue else Color.Gray
                    )
                ) {
                    Text(genre)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    if (selectedGenres.value.size >= 3) {
                        navController.navigate("loginScreen")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Finalizar")
            }
        }
    }
}


