package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.ui.theme.BookieTheme

@Composable
fun FinalizarPropostaSucesso(navController: NavController) {

    LayoutVariant(navController, "Sucesso ao Oferecer Troca") {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxHeight().padding(16.dp)
        ) {
            Text(text = "Proposta enviada...", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(24.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Icon(
                    Icons.Filled.CheckCircleOutline,
                    contentDescription = "Sucesso",
                    modifier = Modifier.size(40.dp)
                )
                Text(text = "Proposta Enviada com Sucesso", style = MaterialTheme.typography.titleMedium, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 16.dp))
                Text(text = "Por favor, aguarde até que a proposta seja respondida. O andamento pode ser visto na aba de notificações.", style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp))

            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Button(
                    onClick = { navController.navigate("telaNotificacoes") }
                ) {
                    Text(text = "Ir para Notificações")
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//private fun Prev() {
//    BookieTheme {
//        FinalizarPropostaSucesso()
//    }
//}