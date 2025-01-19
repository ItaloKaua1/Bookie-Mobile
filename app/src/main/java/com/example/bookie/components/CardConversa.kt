package com.example.bookie.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.bookie.R
import com.example.bookie.ui.theme.BookieTheme

@Composable
fun CardConversa() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        AsyncImage(
            model = R.drawable.avatar,
            contentDescription = null,
            modifier = Modifier.height(40.dp).width(40.dp),
        )
        Column(
            modifier = Modifier.weight(1f).padding(start = 8.dp)
        ) {
            Text(text = "Alice", style = MaterialTheme.typography.titleSmall)
            Text(text = "Teste", style = MaterialTheme.typography.bodySmall)
        }
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    Color.Green
                ),
        ) {
            Text(text = "1", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 4.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun P() {
    BookieTheme {
        CardConversa()
    }
}