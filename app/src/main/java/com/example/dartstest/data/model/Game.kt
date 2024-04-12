package com.example.dartstest.data.model

data class Game(
    val gameId: String? = null,
    val playerId: String? = null,
    val targetScore: Int? = null,
    val legsToWin: Int? = null,
    val remainingPoints: Int? = null,
    val legs: List<Leg>? = null,
    val finished: Boolean? = null
){
    fun getLastLeg(): Leg? {
        return legs?.lastOrNull()
    }
}
