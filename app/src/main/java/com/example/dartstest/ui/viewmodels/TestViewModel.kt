package com.example.dartstest.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dartstest.data.model.Throw
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class TestViewModel : ViewModel() {
    val state = mutableStateOf<List<Throw>>(emptyList())

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            try {
                val throwsList = getDataFromFirestore()
                state.value = throwsList
            } catch (e: Exception) {
                Log.d("Error", "$e")
            }
        }
    }
}



suspend fun getDataFromFirestore(): List<Throw> {
    val db = Firebase.firestore

    val gameId = "abc123"
    val gameDocumentRef = db.collection("games").document(gameId)
    val throwsList = mutableListOf<Throw>()

    try {
        db.collection("throws").get().await().map { document ->
            val singleThrow = document.toObject(Throw::class.java)
            throwsList.add(singleThrow)
        }
    } catch (e: FirebaseFirestoreException) {
        Log.d("Error", "$e")
    }

    return throwsList
}