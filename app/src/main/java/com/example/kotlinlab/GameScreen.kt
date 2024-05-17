package com.example.kotlinlab

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.kotlinlab.ui.theme.KotlinLabTheme

@Composable
private fun CircularButton(
    onClick: () -> Unit,
    color: Color
) {
    Button(
        onClick = onClick,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors =
        ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier
            .size(50.dp)
            .background(color = MaterialTheme.colorScheme.background)
    ) {}
}

@Composable
private fun SelectableColorsRow(
    colors: List<Color?>,
    onClick: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        CircularButton(
            color = colors[0] ?: Color.Transparent,
            onClick = { onClick(0) })
        CircularButton(
            color = colors[1] ?: Color.Transparent,
            onClick = { onClick(1) })
        CircularButton(
            color = colors[2] ?: Color.Transparent,
            onClick = { onClick(2) })
        CircularButton(
            color = colors[3] ?: Color.Transparent,
            onClick = { onClick(3) })
    }
}

@Composable
private fun SmallCircle(
    color: Color
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(color)
            .border(2.dp, MaterialTheme.colorScheme.outline, CircleShape)
            .size(20.dp)
    )
}

@Composable
private fun FeedbackCircles(
    colors: List<Color>
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SmallCircle(color = colors[0])
            SmallCircle(color = colors[1])
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SmallCircle(color = colors[2])
            SmallCircle(color = colors[3])
        }
    }
}

@Composable
private fun GameRow(
    selectedColors: MutableList<Color?>,
    feedbackColors: MutableList<Color>,
    clickable: Boolean,
    onSelectColorClick: (Int) -> Unit,
    onCheckClick: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
        SelectableColorsRow(
            colors = selectedColors,
            onClick = onSelectColorClick
        )
        IconButton(
            onClick = onCheckClick,
            enabled = clickable,
            colors = IconButtonDefaults.filledIconButtonColors(),
            modifier = Modifier
                .clip(CircleShape)
                .size(50.dp)
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            Image(
                painter = painterResource(
                    id = R.drawable.ic_baseline_check_24
                ),
                contentDescription = "Check",
                contentScale = ContentScale.Crop
            )
        }
        FeedbackCircles(colors = feedbackColors)
    }
}

private fun selectNextAvailableColor(
    availableColors: List<Color>,
    selectedColors: SnapshotStateList<Color?>,
    buttonNumber: Int
): SnapshotStateList<Color?> {
    var currentIndex = availableColors.indexOf(selectedColors[buttonNumber])

    if (currentIndex < 0)
        currentIndex = 0

    while (selectedColors.contains(availableColors[currentIndex])) {
        if (currentIndex < availableColors.size - 1) {
            currentIndex++
        } else {
            currentIndex = 0
        }
    }
    selectedColors[buttonNumber] = availableColors[currentIndex]
    return selectedColors
}

private fun selectRandomColors(availableColors: MutableList<Color>, colorsNumber: Int): MutableList<Color> {
    return availableColors.shuffled().take(colorsNumber).toMutableList()
}

private fun checkColors(
    selectedColors: SnapshotStateList<Color?>,
    correctColors: MutableList<Color>,
    notFoundColor: Color,
    feedbackColors: SnapshotStateList<Color>
): SnapshotStateList<Color> {
    feedbackColors.clear()
    selectedColors.forEachIndexed { index, selectedColor ->
        if (correctColors.contains(selectedColor)) {
            if (correctColors[index] == selectedColor) {
                feedbackColors.add(Color.Red)
            } else {
                feedbackColors.add(Color.Yellow)
            }
        } else {
            feedbackColors.add(notFoundColor)
        }
    }
    return feedbackColors
}

data class GameRound(
    val notFoundColor: Color,
    var selectedColors: SnapshotStateList<Color?> = mutableStateListOf(null, null, null, null),
    var feedbackColors: SnapshotStateList<Color> = mutableStateListOf(
        notFoundColor,
        notFoundColor,
        notFoundColor,
        notFoundColor
    ),
    var isRowDone: Boolean = false
)

@Composable
fun GameScreen(colorsNumber: Int, onShowScoresClicked: (Int)->Unit, onLogoutClicked: ()->Unit) {
    val defColorsSet: List<Color> = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.Black,
        Color.Cyan,
        Color.Magenta,
        Color.DarkGray,
        Color.LightGray,
        Color.Gray
    )

    val availableColors = remember { mutableStateOf(defColorsSet.take(colorsNumber).toMutableList()) }
    val notFoundColor: Color = MaterialTheme.colorScheme.background
    val correctColors = remember { mutableStateOf(selectRandomColors(availableColors.value, 4)) }

    val gameRounds = remember {
        mutableStateListOf(GameRound(notFoundColor))
    }

    val isGameFinished = remember {
        mutableStateOf(false)
    }
    val score = rememberSaveable { mutableStateOf("0") }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Your score: ${score.value}",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 48.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(gameRounds.toList().size) { rowNumber ->
                    GameRow(
                        selectedColors = gameRounds[rowNumber].selectedColors,
                        feedbackColors = gameRounds[rowNumber].feedbackColors,
                        clickable = !gameRounds[rowNumber].selectedColors.contains(null) && !gameRounds[rowNumber].isRowDone,
                        onSelectColorClick = { buttonNumber ->
                            gameRounds[rowNumber].selectedColors = selectNextAvailableColor(
                                availableColors.value,
                                gameRounds[rowNumber].selectedColors,
                                buttonNumber
                            )
                        },
                        onCheckClick = {
                            gameRounds[rowNumber].feedbackColors =
                                checkColors(
                                    gameRounds[rowNumber].selectedColors,
                                    correctColors.value,
                                    notFoundColor,
                                    gameRounds[rowNumber].feedbackColors
                                )
                            var scoreInt = score.value.toInt()
                            score.value = (++scoreInt).toString()
                            gameRounds[rowNumber].isRowDone = true
                            if (gameRounds[rowNumber].feedbackColors.count { it == Color.Red } != 4)
                                gameRounds.add(GameRound(notFoundColor))
                            else
                                isGameFinished.value = true
                        }
                    )
                }
            }

            if (isGameFinished.value) {
                Button(onClick = {
                    onShowScoresClicked(score.value.toInt())
                }) {
                    Text("High score table")
                }
            }
        }
        Button(
            onClick = {
                onLogoutClicked()
            },
            modifier = Modifier.align(Alignment.BottomCenter)

        ) {
            Text("Logout")
        }
    }
}

@Preview
@Composable
fun GameScreenPreview() {
    KotlinLabTheme {
        GameScreen(5, onShowScoresClicked = {}, onLogoutClicked = {})
    }
}