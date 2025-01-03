package com.example.bookie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookie.components.NavigationDrawer
import com.example.bookie.ui.theme.BookieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Bookie)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            BookieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Screen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    NavigationDrawer()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookieTheme {
        Screen()
    }
}
