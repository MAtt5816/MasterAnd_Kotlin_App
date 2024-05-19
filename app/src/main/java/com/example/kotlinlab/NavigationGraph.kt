package com.example.kotlinlab

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.kotlinlab.views.GameScreen
import com.example.kotlinlab.views.LoginScreen
import com.example.kotlinlab.views.ScoresScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = "login",
        enterTransition = {
            fadeIn()
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(easing = EaseIn)
            )
        },
        exitTransition = {
            fadeOut()
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(easing = EaseOut)
            )
        }
    ) {
        composable("login") { backStackEntry ->
            val clearForm = backStackEntry.savedStateHandle.get<Boolean>("clearForm") ?: false
            LoginScreen(
                onStartGameClicked = { colorsNumber ->
                    navController.navigate("game/$colorsNumber")
                },
                clearForm = clearForm
            )

            backStackEntry.savedStateHandle["clearForm"] = false
        }
        composable(
            "game/{colorsNumber}",
            arguments = listOf(navArgument("colorsNumber") { type = NavType.IntType })
        ) { backStackEntry ->
            val colorsNumber = backStackEntry.arguments?.getInt("colorsNumber") ?: 0
            GameScreen(
                colorsNumber = colorsNumber,
                onShowScoresClicked = { points ->
                    navController.navigate("scores/$points/$colorsNumber")
                },
                onLogoutClicked = {
                    navController.getBackStackEntry("login").savedStateHandle["clearForm"] = true
                    navController.popBackStack("login", false)
                }
            )
        }
        composable(
            "scores/{points}/{colorsNumber}",
            arguments = listOf(
                navArgument("points") { type = NavType.IntType },
                navArgument("colorsNumber") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val points = backStackEntry.arguments?.getInt("points") ?: 0
            ScoresScreen(
                score = points,
                onRestartGameClicked = {
                    navController.navigate("game/${backStackEntry.arguments?.getInt("colorsNumber")}")
                },
                onLogoutClicked = {
                    navController.getBackStackEntry("login").savedStateHandle["clearForm"] = true
                    navController.popBackStack("login", false)
                }
            )
        }
    }
}
