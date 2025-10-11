package com.mitchelllustig.touchtris.ui.screens.highscores

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.ui.screens.gamemenu.SettingItem
import com.mitchelllustig.touchtris.ui.theme.TouchtrisTheme
import com.mitchelllustig.touchtris.ui.widgets.Background
import com.mitchelllustig.touchtris.ui.widgets.FancyText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import java.text.NumberFormat
import java.util.Locale

@Composable
fun HighScoresScreen(
    viewModel: HighScoresViewModel,
    onNavigateBack: () -> Unit,
) {
    HighScoresScreenContent(
        memoryCountFlow = viewModel.memoryCount,
        nextPieceCountFlow = viewModel.nextPieceCount,
        ghostPieceFlow = viewModel.ghostPiece,
        holdPieceFlow = viewModel.holdPiece,
        randomBagFlow = viewModel.randomBag,
        scoresQueryingFlow = viewModel.scoresQuerying,
        scoresFlow = viewModel.scores
    )
}

@Composable
fun HighScoresScreenContent(
    memoryCountFlow: MutableStateFlow<Int>,
    nextPieceCountFlow: MutableStateFlow<Int>,
    ghostPieceFlow: MutableStateFlow<Int>,
    holdPieceFlow: MutableStateFlow<Int>,
    randomBagFlow: MutableStateFlow<Int>,
    scoresQueryingFlow: MutableStateFlow<Boolean>,
    scoresFlow: Flow<List<HighScore>>,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        val configuration = LocalConfiguration.current
        Background()
        if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically
            ){
                Column(
                    modifier = Modifier.weight(0.5f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FancyText(
                        text = "High Scores",
                        modifier = Modifier.padding(20.dp).weight(0.15f),
                        textSize = 45.sp,
                    )
                    ScoresList(
                        scoresQueryingFlow = scoresQueryingFlow,
                        scoresFlow = scoresFlow,
                        modifier = Modifier.fillMaxWidth().weight(0.25f),
                    )
                }
                GameSettings(
                    memoryCountFlow = memoryCountFlow,
                    nextPieceCountFlow = nextPieceCountFlow,
                    ghostPieceFlow = ghostPieceFlow,
                    holdPieceFlow = holdPieceFlow,
                    randomBagFlow = randomBagFlow,
                    modifier = Modifier.fillMaxWidth().weight(0.5f)
                )
            }
        }else{
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FancyText(
                    text = "High Scores",
                    modifier = Modifier.padding(20.dp).weight(0.15f),
                    textSize = 45.sp,
                )
                ScoresList(
                    scoresQueryingFlow = scoresQueryingFlow,
                    scoresFlow = scoresFlow,
                    modifier = Modifier.fillMaxWidth().weight(0.25f),
                )
                GameSettings(
                    memoryCountFlow = memoryCountFlow,
                    nextPieceCountFlow = nextPieceCountFlow,
                    ghostPieceFlow = ghostPieceFlow,
                    holdPieceFlow = holdPieceFlow,
                    randomBagFlow = randomBagFlow,
                    modifier = Modifier.fillMaxWidth().weight(0.5f)
                )
            }
        }
    }
}

@Composable
fun ScoresList(
        scoresQueryingFlow: MutableStateFlow<Boolean>,
        scoresFlow: Flow<List<HighScore>>,
        modifier: Modifier = Modifier,
    ){
    val scoresQuerying by scoresQueryingFlow.collectAsState(false)
    val scores by scoresFlow.collectAsState(listOf())
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        var listAlpha = 1f
        if (scoresQuerying) {
            CircularProgressIndicator(modifier = Modifier.testTag("listLoadingIndicator"))
            listAlpha = 0f
        }
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).alpha(listAlpha),
            verticalArrangement = Arrangement.Center
        ) {
            Row(modifier = Modifier.fillMaxWidth()){
                FancyText(
                    text = "Name",
                    modifier = Modifier.weight(0.30f),
                    textSize = 25.sp,
                )
                FancyText(
                    text = "Level",
                    modifier = Modifier.weight(0.30f),
                    textSize = 25.sp,
                )
                FancyText(
                    text = "Score",
                    modifier = Modifier.weight(0.40f),
                    textSize = 25.sp,
                )
            }
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .height(2.dp)
                    .fillMaxWidth()
            )
            Box(
                modifier = Modifier
                    .height(5.dp)
                    .fillMaxWidth()
            )
            for (i in 0 until 5){
                val score = scores.getOrElse(i) {
                    HighScore(
                        id = -1L,
                        name = "----------",
                        timestamp = System.currentTimeMillis(),
                        score = -1,
                        level = -1,
                        memoryCount = 1,
                        nextPieceCount = 1,
                        ghostPiece = 1,
                        holdPiece = 1,
                        randomBag = 1
                    )
                }
                val textColor = MaterialTheme.colorScheme.secondaryContainer
                val borderColor = MaterialTheme.colorScheme.onSecondaryContainer
                Row(modifier = Modifier.fillMaxWidth()){
                    FancyText(
                        text = score.name,
                        modifier = Modifier.weight(0.35f),
                        textSize = 18.sp,
                        textColor = textColor,
                        borderColor = borderColor,
                    )
                    FancyText(
                        text = if (score.level == -1) "-" else score.level.toString(),
                        modifier = Modifier.weight(0.20f),
                        textSize = 18.sp,
                        textColor = textColor,
                        borderColor = borderColor,
                    )
                    FancyText(
                        text = if (score.score == -1L) "-" else NumberFormat.getNumberInstance(Locale.getDefault()).format(score.score),
                        modifier = Modifier.weight(0.45f),
                        textSize = 18.sp,
                        textColor = textColor,
                        borderColor = borderColor,
                    )
                }
            }
        }

    }
}

@Composable
fun GameSettings(
    memoryCountFlow: MutableStateFlow<Int>,
    nextPieceCountFlow: MutableStateFlow<Int>,
    ghostPieceFlow: MutableStateFlow<Int>,
    holdPieceFlow: MutableStateFlow<Int>,
    randomBagFlow: MutableStateFlow<Int>,
    modifier: Modifier = Modifier,
){
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
            modifier = Modifier.padding(vertical = 20.dp),
        ) {
            itemsIndexed(items = listOf(
                Triple("Memory Mode", memoryCountFlow, memoryValues),
                Triple("Next Piece", nextPieceCountFlow, nextValues),
                Triple("Ghost", ghostPieceFlow, ghostValues),
                Triple("Hold Piece", holdPieceFlow, holdValues),
                Triple("Bag Random", randomBagFlow, bagValues)
            )) { index, (label, flow, values) ->
                SettingItem(
                    modifier = Modifier.height(itemHeight),
                    label = label,
                    value = flow.collectAsState(),
                    values = values,
                    onValueChange = { index, _ -> flow.value = index }
                )
            }
        }
    }
}

@Composable
private fun HighScoresScreenMock() {
    val scores = mutableListOf<HighScore>()
    val mockScore = HighScore(
        id = 1L,
        name = "Test Name1",
        timestamp = System.currentTimeMillis(),
        score = 34567890,
        level = 255,
        memoryCount = 1,
        nextPieceCount = 1,
        ghostPiece = 1,
        holdPiece = 1,
        randomBag = 1
    )
    val mockScore2 = HighScore(
        id = 2L,
        name = "Josh",
        timestamp = System.currentTimeMillis(),
        score = 567890,
        level = 19,
        memoryCount = 1,
        nextPieceCount = 1,
        ghostPiece = 1,
        holdPiece = 1,
        randomBag = 1
    )
    scores.add(mockScore)
    scores.add(mockScore2)
    TouchtrisTheme {
        HighScoresScreenContent(
            memoryCountFlow = remember { MutableStateFlow(1) },
            nextPieceCountFlow = remember { MutableStateFlow(3) },
            ghostPieceFlow = remember { MutableStateFlow(1) },
            holdPieceFlow = remember { MutableStateFlow(1) },
            randomBagFlow = remember { MutableStateFlow(1) },
            scoresQueryingFlow = remember { MutableStateFlow(false) },
            scoresFlow = flowOf(scores)
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=portrait")
@Composable
private fun HighScoresScreenPreviewPortrait() {
    HighScoresScreenMock()
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun HighScoresScreenPreviewLandscape() {
    HighScoresScreenMock()
}
