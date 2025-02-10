package com.example.bookie.components

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.bookie.R
import com.example.bookie.UserRepository
import com.example.bookie.models.Livro
import com.example.bookie.models.NavigationItem
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

private fun logout(navController: NavController, context: Context) {
    FirebaseAuth.getInstance().signOut()
    Toast.makeText(context, "Logout realizado com sucesso!", Toast.LENGTH_SHORT).show()
    navController.navigate("loginScreen")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(navController: NavController, content: @Composable () -> Unit) {
    val context = LocalContext.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navigationItems = listOf(
        NavigationItem(
            title = "meu perfil",
            icon = Icons.Outlined.Person,
            action = { navController.navigate("telaPerfil") }
        ),
        NavigationItem(
            title = "configurações",
            icon = Icons.Outlined.Settings,
            action = { navController.navigate("configuracoesTela") }
        ),
        NavigationItem(
            title = "solicitações de amizade",
            icon = Icons.Outlined.AccountBox,
            action = { navController.navigate("telaPerfil") }
        ),
        NavigationItem(
            title = "relatórios de leitura",
            icon = Icons.Outlined.List,
            action = { navController.navigate("telaPerfil") }
        ),
        NavigationItem(
            title = "salvos",
            icon = Icons.Outlined.Done,
            action = { navController.navigate("telaPerfil") }
        ),
        NavigationItem(
            title = "trocar livros",
            icon = Icons.Outlined.Refresh,
            action = { navController.navigate("telaPerfil") }
        ),
        NavigationItem(
            title = "sair",
            icon = Icons.Outlined.ExitToApp,
            action = { logout(navController, context) },
        ),
    )

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {

                val context = LocalContext.current
                val userRepo = UserRepository(context)
                val userName = userRepo.currentUserName.collectAsState(initial = "")

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 40.dp, start = 16.dp, end = 16.dp, bottom = 16.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp).fillMaxWidth().padding(end = 8.dp),
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Imagem de Usuário"
                    )
                    Column {
                        Text(text = "${userName.value}")
                        Text(text = "@oliviarodri")
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Text(text = "19 livros")
                    Text(text = "89 amigos")
                }
                HorizontalDivider(
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 24.dp)
                )
                navigationItems.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        label = { Text(text = item.title) },
                        selected = false,
                        onClick = {
                            scope.launch {
                                drawerState.close()
                                item.action()
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title
                            )
                        },
                        modifier = Modifier.padding(vertical = 8.dp).height(24.dp)
                    )
                }
            }
        },
    ) {
        Scaffold(
            topBar = {
                TopBar(
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isOpen) close()
                                else open()
                            }
                        }
                    },
                    navController = navController // Passando o NavController aqui
                )
            },
            bottomBar = {
                BottomBar(navController)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { /* Navegar para tela de newpost */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_add),
                        contentDescription = "Adicionar novo post"
                    )
                }
            }
        ) { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                content()
            }
        }
    }
}
