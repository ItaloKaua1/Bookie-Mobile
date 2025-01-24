package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bookie.components.LayoutVariant
import com.example.bookie.ui.theme.quaternary

@Composable
fun ResultadosDescScreen(navController: NavController){

    LayoutVariant(navController, "Descobrir", false) {

        Column(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
        ) {
            Text(
                "Melhores resultados pra sua busca: ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.colorScheme.quaternary,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}