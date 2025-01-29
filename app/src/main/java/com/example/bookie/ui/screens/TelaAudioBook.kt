package com.example.bookie.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.bookie.AppData
import com.example.bookie.R
import com.example.bookie.models.Livro
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import coil3.compose.rememberConstraintsSizeResolver
import com.example.bookie.components.LayoutVariant
import com.example.bookie.models.AudioBookViewModel

private fun getLivro(id: String): Livro? {
    val appData = AppData.getInstance()
    return appData.getLivroBusca(id)
}

@Composable
fun TelaAudioBook(
    navController: NavController,
    viewModel: AudioBookViewModel = viewModel(),
    bookId: String
) {
    val isPlaying by viewModel.isPlaying.collectAsState()
    val appData = AppData.getInstance()
    val livro = getLivro(bookId)

    //calcular tamanho da tela
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val oneFifthWidth = screenWidth / 5

    LayoutVariant(navController, "Nome do Livro") {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.height(90.dp))

            //capa do livro
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                if (livro?.getCapa().isNullOrEmpty()) {
                    Image(
                        painter = painterResource(id = R.drawable.capa_notfound),//colocar capa do livro que foi clicado
                        contentDescription = "Capa do livro",
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    AsyncImage(
                        model = livro?.getCapa(),
                        contentDescription = "Capa do livro",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //controles de reprodução
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Filled.SkipPrevious,
                        contentDescription = "Voltar"
                    )
                }
                IconButton(onClick = {
                    if (isPlaying) {
                        viewModel.pauseAudio()
                    } else {
                        viewModel.fetchRss("52")
                    }
                },
                    modifier = Modifier
                        .padding(start = 36.dp, end = 36.dp)
                ) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = if (isPlaying) Icons.Filled.Pause
                        else Icons.Filled.PlayArrow,
                        contentDescription = "Reproduzir/Pausar"
                    )
                }
                IconButton(onClick = { }) {
                    Icon(
                        modifier = Modifier.size(40.dp),
                        imageVector = Icons.Filled.SkipNext,
                        contentDescription = "Avançar"
                    )
                }
            }
        }
    }
}
