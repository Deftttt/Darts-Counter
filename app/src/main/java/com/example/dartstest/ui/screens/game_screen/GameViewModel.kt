package com.example.dartstest.ui.screens.game_screen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dartstest.data.model.Game
import com.example.dartstest.data.model.Leg
import com.example.dartstest.data.model.Throw
import com.example.dartstest.data.model.Turn
import com.example.dartstest.data.model.Type
import com.example.dartstest.data.repository.GameRepository
import com.example.dartstest.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _game: MutableStateFlow<Game?> = MutableStateFlow(null)
    val game: StateFlow<Game?> = _game

    private val _scoringType: MutableState<Type> = mutableStateOf(Type.S)
    val scoringType: MutableState<Type> = _scoringType

    fun observeGame(gameId: String) {
        gameRepository.observeGame(gameId) { game, exception ->
            if (exception != null) {
                // Obsługa błędu
                return@observeGame
            }

            if (game != null) {
                Log.d("GAME_DB", "Snapshot received!")
                _game.value = game
            }
        }
    }

    fun updateGame(game: Game){
        viewModelScope.launch {
            val result = gameRepository.updateGame(game)
            if(result is Resource.Success){
                val gameId = result.data.toString()
                Log.d("GAME_DB", game.remainingPoints.toString())
                //onCreateGameClicked(gameId)
            }

        }
    }

    fun processThrow(singleThrow: Throw) {
        _game.value?.let { game ->

            val pointsScored = calculatePointsScored(singleThrow)
            val points = game.remainingPoints!! - pointsScored

            // Skończenie lega
            if (points == 0) {
                finishAndStartNewLeg(singleThrow)
            }
            // Bust
            else if(points <= 1){
                val pointsBeforeBust = game.remainingPoints + addPointsAfterBust(game.getLastLeg()!!.getLastTurn())
                updateGameState(Throw(0, Type.S), pointsBeforeBust, true)
                Log.d("GAME_STATUS", addPointsAfterBust(game.getLastLeg()!!.getLastTurn()).toString())
            }
            // Normalny rzut
            else {
                updateGameState(singleThrow, points)
            }


        }
    }

    private fun calculatePointsScored(singleThrow: Throw): Int {
        val multiplier = when (singleThrow.type) {
            Type.D -> 2
            Type.T -> 3
            else -> 1
        }
        return singleThrow.sector * multiplier
    }

    private fun addPointsAfterBust(turn: Turn): Int{
        if(turn.isTurnOver())
            return 0
        var points = 0
        for(singleThrow in turn.throws){
           points += calculatePointsScored(singleThrow)
        }
        return points
    }




    private fun finishAndStartNewLeg(lastThrow: Throw) {
        _game.value = _game.value?.let { game ->
            val updatedLegs = updateLegs(game.legs, lastThrow) + Leg()
            val isGameFinished = updatedLegs.size > (game.legsToWin ?: 0)

            if (isGameFinished) {
                Log.d("GAME_STATUS", "Game finished!")
                game.copy(
                    legs = updateLegs(game.legs, lastThrow),
                    remainingPoints = game.targetScore,
                    finished = true
                )
            } else {
                game.copy(
                    legs = updatedLegs,
                    remainingPoints = game.targetScore
                )
            }

        }

    }

    private fun updateGameState(singleThrow: Throw, updatedRemainingPoints: Int, busted: Boolean = false) {
        _game.value = _game.value?.copy(
            legs = updateLegs(_game.value?.legs, singleThrow, busted),
            remainingPoints = updatedRemainingPoints
        )
        updateGame(game.value!!)
    }

    private fun updateLegs(legs: List<Leg>?, singleThrow: Throw, busted: Boolean = false): List<Leg> {
        val lastLeg = legs?.lastOrNull()

        if (lastLeg != null) {
            val lastTurn = lastLeg.turns.lastOrNull()

            if (lastTurn != null && !lastTurn.isTurnOver()) {
                val updatedTurns = lastLeg.turns.dropLast(1) + lastTurn.copy(
                    throws = lastTurn.throws + singleThrow
                )
                if(busted){
                    return legs.dropLast(1) + lastLeg.copy(turns = updatedTurns + Turn())
                }
                return legs.dropLast(1) + lastLeg.copy(turns = updatedTurns)
            } else {
                // nowa tura
                val newTurn = Turn(throws = listOf(singleThrow))
                if(busted){
                    return legs.dropLast(1) + lastLeg.copy(turns = lastLeg.turns + newTurn + Turn())
                }
                return legs.dropLast(1) + lastLeg.copy(turns = lastLeg.turns + newTurn)
            }
        } else {
            // pierwszy leg
            val newTurn = Turn(throws = listOf(singleThrow))
            return legs.orEmpty() + Leg(turns = listOf(newTurn))
        }

    }


}

