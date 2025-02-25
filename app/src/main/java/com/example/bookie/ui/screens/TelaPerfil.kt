package com.example.bookie.ui.screens


import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.width
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.rememberAsyncImagePainter
import com.example.bookie.UserRepository
import com.example.bookie.components.CardPost
import com.example.bookie.components.LayoutVariant
import com.example.bookie.components.MinhasListas
import com.example.bookie.components.MinhasPostagens
import com.example.bookie.models.ImageLinks
import com.example.bookie.models.Livro
import com.example.bookie.models.Post
import com.example.bookie.models.ThematicList
import com.example.bookie.models.VolumeInfo
import com.example.bookie.services.FeedViewModel
import com.example.bookie.services.FeedViewModelFactory
import com.example.bookie.services.PostRepository
import com.example.bookie.services.SavedPostsRepository


@Composable
fun TelaPerfil(navController: NavHostController) {
    val feedViewModel: FeedViewModel = viewModel(
        factory = FeedViewModelFactory(PostRepository())
    )

    var tabIndex by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("minhas postagens", "minhas listas")
    val post = Post("usuario", "Post de Teste", "Texto do post de teste", 5, 3, 4.5f, Date())
    val livro = Livro("", VolumeInfo(ImageLinks("", ""), "Livro Teste", listOf("Autor Teste"), "Sinopse Teste", 34))
    val post2 = Post("usuario", "Post de Teste", "Texto do post de teste", 5, 3, 4.5f, Date(), livro)
    val thematicList = remember {
        listOf(
            ThematicList("1", "Favoritos", "Livros que eu amei ler!", listOf()),
            ThematicList("2", "Para Ler", "Livros que quero ler em breve", listOf())
        )
    }


    val context = LocalContext.current
    val userRepo = UserRepository(context)
    val userName by userRepo.currentUserName.collectAsState(initial = "")
    val posts by feedViewModel.posts.collectAsState()
    val savedPosts by SavedPostsRepository.getSavedPostsFlow().collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        feedViewModel.fetchPosts()
    }

    val userPhotoUrl by userRepo.currentUserPhotoUrl.collectAsState(initial = null)
    val userBio by userRepo.currentBio.collectAsState(initial = null)
    val userPosts = posts.filter { it.usuario == userName }

    val userRepository = UserRepository(LocalContext.current)
    val currentUserName by userRepository.currentUserName.collectAsState(initial = "")

    LayoutVariant(navController, "Meu perfil", true) {
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(72.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (userPhotoUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(userPhotoUrl),
                        contentDescription = "Foto do usuário",
                        modifier = Modifier.size(64.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AccountCircle,
                        contentDescription = "Foto do usuário",
                        modifier = Modifier.size(64.dp)
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(40.dp)) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = userPosts.size.toString(), style = MaterialTheme.typography.titleMedium)
                        Text(text = "postagens", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.clickable { navController.navigate("minhaEstante") }
                    ) {
                        Text(text = "19", style = MaterialTheme.typography.titleMedium)
                        Text(text = "livros", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "89", style = MaterialTheme.typography.titleMedium)
                        Text(text = "amigos", style = MaterialTheme.typography.bodySmall)
                        
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
                            modifier = Modifier.clickable { navController.navigate("friendsScreen") }
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
                userBio?.let { Text(text = it, style = MaterialTheme.typography.bodyMedium) }

                Column {
                    TabRow(selectedTabIndex = tabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                text = { Text(title) },
                                selected = tabIndex == index,
                                onClick = { tabIndex = index }
                            )
                        }
                    }
                    when (tabIndex) {
                        0 -> {
                            if (userPosts.isEmpty()) {
                                Text(
                                    text = "Sem postagens ainda",
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                LazyColumn(
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxSize().padding(16.dp)
                                ) {
                                    items(posts) { post ->
                                        val isSaved = savedPosts.any { it.id == post.id }
                                        val isOwner = post.usuario == currentUserName
                                        CardPost(
                                            post = post,
                                            isSaved = isSaved,
                                            onClick = { navController.navigate("expandedPost/${post.id}") },
                                            onSaveClick = {
                                                if (isSaved) {
                                                    SavedPostsRepository.unsavePost(post)
                                                } else {
                                                    SavedPostsRepository.savePost(post)
                                                }
                                            },
                                            isOwner = isOwner,
                                            onDelete = if (isOwner) {
                                                {
                                                    PostRepository().deletePost(post,
                                                        onSuccess = {
                                                            Log.d("FeedScreen", "Post excluído com sucesso")
                                                            feedViewModel.fetchPosts()
                                                        },
                                                        onFailure = { e ->
                                                            Log.e("FeedScreen", "Erro ao excluir post: ${e.message}")
                                                        }
                                                    )
                                                }
                                            } else null
                                        )
                                    }
                                }
                            }
                        }
                        1 -> MinhasListas()
                    }
                }
                @Composable
                fun MinhasListas(navController: NavHostController, thematicLists: List<ThematicList>) {
                    ThematicListsScreen(navController, thematicLists)
                }

                when (tabIndex) {
                    0 -> MinhasPostagens(posts = posts, userName = userName)
                    1 -> MinhasListas(navController, thematicList)
                }
            }

            if (tabIndex == 1) {
                FloatingActionButton(
                    onClick = { navController.navigate("criarLista") },
                    containerColor = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Criar Lista",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
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
