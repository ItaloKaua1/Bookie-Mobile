package com.example.bookie.components

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.bookie.models.NavigationItem
import com.example.bookie.models.ThemeViewModel
import com.example.bookie.ui.screens.ConfiguracoesScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(themeViewModel: ThemeViewModel) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    val scope = rememberCoroutineScope()
    val currentScreen = remember { mutableStateOf(ScreenType.DRAWER) } // Usa uma enum para controle de telas

    val navigationItems = listOf(
        NavigationItem(
            title = "meu perfil",
            icon = Icons.Outlined.Person
        ),
        NavigationItem(
            title = "configurações",
            icon = Icons.Outlined.Settings
        ),
        NavigationItem(
            title = "solicitações de amizade",
            icon = Icons.Outlined.AccountBox
        ),
        NavigationItem(
            title = "relatórios de leitura",
            icon = Icons.Outlined.List
        ),
        NavigationItem(
            title = "salvos",
            icon = Icons.Outlined.Done
        ),
        NavigationItem(
            title = "trocar livros",
            icon = Icons.Outlined.Refresh
        ),
        NavigationItem(
            title = "sair",
            icon = Icons.Outlined.ExitToApp
        ),
    )

    when (currentScreen.value) {
        ScreenType.DRAWER -> {
            // Tela inicial com o Drawer
            ModalNavigationDrawer(
                drawerState = drawerState,
                drawerContent = {
                    ModalDrawerSheet {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(
                                top = 40.dp,
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp
                            )
                        ) {
                            Icon(
                                modifier = Modifier
                                    .size(48.dp)
                                    .fillMaxWidth()
                                    .padding(end = 8.dp),
                                imageVector = Icons.Outlined.AccountCircle,
                                contentDescription = "Imagem de Usuário"
                            )
                            Column {
                                Text(text = "Olivia")
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
                        navigationItems.forEach { item ->
                            NavigationDrawerItem(
                                label = { Text(text = item.title) },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    if (item.title == "configurações") {
                                        currentScreen.value = ScreenType.CONFIGURACOES // Alterna para a tela de configurações
                                    }
                                },
                                icon = {
                                    Icon(
                                        imageVector = item.icon,
                                        contentDescription = item.title
                                    )
                                },
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .height(24.dp)
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
                                    if (drawerState.isOpen) drawerState.close() else drawerState.open()
                                }
                            }
                        )
                    }
                ) { contentPadding ->
                    // Conteúdo principal do Drawer
                    Text(
                        text = "Bem-vindo ao App",
                        modifier = Modifier.padding(contentPadding).padding(16.dp)
                    )
                }
            }
        }

        ScreenType.CONFIGURACOES -> {
            ConfiguracoesScreen(
                themeViewModel = themeViewModel,
                onBackClick = { currentScreen.value = ScreenType.DRAWER }
            )
        }

    }
}

// Enum para controle de telas
enum class ScreenType {
    DRAWER,
    CONFIGURACOES
}
