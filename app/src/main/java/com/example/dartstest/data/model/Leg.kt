package com.example.dartstest.data.model

data class Leg(
    val turns: List<Turn> = emptyList()
){
    fun getLastTurn(): Turn{
        return turns.last()
    }
}


