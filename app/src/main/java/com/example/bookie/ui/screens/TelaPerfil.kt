package com.example.bookie.ui.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.components.LayoutVariant
import com.example.bookie.components.MinhasListas
import com.example.bookie.components.MinhasPostagens
import com.example.bookie.models.ImageLinks
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.models.VolumeInfo
import com.example.bookie.services.FeedViewModel
import java.util.Date


@Composable
fun TelaPerfil(navController: NavHostController, feedViewModel: FeedViewModel) {
    var tabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("minhas postagens", "minhas listas")

    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val userName by userRepo.currentUserName.collectAsState(initial = "")

    val posts by feedViewModel.posts.collectAsState()

    LayoutVariant(navController, "Meu perfil", true) {
        Column {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(72.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto do usuário",
                        modifier = Modifier
                            .size(64.dp)
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(40.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(text = "13", style = MaterialTheme.typography.titleMedium)
                            Text(text = "postagens", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            modifier = Modifier.clickable { navController.navigate("minhaEstante") }
                        ) {
                            Text(text = "19", style = MaterialTheme.typography.titleMedium)
                            Text(text = "livros", style = MaterialTheme.typography.bodySmall)
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Text(text = "89", style = MaterialTheme.typography.titleMedium)
                            Text(text = "amigos", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(top = 16.dp).padding(horizontal = 8.dp),
            ) {
                Text(text = userName, style = MaterialTheme.typography.titleMedium)
                Text(text = "sobre", style = MaterialTheme.typography.bodyMedium)
            }

            Column {
                TabRow(selectedTabIndex = tabIndex) {
                    tabs.forEachIndexed { index, title ->
                        Tab(text = { Text(title) },
                            selected = tabIndex == index,
                            onClick = { tabIndex = index }
                        )
                    }
                }
                when (tabIndex) {
                    0 -> MinhasPostagens(posts = posts, userName = userName)
                    1 -> MinhasListas()
                }
            }
        }
    }
}


//@Preview(showBackground = true)
//@Composable
//private fun GreetingPreview() {
//    BookieTheme {
//        TelaPerfil()
//    }
//}
