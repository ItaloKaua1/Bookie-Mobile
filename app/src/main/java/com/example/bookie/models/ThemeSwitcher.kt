import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.bookie.R
import com.example.bookie.models.ThemeViewModel

@Composable
fun ThemeSwitcher(
    themeViewModel: ThemeViewModel = viewModel(),
    content: @Composable () -> Unit
) {
    // Observar o estado do tema
    val isDarkTheme = themeViewModel.isDarkTheme.collectAsState().value

    // Cores do tema claro e escuro
    val darkColorScheme = darkColorScheme(
        primary = colorResource(id = R.color.white),
        background = Color.Black,
        onBackground = Color.White
    )
    val lightColorScheme = lightColorScheme(
        primary = colorResource(id = R.color.black),
        background = Color.White,
        onBackground = Color.Black
    )

    // Controle da barra de status
    val systemUiController = rememberSystemUiController()
    val statusBarColor = if (isDarkTheme) Color.Black else Color.White
    val darkIcons = !isDarkTheme

    systemUiController.setStatusBarColor(
        color = statusBarColor,
        darkIcons = darkIcons
    )

    MaterialTheme(
        colorScheme = if (isDarkTheme) darkColorScheme else lightColorScheme,
        content = content
    )
}
