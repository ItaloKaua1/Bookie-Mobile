package com.example.bookie

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookie.components.ConfiguracoesViewModel
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.models.FcmToken
import com.example.bookie.services.BooksRepositorio
import com.example.bookie.services.FeedViewModel
import com.example.bookie.services.PostRepository
import com.example.bookie.ui.screens.*
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen1
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen2
import com.example.bookie.ui.screens.CadastroScreens.CadastroScreen3
import com.example.bookie.ui.screens.ConfigsScreen.AnimacaoConfig
import com.example.bookie.ui.screens.ConfigsScreen.EditEmail
import com.example.bookie.ui.screens.ConfigsScreen.EditNome
import com.example.bookie.ui.screens.ConfigsScreen.EditSenha
import com.example.bookie.ui.screens.ConfigsScreen.EditarPerfil
import com.example.bookie.ui.screens.ConfigsScreen.NotificacoesConfig
import com.example.bookie.ui.screens.ConfigsScreen.TemaConfig
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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val configuracoesViewModel: ConfiguracoesViewModel by viewModels()

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


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Bookie)
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        FirebaseMessaging.getInstance().isAutoInitEnabled = true

        val TAG = "token-teste"

        val context = applicationContext

        var db = FirebaseFirestore.getInstance()
        val userRepo = UserRepository(context)

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            GlobalScope.launch {
                val userId = userRepo.currentUserId.first()
                val fcmToken = FcmToken(token)

                db.collection("fcmTokens").document(userId).set(fcmToken).addOnCompleteListener { it ->
                    if (it.isSuccessful) {

                    } else {
                        Toast.makeText(context, "Desculpe, ocorreu um erro ao setar o token", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Log.d(TAG, "token de teste: $token")
//            Toast.makeText(baseContext, "token de teste: $token", Toast.LENGTH_SHORT).show()
        })

        setContent {
            val navController = rememberNavController()
            val temaEscuro = configuracoesViewModel.temaEscuro.collectAsState().value
            val cores = if (temaEscuro) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = cores) {
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
                    composable("editarPerfil") { EditarPerfil(navController) }
                    composable("feedScreen") { FeedScreen(navController, feedViewModel = FeedViewModel(postRepository = PostRepository())) }
                    composable("listarLivros") { ListarLivros(navController) }
                    composable("minhaEstante") { MinhaEstante(navController) }
                    composable("telaPerfil") { TelaPerfil(navController, feedViewModel = FeedViewModel(postRepository = PostRepository()))}
                    composable("telaNotificacoes") { TelaNotificacoes(navController) }
                    composable("configuracoesTela") {
                        ConfiguracoesTela(navController = navController, viewModel = configuracoesViewModel)
                    }
                    composable(
                        route = "telaLivro/{id}/{estante}",
                        arguments = listOf(
                            navArgument(name = "id") { type = NavType.StringType },
                            navArgument(name = "estante") { type = NavType.BoolType }
                        )
                    ) { backstackEntry ->
                        val idLivro = backstackEntry.arguments?.getString("id")
                        val estante = backstackEntry.arguments?.getBoolean("estante")
                        if (idLivro != null) {
                            TelaLivro(navController, id = idLivro, estante = estante)
                        }
                    }
                    composable("descobrirLivro") { DescobrirScreen(navController) }
                    composable("resultadosDescobrir/{query}") { backStackEntry ->
                        val query = backStackEntry.arguments?.getString("query") ?: ""
                        ResultadosDescScreen(navController, query, context)
                    }
                    composable(
                        route = "telaAudioBook/{id}",
                        arguments = listOf(
                            navArgument(name = "id") { type = NavType.StringType }
                        )
                    ) { backstackEntry ->
                        val idLivro = backstackEntry.arguments?.getString("id")
                        if (idLivro != null) {
                            TelaAudioBook(navController, bookId = idLivro)
                        }
                    }
                    composable("telaChat") { TelaChat(navController) }
                    composable(
                        route = "telaConversa/{id}",
                        arguments = listOf(
                            navArgument(name = "id") {
                                type = NavType.StringType
                            },
                        )
                    ) { backstackEntry ->
                        val id = backstackEntry.arguments?.getString("id")
                        if (id != null) {
                            TelaConversa(navController, id)
                        }
                    }
                    composable(
                        route = "disponibilizarParaTrocaScreen/{id}/{estante}",
                        arguments = listOf(
                            navArgument(name = "id") {
                                type = NavType.StringType
                            },
                            navArgument(name = "estante") {
                                type = NavType.BoolType
                            },
                        )
                    ) { backstackEntry ->
                        val id = backstackEntry.arguments?.getString("id")
                        val estante = backstackEntry.arguments?.getBoolean("estante")
                        if (id != null) {
                            DisponibilizarParaTrocaScreen(navController, id, estante)
                        }
                    }
                    composable("listarLivrosTroca") { ListarLivrosTroca(navController) }
                    composable("finalizarPropostaSucesso") { FinalizarPropostaSucesso(navController) }
                    composable(
                        route = "telaLivroTroca/{id}",
                        arguments = listOf(
                            navArgument(name = "id") { type = NavType.StringType },
                        )
                    ) { backstackEntry ->
                        val id = backstackEntry.arguments?.getString("id")
                        if (id != null) {
                            TelaLivroTroca(navController, id)
                        }
                    }
                    composable(
                        route = "finalizarProposta/{id}",
                        arguments = listOf(
                            navArgument(name = "id") { type = NavType.StringType },
                        )
                    ) { backstackEntry ->
                        val id = backstackEntry.arguments?.getString("id")
                        if (id != null) {
                            FinalizarProposta(navController, id)
                        }
                    }
                    composable(
                        route = "visualizarProposta/{id}",
                        arguments = listOf(
                            navArgument(name = "id") { type = NavType.StringType },
                        )
                    ) { backstackEntry ->
                        val id = backstackEntry.arguments?.getString("id")
                        if (id != null) {
                            VisualizarProposta(navController, id)
                        }
                    }
                    composable("editNome") { EditNome(navController) }
                    composable("editEmail") { EditEmail(navController) }
                    composable("editSenha") { EditSenha(navController) }
                    composable("temaConfig") { TemaConfig(navController) }
                    composable("animacaoConfig") { AnimacaoConfig(navController) }
                    composable("notiConfig") { NotificacoesConfig(navController) }
                    composable("createPost") {
                        val context = LocalContext.current // Obtém o contexto atual
                        CreatePostScreen(
                            navController = navController,
                            postRepository = PostRepository(),
                            booksRepositorio = BooksRepositorio(),
                            userRepository = UserRepository(context) // Passa o contexto para o UserRepository
                        )
                    }
                }
            }
        }
    }
}
