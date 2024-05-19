package com.example.kotlinlab.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.kotlinlab.NavigationGraph

@Composable
fun MainScreen() {
    val navController = rememberNavController ()
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        NavigationGraph(navController)
    }
}
