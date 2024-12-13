package com.example.bookie

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.models.ThemeViewModel
import com.example.bookie.ui.theme.BookieTheme

class MainActivity : ComponentActivity() {
    private val themeViewModel: ThemeViewModel by viewModels()

    @SuppressLint("StateFlowValueCalledInComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Bookie) // Define o tema inicial
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val isDarkTheme = themeViewModel.isDarkTheme.collectAsState()

            BookieTheme(darkTheme = isDarkTheme.value) {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(
                        modifier = Modifier.padding(innerPadding),
                        themeViewModel = themeViewModel
                    )
                }
            }
        }

    }
}

@Composable
fun Screen(modifier: Modifier = Modifier, themeViewModel: ThemeViewModel) {
    NavigationDrawer(themeViewModel = themeViewModel)
}

