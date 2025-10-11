package com.mitchelllustig.touchtris.ui.widgets

import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density

@Composable
fun ShadowIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    shadow: Shadow? = null,
) {
    if (shadow != null) {
        val density: Density = LocalDensity.current
        with(density) {
            Icon(
                imageVector = imageVector,
                contentDescription = contentDescription,
                modifier = modifier
                    .offset(shadow.offset.x.toDp(), shadow.offset.y.toDp())
                    .blur(radius = shadow.blurRadius.toDp()),
                tint = shadow.color
            )
        }
    }

    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        modifier = modifier,
        tint = tint
    )
}
