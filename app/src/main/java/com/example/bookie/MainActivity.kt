package com.example.bookie

import android.Manifest
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookie.models.ConfiguracoesViewModel
import com.example.bookie.models.FcmToken
import com.example.bookie.models.ThemeOption
import com.example.bookie.services.BooksRepositorio
import com.example.bookie.services.PostRepository
import com.example.bookie.services.auth.GoogleSignInHelper
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import android.content.Intent
import com.google.firebase.auth.FirebaseUser

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


    private lateinit var googleSignInHelper: GoogleSignInHelper

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Bookie)
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        googleSignInHelper = GoogleSignInHelper(this)

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
            val themeOption = configuracoesViewModel.themeOption.collectAsState().value

            val isDarkTheme = when (themeOption) {
                ThemeOption.DARK -> true
                ThemeOption.LIGHT -> false
                ThemeOption.AUTO -> {
                    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
                    hour < 6 || hour >= 18
                }
            }
            val cores = if (isDarkTheme) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = cores) {
                val primaryColor = MaterialTheme.colorScheme.primaryContainer
                SideEffect {
                    window.statusBarColor = primaryColor.toArgb()
                    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = !isDarkTheme
                }

                val startDestination = intent.getStringExtra("startDestination") ?: "loginScreen"
                NavHost(navController = navController, startDestination = startDestination) {
                    composable("loginScreen") {
                        LoginScreen(
                            navController = navController,
                            onGoogleSignIn = {
                                googleSignInHelper.signIn(
                                    onSuccess = { user: FirebaseUser -> // Especificando o tipo explicitamente
                                        navController.navigate("feedScreen") {
                                            popUpTo("loginScreen") { inclusive = true }
                                        }
                                    },
                                    onError = { exception ->
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Erro no login: ${exception.message}",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                )
                            }
                        )
                    }
                    composable("cadastroScreen1") { CadastroScreen1(navController) }
                    composable("cadastroScreen2") { CadastroScreen2(navController) }
                    composable("cadastroScreen3") { CadastroScreen3(navController) }
                    composable("editarPerfil") { EditarPerfil(navController) }
                    composable("feedScreen") { FeedScreen(navController) }
                    composable("listarLivros") { ListarLivros(navController) }
                    composable("minhaEstante") { MinhaEstante(navController) }
                    composable("telaPerfil") { TelaPerfil(navController) }
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
                            navArgument(name = "id") { type = NavType.StringType }
                        )
                    ) { backstackEntry ->
                        val id = backstackEntry.arguments?.getString("id")
                        if (id != null) {
                            TelaConversa(navController, id)
                        }
                    }

                    composable("descobrirLivro") { DescobrirScreen(navController) }
                    composable("resultadosDescobrir") { ResultadosDescScreen(
                        navController,
                        query = TODO(),
                        context = TODO()
                    ) }
                    composable("criarLista") { CriarListaScreen(navController) }
                    composable(
                        route = "detalhesListas/{id}/{nome}/{descricao}/{quantidadeLivros}",
                        arguments = listOf(
                            navArgument("id") { type = NavType.StringType },
                            navArgument("nome") { type = NavType.StringType },
                            navArgument("descricao") { type = NavType.StringType },
                            navArgument("quantidadeLivros") { type = NavType.IntType }
                        )
                    ) { backStackEntry ->
                        val id = backStackEntry.arguments?.getString("id") ?: "ID Indisponível"
                        val nome = backStackEntry.arguments?.getString("nome") ?: "Nome Indisponível"
                        val descricao = backStackEntry.arguments?.getString("descricao") ?: "Sem descrição"
                        val quantidadeLivros = backStackEntry.arguments?.getInt("quantidadeLivros") ?: 0

                        ThematicListDetailsScreen(navController, nome, descricao, quantidadeLivros, id)
                    }
                    composable(
                        route = "adicionarLivrosScreen/{nome}/{descricao}"
                    ) { backStackEntry ->
                        val nome = backStackEntry.arguments?.getString("nome") ?: ""
                        val descricao = backStackEntry.arguments?.getString("descricao") ?: ""

                        AdicionarLivrosScreen(navController, nome, descricao)
                    }
                    composable("friendsScreen") { FriendsScreen(navController) }
                    composable("friendsSolicitationScreen") { FriendsSolicitationScreen(navController) }
                    composable("clubesScreen"){ ClubesScreen(navController) }
                    composable("createClubScreen"){ CreateClubScreen(navController) }
                    composable("clube/{clubeId}") { backStackEntry ->
                        val clubeId = backStackEntry.arguments?.getString("clubeId")
                        if (clubeId != null) {
                            TelaClubeDetalhes(clubeId, navController)
                        }
                    }
                    composable("selecionarLivroScreen"){SelecionarLivroScreen(navController)}
                    composable("criarTopico/{clubeId}") { backStackEntry ->
                        val clubeId = backStackEntry.arguments?.getString("clubeId")
                        clubeId?.let { CriarTopicoScreen(it, navController) }
                    }
                    composable(
                        route = "disponibilizarParaTrocaScreen/{id}/{estante}",
                        arguments = listOf(
                            navArgument(name = "id") { type = NavType.StringType },
                            navArgument(name = "estante") { type = NavType.BoolType }
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
                            navArgument(name = "id") { type = NavType.StringType }
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
                            navArgument(name = "id") { type = NavType.StringType }
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
                            navArgument(name = "id") { type = NavType.StringType }
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
                        val context = LocalContext.current
                        CreatePostScreen(
                            navController = navController,
                            postRepository = PostRepository(),
                            booksRepositorio = BooksRepositorio(),
                            userRepository = UserRepository(context)
                        )
                    }
                    composable(
                        route = "expandedPost/{postId}",
                        arguments = listOf(navArgument("postId") { type = NavType.StringType })
                    ) { backStackEntry ->
                        val postId = backStackEntry.arguments?.getString("postId") ?: ""
                        val context = LocalContext.current
                        ExpandedPostScreen(postId = postId, navController = navController, userRepository = UserRepository(context))
                    }
                    composable("savedPostsScreen") { SavedPostsScreen(navController) }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            googleSignInHelper.handleSignInResult(data)
        }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }
}