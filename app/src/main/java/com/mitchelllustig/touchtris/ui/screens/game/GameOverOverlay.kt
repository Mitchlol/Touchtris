package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mitchelllustig.touchtris.ui.widgets.FancyText

@Composable
fun GameOverOverlay(gameState: GameState){
    // Animate opacity
    val alpha by animateFloatAsState(
        targetValue = if (gameState == GameState.GameOver) 1f else 0f,
        animationSpec = if (gameState == GameState.GameOver) tween(durationMillis = 600, easing = FastOutSlowInEasing) else snap(),
        label = "alpha"
    )
    // Animate scale with a bounce
    val scale by animateFloatAsState(
        targetValue = if (gameState == GameState.GameOver) 1f else 0.5f, // start smaller
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ),
        label = "scale"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.alpha = alpha
            }
            .semantics { testTag = if (alpha > 0.01f) "GameOverVisible" else "GameOverHidden" }
    ){
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xAA000000))
        )
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        FancyText(
            text = "Game over",
            textSize = 50.sp,
            modifier = Modifier
                .graphicsLayer {
                    this.alpha = alpha
                    this.scaleX = scale
                    this.scaleY = scale
                }
        )
    }

}

@Preview
@Composable
fun GameOverOverlayPreview(){
    GameOverOverlay(gameState = GameState.GameOver)
}

