package com.mitchelllustig.touchtris.ui.widgets

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.core.content.res.ResourcesCompat
import com.mitchelllustig.touchtris.R
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme // Added for LogoPreview

@Composable
fun Logo(
    text: String = "Touchtris",
    textSize: Dp = 35.dp,
    textColor: Color = MaterialTheme.colorScheme.primary,
    borderColor: Color = MaterialTheme.colorScheme.onPrimaryContainer
) {
    val context = LocalContext.current
    Canvas(modifier = Modifier
        .height((textSize.value*2).dp)
        .width((text.length * (textSize.value*0.75)).dp)
        .semantics { this.contentDescription = "Logo:$text" }
        /*.padding(all = 10.dp)*/) {
        val fillPaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            this.textSize = textSize.toPx()
            color = textColor.toArgb()
            typeface = ResourcesCompat.getFont(context, R.font.bungee)
            textAlign = android.graphics.Paint.Align.CENTER
            setShadowLayer(textSize.toPx()/10, textSize.toPx()/10, textSize.toPx()/10, Color.Black.toArgb())
        }
        val ourlinePaint = Paint().asFrameworkPaint().apply {
            isAntiAlias = true
            this.textSize = textSize.toPx()
            color = borderColor.toArgb()
            style = android.graphics.Paint.Style.STROKE
            this.strokeWidth = textSize.toPx()/18
            typeface = ResourcesCompat.getFont(context, R.font.bungee)
            textAlign = android.graphics.Paint.Align.CENTER
        }

        val path = Path().apply {

            arcTo(
                Rect(
                    Offset(-size.width * 0.2f,size.height * 0.5f),
                    Offset(size.width * 1.2f, size.height * 4.0f)
                ),
                -180f,
                180f,
                false
            )
        }

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawTextOnPath(
                text,
                path.asAndroidPath(),
                0f,
                0f,
                fillPaint,
            )
            canvas.nativeCanvas.drawTextOnPath(
                text,
                path.asAndroidPath(),
                0f,
                0f,
                ourlinePaint,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LogoPreview() {
    TouchtrisTheme {
        Logo()
    }
}
