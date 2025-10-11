package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.SquareColorInfo
import com.mitchelllustig.touchtris.data.getColorInfo
import com.mitchelllustig.touchtris.extensions.blockSpaceScale
import com.mitchelllustig.touchtris.extensions.drawTouchtrisPiece
import com.mitchelllustig.touchtris.ui.widgets.FancyText


private fun Color.darken(fraction: Float = 0.2f): Color {
    return Color(
        red = (red * (1 - fraction)).coerceIn(0f, 1f),
        green = (green * (1 - fraction)).coerceIn(0f, 1f),
        blue = (blue * (1 - fraction)).coerceIn(0f, 1f),
        alpha = alpha
    )
}

@Composable
fun NextPieces(
    nextPieces: List<com.mitchelllustig.touchtris.data.Pieces>,
    level: Int,
    heldPiece: com.mitchelllustig.touchtris.data.Pieces?,
    holdLock: Boolean?,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(modifier = Modifier.height(20.dp)){}
        if(holdLock != null){
            FancyText(
                text = "Hold",
                textSize = 20.sp
            )
            Canvas(
                modifier = Modifier
                    .padding(5.dp)
                    .aspectRatio(1f),
            ) {
                drawIntoCanvas { canvas ->
                    val squareSize = (size.width / ((3 * blockSpaceScale) + 1))
                    if(heldPiece != null){
                        canvas.drawTouchtrisPiece(
                            if(heldPiece in arrayOf(Pieces.O, Pieces.I)) 2f else 1.5f,
                            1f,
                            squareSize,
                            SquareColorInfo(
                                getColorInfo(
                                    heldPiece,
                                    level
                                ).primaryColor.darken(if (holdLock) 0.8f else 0f),
                                getColorInfo(
                                    heldPiece,
                                    level
                                ).secondaryColor.darken(if (holdLock) 0.5f else 0f),
                                heldPiece.isDark,
                            ),
                            heldPiece
                        )
                    }
                }
            }
        }
        FancyText(
            text = "Next",
            textSize = 20.sp
        )
        Canvas(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
        ) {
            drawIntoCanvas { canvas ->
                val squareSize = (size.width / ((3 * blockSpaceScale) + 1))
                for ((index, piece) in nextPieces.withIndex()) {
                    canvas.drawTouchtrisPiece(
                        if(piece in arrayOf(Pieces.O, Pieces.I)) 2f else 1.5f,
                        1f + index * 3f,
                        squareSize,
                        SquareColorInfo(
                            getColorInfo(
                                piece,
                                level
                            ).primaryColor.darken((index + 3) / 8f),
                            getColorInfo(
                                piece,
                                level
                            ).secondaryColor.darken((index + 3) / 8f),
                            piece.isDark,
                        ),
                        piece
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewNextPieces() {
    NextPieces(
        modifier = Modifier.width(100.dp),
        level = 13,
        nextPieces = listOf(
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.L,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.O,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.I,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.Z,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.T
        ),
        heldPiece = _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.T,
        holdLock = null
    )
}

@Preview
@Composable
fun PreviewNextPieces2() {
    NextPieces(
        modifier = Modifier.width(100.dp),
        level = 13,
        nextPieces = listOf(
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.L,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.O,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.I,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.Z,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.T
        ),
        heldPiece = null,
        holdLock = false
    )
}

@Preview
@Composable
fun PreviewNextPieces3() {
    NextPieces(
        modifier = Modifier.width(100.dp),
        level = 13,
        nextPieces = listOf(
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.L,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.O,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.I,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.Z,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.T
        ),
        heldPiece = _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.T,
        holdLock = false
    )
}

@Preview
@Composable
fun PreviewNextPieces4() {
    NextPieces(
        modifier = Modifier.width(100.dp),
        level = 13,
        nextPieces = listOf(
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.L,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.O,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.I,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.Z,
            _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.T
        ),
        heldPiece = _root_ide_package_.com.mitchelllustig.touchtris.data.Pieces.I,
        holdLock = true
    )
}
