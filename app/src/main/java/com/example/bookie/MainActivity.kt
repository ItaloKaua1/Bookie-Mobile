package com.example.bookie

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookie.components.ConfiguracoesViewModel
import com.example.bookie.ui.screens.*
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen1
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen2
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen3

class MainActivity : ComponentActivity() {
    private val configuracoesViewModel: ConfiguracoesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val temaEscuro = configuracoesViewModel.temaEscuro.collectAsState().value
            val cores = if (temaEscuro) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = cores) {
                // Configura a barra de status com a cor do tema
                val primaryColor = MaterialTheme.colorScheme.primaryContainer
                SideEffect {
                    window.statusBarColor = primaryColor.toArgb()
                    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !temaEscuro
                }

                val startDestination = intent.getStringExtra("startDestination") ?: "loginScreen"
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("loginScreen") { LoginScreen(navController) }
                    composable("cadastroScreen1") { CadastroScreen1(navController) }
                    composable("cadastroScreen2") { CadastroScreen2(navController) }
                    composable("cadastroScreen3") { CadastroScreen3(navController) }
                    composable("feedScreen") { FeedScreen(navController) }
                    composable("listarLivros") { ListarLivros(navController) }
                    composable("minhaEstante") { MinhaEstante(navController) }
                    composable("telaPerfil") { TelaPerfil(navController) }
                    composable("telaNotificacoes") { TelaNotificacoes(navController) }
                    composable("configuracoesTela") {
                        ConfiguracoesTela(navController = navController, viewModel = configuracoesViewModel)
                    }
                    composable("descobrirLivro") { DescobrirScreen(navController) }
                    composable("resultadosDescobrir") { ResultadosDescScreen(navController) }
                }
            }
        }
    }
}
