package com.mitchelllustig.touchtris.ui.screens.mainmenu

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LifecycleResumeEffect
import com.mitchelllustig.touchtris.ui.screens.splash.SplashScreen
import com.mitchelllustig.touchtris.ui.widgets.Background
import com.mitchelllustig.touchtris.ui.widgets.HapticButton
import com.mitchelllustig.touchtris.ui.widgets.Logo
import kotlinx.serialization.Serializable

@Serializable
object MainMenu

@Composable
fun MainMenuScreen(
    viewModel: MainMenuViewModel,
    onNavigateToGameMenu: () -> Unit,
    onNavigateToContinue: () -> Unit,
    onNavigateToAppMenu: () -> Unit,
    onNavigateToHighScores: () -> Unit,
) {

    LifecycleResumeEffect(Unit) {
        viewModel.refreshAppSettings()
        viewModel.continueSettings.loadSettings()
        onPauseOrDispose {
        }
    }

    MainMenuScreenContent(
        hapticEnabledState = viewModel.hapticEnabled.collectAsState(),
        continueEnabledState = viewModel.continueSettings.isContinueAvailable,
        onNavigateToGameMenu = onNavigateToGameMenu,
        onNavigateToContinue = onNavigateToContinue,
        onNavigateToAppMenu = onNavigateToAppMenu,
        onNavigateToHighScores = onNavigateToHighScores
    )
}

// Stateless, previewable content
@Composable
fun MainMenuScreenContent(
    hapticEnabledState: State<Boolean>,
    continueEnabledState: State<Boolean>,
    onNavigateToGameMenu: () -> Unit,
    onNavigateToContinue: () -> Unit,
    onNavigateToAppMenu: () -> Unit,
    onNavigateToHighScores: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    Background()
    if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                Logo(
                    textSize = 50.dp,
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                MainMenuButtonColumn(
                    hapticEnabledState = hapticEnabledState,
                    continueEnabledState = continueEnabledState,
                    onNavigateToGameMenu = onNavigateToGameMenu,
                    onNavigateToContinue = onNavigateToContinue,
                    onNavigateToAppMenu = onNavigateToAppMenu,
                    onNavigateToHighScores = onNavigateToHighScores,
                )
            }
        }
    }else{
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                Logo(
                    textSize = 50.dp,
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.5f),
                contentAlignment = Alignment.Center,
            ) {
                MainMenuButtonColumn(
                    hapticEnabledState = hapticEnabledState,
                    continueEnabledState = continueEnabledState,
                    onNavigateToGameMenu = onNavigateToGameMenu,
                    onNavigateToContinue = onNavigateToContinue,
                    onNavigateToAppMenu = onNavigateToAppMenu,
                    onNavigateToHighScores = onNavigateToHighScores,
                )
            }

        }
    }

}

@Composable
private fun MainMenuButtonColumn(
    hapticEnabledState: State<Boolean>,
    continueEnabledState: State<Boolean>,
    onNavigateToGameMenu: () -> Unit,
    onNavigateToContinue: () -> Unit,
    onNavigateToAppMenu: () -> Unit,
    onNavigateToHighScores: () -> Unit
){

    val continueEnabled by continueEnabledState
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        HapticButton(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(vertical = 8.dp),
            onClick = onNavigateToGameMenu,
            hapticEnabled = hapticEnabledState
        ) {
            Text(
                text = "New Game",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            )
        }
        HapticButton(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(vertical = 8.dp),
            onClick = onNavigateToContinue,
            hapticEnabled = hapticEnabledState,
            enabled = continueEnabled
        ) {
            Text(
                text = "Continue",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            )
        }
        HapticButton(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(vertical = 8.dp),
            onClick = onNavigateToAppMenu,
            hapticEnabled = hapticEnabledState
        ) {
            Text(
                text = "Options",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            )
        }
        HapticButton(
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(vertical = 8.dp),
            onClick = onNavigateToHighScores,
            hapticEnabled = hapticEnabledState
        ) {
            Text(
                text = "High Scores",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize
            )
        }
    }
}


@Composable
private fun MainMenuScreenMock() {
    val dummyHaptic = remember { mutableStateOf(true) }
    val dummyContinue = remember { mutableStateOf(false) }
    MaterialTheme {
        MainMenuScreenContent(
            hapticEnabledState = dummyHaptic,
            continueEnabledState = dummyContinue,
            onNavigateToGameMenu = {},
            onNavigateToContinue = {},
            onNavigateToAppMenu = {},
            onNavigateToHighScores = {}
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=portrait")
@Composable
private fun MainMenuScreenPreviewPortrait() {
    MainMenuScreenMock()
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun MainMenuScreenPreviewLandscape() {
    MainMenuScreenMock()
}
