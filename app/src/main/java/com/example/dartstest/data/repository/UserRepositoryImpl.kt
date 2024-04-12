package com.example.dartstest.data.repository

import com.example.dartstest.data.model.User
import com.example.dartstest.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun addUserToFirestore(userId: String, email: String, username: String): Resource<Unit> {
        return try {
            val user = User(email = email, username = username)
            firestore.collection("users").document(userId).set(user).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message.toString())
        }
    }
}