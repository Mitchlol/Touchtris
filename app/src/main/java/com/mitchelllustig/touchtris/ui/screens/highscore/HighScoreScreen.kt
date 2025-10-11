package com.mitchelllustig.touchtris.ui.screens.highscore

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mitchelllustig.touchtris.database.HighScore
import com.mitchelllustig.touchtris.extensions.asSetter
import com.mitchelllustig.touchtris.ui.widgets.Background
import com.mitchelllustig.touchtris.ui.widgets.FancyText
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asStateFlow
import java.text.NumberFormat
import java.util.Locale
import kotlin.collections.List

@Composable
fun HighScoreScreen(
    viewModel: HighScoreViewModel,
    onNavigateBack: () -> Unit,
) {
    val scores = viewModel.scores.collectAsStateWithLifecycle(emptyList())
    HighScoreScreenContent(
        viewModel.id,
        scores,
        viewModel.name.collectAsState(),
        viewModel.name.asSetter(),
        viewModel.submitting.collectAsState(),
        viewModel::updateScore,
        onNavigateBack,
    )
}

@Composable
fun HighScoreScreenContent(
    id: Long,
    scores: State<List<HighScore>>,
    name: State<String>,
    onNameChanged: (String) -> Unit,
    submitting: State<Boolean>,
    updateScore: (()-> Unit) -> Unit,
    onNavigateBack: () -> Unit,
) {
    val configuration = LocalConfiguration.current
    Background()
    if(configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment  = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleText()
                ScoresList(
                    id,
                    scores,
                    Modifier.weight(1f)
                )
            }
            Column(
                modifier = Modifier.weight(0.5f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                MessageText()
                Form(
                    name = name,
                    onNameChanged = onNameChanged,
                    submitting = submitting,
                    updateScore = updateScore,
                    onNavigateBack = onNavigateBack,
                )
            }
        }
    }else{
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText()
            MessageText()
            ScoresList(
                id,
                scores,
                Modifier.weight(1f)
            )
            Form(
                name = name,
                onNameChanged = onNameChanged,
                submitting = submitting,
                updateScore = updateScore,
                onNavigateBack = onNavigateBack,
            )
        }
    }
}

@Composable
private fun TitleText(){
    FancyText(
        text = "High Score",
        modifier = Modifier.padding(20.dp),
        textSize = 45.sp,
    )
}

@Composable
private fun MessageText(modifier: Modifier = Modifier){
    Column(modifier) {
        FancyText(
            text = "Congratulations, you are",
            modifier = Modifier.padding(top = 20.dp, start = 20.dp, end = 20.dp),
            textSize = 18.sp,
        )
        FancyText(
            text = "a touchtris master!",
            modifier = Modifier.padding(bottom = 20.dp, start = 20.dp, end = 20.dp),
            textSize = 18.sp,
        )
    }
}

@Composable
private fun ScoresList(
    id: Long,
    scores: State<List<HighScore>>,
    modifier: Modifier
){
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        if (scores.value.size == 0) {
            CircularProgressIndicator()
        } else {
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
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
                    val score = scores.value.getOrElse(i) {
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
                    var flashColor by remember { mutableStateOf(id == score.id) }
                    if(id == score.id){
                        LaunchedEffect(Unit) {
                            while (true) {
                                delay(500)
                                flashColor = !flashColor
                            }
                        }
                    }

                    val textColor = if(flashColor){
                        MaterialTheme.colorScheme.tertiaryContainer
                    }else{
                        MaterialTheme.colorScheme.secondaryContainer
                    }
                    val borderColor = if(flashColor){
                        MaterialTheme.colorScheme.onTertiaryContainer
                    }else{
                        MaterialTheme.colorScheme.onSecondaryContainer
                    }
                    Row(modifier = Modifier.fillMaxWidth()){
                        FancyText(
                            text = if(id == score.id) "New Score!" else score.name,
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
}

@Composable
private fun Form(
    name: State<String>,
    onNameChanged: (String) -> Unit,
    submitting: State<Boolean>,
    updateScore: (()-> Unit) -> Unit,
    onNavigateBack: () -> Unit
){
    TextField(
        value = name.value,
        onValueChange = { newValue ->
            if (newValue.length <= 10) {
                onNameChanged(newValue)
            }
        },
        label = { Text("Enter your name") },
        singleLine = true,
        modifier = Modifier.padding(20.dp),
    )
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .testTag("submitButton"),
        enabled = !submitting.value,
        onClick = {
            updateScore(onNavigateBack)
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        if(!submitting.value) {
            Text("Submit")
        }else{
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun HighScoreScreenMock() {
    val scores = remember { mutableStateOf(mutableListOf<HighScore>()) }
    val name = remember { mutableStateOf("") }
    val submitting = remember { mutableStateOf(false) }
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
    scores.value.add(mockScore)
    scores.value.add(mockScore2)
    MaterialTheme {
        HighScoreScreenContent(
            id = 1L,
            scores = scores,
            name = name,
            onNameChanged = {},
            submitting = submitting,
            updateScore = {},
            onNavigateBack = {},
        )
    }
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=portrait")
@Composable
private fun HighScoreScreenPreviewVertical() {
    HighScoreScreenMock()
}

@Preview(showBackground = true, showSystemUi = true, device = "spec:width=411dp,height=891dp,orientation=landscape")
@Composable
private fun HighScoreScreenPreviewHorizontal() {
    HighScoreScreenMock()
}
