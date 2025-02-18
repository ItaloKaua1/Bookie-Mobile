package com.example.bookie.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.example.bookie.R
import kotlinx.coroutines.launch

@Composable
fun LayoutVariant(navController: NavController, titulo: String, mostrarAcoes: Boolean = false, content: @Composable () -> Unit) {
    Scaffold(
        topBar = {
            TopBarVariante(navController, titulo, mostrarAcoes)
        },
        bottomBar = {
            BottomBar(navController)
        },
    ) { innerPadding ->
        // Screen content
        Column(modifier = Modifier.padding(innerPadding)) {
            content()
        }
    }
}