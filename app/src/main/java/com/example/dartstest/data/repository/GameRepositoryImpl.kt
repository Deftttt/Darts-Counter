package com.example.dartstest.data.repository

import android.util.Log
import com.example.dartstest.data.model.Game
import com.example.dartstest.util.Resource
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : GameRepository {


/*    override suspend fun addGame(game: Game): Resource<String> {
        return try {
            val gamesCollection = firestore.collection("games")
            val documentReference = gamesCollection.add(game).await()
            val gameId = documentReference.id
            Resource.Success(gameId)
        } catch (e: Exception) {
            Resource.Error("Failed to add game: ${e.message}")
        }
    }*/
    override suspend fun addGame(game: Game): Resource<String> {
        return try {
            val gamesCollection = firestore.collection("games")
            val documentReference = gamesCollection.document()
            val gameId = documentReference.id
            documentReference.set(game.copy(gameId = gameId))
            Resource.Success(gameId)
        } catch (e: Exception) {
            Resource.Error("Failed to add game: ${e.message}")
        }
    }

    override suspend fun updateGame(game: Game): Resource<String> {
        return try {
            firestore.collection("games").document(game.gameId!!).set(game)
            Resource.Success("XD")
        } catch (e: Exception) {
            Resource.Error("Failed to add game: ${e.message}")
        }
    }

    override fun observeGame(gameId: String, callback: (Game?, Exception?) -> Unit) {
        val docRef = firestore.collection("games").document(gameId)

        docRef.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                callback(null, exception)
                return@addSnapshotListener
            }

            if (snapshot != null && snapshot.exists()) {
                val game = snapshot.toObject(Game::class.java)
                callback(game, null)
            } else {
                callback(null, null)
            }
        }
    }

/*    override suspend fun updateGame(game: Game) {
        return try {
            val docRef = firestore.collection("games").document(gameId)
            Resource.Success(gameId)
        } catch (e: Exception) {
            Resource.Error("Failed to add game: ${e.message}")
        }
    }*/


}

/*    override suspend fun getGameById(gameId: String): Resource<Game?> {
        return try {
            val docRef = firestore.collection("games").document(gameId.toString())
            val documentSnapshot = Tasks.await(docRef.get())

            if (documentSnapshot.exists()) {
                val game: Game? = documentSnapshot.toObject(Game::class.java)
                Resource.Success(game)
            } else {
                Resource.Error("Failed to get game")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to get game: ${e.message}")
        }
    }*/

/*    override fun observeGame(gameId: String, callback: (DocumentSnapshot?, FirebaseFirestoreException?) -> Unit) {
        val docRef = firestore.collection("games").document(gameId)
        docRef.addSnapshotListener(callback)
    }*/