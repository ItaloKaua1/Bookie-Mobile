package com.example.bookie.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.bookie.R
import com.example.bookie.ui.theme.BookieTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarVariante(navController: NavController, titulo: String, mostrarAcoes: Boolean = false) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                TextButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        modifier = Modifier.size(22.dp),
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "voltar",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Text(text = titulo, style = MaterialTheme.typography.titleMedium)
            }
        },
        actions = {
            if (mostrarAcoes) {
                IconButton(onClick = {navController.navigate("editarPerfil")}) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar Perfil", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = {}) {
                    Icon(
                        modifier = Modifier.size(28.dp).fillMaxWidth().padding(end = 8.dp),
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Compartilar",
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        },
        modifier = Modifier.height(64.dp)
    )
}

//@Preview
//@Composable
//private fun TopBarVariantePreview() {
//    BookieTheme {
//        TopBarVariante("Teste")
//    }
//}