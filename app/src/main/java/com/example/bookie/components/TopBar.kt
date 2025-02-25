package com.example.bookie.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asLiveData
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.ui.theme.BookieTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    onOpenDrawer: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val userName = userRepo.currentUserName.collectAsState(initial = "")
    val userPhotoUrl by userRepo.currentUserPhotoUrl.collectAsState(initial = null)

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (userPhotoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userPhotoUrl),
                        contentDescription = "Foto do usuário",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(48.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto do usuário",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 8.dp)
                    )
                }
                Text("Oi, ${userName.value}!")
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("descobrirLivro") }) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.ic_descobrir_livros_ref),
                    contentDescription = "Descobrir Livros",
                )
            }
            IconButton(onClick = {navController.navigate("clubesScreen")}) {
                Icon(
                    modifier = Modifier
                        .size(48.dp)
                        .padding(2.dp),
                    painter = painterResource(R.drawable.ic_local_library_ref),
                    contentDescription = "Minha Biblioteca",
                )
            }
        },
        modifier = Modifier.height(64.dp)
    )
}

@Preview
@Composable
private fun TopBarPreview() {
    BookieTheme {

        val navController = rememberNavController()
        TopBar(onOpenDrawer = { }, navController = navController/* Provide a mock or suitable preview NavController */)
    }
}
