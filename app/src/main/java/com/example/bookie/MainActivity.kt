package com.example.bookie

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookie.ui.screens.FeedScreen
import com.example.bookie.ui.screens.ListarLivros
import com.example.bookie.ui.screens.MinhaEstante
import com.example.bookie.ui.screens.TelaLivro
import com.example.bookie.ui.screens.TelaPerfil
import com.example.bookie.ui.theme.BookieTheme
import com.google.firebase.Firebase
import com.google.firebase.messaging.messaging

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
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
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

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Bookie)
        super.onCreate(savedInstanceState)

        askNotificationPermission()

        Firebase.messaging.isAutoInitEnabled = true

        enableEdgeToEdge()
        setContent {
            BookieTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Screen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "feedScreen") {
                    composable(
                        route = "feedScreen"
                    ) {
                        FeedScreen(navController)
                    }
                    composable(
                        route = "listarLivros"
                    ) {
                        ListarLivros(navController)
                    }
                    composable(
                        route = "telaLivro/{id}",
                        arguments = listOf(
                            navArgument(name = "id") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        backstackEntry ->
                        val idLivro = backstackEntry.arguments?.getString("id")
                        if (idLivro != null) {
                            TelaLivro(navController, id = idLivro)
                        }
                    }
                    composable(
                        route = "minhaEstante"
                    ) {
                        MinhaEstante(navController)
                    }
                    composable(
                        route = "telaPerfil"
                    ) {
                        TelaPerfil(navController)
                    }
                }
            }
        }
    }
}
//
//@Composable
//fun Screen(modifier: Modifier = Modifier) {
//    Column (
//        modifier = Modifier
//            .fillMaxSize()
//    ) {
//        Row (
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .background(Color.Yellow),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            NavigationDrawer()
//        }
//
//        // content row
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Green),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Text(text = "Some Other Contents")
//        }
//
//        // bottom row
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp)
//                .background(Color.Yellow),
//            horizontalArrangement = Arrangement.Center,
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            BottomBar()
//        }
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    BookieTheme {
//        Screen()
//    }
//}
