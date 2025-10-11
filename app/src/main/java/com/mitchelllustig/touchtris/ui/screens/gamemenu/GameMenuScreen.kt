package com.mitchelllustig.touchtris.ui.screens.gamemenu

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.LaunchedEffect
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
import com.mitchelllustig.touchtris.ui.widgets.HapticButton
import com.mitchelllustig.touchtris.ui.widgets.HapticIconButton
import com.mitchelllustig.touchtris.ui.widgets.Logo
import com.mitchelllustig.touchtris.ui.widgets.ShadowIcon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Serializable

@Serializable
object GameMenu

@Composable
fun GameMenuScreen(
    viewModel: GameMenuViewModel,
    onNavigateToGame: () -> Unit,
) {
    LaunchedEffect(Unit) {
        viewModel.startGameEvents.collect { event ->
            if (event) {
                onNavigateToGame()
            }
        }
    }

    GameMenuScreenContent(
        hapticEnabled = viewModel.hapticEnabled.collectAsState(),
        startingLevel = viewModel.startingLevel,
        memoryCount = viewModel.memoryCount,
        nextPieceCount = viewModel.nextPieceCount,
        ghostPiece = viewModel.ghostPiece,
        holdPiece = viewModel.holdPiece,
        randomBag = viewModel.randomBag,
        onStartGameClick = { viewModel.startGame() }
    )
}

@Composable
fun GameMenuScreenContent(
    hapticEnabled: State<Boolean>,
    startingLevel: MutableStateFlow<Int>,
    memoryCount: MutableStateFlow<Int>,
    nextPieceCount: MutableStateFlow<Int>,
    ghostPiece: MutableStateFlow<Int>,
    holdPiece: MutableStateFlow<Int>,
    randomBag: MutableStateFlow<Int>,
    onStartGameClick: () -> Unit
) {
    val configuration = LocalConfiguration.current
    Background()
    Row(
        modifier = Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.25f),
                contentAlignment = Alignment.Center,
            ) {
                Logo(
                    text = "New Game",
                    textSize = 55.dp,
                )
            }
            if(configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(0.5f),
                    contentAlignment = Alignment.Center,
                ) {
                    GameSettings(
                        startingLevel = startingLevel,
                        memoryCount = memoryCount,
                        nextPieceCount = nextPieceCount,
                        ghostPiece = ghostPiece,
                        holdPiece = holdPiece,
                        randomBag = randomBag,
                        modifier = Modifier,
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.15f),
                contentAlignment = Alignment.Center,
            ) {
                HapticButton(
                    hapticEnabled = hapticEnabled,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(horizontal = 16.dp),
                    onClick = onStartGameClick
                ) {
                    Text(
                        text = "Start",
                        fontSize = MaterialTheme.typography.displayMedium.fontSize
                    )
                }
            }
        }
        if(configuration.orientation != Configuration.ORIENTATION_PORTRAIT) {
            GameSettings(
                startingLevel = startingLevel,
                memoryCount = memoryCount,
                nextPieceCount = nextPieceCount,
                ghostPiece = ghostPiece,
                holdPiece = holdPiece,
                randomBag = randomBag,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun GameSettings(
    startingLevel: MutableStateFlow<Int>,
    memoryCount: MutableStateFlow<Int>,
    nextPieceCount: MutableStateFlow<Int>,
    ghostPiece: MutableStateFlow<Int>,
    holdPiece: MutableStateFlow<Int>,
    randomBag: MutableStateFlow<Int>,
    modifier: Modifier = Modifier
){
    val levelValues = remember { (0 .. 255).toList() }
    val ghostValues = remember { listOf("Off", "On") }
    val memoryValues = remember { (0 .. 5).toList() }
    val nextValues = remember { (0 .. 5).toList() }
    val holdValues = remember { listOf("Off", "On") } // This seems to be a duplicate of ghostValues
    val bagValues = remember { listOf("Off", "On") }
    Box(modifier = modifier) {
        val itemHeight = 50.dp
        val listState = rememberLazyListState()
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(vertical = 20.dp)
// Not sure how i feel about this styling
//                .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
//                .drawWithContent {
//                    drawContent()
//                    drawRect(
//                        brush = Brush.verticalGradient(
//                            0f to Color.Transparent,
//                            0.25f to Color.White,
//                            0.75f to Color.White,
//                            1f to Color.Transparent),
//                        blendMode = BlendMode.DstIn
//                    )
//                },
//            contentPadding = PaddingValues(vertical = itemHeight * 2),
        ) {
            itemsIndexed(items = listOf(
                Triple("Starting Speed", startingLevel, levelValues),
                Triple("Memory Mode", memoryCount, memoryValues),
                Triple("Next Piece", nextPieceCount, nextValues),
                Triple("Ghost", ghostPiece, ghostValues),
                Triple("Hold Piece", holdPiece, holdValues),
                Triple("Bag Random", randomBag, bagValues)
            )) { index, (label, stateFlow, values) ->
                SettingItem(
                    modifier = Modifier.height(itemHeight),
                    label = label,
                    value = stateFlow.collectAsState(),
                    values = values,
                    onValueChange = { index, _ -> stateFlow.value = index }
                )
            }
        }
    }
}

@Composable
fun <T> SettingItem(
    modifier: Modifier = Modifier,
    hapticEnabled: State<Boolean> = mutableStateOf(false),
    label: String,
    value: State<Int>,
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
                color = MaterialTheme.colorScheme.secondary,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth()
            )
        },
        trailingContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val currentIndex = value.value
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
                    color = MaterialTheme.colorScheme.secondary,
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
private fun GameMenuScreenMock() {
    TouchtrisTheme {
        GameMenuScreenContent(
            hapticEnabled = remember {mutableStateOf(false)},
            startingLevel = MutableStateFlow(9),
            memoryCount = MutableStateFlow(0),
            nextPieceCount = MutableStateFlow(5),
            ghostPiece = MutableStateFlow(1),
            holdPiece = MutableStateFlow(1),
            randomBag = MutableStateFlow(1),
            onStartGameClick = {}
        )
    }
}
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=portrait")
@Composable
private fun GameMenuScreenPreviewPortrait() {
    GameMenuScreenMock()
}
@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun GameMenuScreenPreviewLandscape() {
    GameMenuScreenMock()
}
