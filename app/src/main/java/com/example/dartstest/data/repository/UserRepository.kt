package com.example.dartstest.data.repository

import com.example.dartstest.util.Resource

interface UserRepository {
    suspend fun addUserToFirestore(userId: String, email: String, username: String): Resource<Unit>
}