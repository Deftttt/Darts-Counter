package com.example.dartstest.ui.screens.login_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dartstest.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()


    fun loginUser(email: String, password: String, onSignInComplete: () -> Unit) = viewModelScope.launch {
        repository.loginUser(email, password).collect{ result ->
            when(result){
                is com.example.dartstest.util.Resource.Success -> {
                    _signInState.send(SignInState(isSuccess = "Sign in success"))
                    Log.d("LOGIN", repository.currentUserId)
                    onSignInComplete()
                }

                is com.example.dartstest.util.Resource.Loading -> {
                    _signInState.send(SignInState(isLoading = true))
                }

                is com.example.dartstest.util.Resource.Error -> {
                    _signInState.send(SignInState(isError = result.message))
                }

            }
        }
    }

}