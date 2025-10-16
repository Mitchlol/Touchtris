package com.mitchelllustig.touchtris.ui.screens.appmenu

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import com.mitchelllustig.touchtris.ui.widgets.Background
import com.mitchelllustig.touchtris.ui.widgets.HapticIconButton
import com.mitchelllustig.touchtris.ui.widgets.Logo
import com.mitchelllustig.touchtris.ui.widgets.ShadowIcon
import kotlinx.serialization.Serializable

@Serializable
object AppMenu

@Composable
fun AppMenuScreen(
    viewModel: AppMenuViewModel
) {

    AppMenuScreenContent(
        hapticEnabled = viewModel.hapticEnabled.collectAsState(),
        zeroPosition = viewModel.zeroPosition.collectAsState(),
        displayGrid = viewModel.displayGrid.collectAsState(),
        soundsEnabled = viewModel.soundsEnabled.collectAsState(),
        highlightCenter = viewModel.highlightCenter.collectAsState(),
        onHapticEnabledChanged = viewModel::onHapticEnabledChanged,
        onZeroPositionChanged = viewModel::onZeroPositionChanged,
        onDisplayGridChanged = viewModel::onDisplayGridChanged,
        onSoundsEnabledChanged = viewModel::onSoundsEnabledChanged,
        onHighlightCenterChanged = viewModel::onHighlightCenterChanged,
    )
}

@Composable
fun AppMenuScreenContent(
    hapticEnabled: State<Boolean>,
    zeroPosition: State<Boolean>,
    displayGrid: State<Boolean>,
    soundsEnabled: State<Boolean>,
    highlightCenter: State<Boolean>,
    onHapticEnabledChanged: (Boolean) -> Unit,
    onZeroPositionChanged: (Boolean) -> Unit,
    onDisplayGridChanged: (Boolean) -> Unit,
    onSoundsEnabledChanged: (Boolean) -> Unit,
    onHighlightCenterChanged: (Boolean) -> Unit,
) {

    val configuration = LocalConfiguration.current
    Background()
    if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f),
                contentAlignment = Alignment.Center,
            ) {
                Logo(
                    text = "Options",
                    textSize = 55.dp,
                )
            }

            SettingsList(
                hapticEnabled = hapticEnabled,
                zeroPosition = zeroPosition,
                displayGrid = displayGrid,
                soundsEnabled = soundsEnabled,
                highlightCenter = highlightCenter,
                onHapticEnabledChanged = onHapticEnabledChanged,
                onZeroPositionChanged = onZeroPositionChanged,
                onDisplayGridChanged = onDisplayGridChanged,
                onSoundsEnabledChanged = onSoundsEnabledChanged,
                onHighlightCenterChanged = onHighlightCenterChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.75f),
            )
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.Center,
            ) {
                Logo(
                    text = "Options",
                    textSize = 55.dp,
                )
            }

            SettingsList(
                hapticEnabled = hapticEnabled,
                zeroPosition = zeroPosition,
                displayGrid = displayGrid,
                soundsEnabled = soundsEnabled,
                highlightCenter = highlightCenter,
                onHapticEnabledChanged = onHapticEnabledChanged,
                onZeroPositionChanged = onZeroPositionChanged,
                onDisplayGridChanged = onDisplayGridChanged,
                onSoundsEnabledChanged = onSoundsEnabledChanged,
                onHighlightCenterChanged = onHighlightCenterChanged,
                modifier = Modifier
                    .weight(0.5f)
                    .fillMaxHeight(),
            )
        }
    }
}

@Composable
private fun SettingsList(
    hapticEnabled: State<Boolean>,
    zeroPosition: State<Boolean>,
    displayGrid: State<Boolean>,
    soundsEnabled: State<Boolean>,
    highlightCenter: State<Boolean>,
    onHapticEnabledChanged: (Boolean) -> Unit,
    onZeroPositionChanged: (Boolean) -> Unit,
    onDisplayGridChanged: (Boolean) -> Unit,
    onSoundsEnabledChanged: (Boolean) -> Unit,
    onHighlightCenterChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val booleanValues = remember { listOf("Off", "On") }
        val itemHeight = 50.dp
        val listState = rememberLazyListState()
        val settingsItems = listOf(
            Triple("Haptic Feedback", hapticEnabled, onHapticEnabledChanged),
            Triple("Sound", soundsEnabled, onSoundsEnabledChanged),
            Triple("Right Zero Label", zeroPosition, onZeroPositionChanged),
            Triple("Grid", displayGrid, onDisplayGridChanged),
            Triple("Highlight Center", highlightCenter, onHighlightCenterChanged),
        )
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(vertical = 20.dp)
        ) {
            itemsIndexed(items = settingsItems) { index, (label, state, onStateChanged) ->
                SettingItem(
                    modifier = Modifier.height(itemHeight),
                    label = label,
                    value = state,
                    values = booleanValues,
                    hapticEnabled = hapticEnabled,
                    onValueChange = { newIndex, _ -> onStateChanged(newIndex == 1) }
                )
            }
        }
    }
}

@Composable
private fun <T> SettingItem(
    modifier: Modifier = Modifier,
    hapticEnabled: State<Boolean> = mutableStateOf(false),
    label: String,
    value: State<Boolean>,
    values: List<T>,
    onValueChange: (newIndex: Int, newValue: T) -> Unit,
) {
    ListItem(
        modifier = modifier,
        colors = ListItemDefaults.colors(
            containerColor = Color.Transparent
        ),
        headlineContent = {
            Text(
                text = label,
                style = MaterialTheme.typography.headlineMedium.copy(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                ),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
            )
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentIndex = if(value.value) 1 else 0
                HapticIconButton(
                    hapticEnabled = hapticEnabled,
                    onClick = {
                        val newIndex = currentIndex - 1
                        onValueChange(newIndex, values[newIndex])
                    },
                    enabled = currentIndex > 0,
                ) {
                    ShadowIcon(
                        Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Decrease $label",
                        tint = if (currentIndex > 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(50.dp),
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                }
                Text(
                    text = if (currentIndex in values.indices) values[currentIndex].toString() else "N/A",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    ),
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.width(50.dp),
                    textAlign = TextAlign.Center,
                    fontSize = 20.sp,
                )
                HapticIconButton(
                    hapticEnabled = hapticEnabled,
                    onClick = {
                        val newIndex = currentIndex + 1
                        onValueChange(newIndex, values[newIndex])
                    },
                    enabled = currentIndex < values.size - 1
                ) {
                    ShadowIcon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Increase $label",
                        tint = if (currentIndex < values.size - 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(50.dp),
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(4f, 4f),
                            blurRadius = 8f
                        )
                    )
                }
            }
        }
    )
}

@Composable
private fun AppMenuScreenMock() {
    TouchtrisTheme {
        AppMenuScreenContent(
            hapticEnabled = remember { mutableStateOf(true) },
            zeroPosition = remember { mutableStateOf(false) },
            displayGrid = remember { mutableStateOf(false) },
            soundsEnabled = remember { mutableStateOf(false) },
            highlightCenter = remember { mutableStateOf(false) },
            onHapticEnabledChanged = { },
            onZeroPositionChanged = { },
            onDisplayGridChanged = { },
            onSoundsEnabledChanged = { },
            onHighlightCenterChanged = { },
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=portrait")
@Composable
private fun AppMenuScreenPreviewPortrait() {
    AppMenuScreenMock()
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun AppMenuScreenPreviewLandscape() {
    AppMenuScreenMock()
}
