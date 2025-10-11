package com.mitchelllustig.touchtris.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun VerticalProgress(
    progress: Float,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    trackColor: Color = MaterialTheme.colorScheme.secondaryContainer,
) {
    val mProgress = progress
    Box(){
        Column(
            modifier = modifier
                .clip(RoundedCornerShape(12.dp))
                .background(trackColor)
                .width(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .weight(max(mProgress, 0.0001f))
                    .fillMaxWidth()
                    .background(color)
                    .testTag("progressFilled")
            )
            Box(
                modifier = Modifier
                    .weight(if ((1f - mProgress) == 0f) 0.0001f else max(1f - mProgress, 0.0001f))
                    .fillMaxWidth()
                    .testTag("progressEmpty")
            )
        }
    }

}