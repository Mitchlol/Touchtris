package com.mitchelllustig.touchtris.extensions

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.SquareColorInfo

const val blockSpaceScale = 1.0f + (1.0f/7.0f)

fun Canvas.withTranslate(x: Float, y: Float, block: Canvas.() -> Unit) {
    save()
    translate(x, y)
    block()
    restore()
}

fun Canvas.withRotate(degrees: Float, block: Canvas.() -> Unit) {
    save()
    rotate(degrees)
    block()
    restore()
}
fun Canvas.moveSquare(size: Float, x: Float, y: Float, block: Canvas.() -> Unit){
    save()
    translate(x * size * blockSpaceScale, y * size *blockSpaceScale)
    block()
    restore()
}


fun Canvas.drawTouchtrisSquare(
    size: Float,
    colorInfo: SquareColorInfo,
    glow: Boolean = false,
) {
    if(glow){
        val paint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            color = Color.Transparent.toArgb()
            setShadowLayer(size/4, 0f, 0f, Color.White.toArgb())
        }
        nativeCanvas.drawRect(0f, 0f, size, size, paint)
    }
    if (size < 7) {
        var paint = Paint().apply {
            isAntiAlias = true
            this.color = colorInfo.primaryColor
        }
        drawRect(0f, 0f, size, size, paint)
        return
    }
    val pixelSize = (size / 7)
    var paint = Paint().apply {
        isAntiAlias = true
        this.color = colorInfo.primaryColor
    }
    var paint2 = Paint().apply {
        isAntiAlias = true
        this.color = colorInfo.secondaryColor
    }
    if (colorInfo.dark) {
        // Solid background
        drawRect(0f, 0f, size, size, paint)
        // 4 Pixels of reflection
        drawRect(0f, 0f, pixelSize, pixelSize, paint2)
        drawRect(
            pixelSize,
            pixelSize,
            pixelSize * 2,
            pixelSize * 2,
            paint2
        )
        drawRect(
            pixelSize * 2,
            pixelSize,
            pixelSize * 3,
            pixelSize * 2,
            paint2
        )
        drawRect(
            pixelSize,
            pixelSize * 2,
            pixelSize * 2,
            pixelSize * 3,
            paint2
        )
    } else {
        // Solid background
        drawRect(0f, 0f, size, size, paint)
        // 1 Pixels of reflection
        drawRect(0f, 0f, pixelSize, pixelSize, paint2)
        // Gloss Fill region
        drawRect(
            pixelSize,
            pixelSize,
            size - pixelSize,
            size - pixelSize,
            paint2
        )
    }
}

fun Canvas.drawTouchtrisPiece(
    x: Float,
    y: Float,
    size: Float,
    colorInfo: SquareColorInfo,
    piece: Pieces = Pieces.T,
    orientation: Orientations = Orientations.zero,
    glow: Boolean = false,
    highlightCenter: Boolean = false,
) {
    moveSquare(size, x, y){
        for (position in piece.getSubPositions(orientation)){
            // Pieces are normally constrained to bounds, but ghost piece can overflow, this stops it, but renders the inbounds squares.
            if(y + position.y.toFloat() >= 0){
                moveSquare(size, position.x.toFloat(), position.y.toFloat()) {
                    if(highlightCenter && position.x == 0 && position.y == 0){
                        val highlightColorInfo = colorInfo.copy(dark = !colorInfo.dark)
                        drawTouchtrisSquare(size, highlightColorInfo, glow)
                    }else{
                        drawTouchtrisSquare(size, colorInfo, glow)
                    }
                }
            }
        }
    }
}
