package com.example.dartstest.data.model

data class Turn(
    val throws: List<Throw> = emptyList()
){

    fun isTurnOver(): Boolean{
        return throws.size == 3
    }
}
