package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import com.mitchelllustig.touchtris.data.GameBoard
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.Point
import com.mitchelllustig.touchtris.data.SquareColorInfo
import com.mitchelllustig.touchtris.data.getColorInfo
import com.mitchelllustig.touchtris.extensions.blockSpaceScale
import com.mitchelllustig.touchtris.extensions.drawTouchtrisPiece
import com.mitchelllustig.touchtris.extensions.drawTouchtrisSquare
import com.mitchelllustig.touchtris.extensions.moveSquare
import com.mitchelllustig.touchtris.extensions.withTranslate

@Composable
fun LineClearCanvas(
    modifier: Modifier,
    gameState: GameState,
    clearingLines: List<Int>,
    progress: Float
){
    Canvas(
        modifier = modifier,
    ) {
        if(gameState == GameState.LineClear){
            val squareSize = (size.width / ((9 * blockSpaceScale) + 1))
            var blackPaint = Paint().apply {
                this.color = Color.Black
            }
            var whitePaint = Paint().apply {
                this.color = Color.White
                this.alpha = 0.5f
            }
            drawIntoCanvas { canvas ->
                for (lineNumber in clearingLines){
                    canvas.moveSquare(squareSize, 0f, lineNumber.toFloat()){
                        canvas.withTranslate(size.width/2, 0f){
                            canvas.drawRect(0f,0f, (size.width/2) * progress, squareSize, blackPaint)
                            canvas.drawRect((size.width/2) * -progress,0f, 0f, squareSize, blackPaint)
                        }
                    }
                }
                if(clearingLines.size > 3) {
                    // This renders into overflow, this might be bad?
                    if (
                        (progress > 0.1 && progress < 0.15) ||
                        (progress > 0.3 && progress < 0.35) ||
                        (progress > 0.6 && progress < 0.65) ||
                        (progress > 0.9 && progress < 0.95)
                    ) {
                        canvas.withTranslate(-1000f, -1000f) {
                            canvas.drawRect(0f, 0f, 3000f, 1000f, whitePaint)
                        }
                        canvas.withTranslate(-1000f, 0f) {
                            canvas.drawRect(0f, 0f, 1000f, 4000f, whitePaint)
                        }
                        canvas.withTranslate(size.width, 0f) {
                            canvas.drawRect(0f, 0f, 1000f, 4000f, whitePaint)
                        }
                        canvas.withTranslate(0f, size.height) {
                            canvas.drawRect(0f, 0f, size.width, 4000f, whitePaint)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GridCanvas(
    modifier: Modifier,
    gameBoard: GameBoard,
){
    Canvas(
        modifier = modifier,
    ) {
        //const val blockSpaceScale = 1.0f + (1.0f/7.0f)
        val squareSize = (size.width / ((9 * blockSpaceScale) + 1))
        val lineSize = squareSize * (1-blockSpaceScale)
        var gridPaint = Paint().apply {
            this.color = Color(0xFF222222)
        }
        drawIntoCanvas { canvas ->
            for (y in 1 until gameBoard.squares.size) {
                canvas.moveSquare(squareSize, 0f, y.toFloat()) {
                    canvas.drawRect(0f, 0.25f * lineSize, size.width, 0.75f * lineSize, gridPaint)
                }
            }
            for (x in 1 until gameBoard.squares[0].size) {
                canvas.moveSquare(squareSize, x.toFloat(), 0f) {
                    canvas.drawRect(0.25f * lineSize, 0f, 0.75f * lineSize, size.height, gridPaint)
                }
            }
        }

    }
}

@Composable
fun GameBoardCanvas(
    modifier: Modifier,
    gameBoard: GameBoard,
    ghostPosition: Point<Int>?,
    currentPiece: Pieces,
    pieceOrientation: Orientations,
    level: Int,
    gameState: GameState
) {
    Canvas(
        modifier = modifier,
    ) {
        val squareSize = (size.width / ((9 * blockSpaceScale) + 1))
        drawIntoCanvas { canvas ->
            drawRect(Color.Black)
            for (y in 0..19) {
                for (x in 0..9) {
                    canvas.moveSquare(squareSize, x.toFloat(), y.toFloat()) {
                        val squareColorInfo = gameBoard.squares[y][x]
                        if (squareColorInfo != null && squareColorInfo.renderDelay == 0) {
                            canvas.drawTouchtrisSquare(
                                squareSize,
                                gameBoard.squares[y][x]!!
                            )
                        }
                    }
                }
            }
            canvas.drawTouchtrisPiece(
                5f,
                0f,
                squareSize,
                if (gameState == GameState.Prepiece) SquareColorInfo(Color.White, Color.White, true) else getColorInfo(currentPiece, level),
                currentPiece,
                Orientations.zero,
                true,
            )
            if (ghostPosition != null){
                canvas.drawTouchtrisPiece(
                    ghostPosition.x.toFloat(),
                    ghostPosition.y.toFloat(),
                    squareSize,
                    SquareColorInfo(
                        Color.DarkGray,
                        Color.DarkGray,
                        false,
                    ),
                    currentPiece,
                    pieceOrientation
                )
            }
        }
    }
}