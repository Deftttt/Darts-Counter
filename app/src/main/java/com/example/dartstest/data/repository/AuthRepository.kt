package com.example.dartstest.data.repository

import com.example.dartstest.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String, username: String): Flow<Resource<AuthResult>>
    val currentUserId: String
}