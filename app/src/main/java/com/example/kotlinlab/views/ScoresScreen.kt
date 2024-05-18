package com.example.kotlinlab.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlinlab.AppViewModelProvider
import com.example.kotlinlab.viewmodels.ScoresViewModel
import kotlinx.coroutines.launch

data class Score(
    val name: String,
    val points: Int
)

@Composable
fun ScoresScreen(
    score: Int,
    onRestartGameClicked: () -> Unit,
    onLogoutClicked: () -> Unit,
    viewModel: ScoresViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val coroutineScope = rememberCoroutineScope()

    var scores = remember {
        mutableStateListOf<Score>()
    }

    LaunchedEffect(key1 = score) {
        coroutineScope.launch {
            viewModel.showHighscore()
            val playersScores = viewModel.playersWithScore
            for (player in playersScores) {
                scores.add(
                    Score(
                        name = viewModel.getPlayerName(player.playerId),
                        points = viewModel.getPlayerScore(player.scoreId)
                    )
                )
            }

            val sortedScores = scores.sortedBy { it.points }
            scores.clear()
            scores.addAll(sortedScores)
        }
    }

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "Results",
                style = MaterialTheme.typography.displayLarge,
                modifier = Modifier.padding(bottom = 28.dp)
            )
            Text(
                text = "Recent score: $score",
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(bottom = 28.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(scores.toList().size) { rowNumber ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = scores[rowNumber].name,
                            style = MaterialTheme.typography.displayMedium
                        )
                        Text(
                            text = scores[rowNumber].points.toString(),
                            style = MaterialTheme.typography.displayMedium
                        )
                    }
                    Divider(color = Color.Black, thickness = 2.dp)
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = { onRestartGameClicked() },
                    modifier = Modifier.padding(bottom = 18.dp)
                ) {
                    Text("Restart game")
                }
                Button(
                    onClick = { onLogoutClicked() },
                ) {
                    Text("Logout")
                }
            }
        }
    }
}

@Preview
@Composable
fun ScoresScreenPreview() {
    ScoresScreen(8, onRestartGameClicked = {}, onLogoutClicked = {})
}