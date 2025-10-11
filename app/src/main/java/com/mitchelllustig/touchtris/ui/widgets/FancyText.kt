package com.mitchelllustig.touchtris.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import com.mitchelllustig.touchtris.R
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme

@Composable
fun FancyText(
    text: String = "TouchTris",
    textSize: TextUnit = 35.sp,
    textColor: Color = MaterialTheme.colorScheme.primaryContainer,
    borderColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val textSizePx = with(density) { textSize.toPx() }

    val fillPaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        this.textSize = textSizePx
        color = textColor.toArgb()
        typeface = ResourcesCompat.getFont(context, R.font.bungee)
        textAlign = android.graphics.Paint.Align.CENTER
        setShadowLayer(textSizePx/10, textSizePx/10, textSizePx/10, Color.Black.toArgb())
    }
    val outlinePaint = Paint().asFrameworkPaint().apply {
        isAntiAlias = true
        this.textSize = textSizePx
        color = borderColor.toArgb()
        style = android.graphics.Paint.Style.STROKE
        this.strokeWidth = textSizePx/18
        typeface = ResourcesCompat.getFont(context, R.font.bungee)
        textAlign = android.graphics.Paint.Align.CENTER
    }
    val renderedWidthPx = fillPaint.measureText(text)
    var renderedheightPx = fillPaint.fontMetrics.descent - fillPaint.fontMetrics.ascent
    if(renderedheightPx == 0f){
        renderedheightPx = with(density) {textSize.toPx() * 1.32f}
    }

    Canvas(modifier = modifier
        .height(with(density) {renderedheightPx.toDp()} * 0.84f)
        .width(with(density) {renderedWidthPx.toDp()} + 6.dp)
        .semantics { this.contentDescription = "FancyText:$text" }
    ) {


        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawText(
                text,
                size.width/2,
                size.height * 0.8f,
                fillPaint,
            )
            canvas.nativeCanvas.drawText(
                text,
                size.width/2,
                size.height * 0.8f,
                outlinePaint,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FancyTextPreview() {
    TouchtrisTheme {
        FancyText()
    }
}

@Preview(showBackground = true)
@Composable
fun FancyTextPreview2() {
    TouchtrisTheme {
        FancyText("Congratulations, you are a touchtris master!")
    }
}