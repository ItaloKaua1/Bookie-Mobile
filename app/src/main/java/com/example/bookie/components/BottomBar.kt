package com.example.bookie.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.bookie.models.NavigationItem

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BottomBar() {
    val navigationItems = listOf(
        NavigationItem(
            title = "home",
            icon = Icons.Outlined.Home
        ),
        NavigationItem(
            title = "busca",
            icon = Icons.Outlined.Search
        ),
        NavigationItem(
            title = "criar",
            icon = Icons.Outlined.Add
        ),
        NavigationItem(
            title = "chat",
            icon = Icons.Outlined.Send
        ),
        NavigationItem(
            title = "notificações",
            icon = Icons.Outlined.Notifications
        ),
    )

    BottomAppBar (
        actions = {
            navigationItems.forEachIndexed { index, item ->
                TextButton(
                    onClick = { /* do something */ },
                    colors = ButtonDefaults.buttonColors(contentColor = Color.Gray, containerColor = Color.Transparent)
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            item.icon,
                            contentDescription = item.title
                        )
                        Text(text = item.title)
                    }
                }
            }
        },
    )
}