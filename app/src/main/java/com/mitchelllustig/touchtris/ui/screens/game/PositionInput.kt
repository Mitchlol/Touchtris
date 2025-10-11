package com.mitchelllustig.touchtris.ui.screens.game

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mitchelllustig.touchtris.audio.AudioPlayer
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.Positions
import com.mitchelllustig.touchtris.ui.widgets.HapticButton

private val positionLabelArray = listOf(
    "Left 5",
    "Left 4",
    "Left 3",
    "Left 2",
    "Left 1",
    "Right 1",
    "Right 2",
    "Right 3",
    "Right 4",
    "Right 5",
)
private val positionLabelArrayWithZero = listOf(
    "Left 5",
    "Left 4",
    "Left 3",
    "Left 2",
    "Left 1",
    "Right 0",
    "Right 1",
    "Right 2",
    "Right 3",
    "Right 4",
)

@SuppressLint("UnrememberedMutableState")
@Composable
private fun PositionButton(
    modifier: Modifier = Modifier,
    text: String,
    buttonPositionType: Positions,
    gameState: GameState,
    piece: Pieces,
    piecePosition: Positions,
    pieceOrientation: Orientations,
    hapticEnabled: Boolean,
    onStateChanged: (Positions) -> Unit,
) {
    val enabled = !(
            gameState != GameState.Piece ||
                    !piece.isValidPosition(buttonPositionType, pieceOrientation)
            )
    val selected = piecePosition == buttonPositionType
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
        modifier = modifier,
        enabled = enabled,
        onClick = {
            if (!selected){
                onStateChanged(buttonPositionType)
            }
        },
        colors = colors
    ) {
        Text(text)
    }
}

@Composable
fun PositionInput(
    gameState: GameState,
    piece: Pieces,
    piecePosition: Positions,
    pieceOrientation: Orientations,
    hapticEnabled: Boolean,
    zeroPosition: Boolean,
    onPositionChanged: (Positions) -> Unit,
) {
    DiagonalStacks(
        angle = 60f,
        xBorderOverflow = 7.dp,
        yBorderOverflow = 7.dp,
        modifier = Modifier.fillMaxWidth().fillMaxHeight()
    ) {
        for (movement in Positions.entries) {
            PositionButton(
                modifier = Modifier.width(95.dp),
                text = if (zeroPosition) positionLabelArrayWithZero[movement.ordinal] else positionLabelArray[movement.ordinal],
                buttonPositionType = movement,
                gameState = gameState,
                piece = piece,
                piecePosition = piecePosition,
                pieceOrientation = pieceOrientation,
                hapticEnabled = hapticEnabled,
                onStateChanged = onPositionChanged,
            )
        }
    }
}