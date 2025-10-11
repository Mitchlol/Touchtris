package com.mitchelllustig.touchtris.ui.screens.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mitchelllustig.touchtris.ui.widgets.Background
import com.mitchelllustig.touchtris.ui.widgets.Logo
import kotlinx.coroutines.delay
import kotlinx.serialization.Serializable

@Composable
fun SplashScreen(
    onNavigateToMainMenu: () -> Unit,
) {
    LaunchedEffect(key1 = true) {
        delay(1000L)
        onNavigateToMainMenu()
    }
    Background()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Logo(
            textSize = 50.dp,
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=portrait")
@Composable
private fun SplashScreenPreviewPortrait() {
    SplashScreen {}
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun SplashScreenPreviewLandscape() {
    SplashScreen {}
}
