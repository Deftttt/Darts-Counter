package com.example.dartstest.data.repository

import com.example.dartstest.data.model.Game
import com.example.dartstest.util.Resource
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.flow.Flow

interface GameRepository {
    suspend fun addGame(game: Game): Resource<String>
    suspend fun updateGame(game: Game): Resource<String>

    fun observeGame(gameId: String, callback: (Game?, Exception?) -> Unit)

}