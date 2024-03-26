import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.musicplayerapp.data.bottomNavigationItem.BottomNavigationItem
import com.example.musicplayerapp.data.data_models.Data
import com.example.musicplayerapp.ui.graphs.HomeNavGraph
import com.example.musicplayerapp.ui.music.MusicViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: MusicViewModel,
    modifier: Modifier = Modifier.background(Color.Black),
    isAudioPlaying: Boolean,
    currentPlayingAudio: Data,
    isLoading: Boolean,
    exoPlayer: ExoPlayer,
    onSongItemClick: (Int) -> Unit,
    onStart: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigation(
                navController = navController
            )
        }
    ) { padding ->
        HomeNavGraph(
            navController = navController,
            viewModel = viewModel,
            contentPaddingValues = padding,
            isAudioPlaying = isAudioPlaying,
            currentPlayingAudio = currentPlayingAudio,
            isLoading = isLoading,
            exoPlayer = exoPlayer,
            onSongItemClick = {
                onSongItemClick(it)
            },
            onStart = {
                onStart()
            }
        )
    }
}

@Composable
fun BottomNavigation(
    navController: NavController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    NavigationBar(
        modifier = Modifier.background(
            Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent.copy(alpha = 0.8f),
                    Color.Black
                )
            )
        ),
        containerColor = Color.Transparent
    ) {
        val bottomBarDestination =
            BottomNavigationItem.getItemList().any { it.route == currentDestination?.route }
        if (bottomBarDestination) {
            BottomNavigationItem.getItemList().forEachIndexed { index, item ->
                NavigationBarItem(
                    selected = currentDestination?.hierarchy?.any {
                        it.route == item.route
                    } == true,
                    onClick = {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id)
                            launchSingleTop = true
                        }
                    },
                    icon = { },
                    label = {
                        Text(text = item.title)
                    }
                )
            }
        }
    }
}
