package com.example.dartstest.di

import com.example.dartstest.data.repository.AuthRepository
import com.example.dartstest.data.repository.AuthRepositoryImpl
import com.example.dartstest.data.repository.GameRepository
import com.example.dartstest.data.repository.GameRepositoryImpl
import com.example.dartstest.data.repository.UserRepository
import com.example.dartstest.data.repository.UserRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Singleton
    @Provides
    fun provideUserRepositoryImpl(firestore: FirebaseFirestore): UserRepository {
        return UserRepositoryImpl(firestore)
    }


    @Singleton
    @Provides
    fun provideAuthRepositoryImpl(firebaseAuth: FirebaseAuth, userRepository: UserRepository): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth, userRepository)
    }

    @Singleton
    @Provides
    fun provideGameRepositoryImpl(firestore: FirebaseFirestore): GameRepository {
        return GameRepositoryImpl(firestore)
    }


}