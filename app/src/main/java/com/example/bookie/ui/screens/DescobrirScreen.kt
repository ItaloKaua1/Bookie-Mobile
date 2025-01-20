package com.example.bookie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookie.R
import com.example.bookie.components.LayoutVariant
import com.example.bookie.ui.theme.quaternary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DescobrirScreen(navController: NavController) {
    LayoutVariant(navController, "Descobrir", false) {

        Column(modifier = Modifier.fillMaxSize()) {

            // Imagem fixa no topo
            Image(
                painter = painterResource(id = R.drawable.capa_descobrir),
                contentDescription = "Imagem Capa Descobrir",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp), // Ajuste o tamanho conforme necessário
                    alignment = Alignment.TopStart
            )

            // Conteúdo rolável
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Encontre o livro perfeito pra você!",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.quaternary,
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Resto do conteúdo
                    Column {
                        Text("Qual seu gênero literário favorito?")
                        Spacer(modifier = Modifier.height(8.dp))

                        // Dropdown Menu Example
                        var genreExpanded by remember { mutableStateOf(false) }
                        var selectedGenre by remember { mutableStateOf("Selecione um gênero") }
                        val genreOptions = listOf(
                            "Fantasia",
                            "Ficção Científica",
                            "Romance",
                            "Jovem Adulto",
                            "Terror/Thriller",
                            "Gibis",
                            "Não-Ficção",
                            "Poesias",
                            "Biografias",
                            "Auto-Ajuda",
                            "Desenvolvimento Pessoal",
                            "Conto",
                            "Novela",
                            "Infantil",
                            "Horror",
                            "Gastronomia",
                            "Ação",
                            "Religião e Espiritualidade",
                            "Mangá",
                            "Tecnologia/Ciência",
                            "Viagem",
                            "História",
                            "Ficção Policial"
                        )

                        ExposedDropdownMenuBox(
                            expanded = genreExpanded,
                            onExpandedChange = { genreExpanded = !genreExpanded }
                        ) {
                            TextField(
                                value = selectedGenre,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Gênero literário") },
                                trailingIcon = {
                                    Icon(
                                        imageVector = if (genreExpanded) Icons.Default.ArrowDropUp else Icons.Default.ArrowDropDown,
                                        contentDescription = null
                                    )
                                },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = genreExpanded,
                                onDismissRequest = { genreExpanded = false }
                            ) {
                                genreOptions.forEach { genre ->
                                    DropdownMenuItem(
                                        text = { Text(genre) },
                                        onClick = {
                                            selectedGenre = genre
                                            genreExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Qual seu tipo de protagonista favorito?")
                        val protagonistOptions = listOf(
                            "Herói",
                            "Anti-herói",
                            "Personagem feminina forte",
                            "Protagonista adolescente"
                        )
                        var selectedProtagonist by remember { mutableStateOf(protagonistOptions[0]) }

                        protagonistOptions.forEach { protagonist ->
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = (selectedProtagonist == protagonist),
                                    onClick = { selectedProtagonist = protagonist }
                                )
                                Text(
                                    text = protagonist,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text("Você prefere livros de quais tamanhos?")
                        val tamanhoOptions = listOf(
                            "Curtos (menos de 200 páginas)",
                            "Médios (200-400 páginas)",
                            "Longos (mais de 400 páginas)"
                        )
                        var selectedTamanho by remember { mutableStateOf(tamanhoOptions[0]) }

                        tamanhoOptions.forEach { tamanho ->
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = (selectedTamanho == tamanho),
                                    onClick = { selectedTamanho = tamanho }
                                )
                                Text(
                                    text = tamanho,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }

                        Text("Você gosta de séries de livros ou prefere histórias únicas?")
                        val gostoOptions =
                            listOf("Prefiro histórias únicas)", "Prefiro séries", "Tanto faz")
                        var selectedGosto by remember { mutableStateOf(gostoOptions[0]) }

                        gostoOptions.forEach { gosto ->
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = (selectedGosto == gosto),
                                    onClick = { selectedGosto = gosto }
                                )
                                Text(
                                    text = gosto,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }

                        Text("Qual ritmo de narrativa você prefere?")
                        val ritmoOptions = listOf(
                            "Rápido, com muita ação",
                            "Moderado, equilibrado entre ação e descrição",
                            "Lento, com muitos detalhes e profundidade"
                        )
                        var selectedRitmo by remember { mutableStateOf(ritmoOptions[0]) }

                        ritmoOptions.forEach { ritmo ->
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = (selectedRitmo == ritmo),
                                    onClick = { selectedRitmo = ritmo }
                                )
                                Text(
                                    text = ritmo,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }

                        Text("Qual tema principal mais te atrai em uma história ou narrativa?")
                        val temaOptions = listOf(
                            "Amor e relacionamentos",
                            "Superação de desafios",
                            "Exploração de dilemas morais",
                            "Aventura e autodescoberta",
                            "Mistério e investigação"
                        )
                        var selectedTema by remember { mutableStateOf(temaOptions[0]) }

                        temaOptions.forEach { tema ->
                            Row(
                                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                RadioButton(
                                    selected = (selectedTema == tema),
                                    onClick = { selectedTema = tema }
                                )
                                Text(
                                    text = tema,
                                    style = MaterialTheme.typography.bodyLarge,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
