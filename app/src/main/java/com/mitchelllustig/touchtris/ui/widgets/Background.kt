package com.mitchelllustig.touchtris.ui.widgets

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.platform.LocalConfiguration
import com.mitchelllustig.touchtris.data.Orientations
import com.mitchelllustig.touchtris.data.Pieces
import com.mitchelllustig.touchtris.data.SquareColorInfo
import com.mitchelllustig.touchtris.extensions.blockSpaceScale
import com.mitchelllustig.touchtris.extensions.drawTouchtrisPiece
import com.mitchelllustig.touchtris.extensions.withRotate
import com.mitchelllustig.touchtris.extensions.withTranslate

@Composable
fun Background(color: Color = MaterialTheme.colorScheme.surfaceContainerLowest){
    val configuration = LocalConfiguration.current
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        drawIntoCanvas { canvas ->
            var width = size.width
            var height = size.height
            var translation = 0f
            var rotation = 0f
            if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
                translation = width
                rotation = 90f
                width = size.height
                height = size.width
            }
            canvas.withTranslate(translation, 0f){
                canvas.withRotate(rotation){
                    val blockSize = width / ((9 * blockSpaceScale) + 1)
                    val rows = height / blockSize.times(blockSpaceScale)

                    val rect = Rect(0f, 0f, width, height)
                    canvas.apply {
                        save()
                        clipRect(rect.left, rect.top, rect.right, rect.bottom)

                        drawRect(color, size = Size(width, height))
                        val lightColorInfo = SquareColorInfo(Color.Blue, Color.White, false)
                        val darkColorInfo = SquareColorInfo(Color.Blue, Color.White, true)
                        for (row in 0.. rows.toInt() step 2){
                            val start = if (row % 4 == 2) -2f else 1f
                            canvas.drawTouchtrisPiece(start, row.toFloat(), blockSize, lightColorInfo, Pieces.T, Orientations.zero)
                            canvas.drawTouchtrisPiece(start + 2, row.toFloat()+1, blockSize, darkColorInfo, Pieces.T, Orientations.two)
                            canvas.drawTouchtrisPiece(start + 4, row.toFloat(), blockSize, lightColorInfo, Pieces.T, Orientations.zero)
                            canvas.drawTouchtrisPiece(start + 6, row.toFloat()+1, blockSize, darkColorInfo, Pieces.T, Orientations.two)
                            canvas.drawTouchtrisPiece(start + 8, row.toFloat(), blockSize, lightColorInfo, Pieces.T, Orientations.zero)
                            canvas.drawTouchtrisPiece(start + 10, row.toFloat()+1, blockSize, darkColorInfo, Pieces.T, Orientations.two)
                        }
                        drawRect(color, alpha = 0.98f, size = Size(width, height))
                        restore()
                    }
                }
            }
        }
    }
}