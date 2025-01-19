package com.example.bookie.ui.screens.CadastroScreens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.bookie.components.BackComponent
import com.example.bookie.ui.theme.Purple40
import com.example.bookie.ui.theme.Purple80
import com.example.bookie.ui.theme.PurpleBookie

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CadastroScreen3(navController: NavHostController) {
    val genres = listOf(
        "fantasia", "ficção científica", "romance", "jovem adulto", "terror/thriller", "gibis",
        "não-ficção", "poesias", "biografias", "auto-ajuda", "desenvolvimento pessoal", "conto",
        "novela", "infantil", "horror", "gastronomia", "ação", "religião e espiritualidade", "mangá",
        "tecnologia/ciência", "viagem", "história", "ficção policial"
    )
    val selectedGenres = remember { mutableStateOf(setOf<String>()) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            BackComponent(navController = navController, title = "Cadastre-se")
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ){
                Text(
                    text = "Escolha seus gêneros favoritos",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = Purple40
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ){
                Text(
                    text = "Selecione no mínimo 3 gêneros para finalizar seu cadastro",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 2.dp)
            ){
                FlowRow(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalArrangement = Arrangement.Center
                ) {
                    genres.forEach { genre ->
                        val isSelected = selectedGenres.value.contains(genre)
                        val borderColor = if (isSelected) Color.Gray else PurpleBookie
                        val textColor = if (isSelected) Color.White else PurpleBookie
                        val backgroundColor = if (isSelected) PurpleBookie else Color.White

                        Button(
                            onClick = {
                                selectedGenres.value = if (isSelected) {
                                    selectedGenres.value - genre
                                } else {
                                    selectedGenres.value + genre
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = backgroundColor
                            ),
                            shape = RoundedCornerShape(4.dp),
                            border = BorderStroke(2.dp, borderColor),
                            modifier = Modifier.padding(horizontal = 2.dp)
                        ) {
                            Text(
                                text = genre,
                                maxLines = 1,
                                softWrap = true,
                                color = textColor,
                                modifier = Modifier.padding(horizontal = 2.dp)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ){
                Button(
                    onClick = { navController.navigate("loginScreen") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(56.dp)
                        .padding(bottom = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PurpleBookie
                    ),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = "Finalizar",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }

        }

        // Seção “Já possui cadastro?”
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                ClickableText(
                    text = buildAnnotatedString {
                        append("Você já possui cadastro? ")
                        pushStringAnnotation(tag = "Faça login", annotation = "loginScreen")
                        withStyle(
                            style = SpanStyle(
                                color = Purple40,
                                textDecoration = TextDecoration.Underline,
                                fontSize = 16.sp
                            )
                        ) {
                            append("Faça login")
                        }
                        pop()
                    },
                    onClick = { offset ->
                        navController.navigate("loginScreen")
                    }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
