package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.mitchelllustig.touchtris.ui.widgets.FancyText
import kotlin.math.ceil

@Composable
fun CountdownOverlay(gameState: GameState, progress: Float, modifier: Modifier = Modifier){
    var previousSubProgress by remember { mutableStateOf(progress % (1f/3f)) }
    var reset = false
    if ((progress % (1f/3f)) < previousSubProgress){
        reset = true
    }
    previousSubProgress = progress % (1f/3f)

    // Animate opacity
    val alpha by animateFloatAsState(
        targetValue = if (gameState == GameState.Countdown && !reset) 1f else 0f,
        animationSpec = if (gameState == GameState.Countdown && !reset) tween(durationMillis = 300, easing = FastOutSlowInEasing) else snap(),
        label = "alpha"
    )
    // Animate scale with a bounce
    val scale by animateFloatAsState(
        targetValue = if (gameState == GameState.Countdown && !reset) 1f else 0.5f, // start smaller
        animationSpec = if (gameState == GameState.Countdown && !reset) spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        ) else snap(),
        label = "scale"
    )

    FancyText(
        text = if (gameState == GameState.Countdown) "${4-ceil(progress * 3f).toInt()}" else "", // we tried
        textSize = 50.sp,
        modifier = modifier
            .graphicsLayer {
                this.alpha = alpha
                this.scaleX = scale
                this.scaleY = scale
            }
    )

}

@Preview
@Composable
fun CountdownOverlayPreview(){
    CountdownOverlay(GameState.GameOver, 0.85f)
}

