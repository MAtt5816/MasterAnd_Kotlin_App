package com.example.kotlinlab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ScoresScreen(score: Int, onRestartGameClicked: () -> Unit, onLogoutClicked: ()->Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
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

@Preview
@Composable
fun ScoresScreenPreview() {
    ScoresScreen(8, onRestartGameClicked = {}, onLogoutClicked = {})
}