package com.mitchelllustig.touchtris.ui.screens.game

import android.view.HapticFeedbackConstants
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.mitchelllustig.touchtris.audio.AudioPlayer

@Composable
fun DropButton(
    modifier: Modifier = Modifier,
    gameState: GameState,
    onDrop: () -> Unit,
) {
    val view = LocalView.current
    Button(
        enabled = gameState == GameState.Piece,
        onClick = {
            view.isHapticFeedbackEnabled = true
            view.performHapticFeedback(
                HapticFeedbackConstants.VIRTUAL_KEY,
                HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
            )
            onDrop()
        },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text("Hard Drop")
    }
}