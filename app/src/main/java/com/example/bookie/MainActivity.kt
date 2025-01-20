package com.example.bookie

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookie.components.ConfiguracoesViewModel
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen1
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen2
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen3
import com.example.bookie.ui.screens.ConfiguracoesTela
import com.example.bookie.ui.screens.DescobrirScreen
import com.example.bookie.ui.screens.FeedScreen
import com.example.bookie.ui.screens.ListarLivros
import com.example.bookie.ui.screens.LoginScreen
import com.example.bookie.ui.screens.MinhaEstante
import com.example.bookie.ui.screens.ResultadosDescScreen
import com.example.bookie.ui.screens.TelaLivro
import com.example.bookie.ui.screens.TelaNotificacoes
import com.example.bookie.ui.screens.TelaPerfil
import com.example.bookie.ui.theme.BookieTheme
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    private val configuracoesViewModel: ConfiguracoesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Bookie)
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        val TAG = "token-teste"

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            Log.d(TAG, "token de teste: $token")
//            Toast.makeText(baseContext, "token de teste: $token", Toast.LENGTH_SHORT).show()
        })

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

                NavHost(navController = navController, startDestination = "loginScreen") {
                    composable("loginScreen") { LoginScreen(navController) }
                    composable("cadastroScreen1") { CadastroScreen1(navController) }
                    composable("cadastroScreen2") { CadastroScreen2(navController) }
                    composable("cadastroScreen3") { CadastroScreen3(navController) }
                    composable("feedScreen") { FeedScreen(navController) }
                    composable("listarLivros") { ListarLivros(navController) }
                    composable(
                        route = "telaLivro/{id}/{estante}",
                        arguments = listOf(
                            navArgument(name = "id") {
                                type = NavType.StringType
                            },
                            navArgument(name = "estante") {
                                type = NavType.BoolType
                            }
                        )
                    ) { backstackEntry ->
                        val idLivro = backstackEntry.arguments?.getString("id")
                        val estante = backstackEntry.arguments?.getBoolean("estante")

                        if (idLivro != null) {
                            TelaLivro(navController, id = idLivro, estante = estante)
                        }
                    }
                    composable("minhaEstante") { MinhaEstante(navController) }
                    composable("telaPerfil") { TelaPerfil(navController) }
                    composable("telaNotificacoes") { TelaNotificacoes(navController) }
                    composable("configuracoesTela") { ConfiguracoesTela(navController = navController, viewModel = configuracoesViewModel) }
                    composable("descobrirLivro") { DescobrirScreen(navController) }
                    composable("resultadosDescobrir") { ResultadosDescScreen(navController) }
                }
            }
        }
    }
}
