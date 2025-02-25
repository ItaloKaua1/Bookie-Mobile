//package com.example.bookie.ui.screens
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import com.example.bookie.components.LayoutVariant
//import com.example.bookie.models.Livro
//import com.google.firebase.firestore.FirebaseFirestore
//import com.patrykandpatrick.vico.compose.chart.Chart
//import com.patrykandpatrick.vico.compose.chart.entry.rememberChartEntryModelProducer
//import com.patrykandpatrick.vico.core.entry.entryOf
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun RelatorioLeituraScreen(navController: NavHostController) {
//    var livrosLidos by remember { mutableStateOf(0) }
//    var generosMaisLidos by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
//    var autoresMaisLidos by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
//
//    val db = FirebaseFirestore.getInstance()
//
//    LaunchedEffect(Unit) {
//        db.collection("livros")
//            .whereEqualTo("status", "lido")
//            .addSnapshotListener { snapshot, _ ->
//                snapshot?.let {
//                    val livros = it.documents.mapNotNull { doc -> doc.toObject(Livro::class.java) }
//                    livrosLidos = livros.size
//
//                    generosMaisLidos = livros
//                        .flatMap { listOf(it.volumeInfo?.sinopse ?: "Desconhecido") }
//                        .groupingBy { it }
//                        .eachCount()
//
//                    autoresMaisLidos = livros
//                        .flatMap { it.volumeInfo?.autor ?: emptyList() }
//                        .groupingBy { it }
//                        .eachCount()
//                }
//            }
//    }
//
//    val generosChart = rememberChartEntryModelProducer {
//        generosMaisLidos.mapIndexed { index, (genero, count) -> entryOf(index.toFloat(), count.toFloat()) }
//    }
//
//    val autoresChart = rememberChartEntryModelProducer {
//        autoresMaisLidos.mapIndexed { index, (autor, count) -> entryOf(index.toFloat(), count.toFloat()) }
//    }
//
//    Scaffold(
//        topBar = { TopAppBar(title = { Text("Relatórios de Leitura") }) }
//    ) { padding ->
//        LayoutVariant(navController, "Relatórios de Leitura", true) {
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(padding)
//                    .padding(16.dp)
//            ) {
//                Text(text = "📖 Livros Lidos: $livrosLidos", style = MaterialTheme.typography.titleLarge)
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(text = "📊 Gêneros Mais Lidos", style = MaterialTheme.typography.titleMedium)
//                Chart(
//                    chart = com.patrykandpatrick.vico.compose.chart.column.columnChart(),
//                    model = generosChart.getModel(),
//                    modifier = Modifier.height(200.dp),
//                    isHorizontalScrollEnabled = false
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Text(text = "✍️ Autores Mais Lidos", style = MaterialTheme.typography.titleMedium)
//                Chart(
//                    chart = com.patrykandpatrick.vico.compose.chart.column.columnChart(),
//                    model = autoresChart.getModel(),
//                    modifier = Modifier.height(200.dp),
//                    isHorizontalScrollEnabled = false
//                )
//            }
//        }
//    }
//}
