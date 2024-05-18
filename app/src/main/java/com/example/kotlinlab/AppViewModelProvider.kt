package com.example.kotlinlab

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.kotlinlab.viewmodels.GameViewModel
import com.example.kotlinlab.viewmodels.LoginViewModel
import com.example.kotlinlab.viewmodels.ScoresViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(masterAndApplication().container.playersRepository)
        }
        //TODO: //ProfileViewModel(masterAndApplication().container.playersRepository)
        initializer {
            GameViewModel(
                masterAndApplication().container.scoresRepository
            )
        }
        initializer {
            ScoresViewModel(
                masterAndApplication().container.playerScoresRepository,
                masterAndApplication().container.playersRepository,
                masterAndApplication().container.scoresRepository
            )
        }
    }
}

fun CreationExtras.masterAndApplication(): MasterAndApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MasterAndApplication)
