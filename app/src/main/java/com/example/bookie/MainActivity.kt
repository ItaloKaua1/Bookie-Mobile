package com.example.bookie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookie.ui.screens.ListarLivros
import com.example.bookie.ui.screens.MinhaEstante
import com.example.bookie.ui.screens.TelaLivro
import com.example.bookie.ui.screens.TelaPerfil
import com.example.bookie.ui.theme.BookieTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Bookie)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BookieTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Screen(
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "listarLivros") {
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
