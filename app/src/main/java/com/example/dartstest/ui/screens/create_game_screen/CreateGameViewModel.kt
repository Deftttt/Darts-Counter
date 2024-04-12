package com.example.dartstest.ui.screens.create_game_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dartstest.data.model.Game
import com.example.dartstest.data.model.Leg
import com.example.dartstest.data.repository.AuthRepository
import com.example.dartstest.data.repository.GameRepository
import com.example.dartstest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateGameViewModel @Inject constructor(
    private val gameRepository: GameRepository,
    private val authRepository: AuthRepository
    ): ViewModel() {

    val targetScore: MutableState<Int> = mutableStateOf(0)
    val legsToWin: MutableState<Int> = mutableStateOf(0)

    // Metoda do zatwierdzania utworzenia nowej gry
    fun createGame(onCreateGameClicked: (String) -> Unit) {
        viewModelScope.launch {
            val userId = authRepository.currentUserId
            val game = Game(
                playerId = userId,
                targetScore = targetScore.value,
                legsToWin = legsToWin.value,
                remainingPoints = targetScore.value,
                legs = emptyList(),
                finished = false
            )

            val result = gameRepository.addGame(game)
            if(result is Resource.Success){
                val gameId = result.data.toString()
                Log.d("GAME_DB", gameId)
                onCreateGameClicked(gameId)
            }

        }
    }
}