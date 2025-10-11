package com.mitchelllustig.touchtris.ui.screens.game

import android.annotation.SuppressLint
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import com.mitchelllustig.touchtris.audio.AudioPlayer
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.Positions
import com.mitchelllustig.touchtris.ui.widgets.HapticButton

@SuppressLint("UnrememberedMutableState")
@Composable
private fun OrientationButton(
    modifier: Modifier = Modifier,
    text: String,
    buttonOrientationType: Orientations,
    gameState: GameState,
    piece: Pieces,
    piecePosition: Positions,
    pieceOrientation: Orientations,
    hapticEnabled: Boolean,
    onOrientationChanged: (Orientations) -> Unit,
) {
    val enabled = !(
            gameState != GameState.Piece ||
                    !piece.isValidPosition(piecePosition, buttonOrientationType)
            )
    val selected = pieceOrientation == buttonOrientationType
    val colors = if (selected) {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    } else {
        ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    }
    HapticButton(
        hapticEnabled = mutableStateOf(!selected && hapticEnabled),
        enabled = enabled,
        onClick = {
            if(!selected){
                onOrientationChanged(buttonOrientationType)
            }
        },
        modifier = modifier,
        colors = colors
    ) {
        Text(text)
    }
}

@Composable
fun OrientationInput(
    gameState: GameState,
    piece: Pieces,
    piecePosition: Positions,
    pieceOrientation: Orientations,
    hapticEnabled: Boolean,
    onOrientationChanged: (Orientations) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OrientationButton(
            modifier = Modifier.weight(1f),
            text = "CounterCW",
            buttonOrientationType = Orientations.one,
            gameState = gameState,
            piece = piece,
            piecePosition = piecePosition,
            pieceOrientation = pieceOrientation,
            hapticEnabled = hapticEnabled,
            onOrientationChanged = onOrientationChanged,
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OrientationButton(
                modifier = Modifier.fillMaxWidth(),
                text = "None",
                buttonOrientationType = Orientations.zero,
                gameState = gameState,
                piece = piece,
                piecePosition = piecePosition,
                pieceOrientation = pieceOrientation,
                hapticEnabled = hapticEnabled,
                onOrientationChanged = onOrientationChanged,
            )
            OrientationButton(
                modifier = Modifier.fillMaxWidth(),
                text = "Flip",
                buttonOrientationType = Orientations.two,
                gameState = gameState,
                piece = piece,
                piecePosition = piecePosition,
                pieceOrientation = pieceOrientation,
                hapticEnabled = hapticEnabled,
                onOrientationChanged = onOrientationChanged,
            )
        }
        OrientationButton(
            modifier = Modifier.weight(1f),
            text = "Clockwise",
            buttonOrientationType = Orientations.three,
            gameState = gameState,
            piece = piece,
            piecePosition = piecePosition,
            pieceOrientation = pieceOrientation,
            hapticEnabled = hapticEnabled,
            onOrientationChanged = onOrientationChanged,
        )
    }
}