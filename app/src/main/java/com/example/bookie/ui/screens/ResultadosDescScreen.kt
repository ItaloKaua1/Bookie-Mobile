package com.example.bookie.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.bookie.components.LayoutVariant

@Composable
fun ResultadosDescScreen(navController: NavController){

    LayoutVariant(navController, "Descobrir", false) {

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                "Resultados Aqui"
            )
        }
    }
}