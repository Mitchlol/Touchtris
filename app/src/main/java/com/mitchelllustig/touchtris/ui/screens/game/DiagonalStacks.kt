package com.mitchelllustig.touchtris.ui.screens.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

private fun rotatedBoundingWidth(width: Int, height: Int, angleDegrees: Double): Double {
    val theta = Math.toRadians(angleDegrees)
    val cos = cos(theta)
    val sin = sin(theta)

    return abs(width * cos) + abs(height * sin)
}

private fun rotatedBoundingHeight(width: Int, height: Int, angleDegrees: Double): Double {
    val theta = Math.toRadians(angleDegrees)
    val cos = cos(theta)
    val sin = sin(theta)

    return abs(width * sin) + abs(height * cos)
}

/*
This is a single use layout and is not fully fleshed out, and has a few "quirks".
Should have exactly 10 elements.
All should be the same size.
Should be relatively small compared to the size of the layout.
 */
@Composable
fun DiagonalStacks(
    angle: Float = 45.0f,
    xBorderOverflow: Dp = 0.dp,
    yBorderOverflow: Dp = 0.dp,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
    ) { measurables, constraints ->
        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)
        val placeables = measurables.map { it.measure(childConstraints) }

        val width = constraints.maxWidth
        val height = constraints.maxHeight

        val xBorderOverflowPx = xBorderOverflow.toPx().toInt()
        val yBorderOverflowPx = yBorderOverflow.toPx().toInt()

        val childWidth = placeables.maxOf{it.width}
        val childHeight = placeables.maxOf{it.height}
        val rotatedChildWidth = rotatedBoundingWidth(childWidth, childHeight, angle.toDouble()).toInt()
        val rotatedChildHeight = rotatedBoundingHeight(childWidth, childHeight, angle.toDouble()).toInt()
        val rottedChildXOffset = -((childWidth/2) - (rotatedChildWidth/2))
        val rottedChildYOffset = -((childHeight/2) - (rotatedChildHeight/2))

        val centerX = width/2

        layout(width, height) {
            placeables.forEachIndexed { index, placeable ->
                if (index < 5) {
                    placeable.placeRelativeWithLayer(
                        x = -xBorderOverflowPx + rottedChildXOffset + (index * ((centerX + (xBorderOverflowPx*2) - rotatedChildWidth) / 4)),
                        y = -yBorderOverflowPx + (rottedChildYOffset + ((4 - index) * ((height + (yBorderOverflowPx *2) - rotatedChildHeight) / 4))),
                        layerBlock = { rotationZ = angle }
                    )
                }else if (index < 10){
                    placeable.placeRelativeWithLayer(
                        x = -xBorderOverflowPx + centerX + rottedChildXOffset +((index-5) * ((centerX + (xBorderOverflowPx*2) - rotatedChildWidth)/4)),
                        y = -yBorderOverflowPx + (rottedChildYOffset + ((index-5) * ((height + (yBorderOverflowPx *2) - rotatedChildHeight)/4))),
                        layerBlock = { rotationZ = 360 - angle }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeirdButtonLayoutPreview() {
    DiagonalStacks(modifier = Modifier
        .background(color = Color.Gray)
        .fillMaxWidth()
        .aspectRatio(1.5f)) {
        for (i in 0..9) {
            Button(onClick = {}) {
                Text("hello $i")
            }
        }
    }
}
@Preview()
@Composable
fun WeirdButtonLayoutPreview2() {
    DiagonalStacks(modifier = Modifier
        .background(color = Color.Gray)
        .fillMaxWidth()
        .aspectRatio(1.5f)
    ) {
        for (i in 0 .. 9) {
            Button(modifier = Modifier.background(color = Color.Magenta), onClick = {}) {
                Text("hello $i")
            }
        }
    }
}
@Preview()
@Composable
fun WeirdButtonLayoutPreview3() {
    DiagonalStacks(
        modifier = Modifier
            .background(color = Color.Gray)
            .fillMaxWidth()
            .aspectRatio(1.5f),
        xBorderOverflow = 7.dp,
        yBorderOverflow = 7.dp,
    ) {
        for (i in 0 .. 9) {
            Button(modifier = Modifier.background(color = Color.Magenta), onClick = {}) {
                Text("hello $i")
            }
        }
    }
}

