package com.example.kotlinlab

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun MainScreen() {
    val navController = rememberNavController ()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavigationGraph(navController)
    }
}
