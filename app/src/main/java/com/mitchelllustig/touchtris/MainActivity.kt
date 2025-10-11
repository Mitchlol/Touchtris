package com.mitchelllustig.touchtris

import android.content.SharedPreferences
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mitchelllustig.touchtris.settings.GameSettings
import com.mitchelllustig.touchtris.ui.screens.appmenu.AppMenu
import com.mitchelllustig.touchtris.ui.screens.appmenu.AppMenuScreen
import com.mitchelllustig.touchtris.ui.screens.appmenu.AppMenuViewModel
import com.mitchelllustig.touchtris.ui.screens.game.GameScreen
import com.mitchelllustig.touchtris.ui.screens.game.GameViewModel
import com.mitchelllustig.touchtris.ui.screens.gamemenu.GameMenu
import com.mitchelllustig.touchtris.ui.screens.gamemenu.GameMenuScreen
import com.mitchelllustig.touchtris.ui.screens.gamemenu.GameMenuViewModel
import com.mitchelllustig.touchtris.ui.screens.highscore.HighScoreScreen
import com.mitchelllustig.touchtris.ui.screens.highscore.HighScoreViewModel
import com.mitchelllustig.touchtris.ui.screens.highscores.HighScoresScreen
import com.mitchelllustig.touchtris.ui.screens.highscores.HighScoresViewModel
import com.mitchelllustig.touchtris.ui.screens.mainmenu.MainMenu
import com.mitchelllustig.touchtris.ui.screens.mainmenu.MainMenuScreen
import com.mitchelllustig.touchtris.ui.screens.mainmenu.MainMenuViewModel
import com.mitchelllustig.touchtris.ui.screens.splash.SplashScreen
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.channels.Channel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var prefs: SharedPreferences

    @Inject
    lateinit var gameSettings: GameSettings

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouchTrisApp()
        }
    }
}

@Composable
fun TouchTrisApp(){
    val navController = rememberNavController()
    TouchtrisTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                navController,
                startDestination = "splash",
                modifier = Modifier.padding(innerPadding)
            ) {
                composable(route = "splash") {
                    SplashScreen(
                        onNavigateToMainMenu = {
                            navController.popBackStack()
                            navController.navigate(route = MainMenu)
                        }
                    )
                }
                composable<MainMenu> { backStackEntry ->
                    MainMenuScreen(
                        viewModel = hiltViewModel<MainMenuViewModel>(),
                        onNavigateToGameMenu = {
                            navController.navigate(route = GameMenu)
                        },
                        onNavigateToContinue = {
                            navController.navigate(route = "game?continue=true")
                        },
                        onNavigateToAppMenu = {
                            navController.navigate(route = AppMenu)
                        },
                        onNavigateToHighScores = {
                            navController.navigate(route = "highScores")
                        }
                    )
                }
                composable<AppMenu> { backStackEntry ->
                    AppMenuScreen(
                        hiltViewModel<AppMenuViewModel>(),
                    )
                }
                composable<GameMenu> { backStackEntry ->
                    GameMenuScreen(
                        hiltViewModel<GameMenuViewModel>(),
                        onNavigateToGame = {
                            navController.popBackStack()
                            navController.navigate(route = "game")
                        }
                    )
                }
                composable(
                    route = "game?continue={continue}",
                    arguments = listOf(navArgument("continue") {
                        type = NavType.BoolType
                        defaultValue = false
                    })
                ) { backStackEntry ->
                    GameScreen(
                        hiltViewModel<GameViewModel>(),
                        onNavigateBack = {
                            navController.popBackStack()
                        },
                        onNavigateToHighScore = {highScoreID, ->
                            navController.navigate(route = "highScore?highScoreID=${highScoreID}")
                        }
                    )
                }

                composable(
                    route = "highScore?highScoreID={highScoreID}",
                    arguments = listOf(
                        navArgument("highScoreID") { type = NavType.LongType }
                    )
                ){
                    HighScoreScreen(hiltViewModel<HighScoreViewModel>()) {
                        navController.popBackStack()
                    }
                }

                composable(
                    route = "highScores",
                ){
                    HighScoresScreen(hiltViewModel<HighScoresViewModel>()) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppPreview() {
    TouchtrisTheme {
        SplashScreen {  }
    }
}


