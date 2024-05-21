package com.example.kotlinlab.views

import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.repeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.kotlinlab.AppViewModelProvider
import com.example.kotlinlab.R
import com.example.kotlinlab.ui.theme.KotlinLabTheme
import com.example.kotlinlab.viewmodels.GameViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
private fun CircularButton(
    onClick: () -> Unit,
    color: Color
) {
    val previousColor = remember { mutableStateOf(Color.Transparent) }

    var animateColor by remember { mutableStateOf(false) }
    val animatedColor by animateColorAsState(
        if (animateColor) color else previousColor.value,
        animationSpec = repeatable(iterations = 10, tween(100), repeatMode = RepeatMode.Reverse)
    )

    LaunchedEffect(color) {
        animateColor = true
    }

    Button(
        onClick = onClick,
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor,
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
    colors: List<Color>,
    notFoundColor: Color
) {
    val colorAnimations = remember {
        MutableList(4) { Animatable(notFoundColor) }
    }

    LaunchedEffect(colors) {
        while (true) {
            for (i in 0..3) {
                colorAnimations[i].animateTo(
                    targetValue = colors[i],
                    animationSpec = tween(durationMillis = 500, easing = LinearEasing)
                )
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(5.dp),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SmallCircle(color = colorAnimations[0].value)
            SmallCircle(color = colorAnimations[1].value)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            SmallCircle(color = colorAnimations[2].value)
            SmallCircle(color = colorAnimations[3].value)
        }
    }
}

@Composable
private fun GameRow(
    selectedColors: MutableList<Color?>,
    feedbackColors: MutableList<Color>,
    clickable: Boolean,
    onSelectColorClick: (Int) -> Unit,
    onCheckClick: () -> Unit,
    rowVisible: Boolean,
    notFoundColor: Color
) {
    var animateDp by remember { mutableStateOf(false) }
    val animatedDp by animateDpAsState(
        if (animateDp) 50.dp else 0.dp,
        animationSpec = repeatable(iterations = 1, tween(1000))
    )

    animateDp = clickable

    AnimatedVisibility(
        visible = rowVisible,
        enter = expandVertically(expandFrom = Alignment.Top)
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
                    .size(animatedDp)
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
            FeedbackCircles(colors = feedbackColors, notFoundColor = notFoundColor)
        }
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

private fun selectRandomColors(
    availableColors: MutableList<Color>
): MutableList<Color> {
    return availableColors.shuffled().take(4).toMutableList()
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
fun GameScreen(
    colorsNumber: Int,
    onShowScoresClicked: (Int) -> Unit,
    onLogoutClicked: () -> Unit,
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    playerId: Long
){
    val coroutineScope = rememberCoroutineScope()

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

    val availableColors =
        remember { mutableStateOf(defColorsSet.take(colorsNumber).toMutableList()) }
    val notFoundColor: Color = MaterialTheme.colorScheme.background
    val correctColors = remember { mutableStateOf(selectRandomColors(availableColors.value)) }

    val gameRounds = remember {
        mutableStateListOf(GameRound(notFoundColor))
    }

    val isGameFinished = remember {
        mutableStateOf(false)
    }
    val score = remember { mutableStateOf("0") }

    val rowsVisible = remember { mutableStateListOf(false) }

    LaunchedEffect(score.value) {
        viewModel.points.value = score.value.toInt()
        viewModel.playerId.value = playerId
    }

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
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(gameRounds.toList().size) { rowNumber ->
                    LaunchedEffect(rowNumber) {
                        rowsVisible[rowNumber] = true
                        if (rowNumber == 0)
                            delay(1000L)
                    }

                    val colorChangeable = !gameRounds[rowNumber].isRowDone
                    val clickable =
                        !gameRounds[rowNumber].selectedColors.contains(null) && !gameRounds[rowNumber].isRowDone

                    GameRow(
                        selectedColors = gameRounds[rowNumber].selectedColors,
                        feedbackColors = gameRounds[rowNumber].feedbackColors,
                        clickable = clickable,
                        onSelectColorClick = { buttonNumber ->
                            if (colorChangeable) {
                                gameRounds[rowNumber].selectedColors = selectNextAvailableColor(
                                    availableColors.value,
                                    gameRounds[rowNumber].selectedColors,
                                    buttonNumber
                                )
                            }
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
                            if (gameRounds[rowNumber].feedbackColors.count { it == Color.Red } != 4) {
                                gameRounds.add(GameRound(notFoundColor))
                                rowsVisible.add(false)
                            } else
                                isGameFinished.value = true
                        },
                        rowVisible = rowsVisible[rowNumber],
                        notFoundColor = notFoundColor
                    )
                }
            }

            if (isGameFinished.value) {
                Button(onClick = {
                    coroutineScope.launch {
                        viewModel.saveScore()
                        onShowScoresClicked(score.value.toInt())
                    }
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
        GameScreen(5, onShowScoresClicked = {}, onLogoutClicked = {}, playerId = 0)
    }
}